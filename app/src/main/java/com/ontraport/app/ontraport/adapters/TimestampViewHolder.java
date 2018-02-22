package com.ontraport.app.ontraport.adapters;

import android.text.InputType;
import android.view.View;

public class TimestampViewHolder extends RecordViewHolder {

    public TimestampViewHolder(View view) {
        super(view);
        text_view.setInputType(InputType.TYPE_CLASS_DATETIME);
    }
}
