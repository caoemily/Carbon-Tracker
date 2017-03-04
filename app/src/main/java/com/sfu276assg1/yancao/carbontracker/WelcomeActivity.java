package com.sfu276assg1.yancao.carbontracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setUpAnimation();
        setupTestingButton();
    }

    private void setUpAnimation() {
        final ImageView myCar = (ImageView) findViewById(R.id.myCar);
        myCar.animate().translationX(1000).setDuration(25000).setStartDelay(0);

        final ImageView cloud = (ImageView) findViewById(R.id.cloud);
        cloud.animate().translationX(-1200).setDuration(25000).setStartDelay(0);

        final ImageView cloud2 = (ImageView) findViewById(R.id.cloud2);
        cloud2.animate().translationX(-1200).setDuration(25000).setStartDelay(0);

    }

    private void setupTestingButton() {
        Button button = (Button) findViewById(R.id.testing);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
