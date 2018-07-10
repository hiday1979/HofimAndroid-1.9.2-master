package com.wigitech.yam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.wigitech.yam.R;
/**
 * Created by user on 12/06/2016.
 */
public class CoastMarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context mContext;

    public CoastMarkerInfoWindowAdapter(Context context) {
        mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        //overrides the whole window frame
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        //keeps the window frame, but overrides the window content
        //LayoutInflater layoutInflater =
        //        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = layoutInflater.inflate(R.layout.map_marker_info_contents, null);
        View view = LayoutInflater.from(mContext).inflate(R.layout.map_marker_info_contents, null, false);
        //ImageView icon = (ImageView) view.findViewById(R.id.marker_info_window_icon);
        //icon.setImageResource(R.drawable.info);
        TextView title = (TextView) view.findViewById(R.id.marker_info_window_title);
        title.setText(marker.getTitle());
        return view;
    }
}
