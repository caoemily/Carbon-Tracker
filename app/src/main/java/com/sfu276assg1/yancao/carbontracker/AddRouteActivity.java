package com.sfu276assg1.yancao.carbontracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddRouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        setupAcceptButton();
        setupCancelButton();
    }

    private void setupAcceptButton() {
        Button accBtn = (Button) findViewById(R.id.btn_accRoute);
        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okButtonClick();
            }
        });
    }

    private void okButtonClick(){
        EditText routeNameEntry = (EditText) findViewById(R.id.editText_enterRouteName);
        String routeName = routeNameEntry.getText().toString();
        EditText routeDistanceEntry = (EditText) findViewById(R.id.editView_enterDistance);
        String routeDistanceData = routeDistanceEntry.getText().toString();
        int routeDistance = Integer.parseInt(routeDistanceData);
        EditText highwayPerEntry = (EditText) findViewById(R.id.editView_enterHighwayPer);
        String highwayPerData = highwayPerEntry.getText().toString();
        int highwayPer = Integer.parseInt(highwayPerData);
        EditText cityPerEntry = (EditText) findViewById(R.id.editView_enterCityPer);
        String cityPerData = cityPerEntry.getText().toString();
        int cityPer = Integer.parseInt(cityPerData);


        Intent returnValueIntent = new Intent();
        returnValueIntent.putExtra("routeName", routeName);
        returnValueIntent.putExtra("routeDistance", routeDistance);
        returnValueIntent.putExtra("highwayPer", highwayPer);
        returnValueIntent.putExtra("cityPer", cityPer);
        setResult(Activity.RESULT_OK, returnValueIntent);
        finish();


//        int potWeight = validPositiveNum(routeDistanceData);
//        String potName = validString(potNameData);
//        if(potName==null|| potWeight <=0) {
//            if (potName==null) {
//                String message = "Pot name can't be left empty.";
//                Toast.makeText(EnterNewPot.this, message, Toast.LENGTH_SHORT).show();
//            }
//            if (potWeight <= 0) {
//                String message1 = "Pot weight must be positive number";
//                Toast.makeText(EnterNewPot.this, message1, Toast.LENGTH_SHORT).show();
//            }
//        }
//        else {
//            Intent returnPotValueIntent = new Intent();
//            returnPotValueIntent.putExtra(POT_NAME, potName);
//            returnPotValueIntent.putExtra(POT_WEIGHT, potWeight);
//            setResult(Activity.RESULT_OK, returnPotValueIntent);
//            finish();
//        }
    }

    private void setupCancelButton() {
        Button noBtn = (Button) findViewById(R.id.btn_cancelRoute);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButtonClick();
            }
        });
    }

    private void cancelButtonClick(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
