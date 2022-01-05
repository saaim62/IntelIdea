package com.example.fypintelidea.core.utils;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MyDateTimeStamp {

    public static String getDateTimeFormattedStringFromMilliseconds(long milliSeconds) { //format = 17.09.2018 17:48
        // Create a DateFormatter object for displaying date in specified format.
//        SimpleDateFormat formatter = new SimpleDateFormat();
        String dateFormat = "dd.MM.yyyy HH:mm";
//        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        String milliSecondsInString = Long.toString(milliSeconds);
        if (milliSecondsInString.length() == 10 && milliSecondsInString.length() < 13) {
            milliSecondsInString = milliSecondsInString + "000";
            milliSeconds = Long.valueOf(milliSecondsInString);
        }
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getDateFormattedStringFromMilliseconds(long milliSeconds) { //format = 17.09.2018
        // Create a DateFormatter object for displaying date in specified format.
//        SimpleDateFormat formatter = new SimpleDateFormat();
        String dateFormat = "dd.MM.yyyy";
//        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        String milliSecondsInString = Long.toString(milliSeconds);
        if (milliSecondsInString.length() == 10 && milliSecondsInString.length() < 13) {
            milliSecondsInString = milliSecondsInString + "000";
            milliSeconds = Long.valueOf(milliSecondsInString);
        }
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    // use me if date time is in String else user date.getTime which returns time in long.
    public static Date dateTimeStringToDate(String dateTimeString) {
//        Log.d("DateTest", "dateTimeToLong: " + dateTimeString);
        Date dateTimeLong = null;
        try {
            dateTimeLong = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTimeLong;
    }

    // use me if date time is in String else user date.getTime which returns time in long.
    public static String dateTimeStringToDateInUTC(String dateTimeString) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date dateTimeLong = simpleDateFormat.parse(dateTimeString);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            dateTimeString = simpleDateFormat.format(dateTimeLong);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTimeString;
    }

    public static void setFrescoImage(SimpleDraweeView imageView, String url) {
        Uri uri = Uri.parse(url);
        imageView.setImageURI(uri);
    }
}
