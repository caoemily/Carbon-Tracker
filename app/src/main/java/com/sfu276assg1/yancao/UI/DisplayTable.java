package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;

import java.util.ArrayList;

public class DisplayTable extends AppCompatActivity {
    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    CustomArrayAdapter adapter;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_table2);


        //
        list = (ListView) findViewById(R.id.listViewTable);
        registerForContextMenu(list);
        //registerClickCallBack();
        populateListView();
    }

//    private void registerClickCallBack() {
//        ListView list = (ListView) findViewById(R.id.listViewTable);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
//                Intent intent = new Intent(DisplayTable.this, EditJourneyActivity.class);
//                intent.putExtra("position of journey", position);
//                startActivity(intent);
//            }
//        });
//    }

    private void populateListView() {
        ArrayList<JourneyInfoForListView> itemsForListView = new ArrayList<>();
        for (int i = 0; i < journeyCollection.countJourneys(); i++) {
            itemsForListView.add(new JourneyInfoForListView(journeyCollection.getJourney(i).getCar().getMake(), journeyCollection.getJourney(i).getDate()));
        }
        adapter = new CustomArrayAdapter(this,R.layout.journey_info, itemsForListView);

        ListView list = (ListView) findViewById(R.id.listViewTable);
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
                CarbonModel.getInstance().removeCar(info.position);
                adapter.notifyDataSetChanged();
                populateListView();
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
