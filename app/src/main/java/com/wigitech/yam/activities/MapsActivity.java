package com.wigitech.yam.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchableInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
 import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.github.akashandroid90.imageletter.MaterialLetterIcon;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.wigitech.yam.Analytics.HofimAnalytics;
import com.wigitech.yam.R;
import com.wigitech.yam.adapters.BeachesRecyclerAdapter;
import com.wigitech.yam.adapters.CoastMarkerInfoWindowAdapter;
import com.wigitech.yam.dataacess.JellyFishReportService;
import com.wigitech.yam.fragments.BlankFragment;
import com.wigitech.yam.model.Beach;
import com.wigitech.yam.model.Jellyfish;
import com.wigitech.yam.utils.Config;
import com.wigitech.yam.utils.NotificationUtils;

import org.androidannotations.rest.spring.annotations.Post;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.wigitech.yam.activities.MainActivity.lastLocation;
import static com.wigitech.yam.utils.Utils.mySnackBar;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationSource.OnLocationChangedListener{

    private final String TAG = "TAG";
    private final String sharedPreferencesName = "favBeach";
    private GoogleMap mMap;
    private Context context = MapsActivity.this;
    private Toolbar toolbar;
    private BoomMenuButton bmbJelly;
    private int[] bmbImage;
    private String[] bmbText;
    private ImageView focusMyLocation;
    private MaterialLetterIcon favoriteCoastButton1;
    private MaterialLetterIcon favoriteCoastButton2;
    private MaterialLetterIcon favoriteCoastButton3;
    private final int ZOOM_AMOUNT = 10;
    public static List<Beach> allBeaches = new ArrayList<>();
    private boolean isMarkersOn = false;
    private GoogleApiClient googleApiClient;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private boolean backPressed = false;
    private ConstraintLayout mainView;
    private Tracker mTracker;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static Map<String, Integer> favBeachMap = new HashMap<>();
    private LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    private RecyclerView mRecyclerView;
    private ImageButton imSearch;
    private ImageButton imManu;
    private ImageButton imSinon;
    private android.support.v7.widget.SearchView wsvHofim;
    private Boolean handy;
    private Boolean green;
    private Boolean park;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        MapsActivity mapsActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coast_map);
        getAllBeaches();
        mRecyclerView = (RecyclerView) findViewById(R.id.rvResult);
        setPointer();
        wsvHofim = (android.support.v7.widget.SearchView) findViewById(R.id.wsvSearch);
        SearchView.SearchAutoComplete theTextArea =(SearchView.SearchAutoComplete) wsvHofim.findViewById(R.id.search_src_text);
        theTextArea.setTextColor(Color.WHITE);
        theTextArea.setHintTextColor(Color.WHITE);
        wsvHofim.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                imSearch.setVisibility(View.VISIBLE);
                imManu.setVisibility(View.VISIBLE);
                imSinon.setVisibility(View.GONE);
                wsvHofim.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
//                getFullBeaches();
                return false;
            }
        });

        wsvHofim.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }
            List<Beach> result = new ArrayList<>();

            @Override
            public boolean onQueryTextChange(String newText) {
                    result.removeAll(result);
                    int v = allBeaches.size();
                    String beach = "חוף ";
                    if (!beach.contains(newText)) {
                        for (int i = 0; i < allBeaches.size(); i++) {
                            if (allBeaches.get(i).getBeachName().toLowerCase().contains(newText)) {
                                result.add(allBeaches.get(i));
                            }

                        }
                    }
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerView.setAdapter(new BeachesRecyclerAdapter(getApplicationContext(), result));
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(context, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Beach b = result.get(position);
                                    for (int i = 0; i < allBeaches.size(); i++) {
                                        if (allBeaches.get(i).getBeachId() == b.getBeachId()) {
                                            b = allBeaches.get(i + 1);
                                            break;
                                        }
                                    }

                                    Intent intent = new Intent(MapsActivity.this, CoastDetailsActivity2.class);
                                    intent.putExtra("coastId", b.getBeachId());
                                    intent.putExtra("beachName", b.getBeachName());
                                    intent.putExtra("lat", b.getLatitude());
                                    intent.putExtra("lon", b.getLongitude());
                                    if (sharedPreferences.getInt("favBeach1", -1) == b.getBeachId() |
                                            sharedPreferences.getInt("favBeach2", -1) == b.getBeachId() |
                                            sharedPreferences.getInt("favBeach3", -1) == b.getBeachId()) {
                                        intent.putExtra("fromFav", true);
                                    } else {
                                        intent.putExtra("fromFav", false);
                                    }


                                    startActivity(intent);
                                }

                                @Override
                                public void onLongItemClick(View view, int position) {
                                    // do whatever
                                }
                            })
                    );
                return false;
            }
        });
        imSearch = (ImageButton) findViewById(R.id.imSearch);
        imManu = (ImageButton) findViewById(R.id.imManu);
        imManu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about = new Intent(context, AboutActivity.class);
                startActivity(about);
            }
        });
        imSinon = (ImageButton) findViewById(R.id.imSinon);
        imSinon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSinon();
            }
        });
        imSinon.setVisibility(View.GONE);
        imSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 imSearch.setVisibility(View.GONE);
                 imManu.setVisibility(View.GONE);
                 imSinon.setVisibility(View.VISIBLE);
                 wsvHofim.setVisibility(View.VISIBLE);
