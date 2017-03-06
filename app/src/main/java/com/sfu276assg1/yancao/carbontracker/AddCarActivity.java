package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddCarActivity extends AppCompatActivity {

    String[] carMake ={"a","b","c"};
    String[] carModel ={"d","e","f"};
    String[] carYear ={"1","2","3"};
    Car currentCar = new Car();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        setupAcceptButton();
        setupSinpper();
    }

    private void setupSinpper() {

        Spinner makeSpin = (Spinner) findViewById(R.id.car_make);
        Spinner modelSpin = (Spinner) findViewById(R.id.car_model);
        Spinner yearSpin = (Spinner) findViewById(R.id.car_year);

        ArrayAdapter<String> makeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, carMake);
        ArrayAdapter<String> modelAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, carModel);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, carYear);



        makeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        makeSpin.setAdapter(makeAdapter);

        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpin.setAdapter(modelAdapter);

        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpin.setAdapter(yearAdapter);

        makeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String make = carMake[pos];
                currentCar.setMake(make);
            }

            @Override

            public void onNothingSelected(AdapterView<?> arg0) {
                String make = carMake[0];
                currentCar.setMake(make);

            }
        });

        modelSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String make = carModel[pos];
                currentCar.setModel(make);
            }

            @Override

            public void onNothingSelected(AdapterView<?> arg0) {
                String make = carModel[0];
                currentCar.setModel(make);

            }
        });

        yearSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String make = carYear[pos];
                currentCar.setYear(make);
            }

            @Override

            public void onNothingSelected(AdapterView<?> arg0) {
                String make = carYear[0];
                currentCar.setYear(make);

            }
        });
    }


    private void setupAcceptButton() {
        Button accBtn = (Button) findViewById(R.id.btnOkAddCar);
        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnValueIntent = getIntent();
                int index = returnValueIntent.getIntExtra("carIndex",-1);
                EditText carNameEntry = (EditText) findViewById(R.id.editNickname);
                String carName = carNameEntry.getText().toString();
                if(carName.length()==0 || carName==null){
                    Toast.makeText(getApplicationContext(),"Please enter a Nickname for the car.",Toast.LENGTH_LONG).show();
                }
                else{
                        currentCar.setNickname(carName);
                        CarbonModel.getInstance().addCarToAllCar(currentCar);
                        if(index == -1){
                            CarbonModel.getInstance().addCarToCollecton(currentCar);
                        }
                        else{
                            CarbonModel.getInstance().changeCarInCollection(currentCar,index);
                        }
                        finish();
                        startActivity(new Intent(getApplicationContext(), SelectRouteActivity.class));
                    }
                }
        });
    }


}
