package com.sfu276assg1.yancao.UI;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.DBAdapter;
import com.sfu276assg1.yancao.carbontracker.R;

/**
 * Choose to add journey OR show carbon footprint in table or chart
 */

public class MainActivity extends AppCompatActivity {
    private int unitChoice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDatabase();
        setUpBottomNavigation();
        setupUnitChoice();
        setupNotification();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocas) {
        super.onWindowFocusChanged(hasFocas);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.main_info,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.info:
                Intent intent = new Intent(MainActivity.this, AppInfoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.unit:
                showDialog();
                break;
        }
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

    private void setUpBottomNavigation() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.journey:
                        intent = new Intent(MainActivity.this, SelectTransModeActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.utilities:
                        intent = new Intent(MainActivity.this, MonthlyUtilitiesActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.graph:
                        intent = new Intent(MainActivity.this, SelectGraphActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    private void setupUnitChoice() {
        unitChoice = getUnitChoice();
    }

    private void setupNotification() {
        startService(new Intent(getBaseContext(),NotificationService.class));
    }

    private void showDialog(){
        Dialog dialog = onCreateDialog();
        dialog.show();
    }

    private Dialog onCreateDialog() {
        int unitFromPrevious = getUnitChoice();
        CharSequence[] array = getResources().getStringArray(R.array.graphUnit);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.CHOOSE_UNIT))
                .setIcon(R.drawable.tree)
                .setSingleChoiceItems(array, unitFromPrevious, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unitChoice = which;
                    }
                })
                .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        saveUnitChoice(unitChoice);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.CANCEL),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                    }
                });
        return alertDialogBuilder.create();
    }

    private void saveUnitChoice(int choice) {
        SharedPreferences prefs = this.getSharedPreferences(getResources().getString(R.string.UNIT_CHOICE), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("currentChoice",choice);
        editor.apply();
    }

    private int getUnitChoice(){
        SharedPreferences prefs = this.getSharedPreferences(getResources().getString(R.string.UNIT_CHOICE),MODE_PRIVATE);
        int defaultUnit = 0;
        return prefs.getInt("currentChoice",defaultUnit);
    }
}