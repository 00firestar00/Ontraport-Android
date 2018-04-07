package com.ontraport.mobileapp.main.collection;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.utils.ThemedAlertDialog;

public abstract class DeleteDialog extends ThemedAlertDialog {

    private final String title = "DELETE";
    private final EditText input;

    public DeleteDialog(@NonNull Context context, @ColorInt int theme, int num_deleted, String label) {
        super(context, theme);
        setTitle(title);
        setMessage(num_deleted, label);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_delete, null);
        input = dialogView.findViewById(R.id.edit_text);
        setView(dialogView);

        setNegativeButton();
        setPositiveButton();
    }

    private void setMessage(int num_deleted, String label) {
        super.setMessage("Are you sure you want to delete these " + num_deleted + " " + label + "?\n" +
                "Please type " + title + " to confirm.");
    }

    private void setNegativeButton() {
        setButton(BUTTON_NEGATIVE, "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onCancel();
                        dialog.cancel();
                    }
                });
    }

    private void setPositiveButton() {
        setButton(BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().equals(title)) {
                            onSuccess();
                        }
                        else {
                            onIncorrectInput();
                        }
                        dialog.dismiss();
                    }
                });
    }

    abstract void onCancel();

    abstract void onIncorrectInput();

    abstract void onSuccess();

}
