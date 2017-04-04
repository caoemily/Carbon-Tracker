package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sfu276assg1.yancao.carbontracker.Bill;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.R;

public class MonthlyUtilitiesActivity extends AppCompatActivity {

    ArrayAdapter<Bill> adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_utilities);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = (ListView) findViewById(R.id.billsListView);

        populateListView();
        registerForContextMenu(list);
        selectExistingCar();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocas) {
        super.onWindowFocusChanged(hasFocas);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_plus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(MonthlyUtilitiesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.add_id:
                intent = new Intent(MonthlyUtilitiesActivity.this, AddBillActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateListView() {
        adapter = new MyListAdapter();
        list = (ListView) findViewById(R.id.billsListView);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Bill> {
        public MyListAdapter() {
            super(MonthlyUtilitiesActivity.this, R.layout.bill_view, CarbonModel.getInstance().getBillCollection().getBills());
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.bill_view,parent, false);
            }

            Bill currentBill = CarbonModel.getInstance().getBill(position);

            TextView startDateText = (TextView) itemView.findViewById(R.id.txt_StartDate);
            startDateText.setText(currentBill.getStartDate());

            TextView endDateText = (TextView) itemView.findViewById(R.id.txt_EndDate);
            endDateText.setText(currentBill.getEndDate());

            TextView peopleText = (TextView) itemView.findViewById(R.id.txt_People);
            peopleText.setText("" + currentBill.getPeople());

            TextView electricityText = (TextView) itemView.findViewById(R.id.txt_Electricity);
            electricityText.setText("" + Double.toString(currentBill.getElectricity()));

            TextView gasText = (TextView) itemView.findViewById(R.id.txt_Gas);
            gasText.setText("" + Double.toString(currentBill.getGas()));

            return itemView;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_delete_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                CarbonModel.getInstance().removeBill(info.position);
                CarbonModel.getInstance().getDb().deleteBillRow((info.position + 1));
                adapter.notifyDataSetChanged();
                populateListView();
                return true;
            case R.id.edit:
                Intent intent = new Intent(getApplicationContext(), AddBillActivity.class);
                intent.putExtra("billIndex", info.position);
                startActivity(intent);
                finish();
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void selectExistingCar(){
        list = (ListView) findViewById(R.id.billsListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                String message = Double.toString(CarbonModel.getInstance().getBillCollection().getBill(position).getTotalCarbonEmission());
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

