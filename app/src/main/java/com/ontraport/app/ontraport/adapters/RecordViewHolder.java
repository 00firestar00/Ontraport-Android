package com.ontraport.app.ontraport.adapters;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.ontraport.app.ontraport.R;

public class RecordViewHolder extends RecyclerView.ViewHolder {

    public View view;
    public TextView text_view;
    public TextInputLayout text_input;

    public RecordViewHolder(View view) {
        super(view);
        this.view = view;
        text_view = view.findViewById(R.id.textView1);
        text_input = view.findViewById(R.id.textInput);
    }

    public void setText(String key, String value, String alias) {
        text_view.setTag(key);
        text_view.setText(value);
        text_input.setHint(alias);
    }
}
