package com.sfu276assg1.yancao.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.sfu276assg1.yancao.carbontracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Activity to let user select to display single day, 28 days or a year of chart.
 */

public class SelectGraphActivity extends AppCompatActivity {
    private int year_x;
    private int month_x;
    private int date_x;
    static final int DIALOG_ID = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_graph);
        generateRealTimeCalendar();
        setSingleDayButton();
        set28DaysButton();
        set365DayButton();
    }

    private void set365DayButton() {
        Button button = (Button) findViewById(R.id.display365Days);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                Intent intent = new Intent(SelectGraphActivity.this, DisplayLineChart.class);
                intent.putExtra("today 365", today);
                startActivity(intent);
            }
        });
    }

    private void set28DaysButton() {
        Button button = (Button) findViewById(R.id.display28Days);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                Intent intent = new Intent(SelectGraphActivity.this, DisplayBarChart.class);
                intent.putExtra("today", today);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generateRealTimeCalendar() {
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        date_x = cal.get(Calendar.DATE);
    }

    private void setSingleDayButton() {
        Button button = (Button) findViewById(R.id.displaySingleDay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
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
            Intent intent = new Intent(SelectGraphActivity.this, DisplayCarbonFootprintActivity.class);
            intent.putExtra("single date selected", dateSelected);
            intent.putExtra("mode", 0);
            startActivity(intent);
        }
    };
}

