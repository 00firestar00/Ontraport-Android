package com.ontraport.mobileapp.adapters.views;

import android.graphics.Color;
import android.view.View;

public class DisabledViewHolder extends TextViewHolder {

    public DisabledViewHolder(View view) {
        super(view);
        edit_text.setFocusable(false);
        edit_text.setEnabled(false);
        edit_text.setCursorVisible(false);
        edit_text.setKeyListener(null);
        edit_text.setTextColor(Color.BLACK);
        edit_text.setBackgroundColor(Color.TRANSPARENT);
    }
}
