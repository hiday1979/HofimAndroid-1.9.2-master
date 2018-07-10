package com.wigitech.yam.Analytics;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.wigitech.yam.R;

/**
 * Created by Golanir on 23/06/2017.
 */

public class HofimAnalytics extends Application {

    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    public void onCreate() {
        sAnalytics = GoogleAnalytics.getInstance(this);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }
}
