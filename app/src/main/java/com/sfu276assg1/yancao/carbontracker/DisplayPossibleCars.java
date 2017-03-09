package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Let user choose their car - the exact model, transmission mode.
 */

public class DisplayPossibleCars extends AppCompatActivity {
    Car currentCar = new Car();
    private String carNickname;
    private int mode;
    private int indexOfArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_possible_cars);
        
        generateListView();
        registerClickCallBack();
        Toast.makeText(DisplayPossibleCars.this, "Please select your car", Toast.LENGTH_LONG).show();
    }

    private void generateListView() {
        Intent intent = getIntent();
        carNickname = intent.getStringExtra("car nickname");
        mode = intent.getIntExtra("edit mode", 0);
        indexOfArray = intent.getIntExtra("index", 0);
        ArrayList<String> posCars = new ArrayList<>();
        for (Car car : AddCarActivity.carDescription) {
            posCars.add(car.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,               // Context for activity
                R.layout.listview,  // Layout to use (create)
                posCars);
        ListView list = (ListView) findViewById(R.id.listViewPosCar);
        list.setAdapter(adapter);
    }

    private void registerClickCallBack() {
        ListView list = (ListView) findViewById(R.id.listViewPosCar);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("CLICKEDDDDDDD", carNickname);
                currentCar.setNickname(carNickname);
                currentCar.setMake(AddCarActivity.carDescription.get(position).getMake());
                currentCar.setModel(AddCarActivity.carDescription.get(position).getModel());
                currentCar.setYear(AddCarActivity.carDescription.get(position).getYear());
                currentCar.setHighwayE(AddCarActivity.carDescription.get(position).getHighwayE());
                currentCar.setCityE(AddCarActivity.carDescription.get(position).getCityE());
                currentCar.setFuelType(AddCarActivity.carDescription.get(position).getFuelType());
                if (mode == 0) {
                    CarbonModel.getInstance().addCarToCollecton(currentCar);
                }else{
                    String origCarName = CarbonModel.getInstance().getCarCollection().getCar(indexOfArray).getNickname();
                    for(int i=0; i<CarbonModel.getInstance().getJourneyCollection().countJourneys();i++){
                        if(CarbonModel.getInstance().getJourneyCollection().getJourney(i).getCarName().equals(origCarName)){
                            CarbonModel.getInstance().getJourneyCollection().getJourney(i).changeCarInJourney(carNickname,currentCar);
                        }
                    }
                    CarbonModel.getInstance().changeCarInCollection(currentCar, indexOfArray);
                }
                startActivity(new Intent(getApplicationContext(), SelectRouteActivity.class));
                CarbonModel.getInstance().addCarToAllCar(currentCar);
                finish();

            }
        });
    }
}
