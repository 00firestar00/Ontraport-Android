package com.ontraport.mobileapp.main.collection;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.ontraport.mobileapp.AsyncAdapter;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.collection.asynctasks.GetListAsyncTask;
import com.ontraport.mobileapp.main.record.RecordInfo;
import com.ontraport.mobileapp.utils.Constants;
import com.ontraport.sdk.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

public abstract class AddRemoveDialog extends AlertDialog implements AdapterView.OnItemSelectedListener {

    private final RadioGroup radios;
    private final Spinner drop_down;
    private SubscribeCollectionAdapter adapter;
    private Map<String, Integer> true_values = new HashMap<>();

    public AddRemoveDialog(@NonNull Context context, @StringRes int res, RequestParams params) {
        super(context);
        setTitle(res);
        setMessage(res);

        adapter = getNewAdapter();

        new GetListAsyncTask<>(adapter,
                new String[]{"name"},
                params.getAsInt(Constants.OBJECT_ID)).execute(params);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_add_remove, null);
        radios = dialogView.findViewById(R.id.radio_group);
        drop_down = dialogView.findViewById(R.id.dropDown1);
        drop_down.setOnItemSelectedListener(this);
        drop_down.setAdapter(adapter);
        setView(dialogView);
        setNegativeButton();
        setPositiveButton();
    }

    abstract void onCancel();

    abstract void onSuccess();

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String new_value = drop_down.getItemAtPosition(position).toString();
        String field = (String) drop_down.getTag();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // We do nothing.
    }

    private void setMessage(@StringRes int res) {
        super.setMessage("Add or Remove " + getContext().getString(res));
    }

    private void setNegativeButton() {
        setButton(BUTTON_NEGATIVE, "CANCEL",
                new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onCancel();
                        dialog.cancel();
                    }
                });
    }

    private void setPositiveButton() {
        setButton(BUTTON_POSITIVE, "SUCCESS",
                new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onSuccess();
                        dialog.dismiss();
                    }
                });
    }

    private SubscribeCollectionAdapter getNewAdapter() {
        return new SubscribeCollectionAdapter(getContext(),
                R.layout.record_spinner,
                R.id.spinnerText);
    }

    class SubscribeCollectionAdapter extends ArrayAdapter<String> implements AsyncAdapter<CollectionInfo> {

        SubscribeCollectionAdapter(@NonNull Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        @Override
        public void handleNullResponse() {

        }

        @Override
        public void updateInfo(CollectionInfo collection) {
            // Add the new values
            addAll(collection.getDataValues());
            for (RecordInfo record : collection.getData()) {
                true_values.put(record.toString(), record.getId());
            }
        }

        @Override
        public void updateParentInfo(CollectionInfo collection) {
            throw new UnsupportedOperationException();
        }
    }
}
