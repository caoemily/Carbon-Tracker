package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
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
import java.util.List;
import java.util.Set;

/**
 * Display bar chart of all travelled journeys in 28 days
 */

public class DisplayBarChart extends AppCompatActivity {
    BarChart barChart;
    PieChart chart;
    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    private ArrayList<Journey> journeys = new ArrayList<>();
    private ArrayList<String> nameOfEntries = new ArrayList<>();
    private ArrayList<String> nameOfEntriesDisplay = new ArrayList<>();

    private ArrayList<Float> emissions = new ArrayList<>();
    private int numberOfDaysToGoBack = 28;
    private float totalCarbonElectrical = 0;
    private float totalCarbonNaturalGas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bar_chart);
        generateData();
        generateBarChart();
        generatePieChart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_graphs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_route:
                generatePieChartInRoute();
                break;
            case R.id.action_mode:
                generatePieChart();
                break;
            case R.id.action_tree:
            default:
                break;
        }

        return true;
    }


    private void generatePieChartInRoute() {
        ArrayList<String> nameOfRoutes = new ArrayList<>();
        ArrayList<Float> emissionPerRoute = new ArrayList<>();
        for(Journey journey : journeys) {
            nameOfRoutes.add(journey.getRoute().getName());
        }
        Set<String> hs = new HashSet<>();
        hs.addAll(nameOfRoutes);
        nameOfRoutes.clear();
        nameOfRoutes.addAll(hs);

        for (int i = 0; i < nameOfRoutes.size(); i++) {
            float sumOfCarbonPerRoute = 0;
            for(int j = 0; j < journeys.size(); j++) {
                if(nameOfRoutes.get(i).equals(journeys.get(j).getRoute().getName())) {
                    String emissionString = journeys.get(j).calculateCarbon();
                    sumOfCarbonPerRoute += Float.parseFloat(emissionString);
                }
            }
            emissionPerRoute.add(sumOfCarbonPerRoute);
            if (nameOfRoutes.get(i).equals(" ")){
                nameOfRoutes.set(i, "Other");
            }
        }

        nameOfRoutes.add("Electrical");
        emissionPerRoute.add(totalCarbonElectrical);
        nameOfRoutes.add("Natural Gas");
        emissionPerRoute.add(totalCarbonNaturalGas);
        chart = (PieChart) findViewById(R.id.pieChart_28);

        List<PieEntry> yEntries = new ArrayList<>();
        for(int i = 0; i < emissionPerRoute.size(); i++) {
            yEntries.add(new PieEntry(emissionPerRoute.get(i), nameOfRoutes.get(i)));
        }


        PieDataSet dataSet = new PieDataSet(yEntries, "");
        dataSet.setSelectionShift(5f);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.5f);
        dataSet.setValueLinePart2Length(.1f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);


        dataSet.setSliceSpace(5);
        dataSet.setValueTextSize(12);
        //need to fix the colors!
        dataSet.setColors(generateColorsForGraph());
        PieData data = new PieData(dataSet);
//        data.setValueFormatter(new PercentFormatter());

        Description description = new Description();
//        chart.setDrawSliceText(false);
//        chart.setUsePercentValues(true);
        chart.setDescription(null);
        chart.setRotationEnabled(true);
        chart.setHoleRadius(25f);
        chart.setTransparentCircleAlpha(0);
        chart.setData(data);
        chart.animateY(2000);
        chart.invalidate();

    }

    private void generatePieChart() {
        chart = (PieChart) findViewById(R.id.pieChart_28);
        List<PieEntry> yEntries = new ArrayList<>();
        for(int i = 0; i < emissions.size(); i++) {
            yEntries.add(new PieEntry(emissions.get(i), nameOfEntriesDisplay.get(i)));
        }

        PieDataSet dataSet = new PieDataSet(yEntries, "");
        dataSet.setSliceSpace(5);
        dataSet.setValueTextSize(12);
        dataSet.setColors(generateColorsForGraph());
        PieData data = new PieData(dataSet);

        Description description = new Description();
        chart.setDescription(null);
        chart.setRotationEnabled(true);
        chart.setHoleRadius(25f);
        chart.setTransparentCircleAlpha(0);
        chart.setData(data);
        chart.animateY(2000);
        chart.invalidate();
    }

    private void generateBarChart() {
        barChart = (BarChart) findViewById(R.id.bargraph);

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

        for(int i = 0; i < nameOfEntries.size(); i++) {
            float sumOfCarbon = 0;
            if (!nameOfEntries.get(i).equals("Bike/Walk") && !nameOfEntries.get(i).equals("Public Transit")) {
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
        nameOfEntries.add("Electrical");
        emissions.add(totalCarbonElectrical);

        nameOfEntries.add("Natural Gas");
        emissions.add(totalCarbonNaturalGas);


        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i = 0; i < emissions.size(); i++) {
            barEntries.add(new BarEntry(i, emissions.get(i)));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setColors(generateColorsForGraph());

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.4f);
        data.setValueFormatter(new LargeValueFormatter());
        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.setTouchEnabled(true);
        barChart.animateXY(2000, 2000);
        Description description = new Description();
        description.setText("CO2 in the past month");
        barChart.setDescription(description);
        barChart.invalidate();

        for(String name : nameOfEntries) {
            if(!name.equals("Bike/Walk") && !name.equals("Public Transit")) {
                String[] splitName = name.split(",");
                nameOfEntriesDisplay.add(splitName[0]);
            }else {
                nameOfEntriesDisplay.add(name);
            }
        }

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
            cal.add(Calendar.DAY_OF_MONTH, -numberOfDaysToGoBack);
            today28 = cal.getTime();
            for(int i = 0; i < journeyCollection.countJourneys(); i++) {
                Date date = df.parse(journeyCollection.getJourney(i).getDate());
                if(!(date.before(today28) || date.after(today))) {
                    journeys.add(journeyCollection.getJourney(i));
                }
            }
            for(int j = 0; j <= numberOfDaysToGoBack; j++) {
                cal.setTime(today);
                cal.add(Calendar.DAY_OF_MONTH, -j);
                Date currentDay = cal.getTime();
                String currentDayInString = df.format(currentDay);
                float currentDayCarbonElectrical = (float)CarbonModel.getInstance().getBillCollection().getElectricityCarbonEmission(currentDayInString);
                totalCarbonElectrical += currentDayCarbonElectrical;
                float currentDayCarbonGas = (float)CarbonModel.getInstance().getBillCollection().getGasCarbonEmission(currentDayInString);
                totalCarbonNaturalGas += currentDayCarbonGas;
            }
        }catch (ParseException e) {

        }
    }

    private ArrayList<Integer> generateColorsForGraph() {
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        return colors;
    }
}