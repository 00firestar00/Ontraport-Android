package com.ontraport.mobileapp.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

public class ThemedAlertDialog extends AlertDialog {

    @ColorInt
    private int theme;

    protected ThemedAlertDialog(@NonNull Context context, @ColorInt int theme) {
        super(context);
        this.theme = theme;
    }

    @Override
    public void show() {
        super.show();
        if (getButton(BUTTON_NEGATIVE) != null) {
            getButton(BUTTON_NEGATIVE).setTextColor(theme);
        }

        Button positive = getButton(BUTTON_POSITIVE);
        if (positive != null) {
            positive.setTextColor(Color.WHITE);
            positive.setBackgroundColor(theme);
        }
    }

    @ColorInt
    public int getTheme() {
        return theme;
    }
}
