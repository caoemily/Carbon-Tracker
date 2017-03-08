package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//Customer can add or edit route in this activity. After editing/adding, goes to mainActivity.
//Customer can choose to save the route, or not save the route, the carbon emission will be calculated in both cases.

public class AddRouteActivity extends AppCompatActivity {

    double cityDriv = 0, highwayDriv = 0, totalDis = 0;
    Route newRoute = new Route(0, 0, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        setHighwayDis();
        setupAcceptButton();
        setupNoSaveButton();
    }

    public void setHighwayDis() {
        final EditText highwayPerEntry = (EditText) findViewById(R.id.editView_enterHighwayPer);
        highwayPerEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String highwayPerData = highwayPerEntry.getText().toString();
                highwayDriv = validPositiveNum(highwayPerData);
                if (highwayDriv < 0) {
                    String msg_1 = "Highway drive distance must be positive number.";
                    Toast.makeText(getApplicationContext(), msg_1, Toast.LENGTH_SHORT).show();
                }
                totalDis = highwayDriv + cityDriv;
                final TextView distance = (TextView) findViewById(R.id.text_enterDis);
                distance.setText("" + totalDis);
            }
        });

        final EditText cityPerEntry = (EditText) findViewById(R.id.editView_enterCityPer);
        cityPerEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String cityPerData = cityPerEntry.getText().toString();
                cityDriv = validPositiveNum(cityPerData);
                if (cityDriv < 0) {
                    String msg_2 = "City drive distance must be positive number.";
                    Toast.makeText(getApplicationContext(), msg_2, Toast.LENGTH_SHORT).show();
                }
                totalDis = highwayDriv + cityDriv;
                final TextView distance = (TextView) findViewById(R.id.text_enterDis);
                distance.setText("" + totalDis);
            }
        });
    }

    private void setupNoSaveButton() {
        Button accBtn = (Button) findViewById(R.id.btn_noSaveRoute);
        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Route add = isValidRouteInput();
                if(add!=null){
                    EditText routeNameEntry = (EditText) findViewById(R.id.editText_enterRouteName);
                    String routeName = routeNameEntry.getText().toString();
                    if(!routeName.isEmpty()){
                        add.setName(routeName);
                    }
                    CarbonModel.getInstance().addRouteToAllRoute(add);
                    addJourney();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void setupAcceptButton() {
        Button accBtn = (Button) findViewById(R.id.btn_accRoute);
        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnValueIntent = getIntent();
                int index = returnValueIntent.getIntExtra("routeIndex",-1);
                EditText routeNameEntry = (EditText) findViewById(R.id.editText_enterRouteName);
                String routeName = routeNameEntry.getText().toString();
                Route add = isValidRouteInput();
                if(add != null){
                    if(routeName.length()==0 || routeName == null){
                        Toast.makeText(getApplicationContext(),"Please enter a name for the route.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        add.setName(routeName);
                        CarbonModel.getInstance().addRouteToAllRoute(add);
                        if(index == -1){
                            CarbonModel.getInstance().addRouteToCollecton(add);
                        }
                        else{
                            CarbonModel.getInstance().changeRouteInCollection(add,index);
                        }
                        addJourney();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
            }
        });
    }

    private Route isValidRouteInput() {
        TextView routeDistanceEntry = (TextView) findViewById(R.id.text_enterDis);
        String routeDistanceData = routeDistanceEntry.getText().toString();
        double routeDistance = validPositiveNum(routeDistanceData);

        EditText highwayPerEntry = (EditText) findViewById(R.id.editView_enterHighwayPer);
        String highwayPerData = highwayPerEntry.getText().toString();
        double highwayPer = validPositiveNum(highwayPerData);

        EditText cityPerEntry = (EditText) findViewById(R.id.editView_enterCityPer);
        String cityPerData = cityPerEntry.getText().toString();
        double cityPer = validPositiveNum(cityPerData);

        if(routeDistance <= 0){
            String msg_1 = "Route distance must be positive.";
            Toast.makeText(getApplicationContext(), msg_1, Toast.LENGTH_SHORT).show();
        }
        if(highwayPer < 0){
            String msg_2 = "Highway drive distance must be positive number.";
            Toast.makeText(getApplicationContext(), msg_2, Toast.LENGTH_SHORT).show();
        }
        else if(cityPer < 0){
            String msg_3 = "City drive distance must be positive number.";
            Toast.makeText(getApplicationContext(), msg_3, Toast.LENGTH_SHORT).show();
        }
        else{
            Route newRoute = new Route(routeDistance, highwayPer, cityPer);
            return newRoute;
        }
        return null;
    }

    public double validPositiveNum(String text) {
        int index = 0;
        double result = -1;
        for(int i = 0; i < text.length(); i++){
            if (text.charAt(i) != ' '){
                index = i;
                break;
            }
        }
        String subString = text.substring(index, text.length());
        try {
            result = Double.parseDouble(subString);
        } catch (NumberFormatException ex) {}
        return result;
    }

    private void addJourney() {
        Car car = CarbonModel.getInstance().getLastCarInList();
        Route route = CarbonModel.getInstance().getLastRoute();
        Journey journey = new Journey(car,route);
        CarbonModel.getInstance().addJourney(journey);
    }
}
