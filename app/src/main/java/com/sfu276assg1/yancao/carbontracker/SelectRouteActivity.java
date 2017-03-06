package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

//show saved routes, let customer choose route, edit route, add route or delete route

public class SelectRouteActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);

        list = (ListView) findViewById(R.id.listView_routeList);
        registerForContextMenu(list);
        setLaunchNewRoute();
        selectExistingRoute();
        routeList();
    }

    private void selectExistingRoute(){
        ListView list = (ListView) findViewById(R.id.listView_routeList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You have chosen:  " + textView.getText().toString();
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                Route route = CarbonModel.getInstance().getRouteFromCollection(position);
                CarbonModel.getInstance().addRouteToAllRoute(route);
                addJourney();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }
    private void addJourney() {
        Car car = CarbonModel.getInstance().getLastCarInList();
        Route route = CarbonModel.getInstance().getLastRoute();
        Journey journey = new Journey(car,route);
        CarbonModel.getInstance().addJourney(journey);
    }

    private void setLaunchNewRoute() {
        Button newRouteBtn = (Button) findViewById(R.id.btn_addRoute);
        newRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                intent.putExtra("routeIndex", -1);
                startActivity(intent);
                finish();
            }
        });
    }

    private void routeList() {
        adapter = new ArrayAdapter<> (this,R.layout.route_list, CarbonModel.getInstance().getRouteCollection().getRouteDescriptions());
        list = (ListView) findViewById(R.id.listView_routeList);
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
                CarbonModel.getInstance().removeRouteFromCollection(info.position);
                adapter.notifyDataSetChanged();
                routeList();
                return true;
            case R.id.edit:
                Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                intent.putExtra("routeIndex", info.position);
                startActivity(intent);
                finish();
            default:
                return super.onContextItemSelected(item);
        }
    }
}
