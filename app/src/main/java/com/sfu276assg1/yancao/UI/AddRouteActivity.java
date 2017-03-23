package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sfu276assg1.yancao.carbontracker.BillCollection;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.Journey;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;
import com.sfu276assg1.yancao.carbontracker.Route;
import com.sfu276assg1.yancao.carbontracker.RouteCollection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Double.parseDouble;

//Customer can add or edit route in this activity. After editing/adding, goes to mainActivity.
//Customer can choose to save the route, or not save the route, the carbon emission will be calculated in both cases.

public class AddRouteActivity extends AppCompatActivity {

    String routeName = "";
    double higherEDis, lowerEDis, totalDis;
    int routeChangePosition;
    Route tempRoute;
    Route currentRoute = new Route();
    int mode = 0;



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
        setContentView(R.layout.activity_add_route);
        mode = getIntent().getIntExtra(getResources().getString(R.string.TRANS_MODE),0);
        setIndividualDis();
        setTotalDistance();
        setRouteName();
        setupOkButton();
        setRouteType();

        car_array_index = getLastIndexFromSharedPrefCar();
        elect_array_index = getLastIndexFromSharedPrefElect();
        gas_array_index = getLastIndexFromSharedPrefGas();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddRouteActivity.this, SelectRouteActivity.class);
        intent.putExtra(getResources().getString(R.string.TRANS_MODE),mode);
        startActivity(intent);
        finish();
    }

    public void setIndividualDis() {
        TextView textViewL = (TextView) findViewById(R.id.textView_lowerEDis);
        TextView textViewH = (TextView) findViewById(R.id.textView_higherEDis);
        switch(mode){
            case 1:
                textViewL.setText(""+"Skytrain (km):");
                textViewH.setText(""+"Bus (km):");
                break;
            case 2:
                textViewL.setText(""+"Walk (km):");
                textViewH.setText(""+"Bike (km):");
                break;
        }

        EditText lowEDisEntry = (EditText) findViewById(R.id.editView_enterLowEDis);
        String lowEmission = Double.toString(lowerEDis);
        if (lowerEDis > 0) {
            lowEDisEntry.setText(lowEmission);
        }
        lowEDisEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String lowEData = s.toString();
                    if (lowEData.matches("")){
                        lowerEDis = 0;
                    }
                    else{
                        lowerEDis = Double.parseDouble(lowEData);
                    }
                    totalDis = lowerEDis + higherEDis;
                    TextView distance = (TextView) findViewById(R.id.text_enterDis);
                    distance.setText(" " + totalDis);
                }
                catch(NumberFormatException e){}
            }
        });

        EditText highEDisEntry = (EditText) findViewById(R.id.editView_enterHighEDis);
        String highEmission = Double.toString(higherEDis);
        if (higherEDis > 0 ) {
            highEDisEntry.setText(highEmission);
        }
        highEDisEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String highEData = s.toString();
                    if (highEData.matches("")){
                        higherEDis = 0;
                    }
                    else{
                        higherEDis = Double.parseDouble(highEData);
                    }
                    totalDis = lowerEDis + higherEDis;
                    TextView distance = (TextView) findViewById(R.id.text_enterDis);
                    distance.setText(" " + totalDis);
                }
                catch(NumberFormatException e){}
            }
        });
    }

    public void setTotalDistance() {
        TextView distance = (TextView) findViewById(R.id.text_enterDis);
        if (higherEDis > 0 ) {
            totalDis = higherEDis;
            distance.setText(" " + totalDis);
        }
        else if (lowerEDis > 0 ) {
            totalDis = lowerEDis;
            distance.setText(" " + totalDis);
        }
        if (higherEDis > 0 && lowerEDis > 0) {
            totalDis = lowerEDis + higherEDis;
            distance.setText(" " + totalDis);
        }
    }

    public void setRouteName() {
        EditText routeNameEntry = (EditText) findViewById(R.id.editText_enterRouteName);
        routeNameEntry.setText(routeName);
        routeNameEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String str = s.toString();
                    if (str.matches("")){
                        routeName = "";
                    }
                    else{
                        routeName = s.toString();
                    }
                }
                catch(NumberFormatException e){}
            }
        });
    }

    private void setupOkButton() {
        Button okBtn = (Button) findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (totalDis <= 0) {
                Toast.makeText(getApplicationContext(), "Total distance must be positive.",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                currentRoute.setHighEDis(higherEDis);
                currentRoute.setLowEDis(lowerEDis);
                currentRoute.setDistance(totalDis);
                Intent intent;
                routeChangePosition = getIntent().getIntExtra("routeIndex", -1);
                if(routeChangePosition==-1)
                {
                    if (routeName != "") {
                        currentRoute.setName(routeName);
                    }
                    switch(mode){
                        case 0:
                            CarbonModel.getInstance().addRoute(currentRoute);
                            CarbonModel.getInstance().getDb().insertRouteRow(currentRoute);

                            break;
                        case 1:
                            CarbonModel.getInstance().addBusRoute(currentRoute);
                            CarbonModel.getInstance().getDb().insertBusRouteRow(currentRoute);
                            break;
                        case 2:
                            CarbonModel.getInstance().addWalkRoute(currentRoute);
                            CarbonModel.getInstance().getDb().insertWalkRouteRow(currentRoute);
                            break;
                    }

                    CarbonModel.getInstance().getLastJourney().setRoute(currentRoute);
                    CarbonModel.getInstance().getDb().insertRowJourney(CarbonModel.getInstance().getLastJourney());
                    intent = new Intent(AddRouteActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    checkForType();
                }
                else {
                    extractDataFromIntent();
                    if(routeName.isEmpty()) {
                        Toast.makeText(getApplicationContext(),"Please enter a name for the route.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        currentRoute.setName(routeName);
                        int index;
                        switch(mode){
                            case 0:
                                CarbonModel.getInstance().getDb().updateRouteRow(tempRoute,currentRoute);
                                CarbonModel.getInstance().changeRoute(currentRoute, routeChangePosition);
                                break;
                            case 1:
                                CarbonModel.getInstance().getDb().updateBusRouteRow(tempRoute,currentRoute);
                                CarbonModel.getInstance().changeBusRoute(currentRoute, routeChangePosition);
                                break;
                            case 2:
                                CarbonModel.getInstance().getDb().updateWalkRouteRow(tempRoute,currentRoute);
                                CarbonModel.getInstance().changeWalkRoute(currentRoute, routeChangePosition);
                                break;
                        }
                        //CarbonModel.getInstance().changeRouteInJourney(tempRoute, currentRoute);
                        CarbonModel.getInstance().getDb().updateRouteInJourney(tempRoute,currentRoute);

                        intent = new Intent(AddRouteActivity.this, SelectRouteActivity.class);
                        intent.putExtra(getResources().getString(R.string.TRANS_MODE),mode);

                        startActivity(intent);
                        finish();
                    }
                }
            }

            }
        });
    }

    private void showCurrentJouney(){
        Journey curJourney = CarbonModel.getInstance().getLastJourney();
        String msg = curJourney.getJourneyDes();
        Toast.makeText(getApplicationContext(),msg,
                Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),msg,
                Toast.LENGTH_LONG).show();
    }


    private void setRouteType(){
        switch(mode){
            case 0:
                currentRoute.setType("drive");
                break;
            case 1:
                currentRoute.setType("public");
                break;
            case 2:
                currentRoute.setType("walk");
                break;
        }
    }

    private void extractDataFromIntent() {
        tempRoute = new Route();
        routeChangePosition = getIntent().getIntExtra("routeIndex", -1);
        switch(mode){
            case 0: tempRoute = CarbonModel.getInstance().getRoute(routeChangePosition);
                break;
            case 1: tempRoute = CarbonModel.getInstance().getBusRoute(routeChangePosition);
                break;
            case 2: tempRoute = CarbonModel.getInstance().getWalkRoute(routeChangePosition);
                break;
        }
    }

    public void checkForType()
    {

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
            //Log.d("journeys", "This is the journey date: "+journeyCollection.getJourney(i).getDate()+ "This is the current date: "+formatted);
            if(journeyCollection.getJourney(i).getDate().equals(formatted) && !journeyCollection.getJourney(i).getCar().getNickname().equals(" "))
            {
                double car_carbon = parseDouble(journeyCollection.getJourney(i).calculateCarbon());
                car_trips++;
                total_car_carbon = total_car_carbon + car_carbon;
            }


        }

        Log.d("Total Util", "This is the total utility: "+total_util_carbon);
        Log.d("Total car", "This is the total car: "+total_car_carbon);


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


