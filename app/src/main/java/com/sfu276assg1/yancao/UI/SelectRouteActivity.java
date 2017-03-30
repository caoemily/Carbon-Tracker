package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sfu276assg1.yancao.carbontracker.BillCollection;
import com.sfu276assg1.yancao.carbontracker.CarbonModel;
import com.sfu276assg1.yancao.carbontracker.JourneyCollection;
import com.sfu276assg1.yancao.carbontracker.R;
import com.sfu276assg1.yancao.carbontracker.Route;
import com.sfu276assg1.yancao.carbontracker.RouteCollection;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_route);

        setupInitials();
        registerForContextMenu(list);
        setLaunchNewRoute();
        setRouteType();
        selectExistingRoute();
        routeList();

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

    @Override
    public void onBackPressed() {
        if (edit_journey == 0) {
            if (mode == 0) {
                CarbonModel.getInstance().removeLastJourney();
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
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else {
                    CarbonModel.getInstance().getJourneyCollection().getJourney(edit_journey_postition).setRoute(route);
                    CarbonModel.getInstance().getDb().updateSingleRouteInJourney((edit_journey_postition+1),route);
                    startActivity(new Intent(getApplicationContext(), DisplayTableActivity.class));
                }
                finish();
            }
        });
    }

    private void setLaunchNewRoute() {
        Button newRouteBtn = (Button) findViewById(R.id.btn_addRoute);
        newRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddRouteActivity.class);
                intent.putExtra(getResources().getString(R.string.TRANS_MODE), mode);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY), edit_journey);
                intent.putExtra(getResources().getString(R.string.EDIT_JOURNEY_POSITION), edit_journey_postition);
                startActivity(intent);
                finish();
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

}
