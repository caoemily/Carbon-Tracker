package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectTranModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tran_mode);
        setupSelectCarButton();
        setupSelectPubTranButton();
        setupSelectWalkButton();
    }

    private void setupSelectCarButton() {
        CarbonModel.getInstance().setRouteCollection(MainActivity.db.getRouteList());
        Button button = (Button)findViewById(R.id.btn_selectCar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTranModeActivity.this, SelectCarActivity.class);
                intent.putExtra("TransMode",0);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupSelectPubTranButton() {
        Button button = (Button)findViewById(R.id.btn_selectPubTran);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTranModeActivity.this, SelectRouteActivity.class);
                intent.putExtra("TransMode",1);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupSelectWalkButton() {
        Button button = (Button)findViewById(R.id.btn_selectWalk);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTranModeActivity.this, SelectRouteActivity.class);
                intent.putExtra("TransMode",2);
                startActivity(intent);
                finish();
            }
        });
    }
}
