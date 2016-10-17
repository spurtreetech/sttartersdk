package com.spurtreetech.sttarter.lib.helper.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by RahulT on 17-07-2015.
 */
public class DateTimeHelper {

    /**
     * get current timestamp
     * @return String
     */
    public static String getCurrentTimeStamp() {
        DateTime dt = new DateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        dt.changeTimeZone(Calendar.getInstance().getTimeZone(), TimeZone.getTimeZone("GMT"));
        Log.d("DateTimeHelper", "converted string: " + dt.toString());
        return dt.toString();
    }

    public static long getUnixTimeStamp() {
        return System.currentTimeMillis() / 1000L;
    }

    public static long getUnixTimeStampFromISO8601(String dateTimeString) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTimeString);
            return date.getTime() / 1000L;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTimeStampFromUnixTime(String unixTimeString) {
        long unixTime = Long.parseLong(unixTimeString);
        java.util.Date time = new java.util.Date((long)unixTime*1000);
        DateTime dt = new DateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));
        dt.changeTimeZone(Calendar.getInstance().getTimeZone(), TimeZone.getTimeZone("GMT"));
        Log.d("DateTimeHelper", "getTimeStampFromUnixTime: " + dt.toString());
        return dt.toString();
    }

    public static  String getElapsedTime(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date elapsedDate = sdf.parse(dateString);
            return getElapsedTimeString(elapsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static  String getElapsedTime(Long unixTimeStamp) {
        //long unixTime = Long.parseLong(unixTimeStamp);
        java.util.Date time = new java.util.Date((long)unixTimeStamp*1000);
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //DateTime dt = new DateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));
        //Date date = new Date(time);
        //Date dt = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));
        return getElapsedTimeString(time);
        /*
        try {
            //Date elapsedDate = sdf.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        */
    }

    private static String getElapsedTimeString(Date elapsedDate) {

        int years;
        int months;
        int days;
        int hours;
        int minutes;
        int seconds;

        Date currentDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss aa");
        float diff= currentDate.getTime() - elapsedDate.getTime();
        if (diff >= 0) {
            int yearDiff = Math.round( ( diff/ (365l*2592000000f))>=1?( diff/ (365l*2592000000f)):0);
            if (yearDiff > 0) {
                years = yearDiff;
                return sdf.format(elapsedDate);
               //return years + (years == 1 ? " year" : " yrs") + " ago";
            } else {
                int monthDiff = Math.round((diff / 2592000000f)>=1?(diff / 2592000000f):0);
                if (monthDiff > 0) {
                    if (monthDiff > 11)
                        monthDiff = 11;

                    months = monthDiff;
                    return sdf.format(elapsedDate);
                    //return months + (months == 1 ? " month" : " months") + " ago";
                } else {
                    int dayDiff = Math.round((diff / (86400000f))>=1?(diff / (86400000f)):0);
                    if (dayDiff > 0) {
                        days = dayDiff;
                        if(days==30)
                            days=29;
                        return sdf.format(elapsedDate);
                        //return days + (days == 1 ? " day" : " days") + " ago";
                    } else {
                        int hourDiff = Math.round((diff / (3600000f))>=1?(diff / (3600000f)):0);
                        if (hourDiff > 0) {
                            hours = hourDiff;
                            return hours + (hours == 1 ? " hour" : " hrs") + " ago";
                        } else {
                            int minuteDiff = Math.round((diff / (60000f))>=1?(diff / (60000f)):0);
                            if (minuteDiff > 0) {
                                minutes = minuteDiff;
                                return minutes + (minutes == 1 ? " minute" : " mins") + " ago";
                            } else {
                                int secondDiff =Math.round((diff / (1000f))>=1?(diff / (1000f)):0);
                                if (secondDiff > 0)
                                    seconds = secondDiff;
                                else
                                    seconds = 1;
                                return seconds + (seconds == 1 ? " second" : " secs") + " ago";
                            }
                        }
                    }

                }
            }

        }
        else{
            return "now";
        }
    }


    public static  String getTimeOrDateString(Long unixTimeStamp) {
        //long unixTime = Long.parseLong(unixTimeStamp);
        java.util.Date time = new java.util.Date((long)unixTimeStamp*1000);
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //DateTime dt = new DateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));
        //Date date = new Date(time);
        //Date dt = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));
        return getTimeOrDateStringL((long) unixTimeStamp * 1000);
        /*
        try {
            //Date elapsedDate = sdf.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        */
    }

    private static String getTimeOrDateStringL(long smsTimeInMilis) {

        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        String timeFormatString = "h:mm aa";

        SimpleDateFormat sdfToday =new SimpleDateFormat(timeFormatString);

        String dateTimeFormatString = "MMM dd, h:mm aa";

        String dateTimeFormatStringLastYear = "MMM dd yyyy, h:mm aa";

        SimpleDateFormat sdfThisYear =new SimpleDateFormat(dateTimeFormatString);

        SimpleDateFormat sdfOtherYear =new SimpleDateFormat(dateTimeFormatStringLastYear);


        java.util.Date time = new java.util.Date(smsTimeInMilis);

        final long HOURS = 60 * 60 * 60;
        if(now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ){
            return sdfToday.format(time)+"";
        }else if(now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1 ){
            return "Yesterday at " + sdfToday.format(time);
        }else if(now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)){
            return sdfThisYear.format(time);
        }else if(now.get(Calendar.YEAR) != smsTime.get(Calendar.YEAR)) {
            return sdfOtherYear.format(time);
        }
        else
            return sdfToday.format(time);
    }


    private static String getElapsedTimeString(DateTime elapsedDate) {
        return "";
    }
}
