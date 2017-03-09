package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Display pie chart of all travelled journeys.
 */

public class DisplayCarbonFootprintActivity extends AppCompatActivity {

    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_carbon_footprint);

        generatePieChart();
        setUpShowPieChartButton();
    }

    private void setUpShowPieChartButton() {
        Button button = (Button) findViewById(R.id.showTable);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayCarbonFootprintActivity.this, DisplayTableActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void generatePieChart() {
        List<PieEntry> yEntries = new ArrayList<>();
        List<String> xEntries = new ArrayList<>();
        for(int i = 0; i < journeyCollection.countJourneys(); i++) {
            yEntries.add(new PieEntry((float)journeyCollection.getJourney(i).getNumCarbon(),journeyCollection.getJourney(i).getCarName()));
        }

        for(int i = 0; i < journeyCollection.countJourneys(); i++) {
            xEntries.add(journeyCollection.getJourney(i).getCarName());
        }


        PieDataSet dataSet = new PieDataSet(yEntries, "Car Type");
        dataSet.setSliceSpace(2);
        dataSet.setValueTextSize(12);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        PieChart chart = (PieChart)findViewById(R.id.Chart);
        Description description = new Description();
        description.setText("Amount of Carbon per Car");
        chart.setDescription(description);
        chart.setRotationEnabled(true);
        chart.setHoleRadius(25f);
        chart.setTransparentCircleAlpha(0);
        chart.setData(data);
        chart.animateY(2000);
        chart.invalidate();
    }
}
