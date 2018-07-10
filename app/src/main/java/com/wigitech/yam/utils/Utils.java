package com.wigitech.yam.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;

import com.wigitech.yam.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Golanir on 18/06/2017.
 */

public class Utils {

    Context context;

    public Utils(Context context) {
        this.context = context;
    }

    //snackbar util
    public static void mySnackBar(View view, String message) {
        Snackbar.make(view, message, 2500).show();
    }

    //notification util
    public void blueFlagNotification() {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("חופים")
                .setContentText("הגעת לחוף דגל כחול, תשאיר אותו נקי כמו שמצאת אותו, למה חבל עליך, יש לך פנים יפות, ואנחנו יודעים איפה אתה גר!!!");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, mBuilder.build());
    }

}
