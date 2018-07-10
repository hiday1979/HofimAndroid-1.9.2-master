package com.wigitech.yam.model;

import java.util.List;

public class DetailedBeachMetadata {

    //beach data - get from result metadata
    private int beachid;
    private String beachname;
    private boolean blueflag;
    private boolean handicappedfriendly;
    private String timezone;

    //whole day temperature - get from result body forcast dailyForcast HOUR dailystats
    private int airmaxtemperatureC;
    private int airmintemperatureC;

    //jellyfish and clean reports - get from result body reports
    private String jellyfishtype;
    private String cleantype;

    //forecast - get from result body forcast dailyForcast HOUR hourlyForcast
    private String waveheighttype;
    private int waveheightvalueCm;
    private String windspeedtype;
    private int windspeedvalueKmph;
    private String watertemperaturetype;
    private int watertemperaturevalueC;
    private String winddirectiontype;
    private int winddirectionvalueDeg;
    private String wavedirection;

    //default constructor
    public DetailedBeachMetadata() {}

    //constructor


    public DetailedBeachMetadata(int beachid, String beachname, boolean blueflag, boolean handicappedfriendly, String timezone, int airmaxtemperatureC, int airmintemperatureC, String jellyfishtype, String cleantype, String waveheighttype, int waveheightvalueCm, String windspeedtype, int windspeedvalueKmph, String watertemperaturetype, int watertemperaturevalueC, String winddirectiontype, int winddirectionvalueDeg, String wavedirection) {
        this.beachid = beachid;
        this.beachname = beachname;
        this.blueflag = blueflag;
        this.handicappedfriendly = handicappedfriendly;
        this.timezone = timezone;
        this.airmaxtemperatureC = airmaxtemperatureC;
        this.airmintemperatureC = airmintemperatureC;
        this.jellyfishtype = jellyfishtype;
        this.cleantype = cleantype;
        this.waveheighttype = waveheighttype;
        this.waveheightvalueCm = waveheightvalueCm;
        this.windspeedtype = windspeedtype;
        this.windspeedvalueKmph = windspeedvalueKmph;
        this.watertemperaturetype = watertemperaturetype;
        this.watertemperaturevalueC = watertemperaturevalueC;
        this.winddirectiontype = winddirectiontype;
        this.winddirectionvalueDeg = winddirectionvalueDeg;
        this.wavedirection = wavedirection;
    }

    //getters & setters
    public int getBeachid() {
        return beachid;
    }

    public void setBeachid(int beachid) {
        this.beachid = beachid;
    }

    public String getBeachname() {
        return beachname;
    }

    public void setBeachname(String beachname) {
        this.beachname = beachname;
    }

    public boolean isBlueflag() {
        return blueflag;
    }

    public void setBlueflag(boolean blueflag) {
        this.blueflag = blueflag;
    }

    public boolean isHandicappedfriendly() {
        return handicappedfriendly;
    }

    public void setHandicappedfriendly(boolean handicappedfriendly) {
        this.handicappedfriendly = handicappedfriendly;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getAirmaxtemperatureC() {
        return airmaxtemperatureC;
    }

    public void setAirmaxtemperatureC(int airmaxtemperatureC) {
        this.airmaxtemperatureC = airmaxtemperatureC;
    }

    public int getAirmintemperatureC() {
        return airmintemperatureC;
    }

    public void setAirmintemperatureC(int airmintemperatureC) {
        this.airmintemperatureC = airmintemperatureC;
    }

    public String getJellyfishtype() {
        return jellyfishtype;
    }

    public void setJellyfishtype(String jellyfishtype) {
        this.jellyfishtype = jellyfishtype;
    }

    public String getCleantype() {
        return cleantype;
    }

    public void setCleantype(String cleantype) {
        this.cleantype = cleantype;
    }

    public String getWaveheighttype() {
        return waveheighttype;
    }

    public void setWaveheighttype(String waveheighttype) {
        this.waveheighttype = waveheighttype;
    }

    public int getWaveheightvalueCm() {
        return waveheightvalueCm;
    }

    public void setWaveheightvalueCm(int waveheightvalueCm) {
        this.waveheightvalueCm = waveheightvalueCm;
    }

    public String getWindspeedtype() {
        return windspeedtype;
    }

    public void setWindspeedtype(String windspeedtype) {
        this.windspeedtype = windspeedtype;
    }

    public int getWindspeedvalueKmph() {
        return windspeedvalueKmph;
    }

    public void setWindspeedvalueKmph(int windspeedvalueKmph) {
        this.windspeedvalueKmph = windspeedvalueKmph;
    }

    public String getWatertemperaturetype() {
        return watertemperaturetype;
    }

    public void setWatertemperaturetype(String watertemperaturetype) {
        this.watertemperaturetype = watertemperaturetype;
    }

    public int getWatertemperaturevalueC() {
        return watertemperaturevalueC;
    }

    public void setWatertemperaturevalueC(int watertemperaturevalueC) {
        this.watertemperaturevalueC = watertemperaturevalueC;
    }

    public String getWinddirectiontype() {
        return winddirectiontype;
    }

    public void setWinddirectiontype(String winddirectiontype) {
        this.winddirectiontype = winddirectiontype;
    }

    public int getWinddirectionvalueDeg() {
        return winddirectionvalueDeg;
    }

    public void setWinddirectionvalueDeg(int winddirectionvalueDeg) {
        this.winddirectionvalueDeg = winddirectionvalueDeg;
    }

    public String getWavedirection() {
        return wavedirection;
    }

    public void setWavedirection(String wavedirection) {
        this.wavedirection = wavedirection;
    }
}
