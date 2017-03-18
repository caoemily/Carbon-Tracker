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

/**
 *show saved routes, let customer choose route, edit route, add route or delete route
 */

public class SelectRouteActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView list;
    int mode;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);
        mode = getIntent().getIntExtra("TransMode",0);
        list = (ListView) findViewById(R.id.listView_routeList);
        registerForContextMenu(list);
        setLaunchNewRoute();
        selectExistingRoute();
        setRouteType();
        routeList();
    }

    public void setRouteType() {
        switch(mode){
            case 0: type += "drive";
                break;
            case 1: type += "public";
                break;
            case 2: type += "walk";
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(mode==0){
            CarbonModel.getInstance().removeLastJourney();
            Intent intent = new Intent(SelectRouteActivity.this, SelectCarActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(SelectRouteActivity.this, SelectTranModeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void routeList() {
        String[] temp = {""};
        switch(mode){
            case 0:
                temp = CarbonModel.getInstance().getRouteCollection().getRouteDescriptions();
                break;
            case 1: temp = CarbonModel.getInstance().getBusRouteCollection().getRouteDescriptions();
                break;
            case 2: temp = CarbonModel.getInstance().getWalkRouteCollection().getRouteDescriptions();
                break;
        }
        adapter = new ArrayAdapter<> (this,R.layout.route_list, temp);
        list = (ListView) findViewById(R.id.listView_routeList);
        list.setAdapter(adapter);
    }

    private void selectExistingRoute(){
        ListView list = (ListView) findViewById(R.id.listView_routeList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You have chosen:  " + textView.getText().toString();
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                Route route = new Route ();
                switch(mode){
                    case 0:
                        route = CarbonModel.getInstance().getRoute(position);
                        CarbonModel.getInstance().getLastJourney().setRoute(route);
                        break;
                    case 1: route = CarbonModel.getInstance().getBusRoute(position);
                        CarbonModel.getInstance().addNoCarJourney(route);
                        break;
                    case 2: route = CarbonModel.getInstance().getWalkRoute(position);
                        CarbonModel.getInstance().addNoCarJourney(route);
                        break;
                }

                showCurrentJouney();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }

    private void setLaunchNewRoute() {
        Button newRouteBtn = (Button) findViewById(R.id.btn_addRoute);
        newRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                intent.putExtra("TransMode", mode);
                startActivity(intent);
                finish();
            }
        });
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
                switch(mode){
                    case 0:
                        String routeName = CarbonModel.getInstance().getRoute(info.position).getName();
                        MainActivity.db.deleteRouteRow(routeName);
                        CarbonModel.getInstance().removeRoute(info.position);
                        break;
                    case 1: CarbonModel.getInstance().removeBusRoute(info.position);
                        break;
                    case 2: CarbonModel.getInstance().removeWalkRoute(info.position);
                        break;
                }
                adapter.notifyDataSetChanged();
                routeList();
                return true;
            case R.id.edit:
                Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                intent.putExtra("routeIndex", info.position);
                intent.putExtra("TransMode", mode);
                startActivity(intent);
                finish();
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showCurrentJouney(){
        Journey curJourney = CarbonModel.getInstance().getLastJourney();
        String msg = curJourney.getJourneyDes();
        Toast.makeText(getApplicationContext(),msg,
                Toast.LENGTH_LONG).show();
    }


}
