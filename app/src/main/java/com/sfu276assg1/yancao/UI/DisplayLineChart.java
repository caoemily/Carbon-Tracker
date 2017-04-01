package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.DataForOneMonth;
import com.sfu276assg1.yancao.carbontracker.Journey;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Display line chart for Carbon emissions in 12 months.
 */
public class DisplayLineChart extends AppCompatActivity {

    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    private ArrayList<Journey> journeys = new ArrayList<>();
    private ArrayList<DataForOneMonth> dataForYear = new ArrayList<>();
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_line_chart);

        generateDataForLineChart();
        generateLineChart();
        TextView textView = (TextView) findViewById(R.id.lineChartDes);
        textView.setText("Every point on the line represent 1 month, starting from current month." + "\n" +
                        "Please click on the point to view each month in detail");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SelectGraphActivity.class);
        startActivity(intent);
        finish();
    }

    private void generateLineChart() {
        lineChart = (LineChart) findViewById(R.id.lineChart);

        ArrayList<String> monthToDisplay = new ArrayList<>();
        ArrayList<Entry> yAxisCar = new ArrayList<>();
        ArrayList<Entry> yAxisPublicTransportation = new ArrayList<>();
        ArrayList<Entry> yAxisUtilities = new ArrayList<>();
        for (int i = 0; i < dataForYear.size(); i++) {
            yAxisCar.add(new Entry(i, dataForYear.get(i).getTotalCarbonForCar()));
            monthToDisplay.add(i, String.valueOf(dataForYear.get(i).getMonth()));
        }


        for (int i = 0; i < dataForYear.size(); i++) {
            yAxisPublicTransportation.add(new Entry(i, dataForYear.get(i).getTotalCarbonForPublic()));
        }

        for (int i = 0; i < dataForYear.size(); i++) {
            yAxisUtilities.add(new Entry(i, dataForYear.get(i).getTotalCarbonUtilities()));
        }

        Collections.sort(yAxisCar, new EntryXComparator());
        Collections.sort(yAxisPublicTransportation, new EntryXComparator());
        Collections.sort(yAxisUtilities, new EntryXComparator());
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSet1 = new LineDataSet(yAxisCar,"Car");
        lineDataSet1.setLineWidth(5);
        lineDataSet1.setDrawValues(true);
        lineDataSet1.setColor(Color.BLUE);

        LineDataSet lineDataSet2 = new LineDataSet(yAxisPublicTransportation,"Public Transportation");
        lineDataSet2.setLineWidth(5);
        lineDataSet2.setDrawValues(true);
        lineDataSet2.setColor(Color.RED);

        LineDataSet lineDataSet3 = new LineDataSet(yAxisUtilities,"Utilities");
        lineDataSet3.setLineWidth(5);
        lineDataSet3.setDrawValues(true);
        lineDataSet3.setColor(Color.GREEN);

        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);
        lineDataSets.add(lineDataSet3);

        lineChart.setData(new LineData(lineDataSets));
        lineChart.animateXY(2000, 2000);
        lineChart.setVisibleXRangeMaximum(10.5f);
        Description description = new Description();
        description.setText(" ");
        lineChart.setDescription(description);
        final XAxis axis = lineChart.getXAxis();
        axis.setAxisMaximum((float) yAxisCar.size());

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        lineChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                final Entry entry = lineChart.getEntryByTouchPoint(me.getX(), me.getY());
                Intent intent = new Intent(DisplayLineChart.this, DisplayBarChart.class);
                String lastDayOfMonth = dataForYear.get((int)entry.getX()).getLastDayOfMonth();
                intent.putExtra("today",lastDayOfMonth);
                startActivity(intent);

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });

    }

    private void generateDataForLineChart() {
        Intent intent = getIntent();
        String dateInString = intent.getStringExtra("today");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date today;
        Date today365;
        int month = 0;
        int year = 0;
        try {
            today = df.parse(dateInString);
            DateTime datetime = new DateTime(today);
            month = Integer.parseInt(datetime.toString("MM"));
            year = Integer.parseInt(datetime.toString("yyyy"));
            Calendar cal = new GregorianCalendar();
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_MONTH, -365);
            today365 = cal.getTime();
            for(int i = 0; i < journeyCollection.countJourneys(); i++) {
                Date date = df.parse(journeyCollection.getJourney(i).getDate());
                if(!(date.before(today365) || date.after(today))) {
                    journeys.add(journeyCollection.getJourney(i));
                }
            }
        }catch (ParseException e) {}

        for(int i = 0; i < 12; i++) {
            dataForYear.add(new DataForOneMonth(month, year));
            month--;
            if (month == 0) {
                month = 12;
                year = year - 1;
            }
        }

        for (DataForOneMonth data : dataForYear) {
            float carbonForCar = 0;
            float carbonForPublic = 0;
            for (int i = 0; i < journeys.size(); i++) {
                DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
                int journeyMonth = 0;
                int journeyYear = 0;
                try {
                    Date theDate = formatDate.parse(journeys.get(i).getDate());
                    DateTime dateTime = new DateTime(theDate);
                    journeyMonth = Integer.parseInt(dateTime.toString("MM"));
                    journeyYear = Integer.parseInt(dateTime.toString("yyyy"));
                }catch (ParseException e) {}
                if (data.getMonth() == journeyMonth && data.getYear() == journeyYear) {
                    if (journeys.get(i).getRoute().getType().equals("drive")) {
                        carbonForCar += Float.parseFloat(journeys.get(i).calculateCarbon());
                    }else if (journeys.get(i).getRoute().getType().equals("public")) {
                        carbonForPublic += Float.parseFloat(journeys.get(i).calculateCarbon());
                    }
                }
            }
            data.setTotalCarbonForCar(carbonForCar);
            data.setTotalCarbonForPublic(carbonForPublic);
        }

        for (int i = 0; i < dataForYear.size(); i++) {
            String firstDateString = dataForYear.get(i).getFirstDayOfMonth();
            String lastDayOfMonthString = dataForYear.get(i).getLastDayOfMonth();
            Date firstDate;
            Date lastDay;
            float totalCarbonUtilities = 0;
            try{
                firstDate = df.parse(firstDateString);
                Calendar cal = new GregorianCalendar();
                cal.setTime(firstDate);
                lastDay = df.parse(lastDayOfMonthString);
                Date currentDate = firstDate;
                while(!(currentDate.before(firstDate) || currentDate.after(lastDay))) {
                    String currentDayInString = df.format(currentDate);
                    float currentDayCarbon = (float)CarbonModel.getInstance().getBillCollection().getTotalCarbonEmission(currentDayInString);
                    totalCarbonUtilities += currentDayCarbon;
                    cal.add(Calendar.DAY_OF_MONTH, +1);
                    currentDate = cal.getTime();
                }
                dataForYear.get(i).setTotalCarbonUtilities(totalCarbonUtilities);
            }catch(ParseException e) {}
        }
    }
}

