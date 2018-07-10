package com.wigitech.yam.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.wigitech.yam.Analytics.HofimAnalytics;
import com.wigitech.yam.R;
import com.wigitech.yam.dataacess.CoastDataService;
import com.wigitech.yam.dataacess.JellyFishReportService;
import com.wigitech.yam.model.Beach;
import com.wigitech.yam.model.Jellyfish;
import com.wigitech.yam.services.FavoriteCoastsService;
import com.wigitech.yam.services.MapCoastLocationsService;
import com.wigitech.yam.adapters.CoastMarkerInfoWindowAdapter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EActivity
public class CoastMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //region Consts
    public static String FACEBOOK_URL = "https://www.facebook.com/1544844929145939";
    public static String FACEBOOK_PAGE_ID = "1544844929145939";
    private final String sharedPreferencesName = "favBeach";
    private final String name = "Android Main Screen";

    private final int REQUEST_LOCATION_PERMISSION = 123; //some made up request code const
    private final int REQUEST_TELEPHONY_PERMISSION = 124;
    private static final int LOCATION_UPDATE_INTERVAL_IN_MILLI = 5000;
    private static final long LOCATION_UPDATE_FASTEST_INTERVAL_IN_MILLI = 1000;
    private static final int ZOOM_AMOUNT = 10;
    public static final String LOCATION_KEY = "locationKey";
    private Tracker mTracker;
    private int[] bmbImage;
    private String[] bmbText;

    //endregion

    //region Fields
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TelephonyManager telephonyManager;
    private boolean isExit;
    private Context context;
    //public static List<Beach> allBeaches;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private boolean mIsLocationPermissionAvailable = false;
    private boolean mIsTelephonyPermissionAvailable = false;
    private boolean mIsFocusingMyLocationEnabled = true;
    private final Object mPermissionsLock = new Object();
    private LocationRequest mLocationRequest;
    public static Stack<Integer> favBeachStack = new Stack<>();
    public static Map<String, Integer> favBeachMap = new HashMap<>();
    private Map<ImageView, Integer> mFavoriteButtonToCoastIdMap =
            new HashMap<>(FavoriteCoastsService.NUM_FAVORITE_COASTS);

    private GoogleMap mMap;


    @ViewById(R.id.focus_my_location_button)
    ImageView mFocusMyLocationButton;
    @ViewById(R.id.open_favorite_coast1)
    ImageView mFavoriteCoastButton1;
    @ViewById(R.id.open_favorite_coast2)
    ImageView mFavoriteCoastButton2;
    @ViewById(R.id.open_favorite_coast3)
    ImageView mFavoriteCoastButton3;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.bmb_jelly)
    BoomMenuButton bmbJelly;


    @Bean
    MapCoastLocationsService mMapCoastLocationService;
    @Bean
    FavoriteCoastsService mFavoriteCoastsService;
    @RestService
    CoastDataService mCoastDataService;
    //endregion


    //region FragmentActivity Overridden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coast_map);
        this.context = this;
        sharedPreferences = context.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        HofimAnalytics hofimAnalytics = (HofimAnalytics) getApplication();
        mTracker = hofimAnalytics.getDefaultTracker();
        // [END shared_tracker]

        //set the toolbar
        setSupportActionBar(toolbar);
        //InitDrawer();
        initBMB();
        getAllBeaches();
        getFavBeaches();
        init(savedInstanceState);
        initMap();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.menu_main);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(context, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void getFavBeaches() {
        if (sharedPreferences.getAll().isEmpty()) {
            editor.putInt("favBeach1", -1);
            editor.putInt("favBeach2", -1);
            editor.putInt("favBeach3", -1);
            editor.commit();
            favBeachMap.put("favBeach1", -1);
            favBeachMap.put("favBeach2", -1);
            favBeachMap.put("favBeach3", -1);
        } else {
            Log.i("TAG", "getFavBeaches1: " + (sharedPreferences.getInt("favBeach1", -1) != -1));
            Log.i("TAG", "getFavBeaches2: " + (sharedPreferences.getInt("favBeach2", -1) != -1));
            Log.i("TAG", "getFavBeaches3: " + (sharedPreferences.getInt("favBeach3", -1) != -1));
            mFavoriteCoastButton1.setImageDrawable(
                    (sharedPreferences.getInt("favBeach1", -1) != -1) ?
                            getDrawable(R.drawable.favorite_selected) :
                            getDrawable(R.drawable.favorite_unselected));
            mFavoriteCoastButton2.setImageDrawable(
                    (sharedPreferences.getInt("favBeach2", -1) != -1) ?
                            getDrawable(R.drawable.favorite_selected) :
                            getDrawable(R.drawable.favorite_unselected));
            mFavoriteCoastButton3.setImageDrawable(
                    (sharedPreferences.getInt("favBeach3", -1) != -1) ?
                            getDrawable(R.drawable.favorite_selected) :
                            getDrawable(R.drawable.favorite_unselected));
        }

        mFavoriteCoastButton1.setOnClickListener(v -> {
            if (sharedPreferences.getInt("favBeach1", -1) == -1) {
                return;
            }

            Intent intent = new Intent(context, CoastDetailsActivity2.class);
            intent.putExtra("coastId", sharedPreferences.getInt("favBeach1", -1));
            startActivity(intent);
        });

        mFavoriteCoastButton2.setOnClickListener(v -> {
            if (sharedPreferences.getInt("favBeach2", -1) == -1) {
                return;
            }

            Intent intent = new Intent(context, CoastDetailsActivity2.class);
            intent.putExtra("coastId", sharedPreferences.getInt("favBeach2", -1));
            startActivity(intent);
        });

        mFavoriteCoastButton3.setOnClickListener(v -> {
            if (sharedPreferences.getInt("favBeach3", -1) == -1) {
                return;
            }

            Intent intent = new Intent(context, CoastDetailsActivity2.class);
            intent.putExtra("coastId", sharedPreferences.getInt("favBeach3", -1));
            startActivity(intent);
        });
    }

    private void getAllBeaches() {

        //allBeaches = new ArrayList<>();

        //TODO get normal sqlite for Coast Model

        AndroidNetworking.initialize(context);
        AndroidNetworking.get("http://5.100.253.193:8080/hofim/information/v2/beach")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonAllCoasts = new JSONArray(response);

                            for (int i = 0; i < jsonAllCoasts.length(); i++) {
                                JSONObject jsonCoast = jsonAllCoasts.getJSONObject(i);
                                Gson gson = new Gson();
                                Beach mbeach = gson.fromJson(jsonCoast.toString(), Beach.class);
                                Log.i("TAGG", "" + mbeach.getBeachName());
                                Log.i("TAGG", "i " + i + "----" + mbeach.getBeachId());
                                //allBeaches.add(mbeach);
                                //Log.i("TAG", "allBeaches: " + allBeaches.size());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Log.i("TAG", "onResponse: " + e);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


    private void requestGpsPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            }
        }
    }

    private void initBMB() {
        //images for bmb
        bmbImage = new int[]{R.mipmap.ic_launcher_no_jell, R.mipmap.ic_launcher_lit_jell, R.mipmap.ic_launcher_many_jell};
        //text for bmb
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

                            if (mIsTelephonyPermissionAvailable) {
                                telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                deviceId = telephonyManager.getDeviceId();

                            } else {

                                deviceId = String.valueOf(new Random().nextInt(5));
                            }

                            // building Jellyfish report
                            long timeStamp = Calendar.getInstance(TimeZone.getTimeZone("Israel")).getTimeInMillis();
                            mjellyfish.setTimeStamp(String.valueOf(timeStamp));
                            mjellyfish.setTimeZone("Israel");
                            mjellyfish.setCountry("Israel");
                            mjellyfish.setUser("TESTING");
                            mjellyfish.setPassword("TESTING123");
                            mjellyfish.setDeviceId(deviceId);
                            mjellyfish.setLatitude(mLastLocation.getLatitude());
                            mjellyfish.setLongitude(mLastLocation.getLongitude());

                            switch (index) {

                                case 0:
                                    mjellyfish.setType("NONE");
                                    break;
                                case 1:
                                    mjellyfish.setType("FEW");
                                    break;
                                case 2:
                                    mjellyfish.setType("LOTS");
                                    break;
                            }
                            reportJellys(mjellyfish);
                        }
                    });
            bmbJelly.addBuilder(builder);
        }
    }

    private void reportJellys(Jellyfish mjellyfish) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://5.100.253.193:8080/hofim/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JellyFishReportService jellyFishReportService = retrofit.create(JellyFishReportService.class);

        Call<Jellyfish> call = jellyFishReportService.reportJellyFish(mjellyfish);

        Log.i("jell", "reportJellys: " + mjellyfish.toString());

        call.enqueue(new Callback<Jellyfish>() {
            @Override
            public void onResponse(Call<Jellyfish> call, Response<Jellyfish> response) {
                Toast.makeText(CoastMapActivity.this, "תודה על הדיווח", Toast.LENGTH_SHORT).show();
                Log.i("jell", "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<Jellyfish> call, Throwable t) {
                Toast.makeText(CoastMapActivity.this, "מצטערים, כרגע אין באפשרותך לדווח", Toast.LENGTH_SHORT).show();
                Log.i("jell", "onFailure: " + t);
            }
        });
    }

    private void InitDrawer() {
        //new DrawerBuilder().withActivity(this).build();

        //account header
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.mipmap.ic_launcher)
                //.addProfiles(
                //        new ProfileDrawerItem().withName("חופים").withEmail("www.wigitech.com").withTextColor(headerTextColor)
                //)
                //.withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                //    @Override
                //    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                //        return false;
                //    }
                //})
                .build();
        //items for drawer
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_1);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_2);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_3);

        // advance drawer
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        new DividerDrawerItem(),
                        item3,
                        new DividerDrawerItem()
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {

                    switch (position) {

                        case 1:
                            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                            String facebookUrl = getFacebookPageURL(context);
                            facebookIntent.setData(Uri.parse(facebookUrl));
                            startActivity(facebookIntent);
                            break;

                        case 3:
                            String url = "http://www.hofim.net";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                            break;

                        case 5:
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

                            break;

                    }
                    return false;
                })
                .build();
    }

    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }


    private void explainLocationNeeded() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsStatus && !isExit) {
            AlertDialog dialog = new AlertDialog.Builder(this).
                    setTitle("Location Needed").
                    setMessage("In order to give you\nthe full experience\nwe need GPS enabled\nplease enable gps").
                    setPositiveButton("OK", (dialog1, which) -> {
                        this.finish();
                        dialog1.dismiss();
                    }).show();
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this).
                    setTitle("Location Needed").
                    setMessage("please allow GPS permissions").
                    setPositiveButton("OK", (dialog1, which) -> {
                        this.finish();
                        dialog1.dismiss();
                    }).show();
        }


    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        getFavBeaches();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
        getFavBeaches();
        mTracker.setScreenName("מסך ראשי - מפה");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(LOCATION_KEY, mLastLocation);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        mLastLocation = savedInstanceState.getParcelable(LOCATION_KEY);
        //focusMyLocation();
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mGoogleApiClient.connect();
                    setIsLocationPermissionAvailable(true);
                    handleMyLocation();
                }
                break;
            case REQUEST_TELEPHONY_PERMISSION:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mIsTelephonyPermissionAvailable = true;
                }
        }
    }
    //endregion

    //region OnMapReadyCallback Implementation

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

        mMapCoastLocationService.init(mMap);

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setMapToolbarEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);


        // set the markers - old
        mMap.setInfoWindowAdapter(new CoastMarkerInfoWindowAdapter(this));
        mMap.setOnInfoWindowClickListener(marker -> {
            Integer coastId = mMapCoastLocationService.getCoastIdByMarker(marker);
            startCoastDetailsActivity(coastId);
        });

        mFocusMyLocationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                focusMyLocation();
            }
        });
    }
    //endregion

    //region Connection Listeners Implementation
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().
                addLocationRequest(mLocationRequest);
        final PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient, builder.build());
        final CoastMapActivity coastMapActivity = this;
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                final LocationSettingsStates locationSettingsStates =
                        locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        boolean isPermitted = setPermissions();
                        if (isPermitted) {
                            setIsLocationPermissionAvailable(true);
                            handleMyLocation();
                        }
                        break;
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(
                                    coastMapActivity, REQUEST_LOCATION_PERMISSION);
                        } catch (IntentSender.SendIntentException e) {
                            //ignore
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //location changes are unsatisfied but we can't fix settings...
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //endregion

    //region LocationListener Implementation
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        Log.d("location", "my location is: " + location.toString());
        if (mIsFocusingMyLocationEnabled) {
            focusMyLocation();
            mIsFocusingMyLocationEnabled = false; //one time focus
        }
    }
    //endregion

    //region Methods
    public void init(Bundle savedInstanceState) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY).
                setInterval(LOCATION_UPDATE_INTERVAL_IN_MILLI).
                setFastestInterval(LOCATION_UPDATE_FASTEST_INTERVAL_IN_MILLI);
        updateValuesFromBundle(savedInstanceState);

        //List<Integer> favoriteCoasts = mFavoriteCoastsService.getFavoriteCoasts();
        //mFavoriteButtonToCoastIdMap.put(mFavoriteCoastButton1, favoriteCoasts.get(0));
        //mFavoriteButtonToCoastIdMap.put(mFavoriteCoastButton2, favoriteCoasts.get(1));
        //mFavoriteButtonToCoastIdMap.put(mFavoriteCoastButton3, favoriteCoasts.get(2));
        //setFavoriteButtonState(mFavoriteCoastButton1);
        //setFavoriteButtonState(mFavoriteCoastButton2);
        //setFavoriteButtonState(mFavoriteCoastButton3);

