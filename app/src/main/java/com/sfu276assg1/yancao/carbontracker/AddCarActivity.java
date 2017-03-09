package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

//This is the activity that manipulates the screen after the user hits 'add car' on the Select Transport Mode page.
//setupMakeSpinner(), setupModelSpinner(), setyearSpinner() sets up all the spinner for Make, Model and Year
//setupAcceptButton() is the button that the user clicks after they're done filling in everything.

public class AddCarActivity extends AppCompatActivity {

    CarFamily carFamily = CarbonModel.getInstance().getCarFromFile();
    ArrayList<String> carMake = carFamily.defaultForGetMake();
    ArrayList<String> carModel = new ArrayList<String>();
    ArrayList<String> carYear = new ArrayList<String>();
    static  public ArrayList<Car> carDescription = new ArrayList<>();
    String make,model,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        setupAcceptButton();
        setupMakeSpinner();
        setupModelSpinner();
        setupYearSpinner();
    }

    private void setupPossibleCar() {
        carDescription = carFamily.getDescription(make, model, year);
        for(Car car : carDescription) {
            Log.i("DEBUGGGGGG", car.toString());
        }
    }


    private void setupMakeSpinner() {
        Spinner makeSpin = (Spinner) findViewById(R.id.car_make);
        ArrayAdapter<String> makeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, carMake);
        makeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        makeSpin.setAdapter(makeAdapter);
        makeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                make = carMake.get(pos);
                setupModelSpinner();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                make = carMake.get(0);
            }
        });
    }

    private void setupModelSpinner() {
        carModel = carFamily.getModel(make);
        java.util.Collections.sort(carModel);
        Spinner modelSpin = (Spinner) findViewById(R.id.car_model);
        ArrayAdapter<String> modelAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, carModel);
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpin.setAdapter(modelAdapter);

        modelSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                model = carModel.get(pos);
                setupYearSpinner();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                model = carModel.get(0);
            }
        });
    }

    private void setupYearSpinner() {
        carYear = carFamily.getYear(make, model);
        java.util.Collections.sort(carYear);
        Spinner yearSpin = (Spinner) findViewById(R.id.car_year);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, carYear);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpin.setAdapter(yearAdapter);

        yearSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                year = carYear.get(pos);
                setupPossibleCar();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                year = carYear.get(0);
            }
        });
    }

    private void setupAcceptButton() {
        Button accBtn = (Button) findViewById(R.id.btnOkAddCar);
        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnValueIntent = getIntent();
                int index = returnValueIntent.getIntExtra("carIndex", -1);
                EditText carNameEntry = (EditText) findViewById(R.id.editNickname);
                String carName = carNameEntry.getText().toString();
                if (carName.length() == 0 || carName == null) {
                    Toast.makeText(getApplicationContext(), "Please enter a Nickname for the car.", Toast.LENGTH_LONG).show();
                } else {
                  if (index == -1) {
                      Intent intent = new Intent(getApplicationContext(), DisplayPossibleCars.class);
                      intent.putExtra("car nickname", carName);
                      intent.putExtra("edit mode", 0);
                      intent.putExtra("index", -1);
                      startActivity(intent);
                      finish();
                  } else {
                      Intent intent = new Intent(getApplicationContext(), DisplayPossibleCars.class);
                      intent.putExtra("car nickname", carName);
                      intent.putExtra("edit mode", 1);
                      intent.putExtra("index", index);
                      startActivity(intent);
                      finish();
                  }
                }
            }
        });
    }

}