//                 wsvHofim.setQueryHint("חפש חוף....");
                 wsvHofim.setFocusable(true);
                 wsvHofim.setIconified(false);

            }
        });



    }

    private void setPointer() {

        mainView = (ConstraintLayout) findViewById(R.id.mainView);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

      //  this.MapsActivity.this = this;

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    //displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    mySnackBar(mainView,message);

                    //txtMessage.setText(message);
                }
            }
        };

        lastLocation = new Location("");
        lastLocation.setLongitude(32.142858);
        lastLocation.setLongitude(34.792113);

        //getLastLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        HofimAnalytics hofimAnalytics = (HofimAnalytics) getApplication();
        mTracker = hofimAnalytics.getDefaultTracker();
        // [END shared_tracker]

        //check if location is enebled
        checkLocationEnabed();
        //jellyfish report boomButton
        bmbJelly = (BoomMenuButton) findViewById(R.id.bmb_jelly);
        initBMB();

        //shared preferences
        sharedPreferences =  getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //focus my location button
        focusMyLocation = (ImageView) findViewById(R.id.focus_my_location_button);
        focusMyLocation.setOnClickListener(v -> {
            focusMyLocation();
        });

        favoriteCoastButton1 = (MaterialLetterIcon) findViewById(R.id.open_favorite_coast1);
        favoriteCoastButton2 = (MaterialLetterIcon) findViewById(R.id.open_favorite_coast2);
        favoriteCoastButton3 = (MaterialLetterIcon) findViewById(R.id.open_favorite_coast3);

        bestGPS();

        try {
            focusMyLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void bestGPS() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //create a location manger by system services , get all provider inside
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //declare location by GPS satelite location -> GPS PROVIDER
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //declare location by cellular NETWORK - > NETWORK PROVIDER.
        Location locationNET = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //zero the GPS location time
        long GPSLocationTime = 0;
        if (locationGPS != null) {
            //get GPS location time
            GPSLocationTime = locationGPS.getTime();
        }
        //zero the NETWORK location time
        long NETLocationTime = 0;
        if (locationNET != null) {
            //get NETWORK location time
            NETLocationTime = locationNET.getTime();
        }
        //if GPSlocation - NETLocation is greater then 0 , the GPSLocation is higher then NETlocation, more accurecy
        if (GPSLocationTime - NETLocationTime > 0) {
            //use the locationGPS
            lastLocation = locationGPS;
        } else {
            //use the locationNet
            lastLocation = locationNET;
        }

        //Toast.makeText(MapsActivity.this, "" + lastLocation.getLatitude(), Toast.LENGTH_SHORT).show();
    }


    private void checkLocationEnabed() {
        LocationManager lm = (LocationManager)  getSystemService(LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(MapsActivity.this.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(MapsActivity.this.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MapsActivity.this.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(MapsActivity.this.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    bmbJelly.setVisibility(View.GONE);
                }
            });
            dialog.show();
        }
    }

    private void getAllBeaches() {

        //TODO get normal sqlite for Coast Model
//        http://hofim-hofim1.7e14.starter-us-west-2.openshiftapps.com/v1/api/get_full_beach?lat=" + lat + "&lon=" + lon
        AndroidNetworking.initialize(MapsActivity.this);
        AndroidNetworking.get("http://hofim-hofim1.7e14.starter-us-west-2.openshiftapps.com/v1/api/get_beaches_coords_country?country=Israel")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject data = new  JSONObject(response);
                            JSONArray jsonAllCoasts = data.getJSONArray("data");
                            allBeaches.clear();
                            for (int i = 0; i < jsonAllCoasts.length(); i++) {
                                JSONObject jsonCoast = jsonAllCoasts.getJSONObject(i);
                                Beach mbeach = new Beach();
                                mbeach.setBeachId(i);
                                mbeach.setBeachName(jsonCoast.getString("name"));
                                mbeach.setLongitude(Double.valueOf(jsonCoast.getString("lon")));
                                mbeach.setLatitude(Double.valueOf(jsonCoast.getString("lat")));
//                               gson.fromJson(jsonCoast.toString(), Beach.class);
                                Log.i(TAG, "" + mbeach.getBeachName());
                                Log.i(TAG, "i " + i + "----" + mbeach.getBeachId());
                                allBeaches.add(mbeach);
//                                result.add(mbeach);
                                Log.i(TAG, "allBeaches: " + allBeaches.size());
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Log.i("TAG", "onResponse: " + e);
                        }
                        if (!isMarkersOn) {
                            setBeachMarkers();
                            isMarkersOn = true;
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    private List getFullBeaches(){
        AndroidNetworking.initialize(MapsActivity.this);
        List <Beach> fullBeches =new ArrayList<>();
        Thread n = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <allBeaches.size() ; i++) {
                    AndroidNetworking.get(
                            "http://hofim-hofim1.7e14.starter-us-west-2.openshiftapps.com/v1/api/get_full_beach?lat=" + allBeaches.get(i).getLatitude() + "&lon=" +  allBeaches.get(i).getLongitude())
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject metaData = new  JSONObject(response);
                                        Beach b = new Beach();
                                        b.setBeachId(Integer.getInteger(metaData.getString("_id")));
                                        Log.i("TAG", "onResponse: " + metaData);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {

                                }
                            });
                }
            }
        });
        n.start();
        return fullBeches;
    }

    private void focusMyLocation() {
        if (lastLocation == null) {
            return;
        }
        bestGPS();
        LatLng coordinates = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        //Log.d("locs", coordinates.toString());
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 10));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10));
    }


    private void initBMB() {
        //images for bmb
        //bmbImage = new int[]{R.drawable.no_m, R.drawable.few_m, R.drawable.alot_m,
        //        R.drawable.white_flag, R.drawable.red_flag, R.drawable.black_flag}
        bmbImage = new int[]{R.mipmap.ic_launcher_no_jell, R.mipmap.ic_launcher_lit_jell, R.mipmap.ic_launcher_many_jell,};
        //text for bmb
        //bmbText = new String[]{getString(R.string.no_jellyfish), getString(R.string.few_jellyfish), getString(R.string.some_jellyfish),
        //            getString(R.string.whiteFlag), getString(R.string.redFlag), getString(R.string.blackFlag)};
        bmbText = new String[]{getString(R.string.no_jellyfish), getString(R.string.few_jellyfish), getString(R.string.some_jellyfish)};
        //builder for bmb
        for (int i = 0; i < bmbJelly.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .shadowEffect(false)
                    .normalImageRes(bmbImage[i])
                    .normalText(bmbText[i])
                    .textSize(12)
                    .rippleEffect(true)
                    .normalColor(Color.argb(00, 00, 00, 00))
                    .normalTextColor(Color.WHITE)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            boomButtonClick(index);
                        }

                        private void boomButtonClick(int index) {

                            Jellyfish mjellyfish = new Jellyfish();

                            String deviceId = "000000000000000";

                            deviceId = String.valueOf(new Random().nextInt(5));

                            // building Jellyfish report)
                            long timeStamp = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
                            mjellyfish.setTimeStamp(String.valueOf(timeStamp));
                            mjellyfish.setTimeZone("Israel");
                            mjellyfish.setCountry("Israel");
                            mjellyfish.setUser("TESTING");
                            mjellyfish.setPassword("TESTING123");
                            mjellyfish.setDeviceId(deviceId);
                            try {
                                mjellyfish.setLatitude(lastLocation.getLatitude());
                            } catch (Exception e) {
                                e.printStackTrace();
                                mjellyfish.setLatitude(0.0);
                            }
                            try {
                                mjellyfish.setLongitude(lastLocation.getLongitude());
                            } catch (Exception e) {
                                e.printStackTrace();
                                mjellyfish.setLongitude(0.0);
                            }

                            switch (index) {

                                case 0:
                                    mjellyfish.setType("jellyfish");
                                    mjellyfish.setLevel(0);
                                    mTracker.send(new HitBuilders.EventBuilder()
                                            .setCategory("דיווח מדוזות")
                                            .setAction("אין מדוזות")
                                            .build());
                                    break;
                                case 1:
                                    mjellyfish.setType("jellyfish");
                                    mjellyfish.setLevel(1);
                                    mTracker.send(new HitBuilders.EventBuilder()
                                            .setCategory("דיווח מדוזות")
                                            .setAction("מעט מדוזות")
                                            .build());
                                    break;
                                case 2:
                                    mjellyfish.setType("jellyfish");
                                    mjellyfish.setLevel(2);
                                    mTracker.send(new HitBuilders.EventBuilder()
                                            .setCategory("דיווח מדוזות")
                                            .setAction("הרבה מדוזות")
                                            .build());
                                    break;
                            }
                            Log.i(TAG, "boomButtonClick: " + mjellyfish.getTimeStamp());
//                            reportJellys(mjellyfish);
                            new Thread(new Runnable() {
                                public void run() {
                                    // a potentially  time consuming task
                                    try {
                                        makePostRequest("http://hofim-development-hofim1.7e14.starter-us-west-2.openshiftapps.com/v1/api/report",
                                               "{\n" +
                                                       "    \"lat\" :" +mjellyfish.getLatitude() +" ,\n" +
                                                       "    \"lon\" :" +mjellyfish.getLongitude() + " ,\n" +
                                                       "    \"type\" : \"jellyfish\",\n" +
                                                       "    \"level\" :" +mjellyfish.getLevel() +" \n" +
                                                       "}", context, mainView);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }
                    });
            bmbJelly.addBuilder(builder);
        }
    }

    private void reportJellys(Jellyfish mjellyfish) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hofim-hofim1.7e14.starter-us-west-2.openshiftapps.com/v1/api/report/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JellyFishReportService jellyFishReportService = retrofit.create(JellyFishReportService.class);

        Call<Jellyfish> call = jellyFishReportService.reportJellyFish(mjellyfish);

        Log.i("jell", "reportJellys: " + mjellyfish.toString());

        call.enqueue(new Callback<Jellyfish>() {
            @Override
            public void onResponse(Call<Jellyfish> call, Response<Jellyfish> response) {
                mySnackBar(mainView, "תודה על הדיווח");
                Log.i("jell", "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<Jellyfish> call, Throwable t) {
                mySnackBar(mainView, "מצטערים, כרגע אין באפשרותך לדווח");
                //Toast.makeText(MapsActivity.this, "מצטערים, כרגע אין באפשרותך לדווח", Toast.LENGTH_SHORT).show();
                Log.i("jell", "onFailure: " + t);
            }
        });
    }

    public static String makePostRequest(String stringUrl, String payload,
                                         Context context,View mainView) throws IOException {
        URL url = new URL(stringUrl);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        String line;
        StringBuffer jsonString = new StringBuffer();

        uc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        uc.setRequestMethod("POST");
        uc.setDoInput(true);
        uc.setInstanceFollowRedirects(true);
        uc.connect();
        OutputStreamWriter writer = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
        writer.write(payload);
        writer.close();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            while((line = br.readLine()) != null){
                jsonString.append(line);
            }
            br.close();
            mySnackBar(mainView, "תודה על הדיווח");
        } catch (Exception ex) {
            ex.printStackTrace();
            mySnackBar(mainView, "מצטערים, כרגע אין באפשרותך לדווח");
        }
        uc.disconnect();
        return jsonString.toString();
    }
    private void getFavBeaches() {
        if (sharedPreferences.getAll().isEmpty()) {
            editor.putString("favBeachName1", "");
            editor.putString("favBeachName2", "");
            editor.putString("favBeachName3", "");
            editor.putInt("favBeach1", -1);
            editor.putInt("favBeach2", -1);
            editor.putInt("favBeach3", -1);
            editor.commit();
            favBeachMap.put("favBeach1", -1);
            favBeachMap.put("favBeach2", -1);
            favBeachMap.put("favBeach3", -1);
        } else {
            favoriteCoastButton1.setImageDrawable(getDrawable(R.drawable.favorite_unselected));
            favoriteCoastButton2.setImageDrawable(getDrawable(R.drawable.favorite_unselected));
            favoriteCoastButton3.setImageDrawable(getDrawable(R.drawable.favorite_unselected));

//            Log.i("TAG", "getFavBeaches1: " + (sharedPreferences.getString("favBeach1", "")));
//            Log.i("TAG", "getFavBeaches2: " + (sharedPreferences.getString("favBeach2","")));
//            Log.i("TAG", "getFavBeaches3: " + (sharedPreferences.getString("favBeach3","")))
            String d3 = sharedPreferences.getString("favBeachName3", "AA");
            String d1 = sharedPreferences.getString("favBeachName1", "AA");
            String d2 = sharedPreferences.getString("favBeachName2", "AA");
                if (sharedPreferences.getInt("favBeach1", -1) != -1) {
                    favoriteCoastButton1.setLetter(d1);
                }
                if (sharedPreferences.getInt("favBeach2", -1) != -1) {
                    favoriteCoastButton2.setLetter(d2);
                }
                if (sharedPreferences.getInt("favBeach3", -1) != -1) {
                    favoriteCoastButton3.setLetter(d3);
                }

        }
        favoriteCoastButton1.setOnClickListener(v -> {
            if (sharedPreferences.getString("favBeachName1", "").equals("")) {
                mySnackBar(mainView, "לא הוגדר חוף מועדף לכפתור זה, באפשרותך להוסיף חופים מועדפים על ידי לחיצה על הכוכב במסך חוף");
                return;
            } else {
                Intent intent = new Intent(context, CoastDetailsActivity2.class);
                intent.putExtra("coastId", sharedPreferences.getInt("favBeach1", -1));
                intent.putExtra("btnNumber", 1);
                intent.putExtra("lat", allBeaches.get(sharedPreferences.getInt("favBeach1", -1)).getLatitude());
                intent.putExtra("lon", allBeaches.get(sharedPreferences.getInt("favBeach1", -1)).getLongitude());
                intent.putExtra("fromFav", true);
                //intent.putExtra("beachName", sharedPreferences.getString("favBeachName1", ""));
                startActivity(intent);
            }
        });

        favoriteCoastButton2.setOnClickListener(v -> {
            if (sharedPreferences.getString("favBeachName2", "").equals("")) {
                mySnackBar(mainView, "לא הוגדר חוף מועדף לכפתור זה, באפשרותך להוסיף חופים מועדפים על ידי לחיצה על הכוכב במסך חוף");
                return;
            } else {

                Intent intent = new Intent(context, CoastDetailsActivity2.class);
                intent.putExtra("coastId", sharedPreferences.getInt("favBeach2", -1));
                intent.putExtra("btnNumber", 2);
                intent.putExtra("lat", allBeaches.get(sharedPreferences.getInt("favBeach2", -1)).getLatitude());
                intent.putExtra("lon", allBeaches.get(sharedPreferences.getInt("favBeach2", -1)).getLongitude());
                intent.putExtra("fromFav", true);
                //intent.putExtra("beachName", sharedPreferences.getString("favBeachName2", ""));
                startActivity(intent);
            }
        });

        favoriteCoastButton3.setOnClickListener(v -> {
            if (sharedPreferences.getString("favBeachName3", "").equals("")) {
                mySnackBar(mainView, "לא הוגדר חוף מועדף לכפתור זה, באפשרותך להוסיף חופים מועדפים על ידי לחיצה על הכוכב במסך חוף");
                return;
            } else {
                Intent intent = new Intent(context, CoastDetailsActivity2.class);
                intent.putExtra("coastId", sharedPreferences.getInt("favBeach3", -1));
                intent.putExtra("btnNumber", 3);
                intent.putExtra("lat", allBeaches.get(sharedPreferences.getInt("favBeach3", -1)).getLatitude());
                intent.putExtra("lon", allBeaches.get(sharedPreferences.getInt("favBeach3", -1)).getLongitude());
                intent.putExtra("fromFav", true);
                //intent.putExtra("beachName", sharedPreferences.getString("favBeachName3", ""));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent intent = new Intent(MapsActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            lastLocation = task.getResult();

                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                        }
                    }
                });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        googleApiClient = new GoogleApiClient.Builder(this).
