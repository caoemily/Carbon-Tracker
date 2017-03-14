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

    String routeName = "";
    double cityDriv, highwayDriv, totalDis;
    int routeChangePosition;
    Route tempRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);

        if( getIntent().getExtras() != null)
        {
            extractDataFromIntent();
        }

        setHighwayDis();
        setTotalDistance();
        setRouteName();
        setupOkButton();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddRouteActivity.this, SelectRouteActivity.class);
        startActivity(intent);
        finish();
    }

    public void setHighwayDis() {
        EditText highwayPerEntry = (EditText) findViewById(R.id.editView_enterHighwayPer);
        String highWay = Double.toString(highwayDriv);
        if (highwayDriv > 0) {
            highwayPerEntry.setText(highWay);
        }
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
                    String highwayPerData = s.toString();
                    if (highwayPerData.matches("")){
                        highwayDriv = 0;
                    }
                    else{
                        highwayDriv = Double.parseDouble(highwayPerData);
                    }
                    totalDis = highwayDriv + cityDriv;
                    TextView distance = (TextView) findViewById(R.id.text_enterDis);
                    distance.setText(" " + totalDis);
                }
                catch(NumberFormatException e){}
            }
        });

        EditText cityPerEntry = (EditText) findViewById(R.id.editView_enterCityPer);
        String city = Double.toString(cityDriv);
        if (cityDriv > 0 ) {
            cityPerEntry.setText(city);
        }
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
                    String cityPerData = s.toString();
                    if (cityPerData.matches("")){
                        cityDriv = 0;
                    }
                    else{
                        cityDriv = Double.parseDouble(cityPerData);
                    }
                    totalDis = highwayDriv + cityDriv;
                    TextView distance = (TextView) findViewById(R.id.text_enterDis);
                    distance.setText(" " + totalDis);
                }
                catch(NumberFormatException e){}
            }
        });
    }

    public void setTotalDistance() {
        TextView distance = (TextView) findViewById(R.id.text_enterDis);
        if (cityDriv > 0 ) {
            totalDis = cityDriv;
            distance.setText(" " + totalDis);
        }
        else if (highwayDriv > 0 ) {
            totalDis = highwayDriv;
            distance.setText(" " + totalDis);
        }
        if (cityDriv > 0 && highwayDriv > 0) {
            totalDis = highwayDriv + cityDriv;
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
                Route route = new Route(totalDis, highwayDriv, cityDriv);
                Intent intent;
                if(getIntent().getExtras() == null)
                {
                    if (routeName != "") {
                        route.setName(routeName);
                        CarbonModel.getInstance().addRoute(route);
                    }
                    CarbonModel.getInstance().getLastJourney().setRoute(route);
                    intent = new Intent(AddRouteActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    showCurrentJouney();
                }
                else {
                    if(routeName.isEmpty()) {
                        Toast.makeText(getApplicationContext(),"Please enter a name for the route.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        route.setName(routeName);
                        CarbonModel.getInstance().changeRoute(route, routeChangePosition);
                        CarbonModel.getInstance().changeRouteInJourney(tempRoute, route);
                        intent = new Intent(AddRouteActivity.this, SelectRouteActivity.class);
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

    private void extractDataFromIntent() {
        tempRoute = new Route();
        tempRoute = CarbonModel.getInstance().getRoute(routeChangePosition);
        routeChangePosition = getIntent().getIntExtra("carIndex", 0);
        routeName = CarbonModel.getInstance().getRoute(routeChangePosition).getName();
        cityDriv = CarbonModel.getInstance().getRoute(routeChangePosition).getCity();
        highwayDriv = CarbonModel.getInstance().getRoute(routeChangePosition).getHighway();

    }
}
