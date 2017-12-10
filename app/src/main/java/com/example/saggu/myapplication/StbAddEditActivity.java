package com.example.saggu.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class StbAddEditActivity extends AppCompatActivity implements View.OnClickListener {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    //   static final String SCANFORMATS="DFSDFS";
    String TAG = "STB ADD EDIT";
    DbHendler dbHendler;
    Button buttonScanSn, buttonScanVC, buttonAddSTB, buttonViewSTBs;
    EditText stbSN, stbVC;
    String stbStatus;
    int extra;
    Toolbar toolbar;
    Switch statusSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stb_add_edit);
        dbHendler = new DbHendler(this, null, null, 1);
        stbSN = (EditText) findViewById(R.id.stbSN_editText);
        stbVC = (EditText) findViewById(R.id.stbVC_editText);
        buttonScanSn = (Button) findViewById(R.id.btnScanSN);
        buttonScanVC = (Button) findViewById(R.id.btnScanVC);
        buttonAddSTB = (Button) findViewById(R.id.add_stb);
        buttonAddSTB.setOnClickListener(this);
        buttonScanSn.setOnClickListener(this);
        buttonScanVC.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        statusSwitch = (Switch) findViewById(R.id.status_switch);
        //region status switch listner
        statusSwitch.setChecked(true);

        statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stbStatus = "ON";

                } else {
                    stbStatus = "OFF";

                }
            }
        });
        //check the current state before we display the screen
        if (statusSwitch.isChecked()) {
            stbStatus = "ON";
        } else {
            stbStatus = "OFF";
        }
        //endregion


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage");

        Bundle extras = getIntent().getExtras(); //getting the intent from other activity
       /*checking if bundle
       object have data
        or not*/
        if (extras == null) {
            return;
        }
        if (extras != null) {
            extra = extras.getInt("ID");
            Log.d(TAG, "extra " + extra);

            if (extra > 0) {
                buttonAddSTB.setText("Change");
                getIntent().removeExtra("ID");
                editSTB();
            } else return;
        }
    }

    public void addNewStb() {
        String sn = stbSN.getText().toString().trim();
        if (sn.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the Serial Number", Toast.LENGTH_LONG).show();
            return;
        }
        String vc = stbVC.getText().toString().trim();
        if (vc.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the VC/MAC Number.", Toast.LENGTH_LONG).show();
            return;
        }
        dbHendler.AddNewStb(new STB(sn, vc, stbStatus));
        Toast.makeText(getApplicationContext(), "STB ADDED SUCCESSFULY", Toast.LENGTH_LONG).show();
        stbSN.setText("");
        stbVC.setText("");
    }


    //region For scanning bar code

    int buttonID;

    public void scanBar(int id) {
        buttonID = id;
        try {
            Intent intent = new Intent(ACTION_SCAN);
            //  intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(StbAddEditActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if (buttonID == R.id.btnScanSN) {
                    String contents = intent.getStringExtra("SCAN_RESULT");
                    String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                    stbSN.setText(contents);
                }
                if (buttonID == R.id.btnScanVC) {
                    String contents = intent.getStringExtra("SCAN_RESULT");
                    String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                    Toast toast = Toast.makeText(this, "Content VC:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                    toast.show();
                    stbVC.setText(contents);

                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_stb && buttonAddSTB.getText().equals("ADD")) {
            Log.d(TAG,"addclicked");
            addNewStb();
        }
        if (v.getId() == R.id.add_stb && buttonAddSTB.getText().equals("Change")) {
            Log.d(TAG,"change clicked");
            update();

        }

        if (v.getId() == R.id.btnScanSN) {

            int id = R.id.btnScanSN;
            scanBar(id);
        }
        if (v.getId() == R.id.btnScanVC) {

            int id = R.id.btnScanVC;
            scanBar(id);


        } else {

        }
    }
    //endregion

    public void editSTB() {
        int id = extra;
        Log.d(TAG, "" + id);
        String status="";
        STB stb = dbHendler.getSTBInfo(id);
        String sn = String.valueOf(stb.getSerialNo());   // valueOf is used instead of toString methhod it is recomended
        String vc = String.valueOf(stb.getVcNo());
        status = String.valueOf(stb.getStatus());
        stbSN.setText(sn);
        stbVC.setText(vc);
        if (status.equals("ON")) {
            statusSwitch.setChecked(true);
        }
        if (status.equals("OFF")) {
            statusSwitch.setChecked(false);
        }
    }


    // to update an item
    public void update() {
        int id = extra;
        String serialNo = stbSN.getText().toString().trim();
        if (serialNo.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Serial No.", Toast.LENGTH_LONG).show();
            return;
        }
        String vcNo = stbVC.getText().toString().trim();
        if (vcNo.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter VC No.", Toast.LENGTH_LONG).show();
            return;
        }


        dbHendler.updateStbRecord(new STB(id, serialNo, vcNo, stbStatus));
        stbSN.setText("");
        stbVC.setText("");

        buttonAddSTB.setEnabled(false);
        viewStbRecored();

    }

    private void viewStbRecored() {
        Intent i = new Intent(this, STBRecord.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        finish();
       // Intent i = new Intent(this, STBRecord.class);
       // startActivity(i);
    }

}
