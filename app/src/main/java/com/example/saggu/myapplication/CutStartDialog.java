package com.example.saggu.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Saggu on 4/10/2017.
 */

public class CutStartDialog extends DialogFragment {

    int custid;
    String cStatus;
    String btn;
    String newStatus;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        custid = bundle.getInt("CUSTID");
        cStatus= bundle.getString("STATUS");
        if (cStatus.equals("ACTIVE")){
            btn ="Cut";
            newStatus = "INACTIVE";
        }else {
            btn ="START";
            newStatus = "ACTIVE";

        }





        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Start Or Cut ..");
        builder.setMessage("Select Option or Cancel");
        builder.setPositiveButton(btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewAll activity = (ViewAll)getActivity();
                activity.changeConStatus(custid,newStatus);
                activity.refreshListView();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        Dialog dialog = builder.create();
        return dialog;
    }

}
