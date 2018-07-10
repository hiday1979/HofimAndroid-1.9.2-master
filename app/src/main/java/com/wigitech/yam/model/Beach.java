package com.wigitech.yam.model;

//is used for map markers
public class Beach {

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

    //constructor
    public Beach() {
    }

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

    public void setBeachId(int beachId) {
        this.beachId = beachId;
    }

    public void setBeachName(String beachName) {
        this.beachName = beachName;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setHandicappedFriendly(boolean handicappedFriendly) {
        this.handicappedFriendly = handicappedFriendly;
    }

    public void setBlueFlag(boolean blueFlag) {
        this.blueFlag = blueFlag;
    }

    //endregion
}
