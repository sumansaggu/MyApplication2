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

public class PasswordSetDialog extends DialogFragment implements View.OnClickListener {

    EditText oldpw, newpw, confirmpw;
    String TAG = "PASSWORD", oldp , newp , confirmp ;
    Button ok, cancel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_password, null);
        oldpw = (EditText) view.findViewById(R.id.oldpw);
        newpw = (EditText) view.findViewById(R.id.newpw);
        confirmpw = (EditText) view.findViewById(R.id.confirmpw);
        ok = (Button) view.findViewById(R.id.setpwbtn);
        ok.setOnClickListener(this);
        cancel = (Button) view.findViewById(R.id.cancelpwbtn);
        cancel.setOnClickListener(this);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadShardPref();
        return view;
    }

    @Override
    public void onClick(View v) {
          loadShardPref();
        if (v.getId() == R.id.setpwbtn) {
            oldp = oldpw.getText().toString().trim();
            newp = newpw.getText().toString().trim();
            confirmp = confirmpw.getText().toString().trim();
            Log.d(TAG, "onClick: " + oldp + " " + newp + " " + confirmp);

            if (!oldp.equals(oldp)) {
                Toast.makeText(getActivity(), "Enter Correct Old Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newp.equals("") || confirmp.equals("")) {
                Toast.makeText(getActivity(), "Password is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newp.equals(confirmp)) {
                Toast.makeText(getActivity(), "New Password does not match", Toast.LENGTH_SHORT).show();
                return;
            } else {

                savesharedpref();

            }
        }
        if (v.getId() == R.id.cancelpwbtn) {
            dismiss();

        }
    }

    public void savesharedpref() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pass", newp);
        editor.commit();
        Toast.makeText(getActivity(), "Password Changed "+newp, Toast.LENGTH_SHORT).show();
        dismiss();
    }

    public void loadShardPref() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("pass","N/A");
        Log.d(TAG, "Old password: "+data);
        oldp = data;

    }


}