package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

public class EditJourneyActivity extends AppCompatActivity {
    CalendarView calender;
    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journey);

        generateCalendar();
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
                String date = "" + year + "-" + monthEdited + "-" + dayEdited;
                journeyCollection.getJourney(position).setDate(date);
                startActivity(new Intent(EditJourneyActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
