package com.ontraport.app.ontraport.adapters;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ontraport.app.ontraport.R;
import com.ontraport.app.ontraport.tasks.Update;
import com.ontraport.sdk.http.RequestParams;

public class RecordViewHolder extends RecyclerView.ViewHolder
        implements TextView.OnEditorActionListener, View.OnFocusChangeListener {

    public final View view;
    final EditText edit_text;
    private String old_val;
    private final TextInputLayout text_input;
    private RequestParams params;

    public RecordViewHolder(View view) {
        super(view);
        this.view = view;
        text_input = view.findViewById(R.id.textInput);
        edit_text = view.findViewById(R.id.textView1);
        edit_text.setOnEditorActionListener(this);
        edit_text.setOnFocusChangeListener(this);
    }

    public void setText(String key, String value, String alias) {
        edit_text.setTag(key);
        edit_text.setText(value);
        text_input.setHint(alias);
        old_val = value;
    }

    public void setParams(int object_id, String id) {
        params = new RequestParams();
        params.put("objectID", object_id);
        params.put("id", id);
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
