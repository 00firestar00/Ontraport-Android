package com.ontraport.mobileapp.adapters.views;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.tasks.Update;

public class TextViewHolder extends RecordViewHolder
        implements TextView.OnEditorActionListener, View.OnFocusChangeListener {

    protected final TextInputEditText edit_text;
    protected final TextInputLayout text_input;

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

    private void doUpdate(String field, String new_val) {
        Toast.makeText(view.getContext(), "Updating to: " + new_val, Toast.LENGTH_LONG).show();
        old_val = new_val;
        params.put(field, new_val);
        new Update(view.getContext()).execute(params);
    }
}
