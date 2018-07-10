package com.wigitech.yam.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wigitech.yam.Analytics.HofimAnalytics;
import com.wigitech.yam.R;
import com.wigitech.yam.adapters.DetailedCoastAdapter;
import com.wigitech.yam.model.Beach;
import com.wigitech.yam.model.DetailedBeachHourlyForecast;
import com.wigitech.yam.model.DetailedCoastRecyclerView;
import com.wigitech.yam.model.DetailedCoastWithForcast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wigitech.yam.R.id.toggleFavoriteButton;
import static com.wigitech.yam.activities.CoastMapActivity.favBeachMap;
import static com.wigitech.yam.activities.MapsActivity.allBeaches;
import static com.wigitech.yam.utils.Utils.mySnackBar;

public class CoastDetailsActivity2 extends AppCompatActivity {

    public final String baseUrl = "http://5.100.253.193:8080/hofim/";
    private final String TAG = "TAG";
    private final String sharedPreferencesName = "favBeach";

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static int coastId;
    private String jsonCoastDetailsString = "";
    private RecyclerView recyclerView;
    private DetailedCoastAdapter detailedCoastAdapter;
    private int hourOfDay = 0; //TODO get the current hour of day in int
    private ImageView btnBlueFlag;
    private ImageView btnHandycapFriendly;
    private ImageView btnFavorite;
    private ImageView btnWaze;
    private Button forecastBtn;
    private RelativeLayout mainLayout;
    private Beach mBeach;
    private Tracker mTracker;
    private LinearLayout forcastLayout;
    private Toolbar toolbar;
    private int btnNumber;
    private String beacheName;
    private TextView tvBeachName;
    Boolean inFavBeach = true;
    private DetailedCoastWithForcast detailedCoastWithForcast;
    private DetailedBeachHourlyForecast dbhfToday;
    private List<DetailedCoastRecyclerView> detailedCoastRecyclerViewList = new ArrayList<>();
    private Map<String, String> windBearing = new HashMap<>();
    private Map<String, String> jellyType = new HashMap<>();
    private Double lat;
    private Double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coast_details2);

        setPointer();

    }

    private void setPointer() {

        this.context = this;
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        HofimAnalytics hofimAnalytics = (HofimAnalytics) getApplication();
        mTracker = hofimAnalytics.getDefaultTracker();
        // [END shared_tracker]

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedPreferences = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        tvBeachName = (TextView) findViewById(R.id.tvBeachName);
        //start the forecast activity
        //forcastLayout = (LinearLayout) findViewById(R.id.forcastLayout);
        forecastBtn = (Button) findViewById(R.id.forecastBtn);
        forecastBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, ForecastActivity.class);
            intent.putExtra("coastId", coastId);
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            startActivity(intent);
        });
        AndroidNetworking.initialize(context);

        //get the coastId
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            //TODO add snackbar for error
        } else {
            coastId = extras.getInt("coastId") + 1;
            btnNumber = extras.getInt("btnNumber");
            lat = extras.getDouble("lat");
            lon = extras.getDouble("lon");
            if (extras.getString("beachName") != null) {
                beacheName = extras.getString("beachName");
                inFavBeach = extras.getBoolean("fromFav");
            }

        }
        //set map for winds
        windBearing.put("N", "צפונית");
        windBearing.put("NNE", "צפונית צפון מזרחית");
        windBearing.put("NE", "צפונית מזרחית");
        windBearing.put("ENE", "מזרחית צפון מזרחית");
        windBearing.put("E", "מזרחית");
        windBearing.put("SW", "דרומית מערבית");
        windBearing.put("SSW", "דרומית דרום מערבית");
        windBearing.put("S", "דרומית");
        windBearing.put("SSE", "דרומית דרום מזרחית");
        windBearing.put("SE", "דרומית מזרחית");
        windBearing.put("ESE", "מזרחית דרום מזרחית");
        windBearing.put("W", "מערבית");
        windBearing.put("WSW", "מערבית דרום מערבית");
        windBearing.put("WNW", "מערבית צפון מערבית");
        windBearing.put("NW", "צפונית מערבית");
        windBearing.put("NNW", "צפונית צפון מערבית");

        //set map for jellys
        jellyType.put("NO_VALUE", "אין דיווחים");
        jellyType.put("NONE", "אין מדוזות");
        jellyType.put("FEW", "מעט מדוזות");
        jellyType.put("SOME", "מעט מדוזות");
        jellyType.put("ALOT", "המון מדוזות");


        //load current beach from sqlite
        //TODO SQLITE

        //Buttons
        btnBlueFlag = (ImageView) findViewById(R.id.blueFlagButton);
        btnBlueFlag.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            LayoutInflater inflater = this.getLayoutInflater();
            View adView = inflater.inflate(R.layout.alert_dialog_defaults, null);
            alertDialog.setView(adView);
            TextView adTitleText = (TextView) adView.findViewById(R.id.adTitleText);
            adTitleText.setText("מהו חוף דגל כחול?");
            ImageView adTitleImage = (ImageView) adView.findViewById(R.id.adTitleImage);
            adTitleImage.setImageResource(R.drawable.blue_flag);
            TextView adMsg = (TextView) adView.findViewById(R.id.adMainText);
            adMsg.setText(getString(R.string.ad_blue_flag));
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "הבנתי", (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
        });

        btnHandycapFriendly = (ImageView) findViewById(R.id.accessibilityButton);
        btnHandycapFriendly.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            LayoutInflater inflater = this.getLayoutInflater();
            View adView = inflater.inflate(R.layout.alert_dialog_defaults, null);
            alertDialog.setView(adView);
            TextView adTitleText = (TextView) adView.findViewById(R.id.adTitleText);
            adTitleText.setText("מהו חוף נגיש?");
            ImageView adTitleImage = (ImageView) adView.findViewById(R.id.adTitleImage);
            adTitleImage.setImageResource(R.drawable.accessibility);
            TextView adMsg = (TextView) adView.findViewById(R.id.adMainText);
            adMsg.setText(getString(R.string.ad_accesibility));
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "הבנתי", (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
        });

        btnFavorite = (ImageView) findViewById(toggleFavoriteButton);
        if (inFavBeach) {
            btnFavorite.setImageDrawable(getDrawable(R.drawable.favorite_selected));
        }
        btnFavorite.setOnClickListener(v -> {
            Drawable unSelected = getDrawable(R.drawable.favorite_unselected);
            Drawable selected = getDrawable(R.drawable.favorite_selected);
            if (inFavBeach) {
                btnFavorite.setImageDrawable(unSelected);
                inFavBeach = false;

                if (sharedPreferences.getInt("favBeach1", -1) != -1 && btnNumber == 1) {
                    editor.putString("favBeachName1", "");
                    editor.putInt("favBeach1", -1);
                    favBeachMap.put("favBeach1", -1);
                    editor.commit();
                    return;
                }
                if (sharedPreferences.getInt("favBeach2", -1) != -1 && btnNumber == 2) {
                    editor.putString("favBeachName2", "");
                    editor.putInt("favBeach2", -1);
                    favBeachMap.put("favBeach2", -1);
                    editor.commit();
                    btnFavorite.setImageDrawable(unSelected);
                    return;
                }
                if (sharedPreferences.getInt("favBeach3", -1) != -1 && btnNumber == 3) {
                    editor.putString("favBeachName3", "");
                    editor.putInt("favBeach3", -1);
                    favBeachMap.put("favBeach3", -1);
                    editor.commit();
                    btnFavorite.setImageDrawable(unSelected);
                    Toast.makeText(context,  "הוסר ממועדפים", Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                btnFavorite.setImageDrawable(selected);
                inFavBeach = true;
                addFav(coastId);
                Toast.makeText(context, "נוסף למועדפים", Toast.LENGTH_LONG).show();
            }
        });

        btnWaze = (ImageView) findViewById(R.id.navigateByWazeButton);
        btnWaze.setOnClickListener(v -> {
            String uri = String.format("waze://?ll=%s,%s&navigate=yes",
                    allBeaches.get(coastId - 1).getLatitude(), allBeaches.get(coastId - 1).getLongitude());
            Log.i(TAG, "setPointer: " + allBeaches.get(coastId - 1).getLatitude());
            Log.i(TAG, "setPointer: " + allBeaches.get(coastId - 1).getLongitude());
            Log.i(TAG, allBeaches.get(coastId - 1).getBeachName());
            Log.i(TAG, "CoastId " + coastId);
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(uri)));
            } catch (ActivityNotFoundException e) {
                AlertDialog alert = new AlertDialog.Builder(context).
                        setTitle("Cannot navigate by Waze").
                        setMessage("Could not start navigation by Waze, is it installed on your device?").
                        setNeutralButton("Ok", (dialog, which) -> {
                            dialog.dismiss();
                        }).
                        create();
                alert.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        getBeachDetails();
        //parseCoastDetaildJson(testJson);
        toolbar.setTitle(allBeaches.get(coastId - 1).getBeachName());

        recyclerView = (RecyclerView) findViewById(R.id.dcRecyclerView);
        detailedCoastAdapter = new DetailedCoastAdapter(detailedCoastRecyclerViewList, context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(detailedCoastAdapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor.commit();
    }

    private void addFav(int coastId) {
        String b = allBeaches.get(coastId - 1).getBeachName().replaceFirst(" ","");
        if (sharedPreferences.getInt("favBeach1", -1) == -1) {
            editor.putString("favBeachName1",b);
            editor.putInt("favBeach1", coastId);
            favBeachMap.put("favBeach1", coastId);
            btnNumber = 1;
            editor.commit();
            return;
        } else if (sharedPreferences.getInt("favBeach2", -1) == -1) {
            editor.putString("favBeachName2",b);
            editor.putInt("favBeach2", coastId);
            favBeachMap.put("favBeach2", coastId);
            btnNumber = 2;
            editor.commit();
            return;
        } else if (sharedPreferences.getInt("favBeach3", -1) == -1) {
            editor.putString("favBeachName3",b);
            editor.putInt("favBeach3", coastId);
            favBeachMap.put("favBeach3", coastId);
            btnNumber = 3;
            editor.commit();
            return;
        } else {
            inFavBeach = false;
            btnFavorite.setImageDrawable(getDrawable(R.drawable.favorite_unselected));
            mySnackBar(findViewById(R.id.mainLayout), " הוגדרו כל החופים המועדפים, באפשרותך להסיר חופים מועדפים על ידי לחיצה על הכוכב במסך החוף המועדף");
        }
    }

    private boolean isFave(int coastId) {
        if ((sharedPreferences.getInt("favBeach1", -1) == coastId) ||
                (sharedPreferences.getInt("favBeach2", -1) == coastId) ||
                (sharedPreferences.getInt("favBeach3", -1) == coastId)) return true;
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("מסך פרטי חוף - " + allBeaches.get(coastId - 1).getBeachName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private int checkFav() {
        return sharedPreferences.getInt("favNum", -1);
    }

    private void getBeachDetails() {

        //http://hofim-hofim1.7e14.starter-us-west-2.openshiftapps.com/v1/api/get_full_beach?lat=33.080512&lon=35.105903

        AndroidNetworking.get("http://hofim-hofim1.7e14.starter-us-west-2.openshiftapps.com/v1/api/get_full_beach?lat=" + lat + "&lon=" + lon)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: " + response);
                        parseCoastDetaildJson(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        //TODO add snackber for error
                        //Log.i(TAG, "onError: " + anError);
                    }
                });

    }

    private void parseCoastDetaildJson(String jsonCoastDetailsString) {

        if (jsonCoastDetailsString.length() == 0) {
            showError();
            return;
        }

        try {

            JSONObject metaData = new JSONObject(jsonCoastDetailsString);
            JSONArray hourlyForcast0 = metaData.getJSONArray("weather_hourly");
            JSONArray weather = metaData.getJSONArray("weather_general");
            JSONObject weather0 = weather.getJSONObject(0);
            JSONArray weather1 = weather0.getJSONArray("weather");
            JSONObject dailyStats0 = weather1.getJSONObject(0);
            JSONObject forcast0 = hourlyForcast0.getJSONObject(0);

            Double d =forcast0.getDouble("swellHeight_m") * 100;


            //TODO GET the forcast.....

//            JSONObject dailyForcast0 = dailyForcast.getJSONObject(0);
//            JSONObject dailyStats0 = dailyForcast0.getJSONObject("dailyStats");
//            JSONArray hourlyForcast0 = dailyForcast0.getJSONArray("hourlyForcast");
//            JSONObject hourlyForcast0_0 = hourlyForcast0.getJSONObject(hourOfDay);
//            JSONObject forcast0 = hourlyForcast0_0.getJSONObject("forcast");
//            JSONObject reports = body.getJSONObject("reports");

            //create today DetailedBeachHourlyForcast
            dbhfToday = new DetailedBeachHourlyForecast(
                    forcast0.getString("swellDir16Point"),
                    d.intValue(),
//                    forcast0.getString("windSpeedType"),
                    "Kmph",
                    forcast0.getInt("WindGustKmph"),
//                    forcast0.getString("waterTemperatureType"),
                    "C",
                    forcast0.getInt("waterTemp_C"),
                    forcast0.getString("winddir16Point"),
                    forcast0.getInt("winddirDegree"),
                    forcast0.getString("swellDir16Point"),
                    forcast0.getInt("swellPeriod_secs"),
                    forcast0.getInt("humidity"),
                    forcast0.getInt("FeelsLikeC"),
                    forcast0.getInt("tempC"),
                    forcast0.getInt("windspeedKmph"),
                    metaData.getString("_id"),
                    metaData.getString("name"),
                    metaData.getBoolean("blue_flag"),
                    metaData.getBoolean("disabilities_status"),
                    metaData.getString("city"),
//                    reports.getString("jellyFishType"),
//                    reports.getString("cleanType"),
                    "NONE",
                    "cleanType",
                    dailyStats0.getInt("maxtempC"),
                    dailyStats0.getInt("mintempC")
            );

            //build list for recycler view
            DetailedCoastRecyclerView dcrvWaveHeightType = new DetailedCoastRecyclerView(
                    getString(R.string.WaveFrequency),
                    dbhfToday.getWaveheighttype(),
                    R.drawable.high_wave);

            DetailedCoastRecyclerView dcrvWaveHeight = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.wave_height)),
                    (String.valueOf(dbhfToday.getWaveheightvalueCm()) +
                            " " + getResources().getString(R.string.centimeters)),
                    R.drawable.high_wave);

            DetailedCoastRecyclerView dcrvWindSpeedType = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.windSpeedType)),
                    dbhfToday.getWindspeedtype(),
                    R.drawable.wind_temp_c);

            DetailedCoastRecyclerView dcrvWindSpeed = new DetailedCoastRecyclerView(
                    getResources().getString(R.string.wind_speed),
                    (String.valueOf(dbhfToday.getWindspeedvalueKmph()) +
                            " " + getResources().getString(R.string.kmph)),
                    R.drawable.wind_speed_old);

            DetailedCoastRecyclerView dcrvWaterTemperatureType = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.waterTemperatureType)),
                    dbhfToday.getWatertemperaturetype(),
                    R.drawable.wather_temp);

            DetailedCoastRecyclerView dcrvWaterTemp = new DetailedCoastRecyclerView(
                    getResources().getString(R.string.water_temperature),
                    (String.valueOf(dbhfToday.getWatertemperaturevalueC()) +
                            getResources().getString(R.string.celsious)),
                    R.drawable.wather_temp);

            DetailedCoastRecyclerView dcrvwindDirectionType = new DetailedCoastRecyclerView(
                    getResources().getString(R.string.wind_bearing),
                    windBearing.get(dbhfToday.getWinddirectiontype()) + " (" +
                            String.valueOf(dbhfToday.getWinddirectionvalueDeg()) + "\u00B0" + ")",
                    R.drawable.wind_temp_c);

            DetailedCoastRecyclerView dcrvWindDirectionValueDeg = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.windDirectionValue_Deg)),
                    String.valueOf(dbhfToday.getWinddirectionvalueDeg()),
                    R.drawable.wind_bearing);

            DetailedCoastRecyclerView dcrvWaveDirection = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.waveDirection)),
                    windBearing.get(dbhfToday.getWavedirection()),
                    R.drawable.wind_temp_c);

            DetailedCoastRecyclerView dcrvWaveFrequancyPerSec = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.waveFrequancyPerSec)),
                    String.valueOf(dbhfToday.getWaveFrequancyPerSec()),
                    R.drawable.wave_height);

            DetailedCoastRecyclerView dcrvHumidityPercent = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.humidityPercent)),
                    String.valueOf(dbhfToday.getHumidityPercent()),
                    R.drawable.radiation);

            DetailedCoastRecyclerView dcrvAirTemperatureFeelsLike_C = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.airTemperatureFeelsLikeC)),
                    String.valueOf(dbhfToday.getAirTemperatureFeelsLike_C()),
                    R.drawable.radiation);

            DetailedCoastRecyclerView dcrvAirTemperature_C = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.airTemperature_C)),
                    String.valueOf(dbhfToday.getAirmaxtemperatureC()) + "\u00B0" + "-" +
                            String.valueOf(dbhfToday.getAirmintemperatureC()) + "\u00B0" +
                            " (" + String.valueOf(dbhfToday.getAirTemperature_C()) + "\u00B0" + ")",
                    R.drawable.air_temp_c);

            DetailedCoastRecyclerView dcrvWindGustValue_Kmph = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.windGustValue_Kmph)),
                    String.valueOf(dbhfToday.getWindGustValue_Kmph()),
                    R.drawable.wind_speed_old);

            DetailedCoastRecyclerView dcrvAirMaxTemperature_C = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.airMaxTemperature_C)),
                    String.valueOf(dbhfToday.getAirmaxtemperatureC()),
                    R.drawable.radiation);

            DetailedCoastRecyclerView dcrvAirMinTemperature_C = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.airMinTemperature_C)),
                    String.valueOf(dbhfToday.getAirmintemperatureC()),
                    R.drawable.radiation);

            DetailedCoastRecyclerView dcrvRadiation = new DetailedCoastRecyclerView(
                    getResources().getString(R.string.radiation),
                    "נמוכה",
                    R.drawable.radiation);

            DetailedCoastRecyclerView dcrvContamination = new DetailedCoastRecyclerView(
                    getResources().getString(R.string.contamination),
                    "נמוכה",
                    R.drawable.radiation);

            DetailedCoastRecyclerView dcrvCrowed = new DetailedCoastRecyclerView(
                    getResources().getString(R.string.crowded),
                    "נמוכה",
                    R.drawable.radiation);

            DetailedCoastRecyclerView dcrvJellyfish = new DetailedCoastRecyclerView(
                    getResources().getString(R.string.jellyfish),
                    jellyType.get(dbhfToday.getJellyFishType()),
                    getJellyIcon(dbhfToday.getJellyFishType()));

            detailedCoastRecyclerViewList.add(dcrvWaveHeight);
            detailedCoastRecyclerViewList.add(dcrvAirTemperature_C);
            detailedCoastRecyclerViewList.add(dcrvWaterTemp);
