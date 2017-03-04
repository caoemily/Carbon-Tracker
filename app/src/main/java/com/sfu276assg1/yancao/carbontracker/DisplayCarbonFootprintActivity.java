package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class DisplayCarbonFootprintActivity extends AppCompatActivity {

    private ArrayList<Journey> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_carbon_footprint);

        generateDataForChart();
        generatePieChart();
    }

   /* private void generatePieChart() {
        List<PieEntry> yEntries = new ArrayList<>();
        List<String> xEntries = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            yEntries.add(new PieEntry(list.get(i).getNumCarbon(), list.get(i).getCarName()));
        }

        for(int i = 0; i < list.size(); i++) {
            xEntries.add(list.get(i).getCarName());
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

    private void generateDataForChart() {
        Intent intent = getIntent();
        list = (ArrayList<Journey>)intent.getSerializableExtra("Array List");
    }*/
}
