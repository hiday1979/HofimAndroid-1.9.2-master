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
 * Use the {@link TomorrowForcastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TomorrowForcastFragment extends Fragment {
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
    private Double lat;
    private Double lon;

    TextView todayName;
    TextView todayDate;
    TextView todayDegrees;
    TextView todayForecast;

    private Map<Integer, String> daysOfWeek = new HashMap<>();
    private Map<String, String> windBearing = new HashMap<>();

    private String testJson = "{\"beachId\":22,\"beachName\":\"חוף ראשון לציון\",\"waveHeightType\":\"FEW\",\"waveHeightValue\":\"50\",\"windSpeedType\":\"BREEZE\",\"windSpeedValue\":\"9\",\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue\":\"24\",\"windDirectionType\":\"E\",\"jellyFishType\":\"NONE\",\"cleanType\":\"NONE\",\"blueFlag\":\"false\",\"handicappedFriendly\":\"true\",\"timeZone\":\"Israel\",\"result\":{\"metaData\":{\"beachId\":22,\"beachName\":\"חוף ראשון לציון\",\"stringEncoding\":\"UTF-8\",\"blueFlag\":false,\"handicappedFriendly\":true,\"timeZone\":\"Israel\"},\"body\":{\"forcasts\":{\"size\":3,\"dailyForcast\":[{\"date\":1496869200000,\"size\":24,\"dailyStats\":{\"airMaxTemperature_C\":31,\"airMinTemperature_C\":24},\"hourlyForcast\":[{\"hourOfDay\":0,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":14,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NNE\",\"windDirectionValue_Deg\":340,\"waveDirection\":\"NNW\"}},{\"hourOfDay\":1,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":14,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NE\",\"windDirectionValue_Deg\":340,\"waveDirection\":\"NNW\"}},{\"hourOfDay\":2,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":13,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NE\",\"windDirectionValue_Deg\":340,\"waveDirection\":\"NNW\"}},{\"hourOfDay\":3,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":12,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"ENE\",\"windDirectionValue_Deg\":340,\"waveDirection\":\"NNW\"}},{\"hourOfDay\":4,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":12,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"ENE\",\"windDirectionValue_Deg\":333,\"waveDirection\":\"NNW\"}},{\"hourOfDay\":5,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":12,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"ENE\",\"windDirectionValue_Deg\":327,\"waveDirection\":\"NNW\"}},{\"hourOfDay\":6,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":11,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"E\",\"windDirectionValue_Deg\":320,\"waveDirection\":\"NW\"}},{\"hourOfDay\":7,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":12,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"SSE\",\"windDirectionValue_Deg\":213,\"waveDirection\":\"SSW\"}},{\"hourOfDay\":8,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":12,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"WSW\",\"windDirectionValue_Deg\":107,\"waveDirection\":\"ESE\"}},{\"hourOfDay\":9,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":40,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":12,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NNW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":10,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":40,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":15,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NNW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":11,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":50,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":17,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NNW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":12,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":50,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":20,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NNW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":13,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":40,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":23,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NNW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":14,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":26,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NNW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":15,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":29,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NNW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":16,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":28,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"WSW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":17,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":27,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"SE\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":18,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":40,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":26,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NNE\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":19,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":40,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":23,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NNE\",\"windDirectionValue_Deg\":113,\"waveDirection\":\"ESE\"}},{\"hourOfDay\":20,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":40,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":21,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NNE\",\"windDirectionValue_Deg\":227,\"waveDirection\":\"SW\"}},{\"hourOfDay\":21,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":40,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":18,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NNE\",\"windDirectionValue_Deg\":340,\"waveDirection\":\"NNW\"}},{\"hourOfDay\":22,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":16,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NE\",\"windDirectionValue_Deg\":333,\"waveDirection\":\"NNW\"}},{\"hourOfDay\":23,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":15,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"ENE\",\"windDirectionValue_Deg\":327,\"waveDirection\":\"NNW\"}}]},{\"date\":1496782800000,\"size\":24,\"dailyStats\":{\"airMaxTemperature_C\":29,\"airMinTemperature_C\":24},\"hourlyForcast\":[{\"hourOfDay\":0,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":14,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NE\",\"windDirectionValue_Deg\":320,\"waveDirection\":\"NW\"}},{\"hourOfDay\":1,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":14,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NE\",\"windDirectionValue_Deg\":320,\"waveDirection\":\"NW\"}},{\"hourOfDay\":2,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":10,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":14,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NE\",\"windDirectionValue_Deg\":320,\"waveDirection\":\"NW\"}},{\"hourOfDay\":3,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":10,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":15,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NE\",\"windDirectionValue_Deg\":320,\"waveDirection\":\"NW\"}},{\"hourOfDay\":4,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":10,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":15,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NE\",\"windDirectionValue_Deg\":310,\"waveDirection\":\"NW\"}},{\"hourOfDay\":5,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":10,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":14,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NE\",\"windDirectionValue_Deg\":300,\"waveDirection\":\"WNW\"}},{\"hourOfDay\":6,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":10,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":14,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NE\",\"windDirectionValue_Deg\":290,\"waveDirection\":\"WNW\"}},{\"hourOfDay\":7,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":10,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":14,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"SSE\",\"windDirectionValue_Deg\":290,\"waveDirection\":\"WNW\"}},{\"hourOfDay\":8,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":10,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":15,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"WSW\",\"windDirectionValue_Deg\":290,\"waveDirection\":\"WNW\"}},{\"hourOfDay\":9,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":10,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":15,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NNW\",\"windDirectionValue_Deg\":290,\"waveDirection\":\"WNW\"}},{\"hourOfDay\":10,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":10,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":18,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NNW\",\"windDirectionValue_Deg\":193,\"waveDirection\":\"SSW\"}},{\"hourOfDay\":11,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":10,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":20,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NNW\",\"windDirectionValue_Deg\":97,\"waveDirection\":\"E\"}},{\"hourOfDay\":12,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":10,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":23,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NNW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":13,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":25,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NNW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":14,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":40,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":27,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NNW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":15,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":60,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":29,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NNW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":16,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":50,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":28,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"SW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":17,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":50,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":26,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"ESE\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":18,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":40,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":25,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"N\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":19,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":23,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"N\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":20,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":10,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":21,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NNE\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":21,\"forcast\":{\"waveHeightType\":\"CALM\",\"waveHeightValue_Cm\":0,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":19,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NNE\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":22,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":10,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":17,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NNE\",\"windDirectionValue_Deg\":113,\"waveDirection\":\"ESE\"}},{\"hourOfDay\":23,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":16,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":24,\"windDirectionType\":\"NNE\",\"windDirectionValue_Deg\":227,\"waveDirection\":\"SW\"}}]},{\"date\":1496955600000,\"size\":24,\"dailyStats\":{\"airMaxTemperature_C\":32,\"airMinTemperature_C\":23},\"hourlyForcast\":[{\"hourOfDay\":0,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":13,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"ENE\",\"windDirectionValue_Deg\":320,\"waveDirection\":\"NW\"}},{\"hourOfDay\":1,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":12,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"ENE\",\"windDirectionValue_Deg\":320,\"waveDirection\":\"NW\"}},{\"hourOfDay\":2,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"CALM\",\"windSpeedValue_Kmph\":10,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"E\",\"windDirectionValue_Deg\":320,\"waveDirection\":\"NW\"}},{\"hourOfDay\":3,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"CALM\",\"windSpeedValue_Kmph\":8,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"E\",\"windDirectionValue_Deg\":320,\"waveDirection\":\"NW\"}},{\"hourOfDay\":4,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"CALM\",\"windSpeedValue_Kmph\":9,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"SSE\",\"windDirectionValue_Deg\":217,\"waveDirection\":\"SW\"}},{\"hourOfDay\":5,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"CALM\",\"windSpeedValue_Kmph\":10,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"WSW\",\"windDirectionValue_Deg\":113,\"waveDirection\":\"ESE\"}},{\"hourOfDay\":6,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"CALM\",\"windSpeedValue_Kmph\":10,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":10,\"waveDirection\":\"N\"}},{\"hourOfDay\":7,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":13,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":7,\"waveDirection\":\"N\"}},{\"hourOfDay\":8,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":16,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":3,\"waveDirection\":\"N\"}},{\"hourOfDay\":9,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":20,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":19,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":10,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":30,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":21,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":11,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":50,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":23,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":12,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":60,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":25,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":13,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":50,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":26,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":14,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":50,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":26,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":15,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":40,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":27,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":16,\"forcast\":{\"waveHeightType\":\"FEW\",\"waveHeightValue_Cm\":40,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":26,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":17,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":50,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":25,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":18,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":50,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":24,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":0,\"waveDirection\":\"N\"}},{\"hourOfDay\":19,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":50,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":23,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":100,\"waveDirection\":\"E\"}},{\"hourOfDay\":20,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":60,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":21,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":200,\"waveDirection\":\"SSW\"}},{\"hourOfDay\":21,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":60,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":19,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":300,\"waveDirection\":\"WNW\"}},{\"hourOfDay\":22,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":60,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":18,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"NW\",\"windDirectionValue_Deg\":300,\"waveDirection\":\"WNW\"}},{\"hourOfDay\":23,\"forcast\":{\"waveHeightType\":\"SOME\",\"waveHeightValue_Cm\":70,\"windSpeedType\":\"BREEZE\",\"windSpeedValue_Kmph\":16,\"waterTemperatureType\":\"FINE\",\"waterTemperatureValue_C\":25,\"windDirectionType\":\"WNW\",\"windDirectionValue_Deg\":300,\"waveDirection\":\"WNW\"}}]}]},\"reports\":{\"jellyFishType\":\"NONE\",\"cleanType\":\"NONE\"}}}}";

    public TomorrowForcastFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TomorrowForcastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TomorrowForcastFragment newInstance(int param1, Double lat ,Double lon, String param2) {
        TomorrowForcastFragment fragment = new TomorrowForcastFragment();
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

        //set the "new" date
        localDate = LocalDate.now().plusDays(2);

        getBeachDetails();
        //parseCoastDetaildJson(testJson);

//        todayName.setText(daysOfWeek.get(getNewDay(rightNow.get(Calendar.DAY_OF_WEEK))));
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
        i = i+2;
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

            JSONObject object = new JSONObject(jsonCoastDetailsString);

            JSONArray weather_general = object.getJSONArray("weather_general");

            JSONObject metaData = weather_general.getJSONObject(0);

            JSONArray forcasts = metaData.getJSONArray("weather");

            JSONObject nextDay = forcasts.getJSONObject(2);

            JSONArray nextDayData = nextDay.getJSONArray("hourly");

            JSONObject nextDayData1 = nextDayData.getJSONObject(0);



//            JSONObject dailyForcast0 = dailyForcast.getJSONObject(2);
//            JSONObject dailyStats2 = dailyForcast0.getJSONObject("dailyStats");
//            JSONArray hourlyForcast0 = dailyForcast0.getJSONArray("hourlyForcast");

            int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);

            Double d = nextDayData1.getDouble("swellHeight_m") * 100;

//            JSONObject hourlyForcast0_0 = hourlyForcast0.getJSONObject(currentHour);
//            JSONObject forcast0 = hourlyForcast0_0.getJSONObject("forcast");
//            JSONObject reports = body.getJSONObject("reports");
            //create today DetailedBeachHourlyForcast
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
                    R.drawable.high_wave);

            DetailedCoastRecyclerView dcrvWaveFrequancyPerSec = new DetailedCoastRecyclerView(
                    (getResources().getString(R.string.waveFrequancyPerSec)),
                    String.valueOf(dbhfToday.getWaveFrequancyPerSec()),
                    R.drawable.high_wave);

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
                    R.drawable.air_temp_max);

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
