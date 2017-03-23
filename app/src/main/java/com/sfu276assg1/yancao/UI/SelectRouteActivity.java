package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sfu276assg1.yancao.carbontracker.BillCollection;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;
import com.sfu276assg1.yancao.carbontracker.Route;
import com.sfu276assg1.yancao.carbontracker.RouteCollection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Double.parseDouble;

/**
 *show saved routes, let customer choose route, edit route, add route or delete route
 */

public class SelectRouteActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView list;
    int mode;
    int edit_journey;
    int edit_journey_postition;
    String type = "";

    int car_array_index, elect_array_index, gas_array_index;

    String[] tooMuchCar = {"Try to take the bike!", "Try to take the public transit", "Try to walk!",
            "Avoid areas with congested traffic!", "Plan out your journey so you don't get lost and waste fuel!",
            "Keep your vehicles well maintained!","Don't accelerate unnecessarily!", "Buy a fuel efficient car!"};

    String[] tooMuchElectricity = {"Turn off the lights when you can!", "Install Compact Fluorescent Bulbs to save energy!", "Wash your clothes with cold water!",
            "Set your refrigerator to the optimal temperature!", "Turn off your lights when you're not using it!", "Wash and dry full loads!", "Cut your heating needs!",
            "Unplug unnecessary appliances!", "Run your dishwasher only with a full load!"};

    String[] tooMuchGas = {"Insulate your house!", "Take quicker showers!", "Close off doors and vents in unused rooms to conserve heat within your home!",
            "Upgrade your heating equipments!", "Don't let the water run!", "Install a programmable thermostat!", "Seal air leaks with caulk!", "Replace Any old Natural Gas Heaters!" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);

        mode = getIntent().getIntExtra(getResources().getString(R.string.TRANS_MODE), 0);
        edit_journey = getIntent().getIntExtra(getResources().getString(R.string.EDIT_JOURNEY), 0);
        edit_journey_postition = getIntent().getIntExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), 0);
        list = (ListView) findViewById(R.id.listView_routeList);
        registerForContextMenu(list);
        setLaunchNewRoute();
        setRouteType();
        selectExistingRoute();
        routeList();

        car_array_index = getLastIndexFromSharedPrefCar();
        elect_array_index = getLastIndexFromSharedPrefElect();
        gas_array_index = getLastIndexFromSharedPrefGas();
    }

    public void setRouteType() {
        switch(mode){
            case 0: type += "Drive";
                break;
            case 1: type += "Public Transit";
                break;
            case 2: type += "Bile/Walk";
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (edit_journey == 0) {
            if (mode == 0) {
                CarbonModel.getInstance().removeLastJourney();
                Intent intent = new Intent(SelectRouteActivity.this, SelectCarActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SelectRouteActivity.this, SelectTransModeActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else if (edit_journey == 1) {
            Intent intent = new Intent(SelectRouteActivity.this, DisplayTableActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(SelectRouteActivity.this, SelectCarActivity.class);
            intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), edit_journey);
            intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), edit_journey_postition);
            startActivity(intent);
            finish();
        }
    }

    private void routeList() {
        String[] temp = {""};
        switch(mode){
            case 0:
                temp = CarbonModel.getInstance().getRouteCollection().getRouteDescriptions();
                break;
            case 1: temp = CarbonModel.getInstance().getBusRouteCollection().getRouteDescriptions();
                break;
            case 2: temp = CarbonModel.getInstance().getWalkRouteCollection().getRouteDescriptions();
                break;
        }
        adapter = new ArrayAdapter<> (this,R.layout.route_list, temp);
        list = (ListView) findViewById(R.id.listView_routeList);
        list.setAdapter(adapter);
    }

    private void selectExistingRoute(){
        ListView list = (ListView) findViewById(R.id.listView_routeList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Route route = new Route ();
                switch(mode){
                    case 0:
                        route = CarbonModel.getInstance().getRoute(position);
                        break;
                    case 1:
                        route = CarbonModel.getInstance().getBusRoute(position);
                        break;
                    case 2:
                        route = CarbonModel.getInstance().getWalkRoute(position);
                        break;
                }
                if (edit_journey == 0) {
                    CarbonModel.getInstance().getLastJourney().setRoute(route);
                    CarbonModel.getInstance().getDb().insertRowJourney(CarbonModel.getInstance().getLastJourney());
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else {
                    CarbonModel.getInstance().getJourneyCollection().getJourney(edit_journey_postition).setRoute(route);
                    CarbonModel.getInstance().getDb().updateSingleRouteInJourney((edit_journey_postition+1),route);
                    startActivity(new Intent(getApplicationContext(), DisplayTableActivity.class));
                }
                checkForType();
                finish();
            }
        });
    }

    private void setLaunchNewRoute() {
        Button newRouteBtn = (Button) findViewById(R.id.btn_addRoute);
        newRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                intent.putExtra(getResources().getString(R.string.TRANS_MODE), mode);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), edit_journey);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), edit_journey_postition);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_delete_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                switch(mode){
                    case 0:
                        CarbonModel.getInstance().getDb().deleteRouteRow((info.position+1));
                        CarbonModel.getInstance().removeRoute(info.position);
                        break;
                    case 1:
                        CarbonModel.getInstance().getDb().deleteBusRouteRow((info.position+1));
                        CarbonModel.getInstance().removeBusRoute(info.position);
                        break;
                    case 2:
                        CarbonModel.getInstance().getDb().deleteWalkRouteRow((info.position+1));
                        CarbonModel.getInstance().removeWalkRoute(info.position);
                        break;
                }
                adapter.notifyDataSetChanged();
                routeList();
                return true;
            case R.id.edit:
                Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), edit_journey);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), edit_journey_postition);
                intent.putExtra("routeIndex", info.position);
                intent.putExtra(getResources().getString(R.string.TRANS_MODE), mode);
                startActivity(intent);
                finish();
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void checkForType() {
        JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
        BillCollection billCollection = CarbonModel.getInstance().getBillCollection();
        RouteCollection routeCollection = CarbonModel.getInstance().getRouteCollection();

        int i, car_trips = 0;
        double total_car_carbon = 0;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd");

        String formatted = formattedDate.format(cal.getTime());

        Double total_util_carbon = CarbonModel.getInstance().getBillCollection().getTotalCarbonEmission(reportDate);
        Double total_elect_carbon = CarbonModel.getInstance().getBillCollection().getElectricityCarbonEmission(reportDate);
        Double total_gas_carbon = CarbonModel.getInstance().getBillCollection().getGasCarbonEmission(reportDate);

        for(i = 0; i < journeyCollection.countJourneys();i++)
        {
            if(journeyCollection.getJourney(i).getDate().equals(formatted) && !journeyCollection.getJourney(i).getCar().getNickname().equals(" "))
            {
                double car_carbon = parseDouble(journeyCollection.getJourney(i).calculateCarbon());
                car_trips++;
                total_car_carbon = total_car_carbon + car_carbon;
            }
        }
        if(total_car_carbon > total_util_carbon)
        {
            car_array_index = getLastIndexFromSharedPrefCar();
            Math.round(total_car_carbon);
            String total_car_carbon_str = Double.toString(total_car_carbon);
            String car_trips_str = Integer.toString(car_trips);
            String car_msg = "You have gone on "+car_trips_str+" trips today. And the amount of carbon emitted by your car today is: "+total_car_carbon_str+". "+tooMuchCar[car_array_index%8];
            Toast.makeText(getApplicationContext(), car_msg, Toast.LENGTH_LONG).show();
            car_array_index++;
            storeLastIndexCar();
        }
        else // total_util_carbon > total_car_carbon
        {
            if(total_elect_carbon > total_gas_carbon) // more electricity
            {
                elect_array_index = getLastIndexFromSharedPrefElect();
                Math.round(total_elect_carbon);
                String total_elect_carbon_str = Double.toString(total_elect_carbon);
                String elect_msg = "The amount of carbon emission by electricity you have produced today is: "+total_elect_carbon_str+". "+tooMuchElectricity[elect_array_index%9];
                Toast.makeText(getApplicationContext(), elect_msg, Toast.LENGTH_LONG).show();
                elect_array_index++;
                storeLastIndexElect();
            }
            else
            {
                gas_array_index = getLastIndexFromSharedPrefGas();
                Math.round(total_gas_carbon);
                String total_gas_carbon_str = Double.toString(total_gas_carbon);
                String gas_msg = "The amount of carbon emission by natural gas you have produced today is: "+total_gas_carbon_str+". "+tooMuchGas[gas_array_index%8];
                Toast.makeText(getApplicationContext(), gas_msg, Toast.LENGTH_LONG).show();
                gas_array_index++;
                Log.d("gas array index", "This is the gas array index after: "+gas_array_index);
                storeLastIndexGas();
            }
        }
    }
    private int getLastIndexFromSharedPrefCar()
    {
        SharedPreferences prefs = getSharedPreferences("car array", MODE_PRIVATE);
        int extractedValueCar = prefs.getInt("car array index", 0); //first time 0
        return extractedValueCar;
    }
    private int getLastIndexFromSharedPrefElect()
    {
        SharedPreferences prefs = getSharedPreferences("electricity array", MODE_PRIVATE);
        int extractedValueElect = prefs.getInt("elect array index", 0); //first time 0
        return extractedValueElect;
    }
    private int getLastIndexFromSharedPrefGas()
    {
        SharedPreferences prefs = getSharedPreferences("gas array", MODE_PRIVATE);
        int extractedValueGas = prefs.getInt("gas array index", 0); //first time 0
        return extractedValueGas;
    }
    private void storeLastIndexCar()
    {
        int val = car_array_index;
        SharedPreferences prefs = getSharedPreferences("car array", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("car array index", val );
        editor.commit();
    }

    private void storeLastIndexElect()
    {
        int val = elect_array_index;
        SharedPreferences prefs = getSharedPreferences("elect array", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("elect array index", val );
        editor.commit();
    }
    private void storeLastIndexGas()
    {
        int val = gas_array_index;
        SharedPreferences prefs = getSharedPreferences("gas array", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("gas array index", val );
        editor.commit();
    }


}