//        mFavoriteCoastsService.setOnFavoriteCoastStateChangedListener(
//                (coastFavoriteNum, isSelected, coastId) -> {
//                    ImageView favoriteButton = null;
//                    switch (coastFavoriteNum) {
//                        case 0:
//                            favoriteButton = mFavoriteCoastButton1;
//                            break;
//                        case 1:
//                            favoriteButton = mFavoriteCoastButton2;
//                            break;
//                        case 2:
//                            favoriteButton = mFavoriteCoastButton3;
//                            break;
//                    }
//                    if (favoriteButton == null)
//                        return;
//                    mFavoriteButtonToCoastIdMap.put(favoriteButton, isSelected ? coastId : -1);
//                    setFavoriteButtonState(favoriteButton);
//                });
    }

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //create google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();
    }


    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            return;
        if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
            mLastLocation = savedInstanceState.getParcelable(LOCATION_KEY);
        }
        focusMyLocation();
    }

    private void handleMyLocation() {
        //startLocationUpdates();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startLocationUpdates();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        focusMyLocation();
    }

    private void startLocationUpdates() {
        //mMap.setMyLocationEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(false);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private void showRationaleDialog(String title, String content, String okText, final String[] permissions) {
        final CoastMapActivity coastMapActivity = this;
        AlertDialog dialog = new AlertDialog.Builder(this).
                setTitle(title).
                setMessage(content).
                setNeutralButton(okText, (dialog1, which) -> ActivityCompat.requestPermissions(coastMapActivity,
                        permissions,
                        REQUEST_LOCATION_PERMISSION)).
                create();
        dialog.show();
    }

    /**
     * Checks if the app has permissions to access locations.
     * if not this method prompts the user to give permissions and based on the result
     * invokes the method <i>onRequestPermissionsResult</i>
     *
     * @return true if permitted, false otherwise.
     */
    private boolean setPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showRationaleDialog("gief", "because!", "Ok",
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                showRationaleDialog("gief", "because!", "Ok",
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION});
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            }
            return false;
        }
        return true;
    }

    private void focusMyLocation() {
        if (mLastLocation == null) {
            return;
        }
        LatLng coordinates = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        Log.d("locs", coordinates.toString());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, ZOOM_AMOUNT));
    }

    private void startCoastDetailsActivity(Integer coastId) {
        if (coastId == null) {
            Toast.makeText(
                    CoastMapActivity.this,
                    "An error has occurred while trying to get coast details",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        //design 1
//        Intent intent = new Intent(CoastMapActivity.this, CoastDetailsActivity_.class);
//        intent.putExtra(IntentExtrasService.COAST_ID, coastId);
//        startActivity(intent);

        //design 2
        Intent intent = new Intent(context, CoastDetailsActivity2.class);
        intent.putExtra("coastId", coastId);

        startActivity(intent);
    }

    private void setFavoriteButtonState(ImageView favoriteButton) {
        final int coastId = mFavoriteButtonToCoastIdMap.get(favoriteButton);
        if (coastId == -1) {
            favoriteButton.setImageResource(R.drawable.favorite_unselected);
            favoriteButton.setOnClickListener(null);
            return;
        }
        favoriteButton.setImageResource(R.drawable.favorite_selected);
        favoriteButton.setOnClickListener(v -> {
            Marker marker = mMapCoastLocationService.getMarkerByCoastId(coastId);
            marker.showInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        });
    }

    //endregion

    //region Properties
    @org.jetbrains.annotations.Contract(pure = true)
    private boolean getIsLocationPermissionAvailable() {
        synchronized (mPermissionsLock) {
            return mIsLocationPermissionAvailable;
        }
    }

    private void setIsLocationPermissionAvailable(boolean isLocationPermissionAvailable) {
        synchronized (mPermissionsLock) {
            mIsLocationPermissionAvailable = isLocationPermissionAvailable;
        }
    }


}
