package com.ontraport.mobileapp.main.record.views;

import android.text.InputType;
import android.view.View;

public class EmailViewHolder extends TextViewHolder {

    public EmailViewHolder(View view) {
        super(view);
        edit_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }
}
