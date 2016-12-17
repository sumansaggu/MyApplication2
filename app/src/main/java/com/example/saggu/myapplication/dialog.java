package com.example.saggu.myapplication;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Saggu on 12/16/2016.
 */

public class Dialog extends DialogFragment implements View.OnClickListener {
    Button Ok, Cancel;
    String TAG = "MyApp_DialogBox";
    TextView fees_dailog, balance_dialog;
    EditText reciept_dialog;
    DbHendler dbHendler;
    int id;
    TextView title_dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_layout, null);
        Ok = (Button) view.findViewById(R.id.buttonYes);
        Cancel = (Button) view.findViewById(R.id.buttonNo);
        Ok.setOnClickListener(this);
        Cancel.setOnClickListener(this);
        fees_dailog = (TextView) view.findViewById(R.id.fees_dialog);
        balance_dialog = (TextView) view.findViewById(R.id.balance_dialog);
        reciept_dialog = (EditText) view.findViewById(R.id.reciept_dialog);
        title_dialog = (TextView) view.findViewById(R.id.title_dialog);
        Bundle bundle = getArguments();
        id = bundle.getInt("ID");
        Log.d(TAG,""+id);
        setCancelable(false); //preventing from cancel when clicking on background
        dbHendler = new DbHendler(getActivity(),null,null,1);
        getinformation();
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonYes) {
            Log.d(TAG, "ok button clicked");

        } else {
            Log.d(TAG, "cancel was clicked");
            dismiss();// cancel the dialog
        }
    }

    public void getinformation() {

        PersonInfo personInfo = dbHendler.getInfo(id);
        String name = personInfo.getName().toString().trim();
        title_dialog.setText(name);
        int fees = personInfo.get_fees();
        String fEES = Integer.toString(fees);
        int balance = personInfo.get_balance();
        String bALANCE = Integer.toString(balance);
        fees_dailog.setText(fEES);
        balance_dialog.setText(bALANCE);
    }
}
