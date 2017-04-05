package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.Journey;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Display pie chart of all travelled journeys.
 */

public class DisplayCarbonFootprintActivity extends AppCompatActivity {
    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    private ArrayList<Journey> journeys = new ArrayList<>();
    private String chosenDate;
    private float carbonForUtilitiesElectrical;
    private float carbonForUtilitiesGas;
    private int unitChose = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_carbon_footprint);
        generateInfoForChart();
        generatePieChart();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SelectGraphActivity.class);
        startActivity(intent);
        finish();
    }
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
        chart.setDescription(null);
        chart.setRotationEnabled(true);
        chart.setHoleRadius(25f);
        chart.setTransparentCircleAlpha(0);
        chart.setData(data);
        chart.animateY(2000);
        chart.invalidate();
    }

    private void generatePieChart() {
        List<PieEntry> yEntries = new ArrayList<>();
        List<String> xEntries = new ArrayList<>();
        //NEED TO CHANGE TO TREE!!!!!!!
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
                        yEntries.add(new PieEntry(Float.valueOf(journeys.get(i).calculateCarbon()), journeys.get(i).getCar().getMake()));
                    }else{
                        yEntries.add(new PieEntry(Float.valueOf(journeys.get(i).calculateCarbonTreeYear()), journeys.get(i).getCar().getMake()));
                    }
                    xEntries.add(journeyCollection.getJourney(i).getCar().getMake());
                }else{
                    if(unitChose != 0) {
                        yEntries.add(new PieEntry(Float.valueOf(journeys.get(i).calculateCarbon()), journeys.get(i).getRoute().getType()));
                    }else{
                        yEntries.add(new PieEntry(Float.valueOf(journeys.get(i).calculateCarbonTreeYear()), journeys.get(i).getRoute().getType()));

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
        chart.setDescription(null);
        chart.setRotationEnabled(true);
        chart.setHoleRadius(25f);
        chart.setTransparentCircleAlpha(0);
        chart.setData(data);
        chart.animateY(2000);
        chart.invalidate();
    }
}