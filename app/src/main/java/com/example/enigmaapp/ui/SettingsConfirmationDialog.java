package com.example.enigmaapp.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.enigmaapp.R;

public class SettingsConfirmationDialog extends AppCompatDialogFragment {
    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.settings_confirmation_modal, null);
        builder.setView(view)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alert = builder.create();

        // TODO: fix btn bgc
//        Button btn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
//        btn.setBackgroundColor(R.color.colorPrimary);
//        btn.setTextColor(R.color.textColor);
        return alert;
    }
}
