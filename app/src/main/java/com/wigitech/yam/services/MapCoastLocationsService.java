package com.wigitech.yam.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.annimon.stream.Stream;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wigitech.yam.R;
import com.wigitech.yam.dataacess.CoastDataService;
import com.wigitech.yam.model.Coast;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 09/07/2016.
 */
@EBean
public class MapCoastLocationsService {

    //region Fields
    private GoogleMap mMap;
    private Map<Marker, Integer> mMarkerCoastMapper = new HashMap<>();
    private BitmapDescriptor mCoastPinBitmapDescriptor;

    @RootContext
    Context mContext;

    @RestService
    CoastDataService mCoastDataService;
    //endregion

    //region Methods
    /**
     * This must be called after calling the constructor.
     * The use of init is to compensate for AndroidAnnotations restriction
     * of empty or context inject constructors.
     * @param map the map to display the data.
     */

    public void init(GoogleMap map) {
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.pin);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        mCoastPinBitmapDescriptor = BitmapDescriptorFactory.
                fromBitmap(bitmap);
        mMap = map;
        updateCoastMarkers();
    }

    public Integer getCoastIdByMarker(Marker marker) {
        return mMarkerCoastMapper.get(marker);
    }

    /**
     * Gets the marker that matches the coast id.
     * note: this method may have a long running time, and should be used with care.
     * @param coastId the coastId to search
     * @return the associated Marker.
     */
    public Marker getMarkerByCoastId(int coastId) {
        return Stream.of(mMarkerCoastMapper.keySet()).
                filter(value -> mMarkerCoastMapper.get(value) == coastId).
                findFirst().
                get();
    }

    @Background
    void updateCoastMarkers() {
        Coast[] allCoasts = mCoastDataService.getAllCoasts();
        updateCoastMarkersOnMap(allCoasts);
    }

    @UiThread
    void updateCoastMarkersOnMap(Coast[] coasts) {
        for (Coast coast : coasts) {
            Marker marker = mMap.addMarker(new MarkerOptions().
                    position(new LatLng(coast.getLatitude(), coast.getLongitude())).
                    title(coast.getBeachName()).icon(mCoastPinBitmapDescriptor));
            mMarkerCoastMapper.put(marker, coast.getBeachId());
        }
    }
    //endregion
}
