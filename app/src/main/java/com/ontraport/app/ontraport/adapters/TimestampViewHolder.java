package com.ontraport.app.ontraport.adapters;

import android.text.InputType;
import android.view.View;

public class TimestampViewHolder extends RecordViewHolder {

    public TimestampViewHolder(View view) {
        super(view);
        edit_text.setInputType(InputType.TYPE_CLASS_DATETIME);
    }
}
