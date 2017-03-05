package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpAddJourneytButton();
        setUpViewDataButton();

    }

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
                Intent intent = new Intent(MainActivity.this, DisplayTableActivity.class);
                startActivity(intent);
            }
        });
    }
}
