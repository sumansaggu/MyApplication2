package com.example.saggu.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText person_name;
    EditText contact_no;
    EditText cust_no;
    EditText monthly_fees;
    TextView balance_;
    DbHendler dbHendler;
    Button changeButton;
    Button buttonAdd;
    int rowId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHendler = new DbHendler(this, null, null, 1);
        person_name = (EditText) findViewById(R.id.person_name);
        contact_no = (EditText) findViewById(R.id.contact_no);
        changeButton = (Button) findViewById(R.id.changeButton);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        cust_no = (EditText) findViewById(R.id.cust_no);
        monthly_fees = (EditText) findViewById(R.id.fees);
        balance_ = (TextView) findViewById(R.id.balance);


        Bundle extras = getIntent().getExtras(); //getting the intent from other activity
       /*checking if bundle
       object have data
        or not*/
        if (extras == null) {
            return;
        }

        if (extras != null) {
            rowId = extras.getInt("ID");
            if (rowId > 0) {
                buttonAdd.setVisibility(View.INVISIBLE);
                changeButton.setVisibility(View.VISIBLE);
                getIntent().removeExtra("ID");
                edit();
            } else return;
        }
    }

    //add product to a database
    public void addButtonClicked(View view) {

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
        }        int custNo = Integer.parseInt(Custno);

        String Fees = monthly_fees.getText().toString().trim();
        if (Fees.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the monthly fees", Toast.LENGTH_LONG).show();
            return;
        }        int fees = Integer.parseInt(Fees);

        String Balance = balance_.getText().toString().trim();
        if (Balance.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the balance", Toast.LENGTH_LONG).show();
            return;
        }        int balance = Integer.parseInt(Balance);


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
                    + " Customer " + info.get_cust_no() + " Fees " + info.get_fees();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
    }


    public void viewAll(View view) {
        Intent i = new Intent(this, ViewAll.class);
        startActivity(i);
    }

    public void edit() {
        int id = rowId;

        Log.d("Tagggggggggg", "" + id);
        PersonInfo personInfo = dbHendler.getInfo(id);
        String name = personInfo.getName().toString().trim();
        String no = personInfo.getPhoneNumber().toString().trim();
        Toast.makeText(getApplicationContext(), "Edit selcected For " + name + " and " + no, Toast.LENGTH_LONG).show();
        person_name.setText(name);
        contact_no.setText(no);

    }

    public void update(View view) {
        int id = rowId;
        String name = person_name.getText().toString().trim();
        String no = contact_no.getText().toString().trim();
        dbHendler.updateInfo(new PersonInfo(id, name, no));
        person_name.setText("");
        contact_no.setText("");
        changeButton.setEnabled(false);

    }


}