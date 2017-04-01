package com.sfu276assg1.yancao.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;

import java.util.Calendar;

/**
 * Display table of all travelled journeys.
 */

public class DisplayTableActivity extends AppCompatActivity implements View.OnClickListener {

    private static int position;
    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    int mode_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_table);

        populateTable();
        setUpShowPieChartButton();
    }

    private void setUpShowPieChartButton() {
        Button button = (Button) findViewById(R.id.showPieChart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayTableActivity.this, SelectGraphActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void populateTable() {
        TableLayout table = (TableLayout) findViewById(R.id.table);
        TableRow titleRow = new TableRow(this);
        titleRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT,
                1.0f));
        table.addView(titleRow);
        for(int i = 0; i < 5; i++) {
            TextView textView = new TextView(this);
            textView.setTextSize(10);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1.0f);
            params.setMargins(1,1,1,1);
            textView.setLayoutParams(params);
            textView.setBackgroundColor(Color.GRAY);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            textView.setPadding(50, 50, 50, 50);
            if (i == 0) {
                textView.setText("Date");
            }
            if (i == 1) {
                textView.setText("Route");
            }
            if (i == 2) {
                textView.setText("Distance" + " (km)");
            }
            if (i == 3) {
                textView.setText("Transportation");
            }
            if (i == 4) {
                textView.setText("Carbon Footprint" + " (kg)");
            }
            titleRow.addView(textView);
        }
        for(int row = 0; row < journeyCollection.countJourneys(); row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setId(row);
            tableRow.setOnClickListener(DisplayTableActivity.this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            table.addView(tableRow);
            for(int col = 0; col < 5; col++) {
                TextView textView = new TextView(this);
                textView.setTextSize(10);
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f);
                params.setMargins(1,1,1,1);
                textView.setLayoutParams(params);
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                textView.setPadding(50, 50, 50, 50);
                if (col == 0) {
                    textView.setText(journeyCollection.getJourney(row).getDate());
                }
                if (col == 1) {
                    textView.setText(journeyCollection.getJourney(row).getRoute().getName());
                }
                if (col == 2) {
                    String distance = Double.toString(journeyCollection.getJourney(row).getRoute().getDistance());
                    textView.setText(distance);
                }
                if (col == 3) {
                    String type = journeyCollection.getJourney(row).getRoute().getType();
                    if (type.equals("Drive")) {
                        textView.setText(journeyCollection.getJourney(row).getCar().getNickname());
                    }
                    else {
                        textView.setText(type);
                    }
                }
                if (col == 4) {
                    textView.setText("" + journeyCollection.getJourney(row).calculateCarbon());
                }
                tableRow.addView(textView);
            }
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_for_edit, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.editModeRoute) {
            Dialog dialog = onCreateDialogSingleChoice();
            dialog.show();
            return true;
        }
        else if(item.getItemId() == R.id.editDate) {
            DialogFragment newFragment = new DisplayTableActivity.DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
            return true;
        }
        else if (item.getItemId() == R.id.delete) {
            CarbonModel.getInstance().removeJourney(position);
            CarbonModel.getInstance().getDb().deleteJourneyRow((position+1));
            finish();
            startActivity(getIntent());
            return true;
        }
        else {
            return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        position = v.getId();
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.showContextMenu();
                return true;
            }
        });
        registerForContextMenu(v);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

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
            CarbonModel.getInstance().getJourneyCollection().getJourney(position).setDate(dateEdited);
            CarbonModel.getInstance().getDb().updateDateInJourney((position+1),dateEdited);
            getActivity().finish();
            startActivity(new Intent(getActivity(), DisplayTableActivity.class));
        }
    }

    public Dialog onCreateDialogSingleChoice() {
        CharSequence[] array = {"Car", "Bike / Walk", "Public Transit"};
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
                            Intent intent = new Intent(DisplayTableActivity.this, SelectCarActivity.class);
                            intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), position);
                            intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), 2);
                            intent.putExtra(getResources().getString(R.string.TRANS_MODE), 0);
                            startActivity(intent);
                        }
                        else if (mode_id == 1) {
                            Intent intent = new Intent(DisplayTableActivity.this, SelectRouteActivity.class);
                            intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), 1);
                            intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), position);
                            intent.putExtra(getResources().getString(R.string.TRANS_MODE), 2);
                            startActivity(intent);
                        }
                        else if (mode_id == 2) {
                            Intent intent = new Intent(DisplayTableActivity.this, SelectRouteActivity.class);
                            intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), 1);
                            intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), position);
                            intent.putExtra(getResources().getString(R.string.TRANS_MODE), 1);
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
