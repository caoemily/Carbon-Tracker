package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sfu276assg1.yancao.carbontracker.Bill;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.R;

public class AddBillActivity extends AppCompatActivity {

    String startDate = "", endDate = "";
    double gas, electricity;
    int people;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        if( getIntent().getExtras() != null)
        {
            extractDataFromIntent();
        }

        setDate();
        setConsumption();
        setPeople();
        setupOkButton();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddBillActivity.this, MonthlyUtilitiesActivity.class);
        startActivity(intent);
        finish();
    }

    private void setUpUtilitiesButton() {
        Button button = (Button) findViewById(R.id.okBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBillActivity.this, MonthlyUtilitiesActivity.class);
                startActivity(intent);
                finish();
            }
        });
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

    private void setupOkButton() {
        Button okBtn = (Button) findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDate.length() == 0 || startDate == null){
                    Toast.makeText(getApplicationContext(), "Please enter the start date of the bill", Toast.LENGTH_SHORT).show();

                }
                else if (endDate.length() == 0 || endDate == null) {
                    Toast.makeText(getApplicationContext(), "Please enter the end date of the bill", Toast.LENGTH_SHORT).show();
                }
                else if (people == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter the number of people in the house", Toast.LENGTH_SHORT).show();
                }
                else if (electricity == 0 && gas == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter the consumptions", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent;
                    Bill bill = new Bill(startDate, endDate, electricity, gas, people);
                    if(getIntent().getExtras() == null) {
                        CarbonModel.getInstance().addBill(bill);
                        CarbonModel.getInstance().getDb().insertBillRow(bill);
                        intent = new Intent(AddBillActivity.this, MonthlyUtilitiesActivity.class);
                    }
                    else {
                        CarbonModel.getInstance().changeBill(bill, position);
                        CarbonModel.getInstance().getDb().updateBillRow((position + 1),bill);
                        intent = new Intent(AddBillActivity.this, MonthlyUtilitiesActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void extractDataFromIntent() {
        position = getIntent().getIntExtra("billIndex", 0);
        startDate = CarbonModel.getInstance().getBill(position).getStartDate();
        endDate = CarbonModel.getInstance().getBill(position).getEndDate();
        electricity = CarbonModel.getInstance().getBill(position).getElectricity();
        gas = CarbonModel.getInstance().getBill(position).getGas();
        people = CarbonModel.getInstance().getBill(position).getPeople();
    }

}
