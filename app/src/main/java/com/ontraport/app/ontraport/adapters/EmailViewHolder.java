package com.ontraport.app.ontraport.adapters;

import android.text.InputType;
import android.view.View;

public class EmailViewHolder extends RecordViewHolder {

    public EmailViewHolder(View view) {
        super(view);
        text_view.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }
}
