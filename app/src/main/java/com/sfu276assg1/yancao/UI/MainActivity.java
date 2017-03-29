package com.sfu276assg1.yancao.UI;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sfu276assg1.yancao.carbontracker.BillCollection;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.DBAdapter;
import com.sfu276assg1.yancao.carbontracker.Journey;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;
import com.sfu276assg1.yancao.carbontracker.RouteCollection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Double.parseDouble;

/**
 * Choose to add journey OR show carbon footprint in table or chart
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDatabase();
        setUpAddJourneytButton();
        setUpShowTableButton();
        setUpShowChartButton();
        setUpUtilitiesButton();
        setUpNotification();
        setUpPieChart();
    }

    private void setUpPieChart() {
        //Vu's chart code here
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.main_info,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(MainActivity.this, AppInfoActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    private void setupDatabase() {
        DBAdapter db = new DBAdapter(getApplicationContext());
        db.open();
        CarbonModel.getInstance().setDb(db);
        CarbonModel.getInstance().setJourneyCollection(CarbonModel.getInstance().getDb().getJourneyList());
        CarbonModel.getInstance().setCarCollection(CarbonModel.getInstance().getDb().getCarList());
        CarbonModel.getInstance().setRouteCollection(CarbonModel.getInstance().getDb().getRouteList());
        CarbonModel.getInstance().setBusRouteCollection(CarbonModel.getInstance().getDb().getBusRouteList());
        CarbonModel.getInstance().setWalkRouteCollection(CarbonModel.getInstance().getDb().getWalkRouteList());
        CarbonModel.getInstance().setBillCollection(CarbonModel.getInstance().getDb().getBillList());
    }

    private void setUpAddJourneytButton() {
        ImageButton button = (ImageButton) findViewById(R.id.addJourneyBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectTransModeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setUpShowTableButton() {
        ImageButton button = (ImageButton) findViewById(R.id.showTableBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayTableActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpShowChartButton() {
        ImageButton button = (ImageButton) findViewById(R.id.showChartBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectGraphActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpUtilitiesButton() {
        ImageButton button = (ImageButton) findViewById(R.id.utilitiesBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MonthlyUtilitiesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setUpNotification() {

        int previousActicity=-1;
        if( getIntent().getExtras() != null)
        {
            previousActicity = getIntent().getIntExtra("PreviousActivity", -1);
        }
        if(previousActicity!=0){
            return;
        }
        Dialog dialog = onCreateDialog(0);
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
        BillCollection billCollection = CarbonModel.getInstance().getBillCollection();
        int journeyCount = 0, billCount =0;
        for(int i=0; i<journeyCollection.countJourneys();i++){
            if(today.equals(journeyCollection.getJourney(i).getDate())){
                journeyCount++;
            }
        }
        for(int i=0; i<billCollection.countBills();i++){
            if(today.equals(billCollection.getBill(i).getRecordDate())){
                billCount++;
            }
        }

        if(journeyCount==0&&billCount==0){
            dialog = onCreateDialog(0);
        }
        else if(journeyCount>=3 && billCount==0){
            dialog = onCreateDialog(1);
        }
        else if (billCount>0&&journeyCount==0){
            dialog = onCreateDialog(3);
        }
        else {
            dialog = onCreateDialog(2);
        }
        dialog.show();
    }

    public Dialog onCreateDialog(int which) {
        final int choice = which;
        String message = "";
        Drawable icon = getApplicationContext().getResources().getDrawable(R.drawable.car);
        if(choice==1){
            icon =  getApplicationContext().getResources().getDrawable(R.drawable.utilityicon);
        }
        switch(choice){
            case 0:
                message = "You haven't entered any journey today, do you want to enter one?";
                break;
            case 1:
                message = "You have entered several journeys for today, do you want to enter a utility bill?";
                break;
            case 2:
                message = "Do you want to enter more journeys for today?";
                break;
            case 3:
                message = "You have entered utility bill today, do you want to enter a journey?";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Notification")
                .setIcon(icon)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (choice == 1) {
                            Intent intent = new Intent(getApplicationContext(), MonthlyUtilitiesActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(), SelectTransModeActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        return builder.create();
    }

}
