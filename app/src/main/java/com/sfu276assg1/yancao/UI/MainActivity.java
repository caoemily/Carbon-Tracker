package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.sfu276assg1.yancao.carbontracker.R;

/**
 * Choose to add journey OR show carbon footprint in table or chart
 */

public class MainActivity extends AppCompatActivity {

    public static DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDatabase();

        setUpAddJourneytButton();
        setUpShowTableButton();
        setUpShowChartButton();
    }

    private void setupDatabase() {
        db = new DBAdapter(getApplicationContext());
        db.open();
        CarbonModel.getInstance().setJourneyCollection(db.getJourneyList());
        CarbonModel.getInstance().setCarCollection(db.getCarList());
    }

    private void setUpAddJourneytButton() {
        ImageButton button = (ImageButton) findViewById(R.id.addJourneyBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectTranModeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setUpShowTableButton() {
        ImageButton button = (ImageButton) findViewById(R.id.showTableBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayTableActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpShowChartButton() {
        ImageButton button = (ImageButton) findViewById(R.id.showChartBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayCarbonFootprintActivity.class);
                startActivity(intent);
            }
        });
    }
}