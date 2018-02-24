package com.ontraport.app.ontraport.adapters;

import android.text.InputType;
import android.view.View;

public class TextViewHolder extends RecordViewHolder {

    public TextViewHolder(View view) {
        super(view);
        edit_text.setInputType(InputType.TYPE_CLASS_TEXT);
    }
}
