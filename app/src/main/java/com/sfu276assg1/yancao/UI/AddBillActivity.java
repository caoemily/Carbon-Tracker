package com.sfu276assg1.yancao.UI;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sfu276assg1.yancao.carbontracker.Bill;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddBillActivity extends AppCompatActivity {

    String startDate = "", endDate = "", recordDate = "";
    String currentTip = "";
    double gas, electricity;
    int people;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if( getIntent().getExtras() != null)
        {
            extractDataFromIntent();
        }

        setDate();
        setConsumption();
        setPeople();
        UiChangeListener();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddBillActivity.this, MonthlyUtilitiesActivity.class);
        startActivity(intent);
        finish();
    }

    public void UiChangeListener()
    {
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_ok, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AddBillActivity.this, MonthlyUtilitiesActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.ok_id:
                setupAcceptButton();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setDate() {
        EditText edit_startDate = (EditText) findViewById(R.id.editStartDate);
        edit_startDate.setText(startDate);
        edit_startDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String str = s.toString();
                    if (str.matches("")){
                        startDate = "";
                    }
                    else{
                        startDate = s.toString();
                    }
                }
                catch(NumberFormatException e){}
            }
        });

        EditText edit_endDate = (EditText) findViewById(R.id.editEndDate);
        edit_endDate.setText(endDate);
        edit_endDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String str = s.toString();
                    if (str.matches("")){
                        endDate = "";
                    }
                    else{
                        endDate = s.toString();
                    }
                }
                catch(NumberFormatException e){}
            }
        });
    }

    public void setConsumption() {
        EditText edit_electricity = (EditText) findViewById(R.id.editElectricity);
        String num_electricity = Double.toString(electricity);
        if (electricity > 0) {
            edit_electricity.setText(num_electricity);
        }
        edit_electricity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String data = s.toString();
                    if (data.matches("")){
                        electricity = 0;
                    }
                    else{
                        electricity = Double.parseDouble(data);
                    }
                }
                catch(NumberFormatException e){}
            }
        });

        EditText edit_gas = (EditText) findViewById(R.id.editGas);
        String num_gas = Double.toString(gas);
        if (gas > 0 ) {
            edit_gas.setText(num_gas);
        }
        edit_gas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String data = s.toString();
                    if (data.matches("")){
                        gas = 0;
                    }
                    else{
                        gas = Double.parseDouble(data);
                    }
                }
                catch(NumberFormatException e){}
            }
        });
    }

    public void setPeople() {
        EditText edit_people = (EditText) findViewById(R.id.editPeople);
        String num_people = Double.toString(people);
        if (people > 0 ) {
            edit_people.setText(num_people);
        }
        edit_people.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String data = s.toString();
                    if (data.matches("")){
                        people = 0;
                    }
                    else{
                        people = Integer.parseInt(data);
                    }
                }
                catch(NumberFormatException e){}
            }
        });
    }

    private void setupAcceptButton() {
        if (startDate.length() == 0 || startDate == null){
            Toast.makeText(getApplicationContext(), R.string.bill_startdate, Toast.LENGTH_SHORT).show();

        }
        else if (endDate.length() == 0 || endDate == null) {
            Toast.makeText(getApplicationContext(), R.string.bill_enddate, Toast.LENGTH_SHORT).show();
        }
        else if (people == 0) {
            Toast.makeText(getApplicationContext(), R.string.bill_people, Toast.LENGTH_SHORT).show();
        }
        else if (electricity == 0 && gas == 0) {
            Toast.makeText(getApplicationContext(), R.string.bill_consumption, Toast.LENGTH_SHORT).show();
        }
        else {
            recordDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            Bill bill = new Bill(startDate, endDate, electricity, gas, people, recordDate);
            if(getIntent().getExtras() == null) {
                CarbonModel.getInstance().addBill(bill);
                CarbonModel.getInstance().getDb().insertBillRow(bill);
            }
            else {
                CarbonModel.getInstance().changeBill(bill, position);
                CarbonModel.getInstance().getDb().updateBillRow((position + 1),bill);
            }
            currentTip = SelectRouteActivity.setupTips(getApplicationContext());
            showDialog(currentTip);
        }
    }

    private void extractDataFromIntent() {
        position = getIntent().getIntExtra("billIndex", 0);
        startDate = CarbonModel.getInstance().getBill(position).getStartDate();
        endDate = CarbonModel.getInstance().getBill(position).getEndDate();
        electricity = CarbonModel.getInstance().getBill(position).getElectricity();
        gas = CarbonModel.getInstance().getBill(position).getGas();
        people = CarbonModel.getInstance().getBill(position).getPeople();
        recordDate = CarbonModel.getInstance().getBill(position).getRecordDate();
    }

    private void showDialog(String theTip){
        Dialog dialog = onCreateDialog(theTip);
        dialog.show();
    }

    private Dialog onCreateDialog(String theTip) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.TIP))
                .setMessage(theTip)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.NEXT_TIP),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        currentTip = SelectRouteActivity.setupTips(getApplicationContext());
                        showDialog(currentTip);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.SKIP),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        AddBillActivity.this.finish();
                        Intent intent = new Intent(getApplicationContext(), MonthlyUtilitiesActivity.class);
                        startActivity(intent);
                    }
                });
        return alertDialogBuilder.create();
    }

}
