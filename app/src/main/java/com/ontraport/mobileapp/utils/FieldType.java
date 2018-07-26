package com.ontraport.mobileapp.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Define the list of accepted constants and declare the NavigationMode annotation
@Retention(RetentionPolicy.SOURCE)
@IntDef({
        FieldType.ADDRESS,
        FieldType.CHECK,
        FieldType.COUNTRY,
        FieldType.DROP,
        FieldType.EMAIL,
        FieldType.FULLDATE,
        FieldType.LIST,
        FieldType.LONGTEXT,
        FieldType.MERGEFIELD,
        FieldType.NUMERIC,
        FieldType.PARENT,
        FieldType.PHONE,
        FieldType.PRICE,
        FieldType.SMS,
        FieldType.STATE,
        FieldType.SUBSCRIPTION,
        FieldType.TEXT,
        FieldType.TIMESTAMP,
        FieldType.TIMEZONE,
        FieldType.URL,
        FieldType.DISABLED,
})

public @interface FieldType {
    int ADDRESS = 0;
    int CHECK = 1;
    int COUNTRY = 2;
    int DROP = 3;
    int EMAIL = 4;
    int FULLDATE = 5;
    int LIST = 6;
    int LONGTEXT = 7;
    int MERGEFIELD = 8;
    int NUMERIC = 9;
    int PARENT = 10;
    int PHONE = 11;
    int PRICE = 12;
    int SMS = 13;
    int STATE = 14;
    int SUBSCRIPTION = 15;
    int TEXT = 16;
    int TIMESTAMP = 17;
    int TIMEZONE = 18;
    int URL = 19;
    int DISABLED = -1;
}
