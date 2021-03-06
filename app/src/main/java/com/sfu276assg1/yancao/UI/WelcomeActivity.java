package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfu276assg1.yancao.carbontracker.R;

/**
 * welcome animation
 */

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setUpAnimation();
        setupSkipButton();
        setupNotification();
    }

    private void setUpAnimation() {
        final ImageView cloud = (ImageView) findViewById(R.id.cloud);
        final ImageView cloud2 = (ImageView) findViewById(R.id.cloud2);
        Animation trans_cloud = AnimationUtils.loadAnimation(this, R.anim.custom_anim_cloud);
        cloud.startAnimation(trans_cloud);
        cloud2.startAnimation(trans_cloud);

        final ImageView car = (ImageView) findViewById(R.id.myCar);
        Animation trans_car = AnimationUtils.loadAnimation(this, R.anim.custom_anim_car);
        car.startAnimation(trans_car);

        trans_car.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.putExtra("PreviousActivity", 0);
                startActivity(intent);
                finish();
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocas) {
        super.onWindowFocusChanged(hasFocas);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void setupSkipButton() {
        TextView skip = (TextView) findViewById(R.id.skipBtn);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.putExtra("PreviousActivity", "Welcome");
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupNotification() {
        startService(new Intent(getBaseContext(),NotificationService.class));
    }

}
