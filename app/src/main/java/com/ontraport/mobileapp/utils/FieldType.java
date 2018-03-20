package com.ontraport.mobileapp.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Define the list of accepted constants and declare the NavigationMode annotation
@Retention(RetentionPolicy.SOURCE)
@IntDef({
        FieldType.TEXT,
        FieldType.DISABLED,
        FieldType.PHONE,
        FieldType.TIMESTAMP,
        FieldType.URL,
        FieldType.NUMERIC,
        FieldType.EMAIL,
        FieldType.DROP,
        FieldType.LIST,
        FieldType.PARENT,
        FieldType.DATE
})

public @interface FieldType {
    int TEXT = 0;
    int DISABLED = 1;
    int PHONE = 2;
    int TIMESTAMP = 3;
    int URL = 4;
    int NUMERIC = 5;
    int EMAIL = 6;
    int DROP = 7;
    int LIST = 8;
    int PARENT = 9;
    int DATE = 10;
}

