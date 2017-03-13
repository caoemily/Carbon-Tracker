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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        setHighwayDis();
        setupAcceptButton();
        setupNoSaveButton();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddRouteActivity.this, SelectRouteActivity.class);
        startActivity(intent);
        finish();
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
                try {
                    String highwayPerData = highwayPerEntry.getText().toString();
                    if (highwayPerData.matches("")){
                        highwayDriv = 0;
                    }
                    else{
                        highwayDriv = Double.parseDouble(highwayPerData);
                    }
                    totalDis = highwayDriv + cityDriv;
                    final TextView distance = (TextView) findViewById(R.id.text_enterDis);
                    distance.setText("" + totalDis);
                }
                catch(NumberFormatException e){}
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
                try {
                    String cityPerData = cityPerEntry.getText().toString();
                    if (cityPerData.matches("")){
                        cityDriv = 0;
                    }
                    else{
                        cityDriv = Double.parseDouble(cityPerData);
                    }
                    totalDis = highwayDriv + cityDriv;
                    final TextView distance = (TextView) findViewById(R.id.text_enterDis);
                    distance.setText("" + totalDis);
                }
                catch(NumberFormatException e){}
            }
        });
    }

    private void setupNoSaveButton() {
        Button accBtn = (Button) findViewById(R.id.btn_noSaveRoute);
        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalDis <= 0){
                    Toast.makeText(getApplicationContext(),"Total distance must be positive.",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Route route = new Route(totalDis, highwayDriv, cityDriv);
                    CarbonModel.getInstance().addRouteToAllRoute(route);
                    addJourney();
                    showCurrentJouney();
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
            int index = returnValueIntent.getIntExtra("routeIndex", -1);
            EditText routeNameEntry = (EditText) findViewById(R.id.editText_enterRouteName);
            String routeName = routeNameEntry.getText().toString();
            if(routeName.isEmpty()) {
                Toast.makeText(getApplicationContext(),"Please enter a name for the route.",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                if (totalDis <= 0) {
                    Toast.makeText(getApplicationContext(), "Total distance must be positive.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Route route = new Route(totalDis, highwayDriv, cityDriv);
                    route.setName(routeName);
                    if (index == -1) {
                        CarbonModel.getInstance().addRouteToCollecton(route);
                    } else {
                        String origRouteName = CarbonModel.getInstance().getRouteCollection().getRoute(index).getName();
                        for(int i=0; i<CarbonModel.getInstance().getJourneyCollection().countJourneys();i++){
                            if(CarbonModel.getInstance().getJourneyCollection().getJourney(i).getRouteName().equals(origRouteName)){
                                CarbonModel.getInstance().getJourneyCollection().getJourney(i).changeRouteInJourney(routeName,route);
                            }
                        }
                        CarbonModel.getInstance().changeRouteInCollection(route, index);
                    }
                    CarbonModel.getInstance().addRouteToAllRoute(route);
                    addJourney();
                    showCurrentJouney();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
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

    private void addJourney() {
        Car car = CarbonModel.getInstance().getLastCarInList();
        Route route = CarbonModel.getInstance().getLastRoute();
        Journey journey = new Journey(car,route);
        CarbonModel.getInstance().addJourney(journey);
    }
}
