package com.sfu276assg1.yancao.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sfu276assg1.yancao.carbontracker.BillCollection;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;
import com.sfu276assg1.yancao.carbontracker.Route;

/**
 *show saved routes, let customer choose route, edit route, add route or delete route
 */

public class SelectRouteActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView list;
    int mode;
    int edit_journey;
    int edit_journey_postition;
    String type = "";
    String currentTip = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupInitials();
        registerForContextMenu(list);
        setRouteType();
        selectExistingRoute();
        routeList();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocas) {
        super.onWindowFocusChanged(hasFocas);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
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
        switch (item.getItemId()) {
            case android.R.id.home:
                if (edit_journey == 0) {
                    if (mode == 0) {
                        Intent intent = new Intent(SelectRouteActivity.this, SelectCarActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SelectRouteActivity.this, SelectTransModeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else if (edit_journey == 1) {
                    Intent intent = new Intent(SelectRouteActivity.this, DisplayTableActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SelectRouteActivity.this, SelectCarActivity.class);
                    intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), edit_journey);
                    intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), edit_journey_postition);
                    startActivity(intent);
                    finish();
                }
                return true;
            case R.id.add_id:
                    Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                    intent.putExtra(getResources().getString(R.string.TRANS_MODE), mode);
                    intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), edit_journey);
                    intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), edit_journey_postition);
                    startActivity(intent);
                    finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupInitials(){
        mode = getIntent().getIntExtra(getResources().getString(R.string.TRANS_MODE), AddRouteActivity.TRANSMODE_DEFAULT);
        edit_journey = getIntent().getIntExtra(getResources().getString(R.string.EDIT_JOURNEY), SelectCarActivity.EDITJOURNEY_DEFAULT);
        edit_journey_postition = getIntent().getIntExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), SelectCarActivity.EDITJOURNEY_POSITION_DEFAULT);
        list = (ListView) findViewById(R.id.listView_routeList);
    }

    private void setRouteType() {
        switch(mode){
            case 0: type += "Drive";
                break;
            case 1: type += "Public Transit";
                break;
            case 2: type += "Bile/Walk";
                break;
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
                Route route = new Route ();
                switch(mode){
                    case 0:
                        route = CarbonModel.getInstance().getRoute(position);
                        break;
                    case 1:
                        route = CarbonModel.getInstance().getBusRoute(position);
                        break;
                    case 2:
                        route = CarbonModel.getInstance().getWalkRoute(position);
                        break;
                }
                if (edit_journey == 0) {
                    CarbonModel.getInstance().getLastJourney().setRoute(route);
                    CarbonModel.getInstance().getDb().insertRowJourney(CarbonModel.getInstance().getLastJourney());

                }
                else {
                    CarbonModel.getInstance().getJourneyCollection().getJourney(edit_journey_postition).setRoute(route);
                    CarbonModel.getInstance().getDb().updateSingleRouteInJourney((edit_journey_postition + 1),route);
                }
                currentTip = setupTips(getApplicationContext());
                showDialog(currentTip);
            }
        });
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
                switch(mode){
                    case 0:
                        CarbonModel.getInstance().getDb().deleteRouteRow((info.position+1));
                        CarbonModel.getInstance().removeRoute(info.position);
                        break;
                    case 1:
                        CarbonModel.getInstance().getDb().deleteBusRouteRow((info.position+1));
                        CarbonModel.getInstance().removeBusRoute(info.position);
                        break;
                    case 2:
                        CarbonModel.getInstance().getDb().deleteWalkRouteRow((info.position+1));
                        CarbonModel.getInstance().removeWalkRoute(info.position);
                        break;
                }
                adapter.notifyDataSetChanged();
                routeList();
                return true;
            case R.id.edit:
                Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), edit_journey);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), edit_journey_postition);
                intent.putExtra(AddRouteActivity.ROUTE_INDEX, info.position);
                intent.putExtra(getResources().getString(R.string.TRANS_MODE), mode);
                startActivity(intent);
                finish();
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showDialog(String theTip){
        Dialog dialog = onCreateDialog(theTip);
        dialog.show();
    }

    private Dialog onCreateDialog(String theTip) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Tips")
                .setMessage(theTip)
                .setCancelable(false)
                .setPositiveButton("Next Tip",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        currentTip = setupTips(getApplicationContext());
                        showDialog(currentTip);
                    }
                })
                .setNegativeButton("Skip",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        SelectRouteActivity.this.finish();
                        if (edit_journey == 0){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(), DisplayTableActivity.class);
                            startActivity(intent);
                        }
                    }
                });
        return alertDialogBuilder.create();
    }

    static public String setupTips(Context context) {
        JourneyCollection journeyCollection = CarbonModel.getInstance().getJourneyCollection();
        BillCollection billCollection = CarbonModel.getInstance().getBillCollection();
        int whichTip = CarbonModel.getInstance().showWhichTip(journeyCollection, billCollection);
        int index = 0;
        String tip = "";
        switch (whichTip) {
            case 0:
                index = getLastIndexFromSharedPrefCar(context);
                tip = CarbonModel.getInstance().generateCarTip(journeyCollection, index);
                index++;
                storeLastIndexCar(context,index);
                break;
            case 1:
                index = getLastIndexFromSharedPrefElect(context);
                tip = CarbonModel.getInstance().generateElecTip(billCollection, index);
                index++;
                storeLastIndexElect(context,index);
                break;
            case 2:
                index = getLastIndexFromSharedPrefGas(context);
                tip = CarbonModel.getInstance().generateGasTip(billCollection, index);
                index++;
                storeLastIndexGas(context,index);
                break;
        }
        return tip;
    }

    static public int getLastIndexFromSharedPrefCar(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("car array", MODE_PRIVATE);
        int extractedValueCar = prefs.getInt("car array index", 0);
        return extractedValueCar;
    }
    static public int getLastIndexFromSharedPrefElect(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("electricity array", MODE_PRIVATE);
        int extractedValueElect = prefs.getInt("elect array index", 0);
        return extractedValueElect;
    }
    static public int getLastIndexFromSharedPrefGas(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("gas array", MODE_PRIVATE);
        int extractedValueGas = prefs.getInt("gas array index", 0);
        return extractedValueGas;
    }
    static public void storeLastIndexCar(Context context, int index) {
        SharedPreferences prefs = context.getSharedPreferences("car array", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("car array index", index);
        editor.commit();
    }
    static public void storeLastIndexElect(Context context, int index) {
        SharedPreferences prefs = context.getSharedPreferences("elect array", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("elect array index", index );
        editor.commit();
    }
    static public void storeLastIndexGas(Context context, int index) {
        SharedPreferences prefs = context.getSharedPreferences("gas array", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("gas array index", index );
        editor.commit();
    }

}
