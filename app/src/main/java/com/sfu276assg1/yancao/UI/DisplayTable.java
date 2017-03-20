package com.sfu276assg1.yancao.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;

import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;

import java.util.ArrayList;
import java.util.Calendar;

public class DisplayTable extends AppCompatActivity {
    private static int position;
    private JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
    CustomArrayAdapter adapter;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_table_listview);

        list = (ListView) findViewById(R.id.listViewTable);
        registerForContextMenu(list);
        populateListView();
    }

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
        inflater.inflate(R.menu.context_menu_for_edit, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.delete) {
            CarbonModel.getInstance().removeJourney(info.position);
            CarbonModel.getInstance().getDb().deleteJourneyRow((info.position+1));
            adapter.notifyDataSetChanged();
            populateListView();
            return true;
        }else if(item.getItemId() == R.id.edit) {
            Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
            intent.putExtra("routeIndex", info.position);
            startActivity(intent);
            finish();
            return true;
        }else if(item.getItemId() == R.id.changeDate) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
            position = info.position;
            return true;
        }else{
            return super.onContextItemSelected(item);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Log.d("DEBUGGG DATE", "" + day + " " + month + " " + year);
            String monthEdited = String.format("%02d", month + 1);
            String dayEdited = String.format("%02d", day);
            String dateEdited = "" + year + "-" + monthEdited + "-" + dayEdited;
            CarbonModel.getInstance().getJourneyCollection().getJourney(position).setDate(dateEdited);
            getActivity().finish();
            startActivity(new Intent(getActivity(), DisplayTable.class));
        }
    }

}