//                addConnectionCallbacks(this).
//                addOnConnectionFailedListener(this).
//                addApi(LocationServices.API).build();
//        googleApiClient.connect();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //location services
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        lastLocation = locationManager.getLastKnownLocation(provider);

        mMap.setInfoWindowAdapter(new CoastMarkerInfoWindowAdapter(MapsActivity.this));
        mMap.setOnInfoWindowClickListener(marker -> {
            //Log.i(TAG, "marker: " + marker.getId());
            int markerId = Integer.parseInt(marker.getId().replaceAll("[\\D]", ""));
            //Log.i(TAG, "new id: " + markerId);
            Intent intent = new Intent(MapsActivity.this, CoastDetailsActivity2.class);
            intent.putExtra("coastId", markerId);
            intent.putExtra("lat", allBeaches.get(markerId).getLatitude());
            intent.putExtra("lon", allBeaches.get(markerId).getLongitude());
            intent.putExtra("beachName", allBeaches.get(markerId).getBeachName());
            if(sharedPreferences.getInt("favBeach1", -1) == markerId |
                    sharedPreferences.getInt("favBeach2", -1) == markerId |
                    sharedPreferences.getInt("favBeach3", -1) == markerId){
                intent.putExtra("fromFav", true);
            } else {intent.putExtra("fromFav", false);}
            startActivity(intent);
        });

        //remove default map buttons
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setMapToolbarEnabled(false);

        //get List of all the beaches

        //set all the beach markers
        setBeachMarkers();

        bestGPS();

        try {
            LatLng coord = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 10));
        } catch (Exception e) {
            e.printStackTrace();
            Location fakeLocation = new Location("fake");
            fakeLocation.setLatitude(32.142858d);
            fakeLocation.setLongitude(34.792113d);
            LatLng coord = new LatLng(fakeLocation.getLatitude(),fakeLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 10));
        }
