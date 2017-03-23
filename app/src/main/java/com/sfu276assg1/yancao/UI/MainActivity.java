package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sfu276assg1.yancao.carbontracker.BillCollection;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.DBAdapter;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;
import com.sfu276assg1.yancao.carbontracker.RouteCollection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Double.parseDouble;

/**
 * Choose to add journey OR show carbon footprint in table or chart
 */

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

        setupDatabase();
        setUpAddJourneytButton();
        setUpShowTableButton();
        setUpShowChartButton();
        setUpUtilitiesButton();
        setUpNextTips();
    }

    private void setupDatabase() {
        DBAdapter db = new DBAdapter(getApplicationContext());
        db.open();
        CarbonModel.getInstance().setDb(db);
        CarbonModel.getInstance().setJourneyCollection(CarbonModel.getInstance().getDb().getJourneyList());
        CarbonModel.getInstance().setCarCollection(CarbonModel.getInstance().getDb().getCarList());
        CarbonModel.getInstance().setRouteCollection(CarbonModel.getInstance().getDb().getRouteList());
        CarbonModel.getInstance().setBusRouteCollection(CarbonModel.getInstance().getDb().getBusRouteList());
        CarbonModel.getInstance().setWalkRouteCollection(CarbonModel.getInstance().getDb().getWalkRouteList());
        CarbonModel.getInstance().setBillCollection(CarbonModel.getInstance().getDb().getBillList());
    }

    private void setUpAddJourneytButton() {
        ImageButton button = (ImageButton) findViewById(R.id.addJourneyBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectTransModeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setUpShowTableButton() {
        ImageButton button = (ImageButton) findViewById(R.id.showTableBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayTableActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpShowChartButton() {
        ImageButton button = (ImageButton) findViewById(R.id.showChartBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectGraphActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpUtilitiesButton() {
        ImageButton button = (ImageButton) findViewById(R.id.utilitiesBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MonthlyUtilitiesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setUpNextTips() {
        ImageButton button = (ImageButton) findViewById(R.id.nextTipsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        checkForType();

            }
        });
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

        for(i = 0; i < journeyCollection.countJourneys();i++) {
            //Log.d("journeys", "This is the journey date: "+journeyCollection.getJourney(i).getDate()+ "This is the current date: "+formatted);
            if(journeyCollection.getJourney(i).getDate().equals(formatted) && !journeyCollection.getJourney(i).getCar().getNickname().equals(" ")) {
                double car_carbon = parseDouble(journeyCollection.getJourney(i).calculateCarbon());
                car_trips++;
                total_car_carbon = total_car_carbon + car_carbon;
            }
        }
        if(total_car_carbon > total_util_carbon) {
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
            else {
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
    private int getLastIndexFromSharedPrefCar() {
        SharedPreferences prefs = getSharedPreferences("car array", MODE_PRIVATE);
        int extractedValueCar = prefs.getInt("car array index", 0); //first time 0
        return extractedValueCar;
    }

    private int getLastIndexFromSharedPrefElect() {
        SharedPreferences prefs = getSharedPreferences("electricity array", MODE_PRIVATE);
        int extractedValueElect = prefs.getInt("elect array index", 0); //first time 0
        return extractedValueElect;
    }
    private int getLastIndexFromSharedPrefGas() {
        SharedPreferences prefs = getSharedPreferences("gas array", MODE_PRIVATE);
        int extractedValueGas = prefs.getInt("gas array index", 0); //first time 0
        return extractedValueGas;
    }
    private void storeLastIndexCar() {
        int val = car_array_index;
        SharedPreferences prefs = getSharedPreferences("car array", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("car array index", val );
        editor.commit();
    }
    private void storeLastIndexElect() {
        int val = elect_array_index;
        SharedPreferences prefs = getSharedPreferences("elect array", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("elect array index", val );
        editor.commit();
    }
    private void storeLastIndexGas() {
        int val = gas_array_index;
        SharedPreferences prefs = getSharedPreferences("gas array", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("gas array index", val );
        editor.commit();
    }

}
