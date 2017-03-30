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

    public static final int INVALID_INDEX = -1;
    public static final int TRANSMODE_DEFAULT = 0;
    public static final String ROUTE_INDEX = "routeIndex";


    String routeName = "";
    double higherEDis, lowerEDis, totalDis;
    int routeChangePosition;
    Route tempRoute;
    Route currentRoute = new Route();
    int mode = 0;
    int edit_journey;
    int edit_journey_postition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);

        setInitialSettings();
        setIndividualDis();
        setTotalDistance();
        setRouteName();
        setRouteType();
        setupOkButton();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddRouteActivity.this, SelectRouteActivity.class);
        intent.putExtra(getResources().getString(R.string.TRANS_MODE), mode);
        intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), edit_journey);
        intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), edit_journey_postition);
        startActivity(intent);
        finish();
    }

    public void setInitialSettings(){
        mode = getIntent().getIntExtra(getResources().getString(R.string.TRANS_MODE), TRANSMODE_DEFAULT);
        routeChangePosition = getIntent().getIntExtra(ROUTE_INDEX, INVALID_INDEX);
        if(routeChangePosition != INVALID_INDEX) {
            extractDataFromIntent();
        }
        edit_journey = getIntent().getIntExtra(getResources().getString(R.string.EDIT_JOURNEY), SelectCarActivity.EDITJOURNEY_DEFAULT);
        edit_journey_postition = getIntent().getIntExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), SelectCarActivity.EDITJOURNEY_POSITION_DEFAULT);
    }

    public void setIndividualDis() {
        TextView textViewL = (TextView) findViewById(R.id.textView_lowerEDis);
        TextView textViewH = (TextView) findViewById(R.id.textView_higherEDis);
        switch(mode){
            case 1:
                textViewL.setText(""+ getResources().getString(R.string.SKYTRAIN) + " (km)");
                textViewH.setText(""+ getResources().getString(R.string.BUS) + " (km)");
                break;
            case 2:
                textViewL.setText(""+ getResources().getString(R.string.WALK) + " (km)");
                textViewH.setText(""+getResources().getString(R.string.BIKE) + " (km)");
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
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.TOTAL_DISTANCE_POSITIVE),
                        Toast.LENGTH_SHORT).show();
            }
            else {
                currentRoute.setHighEDis(higherEDis);
                currentRoute.setLowEDis(lowerEDis);
                currentRoute.setDistance(totalDis);
                Intent intent;
                routeChangePosition = getIntent().getIntExtra(ROUTE_INDEX, INVALID_INDEX);
                if(routeChangePosition == -1)
                {
                    if (routeName != "") {
                        currentRoute.setName(routeName);
                        switch (mode) {
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
                    }
                    if (edit_journey == 0) {
                        CarbonModel.getInstance().getLastJourney().setRoute(currentRoute);
                        CarbonModel.getInstance().getDb().insertRowJourney(CarbonModel.getInstance().getLastJourney());
                        intent = new Intent(AddRouteActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        CarbonModel.getInstance().getJourneyCollection().getJourney(edit_journey_postition).setRoute(currentRoute);
                        CarbonModel.getInstance().getDb().updateSingleRouteInJourney((edit_journey_postition+1),currentRoute);
                        intent = new Intent(AddRouteActivity.this, DisplayTableActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
                else {
                    if(routeName.isEmpty()) {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.ENTER_ROUT_NAME),
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        currentRoute.setName(routeName);
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
                        CarbonModel.getInstance().changeRouteInJourney(tempRoute, currentRoute);
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

    private void setRouteType(){
        switch(mode){
            case 0:
                currentRoute.setType("Drive");
                break;
            case 1:
                currentRoute.setType("Public Transit");
                break;
            case 2:
                currentRoute.setType("Bike/Walk");
                break;
        }
    }

    private void extractDataFromIntent() {
        tempRoute = new Route();
        switch(mode){
            case 0:
                tempRoute = CarbonModel.getInstance().getRoute(routeChangePosition);
                lowerEDis = CarbonModel.getInstance().getRoute(routeChangePosition).getLowEDis();
                higherEDis = CarbonModel.getInstance().getRoute(routeChangePosition).getHighEDis();
                routeName = CarbonModel.getInstance().getRoute(routeChangePosition).getName();
                break;
            case 1:
                tempRoute = CarbonModel.getInstance().getBusRoute(routeChangePosition);
                lowerEDis = CarbonModel.getInstance().getBusRoute(routeChangePosition).getLowEDis();
                higherEDis = CarbonModel.getInstance().getBusRoute(routeChangePosition).getHighEDis();
                routeName = CarbonModel.getInstance().getBusRoute(routeChangePosition).getName();
                break;
            case 2:
                tempRoute = CarbonModel.getInstance().getWalkRoute(routeChangePosition);
                lowerEDis = CarbonModel.getInstance().getWalkRoute(routeChangePosition).getLowEDis();
                higherEDis = CarbonModel.getInstance().getWalkRoute(routeChangePosition).getHighEDis();
                routeName = CarbonModel.getInstance().getWalkRoute(routeChangePosition).getName();
                break;
        }
    }

}