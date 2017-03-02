package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteRouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_route);
        populateJourneyListView();
        setClickToDelete();
    }

    private void setClickToDelete(){
        ListView routeList = (ListView) findViewById(R.id.listView_deleteRoute);
        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You have chosen:  " + textView.getText().toString();
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                CarbonModel.getInstance().removeRouteFromCollection(position);
                finish();
                startActivity(new Intent(getApplicationContext(),SelectRouteActivity.class));
            }
        });
    }

    private void populateJourneyListView() {
        ArrayAdapter<String> routeAdapter = new ArrayAdapter<String>
                (this,R.layout.routedescription,
                        CarbonModel.getInstance().getRouteCollection().getRouteDescriptions());
        final ListView routeList = (ListView) findViewById(R.id.listView_deleteRoute);
        routeList.setAdapter(routeAdapter);
    }
}
