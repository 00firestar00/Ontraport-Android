package com.ontraport.mobileapp.adapters.views;

import android.text.InputType;
import android.view.View;

public class PhoneViewHolder extends TextViewHolder {

    public PhoneViewHolder(View view) {
        super(view);
        edit_text.setInputType(InputType.TYPE_CLASS_PHONE);
    }
}
