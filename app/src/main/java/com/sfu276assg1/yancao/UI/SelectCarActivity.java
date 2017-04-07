package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sfu276assg1.yancao.carbontracker.Car;
import com.sfu276assg1.yancao.carbontracker.CarListViewAdapter;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * show saved cars, let customer choose car, edit car, add car or delete car
 */

public class SelectCarActivity extends AppCompatActivity {

    public final static int EDITJOURNEY_DEFAULT= 0;
    public final static int EDITJOURNEY_POSITION_DEFAULT= 0;

    private List<Car> carList = new ArrayList<>();
    private ArrayList<Car> cars = new ArrayList<>();
    int edit_journey;
    int edit_journey_id;

    private ListView list;
    private CarListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupInitials();
        readDataFromFile();
        selectExistingCar();
        setCarList();
    }

    @Override
    public void onBackPressed() {
        if (edit_journey == 0) {
            Intent intent = new Intent(SelectCarActivity.this, SelectTransModeActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(SelectCarActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocas) {
        super.onWindowFocusChanged(hasFocas);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_plus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (edit_journey == 0) {
                    CarbonModel.getInstance().removeLastJourney();
                    Intent intent = new Intent(SelectCarActivity.this, SelectTransModeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SelectCarActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            case R.id.add_id:
                Intent intent = new Intent(getApplicationContext(), AddCarActivity.class);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), edit_journey);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_ID), edit_journey_id);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupInitials() {
        carList = CarbonModel.getInstance().getCarCollection().getCollection();
        edit_journey = getIntent().getIntExtra(getResources().getString(R.string.EDIT_JOURNEY), EDITJOURNEY_DEFAULT);
        edit_journey_id = getIntent().getIntExtra(getResources().getString(R.string.EDIT_JOURNEY_ID), EDITJOURNEY_POSITION_DEFAULT);
        list = (ListView) findViewById(R.id.listView_carList);
    }

    private void readDataFromFile() {
        InputStream is = getResources().openRawResource(R.raw.vehicles1);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        try {
            reader.readLine();
            while ( (line = reader.readLine()) != null) {
                String[] tokens = line.split(",");

                Car carData = new Car();
                carData.setMake(tokens[0]);
                carData.setModel(tokens[1]);
                carData.setYear(tokens[2]);
                carData.setCityE(Integer.parseInt(tokens[3]));
                carData.setHighwayE(Integer.parseInt(tokens[4]));
                if (tokens[5].length() > 0 && !tokens[5].equals("NA")) {
                    carData.setCylinders(Integer.parseInt(tokens[5]));
                }else{
                    carData.setCylinders(0);
                }
                if (tokens[6].length() > 0 && !tokens[6].equals("NA")) {
                    carData.setDisplacement(Double.parseDouble(tokens[6]));
                }else {
                    carData.setDisplacement(0);
                }
                if (tokens[7].length() > 0) {
                    carData.setDrive(tokens[7]);
                }else{
                    carData.setDrive("");
                }
                carData.setFuelType(tokens[8]);
                if (tokens[9].length() > 0) {
                    carData.setTransmission(tokens[9]);
                }else{
                    carData.setTransmission("");
                }
                if (!carData.getFuelType().equals("CNG") && !carData.getFuelType().equals("Gasoline or natural gas")) {
                    cars.add(carData);
                }
            }
        } catch (IOException e) {
            Log.d("ERORRRRRR FILE", "NO GOOOOOODDDDD");
            e.printStackTrace();
        }
        CarbonModel.getInstance().setCarFamily(cars);
    }

    private void setCarList() {
        adapter = new CarListViewAdapter(this,R.layout.car_list, carList);
        list.setAdapter(adapter);
    }

    public void updateAdapter() {
        adapter.notifyDataSetChanged();
        setCarList();
    }

    public void editCar(int position) {
        Intent intent = new Intent(getApplicationContext(), AddCarActivity.class);
        intent.putExtra("carIndex", position);
        intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), edit_journey);
        intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_ID), edit_journey_id);
        startActivity(intent);
        finish();
    }

    private void selectExistingCar(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Car car = CarbonModel.getInstance().getCar(position);
                Intent intent;
                if (edit_journey == 0) {
                    CarbonModel.getInstance().getLastJourney().setCar(car);
                }
                else {
                    CarbonModel.getInstance().getJourneyCollection().getJourney_id(edit_journey_id).setCar(car);
                    CarbonModel.getInstance().getDb().updateSingleCarInJourney((edit_journey_id),car);
                }
                intent = new Intent(getApplicationContext(),SelectRouteActivity.class);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), edit_journey);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_ID), edit_journey_id);
                startActivity(intent);
                finish();
            }
        });
    }

}
