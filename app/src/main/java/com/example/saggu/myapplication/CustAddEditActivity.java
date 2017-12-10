package com.example.saggu.myapplication;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import schemasMicrosoftComOfficeOffice.STInsetMode;

import static com.example.saggu.myapplication.R.id.date;

public class CustAddEditActivity extends AppCompatActivity implements Communicator, View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText person_name;
    EditText contact_no;
    EditText cust_no;
    EditText monthly_fees;
    EditText nickName;
    TextView dateTxtview;
    TextView startdate;
    EditText balance_;
    DbHendler dbHendler;
    Button stbButton, buttonAdd, viewAll;
    List<Area> areas;
    List<String> items = new ArrayList<>();
    Calendar calendar;

    int id;
    int extra2;
    String TAG = "MyApp_MainActivity";
    private Toolbar toolbar;
    Spinner spinner;
    int areaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cust_add_edit_activity);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Customer");

        //region Description
        dbHendler = new DbHendler(this, null, null, 1);
        person_name = (EditText) findViewById(R.id.person_name);
        contact_no = (EditText) findViewById(R.id.contact_no);
        //   stbButton = (Button) findViewById(R.id.stbButton);
//        stbButton.setOnClickListener(this);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);
        //  viewAll = (Button) findViewById(R.id.buttonViewAll);
        // viewAll.setOnClickListener(this);
        cust_no = (EditText) findViewById(R.id.cust_no);
        monthly_fees = (EditText) findViewById(R.id.fees);
        balance_ = (EditText) findViewById(R.id.balance);
        nickName = (EditText) findViewById(R.id.etxtnickname);
        dateTxtview = (TextView) findViewById(R.id.datetxtview);
        startdate = (TextView) findViewById(R.id.date_txtview);
        startdate.setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.spinner_area);
        spinner.setOnItemSelectedListener(this);
        getDate();
        loadSpinnerData();


        //region get extra data from bundle
        Bundle extras = getIntent().getExtras(); //getting the intent from other activity
       /*checking if bundle
       object have data
        or not*/
        if (extras == null) {
            return;
        }
        if (extras != null) {

            id = extras.getInt("ID");
            extra2 = extras.getInt("addstb");


            if (id > 0) {
                // buttonAdd.setVisibility(View.INVISIBLE);
                buttonAdd.setText("Change");
                //   stbButton.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle("Edit Customer");
                dateTxtview.setVisibility(View.INVISIBLE);
                startdate.setVisibility(View.INVISIBLE);
                balance_.setEnabled(false);
                //changeButton.setVisibility(View.VISIBLE);
                getIntent().removeExtra("ID");
                editCustomer();
            } else return;
        }
        //endregion
    }

    //add product to a database
    public void addButtonClicked() {
        //region Description
        String name = person_name.getText().toString().trim();
        if (name.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the Name", Toast.LENGTH_LONG).show();
            return;
        }
        String no = contact_no.getText().toString().trim();
        if (no.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the Contact No.", Toast.LENGTH_LONG).show();
            return;
        }
        String nName = nickName.getText().toString().trim();
        if (nName.equals("")) {
            nName = "N/A";
        }
        String Custno = cust_no.getText().toString().trim();
        if (Custno.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the Customer No.", Toast.LENGTH_LONG).show();
            return;
        }
        float custNo = Float.parseFloat(Custno);

        String Fees = monthly_fees.getText().toString().trim();
        if (Fees.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the monthly fees", Toast.LENGTH_LONG).show();
            return;
        }
        int fees = Integer.parseInt(Fees);

        String Balance = balance_.getText().toString().trim();
        if (Balance.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the balance", Toast.LENGTH_LONG).show();
            return;
        }
        int balance = Integer.parseInt(Balance);
        String date = startdate.getText().toString().trim();
        if (startdate.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the Start Date", Toast.LENGTH_LONG).show();
            return;
        }
        //endregion
        dbHendler.addPerson(new PersonInfo(name, no, custNo, fees, balance, areaId, date, nName), this);
        person_name.setText("");
        contact_no.setText("");
        cust_no.setText("");
        monthly_fees.setText("");
        balance_.setText("");
        nickName.setText("");
        //  Toast.makeText(getApplicationContext(), name + " Saved", Toast.LENGTH_LONG).show();


    }


    /**
     * Function to load the spinnerArea data from SQLite database
     */
    private void loadSpinnerData() {

        // Spinner Drop down elements
        areas = dbHendler.getAllAreas();
        for (Area area : areas) {
            String singleitem = area.get_areaName();
            items.add(singleitem);
        }

        // Creating adapter for spinnerArea
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinnerArea
        spinner.setAdapter(dataAdapter);
    }

    //call to listview
    public void viewAll() {
        Intent i = new Intent(this, ViewAll.class);
        startActivity(i);
    }

    // to edit an item
    public void editCustomer() {
        int id = this.id;
        PersonInfo personInfo = dbHendler.getCustInfo(id);
        String name = personInfo.getName().toString().trim();
        String no = personInfo.getPhoneNumber().toString().trim();
        float custNo = personInfo.get_cust_no();
        String cUSTnO = String.valueOf(custNo);
        int fees = personInfo.get_fees();
        String fEES = Integer.toString(fees);
        int balance = personInfo.get_balance();
        String bALANCE = Integer.toString(balance);
        String nName = personInfo.get_nName().toString().trim();
        String startdate = personInfo.get_startdate();
        int areaID = personInfo.get_area();
        String area = dbHendler.getAreaName(areaID);
        person_name.setText(name);
        contact_no.setText(no);
        cust_no.setText(cUSTnO);
        monthly_fees.setText(fEES);
        balance_.setText(bALANCE);
        nickName.setText(nName);
        this.startdate.setText(startdate);
        // retrieving the index of element u
        int retval = items.indexOf(area);

        spinner.setSelection(retval);
    }

    // to update an item
    public void update() {
        int id = this.id;
        String name = person_name.getText().toString().trim();
        if (name.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the Name", Toast.LENGTH_LONG).show();
            return;
        }
        String no = contact_no.getText().toString().trim();
        if (no.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the Contact No.", Toast.LENGTH_LONG).show();
            return;
        }
        String nickname = nickName.getText().toString().trim();
        if (nickname.equals("")) {
            nickname = "N/A";
        }
        String Custno = cust_no.getText().toString().trim();
        if (Custno.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the Customer No.", Toast.LENGTH_LONG).show();
            return;
        }
        float custNo = Float.parseFloat(Custno);

        String Fees = monthly_fees.getText().toString().trim();
        if (Fees.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the monthly fees", Toast.LENGTH_LONG).show();
            return;
        }
        int fees = Integer.parseInt(Fees);

        String Balance = balance_.getText().toString().trim();
        if (Balance.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the balance", Toast.LENGTH_LONG).show();
            return;
        }
        int balance = Integer.parseInt(Balance);
        String date = startdate.getText().toString().trim();
        if (startdate.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the Start Date", Toast.LENGTH_LONG).show();
            return;
        }


        dbHendler.updateInfo(new PersonInfo(id, name, no, custNo, fees, balance, areaId, date, nickname));
        person_name.setText("");
        contact_no.setText("");
        cust_no.setText("");
        monthly_fees.setText("");
        balance_.setText("");
        nickName.setText("");
        //changeButton.setEnabled(false);
        finish();
        viewAll();

    }

    public void getMonth() {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d(TAG, "month is " + month);
    }


    //region OptionMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, "selected", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ViewAll.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.buttonAdd && buttonAdd.getText().equals("Add")) {
            addButtonClicked();

        }
        if (v.getId() == R.id.buttonAdd && buttonAdd.getText().equals("Change")) {
            update();
            Log.d(TAG, "change clicked");

        }

        if (v.getId() == R.id.date_txtview) {
            Toast.makeText(this, "Select the Date", Toast.LENGTH_SHORT).show();
            pickDate();
        } else {

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        String item = parent.getItemAtPosition(position).toString();
        areaId = dbHendler.getAreaID(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

    }


    public void pickDate() {
        //   Log.d(TAG, "date change called");
        DialogFragment newFragment = new MyDatePicker();
        newFragment.show(getFragmentManager(), "datepicker");
        Bundle bundle = new Bundle();
        bundle.putInt("id", 1);
        newFragment.setArguments(bundle);


    }

    public String getDate() {
        calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(calendar.getTime());
        startdate.setText(formattedDate);
        return formattedDate;

    }


    @Override
    public void respond(String date) {
        startdate.setText(date);
    }

    @Override
    public void respond2(String date2) {

    }
}