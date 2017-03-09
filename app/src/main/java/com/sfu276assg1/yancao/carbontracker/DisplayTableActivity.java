package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class DisplayTableActivity extends AppCompatActivity {

    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_table);

        populateTable();
        setUpShowPieChartButton();
    }

    private void setUpShowPieChartButton() {
        Button button = (Button) findViewById(R.id.showPieChart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayTableActivity.this, DisplayCarbonFootprintActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void populateTable() {
        TableLayout table = (TableLayout) findViewById(R.id.table);
        TableRow titleRow = new TableRow(this);
        titleRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT,
                1.0f));
        table.addView(titleRow);
        for(int i = 0; i < 5; i++) {
            TextView textView = new TextView(this);
            textView.setTextSize(10);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1.0f);
            params.setMargins(1,1,1,1);
            textView.setLayoutParams(params);
            textView.setBackgroundColor(Color.GRAY);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            textView.setPadding(50, 50, 50, 50);
            if (i == 0) {
                textView.setText("Date");
            }
            if (i == 1) {
                textView.setText("Route");
            }
            if (i == 2) {
                textView.setText("Distance" + " (km)");
            }
            if (i == 3) {
                textView.setText("Car");
            }
            if (i == 4) {
                textView.setText("Carbon Footprint" + " (kg)");
            }
            titleRow.addView(textView);
        }
        for(int row = 0; row < journeyCollection.countJourneys(); row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            table.addView(tableRow);
            for(int col = 0; col < 5; col++) {
                TextView textView = new TextView(this);
                textView.setTextSize(10);
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f);
                params.setMargins(1,1,1,1);
                textView.setLayoutParams(params);
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                textView.setPadding(50, 50, 50, 50);
                if (col == 0) {
                    textView.setText(journeyCollection.getJourney(row).getDate());
                }
                if (col == 1) {
                    textView.setText(journeyCollection.getJourney(row).getRouteName());
                }
                if (col == 2) {
                    textView.setText("" + journeyCollection.getJourney(row).getDistance());
                }
                if (col == 3) {
                    textView.setText(journeyCollection.getJourney(row).getCarName());
                }
                if (col == 4) {
                    textView.setText("" + journeyCollection.getJourney(row).getNumCarbon());
                }

                tableRow.addView(textView);
            }
        }
    }
}
