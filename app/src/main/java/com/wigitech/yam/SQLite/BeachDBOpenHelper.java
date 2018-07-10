package com.wigitech.yam.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BeachDBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "beachs.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_BEACHES = "beachs";
    public static final String COLUMN_BEACHID = "beachId";
    public static final String COLUMN_BEACHNAME = "beachName";
    public static final String COLUMN_BEACHPARKING = "BeachParking";
    public static final String COLUMN_BEACHDESCRIPTION = "BeachDescription";
    public static final String COLUMN_BEACHLATITUDE = "BeachLatitude";
    public static final String COLUMN_BEACHLONGTITUDE = "BeachLongitude";
    public static final String COLUMN_BEACHHANDICAP = "BeachHandicap";
    public static final String COLUMN_BEACHBLUEFLAG = "BeachBlueFlag";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_BEACHES + " (" +
                    COLUMN_BEACHID + " INTEGER, " +
                    COLUMN_BEACHNAME + " TEXT, " +
                    COLUMN_BEACHPARKING + " TEXT, " +
                    COLUMN_BEACHDESCRIPTION + " TEXT, " +
                    COLUMN_BEACHLATITUDE + " NUMERIC " +
                    COLUMN_BEACHLONGTITUDE + " NUMERIC " +
                    COLUMN_BEACHHANDICAP + " INTEGER " +
                    COLUMN_BEACHBLUEFLAG + " INTEGER " +
                    ")";

    public BeachDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEACHES);
        onCreate(db);
    }
}
