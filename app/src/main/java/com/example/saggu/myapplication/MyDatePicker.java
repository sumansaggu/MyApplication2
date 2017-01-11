package com.example.saggu.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Saggu on 1/6/2017.
 */

public class MyDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    Communicator comm;
    int id;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle =getArguments();
        if (bundle !=null){
             id = bundle.getInt("id");
            Log.d("tag",""+id);
        }

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comm = (Communicator) getActivity();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(id==1){

            int month = monthOfYear + 1;
            String fm = "" + month;
            String fd = "" + dayOfMonth;
            if (month < 10) {
                fm = "0" + month;
            }
            if (dayOfMonth < 10) {
                fd = "0" + dayOfMonth;
            }
            String date = "" + year + "-" + fm + "-" + fd;
            Log.d("tag", "" + date);
            comm.respond(date);

        }else {

            int month = monthOfYear + 1;
            String fm = "" + month;
            String fd = "" + dayOfMonth;
            if (month < 10) {
                fm = "0" + month;
            }
            if (dayOfMonth < 10) {
                fd = "0" + dayOfMonth;
            }
            String date = "" + year + "-" + fm + "-" + fd;
            Log.d("tag", "" + date);
            comm.respond2(date);


        }





    }
}
