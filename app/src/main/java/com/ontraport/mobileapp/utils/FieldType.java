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
        FieldType.FULLDATE,
        FieldType.LIST,
        FieldType.LONGTEXT,
        FieldType.NUMERIC,
        FieldType.PRICE,
        FieldType.PHONE,
        FieldType.STATE,
        FieldType.DROP,
        FieldType.TEXT,
        FieldType.EMAIL,
        FieldType.SMS,
        FieldType.MERGEFIELD,
        FieldType.PARENT,
        FieldType.URL,
        FieldType.DISABLED
})

public @interface FieldType {
    int ADDRESS = 0;
    int CHECK = 1;
    int COUNTRY = 2;
    int FULLDATE = 3;
    int LIST = 4;
    int LONGTEXT = 5;
    int NUMERIC = 6;
    int PRICE = 7;
    int PHONE = 8;
    int STATE = 9;
    int DROP = 10;
    int TEXT = 11;
    int EMAIL = 12;
    int SMS = 13;
    int MERGEFIELD = 14;
    int PARENT = 15;
    int URL = 16;
    int DISABLED = -1;
}
