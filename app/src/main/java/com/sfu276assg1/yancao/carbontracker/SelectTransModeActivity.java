package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class SelectTransModeActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trans_mode);

        list = (ListView) findViewById(R.id.listView_carList);
        registerForContextMenu(list);
        setLaunchNewCar();
        selectExistingCar();
        routeList();
    }

    private void selectExistingCar(){
        ListView list = (ListView) findViewById(R.id.listView_carList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You have chosen:  " + textView.getText().toString();
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                Car car = CarbonModel.getInstance().getCarFromCollection(position);
                CarbonModel.getInstance().addCarToAllCar(car);
                startActivity(new Intent(getApplicationContext(),SelectRouteActivity.class));
                finish();
            }
        });
    }

    private void setLaunchNewCar() {
        Button newRouteBtn = (Button) findViewById(R.id.btn_addCar);
        newRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddCarActivity.class);
                intent.putExtra("carIndex", -1);
                startActivity(intent);
                finish();
            }
        });
    }

    private void routeList() {
        adapter = new ArrayAdapter<>(this,R.layout.route_list, CarbonModel.getInstance().getCarCollection().getCarDescription());
        list = (ListView) findViewById(R.id.listView_carList);
        list.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_route_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                CarbonModel.getInstance().removeCarFromCollection(info.position);
                adapter.notifyDataSetChanged();
                routeList();
                return true;
            case R.id.edit:
                Intent intent = new Intent(getApplicationContext(), AddCarActivity.class);
                intent.putExtra("carIndex", info.position);
                startActivity(intent);
                finish();
            default:
                return super.onContextItemSelected(item);
        }
    }
}
