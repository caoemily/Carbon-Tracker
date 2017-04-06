package com.sfu276assg1.yancao.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.DBAdapter;
import com.sfu276assg1.yancao.carbontracker.Journey;
import com.sfu276assg1.yancao.carbontracker.JourneyListViewAdapter;
import com.sfu276assg1.yancao.carbontracker.R;
import com.sfu276assg1.yancao.carbontracker.SeparatedListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Choose to add journey OR show carbon footprint in table or chart
 */

public class MainActivity extends AppCompatActivity {
    private int unitChoice = 0;
    private int total;
    private static int position;
    private static List<Journey> journeyList;
    private SeparatedListAdapter adapter;
    private ListView journalListView;
    private JourneyListViewAdapter journeyAdapter;
    private int mode_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.co2_cloud1);

        setupDatabase();
        setUpBottomNavigation();
        setupUnitChoice();
        setUpJourney();
        setUpTotalTrip();
        if (total > 0) {
            setJourneyList();
        }
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
        CarbonModel.getInstance().setUnitChoice(getUnitChoice());
        unitChoice = getUnitChoice();
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
                        CarbonModel.getInstance().setUnitChoice(unitChoice);
                        updateAdapter();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.CANCEL),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                    }
                });
        return alertDialogBuilder.create();
    }

    public void setUpJourney() {
//       Collections.sort(CarbonModel.getInstance().getDb().getJourneyList().getCollection());
//        Collections.sort(CarbonModel.getInstance().getJourneyCollection().getCollection());
        journeyList = CarbonModel.getInstance().getJourneyCollection().getCollection();
        Collections.sort(journeyList);
    }

    public void setUpTotalTrip() {
        total = CarbonModel.getInstance().getJourneyCollection().countJourneys();
        TextView total_trip = (TextView) findViewById(R.id.trip_total);
        if (total <= 1) {
            total_trip.setText(Integer.toString(total) + " " + getString(R.string.trip));
        }
        else{
            total_trip.setText(Integer.toString(total) + " " + getString(R.string.trips));
        }
    }

    public void setJourneyList() {
        ArrayList<String> days = new ArrayList<>();
        if (total > 0) {
            days.add(journeyList.get(0).getDate());
        }
        int index = 0;
        for (int i = 1; i < total; i++) {
            String currentDay = journeyList.get(i).getDate();
            if (!currentDay.equals(days.get(index))) {
                days.add(currentDay);
                index++;
            }
        }

        // Create the ListView Adapter
        adapter = new SeparatedListAdapter(this);

        // Add Sections
        for (int i = 0; i < days.size(); i++) {
            List<Journey> journeys = new ArrayList<>();
            for (int j = 0; j < total; j++) {
                if (days.get(i).equals(journeyList.get(j).getDate())) {
                    journeys.add(journeyList.get(j));
                }
            }
            journeyAdapter = new JourneyListViewAdapter(this, R.layout.list_item, journeys);
            adapter.addSection(days.get(i), journeyAdapter);
        }

        // Get a reference to the ListView holder
        journalListView = (ListView) this.findViewById(R.id.journey_list);

        // Set the adapter on the ListView holder
        journalListView.setAdapter(adapter);
    }

    public void updateAdapter() {
        adapter.notifyDataSetChanged();
        setUpJourney();
        setUpTotalTrip();
        setJourneyList();
    }

    public void editDate(int position) {
        this.position = position;
        DialogFragment newFragment = new MainActivity.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void editJourney(int position) {
        this.position = position;
        Dialog dialog = onCreateDialogSingleChoice();
        dialog.show();
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Log.d("DEBUGGG DATE", "" + day + " " + month + " " + year);
            String monthEdited = String.format("%02d", month + 1);
            String dayEdited = String.format("%02d", day);
            String dateEdited = "" + year + "-" + monthEdited + "-" + dayEdited;
            //please check here, Steven
            // also need to update delete in JourneyListViewAdapter,which should be based on id as well.
            int id = journeyList.get(position).getId();
            //CarbonModel.getInstance().getJourneyCollection().getJourney(position).setDate(dateEdited);
            //int id = CarbonModel.getInstance().getJourneyCollection().getJourney(position).getId();
            CarbonModel.getInstance().getDb().updateDateInJourney(id,dateEdited);
            getActivity().finish();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }

    public Dialog onCreateDialogSingleChoice() {
        CharSequence[] array = {"Car", "Public Transit", "Bike / Walk"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Select Transportation Mode")
                .setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mode_id = which;
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mode_id == 0){
                            Intent intent = new Intent(MainActivity.this, SelectCarActivity.class);
                            intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), position);
                            intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), 2);
                            intent.putExtra(getResources().getString(R.string.TRANS_MODE), 0);
                            startActivity(intent);
                        }
                        else if (mode_id == 1) {
                            Intent intent = new Intent(MainActivity.this, SelectRouteActivity.class);
                            intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), 1);
                            intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), position);
                            intent.putExtra(getResources().getString(R.string.TRANS_MODE), 1);
                            startActivity(intent);
                        }
                        else if (mode_id == 2) {
                            Intent intent = new Intent(MainActivity.this, SelectRouteActivity.class);
                            intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), 1);
                            intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), position);
                            intent.putExtra(getResources().getString(R.string.TRANS_MODE), 2);
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