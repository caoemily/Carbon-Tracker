package com.sfu276assg1.yancao.UI;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sfu276assg1.yancao.carbontracker.R;

public class AppInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        displayAboutDevelopersLink();
        displayVersionNum();
        displayAboutDevelopers();
        displayAboutApp();
        displayImageSources();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayAboutDevelopersLink()
    {
        TextView aboutTxt = (TextView) findViewById(R.id.clickable_link_txt);

        aboutTxt.setClickable(true);
        aboutTxt.setMovementMethod(LinkMovementMethod.getInstance());
        String coursehomepage = "<a href= 'https://www.sfu.ca/computing.html'> Homepage </a>";
        aboutTxt.setText(Html.fromHtml(coursehomepage));


    }

    private void displayVersionNum() {
        String versionNum = "";
        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionNum = "ver:" + packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        TextView tv = (TextView) findViewById(R.id.verNum);
        tv.setText(versionNum);
    }

    private void displayAboutDevelopers()
    {
        TextView textView = (TextView) findViewById(R.id.about_developers);
        textView.setText(R.string.about_developer);

    }

    private void displayAboutApp()
    {
        TextView textView = (TextView) findViewById(R.id.about_app);
        textView.setText(R.string.about_app);

    }

    public void displayImageSources()
    {
        String[] citations = getResources().getStringArray(R.array.citations);

        TextView citationsTxt = (TextView)findViewById(R.id.citation1);
        TextView sourcesTxt = (TextView)findViewById(R.id.web1);
        String alienImage = "<a href=     http://www.iconarchive.com/show/100-flat-2-icons-by-graphicloads/bus-icon.html'> Bus Icon </a>";
        setupTextViews(citationsTxt, citations[0], sourcesTxt, alienImage );


        TextView citationsTxt2 = (TextView)findViewById(R.id.citation2);
        TextView sourcesTxt2 = (TextView)findViewById(R.id.web2);
        String starbg = "<a href=     http://icons.iconarchive.com/icons/icons8/windows-8/512/Time-Plus-1day-icon.png'> 1 Day </a>";
        setupTextViews(citationsTxt2, citations[1], sourcesTxt2, starbg);

        TextView citationsTxt3 = (TextView)findViewById(R.id.citation3);
        TextView sourcesTxt3 = (TextView)findViewById(R.id.web3);
        String alienScanSound = "<a href=    http://icons.iconarchive.com/icons/icons8/windows-8/512/Time-Minus-1month-icon.png'> 1 Month</a>";
        setupTextViews(citationsTxt3, citations[2], sourcesTxt3, alienScanSound);

        TextView citationsTxt4 = (TextView)findViewById(R.id.citation4);
        TextView sourcesTxt4 = (TextView)findViewById(R.id.web4);
        String alienFoundSound = "<a href=   http://icons.iconarchive.com/icons/icons8/windows-8/512/Time-Minus-1year-icon.png'>1 Year </a>";
        setupTextViews(citationsTxt4, citations[3], sourcesTxt4, alienFoundSound);

        TextView citationsTxt5 = (TextView)findViewById(R.id.citation5);
        TextView sourcesTxt5 = (TextView)findViewById(R.id.web5);
        String radar = "<a href=    https://cdn2.iconfinder.com/data/icons/summer-olympic-sport/256/bike-street-512.png'> 1 Year </a>";
        setupTextViews(citationsTxt5, citations[4], sourcesTxt5, radar);

        TextView citationsTxt6 = (TextView)findViewById(R.id.citation6);
        TextView sourcesTxt6 = (TextView)findViewById(R.id.web6);
        String month = "<a href=    https://www.vexels.com/download-vector/77467/flat-vintage-cars-icon-set'> Vintage Car </a>";
        setupTextViews(citationsTxt6, citations[5], sourcesTxt6, month);

        TextView citationsTxt7 = (TextView)findViewById(R.id.citation7);
        TextView sourcesTxt7 = (TextView)findViewById(R.id.web7);
        String year = "<a href=    http://apple.wallpapersfine.com/iPhone6/2229.html'> Graph Bg </a>";
        setupTextViews(citationsTxt7, citations[6], sourcesTxt7, year);

        TextView citationsTxt8 = (TextView)findViewById(R.id.citation8);
        TextView sourcesTxt8 = (TextView)findViewById(R.id.web8);
        String walltor = "<a href=   http://www.walltor.com/images/wallpaper/cg-composite-summer--water-air-and-greeneryphoto-mnipulation--sunbeams-shining-through-trees-dreamy-effect-44877.jpg'> Composite Bg </a>";
        setupTextViews(citationsTxt8, citations[7], sourcesTxt8, walltor);


    }

    private void setupTextViews(TextView citationTxt, String citation,TextView sourcesTxt, String hyperlink)
    {
        citationTxt.setText(citation);

        sourcesTxt.setClickable(true);
        sourcesTxt.setMovementMethod(LinkMovementMethod.getInstance());
        sourcesTxt.setText(Html.fromHtml(hyperlink));
    }

}
