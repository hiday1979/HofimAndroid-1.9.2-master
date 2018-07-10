package com.wigitech.yam.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by GolaNir on 07/06/2017.
 */

public class DetailedCoastWithForcast {


    @SerializedName("beachId")
    private int beachid;
    @SerializedName("beachName")
    private String beachname;
    @SerializedName("waveHeightType")
    private String waveheighttype;
    @SerializedName("waveHeightValue")
    private String waveheightvalue;
    @SerializedName("windSpeedType")
    private String windspeedtype;
    @SerializedName("windSpeedValue")
    private String windspeedvalue;
    @SerializedName("waterTemperatureType")
    private String watertemperaturetype;
    @SerializedName("waterTemperatureValue")
    private String watertemperaturevalue;
    @SerializedName("windDirectionType")
    private String winddirectiontype;
    @SerializedName("jellyFishType")
    private String jellyfishtype;
    @SerializedName("cleanType")
    private String cleantype;
    @SerializedName("blueFlag")
    private String blueflag;
    @SerializedName("handicappedFriendly")
    private String handicappedfriendly;
    @SerializedName("timeZone")
    private String timezone;
    @SerializedName("result")
    private Result result;

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

    public String getWaveheighttype() {
        return waveheighttype;
    }

    public void setWaveheighttype(String waveheighttype) {
        this.waveheighttype = waveheighttype;
    }

    public String getWaveheightvalue() {
        return waveheightvalue;
    }

    public void setWaveheightvalue(String waveheightvalue) {
        this.waveheightvalue = waveheightvalue;
    }

    public String getWindspeedtype() {
        return windspeedtype;
    }

    public void setWindspeedtype(String windspeedtype) {
        this.windspeedtype = windspeedtype;
    }

    public String getWindspeedvalue() {
        return windspeedvalue;
    }

    public void setWindspeedvalue(String windspeedvalue) {
        this.windspeedvalue = windspeedvalue;
    }

    public String getWatertemperaturetype() {
        return watertemperaturetype;
    }

    public void setWatertemperaturetype(String watertemperaturetype) {
        this.watertemperaturetype = watertemperaturetype;
    }

    public String getWatertemperaturevalue() {
        return watertemperaturevalue;
    }

    public void setWatertemperaturevalue(String watertemperaturevalue) {
        this.watertemperaturevalue = watertemperaturevalue;
    }

    public String getWinddirectiontype() {
        return winddirectiontype;
    }

