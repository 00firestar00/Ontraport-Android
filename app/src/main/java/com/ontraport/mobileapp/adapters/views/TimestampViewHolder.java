package com.ontraport.mobileapp.adapters.views;

import android.text.InputType;
import android.view.View;

public class TimestampViewHolder extends TextViewHolder {

    public TimestampViewHolder(View view) {
        super(view);
        edit_text.setInputType(InputType.TYPE_CLASS_DATETIME);
    }
}
