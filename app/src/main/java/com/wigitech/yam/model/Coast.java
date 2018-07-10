package com.wigitech.yam.model;

/**
 * Created by user on 09/07/2016.
 */
public class Coast {

    //region Fields
    private int beachId;
    private String beachName;
    private String parking;
    private String description;
    private double latitude;
    private double longitude;
    private boolean handicappedFriendly;
    private boolean blueFlag;
    //endregion


    //region Properties
    public int getBeachId() {
        return beachId;
    }

    public String getBeachName() {
        return beachName;
    }

    public String getParking() {
        return parking;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isHandicappedFriendly() {
        return handicappedFriendly;
    }

    public boolean isBlueFlag() {
        return blueFlag;
    }

    //endregion
}
