package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.Journey;
import com.sfu276assg1.yancao.carbontracker.R;

public class CalendarForJourney extends AppCompatActivity {
    CalendarView calender;
    private String journeyDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journey);

        generateCalendar();
        setupOkButton();
    }

    private void setupOkButton() {
        Button button = (Button) findViewById(R.id.selectDateOk);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Journey journey = new Journey(journeyDate);
                CarbonModel.getInstance().addJourney(journey);
                Intent intent = new Intent(CalendarForJourney.this, SelectTransModeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void generateCalendar() {
        Intent intent = getIntent();
        final int position = intent.getIntExtra("position of journey", 0);
        calender = (CalendarView) findViewById(R.id.calender);
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String monthEdited = String.format("%02d", month + 1);
                String dayEdited = String.format("%02d", dayOfMonth);
                journeyDate = "" + year + "-" + monthEdited + "-" + dayEdited;
            }
        });
    }
}
