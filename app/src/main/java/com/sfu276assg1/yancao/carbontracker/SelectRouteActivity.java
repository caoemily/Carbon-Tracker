package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectRouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);
        setLaunchNewRoute();
        setLaunchEdit();
        selectExistingRoute();
        setDelRoute();
        populateRouteListView();
    }

    private void selectExistingRoute(){
        ListView routeList = (ListView) findViewById(R.id.listView_selectRoute);
        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You have chosen:  " + textView.getText().toString();
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                Route current = CarbonModel.getInstance().getRouteFromCollection(position);
                CarbonModel.getInstance().addRouteToAllRoute(current);
                finish();
                startActivity(new Intent(getApplicationContext(),TempActivity.class));
            }
        });
    }

    private void setLaunchNewRoute() {
        Button newRouteBtn = (Button) findViewById(R.id.btn_addRoute);
        newRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                intent.putExtra("routeIndex", -1);
                finish();
                startActivity(intent);
            }
        });
    }

    private void setDelRoute() {
        Button newRouteBtn = (Button) findViewById(R.id.btn_delRoute);
        newRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DeleteRouteActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setLaunchEdit(){
        ListView potsList = (ListView) findViewById(R.id.listView_selectRoute);
        potsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;

                String message = "You selected: " + textView.getText().toString();
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                intent.putExtra("routeIndex", position);
                finish();
                startActivity(intent);
                return true;
            }
        });
    }

    private void populateRouteListView() {
        ArrayAdapter<String> routeAdapter = new ArrayAdapter<String>
                (this,R.layout.routedescription,
                        CarbonModel.getInstance().getRouteCollection().getRouteDescriptions());
        final ListView routeList = (ListView) findViewById(R.id.listView_selectRoute);
        routeList.setAdapter(routeAdapter);
    }
}
