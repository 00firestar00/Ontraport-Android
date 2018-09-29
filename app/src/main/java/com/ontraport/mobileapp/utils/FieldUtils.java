package com.ontraport.mobileapp.utils;


import android.content.Context;
import android.icu.text.DateFormat;
import android.os.Build;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.sdk.objects.ObjectType;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldUtils {

    public static final String OBJECT_ID = "objectID";
    public static final String OBJECT_TYPE_ID = "object_type_id";

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

    public static String[] getParentLabelListFields(ObjectType type) {
        if (type == null) {
            // null could just mean ObjectType doesn't have a definition.
            // This happens in the case of custom object ids.
            return new String[]{"id"};
        }

        switch (type) {
            case STAFF:
                return new String[]{"id", "firstname", "lastname"};
            case PARTNER:
                return new String[]{"id", "contact_label"};
            case TRACKING_CAMPAIGN:
            case TRACKING_LEAD_SOURCE:
            case TRACKING_MEDIUM:
            case TRACKING_CONTENT:
            case TRACKING_TERM:
            case DEAL:
            case COMPANY:
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

    public static boolean allowsEmptyValues(ObjectType type) {
        if (type == ObjectType.STAFF) {
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
        return input.replace("()", "").trim();
    }

    public static String[] legacyListToArray(String legacy) {
        return legacy.split("\\*\\/\\*");
    }

    public static String findIdField(Map<String, String> fields) {

        String[] possible_fields = new String[]{"id", "drip_id", "form_id"};

        for (String possible : possible_fields) {
            if (fields.containsKey(possible)) {
                return possible;
            }
        }
        return "id";
    }
}

