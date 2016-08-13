package com.example.abhi.thunderbolt10;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by abhi on 8/7/2016.
 */
public class CurrentWeather {
    String mIcon;
    private long mTime;
    private double mTemperature;

    public String getTimeZone()  {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    private String mTimeZone;
    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    private double mHumidity;
    private double mPrecipChance;
    private String summmary;

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }
//clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
public int getIconId() {
    // clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night.
    int iconId = R.drawable.clear_day;

    if (mIcon.equals("clear-day")) {
        iconId = R.drawable.clear_day;
    }
    else if (mIcon.equals("clear-night")) {
        iconId = R.drawable.clear_night;
    }
    else if (mIcon.equals("rain")) {
        iconId = R.drawable.rain;
    }
    else if (mIcon.equals("snow")) {
        iconId = R.drawable.snow;
    }
    else if (mIcon.equals("sleet")) {
        iconId = R.drawable.sleet;
    }
    else if (mIcon.equals("wind")) {
        iconId = R.drawable.wind;
    }
    else if (mIcon.equals("fog")) {
        iconId = R.drawable.fog;
    }
    else if (mIcon.equals("cloudy")) {
        iconId = R.drawable.cloudy;
    }
    else if (mIcon.equals("partly-cloudy-day")) {
        iconId = R.drawable.partly_cloudy;
    }
    else if (mIcon.equals("partly-cloudy-night")) {
        iconId = R.drawable.cloudy_night;
    }

    return iconId;
}


    public long getTime() {
        return mTime;
    }
    public String getDateFormat()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime()*1000);
        String timeString = formatter.format(dateTime);
        return timeString;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        return (int)Math.round(mTemperature);//using math round converts double to long and (int) for int
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public int getPrecipChance() {

        double precipPercantage = mPrecipChance*100;
        return (int)Math.round(precipPercantage);
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummmary() {
        return summmary;
    }

    public void setSummmary(String summmary) {
        this.summmary = summmary;
    }
}
