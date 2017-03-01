package com.sfu276assg1.yancao.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButton();
        setupText();
    }

    private void setupText() {
        TextView textView = (TextView) findViewById(R.id.text_main);
        Route current = CarbonModel.getInstance().getCurrentRoute();
        String s = current.getSingleRouteDes();

        textView.setText(s);
    }

    private void setButton() {
        Button btn = (Button) findViewById(R.id.btn_main);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectRouteActivity.class);
                startActivity(intent);
            }
        });
    }
}
