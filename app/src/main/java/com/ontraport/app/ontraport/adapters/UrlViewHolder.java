package com.ontraport.app.ontraport.adapters;

import android.text.InputType;
import android.view.View;

public class UrlViewHolder extends RecordViewHolder {

    public UrlViewHolder(View view) {
        super(view);
        edit_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
    }
}
