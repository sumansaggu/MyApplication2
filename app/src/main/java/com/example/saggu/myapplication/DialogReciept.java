package com.example.saggu.myapplication;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Saggu on 12/16/2016.
 */

public class DialogReciept extends DialogFragment implements View.OnClickListener{
    String TAG = "MyApp_DialogBox";

    DbHendler dbHendler;
    Calendar calendar;
    TextView title_dialog;
    TextView fees_dailog;
    TextView balance_dialog;
    EditText reciept_dialog;
    TextView date;
    EditText remark;
    Button Ok, Cancel;


    int id;


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
        date = (TextView) view.findViewById(R.id.date);
        date.setOnClickListener(this);

        remark = (EditText) view.findViewById(R.id.remarksEditText);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = getArguments();
        id = bundle.getInt("ID");
        //  setCancelable(false); //preventing from cancel when clicking on background
        dbHendler = new DbHendler(getActivity(), null, null, 1);
        getinformation();
        getDate();

        return view;
    }



    public void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) getView().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonYes) {
            updateReciept();
            getinformation();
            viewfeestable();


        }
        if (v.getId() == R.id.date) {
            hideKeyboard();
            pickDate();

        }
        if (v.getId() == R.id.buttonNo) {
            dismiss();
        }


    }


    public void getinformation() {
        PersonInfo personInfo = dbHendler.getCustInfo(id);
        String name = personInfo.getName().toString().trim();
        title_dialog.setText(name);
        int fees = personInfo.get_fees();
        String fEES = Integer.toString(fees);
        int balance = personInfo.get_balance();
        String bALANCE = Integer.toString(balance);
        fees_dailog.setText(fEES);
        balance_dialog.setText(bALANCE);
        //getDialog().setTitle(name);
    }


    public void updateReciept() {
        PersonInfo personInfo = dbHendler.getCustInfo(id);
        int balance = personInfo.get_balance();
        Log.d(TAG, "" + balance);

        String Reciept = reciept_dialog.getText().toString().trim();
        if (Reciept.equals("")) {
            Toast.makeText(this.getActivity(), "Enter the amount", Toast.LENGTH_SHORT).show();
            return;
        } else {
            int id = this.id;
            int reciept = Integer.parseInt(Reciept);
            String datefromEditText = date.getText().toString().trim();
            Log.d(TAG, "" + (balance - reciept));
            int newbalance = balance - reciept;
            String name = personInfo.getName();
            String remark = this.remark.getText().toString();

            dbHendler.updateBalance(new PersonInfo(id, newbalance));  //new balance to customer table
            dbHendler.addFees(new Fees(id, reciept, datefromEditText, remark));//fees recieved and date to fees table

            ViewAll activity = (ViewAll) getActivity();
            activity.refreshListView();
            Toast.makeText(this.getActivity(), "Added Rs. " + reciept + " to " + name, Toast.LENGTH_SHORT).show();
            reciept_dialog.setText("");
            Ok.setEnabled(false);
            Cancel.setText("Back");
            getinformation();
        }

    }


    public void viewfeestable() {
        List<Fees> fees = dbHendler.viewFees();
        for (Fees fees1 : fees) {
            String log = "No: " + fees1.getNo() + " Id: " + fees1.getId()
                    + " Fees: " + fees1.getFees() + " Date: " + fees1.getDate();
            Log.d(TAG, log);
        }
    }

    public String getDate() {
        calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(calendar.getTime());
        date.setText(formattedDate);
        return formattedDate;

    }

    public void pickDate() {
        Log.d(TAG, "pick date called");
        DialogFragment newFragment = new MyDatePicker();
        newFragment.show(getFragmentManager(), "datepicker");
        Bundle bundle = new Bundle();
        bundle.putInt("id", 1);
        newFragment.setArguments(bundle);


    }

    public void changeText(String data) {
        date.setText(data);
    }


}
