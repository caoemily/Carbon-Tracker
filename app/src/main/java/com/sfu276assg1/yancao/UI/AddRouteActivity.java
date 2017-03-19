package com.sfu276assg1.yancao.UI;

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

import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.Journey;
import com.sfu276assg1.yancao.carbontracker.R;
import com.sfu276assg1.yancao.carbontracker.Route;

//Customer can add or edit route in this activity. After editing/adding, goes to mainActivity.
//Customer can choose to save the route, or not save the route, the carbon emission will be calculated in both cases.

public class AddRouteActivity extends AppCompatActivity {

    String routeName = "";
    double higherEDis, lowerEDis, totalDis;
    int routeChangePosition;
    Route tempRoute;
    Route currentRoute = new Route();
    int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        mode = getIntent().getIntExtra("TransMode",0);
        setIndividualDis();
        setTotalDistance();
        setRouteName();
        setupOkButton();
        setRouteType();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddRouteActivity.this, SelectRouteActivity.class);
        intent.putExtra("TransMode",mode);
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
                            MainActivity.db.insertRouteRow(currentRoute);
                            CarbonModel.getInstance().getLastJourney().setRoute(currentRoute);
                            break;
                        case 1:
                            CarbonModel.getInstance().addBusRoute(currentRoute);
                            MainActivity.db.insertBusRouteRow(currentRoute);
                            CarbonModel.getInstance().getJourneyCollection().addJourney(new Journey(currentRoute));
                            break;
                        case 2:
                            CarbonModel.getInstance().addWalkRoute(currentRoute);
                            MainActivity.db.insertWalkRouteRow(currentRoute);
                            CarbonModel.getInstance().getJourneyCollection().addJourney(new Journey(currentRoute));
                            break;
                    }

                    MainActivity.db.insertRowJourney(CarbonModel.getInstance().getLastJourney());
                    intent = new Intent(AddRouteActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    showCurrentJouney();
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
                                MainActivity.db.updateRouteRow(tempRoute.getName(),currentRoute);
                                CarbonModel.getInstance().changeRoute(currentRoute, routeChangePosition);
                                break;
                            case 1:
                                MainActivity.db.updateBusRouteRow(tempRoute.getName(),currentRoute);
                                CarbonModel.getInstance().changeBusRoute(currentRoute, routeChangePosition);
                                break;
                            case 2:
                                MainActivity.db.updateWalkRouteRow(tempRoute.getName(),currentRoute);
                                CarbonModel.getInstance().changeWalkRoute(currentRoute, routeChangePosition);
                                break;
                        }
                        CarbonModel.getInstance().changeRouteInJourney(tempRoute, currentRoute);
                        //update database for journeyCollection

                        intent = new Intent(AddRouteActivity.this, SelectRouteActivity.class);
                        intent.putExtra("TransMode",mode);
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
}
