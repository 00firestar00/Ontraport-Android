package com.ontraport.mobileapp.main.record.views;

import android.text.InputType;
import android.view.View;

public class UrlViewHolder extends TextViewHolder {

    public UrlViewHolder(View view) {
        super(view);
        edit_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
    }
}
