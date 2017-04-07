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
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Display line chart for Carbon emissions in 12 months.
 */
public class DisplayLineChart extends AppCompatActivity {
    private int year_x;
    private int month_x;
    private int date_x;
    static final int DIALOG_ID = 0;

    LineChart lineChart;
    PieChart chart;
    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    private ArrayList<Journey> journeys = new ArrayList<>();
    private ArrayList<DataForOneMonth> dataForYear = new ArrayList<>();

    private ArrayList<String> nameOfEntries = new ArrayList<>();
    private ArrayList<String> nameOfEntriesDisplay = new ArrayList<>();
    private ArrayList<Float> emissions = new ArrayList<>();

    private int numberOfDaysGoBack = 365;
    private float totalCarbonElectrical = 0;
    private float totalCarbonNaturalGas = 0;
    private int unitChose = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_line_chart);

        getSupportActionBar().setTitle(R.string.action_graph);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        generateRealTimeCalendar();
        setUpBottomNavigation();

        generateDataForLineChart();
        generateLineChart();
//        TextView textView = (TextView) findViewById(R.id.lineChartDes);
//        textView.setText("Every point on the line represent 1 month, starting from current month." + "\n" +
//                        "Please click on the point to view each month in detail");
        generateDataForPieChart();
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
                        intent = new Intent(DisplayLineChart.this, DisplayBarChart.class);
                        intent.putExtra("today", today);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.show_year_chart:
                        String todayNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        intent = new Intent(DisplayLineChart.this, DisplayLineChart.class);
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
            Intent intent = new Intent(DisplayLineChart.this, DisplayCarbonFootprintActivity.class);
            intent.putExtra("single date selected", dateSelected);
            intent.putExtra("mode", 0);
            startActivity(intent);
        }
    };

    private void generateDataForPieChart() {
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
                        }else {
                            emission = journeys.get(j).calculateCarbonTreeYearDouble();
                        }
                        sumOfCarbon += (float) (emission);
                    }
                }
            }else{
                for (int y = 0; y < journeys.size(); y++) {
                    if(nameOfEntries.get(i).equals(journeys.get(y).getRoute().getType())){
                        double emission = 0;
                        if(unitChose != 0) {
                            emission = journeys.get(y).calculateCarbonDouble();
                        }else {
                            emission = journeys.get(y).calculateCarbonTreeYearDouble();
                        }
                        sumOfCarbon += (float) (emission);
                    }
                }
            }
            emissions.add(sumOfCarbon);
        }
        nameOfEntries.add(getString(R.string.electrical));
        emissions.add(totalCarbonElectrical);

        nameOfEntries.add(getString(R.string.natural_gas));
        emissions.add(totalCarbonNaturalGas);

        for(String name : nameOfEntries) {
            if(!name.equals("Bike/Walk") && !name.equals("Public Transit")) {
                String[] splitName = name.split(",");
                nameOfEntriesDisplay.add(splitName[0]);
            }else {
                nameOfEntriesDisplay.add(name);
            }
        }
    }

    private void generatePieChart() {

        chart = (PieChart) findViewById(R.id.pieChart_365);
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
        chart.setEntryLabelColor(Color.DKGRAY);
        chart.setRotationEnabled(true);
        chart.setHoleRadius(25f);
        chart.setTransparentCircleAlpha(0);
        chart.setData(data);
        chart.animateY(2000);
        chart.invalidate();

        Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);

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
                    sumOfCarbonPerRoute += (float) (emission);
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
        chart = (PieChart) findViewById(R.id.pieChart_365);

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


    private void generateLineChart() {
        lineChart = (LineChart) findViewById(R.id.lineChart);
        float averageCO2PerMonth = 0;
        float targetCO2 = 0;
        float averageCO2PerDay = (float)36.2;
        if(unitChose != 0) {
            averageCO2PerMonth = averageCO2PerDay * 30;
            targetCO2 = (averageCO2PerMonth * 70) / 100;
        }else{
            averageCO2PerMonth = (averageCO2PerDay * 30) / 20;
            targetCO2 = (averageCO2PerMonth * 70) / 100;
        }

        ArrayList<String> monthToDisplay = new ArrayList<>();
        ArrayList<Entry> yAxisCar = new ArrayList<>();
        ArrayList<Entry> yAxisPublicTransportation = new ArrayList<>();
        ArrayList<Entry> yAxisUtilities = new ArrayList<>();
        ArrayList<Entry> yAxisAvgCanadian = new ArrayList<>();
        ArrayList<Entry> yAxisTargetCO2 = new ArrayList<>();
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


        for (int i = 0; i < dataForYear.size(); i++) {
            yAxisAvgCanadian.add(new Entry(i, averageCO2PerMonth));
        }

        for (int i = 0; i < dataForYear.size(); i++) {
            yAxisTargetCO2.add(new Entry(i, targetCO2));
        }

        Collections.sort(yAxisCar, new EntryXComparator());
        Collections.sort(yAxisPublicTransportation, new EntryXComparator());
        Collections.sort(yAxisUtilities, new EntryXComparator());
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSet1 = new LineDataSet(yAxisCar,getString(R.string.car));
        lineDataSet1.setLineWidth(5);
        lineDataSet1.setDrawValues(true);
        lineDataSet1.setColor(Color.BLUE);

        LineDataSet lineDataSet2 = new LineDataSet(yAxisPublicTransportation,getString(R.string.public_trans));
        lineDataSet2.setLineWidth(5);
        lineDataSet2.setDrawValues(true);
        lineDataSet2.setColor(Color.RED);

        LineDataSet lineDataSet3 = new LineDataSet(yAxisUtilities,getString(R.string.utilities));
        lineDataSet3.setLineWidth(5);
        lineDataSet3.setDrawValues(true);
        lineDataSet3.setColor(Color.GREEN);

        LineDataSet lineDataSet4 = new LineDataSet(yAxisAvgCanadian,getString(R.string.average_canadian));
        lineDataSet4.setLineWidth(5);
        lineDataSet4.setDrawValues(true);
        lineDataSet4.setColor(Color.CYAN);

        LineDataSet lineDataSet5 = new LineDataSet(yAxisTargetCO2,getString(R.string.target_co2));
        lineDataSet5.setLineWidth(5);
        lineDataSet5.setDrawValues(true);
        lineDataSet5.setColor(Color.GRAY);

        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);
        lineDataSets.add(lineDataSet3);
        lineDataSets.add(lineDataSet4);
        lineDataSets.add(lineDataSet5);

        Legend legend = lineChart.getLegend();
        legend.setWordWrapEnabled(true);


        lineChart.setData(new LineData(lineDataSets));
        lineChart.animateXY(2000, 2000);
        lineChart.setVisibleXRangeMaximum(11f);
        Description description = new Description();
        description.setText(" ");
        lineChart.setDescription(description);
        final XAxis axis = lineChart.getXAxis();
        axis.setAxisMaximum((float) yAxisCar.size());

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        lineChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {}

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {}

            @Override
            public void onChartLongPressed(MotionEvent me) {}

            @Override
            public void onChartDoubleTapped(MotionEvent me) {}

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                final Entry entry = lineChart.getEntryByTouchPoint(me.getX(), me.getY());
                Intent intent = new Intent(DisplayLineChart.this, DisplayBarChart.class);
                String lastDayOfMonth = dataForYear.get((int)entry.getX()).getLastDayOfMonth();
                intent.putExtra("today",lastDayOfMonth);
                intent.putExtra(getString(R.string.UNIT_CHOICE), unitChose);
                startActivity(intent);
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {}

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {}

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {}
        });

    }

    private void generateDataForLineChart() {
        Intent intent = getIntent();
        unitChose = CarbonModel.getInstance().getUnitChoice();
        String dateInString = intent.getStringExtra("today 365");
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
            cal.add(Calendar.DAY_OF_MONTH, -numberOfDaysGoBack);
            today365 = cal.getTime();
            for(int i = 0; i < journeyCollection.countJourneys(); i++) {
                Date date = df.parse(journeyCollection.getJourney(i).getDate());
                if(!(date.before(today365) || date.after(today))) {
                    journeys.add(journeyCollection.getJourney(i));
                }
            }

            for(int j = 0; j <= numberOfDaysGoBack; j++) {
                cal.setTime(today);
                cal.add(Calendar.DAY_OF_MONTH, -j);
                Date currentDay = cal.getTime();
                String currentDayInString = df.format(currentDay);
                //NEED THISSSS TREEE
                float currentDayCarbonElectrical;
                float currentDayCarbonGas;
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
                    if (journeys.get(i).getRoute().getType().equals("Drive")) {
                        if(unitChose != 0) {
                            carbonForCar += (float)(journeys.get(i).calculateCarbonDouble());
                        }else{
                            carbonForCar += (float)(journeys.get(i).calculateCarbonTreeYearDouble());
                        }
                    }else if (journeys.get(i).getRoute().getType().equals("Public Transit")) {
                        if(unitChose !=0) {
                            carbonForPublic += (float)(journeys.get(i).calculateCarbonDouble());
                        }else{
                            carbonForPublic += (float)(journeys.get(i).calculateCarbonTreeYearDouble());
                        }
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
                    //NEED TO ADD FOR TREEEE
                    float currentDayCarbon = 0;
                    if(unitChose != 0) {
                        currentDayCarbon = (float) CarbonModel.getInstance().getBillCollection().getTotalCarbonEmission(currentDayInString);
                    }else{
                        currentDayCarbon = (float) CarbonModel.getInstance().getBillCollection().getTotalCarbonEmissionTreeYear(currentDayInString);
                    }
                    totalCarbonUtilities += currentDayCarbon;
                    cal.add(Calendar.DAY_OF_MONTH, +1);
                    currentDate = cal.getTime();
                }
                dataForYear.get(i).setTotalCarbonUtilities(totalCarbonUtilities);
            }catch(ParseException e) {}
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