//            detailedCoastRecyclerViewList.add(dcrvWaveDirection);
            detailedCoastRecyclerViewList.add(dcrvwindDirectionType);
            //detailedCoastRecyclerViewList.add(dcrvWindDirectionValueDeg);
            detailedCoastRecyclerViewList.add(dcrvWindSpeed);
            //detailedCoastRecyclerViewList.add(dcrvAirMaxTemperature_C);
            //detailedCoastRecyclerViewList.add(dcrvAirMinTemperature_C);
            detailedCoastRecyclerViewList.add(dcrvJellyfish);

            //detailedCoastRecyclerViewList.add(dcrvAirTemperatureFeelsLike_C);
            //detailedCoastRecyclerViewList.add(dcrvWaterTemperatureType);
            //detailedCoastRecyclerViewList.add(dcrvWaveHeightType);
            //detailedCoastRecyclerViewList.add(dcrvWindSpeedType);
            //detailedCoastRecyclerViewList.add(dcrvWaveFrequancyPerSec);
            //detailedCoastRecyclerViewList.add(dcrvHumidityPercent);
            //detailedCoastRecyclerViewList.add(dcrvWindGustValue_Kmph);

            btnBlueFlag.setVisibility((dbhfToday.isBlueFlag()) ? View.VISIBLE : View.INVISIBLE);
            btnHandycapFriendly.setVisibility((dbhfToday.isHandicappedFriendly()) ? View.VISIBLE : View.INVISIBLE);
//            btnFavorite.setImageDrawable(isFave(coastId) ?
//                    getDrawable(R.drawable.favorite_selected) :
//                    getDrawable(R.drawable.favorite_unselected));

            detailedCoastAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
            //Log.i(TAG, "parseCoastDetaildJson: " + e);
        }
    }

    private void showError() {
        //TODO test the snackbar (No Coordinator, Only relative)
        Snackbar.make(mainLayout, "Error connecting to server, please try again in a few minutes", BaseTransientBottomBar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    private int getJellyIcon(String jellyfishtype) {
        jellyfishtype = jellyfishtype.toLowerCase();
        switch (jellyfishtype) {
            case "none":
                return R.mipmap.ic_launcher_no_jell;
            //return R.drawable.no_m;
            case "few":
                return R.mipmap.ic_launcher_lit_jell;
            //return R.drawable.few_m;
            case "some":
                return R.mipmap.ic_launcher_lit_jell;
            //return R.drawable.few_m;
            case "lots":
                return R.mipmap.ic_launcher_many_jell;
            //return R.drawable.alot_m;
        }
        return R.drawable.no_jellyfish;
        //return R.drawable.no_m;

    }
}

