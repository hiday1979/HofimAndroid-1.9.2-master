package com.wigitech.yam.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wigitech.yam.model.Beach;

/**
 * Created by Golanir on 18/07/2017.
 */

public class BeachesDataSource {

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            BeachDBOpenHelper.COLUMN_BEACHID,
            BeachDBOpenHelper.COLUMN_BEACHNAME,
            BeachDBOpenHelper.COLUMN_BEACHPARKING,
            BeachDBOpenHelper.COLUMN_BEACHDESCRIPTION,
            BeachDBOpenHelper.COLUMN_BEACHLATITUDE,
            BeachDBOpenHelper.COLUMN_BEACHLONGTITUDE,
            BeachDBOpenHelper.COLUMN_BEACHHANDICAP,
            BeachDBOpenHelper.COLUMN_BEACHBLUEFLAG};

    public BeachesDataSource(Context context) {
        dbhelper = new BeachDBOpenHelper(context);
    }

    public void open() {
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        dbhelper.close();
    }

    public void create(Beach beach) {
        ContentValues values = new ContentValues();
        values.put(BeachDBOpenHelper.COLUMN_BEACHID, beach.getBeachId());
        values.put(BeachDBOpenHelper.COLUMN_BEACHNAME, beach.getBeachName());
        values.put(BeachDBOpenHelper.COLUMN_BEACHPARKING, beach.getParking());
        values.put(BeachDBOpenHelper.COLUMN_BEACHDESCRIPTION, beach.getDescription());
        values.put(BeachDBOpenHelper.COLUMN_BEACHLATITUDE, beach.getLatitude());
        values.put(BeachDBOpenHelper.COLUMN_BEACHLONGTITUDE, beach.getLongitude());
        values.put(BeachDBOpenHelper.COLUMN_BEACHHANDICAP, beach.isHandicappedFriendly());
        values.put(BeachDBOpenHelper.COLUMN_BEACHBLUEFLAG, beach.isBlueFlag());
        database.insert(BeachDBOpenHelper.TABLE_BEACHES, null, values);
    }

}
