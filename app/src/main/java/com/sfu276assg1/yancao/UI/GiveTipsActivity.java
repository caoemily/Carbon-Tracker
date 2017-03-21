package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sfu276assg1.yancao.carbontracker.BillCollection;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;
import com.sfu276assg1.yancao.carbontracker.RouteCollection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class GiveTipsActivity extends AppCompatActivity {

    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    private BillCollection billCollection = CarbonModel.getInstance().getBillCollection();
    private RouteCollection routeCollection = CarbonModel.getInstance().getRouteCollection();

    int car_array_index = 0, elect_array_index =0, gas_array_index = 0;




    String[] tooMuchCar = {"Try to take the bike!", "Try to take the public transit", "Try to walk!",
            "Avoid areas with congested traffic!", "Plan out your journey so you don't get lost and waste fuel!",
            "Keep your vehicles well maintained","Don't accelerate unnecessarily!"};

    String[] tooMuchElectricity = {"Turn off the lights when you can!", "Install Compact Fluorescent Bulbs to save energy!", "Wash your clothes with cold water!",
            "Set your refrigerator to the optimal temperature!", "Turn off your lights when you're not using it!", "Wash and dry full loads!", "Cut your heating needs!",
            "Unplug unnecessary appliances!"};

    String[] tooMuchGas = {"Insulate your house!", "Take quicker showers!", "Close off doors and vents in unused rooms to conserve heat within your home!",
            "Upgrade your heating equipments!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_tips);

        storeLastIndexCar();
        storeLastIndexElect();
        storeLastIndexGas();

        checkForType();


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GiveTipsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void checkForType()
    {
        int i, car_trips = 0;
        int total_car_carbon = 0;

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        ArrayList itemsForTips = new ArrayList<>();

        Double total_util_carbon = CarbonModel.getInstance().getBillCollection().getTotalCarbonEmission(reportDate);
        Double total_elect_carbon = CarbonModel.getInstance().getBillCollection().getElectricityCarbonEmission(reportDate);
        Double total_gas_carbon = CarbonModel.getInstance().getBillCollection().getGasCarbonEmission(reportDate);

        //checking for cars
        for(i = 0; i < journeyCollection.countJourneys();i++)
        {
            if(journeyCollection.getJourney(i).getDate().equals(cal) && !journeyCollection.getJourney(i).getCar().getNickname().equals(""))
            {
                String car_carbon = journeyCollection.getJourney(i).calculateCarbon();
                itemsForTips.add(car_carbon);
                car_trips++;
                total_car_carbon = total_car_carbon + parseInt(car_carbon);
            }


        }


        if(car_trips > 3)
        {
            TextView giveTipsTxt = (TextView)findViewById(R.id.txtTips);


            String car_trips_str = Integer.toString(car_trips);

            giveTipsTxt.setText("You have gone on"+car_trips_str+"trips today, maybe consider combining errands to make fewer trips!");


        }

        else if (total_car_carbon > total_util_carbon)
        {
            car_array_index = getLastIndexFromSharedPrefCar();

            TextView giveTipsTxt = (TextView)findViewById(R.id.txtTips);

            String total_car_carbon_str = Integer.toString(total_car_carbon);

            giveTipsTxt.setText("The amount of carbon emission by car you have produced today is: "+total_car_carbon_str+"."+tooMuchCar[car_array_index]);

        }

        else // total_util_carbon > total_car_carbon
        {
            if(total_elect_carbon > total_gas_carbon) // more electricity
            {
                elect_array_index = getLastIndexFromSharedPrefElect();

                TextView giveTipsTxt = (TextView)findViewById(R.id.txtTips);

                String total_elect_carbon_str = Double.toString(total_elect_carbon);

                giveTipsTxt.setText("The amount of carbon emission by electricity you have produced today is: "+total_elect_carbon_str+"."+tooMuchElectricity[elect_array_index]);

            }

            else
            {
                gas_array_index = getLastIndexFromSharedPrefGas();

                TextView giveTipsTxt = (TextView)findViewById(R.id.txtTips);

                String total_gas_carbon_str = Double.toString(total_gas_carbon);

                giveTipsTxt.setText("The amount of carbon emission by natural gas you have produced today is: "+total_gas_carbon_str+"."+tooMuchGas[gas_array_index]);
            }
        }

    }
    private int getLastIndexFromSharedPrefCar()
    {
        SharedPreferences prefs = getSharedPreferences("car array", MODE_PRIVATE);
        int extractedValue = prefs.getInt("car array index", 0); //first time 0
        return extractedValue;
    }

    private int getLastIndexFromSharedPrefElect()
    {
        SharedPreferences prefs = getSharedPreferences("electricity array", MODE_PRIVATE);
        int extractedValue = prefs.getInt("elect array index", 0); //first time 0
        return extractedValue;
    }

    private int getLastIndexFromSharedPrefGas()
    {
        SharedPreferences prefs = getSharedPreferences("gas array", MODE_PRIVATE);
        int extractedValue = prefs.getInt("gas array index", 0); //first time 0
        return extractedValue;
    }



    private void storeLastIndexCar()
    {
        int val = car_array_index++;
        SharedPreferences prefs = getSharedPreferences("car array", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("CarArrayIndex", val );
        editor.commit();
    }

    private void storeLastIndexElect()
    {
        int val = elect_array_index++;
        SharedPreferences prefs = getSharedPreferences("elect array", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("ElectArrayIndex", val );
        editor.commit();
    }

    private void storeLastIndexGas()
    {
        int val = gas_array_index++;
        SharedPreferences prefs = getSharedPreferences("gas array", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("GasArrayIndex", val );
        editor.commit();
    }



}