//        finally {
//            Location fakeLocation = new Location("fake");
//            fakeLocation.setLatitude(32.142858d);
//            fakeLocation.setLongitude(34.792113d);
//            LatLng coord = new LatLng(fakeLocation.getLatitude(),fakeLocation.getLongitude());
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 10));
//        }


    }

    private void setBeachMarkers() {
        final int beachListSize = allBeaches.size();
        for (int i = 0; i < beachListSize; i++) {
            LatLng c = new LatLng(allBeaches.get(i).getLatitude(), allBeaches.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(c)
                    .title(allBeaches.get(i).getBeachName()));
            //mMap.animateCamera(CameraUpdateFactory.newLatLng(c));
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressed) {
            finish();
        } else {
            mySnackBar(mainView, getString(R.string.close_app));
            backPressed = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("מסך ראשי - מפה");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(this);
        getFavBeaches();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        LatLng coordinates = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        //Log.d("locs", coordinates.toString());
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 10));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, ZOOM_AMOUNT));
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
    }


//    @Override
//    public void onObjectReady(String title, int position) {
//        Intent intent = new Intent(MapsActivity.this, CoastDetailsActivity2.class);
//        intent.putExtra("coastId", position);
//        startActivity(intent);
//    }
    private Boolean showSinon(){
        final Dialog mBottomSheetDialog = new Dialog(context, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.sinon_popup); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        TextView tvPop = mBottomSheetDialog.findViewById(R.id.tvDistance);
        RadioButton cbPark = mBottomSheetDialog.findViewById(R.id.cbPark);
        RadioButton cbhandy = mBottomSheetDialog.findViewById(R.id.cbHandy);
        RadioButton cbGreen = mBottomSheetDialog.findViewById(R.id.cbGreen);
        cbGreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                green = cbGreen.isChecked();
                Toast.makeText(context, "" + green, Toast.LENGTH_LONG).show();
            }
        });
        Button etDistance = mBottomSheetDialog.findViewById(R.id.etDistance);
        etDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "jhgkjv", Toast.LENGTH_LONG).show();
                cbGreen.setChecked(false);
                cbhandy.setChecked(false);
                cbPark.setChecked(false);
            }
        });
        Button btnPop = mBottomSheetDialog.findViewById(R.id.btn_close_popup);
        btnPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("pop","close");
                mBottomSheetDialog.dismiss();
            }
        });
        return true;
    }

//    public void clearChoise() {
//        Toast.makeText(context, "jhgkjv", Toast.LENGTH_LONG).show();
//        RadioButton cbPark = (RadioButton) findViewById(R.id.cbPark);
//        RadioButton cbhandy = (RadioButton) findViewById(R.id.cbHandy);
//        RadioButton cbGreen = (RadioButton) findViewById(R.id.cbGreen);
//        cbGreen.setChecked(false);
////        cbhandy.setChecked(false);
////        cbPark.setChecked(false);
//    }
}

