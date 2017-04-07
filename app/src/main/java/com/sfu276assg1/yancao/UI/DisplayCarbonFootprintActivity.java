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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.Journey;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Display pie chart of all travelled journeys.
 */

public class DisplayCarbonFootprintActivity extends AppCompatActivity {

    private int year_x;
    private int month_x;
    private int date_x;
    static final int DIALOG_ID = 0;

    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    private ArrayList<Journey> journeys = new ArrayList<>();
    private String chosenDate;
    private float carbonForUtilitiesElectrical;
    private float carbonForUtilitiesGas;
    private int unitChose = 0;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_carbon_footprint);

        getSupportActionBar().setTitle(R.string.action_graph);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        generateRealTimeCalendar();
        setUpBottomNavigation();
        generateInfoForChart();
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
                        intent = new Intent(DisplayCarbonFootprintActivity.this, DisplayBarChart.class);
                        intent.putExtra("today", today);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.show_year_chart:
                        String todayNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        intent = new Intent(DisplayCarbonFootprintActivity.this, DisplayLineChart.class);
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
            Intent intent = new Intent(DisplayCarbonFootprintActivity.this, DisplayCarbonFootprintActivity.class);
            intent.putExtra("single date selected", dateSelected);
            intent.putExtra("mode", 0);
            startActivity(intent);
        }
    };

    private void generateInfoForChart() {
        Intent intent = getIntent();
        chosenDate = intent.getStringExtra("single date selected");
        unitChose = CarbonModel.getInstance().getUnitChoice();
        if(intent.getIntExtra("mode", 0) == 0) {
            for(int i = 0; i < journeyCollection.countJourneys(); i++) {
                if(journeyCollection.getJourney(i).getDate().equals(chosenDate)) {
                    journeys.add(journeyCollection.getJourney(i));
                }
            }
        }
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
                    double emission = 0;
                    if(unitChose != 0) {
                        emission = journeys.get(j).calculateCarbonDouble();
                    }else{
                        emission = journeys.get(j).calculateCarbonTreeYearDouble();
                    }
                    sumOfCarbonPerRoute += (float)emission;
                }
            }
            emissionPerRoute.add(sumOfCarbonPerRoute);
            if (nameOfRoutes.get(i).equals(" ")){
                nameOfRoutes.set(i, getString(R.string.other));
            }
        }
        nameOfRoutes.add(getString(R.string.electrical));
        emissionPerRoute.add(carbonForUtilitiesElectrical);
        nameOfRoutes.add(getString(R.string.natural_gas));
        emissionPerRoute.add(carbonForUtilitiesGas);
        List<PieEntry> yEntries = new ArrayList<>();
        for(int i = 0; i < emissionPerRoute.size(); i++) {
            yEntries.add(new PieEntry(emissionPerRoute.get(i), nameOfRoutes.get(i)));
        }
        PieDataSet dataSet = new PieDataSet(yEntries, "");
        dataSet.setSliceSpace(5);
        dataSet.setValueTextSize(12);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        PieChart chart = (PieChart) findViewById(R.id.Chart);
        Description description = new Description();
        description.setText("Amount of Carbon per Car");
        chart.setEntryLabelColor(Color.DKGRAY);
        chart.setDescription(null);
        chart.setRotationEnabled(true);
        chart.setHoleRadius(25f);
        chart.setTransparentCircleAlpha(0);
        chart.setData(data);
        Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setEnabled(true);
        chart.animateY(2000);
        chart.invalidate();
    }

    private void generatePieChart() {
        List<PieEntry> yEntries = new ArrayList<>();
        List<String> xEntries = new ArrayList<>();
        if(unitChose != 0) {
            carbonForUtilitiesElectrical = (float) CarbonModel.getInstance().getBillCollection().getElectricityCarbonEmission(chosenDate);
            carbonForUtilitiesGas = (float) CarbonModel.getInstance().getBillCollection().getGasCarbonEmission(chosenDate);
        }else{
            carbonForUtilitiesElectrical = (float) CarbonModel.getInstance().getBillCollection().getElectricityCarbonEmissionTreeYear(chosenDate);
            carbonForUtilitiesGas = (float) CarbonModel.getInstance().getBillCollection().getGasCarbonEmissionTreeYear(chosenDate);
        }
        if (!journeys.isEmpty()) {
            for (int i = 0; i < journeys.size(); i++) {
                if (journeys.get(i).getRoute().getType().equals("Drive")) {
                    if(unitChose != 0) {
                        yEntries.add(new PieEntry((float) (journeys.get(i).calculateCarbonDouble()), journeys.get(i).getCar().getMake()));
                    }else{
                        yEntries.add(new PieEntry((float)(journeys.get(i).calculateCarbonTreeYearDouble()), journeys.get(i).getCar().getMake()));
                    }
                    xEntries.add(journeyCollection.getJourney(i).getCar().getMake());
                }else{
                    if(unitChose != 0) {
                        yEntries.add(new PieEntry((float)(journeys.get(i).calculateCarbonDouble()), journeys.get(i).getRoute().getType()));
                    }else{
                        yEntries.add(new PieEntry((float)(journeys.get(i).calculateCarbonTreeYearDouble()), journeys.get(i).getRoute().getType()));

                    }
                    xEntries.add(journeyCollection.getJourney(i).getRoute().getType());
                }
            }
            yEntries.add(new PieEntry(carbonForUtilitiesElectrical, getString(R.string.electrical)));
            xEntries.add(getString(R.string.electrical));
            yEntries.add(new PieEntry(carbonForUtilitiesGas, getString(R.string.natural_gas)));
            xEntries.add(getString(R.string.natural_gas));
        }else{
            yEntries.add(new PieEntry(carbonForUtilitiesElectrical, getString(R.string.electrical)));
            xEntries.add(getString(R.string.electrical));
            yEntries.add(new PieEntry(carbonForUtilitiesGas, getString(R.string.natural_gas)));
            xEntries.add(getString(R.string.natural_gas));
        }

        PieDataSet dataSet = new PieDataSet(yEntries, "");
        dataSet.setSliceSpace(5);
        dataSet.setValueTextSize(12);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        PieChart chart = (PieChart) findViewById(R.id.Chart);
        Description description = new Description();
        description.setText("Amount of Carbon per Car");
        chart.setEntryLabelColor(Color.DKGRAY);
        chart.setDescription(null);
        chart.setRotationEnabled(true);
        chart.setHoleRadius(25f);
        chart.setTransparentCircleAlpha(0);
        chart.setData(data);
        Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);
        chart.animateY(2000);
        chart.invalidate();
    }
}