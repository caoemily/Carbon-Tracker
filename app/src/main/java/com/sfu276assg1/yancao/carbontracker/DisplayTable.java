package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DisplayTable extends AppCompatActivity {
    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_table2);

        populateListView();
        registerClickCallBack();
    }

    private void registerClickCallBack() {
        ListView list = (ListView) findViewById(R.id.listViewTable);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Intent intent = new Intent(DisplayTable.this, EditJourneyActivity.class);
                intent.putExtra("position of journey", position);
                startActivity(intent);
            }
        });
    }

    private void populateListView() {
        ArrayList<JourneyInfo> itemsForListView = new ArrayList<>();
        for (int i = 0; i < journeyCollection.countJourneys(); i++) {
            itemsForListView.add(new JourneyInfo(journeyCollection.getJourney(i).getCar().getMake(), journeyCollection.getJourney(i).getDate()));
        }
        CustomArrayAdapter adapter = new CustomArrayAdapter(this,R.layout.journey_info, itemsForListView);

        ListView list = (ListView) findViewById(R.id.listViewTable);
        list.setAdapter(adapter);
    }
}
