package com.ontraport.mobileapp.main.collection;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

public abstract class DeleteDialog extends AlertDialog {

    private final String title = "DELETE";
    private final EditText input;

    public DeleteDialog(@NonNull Context context, int num_deleted, String label) {
        super(context);
        setTitle(title);
        setMessage(num_deleted, label);
        input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        setView(input);
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
