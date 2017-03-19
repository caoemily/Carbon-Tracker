package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.Journey;
import com.sfu276assg1.yancao.carbontracker.R;

public class SelectTransModeActivity extends AppCompatActivity {
    CalendarView calendar;
    private String journeyDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trans_mode);

        createRadioButtons();
        generateCalendar();
        setupOkButton();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SelectTransModeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void createRadioButtons() {
        RadioGroup group = (RadioGroup) findViewById(R.id.transMode);
        String[] transporations = getResources().getStringArray(R.array.transportations);
        for (int i = 0; i < transporations.length; i++) {
            RadioButton button = new RadioButton(this);
            button.setText(transporations[i]);
            group.addView(button);
        }
    }

    private void setupOkButton() {
        Button button = (Button) findViewById(R.id.selectDateOk);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Journey journey = new Journey(journeyDate);
                CarbonModel.getInstance().addJourney(journey);

                RadioGroup group = (RadioGroup) findViewById(R.id.transMode);
                int idOfSelected = group.getCheckedRadioButtonId();

                if (idOfSelected < 0) {
                    Toast.makeText(getApplicationContext(),"Please Select Transportation Mode",Toast.LENGTH_SHORT).show();

                }
                else {
                    RadioButton radioButton = (RadioButton) findViewById(idOfSelected);
                    String mode = radioButton.getText().toString();
                    switch (mode) {
                        case "Car": {
                            Intent intent = new Intent(SelectTransModeActivity.this, SelectCarActivity.class);
                            intent.putExtra("TransMode", 0);
                            startActivity(intent);
                            break;
                        }
                        case "Bike / Walk": {
                            Intent intent = new Intent(SelectTransModeActivity.this, SelectRouteActivity.class);
                            intent.putExtra("TransMode", 1);
                            startActivity(intent);
                            break;
                        }
                        case "Public Transit": {
                            Intent intent = new Intent(SelectTransModeActivity.this, SelectRouteActivity.class);
                            intent.putExtra("TransMode", 2);
                            startActivity(intent);
                            break;
                        }
                    }
                    finish();
                }
            }
        });
    }

    private void generateCalendar() {
        calendar = (CalendarView) findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String monthEdited = String.format("%02d", month + 1);
                String dayEdited = String.format("%02d", dayOfMonth);
                journeyDate = "" + year + "-" + monthEdited + "-" + dayEdited;
            }
        });
    }

}
