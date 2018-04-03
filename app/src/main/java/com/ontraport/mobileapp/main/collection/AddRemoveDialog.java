package com.ontraport.mobileapp.main.collection;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
    private final ListView list_selection;
    private SubscribeCollectionAdapter adapter;
    private ListSelectionAdapter list_adapter;
    private Map<String, Integer> true_values = new HashMap<>();
    private int previous_pos = -1;

    public AddRemoveDialog(@NonNull Context context, @StringRes int res, RequestParams params) {
        super(context);
        setTitle(res);
        setMessage(res);

        adapter = getNewAdapter();
        adapter.add("Select...");

        new GetListAsyncTask<>(adapter,
                new String[]{"name"},
                params.getAsInt(Constants.OBJECT_ID)).execute(params);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_add_remove, null);
        radios = dialogView.findViewById(R.id.radio_group);
        drop_down = dialogView.findViewById(R.id.drop_down);
        list_selection = dialogView.findViewById(R.id.list_selection);
        list_adapter = new ListSelectionAdapter(getContext(), R.layout.record_list, R.id.listText);
        list_selection.setAdapter(list_adapter);
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
        if (position == previous_pos || position < 1) {
            return;
        }
        previous_pos = position;
        drop_down.setSelection(0);
        String new_value = drop_down.getItemAtPosition(position).toString();
        list_adapter.add(new_value);
        adapter.remove(new_value);
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

    class ListSelectionAdapter extends ArrayAdapter<String> implements View.OnClickListener {

        ListSelectionAdapter(@NonNull Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        @NonNull
        @Override
        public View getView(final int position, View convert_view, @NonNull ViewGroup parent) {
            View row = super.getView(position, convert_view, parent);
            Button remove = row.findViewById(R.id.remove);
            remove.setOnClickListener(this);
            return row;
        }


        @Override
        public void onClick(View v) {
            TextView text = ((View) v.getParent()).findViewById(R.id.listText);
            String to_remove = text.getText().toString();
            remove(to_remove);
            adapter.add(to_remove);
        }
    }
}
