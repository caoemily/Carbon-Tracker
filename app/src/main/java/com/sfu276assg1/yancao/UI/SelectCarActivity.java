package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sfu276assg1.yancao.carbontracker.Car;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * show saved cars, let customer choose car, edit car, add car or delete car
 */

public class SelectCarActivity extends AppCompatActivity {
    private ArrayList<Car> cars = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView list;
    int edit_journey;
    int edit_journey_postition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car);

        edit_journey = getIntent().getIntExtra(getResources().getString(R.string.EDIT_JOURNEY), 0);
        edit_journey_postition = getIntent().getIntExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), 0);
        list = (ListView) findViewById(R.id.listView_carList);
        registerForContextMenu(list);
        readDataFromFile();
        setLaunchNewCar();
        selectExistingCar();
        carList();
    }

    @Override
    public void onBackPressed() {
        if (edit_journey == 0) {
            Intent intent = new Intent(SelectCarActivity.this, SelectTransModeActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(SelectCarActivity.this, DisplayTableActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setLaunchNewCar() {
        Button newRouteBtn = (Button) findViewById(R.id.btn_addCar);
        newRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddCarActivity.class);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), edit_journey);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), edit_journey_postition);
                startActivity(intent);
                finish();
            }
        });
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

    private void carList() {
        adapter = new ArrayAdapter<>(this,R.layout.route_list, CarbonModel.getInstance().getCarCollection().getCarDescription());
        list = (ListView) findViewById(R.id.listView_carList);
        list.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_delete_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                CarbonModel.getInstance().getDb().deleteCarRow((info.position+1));
                CarbonModel.getInstance().removeCar(info.position);
                adapter.notifyDataSetChanged();
                carList();
                return true;
            case R.id.edit:
                Intent intent = new Intent(getApplicationContext(), AddCarActivity.class);
                intent.putExtra("carIndex", info.position);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), edit_journey);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), edit_journey_postition);
                startActivity(intent);
                finish();
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void selectExistingCar(){
        ListView list = (ListView) findViewById(R.id.listView_carList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You have chosen:  " + textView.getText().toString();
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                Car car = CarbonModel.getInstance().getCar(position);
                Car tempCar = CarbonModel.getInstance().getJourneyCollection().getJourney(edit_journey_postition).getCar();
                Intent intent;
                if (edit_journey == 0) {
                    CarbonModel.getInstance().getLastJourney().setCar(car);
                }
                else {
                    CarbonModel.getInstance().getJourneyCollection().getJourney(edit_journey_postition).setCar(car);
                    CarbonModel.getInstance().getDb().updateSingleCarInJourney((edit_journey_postition+1),car);
                }
                intent = new Intent(getApplicationContext(),SelectRouteActivity.class);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), edit_journey);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), edit_journey_postition);
                startActivity(intent);
                finish();
            }
        });
    }

}
