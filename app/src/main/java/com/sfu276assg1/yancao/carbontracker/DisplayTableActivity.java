package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayTableActivity extends AppCompatActivity {
    private int rows = 3;
    private ArrayList<Journey> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_table);
        retrieveData();
        populateTable();
    }

    private void retrieveData() {
            Intent intent = getIntent();
            list = (ArrayList<Journey>)intent.getSerializableExtra("Array List");
    }

    private void populateTable() {
        TableLayout table = (TableLayout) findViewById(R.id.table);
        TableRow titleRow = new TableRow(this);
        titleRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT,
                1.0f));
        table.addView(titleRow);
        for(int i = 0; i < 3; i++) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1.0f));
            textView.setPadding(0, 0, 0, 0);
            if (i == 0) {
                textView.setText("Car");
            }
            if (i == 1) {
                textView.setText("Route");
            }
            if (i == 2) {
                textView.setText("Carbon Footprint");
            }

            titleRow.addView(textView);
        }
        for(int row = 0; row < list.size(); row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            table.addView(tableRow);
            for(int col = 0; col < 3; col++) {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));
                textView.setPadding(0, 0, 0, 0);
                if (col == 0) {
                    textView.setText(list.get(row).getCarName());
                }
                if (col == 1) {
                    textView.setText(list.get(row).getRouteName());
                }
                if (col == 2) {
                    textView.setText("" + list.get(row).getNumCarbon());
                }

                tableRow.addView(textView);
            }
        }
    }
}
