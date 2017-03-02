package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //ArrayList<Journey> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        generateArrayList();

        setUpAddJourneytButton();
        setUpViewDataButton();
    }

//    private void generateArrayList() {
//        for(int i = 1; i < 6; i++) {
//            list.add(new Journey(i, "Honda " + i, "This is routes " + i));
//        }
//        for(Journey journey : list) {
//            Log.d("DEBUGGG", journey.toString());
//        }
//    }
//
    private void setUpAddJourneytButton() {
        Button button = (Button) findViewById(R.id.addJourneyBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectRouteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpViewDataButton() {
        Button button = (Button) findViewById(R.id.viewDataBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayCarbonFootprintActivity.class);
//                intent.putExtra("Array List", list);
                startActivity(intent);
            }
        });
    }
}
