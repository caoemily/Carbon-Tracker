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
import android.widget.Toast;

import com.sfu276assg1.yancao.carbontracker.Bill;
import com.sfu276assg1.yancao.carbontracker.BillCollection;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;
import com.sfu276assg1.yancao.carbontracker.RouteCollection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Double.parseDouble;

public class AddBillActivity extends AppCompatActivity {

    String startDate = "", endDate = "";
    double gas, electricity;
    int people;
    int position;

    int car_array_index, elect_array_index, gas_array_index;


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
        setContentView(R.layout.activity_add_bill);

        if( getIntent().getExtras() != null)
        {
            extractDataFromIntent();
        }

        setDate();
        setConsumption();
        setPeople();
        setupOkButton();

        car_array_index = getLastIndexFromSharedPrefCar();
        elect_array_index = getLastIndexFromSharedPrefElect();
        gas_array_index = getLastIndexFromSharedPrefGas();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddBillActivity.this, MonthlyUtilitiesActivity.class);
        startActivity(intent);
        finish();
    }

    private void setUpUtilitiesButton() {
        Button button = (Button) findViewById(R.id.okBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBillActivity.this, MonthlyUtilitiesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void setDate() {
        EditText edit_startDate = (EditText) findViewById(R.id.editStartDate);
        edit_startDate.setText(startDate);
        edit_startDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String str = s.toString();
                    if (str.matches("")){
                        startDate = "";
                    }
                    else{
                        startDate = s.toString();
                    }
                }
                catch(NumberFormatException e){}
            }
        });

        EditText edit_endDate = (EditText) findViewById(R.id.editEndDate);
        edit_endDate.setText(endDate);
        edit_endDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String str = s.toString();
                    if (str.matches("")){
                        endDate = "";
                    }
                    else{
                        endDate = s.toString();
                    }
                }
                catch(NumberFormatException e){}
            }
        });
    }

    public void setConsumption() {
        EditText edit_electricity = (EditText) findViewById(R.id.editElectricity);
        String num_electricity = Double.toString(electricity);
        if (electricity > 0) {
            edit_electricity.setText(num_electricity);
        }
        edit_electricity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String data = s.toString();
                    if (data.matches("")){
                        electricity = 0;
                    }
                    else{
                        electricity = Double.parseDouble(data);
                    }
                }
                catch(NumberFormatException e){}
            }
        });

        EditText edit_gas = (EditText) findViewById(R.id.editGas);
        String num_gas = Double.toString(gas);
        if (gas > 0 ) {
            edit_gas.setText(num_gas);
        }
        edit_gas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String data = s.toString();
                    if (data.matches("")){
                        gas = 0;
                    }
                    else{
                        gas = Double.parseDouble(data);
                    }
                }
                catch(NumberFormatException e){}
            }
        });
    }

    public void setPeople() {
        EditText edit_people = (EditText) findViewById(R.id.editPeople);
        String num_people = Double.toString(people);
        if (people > 0 ) {
            edit_people.setText(num_people);
        }
        edit_people.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String data = s.toString();
                    if (data.matches("")){
                        people = 0;
                    }
                    else{
                        people = Integer.parseInt(data);
                    }
                }
                catch(NumberFormatException e){}
            }
        });
    }

    private void setupOkButton() {
        Button okBtn = (Button) findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDate.length() == 0 || startDate == null){
                    Toast.makeText(getApplicationContext(), "Please enter the start date of the bill", Toast.LENGTH_SHORT).show();

                }
                else if (endDate.length() == 0 || endDate == null) {
                    Toast.makeText(getApplicationContext(), "Please enter the end date of the bill", Toast.LENGTH_SHORT).show();
                }
                else if (people == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter the number of people in the house", Toast.LENGTH_SHORT).show();
                }
                else if (electricity == 0 && gas == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter the consumptions", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent;
                    Bill bill = new Bill(startDate, endDate, electricity, gas, people);
                    if(getIntent().getExtras() == null) {
                        CarbonModel.getInstance().addBill(bill);
                        CarbonModel.getInstance().getDb().insertBillRow(bill);
                        intent = new Intent(AddBillActivity.this, MonthlyUtilitiesActivity.class);
                    }
                    else {
                        CarbonModel.getInstance().changeBill(bill, position);
                        CarbonModel.getInstance().getDb().updateBillRow((position),bill);
                        intent = new Intent(AddBillActivity.this, MonthlyUtilitiesActivity.class);
                    }
                    checkForType();
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void extractDataFromIntent() {
        position = getIntent().getIntExtra("billIndex", 0);
        startDate = CarbonModel.getInstance().getBill(position).getStartDate();
        endDate = CarbonModel.getInstance().getBill(position).getEndDate();
        electricity = CarbonModel.getInstance().getBill(position).getElectricity();
        gas = CarbonModel.getInstance().getBill(position).getGas();
        people = CarbonModel.getInstance().getBill(position).getPeople();
    }

    public void checkForType()
    {

        JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
        BillCollection billCollection = CarbonModel.getInstance().getBillCollection();
        RouteCollection routeCollection = CarbonModel.getInstance().getRouteCollection();

        int i, car_trips = 0;
        double total_car_carbon = 0;

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
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


        if(total_car_carbon > total_util_carbon)
        {
            car_array_index = getLastIndexFromSharedPrefCar();

            Math.round(total_car_carbon);

            String total_car_carbon_str = Double.toString(total_car_carbon);



            String car_trips_str = Integer.toString(car_trips);
            String car_msg = "You have gone on "+car_trips_str+" trips today. And the amount of carbon emitted by your car today is: "+total_car_carbon_str+"."+tooMuchCar[car_array_index%7];


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
                String elect_msg = "The amount of carbon emission by electricity you have produced today is: "+total_elect_carbon_str+"."+tooMuchElectricity[elect_array_index%7];

                Toast.makeText(getApplicationContext(), elect_msg, Toast.LENGTH_LONG).show();

                elect_array_index++;


                storeLastIndexElect();
            }

            else
            {
                gas_array_index = getLastIndexFromSharedPrefGas();

                Math.round(total_gas_carbon);

                String total_gas_carbon_str = Double.toString(total_gas_carbon);
                String gas_msg = "The amount of carbon emission by natural gas you have produced today is: "+total_gas_carbon_str+"."+tooMuchGas[gas_array_index%4];

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
