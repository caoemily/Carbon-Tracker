package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.DBAdapter;
import com.sfu276assg1.yancao.carbontracker.R;

/**
 * Choose to add journey OR show carbon footprint in table or chart
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDatabase();
        setUpAddJourneytButton();
        setUpShowTableButton();
        setUpShowChartButton();
        setUpUtilitiesButton();
    }

    private void setupDatabase() {
        DBAdapter db = new DBAdapter(getApplicationContext());
        db.open();
        CarbonModel.getInstance().setDb(db);
        CarbonModel.getInstance().setJourneyCollection(CarbonModel.getInstance().getDb().getJourneyList());
        CarbonModel.getInstance().setCarCollection(CarbonModel.getInstance().getDb().getCarList());
        CarbonModel.getInstance().setRouteCollection(CarbonModel.getInstance().getDb().getRouteList());
        CarbonModel.getInstance().setBusRouteCollection(CarbonModel.getInstance().getDb().getBusRouteList());
        CarbonModel.getInstance().setWalkRouteCollection(CarbonModel.getInstance().getDb().getWalkRouteList());
        CarbonModel.getInstance().setBillCollection(CarbonModel.getInstance().getDb().getBillList());
    }

    private void setUpAddJourneytButton() {
        ImageButton button = (ImageButton) findViewById(R.id.addJourneyBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectTransModeActivity.class);
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
                Intent intent = new Intent(MainActivity.this, DisplayTable.class);
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

    private void setUpUtilitiesButton() {
        ImageButton button = (ImageButton) findViewById(R.id.utilitiesBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MonthlyUtilitiesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
