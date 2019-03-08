package com.example.saggu.myapplication;

import android.app.DialogFragment;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BtwTwoDates extends AppCompatActivity implements Communicator {

    EditText dateFrom, dateTo;
    TextView from_to_textview;
    Calendar calendar;
    DbHendler dbHendler;
    String tag = "BtwTwoDates";
    String newDate = "";
    ListView listView;
    SimpleCursorAdapter simpleCursorAdapter;
    String TAG = "btwTwoDates";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btw_two_dates);
        dbHendler = new DbHendler(this, null, null, 1);
        dateFrom = (EditText) findViewById(R.id.date_from);
        dateTo = (EditText) findViewById(R.id.date_to);
        from_to_textview = (TextView) findViewById(R.id.from_to_textview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Collection Between Two Dates");
        getDate();
        dateFrom.clearFocus();
        dateTo.clearFocus();
        listView = findViewById(R.id.listViewBtwTwoDates);
        listforBtwDates();

        dateFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pickDateFrom();
                    findViewById(R.id.activity_btw_two_dates).requestFocus();//only to clear focus from edti text
                    //  dateFrom.clearFocus();
                }
            }
        });
        dateTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pickDateTo();
                    findViewById(R.id.activity_btw_two_dates).requestFocus();
                    // dateTo.clearFocus();
                }
            }
        });
    }


    public void getCollection() {
        String from, to;
        from = dateFrom.getText().toString();
        to = dateTo.getText().toString();
        int total = dbHendler.colectionBwtwoDates(from, to);
        from_to_textview.setText("" + total);
        listforBtwDates();

    }


    public void listforBtwDates() {
        String from, to;
        from = dateFrom.getText().toString();
        to = dateTo.getText().toString();
        try {

            Cursor cursor = dbHendler.listforBtwtwoDates(from, to);
            if (cursor == null) {
                Log.d(TAG, "listforBtwDates: cursor is null");
                return;
            }

            String[] columns = new String[]{

                    //  DbHendler.KEY_NAME,
                    //  DbHendler.KEY_PHONE_NO,

                    DbHendler.KEY_NAME,
                    DbHendler.KEY_DATE,
                    DbHendler.KEY_RECIEPT,
                    //  "lBalance",
                    "curBalance",
                    DbHendler.KEY_REMARK
            };
            int[] boundTo = new int[]{
                    R.id.btwTwoDatesName,
                    R.id.btwTwoDatesDate,
                    R.id.btwTwodatesAmount,
                    R.id.btwTwoDatesBalance
                    //R.id.remarkInList
            };
            simpleCursorAdapter = new SimpleCursorAdapter(this,
                    R.layout.list_item_btw_two_dates,
                    cursor,
                    columns,
                    boundTo,
                    0);
            listView.setAdapter(simpleCursorAdapter);

        } catch (Exception ex) {
            Log.d(TAG, "displayFeeDeatail: " + ex.toString());
            //  textview.setText("There was an error!");
        }
    }


    public String getDate() {
        calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(calendar.getTime());
        dateFrom.setText(formattedDate);
        dateTo.setText(formattedDate);
        return formattedDate;

    }

    public void pickDateFrom() {
        int idFrom = 1;
        Log.d(tag, "date change called");
        DialogFragment datePicker1 = new MyDatePicker();
        Bundle bundle = new Bundle();
        datePicker1.show(getFragmentManager(), "datepicker");
        datePicker1.setArguments(bundle);
        bundle.putInt("id", idFrom);


    }

    public void pickDateTo() {
        int idTo = 2;
        Log.d(tag, "date change called");
        DialogFragment datePicker1 = new MyDatePicker();
        Bundle bundle = new Bundle();
        datePicker1.show(getFragmentManager(), "datepicker");
        datePicker1.setArguments(bundle);
        bundle.putInt("id", idTo);
    }

    @Override
    public void respond(String date) {
        dateFrom.setText(date);
        getCollection();

    }

    @Override
    public void respond2(String date2) {
        dateTo.setText(date2);
        getCollection();
    }


}
