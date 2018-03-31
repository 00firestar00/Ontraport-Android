package com.ontraport.mobileapp.main.collection;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import com.ontraport.mobileapp.R;

public abstract class AddRemoveDialog extends AlertDialog {

    private final RadioGroup radios;

    public AddRemoveDialog(@NonNull Context context, @StringRes int res) {
        super(context);
        setTitle(res);
        setMessage(res);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_add_remove, null);
        radios = dialogView.findViewById(R.id.radio_group);
        setView(dialogView);
        setNegativeButton();
        setPositiveButton();
    }

    private void setMessage(@StringRes int res) {
        super.setMessage("Add or Remove" + getContext().getString(res));
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

    abstract void onCancel();

    abstract void onSuccess();

}
