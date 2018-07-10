package com.wigitech.yam.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.wigitech.yam.R;
import com.wigitech.yam.adapters.ForecastAdapter;
import com.wigitech.yam.model.DetailedBeachHourlyForecast;
import com.wigitech.yam.model.DetailedCoastRecyclerView;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayForecastFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static final String TAG = "TAG";
    private int mParam1;
    private String mParam2;
    private int hourOfDay = 0;

    private RecyclerView recyclerView;
    private ForecastAdapter forecastAdapter;
    private List<DetailedCoastRecyclerView> dcrvList;
    private RecyclerView.LayoutManager layoutManager;
    private DetailedBeachHourlyForecast dbhfToday;
    private List<DetailedCoastRecyclerView> dbhf0;
    private List<DetailedBeachHourlyForecast> dbhfList;
    private Calendar rightNow = Calendar.getInstance();
    private LocalDate localDate;
    TextView todayName;
    TextView todayDate;
    TextView todayDegrees;
    TextView todayForecast;

    private Map<Integer, String> daysOfWeek = new HashMap<>();
    private Map<String, String> windBearing = new HashMap<>();
    private Double lat;
    private Double lon;

    public TodayForecastFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayForecastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayForecastFragment newInstance(int param1,Double lat,Double lon, String param2) {
        TodayForecastFragment fragment = new TodayForecastFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putDouble("lat", lat);
        args.putDouble("lon", lon);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            lat = getArguments().getDouble("lat");
            lon = getArguments().getDouble("lon");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_forecast, container, false);

        todayName = (TextView) view.findViewById(R.id.tvDay);
        todayDate = (TextView) view.findViewById(R.id.tvDate);
        todayDegrees = (TextView) view.findViewById(R.id.tvDegrees);

        recyclerView = (RecyclerView) view.findViewById(R.id.ForecastFragmentRecyclerView);

        dbhf0 = new ArrayList<>();
        dbhfList = new ArrayList<>();
        daysOfWeek.put(1, getString(R.string.sunday));
        daysOfWeek.put(2, getString(R.string.monday));
        daysOfWeek.put(3, getString(R.string.tuesday));
        daysOfWeek.put(4, getString(R.string.wednesday));
        daysOfWeek.put(5, getString(R.string.thursday));
        daysOfWeek.put(6, getString(R.string.friday));
        daysOfWeek.put(7, getString(R.string.saturday));

        //set map for winds
        windBearing.put("N", "צפונית");
        windBearing.put("NNE", "צפון צפון מזרח");
        windBearing.put("NE", "צפון מזרח");
        windBearing.put("ENE", "מזרחית צפון מזרחית");
        windBearing.put("E", "מזרחית");
        windBearing.put("SW", "דרום מערבית");
        windBearing.put("SSW", "דרום דרום מערבית");
        windBearing.put("S", "דרומית");
        windBearing.put("SSE", "דרום דרום מזרחית");
        windBearing.put("SE", "דרום מזרחית");
        windBearing.put("ESE", "מזרחית דרום מזרחית");
        windBearing.put("W", "מערבית");
        windBearing.put("WSW", "מערבית דרום מערבית");
        windBearing.put("WNW", "מערבית צפון מערבית");
        windBearing.put("NW", "צפון מערבית");
        windBearing.put("NNW", "צפון צפון מערבית");

        getBeachDetails();
        //parseCoastDetaildJson(testJson);

        //set date
        localDate = LocalDate.now().plusDays(1);

