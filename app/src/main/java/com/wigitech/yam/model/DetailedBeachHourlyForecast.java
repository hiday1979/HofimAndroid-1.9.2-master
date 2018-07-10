package com.wigitech.yam.model;

public class DetailedBeachHourlyForecast {

    private String waveheighttype;
    private int waveheightvalueCm;
    private String windspeedtype;
    private int windspeedvalueKmph;
    private String watertemperaturetype;
    private int watertemperaturevalueC;
    private String winddirectiontype;
    private int winddirectionvalueDeg;
    private String wavedirection;
    private int waveFrequancyPerSec;
    private int humidityPercent;
    private int airTemperatureFeelsLike_C;
    private int airTemperature_C;
    private int windGustValue_Kmph;
    private String beachId;
    private String beachName;
    private boolean blueFlag;
    private boolean handicappedFriendly;
    private String timeZone;
    private String jellyFishType;
    private String cleanType;

    //whole day temperature - get from result body forcast dailyForcast HOUR dailystats
    private int airmaxtemperatureC;
    private int airmintemperatureC;

    //default constructor
    public DetailedBeachHourlyForecast() {}

    //constructor

    public DetailedBeachHourlyForecast(String waveheighttype, int waveheightvalueCm, String windspeedtype, int windspeedvalueKmph, String watertemperaturetype, int watertemperaturevalueC, String winddirectiontype, int winddirectionvalueDeg, String wavedirection, int waveFrequancyPerSec, int humidityPercent, int airTemperatureFeelsLike_C, int airTemperature_C, int windGustValue_Kmph, String beachId, String beachName, boolean blueFlag, boolean handicappedFriendly, String timeZone, String jellyFishType, String cleanType, int airmaxtemperatureC, int airmintemperatureC) {
        this.waveheighttype = waveheighttype;
        this.waveheightvalueCm = waveheightvalueCm;
        this.windspeedtype = windspeedtype;
        this.windspeedvalueKmph = windspeedvalueKmph;
        this.watertemperaturetype = watertemperaturetype;
        this.watertemperaturevalueC = watertemperaturevalueC;
        this.winddirectiontype = winddirectiontype;
        this.winddirectionvalueDeg = winddirectionvalueDeg;
        this.wavedirection = wavedirection;
        this.waveFrequancyPerSec = waveFrequancyPerSec;
        this.humidityPercent = humidityPercent;
        this.airTemperatureFeelsLike_C = airTemperatureFeelsLike_C;
        this.airTemperature_C = airTemperature_C;
        this.windGustValue_Kmph = windGustValue_Kmph;
        this.beachId = beachId;
        this.beachName = beachName;
        this.blueFlag = blueFlag;
        this.handicappedFriendly = handicappedFriendly;
        this.timeZone = timeZone;
        this.jellyFishType = jellyFishType;
        this.cleanType = cleanType;
        this.airmaxtemperatureC = airmaxtemperatureC;
        this.airmintemperatureC = airmintemperatureC;
    }


    //getters

    public String getWaveheighttype() {
        return waveheighttype;
    }

    public int getWaveheightvalueCm() {
        return waveheightvalueCm;
    }

    public String getWindspeedtype() {
        return windspeedtype;
    }

    public int getWindspeedvalueKmph() {
        return windspeedvalueKmph;
    }

    public String getWatertemperaturetype() {
        return watertemperaturetype;
    }

    public int getWatertemperaturevalueC() {
        return watertemperaturevalueC;
    }

    public String getWinddirectiontype() {
        return winddirectiontype;
    }

    public int getWinddirectionvalueDeg() {
        return winddirectionvalueDeg;
    }

    public String getWavedirection() {
        return wavedirection;
    }

    public int getAirmaxtemperatureC() {
        return airmaxtemperatureC;
    }

    public int getAirmintemperatureC() {
        return airmintemperatureC;
    }

    public int getWaveFrequancyPerSec() {
        return waveFrequancyPerSec;
    }

    public int getHumidityPercent() {
        return humidityPercent;
    }

    public int getAirTemperatureFeelsLike_C() {
        return airTemperatureFeelsLike_C;
    }

    public int getAirTemperature_C() {
        return airTemperature_C;
    }

    public int getWindGustValue_Kmph() {
        return windGustValue_Kmph;
    }

    public String getBeachId() {
        return beachId;
    }

    public String getBeachName() {
        return beachName;
    }

    public boolean isBlueFlag() {
        return blueFlag;
    }

    public boolean isHandicappedFriendly() {
        return handicappedFriendly;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getJellyFishType() {
        return jellyFishType;
    }

    public String getCleanType() {
        return cleanType;
    }

    public void setWaveheighttype(String waveheighttype) {
        this.waveheighttype = waveheighttype;
    }

    public void setWaveheightvalueCm(int waveheightvalueCm) {
        this.waveheightvalueCm = waveheightvalueCm;
    }

    public void setWindspeedtype(String windspeedtype) {
        this.windspeedtype = windspeedtype;
    }

    public void setWindspeedvalueKmph(int windspeedvalueKmph) {
        this.windspeedvalueKmph = windspeedvalueKmph;
    }

    public void setWatertemperaturetype(String watertemperaturetype) {
        this.watertemperaturetype = watertemperaturetype;
    }

    public void setWatertemperaturevalueC(int watertemperaturevalueC) {
        this.watertemperaturevalueC = watertemperaturevalueC;
    }

    public void setWinddirectiontype(String winddirectiontype) {
        this.winddirectiontype = winddirectiontype;
    }

    public void setWinddirectionvalueDeg(int winddirectionvalueDeg) {
        this.winddirectionvalueDeg = winddirectionvalueDeg;
    }

    public void setWavedirection(String wavedirection) {
        this.wavedirection = wavedirection;
    }

    public void setWaveFrequancyPerSec(int waveFrequancyPerSec) {
        this.waveFrequancyPerSec = waveFrequancyPerSec;
    }

    public void setHumidityPercent(int humidityPercent) {
        this.humidityPercent = humidityPercent;
    }

    public void setAirTemperatureFeelsLike_C(int airTemperatureFeelsLike_C) {
        this.airTemperatureFeelsLike_C = airTemperatureFeelsLike_C;
    }

    public void setAirTemperature_C(int airTemperature_C) {
        this.airTemperature_C = airTemperature_C;
    }

    public void setWindGustValue_Kmph(int windGustValue_Kmph) {
        this.windGustValue_Kmph = windGustValue_Kmph;
    }

    public void setBeachId(String beachId) {
        this.beachId = beachId;
    }

    public void setBeachName(String beachName) {
        this.beachName = beachName;
    }

    public void setBlueFlag(boolean blueFlag) {
        this.blueFlag = blueFlag;
    }

    public void setHandicappedFriendly(boolean handicappedFriendly) {
        this.handicappedFriendly = handicappedFriendly;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public void setJellyFishType(String jellyFishType) {
        this.jellyFishType = jellyFishType;
    }

    public void setCleanType(String cleanType) {
        this.cleanType = cleanType;
    }

    public void setAirmaxtemperatureC(int airmaxtemperatureC) {
        this.airmaxtemperatureC = airmaxtemperatureC;
    }

    public void setAirmintemperatureC(int airmintemperatureC) {
        this.airmintemperatureC = airmintemperatureC;
    }
}
