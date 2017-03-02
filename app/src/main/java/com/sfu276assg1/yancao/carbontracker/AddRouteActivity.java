package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                Route add = isValidRouteInput();
                if(add!=null){
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                if(add!=null){
                    if(routeName.length()==0||routeName==null){
                        Toast.makeText(getApplicationContext(),"Please enter a name for the route.",Toast.LENGTH_LONG).show();
                    }
                    else{
                        add.setName(routeName);
//                        CarbonModel.getInstance().getCurrentRoute().setName(routeName);
//                        Route current = CarbonModel.getInstance().getCurrentRoute();

                        if(index == -1){
                            CarbonModel.getInstance().getRouteCollection().addRoute(add);
                        }
                        else{
                            CarbonModel.getInstance().getRouteCollection().changeRoute(add,index);
                        }
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }
            }
        });
    }

    private Route isValidRouteInput() {
        EditText routeDistanceEntry = (EditText) findViewById(R.id.editView_enterDistance);
        String routeDistanceData = routeDistanceEntry.getText().toString();
        double routeDistance = validPositiveNum(routeDistanceData);

        EditText highwayPerEntry = (EditText) findViewById(R.id.editView_enterHighwayPer);
        String highwayPerData = highwayPerEntry.getText().toString();
        double highwayPer = validPositiveNum(highwayPerData);

        EditText cityPerEntry = (EditText) findViewById(R.id.editView_enterCityPer);
        String cityPerData = cityPerEntry.getText().toString();
        double cityPer = validPositiveNum(cityPerData);

        if(routeDistance<=0){
            String msg_1 = "Route distance must be positive.";
            Toast.makeText(getApplicationContext(), msg_1, Toast.LENGTH_SHORT).show();
        }
        else if(highwayPer<0||highwayPer>100){
            String msg_2 = "Highway% must be between 0 and 100";
            Toast.makeText(getApplicationContext(), msg_2, Toast.LENGTH_SHORT).show();
        }
        else if(cityPer<0||cityPer>100){
            String msg_3 = "City% must be between 0 and 100";
            Toast.makeText(getApplicationContext(), msg_3, Toast.LENGTH_SHORT).show();
        }
        else if(highwayPer+cityPer>100){
            String msg_3 = "Highway% + City% must be less than 100";
            Toast.makeText(getApplicationContext(), msg_3, Toast.LENGTH_SHORT).show();
        }
        else{
            Route newRoute = new Route(routeDistance, highwayPer, cityPer);
//            CarbonModel.getInstance().getCurrentRoute().setRoute(newRoute);
            return newRoute;
        }
        return null;
    }

    public static double validPositiveNum(String text) {
        int index =0;
        double result = 0;
        for(int i=0; i<text.length();i++){
            if (text.charAt(i)!=' '){
                index = i;
                break;
            }
        }
        String subString = text.substring(index,text.length());
        try {
            result = Double.parseDouble(subString);
        } catch (NumberFormatException ex) {}
        return result;
    }
}
