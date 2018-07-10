package com.wigitech.yam.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wigitech.yam.R;

public class AboutActivity extends AppCompatActivity {

    public static String FACEBOOK_URL = "https://www.facebook.com/1544844929145939";
    public static String FACEBOOK_PAGE_ID = "1544844929145939";

    Context context;

    private ImageView btnShaer;
    private Button btnSite;
    private ImageView btnPlayStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        this.context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnShaer = (ImageView) findViewById(R.id.btnShaer);
        btnShaer.setOnClickListener(v -> {
            Intent shaer = new Intent(Intent.ACTION_SEND);
            shaer.setType("text/plain");
            String shearBudy = "תודה על השיתוף";
            String shearSub = "https://play.google.com/store/apps/details?id=com.wigitech.yam";
            shaer.putExtra(Intent.EXTRA_SUBJECT, shearBudy);
            shaer.putExtra(Intent.EXTRA_TEXT, shearSub);
            startActivity(Intent.createChooser(shaer, shearBudy));
        });

        btnSite = (Button) findViewById(R.id.btnSite);
        btnSite.setOnClickListener(v -> {
            String url = "http://www.hofim.net";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        btnPlayStore = (ImageView) findViewById(R.id.btnPlaystore);
        btnPlayStore.setOnClickListener(v -> {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            try {
                startActivity(goToMarket);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
            }
        });



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (!(versionCode >= 3002850)) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

}
