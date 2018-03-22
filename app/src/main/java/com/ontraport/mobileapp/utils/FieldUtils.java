package com.ontraport.mobileapp.utils;


import android.content.Context;
import android.icu.text.DateFormat;
import android.os.Build;
import com.ontraport.mobileapp.OntraportApplication;

import java.util.Date;

public class FieldUtils {

    public static String convertDate(String unix) {
        if (unix == null || unix.isEmpty()) {
            return "";
        }

        Date date = new Date(Long.parseLong(unix) * 1000);
        Context context = OntraportApplication.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String temp = android.icu.text.DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date);
            return temp.replace(", ", " ");
        }

        String date_string;
        java.text.DateFormat date_format = android.text.format.DateFormat.getDateFormat(context);
        date_string = date_format.format(date);
        java.text.DateFormat time_format = android.text.format.DateFormat.getTimeFormat(context);
        date_string += " " + time_format.format(date);

        return date_string;
    }
}
