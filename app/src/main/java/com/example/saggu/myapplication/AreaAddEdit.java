package com.example.saggu.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AreaAddEdit extends AppCompatActivity implements View.OnClickListener {
String TAG= "AREA_ADD_EDIT";
    EditText AreaName, AreaNo;
    Button AreaAddEdit;
    String tag="AreaAddEdit";
    DbHendler dbHendler;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_add_edit);
        dbHendler = new DbHendler(this, null, null, 1);
        AreaName = (EditText) findViewById(R.id.editTextAreaName);
        AreaNo = (EditText) findViewById(R.id.editTextAreaNo);
        AreaAddEdit = (Button) findViewById(R.id.btnAreaAddEdit);
        AreaAddEdit.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Area");

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        if (bundle != null) {

            id = bundle.getInt(AreaList.bundle_stbid);

            Log.d(TAG, "bundle" + id);

            if (id > 0) {
                // buttonAdd.setVisibility(View.INVISIBLE);
                AreaAddEdit.setText("Change");
                //changeButton.setVisibility(View.VISIBLE);
                getIntent().removeExtra("ID");
        //        editArea();
            } else return;
        }
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnAreaAddEdit) {
            String name = AreaName.getText().toString().trim();
            if (name.equals("")) {
                Toast.makeText(this, "Enter Area name", Toast.LENGTH_SHORT).show();
                return;
            }
            String no = AreaNo.getText().toString().trim();
            int areano = Integer.parseInt(no);
            if (no.equals("")) {
                Toast.makeText(this, "Enter Area No.", Toast.LENGTH_SHORT).show();
                return;
            }
            dbHendler.AddArea(areano, name);
            AreaNo.setText("");
            AreaName.setText("");


        }

    }
}
