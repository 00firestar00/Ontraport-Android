package com.ontraport.mobileapp.main.record.views;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.record.asynctasks.UpdateAsyncTask;

public class TextViewHolder extends RecordViewHolder
        implements TextView.OnEditorActionListener, View.OnFocusChangeListener {

    final TextInputEditText edit_text;
    private final TextInputLayout text_input;

    public TextViewHolder(View view) {
        super(view);
        text_input = view.findViewById(R.id.textInput);
        edit_text = view.findViewById(R.id.textView1);
        edit_text.setOnEditorActionListener(this);
        edit_text.setOnFocusChangeListener(this);
        edit_text.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    @Override
    public void setText(String key, String value, String alias) {
        super.setText(key, value, alias);
        edit_text.setTag(key);
        edit_text.setText(value);
        text_input.setHint(alias);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String new_val = edit_text.getText().toString();
        String field = (String) edit_text.getTag();
        if (actionId == EditorInfo.IME_ACTION_DONE && !new_val.equals(old_val)) {
            doUpdate(field, new_val);
            return true;
        }
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        String new_val = edit_text.getText().toString();
        String field = (String) edit_text.getTag();
        if (!hasFocus && !new_val.equals(old_val)) {
            doUpdate(field, new_val);
        }
    }

    @Override
    protected void doUpdate(String field, String new_val) {
        super.doUpdate(field, new_val);
        old_val = new_val;
        params.put(field, new_val);
        new UpdateAsyncTask(view.getContext()).execute(params);
    }
}
