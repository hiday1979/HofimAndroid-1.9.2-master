package com.wigitech.yam.model;

public class DetailedCoast {

    //region Fields
    private int beachId;
    private String beachName;
    private String waveHeightType;
    private String waveHeightValue;
    private String windSpeedType;
    private String windSpeedValue;
    private String waterTemperatureType;
    private String waterTemperatureValue;
    private String windDirectionType;
    private String jellyFishType;
    private String cleanType;
    private boolean blueFlag;
    private boolean handicappedFriendly;
    //private String timeZone;
    //endregion

    //constructors

    //default
    public DetailedCoast() {}

    //full
    public DetailedCoast(int beachId, String beachName, String waveHeightType, String waveHeightValue, String windSpeedType, String windSpeedValue, String waterTemperatureType, String waterTemperatureValue, String windDirectionType, String jellyFishType, String cleanType, boolean blueFlag, boolean handicappedFriendly) {
        this.beachId = beachId;
        this.beachName = beachName;
        this.waveHeightType = waveHeightType;
        this.waveHeightValue = waveHeightValue;
        this.windSpeedType = windSpeedType;
        this.windSpeedValue = windSpeedValue;
        this.waterTemperatureType = waterTemperatureType;
        this.waterTemperatureValue = waterTemperatureValue;
        this.windDirectionType = windDirectionType;
        this.jellyFishType = jellyFishType;
        this.cleanType = cleanType;
        this.blueFlag = blueFlag;
        this.handicappedFriendly = handicappedFriendly;
        //this.timeZone = timeZone;
    }
    //end constructors

    //region Properties
    public int getBeachId() {
        return beachId;
    }

    public String getBeachName() {
        return beachName;
    }

    public String getJellyFishType() {
        return jellyFishType;
    }

    public String getWaterTemperatureType() {
        return waterTemperatureType;
    }

    public String getWaterTemperatureValue() {
        return waterTemperatureValue;
    }

    public String getWaveHeightType() {
        return waveHeightType;
    }

    public String getWaveHeightValue() {
        return waveHeightValue;
    }

    public String getWindDirectionType() {
        return windDirectionType;
    }

    public String getWindSpeedType() {
        return windSpeedType;
    }

    public String getWindSpeedValue() {
        return windSpeedValue;
    }

    public String getCleanType() {
        return cleanType;
    }

    //public String getTimeZone() {
    //    return timeZone;
    //}

    public boolean isBlueFlag() {
        return blueFlag;
    }

    public boolean isHandicappedFriendly() {
        return handicappedFriendly;
    }

    public void setBeachId(int beachId) {
        this.beachId = beachId;
    }

    public void setBeachName(String beachName) {
        this.beachName = beachName;
    }

    public void setWaveHeightType(String waveHeightType) {
        this.waveHeightType = waveHeightType;
    }

    public void setWaveHeightValue(String waveHeightValue) {
        this.waveHeightValue = waveHeightValue;
    }

    public void setWindSpeedType(String windSpeedType) {
        this.windSpeedType = windSpeedType;
    }

    public void setWindSpeedValue(String windSpeedValue) {
        this.windSpeedValue = windSpeedValue;
    }

    public void setWaterTemperatureType(String waterTemperatureType) {
        this.waterTemperatureType = waterTemperatureType;
    }

    public void setWaterTemperatureValue(String waterTemperatureValue) {
        this.waterTemperatureValue = waterTemperatureValue;
    }

    public void setWindDirectionType(String windDirectionType) {
        this.windDirectionType = windDirectionType;
    }

    public void setJellyFishType(String jellyFishType) {
        this.jellyFishType = jellyFishType;
    }

    public void setCleanType(String cleanType) {
        this.cleanType = cleanType;
    }

    public void setBlueFlag(boolean blueFlag) {
        this.blueFlag = blueFlag;
    }

    public void setHandicappedFriendly(boolean handicappedFriendly) {
        this.handicappedFriendly = handicappedFriendly;
    }

    //endregion

    //toString

    @Override
    public String toString() {
        return "DetailedCoast{" +
                "beachId=" + beachId +
                ", beachName='" + beachName + '\'' +
                ", waveHeightType='" + waveHeightType + '\'' +
                ", waveHeightValue='" + waveHeightValue + '\'' +
                ", windSpeedType='" + windSpeedType + '\'' +
                ", windSpeedValue='" + windSpeedValue + '\'' +
                ", waterTemperatureType='" + waterTemperatureType + '\'' +
                ", waterTemperatureValue='" + waterTemperatureValue + '\'' +
                ", windDirectionType='" + windDirectionType + '\'' +
                ", jellyFishType='" + jellyFishType + '\'' +
                ", cleanType='" + cleanType + '\'' +
                ", blueFlag=" + blueFlag +
                ", handicappedFriendly=" + handicappedFriendly +
                '}';
    }

    //end tostring
}