    public void setWinddirectiontype(String winddirectiontype) {
        this.winddirectiontype = winddirectiontype;
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

    public String getBlueflag() {
        return blueflag;
    }

    public void setBlueflag(String blueflag) {
        this.blueflag = blueflag;
    }

    public String getHandicappedfriendly() {
        return handicappedfriendly;
    }

    public void setHandicappedfriendly(String handicappedfriendly) {
        this.handicappedfriendly = handicappedfriendly;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Metadata {
        @SerializedName("beachId")
        private int beachid;
        @SerializedName("beachName")
        private String beachname;
        @SerializedName("stringEncoding")
        private String stringencoding;
        @SerializedName("blueFlag")
        private boolean blueflag;
        @SerializedName("handicappedFriendly")
        private boolean handicappedfriendly;
        @SerializedName("timeZone")
        private String timezone;

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

        public String getStringencoding() {
            return stringencoding;
        }

        public void setStringencoding(String stringencoding) {
            this.stringencoding = stringencoding;
        }

        public boolean getBlueflag() {
            return blueflag;
        }

        public void setBlueflag(boolean blueflag) {
            this.blueflag = blueflag;
        }

        public boolean getHandicappedfriendly() {
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
    }

    public static class Dailystats {
        @SerializedName("airMaxTemperature_C")
        private int airmaxtemperatureC;
        @SerializedName("airMinTemperature_C")
        private int airmintemperatureC;

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
    }

    public static class Forcast {
        @SerializedName("waveHeightType")
        private String waveheighttype;
        @SerializedName("waveHeightValue_Cm")
        private int waveheightvalueCm;
        @SerializedName("windSpeedType")
        private String windspeedtype;
        @SerializedName("windSpeedValue_Kmph")
        private int windspeedvalueKmph;
        @SerializedName("waterTemperatureType")
        private String watertemperaturetype;
        @SerializedName("waterTemperatureValue_C")
        private int watertemperaturevalueC;
        @SerializedName("windDirectionType")
        private String winddirectiontype;
        @SerializedName("windDirectionValue_Deg")
        private int winddirectionvalueDeg;
        @SerializedName("waveDirection")
        private String wavedirection;

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

    public static class Hourlyforcast {
        @SerializedName("hourOfDay")
        private int hourofday;
        @SerializedName("forcast")
        private Forcast forcast;

        public int getHourofday() {
            return hourofday;
        }

        public void setHourofday(int hourofday) {
            this.hourofday = hourofday;
        }

        public Forcast getForcast() {
            return forcast;
        }

        public void setForcast(Forcast forcast) {
            this.forcast = forcast;
        }
    }

    public static class Dailyforcast {
        @SerializedName("date")
        private String date;
        @SerializedName("size")
        private int size;
        @SerializedName("dailyStats")
        private Dailystats dailystats;
        @SerializedName("hourlyForcast")
        private List<Hourlyforcast> hourlyforcast;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public Dailystats getDailystats() {
            return dailystats;
        }

        public void setDailystats(Dailystats dailystats) {
            this.dailystats = dailystats;
        }

        public List<Hourlyforcast> getHourlyforcast() {
            return hourlyforcast;
        }

        public void setHourlyforcast(List<Hourlyforcast> hourlyforcast) {
            this.hourlyforcast = hourlyforcast;
        }
    }

    public static class Forcasts {
        @SerializedName("size")
        private int size;
        @SerializedName("dailyForcast")
        private List<Dailyforcast> dailyforcast;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public List<Dailyforcast> getDailyforcast() {
            return dailyforcast;
        }

        public void setDailyforcast(List<Dailyforcast> dailyforcast) {
            this.dailyforcast = dailyforcast;
        }
    }

    public static class Reports {
        @SerializedName("jellyFishType")
        private String jellyfishtype;
        @SerializedName("cleanType")
        private String cleantype;

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
    }

    public static class Body {
        @SerializedName("forcasts")
        private Forcasts forcasts;
        @SerializedName("reports")
        private Reports reports;

        public Forcasts getForcasts() {
            return forcasts;
        }

        public void setForcasts(Forcasts forcasts) {
            this.forcasts = forcasts;
        }

        public Reports getReports() {
            return reports;
        }

        public void setReports(Reports reports) {
            this.reports = reports;
        }
    }

    public static class Result {
        @SerializedName("metaData")
        private Metadata metadata;
        @SerializedName("body")
        private Body body;

        public Metadata getMetadata() {
            return metadata;
        }

        public void setMetadata(Metadata metadata) {
            this.metadata = metadata;
        }

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }
    }

    @Override
    public String toString() {
        return "DetailedCoastWithForcast{" +
                "beachid=" + beachid +
                ", beachname='" + beachname + '\'' +
                ", waveheighttype='" + waveheighttype + '\'' +
                ", waveheightvalue='" + waveheightvalue + '\'' +
                ", windspeedtype='" + windspeedtype + '\'' +
                ", windspeedvalue='" + windspeedvalue + '\'' +
                ", watertemperaturetype='" + watertemperaturetype + '\'' +
                ", watertemperaturevalue='" + watertemperaturevalue + '\'' +
                ", winddirectiontype='" + winddirectiontype + '\'' +
                ", jellyfishtype='" + jellyfishtype + '\'' +
                ", cleantype='" + cleantype + '\'' +
                ", blueflag='" + blueflag + '\'' +
                ", handicappedfriendly='" + handicappedfriendly + '\'' +
                ", timezone='" + timezone + '\'' +
                ", result=" + result +
                ", result=" +  +
                '}';
    }
}


//    private int airMaxTemperature_C;
//    private int airMinTemperature_C;
//    private String waveHeightType;
//    private int waveHeightValue_Cm;
//    private String windSpeedType;
//    private int windSpeedValue_Kmph;
//    private String waterTemperatureType;
//    private String waterTemperatureValue_C;
//    private String windDirectionType;
//    private int windDirectionValue_Deg;
//    private String waveDirection;
//    private String jellyFishType;
//    private String cleanType;





