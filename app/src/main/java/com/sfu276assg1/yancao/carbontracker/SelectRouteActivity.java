package com.sfu276assg1.yancao.carbontracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectRouteActivity extends AppCompatActivity {

    public static final int RESULT_CODE = 42;
    public static final int MODIFY_CODE = 40;
    //public static final int DELETE_CODE = 45;
    public int index = 0;
    public RouteCollection routeCollection = new RouteCollection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);
        setLaunchNewRoute();
        setLaunchEidtRoute();
        registerClickListItem();
        populatePotListView();
    }

    private void setLaunchNewRoute() {
        Button newRouteBtn = (Button) findViewById(R.id.btn_addRoute);
        newRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                startActivityForResult(intent, RESULT_CODE);
            }
        });
    }

    private void setLaunchEidtRoute(){
        ListView potsList = (ListView) findViewById(R.id.listView_selectRoute);
        potsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                //potIndex = position;
                String message = "You selected to edit: " + textView.getText().toString();
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                index = position;
                startActivityForResult(intent, MODIFY_CODE);
                return true;
            }
        });
    }

//    private void setLaunchDelete() {
//        Button deletePotBtn = (Button) findViewById(R.id.deleteBtn);
//        deletePotBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent deletePotIntent = new Intent(MainActivity.this, DeletePotActivity.class);
//                startActivityForResult(deletePotIntent,DELETE_CODE);
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_CANCELED){
            populatePotListView();
        }
        else{
            switch(requestCode){
                case RESULT_CODE:
                    Route added = getRouteFromIntent(data);
                    routeCollection.addRoute(added);
                    populatePotListView();
                    break;
                case MODIFY_CODE:
                    Route modified = getRouteFromIntent(data);
                    int modIndex = index;
                    routeCollection.changeRoute(modified,modIndex);
                    populatePotListView();
                    break;
//                case DELETE_CODE:
//                    int delIndex = data.getIntExtra("pot", 0);
//                    routeCollection.remove(potIndex);
//                    populatePotListView();
//                    break;
            }
        }
    }

    private void populatePotListView() {
        ArrayAdapter<String> routeAdapter = new ArrayAdapter<String>
                (this,R.layout.routedescription,
                        routeCollection.getRouteDescriptions());
        final ListView routeList = (ListView) findViewById(R.id.listView_selectRoute);
        routeList.setAdapter(routeAdapter);
    }

    private void registerClickListItem(){
        ListView potsList = (ListView) findViewById(R.id.listView_selectRoute);
        potsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You have chosen:  " + textView.getText().toString();
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
//                Pot selectedPot = getPot(textView.getText().toString());
//                String potName = selectedPot.getName();
//                int potWeight = selectedPot.getWeightInG();
//                Intent intent = new Intent(MainActivity.this, CalculationActivity.class);
//                intent.putExtra(POT_NAME, potName);
//                intent.putExtra(POT_WEIGHT, potWeight);
//                startActivity(intent);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));

            }
        });
    }

    public Route getRouteFromIntent(Intent data){
        String routeName = data.getStringExtra("routeName");
        int routeDistance = data.getIntExtra("routeDistance",0);
        int highwayPer = data.getIntExtra("highwayPer", 0);
        int cityPer = data.getIntExtra("cityPer",0);
        Route newRoute = new Route (routeDistance,highwayPer,cityPer);
        newRoute.setName(routeName);
        return newRoute;
    }
}

