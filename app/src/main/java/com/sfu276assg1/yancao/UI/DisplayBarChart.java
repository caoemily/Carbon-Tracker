package com.sfu276assg1.yancao.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
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
    private int year_x;
    private int month_x;
    private int date_x;
    static final int DIALOG_ID = 0;

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
    private int unitChose = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bar_chart);

        getSupportActionBar().setTitle(R.string.action_graph);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        generateRealTimeCalendar();
        setUpBottomNavigation();
        generateData();
        generateBarChart();
        generatePieChart();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocas) {
        super.onWindowFocusChanged(hasFocas);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_graphs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_route:
                generatePieChartInRoute();
                return true;
            case R.id.action_mode:
                generatePieChart();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpBottomNavigation() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_chart);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.show_day_chart:
                        showDialog(DIALOG_ID);
                        break;
                    case R.id.show_month_chart:
                        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        intent = new Intent(DisplayBarChart.this, DisplayBarChart.class);
                        intent.putExtra("today", today);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.show_year_chart:
                        String todayNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        intent = new Intent(DisplayBarChart.this, DisplayLineChart.class);
                        intent.putExtra("today 365", todayNow);
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generateRealTimeCalendar() {
        final android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
        year_x = cal.get(android.icu.util.Calendar.YEAR);
        month_x = cal.get(android.icu.util.Calendar.MONTH);
        date_x = cal.get(android.icu.util.Calendar.DATE);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_ID) {
            return new DatePickerDialog(this, datePickerListener, year_x, month_x, date_x);
        }else{
            return null;
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month;
            date_x = dayOfMonth;
            String monthSelected = String.format("%02d", month + 1);
            String daySelected = String.format("%02d", dayOfMonth);
            String dateSelected = "" + year + "-" + monthSelected+ "-" + daySelected;
            Intent intent = new Intent(DisplayBarChart.this, DisplayCarbonFootprintActivity.class);
            intent.putExtra("single date selected", dateSelected);
            intent.putExtra("mode", 0);
            startActivity(intent);
        }
    };

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
                    String emissionString = "";
                    if(unitChose != 0) {
                        emissionString = journeys.get(j).calculateCarbon();
                    }else{
                        emissionString = journeys.get(j).calculateCarbonTreeYear();
                    }
                    sumOfCarbonPerRoute += Float.parseFloat(emissionString);
                }
            }
            emissionPerRoute.add(sumOfCarbonPerRoute);
            if (nameOfRoutes.get(i).equals(" ")){
                nameOfRoutes.set(i, getString(R.string.other));
            }
        }
        nameOfRoutes.add(getString(R.string.electrical));
        emissionPerRoute.add(totalCarbonElectrical);
        nameOfRoutes.add(getString(R.string.natural_gas));
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

        Description description = new Description();
        chart.setEntryLabelColor(Color.DKGRAY);
        chart.setDescription(null);
        chart.setRotationEnabled(true);
        chart.setHoleRadius(25f);
        chart.setTransparentCircleAlpha(0);
        chart.setData(data);
        chart.animateY(2000);
        chart.invalidate();
        Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);

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
        chart.setEntryLabelColor(Color.DKGRAY);
        chart.setDescription(null);
        chart.setRotationEnabled(true);
        chart.setHoleRadius(25f);
        chart.setTransparentCircleAlpha(0);
        chart.setData(data);
        chart.animateY(2000);
        chart.invalidate();

        Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);
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
                        double emission = 0;
                        if(unitChose != 0) {
                            emission = journeys.get(j).calculateCarbonDouble();
                        }else{
                            emission = journeys.get(j).calculateCarbonTreeYearDouble();
                        }
                        sumOfCarbon += (float)(emission);
                    }
                }
            }else{
                for (int y = 0; y < journeys.size(); y++) {
                    if(nameOfEntries.get(i).equals(journeys.get(y).getRoute().getType())){
                        double emission = 0;
                        if(unitChose != 0) {
                            emission = journeys.get(y).calculateCarbonDouble();
                        }else{
                            emission = journeys.get(y).calculateCarbonTreeYearDouble();
                        }
                        sumOfCarbon += (float)(emission);
                    }
                }
            }
            emissions.add(sumOfCarbon);
        }
        nameOfEntries.add(getString(R.string.electrical));
        emissions.add(totalCarbonElectrical);
        nameOfEntries.add(getString(R.string.natural_gas));
        emissions.add(totalCarbonNaturalGas);


        ArrayList<BarEntry> barEntries = new ArrayList<>();
        float[] emissionInFloat = new float[emissions.size()];
        for(int i = 0; i < emissions.size(); i++) {
            emissionInFloat[i] = emissions.get(i);
        }
        barEntries.add(new BarEntry(0, emissionInFloat));
        if (unitChose != 0) {
            barEntries.add(new BarEntry(2, (float) 36.2 * 30));
            barEntries.add(new BarEntry(4, (float) ((36.2 * 30) * 70) / 100));
        }else{
            barEntries.add(new BarEntry(2, (float) (36.2 * 30) / 20));
            barEntries.add(new BarEntry(4, (float) (((36.2 * 30) * 70) / 100) / 20));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setColors(generateColorsForGraph());


        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.8f);
        data.setValueFormatter(new LargeValueFormatter());
        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.setTouchEnabled(true);
        barChart.animateXY(2000, 2000);
        barChart.setDescription(null);
        barChart.invalidate();

        for(String name : nameOfEntries) {
            if(!name.equals("Bike/Walk") && !name.equals("Public Transit")) {
                String[] splitName = name.split(",");
                nameOfEntriesDisplay.add(splitName[0]);
            }else {
                if(name.equals("Public Transit")) {
                    nameOfEntriesDisplay.add(getString(R.string.public_transit_trans));
                } else {
                    nameOfEntriesDisplay.add(name);
                }
            }
        }

        List<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < nameOfEntriesDisplay.size(); i++) {
            LegendEntry entry = new LegendEntry();
            int color = generateColorsForGraph().get(i);
            entry.formColor = color;
            entry.label = nameOfEntriesDisplay.get(i);
            legendEntries.add(entry);
        }
        Legend legend = barChart.getLegend();
        legend.setCustom(legendEntries);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setFormSize(8f);
        legend.setFormToTextSpace(4f);
        legend.setXEntrySpace(6f);
        legend.setWordWrapEnabled(true);

        ArrayList<String> labels = new ArrayList<>();
        labels.add(getString(R.string.total_in_month));
        labels.add(" ");
        labels.add(getString(R.string.average_canadian));
        labels.add(" ");
        labels.add(getString(R.string.target_co2));
        final XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum((float) 5);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void generateData() {
        Intent intent = getIntent();
        unitChose = CarbonModel.getInstance().getUnitChoice();
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
                float currentDayCarbonElectrical = 0;
                float currentDayCarbonGas = 0;
                if(unitChose != 0) {
                    currentDayCarbonElectrical = (float) CarbonModel.getInstance().getBillCollection().getElectricityCarbonEmission(currentDayInString);
                    currentDayCarbonGas = (float) CarbonModel.getInstance().getBillCollection().getGasCarbonEmission(currentDayInString);
                }else{
                    currentDayCarbonElectrical = (float) CarbonModel.getInstance().getBillCollection().getElectricityCarbonEmissionTreeYear(currentDayInString);
                    currentDayCarbonGas = (float) CarbonModel.getInstance().getBillCollection().getGasCarbonEmissionTreeYear(currentDayInString);
                }
                totalCarbonElectrical += currentDayCarbonElectrical;
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