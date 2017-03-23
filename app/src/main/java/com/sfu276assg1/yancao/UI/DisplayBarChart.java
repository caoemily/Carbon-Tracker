package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.Journey;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

public class DisplayBarChart extends AppCompatActivity {
    BarChart barChart;
    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    private ArrayList<Journey> journeys = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bar_chart);
        generateData();
        generateBarChart();
    }

    private void generateBarChart() {
        barChart = (BarChart) findViewById(R.id.bargraph);

        ArrayList<String> nameOfEntries = new ArrayList<>();
        ArrayList<Float> emissions = new ArrayList<>();
        for(Journey journey : journeys) {
            if(!journey.getCar().getNickname().equals(" ")) {
                nameOfEntries.add(journey.getCar().toString());
            }else{
                nameOfEntries.add(journey.getRoute().getType());
            }
        }

        Set<String> hs = new HashSet<>();
        hs.addAll(nameOfEntries);
        nameOfEntries.clear();
        nameOfEntries.addAll(hs);
        for(String name : nameOfEntries) {
            Log.d("DEBUGGGGGG", name);
        }
        for(int i = 0; i < nameOfEntries.size(); i++) {
            float sumOfCarbon = 0;
            if (!nameOfEntries.get(i).equals("walk") && !nameOfEntries.get(i).equals("public")) {
                for (int j = 0; j < journeys.size(); j++) {
                    if (nameOfEntries.get(i).equals(journeys.get(j).getCar().toString())){
                        String emissionString = journeys.get(j).calculateCarbon();
                        sumOfCarbon += Float.parseFloat(emissionString);
                    }
                }
            }else{
                for (int y = 0; y < journeys.size(); y++) {
                    if(nameOfEntries.get(i).equals(journeys.get(y).getRoute().getType())){
                        String emissionString = journeys.get(y).calculateCarbon();
                        sumOfCarbon += Float.parseFloat(emissionString);
                    }
                }
            }
            emissions.add(sumOfCarbon);
        }
        for (Float emission : emissions) {
            Log.d("DEBUGGGG", "" + emission);
        }

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i = 0; i < emissions.size(); i++) {
            barEntries.add(new BarEntry(i, emissions.get(i)));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Carbon Producers");
        barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.4f);
        data.setValueFormatter(new LargeValueFormatter());
        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.setTouchEnabled(true);
        barChart.animateXY(2000, 2000);
        Description description = new Description();
        description.setText("CO2 in the past 28 days");
        barChart.setDescription(description);
        barChart.invalidate();

        final ArrayList<String> nameOfEntriesDisplay = new ArrayList<>();
        for(String name : nameOfEntries) {
            if(!name.equals("walk") && !name.equals("public")) {
                String[] splitName = name.split(",");
                nameOfEntriesDisplay.add(splitName[0]);
            }else {
                nameOfEntriesDisplay.add(name);
            }
        }

        for(String name : nameOfEntriesDisplay) {
            Log.d("DEBUGGG", name);

        }
        Log.d("SIZEEEE", "" + nameOfEntriesDisplay.size());
        final XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum((float) nameOfEntriesDisplay.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(nameOfEntriesDisplay));

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);



    }

    private void generateData() {
        Intent intent = getIntent();
        String dateInString = intent.getStringExtra("today");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date today;
        Date today28;
        try {
            today = df.parse(dateInString);
            Calendar cal = new GregorianCalendar();
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_MONTH, -28);
            today28 = cal.getTime();
            Log.d("DEBUG TODAY 30", "" + today28);
            for(int i = 0; i < journeyCollection.countJourneys(); i++) {
                Date date = df.parse(journeyCollection.getJourney(i).getDate());
                if(!(date.before(today28) || date.after(today))) {
                    journeys.add(journeyCollection.getJourney(i));
                }
            }
        }catch (ParseException e) {

        }

        for(Journey journey : journeys) {
            Log.d("DEBUGG DATE IN RANGE", "" + journey.getCar().getMake());
        }
    }

}