package com.sfu276assg1.yancao.carbontracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);
        setLaunchNewRoute();
        launchEditOrDelete();
        registerClickListItem();
        populatePotListView();
    }

    private void setLaunchNewRoute() {
        Button newRouteBtn = (Button) findViewById(R.id.btn_addRoute);
        newRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                intent.putExtra("routeIndex", -1);
                startActivity(intent);
            }
        });
    }

    private void launchEditOrDelete(){
        ListView potsList = (ListView) findViewById(R.id.listView_selectRoute);
        potsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;

                String message = "You selected: " + textView.getText().toString();
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

//                TextView tv = (TextView) findViewById(R.id.text_editOrDel);
//                FragmentManager manager = getSupportFragmentManager();
//                RouteDeleteDialog dialog = new RouteDeleteDialog();
//                dialog.show(manager, "RouteMessage");
//                String s = tv.getText().toString();

                Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                intent.putExtra("routeIndex", position);
                startActivity(intent);
                return true;
            }
        });
    }

    private void populatePotListView() {
        ArrayAdapter<String> routeAdapter = new ArrayAdapter<String>
                (this,R.layout.routedescription,
                        CarbonModel.getInstance().getRouteCollection().getRouteDescriptions());
        final ListView routeList = (ListView) findViewById(R.id.listView_selectRoute);
        routeList.setAdapter(routeAdapter);
    }

    private void registerClickListItem(){
        ListView routeList = (ListView) findViewById(R.id.listView_selectRoute);
        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You have chosen:  " + textView.getText().toString();
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();

            }
        });
    }

}