//        todayName.setText(daysOfWeek.get(rightNow.get(Calendar.DAY_OF_WEEK)));
//        todayDate.setText(localDate.getDayOfMonth() + "." + localDate.getMonthOfYear() + "." + localDate.getYear());
//        todayDegrees.setText("" + dbhfToday.getAirmaxtemperatureC() + "\u00B0");
//        todayForecast.setText("");

        forecastAdapter = new ForecastAdapter(this.getContext(), dbhf0);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(forecastAdapter);

        return view;

    }

    private int getNewDay(int i) {
        i = i+1;
        if (i>7) {
            i = i-7;
        }
        return i;
    }

    private void getBeachDetails() {

        AndroidNetworking.get("http://hofim-hofim1.7e14.starter-us-west-2.openshiftapps.com/v1/api/get_full_beach?lat=" + lat + "&lon=" + lon)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i(TAG, "onResponse: " + response);
                        //jsonCoastDetailsString += response;
                        parseCoastDetaildJson(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        //TODO add snackber for error
                        //Log.i(TAG, "onError: " + anError);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void parseCoastDetaildJson(String jsonCoastDetailsString) {

        if (jsonCoastDetailsString.length() == 0) {
            //TODO snackbar for error
            return;
        }

        try {

//            JSONObject object = new JSONObject(jsonCoastDetailsString);
//
//            JSONObject result = object.getJSONObject("result");
//
//            JSONObject metaData = result.getJSONObject("metaData");
//
//            JSONObject body = result.getJSONObject("body");
//
//            JSONObject forcasts = body.getJSONObject("forcasts");
//
//            JSONArray dailyForcast = forcasts.getJSONArray("dailyForcast");
//
//            JSONObject dailyForcast1 = dailyForcast.getJSONObject(1);
//            JSONObject dailyStats1 = dailyForcast1.getJSONObject("dailyStats");
//            JSONArray hourlyForcast1 = dailyForcast1.getJSONArray("hourlyForcast");
//
//            int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
//
//            JSONObject hourlyForcast1_0 = hourlyForcast1.getJSONObject(currentHour);
//            JSONObject forcast0 = hourlyForcast1_0.getJSONObject("forcast");
//            JSONObject reports = body.getJSONObject("reports");
            //create today DetailedBeachHourlyForcast
            JSONObject object = new JSONObject(jsonCoastDetailsString);

            JSONArray weather_general = object.getJSONArray("weather_general");

            JSONObject metaData = weather_general.getJSONObject(0);

            JSONArray forcasts = metaData.getJSONArray("weather");

            JSONObject nextDay = forcasts.getJSONObject(1);

            JSONArray nextDayData = nextDay.getJSONArray("hourly");

            JSONObject nextDayData1 = nextDayData.getJSONObject(0);

            Double d = nextDayData1.getDouble("swellHeight_m") * 100;


            dbhfToday = new DetailedBeachHourlyForecast(
                    nextDayData1.getString("swellDir16Point"),
                    d.intValue(),
                    nextDayData1.getString("winddir16Point"),
                    nextDayData1.getInt("windspeedKmph"),
                    "C",
//                    nextDayData11.getString("waterTemperatureType"),
                    nextDayData1.getInt("waterTemp_C"),
                    nextDayData1.getString("winddir16Point"),
                    nextDayData1.getInt("winddirDegree"),
                    nextDayData1.getString("swellDir16Point"),
                    nextDayData1.getInt("swellPeriod_secs"),
                    nextDayData1.getInt("humidity"),
                    nextDayData1.getInt("FeelsLikeC"),
                    nextDayData1.getInt("tempC"),
                    nextDayData1.getInt("windspeedKmph"),
                    object.getString("_id"),
                    object.getString("name"),
                    object.getBoolean("blue_flag"),
                    object.getBoolean("disabilities_status"),
                    object.getString("city"),
//                    reports.getString("jellyFishType"),
//                    reports.getString("cleanType"),
                    "none",
                    "cleanType",
                    nextDay.getInt("maxtempC"),
                    nextDay.getInt("mintempC")
            );

            //build list for recycler view
            DetailedCoastRecyclerView dcrvWaveHeightType = new DetailedCoastRecyclerView(
                    getString(R.string.WaveFrequency),
                    dbhfToday.getWaveheighttype(),
                    R.drawable.wave_height);

            DetailedCoastRecyclerView dcrvWaveHeight = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.wave_height)),
                    (String.valueOf(dbhfToday.getWaveheightvalueCm()) +
                            " " + getResources().getString(R.string.centimeters)),
                    R.drawable.high_wave);

            DetailedCoastRecyclerView dcrvWindSpeedType = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.windSpeedType)),
                    dbhfToday.getWindspeedtype(),
                    R.drawable.wind_speed_old);

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
                    R.drawable.wind_temp_c);

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
                    R.drawable.max_air_temp);

            DetailedCoastRecyclerView dcrvAirMinTemperature_C = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.airMinTemperature_C)),
                    String.valueOf(dbhfToday.getAirmintemperatureC()),
                    R.drawable.air_temp_min);

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

            //dbhf0.add(dcrvAirMaxTemperature_C);
            //dbhf0.add(dcrvAirMinTemperature_C);
            dbhf0.add(dcrvWaveHeight);
            dbhf0.add(dcrvAirTemperature_C);
            dbhf0.add(dcrvWaterTemp);
            dbhf0.add(dcrvWaveDirection);
            dbhf0.add(dcrvwindDirectionType);
            //dbhf0.add(dcrvWindDirectionValueDeg);
            dbhf0.add(dcrvWindSpeed);

            todayName.setText(daysOfWeek.get(getNewDay(rightNow.get(Calendar.DAY_OF_WEEK))));
            todayDate.setText(localDate.getDayOfMonth() + "." + localDate.getMonthOfYear() + "." + localDate.getYear());
            todayDegrees.setText("" + (dbhfToday.getAirmaxtemperatureC() + dbhfToday.getAirmintemperatureC()) /2 + "\u00B0");

            forecastAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "parseCoastDetaildJson: " + e);

            todayName.setText(daysOfWeek.get(getNewDay(rightNow.get(Calendar.DAY_OF_WEEK))));
            todayDate.setText(localDate.getDayOfMonth() + "." + localDate.getMonthOfYear() + "." + localDate.getYear());
            todayDegrees.setTextSize((float) 32);
            todayDegrees.setText("אין נתונים");
        }

    }
}
