package com.example.saggu.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText person_name;
    EditText contact_no;
    DbHendler dbHendler;
    Button searchbtn;
    EditText searchtxt;
    int value1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHendler = new DbHendler(this, null, null, 1);


        person_name = (EditText) findViewById(R.id.person_name);
        contact_no = (EditText) findViewById(R.id.contact_no);
        searchbtn = (Button) findViewById(R.id.searchbtn);
        searchtxt = (EditText) findViewById(R.id.searchtxt);


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.d("Tag ", "emptyyyyyy");
            return;
        }

        if (extras != null) {
             value1 = extras.getInt("ID");
            Log.d("dfsdfdsf", " " + value1);

            edit();
        }


    }

    //add product to a database
    public void addButtonClicked(View view) {
        String name = person_name.getText().toString().trim();
        String no = contact_no.getText().toString().trim();
        if (name.equals("") || no.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }
        dbHendler.addPerson(new PersonInfo(name, no));
        person_name.setText("");
        contact_no.setText("");


        Toast.makeText(getApplicationContext(), name + " Saved", Toast.LENGTH_LONG).show();


        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<PersonInfo> contacts = dbHendler.getAllContacts();

        for (PersonInfo cn : contacts) {
            String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
    }


    public void viewAll(View view) {
        Intent i = new Intent(this, ViewAll.class);
        startActivity(i);
    }

    public void edit() {
        int id = value1;

    Log.d("Tag", ""+id);
     PersonInfo personInfo = dbHendler.getInfo(id);
        String name = personInfo.getName().toString().trim();
        String no = personInfo.getPhoneNumber().toString().trim();
        Toast.makeText(getApplicationContext(), "Edit selcected For " + name + " and " + no, Toast.LENGTH_LONG).show();
        person_name.setText(name);
        contact_no.setText(no);

    }


    public void searchByID(View view) {

        String text = searchtxt.getText().toString();
        int id = Integer.parseInt(text);


        PersonInfo personInfo = dbHendler.getInfo(id);
        String name = personInfo.getName().toString().trim();
        String no = personInfo.getPhoneNumber().toString().trim();
        person_name.setText(name);
        contact_no.setText(no);

    }


}