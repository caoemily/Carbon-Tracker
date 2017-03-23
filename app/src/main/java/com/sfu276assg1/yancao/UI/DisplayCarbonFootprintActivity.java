package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Display pie chart of all travelled journeys.
 */

public class DisplayCarbonFootprintActivity extends AppCompatActivity {

    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    private ArrayList<Journey> journeys = new ArrayList<>();
    private String chosenDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_carbon_footprint);

        testFindDate();
        generateInfoForChart();
        generatePieChart();
        setUpShowTabletButton();

    }

    private void testFindDate() {

        ArrayList<Date> dates = new ArrayList<>();
        ArrayList<Date> dateInRange = new ArrayList<>();
        String dt = "2017-01-01";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDay;
        try {
            startDay = sdf.parse(dt);
            dates.add(startDay);
        }catch (ParseException e) {

        }

        for(int i = 1; i < 30; i++) {
            DateTime dtOrg = new DateTime(dt);
            DateTime dtPlusOne = dtOrg.plusDays(i);
            dates.add(dtPlusOne.toDate());
        }

        String dateInString = "2017-01-23";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date today;
        Date today30;
        try {
            today = df.parse(dateInString);
            Calendar cal = new GregorianCalendar();
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_MONTH, -28);
            today30 = cal.getTime();
            Log.d("DEBUG TODAY 30", "" + today30);
            for(Date date : dates) {
                if(!(date.before(today30) || date.after(today))) {
                    dateInRange.add(date);
                }
            }
        }catch (ParseException e) {

        }

        //Dates from Jan 1 - Jan 30
        for(Date date : dates) {
            Log.d("DEBUGGGGGGGGGGGGGGGGGG", "" + date);
        }

        //in range from Dec 26 - Jan 23
        for(Date date : dateInRange) {
            Log.d("DEBUGG DATE IN RANGE", "" + date);
        }


    }

    private void generateInfoForChart() {
        Intent intent = getIntent();
        chosenDate = intent.getStringExtra("single date selected");
        //Log.d("Debugggg", chosenDate);
        if(intent.getIntExtra("mode", 0) == 0) {
            for(int i = 0; i < journeyCollection.countJourneys(); i++) {
                if(journeyCollection.getJourney(i).getDate().equals(chosenDate)) {
                    journeys.add(journeyCollection.getJourney(i));
                }
            }
        }
    }

    private void setUpShowTabletButton() {
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

//    private void generatePieChart() {
//        List<PieEntry> yEntries = new ArrayList<>();
//        List<String> xEntries = new ArrayList<>();
//        for(int i = 0; i < journeyCollection.countJourneys(); i++) {
//            yEntries.add(new PieEntry(Float.valueOf(journeyCollection.getJourney(i).calculateCarbon()),journeyCollection.getJourney(i).getCar().getNickname()));
//        }
//
//        for(int i = 0; i < journeyCollection.countJourneys(); i++) {
//            xEntries.add(journeyCollection.getJourney(i).getCar().getNickname());
//        }
//
//        PieDataSet dataSet = new PieDataSet(yEntries, "Car Type");
//        dataSet.setSliceSpace(2);
//        dataSet.setValueTextSize(12);
//        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        PieData data = new PieData(dataSet);
//
//        PieChart chart = (PieChart)findViewById(R.id.Chart);
//        Description description = new Description();
//        description.setText("Amount of Carbon per Car");
//        chart.setDescription(description);
//        chart.setRotationEnabled(true);
//        chart.setHoleRadius(25f);
//        chart.setTransparentCircleAlpha(0);
//        chart.setData(data);
//        chart.animateY(2000);
//        chart.invalidate();
//    }

    private void generatePieChart() {
        if (!journeys.isEmpty()) {
            List<PieEntry> yEntries = new ArrayList<>();
            List<String> xEntries = new ArrayList<>();
            for (int i = 0; i < journeys.size(); i++) {
                yEntries.add(new PieEntry(Float.valueOf(journeys.get(i).calculateCarbon()), journeys.get(i).getCar().getMake()));
            }

            for (int i = 0; i < journeys.size(); i++) {
                xEntries.add(journeyCollection.getJourney(i).getCar().getMake());
            }
            float carbonForUtilities = (float) CarbonModel.getInstance().getBillCollection().getTotalCarbonEmission(chosenDate);
            yEntries.add(new PieEntry(carbonForUtilities, "Utilities"));
            xEntries.add("Utilities");
            PieDataSet dataSet = new PieDataSet(yEntries, "Car Type");
            dataSet.setSliceSpace(2);
            dataSet.setValueTextSize(12);
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData data = new PieData(dataSet);

            PieChart chart = (PieChart) findViewById(R.id.Chart);
            Description description = new Description();
            description.setText("Amount of Carbon per Car");
            chart.setDescription(description);
            chart.setRotationEnabled(true);
            chart.setHoleRadius(25f);
            chart.setTransparentCircleAlpha(0);
            chart.setData(data);
            chart.animateY(2000);
            chart.invalidate();
        }else{
            Toast.makeText(getApplicationContext(), "No Journey", Toast.LENGTH_LONG).show();
        }
    }
}