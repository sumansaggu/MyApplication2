package com.example.saggu.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText person_name;
    EditText contact_no;
    DbHendler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         db = new DbHendler(this, null, null, 1);


        person_name =(EditText)findViewById(R.id.person_name);
        contact_no = (EditText)findViewById(R.id.contact_no);
    }

    //add product to a database
      public void addButtonClicked(View view) {

           String name = person_name.getText().toString().trim();
           String no = contact_no.getText().toString().trim();
          if(name.equals("") || no.equals("")){
              Toast.makeText(getApplicationContext(),"Please fill all fields", Toast.LENGTH_LONG).show();
              return;
          }
           db.addPerson(new PersonInfo(name, no));   person_name.setText(""); contact_no.setText("");


          Toast.makeText(getApplicationContext(), name + " Saved",Toast.LENGTH_LONG).show();


          // Reading all contacts
          Log.d("Reading: ", "Reading all contacts..");
          List<PersonInfo> contacts = db.getAllContacts();

          for (PersonInfo cn : contacts) {
              String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
              // Writing Contacts to log
              Log.d("Name: ", log);
          }
      }
    // delete record
    public void deleteButtonClicked(View view){

        String name = person_name.getText().toString();
        String no = contact_no.getText().toString();
      //  db.deletePerson(name, no); person_name.setText(""); contact_no.setText("");
        Toast.makeText(getApplicationContext(), name + " Deleted " ,Toast.LENGTH_LONG).show();
        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<PersonInfo> contacts = db.getAllContacts();
        for (PersonInfo cn : contacts) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
    }
      public void viewAll(View view){
          Intent i = new Intent(this, ViewAll.class);
          startActivity(i);
      }
}