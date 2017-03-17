package com.sfu276assg1.yancao.carbontracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    }

    private void populateListView() {
        ArrayList<JourneyInfo> itemsForListView = new ArrayList<>();
        for (int i = 0; i < journeyCollection.countJourneys(); i++) {
            itemsForListView.add(new JourneyInfo(journeyCollection.getJourney(i).getCar().getMake(), journeyCollection.getJourney(i).getDate()));
        }
        //ArrayList<String> itemsForListView = journeyCollection.journeyCollectionDescription();

//        ArrayAdapter<JourneyInfo> adapter = new ArrayAdapter<JourneyInfo>(
//                this,
//                R.layout.journey_info,
//                itemsForListView
//        );
        CustomArrayAdapter adapter = new CustomArrayAdapter(this,R.layout.journey_info, itemsForListView);

        ListView list = (ListView) findViewById(R.id.listViewTable);
        list.setAdapter(adapter);
    }
}
