package com.sfu276assg1.yancao.carbontracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddRouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        setupAcceptButton();
        setupNoSaveButton();
    }

    private void setupNoSaveButton() {
        Button accBtn = (Button) findViewById(R.id.btn_noSaveRoute);
        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Route current = getRouteInfo();
                CarbonModel.getInstance().getCurrentRoute().setRoute(current);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                Route current = getRouteInfo();
                EditText routeNameEntry = (EditText) findViewById(R.id.editText_enterRouteName);
                String routeName = routeNameEntry.getText().toString();
                if(routeName.length()==0||routeName==null){
                    Toast.makeText(getApplicationContext(),"Please enter a name for the route.",Toast.LENGTH_LONG).show();
                }
                else{
                    current.setName(routeName);
                    if(index == -1){
                        CarbonModel.getInstance().getRouteCollection().addRoute(current);
                    }
                    else{
                        CarbonModel.getInstance().getRouteCollection().changeRoute(current,index);
                    }
                    CarbonModel.getInstance().getCurrentRoute().setRoute(current);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });
    }

    private Route getRouteInfo() {
        EditText routeDistanceEntry = (EditText) findViewById(R.id.editView_enterDistance);
        String routeDistanceData = routeDistanceEntry.getText().toString();
        int routeDistance = Integer.parseInt(routeDistanceData);
        EditText highwayPerEntry = (EditText) findViewById(R.id.editView_enterHighwayPer);
        String highwayPerData = highwayPerEntry.getText().toString();
        int highwayPer = Integer.parseInt(highwayPerData);
        EditText cityPerEntry = (EditText) findViewById(R.id.editView_enterCityPer);
        String cityPerData = cityPerEntry.getText().toString();
        int cityPer = Integer.parseInt(cityPerData);
        Route newRoute = new Route(routeDistance, highwayPer, cityPer);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(new Date());
        newRoute.setDate(date);
        return newRoute;
    }
}
