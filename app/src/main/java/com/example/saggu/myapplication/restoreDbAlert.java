package com.example.saggu.myapplication;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by Saggu on 1/1/2017.
 */

public class RestoreDbAlert extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
       // id = bundle.getInt("ID");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Database Restore");
        builder.setMessage("Are you Sure??");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Not Restored", Toast.LENGTH_SHORT).show();
                    }
                }

        );
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewAll activity = (ViewAll)getActivity();
                activity.restoreDB();
            }
        });

        Dialog dialog = builder.create();


        return  dialog;
    }
}
