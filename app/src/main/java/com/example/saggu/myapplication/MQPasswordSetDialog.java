package com.example.saggu.myapplication;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Saggu on 5/4/2017.
 */

public class MQPasswordSetDialog extends DialogFragment implements View.OnClickListener {

    EditText id1, pw1, id2,pw2;
    String TAG = "PASSWORD", ID1, PW1, ID2,PW2;
    Button ok, cancel;
    String MQID1, MQPASS1, MQID2, MQPASS2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_mq_password, null);

        id1 = (EditText) view.findViewById(R.id.id1);
        pw1 = (EditText) view.findViewById(R.id.pw1);
        id2 = (EditText) view.findViewById(R.id.id2);
        pw2 = (EditText) view.findViewById(R.id.pw2);
        ok = (Button) view.findViewById(R.id.setpwbtn);
        ok.setOnClickListener(this);
        cancel = (Button) view.findViewById(R.id.cancelpwbtn);
        cancel.setOnClickListener(this);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.setpwbtn) {
            ID1 = id1.getText().toString().trim();
            PW1 = pw1.getText().toString().trim();
            ID2 = id2.getText().toString().trim();
            PW2 = pw2.getText().toString().trim();
            Log.d(TAG, "onClick: " + ID1 + " " + PW1 + " " + ID2+ " "+PW2);

           saveMQsharedpref();
        }
        if (v.getId() == R.id.cancelpwbtn) {
            dismiss();

        }
    }



    public void saveMQsharedpref() {
        Log.d(TAG, "saveMQsharedpref: ");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MQID1", ID1);
        editor.putString("MQPASS1", PW1);
        editor.putString("MQID2", ID2);
        editor.putString("MQPASS2", PW2);
        editor.commit();
        dismiss();
    }

    public void loadShardPref() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("pass", "N/A");
        Log.d(TAG, "Old password: " + data);


    }


}