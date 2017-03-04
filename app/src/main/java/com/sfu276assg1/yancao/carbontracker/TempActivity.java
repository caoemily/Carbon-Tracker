package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        setLaunchMain();
        addJourney();
        populatePotListView();

    }

    private void addJourney() {
        Car car = CarbonModel.getInstance().getLastCarInList();
        if(car == null){
            car = new Car("myCar");
        }
        Route route = CarbonModel.getInstance().getLastRoute();
        Journey journey = new Journey(car,route);
        CarbonModel.getInstance().addJourney(journey);
    }


    private void setLaunchMain() {
        Button newRouteBtn = (Button) findViewById(R.id.btn_backToMain);
        newRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void populatePotListView() {
        JourneyCollection jc= CarbonModel.getInstance().getJourneyCollection();;
        ArrayAdapter<String> routeAdapter = new ArrayAdapter<String>
                (this,R.layout.routedescription,
                        jc.getJourneyDescriptions());
        final ListView routeList = (ListView) findViewById(R.id.listView_journey);
        routeList.setAdapter(routeAdapter);
    }
}

