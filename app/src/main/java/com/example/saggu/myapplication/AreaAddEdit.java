package com.example.saggu.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AreaAddEdit extends AppCompatActivity implements View.OnClickListener {
    String TAG = "AREA_ADD_EDIT";
    EditText AreaName, AreaNo;
    Button AreaAddEdit;
    String tag = "AreaAddEdit";
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
                AreaAddEdit.setText("CHANGE");
                //changeButton.setVisibility(View.VISIBLE);
                getIntent().removeExtra("ID");
                editArea(id);
            } else return;
        }
    }

    private void editArea(int id) {
        Area area = dbHendler.getArea(id);
        int areaID = area.get_id();
        String areaNo = Integer.toString(area.get_areaNo());
        String areaName = area.get_areaName();

        AreaNo.setText(areaNo);
        AreaName.setText(areaName);


    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: called "+v.getId() + AreaAddEdit.getText());

        if (v.getId() == R.id.btnAreaAddEdit && AreaAddEdit.getText().equals("ADD")) {
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


        }if (v.getId() == R.id.btnAreaAddEdit && AreaAddEdit.getText().equals("CHANGE")){
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
          String msg=  dbHendler.changeArea(new Area(id,areano,name));
            AreaNo.setText("");
            AreaName.setText("");
            Toast.makeText(this, "Area Changed to "+ msg, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.area_add_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (id == R.id.add_area) {
            Log.d(TAG, "onOptionsItemSelected: ");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
