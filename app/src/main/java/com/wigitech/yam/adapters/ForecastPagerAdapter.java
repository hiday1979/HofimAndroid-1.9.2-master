package com.wigitech.yam.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wigitech.yam.R;
import com.wigitech.yam.fragments.TodayForecastFragment;
import com.wigitech.yam.fragments.TomorrowForcastFragment;
import com.wigitech.yam.fragments.TwoDaysForcastFragment;

/**
 * Created by GolaNir on 11/06/2017.
 */

public class ForecastPagerAdapter extends FragmentPagerAdapter {

    Context context;
    int coastId;
    private Double lat;
    private Double lon;

    public ForecastPagerAdapter(FragmentManager fm, Context context, int coastId, Double lat, Double lon) {
        super(fm);
        this.context = context;
        this.coastId = coastId;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TodayForecastFragment.newInstance(coastId, lat, lon,  "2");
            case 1:
                return TomorrowForcastFragment.newInstance(coastId, lat, lon,  "2");
            case 2:
                return TwoDaysForcastFragment.newInstance(coastId, lat, lon,  "2");
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.todatForcast);
            case 1:
                return context.getResources().getString(R.string.tomorrowForcast);
            case 2:
                return context.getResources().getString(R.string.twoDayForcast);
        }
        return null;
    }
}
