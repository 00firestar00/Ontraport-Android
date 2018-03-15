package com.ontraport.mobileapp.main.record.views;

import android.text.InputType;
import android.view.View;

public class NumericViewHolder extends TextViewHolder {

    public NumericViewHolder(View view) {
        super(view);
        edit_text.setInputType(InputType.TYPE_CLASS_NUMBER);
    }
}
