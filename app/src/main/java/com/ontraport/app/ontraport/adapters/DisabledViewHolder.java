package com.ontraport.app.ontraport.adapters;

import android.graphics.Color;
import android.view.View;

public class DisabledViewHolder extends RecordViewHolder {

    public DisabledViewHolder(View view) {
        super(view);
        text_view.setFocusable(false);
        text_view.setEnabled(false);
        text_view.setCursorVisible(false);
        text_view.setKeyListener(null);
        text_view.setTextColor(Color.BLACK);
        text_view.setBackgroundColor(Color.TRANSPARENT);
    }
}
