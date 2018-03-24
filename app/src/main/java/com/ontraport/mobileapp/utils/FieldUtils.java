package com.ontraport.mobileapp.utils;


import android.content.Context;
import android.icu.text.DateFormat;
import android.os.Build;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.sdk.objects.ObjectType;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String[] getParentLabelListFields(int parent_id) {
        switch (parent_id) {
            case ObjectType.STAFF:
                return new String[]{"id", "firstname", "lastname"};
            case ObjectType.REFERRER:
                return new String[]{"id", "contact_label"};
            case ObjectType.CAMPAIGN:
            case ObjectType.LEAD_SOURCE:
            case ObjectType.MEDIUM:
            case ObjectType.CONTENT:
            case ObjectType.TERM:
                return new String[]{"id", "name"};
            default:
                return new String[]{"id"};
        }
    }

    public static boolean isSpecialField(String field) {
        switch (field) {
            case "parent_id//name":
                return true;
            default:
                return false;
        }
    }

    public static boolean allowsEmptyValues(int parent_id) {
        if (parent_id == ObjectType.STAFF) {
            return false;
        }
        return true;
    }

    public static String replaceMergeFields(String input, List<String> aliases, List<String> values) {
        Pattern pattern = Pattern.compile("\\[[^\\[\\]]+\\]");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String match = matcher.group();
            int pos = aliases.indexOf(match.substring(1, match.length() - 1));
            if (pos > -1) {
                input = input.replace(match, values.get(pos));
            }
        }
        return input.replace("()","").trim();
    }
}

