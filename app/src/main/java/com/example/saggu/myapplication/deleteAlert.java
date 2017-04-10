package com.example.saggu.myapplication;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;



/**
 * Created by Saggu on 1/1/2017.
 */

public class DeleteAlert extends DialogFragment {
    int custid;
    int stbId;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
         custid = bundle.getInt("CUSTID");
         stbId =bundle.getInt("STBID");
        Log.d("sdfsdd", "custid"+ custid+ " stbid "+stbId);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("DELETE..!!!!");
        builder.setMessage("Are you Sure??");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
                    }
                }

        );
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewAll activity = (ViewAll)getActivity();
                activity.delete(custid, stbId);
            }
        });

        Dialog dialog = builder.create();


        return  dialog;
    }
}
