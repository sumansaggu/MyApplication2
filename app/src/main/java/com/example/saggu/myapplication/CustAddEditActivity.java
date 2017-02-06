package com.example.saggu.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class CustAddEditActivity extends AppCompatActivity implements View.OnClickListener {

    EditText person_name;
    EditText contact_no;
    EditText cust_no;
    EditText monthly_fees;
    TextView balance_;
    DbHendler dbHendler;
    Button stbButton, buttonAdd, viewAll;

    int extra;
    int extra2;
    String TAG = "MyApp_MainActivity";
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_activity);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //region Description
        dbHendler = new DbHendler(this, null, null, 1);
        person_name = (EditText) findViewById(R.id.person_name);
        contact_no = (EditText) findViewById(R.id.contact_no);
       stbButton = (Button) findViewById(R.id.stbButton);
        stbButton.setOnClickListener(this);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);
        viewAll = (Button) findViewById(R.id.buttonViewAll);
        viewAll.setOnClickListener(this);
        cust_no = (EditText) findViewById(R.id.cust_no);
        monthly_fees = (EditText) findViewById(R.id.fees);
        balance_ = (EditText) findViewById(R.id.balance);
       Log.d(TAG,"jfghjgfhjg"+buttonAdd.getText()) ;

        Bundle extras = getIntent().getExtras(); //getting the intent from other activity
       /*checking if bundle
       object have data
        or not*/
        if (extras == null) {
            return;
        }
        if (extras != null) {

            extra = extras.getInt("ID");
            extra2 = extras.getInt("addstb");
            Log.d(TAG, "extra " + extra);

            if (extra > 0) {
                // buttonAdd.setVisibility(View.INVISIBLE);
                buttonAdd.setText("Change");
                //changeButton.setVisibility(View.VISIBLE);
                getIntent().removeExtra("ID");
                editCustomer();
            } else return;
        }
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
        //endregion
        dbHendler.addPerson(new PersonInfo(name, no, custNo, fees, balance));
        person_name.setText("");
        contact_no.setText("");
        cust_no.setText("");
        monthly_fees.setText("");
        balance_.setText("");
        Toast.makeText(getApplicationContext(), name + " Saved", Toast.LENGTH_LONG).show();
        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<PersonInfo> personInfos = dbHendler.getAllContacts();

        for (PersonInfo info : personInfos) {
            String log = "Id: " + info.getID() + " ,Name: " + info.getName() + " ,Phone: " + info.getPhoneNumber()
                    + " Customer: " + info.get_cust_no() + " Fees: " + info.get_fees();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
    }

    //call to listview
    public void viewAll() {
        Intent i = new Intent(this, ViewAll.class);
        startActivity(i);
    }

    // to edit an item
    public void editCustomer() {
        int id = extra;
        PersonInfo personInfo = dbHendler.getCustInfo(id);
        String name = personInfo.getName().toString().trim();
        String no = personInfo.getPhoneNumber().toString().trim();
        float custNo = personInfo.get_cust_no();
        String cUSTnO = String.valueOf(custNo);
        int fees = personInfo.get_fees();
        String fEES = Integer.toString(fees);
        int balance = personInfo.get_balance();
        String bALANCE = Integer.toString(balance);
        Toast.makeText(getApplicationContext(), "Edit selcected For " + name + " and " + no, Toast.LENGTH_LONG).show();
        person_name.setText(name);
        contact_no.setText(no);
        cust_no.setText(cUSTnO);
        monthly_fees.setText(fEES);
        balance_.setText(bALANCE);
    }

    // to update an item
    public void update() {
        int id = extra;
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


        dbHendler.updateInfo(new PersonInfo(id, name, no, custNo, fees, balance));
        person_name.setText("");
        contact_no.setText("");
        cust_no.setText("");
        monthly_fees.setText("");
        balance_.setText("");
        //changeButton.setEnabled(false);
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
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonViewAll) {
            Intent i = new Intent(this, ViewAll.class);
            startActivity(i);
        }
        if (v.getId() == R.id.buttonAdd && buttonAdd.getText().equals("Add")) {
            addButtonClicked();

        }
        if (v.getId() == R.id.buttonAdd && buttonAdd.getText().equals("Change")) {
            update();
            Log.d(TAG, "change clicked");

        }if (v.getId()==R.id.stbButton){
            android.app.FragmentManager manager= getFragmentManager();
            Bundle bundle = new Bundle();
            DialogSTB dialogSTB = new DialogSTB();
            dialogSTB.show(manager,"DialogSTB");

        }else {

        }
    }


}