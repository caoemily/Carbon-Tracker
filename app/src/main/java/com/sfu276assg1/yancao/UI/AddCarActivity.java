package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.sfu276assg1.yancao.carbontracker.Car;
import com.sfu276assg1.yancao.carbontracker.CarFamily;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.R;

import java.util.ArrayList;

//This is the activity that manipulates the screen after the user hits 'add car' on the Select Transport Mode page.
//setupMakeSpinner(), setupModelSpinner(), setyearSpinner() sets up all the spinner for Make, Model and Year
//setupAcceptButton() is the button that the user clicks after they're done filling in everything.

public class AddCarActivity extends AppCompatActivity {

    CarFamily carFamily = CarbonModel.getInstance().getCarFromFile();
    ArrayList<String> carMake = carFamily.defaultForGetMake();
    ArrayList<String> carModel = new ArrayList<>();
    ArrayList<String> carYear = new ArrayList<>();
    private static ArrayList<Car> carDescription;
    String carName = "", make, model, year, fuelType;
    int highWayE, cityE;
    int carChangePosition;
    Car tempCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        if( getIntent().getExtras() != null)
        {
            extractDataFromIntent();
        }

        setCarName();
        setupAcceptButton();
        setupMakeSpinner();
        setupModelSpinner();
        setupYearSpinner();
        generateListView();
        registerClickOnListViewItems();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddCarActivity.this, SelectCarActivity.class);
        startActivity(intent);
        finish();
    }

    public void setCarName() {
        EditText carNameEntry = (EditText) findViewById(R.id.editNickname);
        carNameEntry.setText(carName);
        carNameEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String str = s.toString();
                    if (str.matches("")){
                        carName = "";
                    }
                    else{
                        carName = s.toString();
                    }
                }
                catch(NumberFormatException e){}
            }
        });
    }

    private void setupMakeSpinner() {
        Spinner makeSpin = (Spinner) findViewById(R.id.car_make);
        ArrayAdapter<String> makeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_item, carMake);
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
                R.layout.spinner_item, carModel);
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
                R.layout.spinner_item, carYear);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpin.setAdapter(yearAdapter);

        yearSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                year = carYear.get(pos);
                generateListView();
                Toast.makeText(AddCarActivity.this, "Please select your car", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                year = carYear.get(0);
            }
        });
    }

    private void generateListView() {
        carDescription = carFamily.getDescription(make, model, year);
        ArrayList<String> posCars = new ArrayList<>();
        for (Car car : carDescription) {
            posCars.add(car.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.listview, posCars);
        ListView list = (ListView) findViewById(R.id.listViewPosCar);
        list.setAdapter(adapter);
    }

    private void registerClickOnListViewItems() {
        ListView list = (ListView) findViewById(R.id.listViewPosCar);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                highWayE = AddCarActivity.carDescription.get(position).getHighwayE();
                cityE = AddCarActivity.carDescription.get(position).getCityE();
                fuelType = AddCarActivity.carDescription.get(position).getFuelType();
            }
        });
    }

    private void setupAcceptButton() {
        Button accBtn = (Button) findViewById(R.id.btnOkAddCar);
        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carName.length() == 0 || carName == null) {
                    Toast.makeText(getApplicationContext(), "Please enter a Nickname for the car.", Toast.LENGTH_SHORT).show();
                }
                else if (make.length() == 0 || make == null) {
                    Toast.makeText(getApplicationContext(), "Please choose make.", Toast.LENGTH_SHORT).show();
                }
                else if (model.length() == 0 || model == null) {
                    Toast.makeText(getApplicationContext(), "Please choose model", Toast.LENGTH_SHORT).show();
                }
                else if (year.length() == 0 || year == null) {
                    Toast.makeText(getApplicationContext(), "Please choose year.", Toast.LENGTH_SHORT).show();
                }
                else if (highWayE == 0 || cityE == 0 || fuelType.length() == 0 || fuelType == null) {
                    Toast.makeText(getApplicationContext(), "Please select car.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Car car = new Car(make, model, year, highWayE, cityE, fuelType, carName);
                    Intent intent;
                    if(getIntent().getExtras() == null) {
                        CarbonModel.getInstance().addCar(car);
                        CarbonModel.getInstance().getDb().insertCarRow(car);
                        CarbonModel.getInstance().getLastJourney().setCar(car);
                        intent = new Intent(AddCarActivity.this, SelectRouteActivity.class);
                    }
                    else {
                        String originalName = CarbonModel.getInstance().getCar(carChangePosition).getNickname();
                        CarbonModel.getInstance().changeCar(car, carChangePosition);
                        CarbonModel.getInstance().getDb().updateCarRow(tempCar,car);
                        CarbonModel.getInstance().getDb().updateCarInJourney(tempCar,car);
                        //CarbonModel.getInstance().changeCarInJourney(tempCar, car);
                        intent = new Intent(AddCarActivity.this, SelectCarActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void extractDataFromIntent() {
        tempCar = new Car();
        carChangePosition = getIntent().getIntExtra("carIndex", 0);
        tempCar = CarbonModel.getInstance().getCar(carChangePosition);
    }
}

