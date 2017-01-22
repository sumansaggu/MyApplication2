package com.example.saggu.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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

public class AddEditActivity extends AppCompatActivity implements View.OnClickListener {

    EditText person_name;
    EditText contact_no;
    EditText cust_no;
    EditText monthly_fees;
    TextView balance_;
    DbHendler dbHendler;
    Button changeButton, buttonAdd, viewAll, buttonScanSn, buttonScanVC, buttonViewSTBs;

    int extra;
    int extra2;
    String TAG = "MyApp_MainActivity";
    private Toolbar toolbar;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";


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
        changeButton = (Button) findViewById(R.id.changeButton);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonScanSn= (Button) findViewById(R.id.btnScanSN);
        buttonScanVC= (Button) findViewById(R.id.btnScanVC);
        buttonViewSTBs= (Button) findViewById(R.id.btn_stb_record);
        buttonViewSTBs.setOnClickListener(this);
        buttonScanSn.setOnClickListener(this);
        buttonScanVC.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        viewAll = (Button) findViewById(R.id.buttonViewAll);
        viewAll.setOnClickListener(this);
        cust_no = (EditText) findViewById(R.id.cust_no);
        monthly_fees = (EditText) findViewById(R.id.fees);
        balance_ = (EditText) findViewById(R.id.balance);
        getMonth();


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
                buttonAdd.setVisibility(View.INVISIBLE);
                changeButton.setVisibility(View.VISIBLE);
                getIntent().removeExtra("ID");
                edit();
            }
            if (extra2 == R.id.add_stb) {
                Log.d(TAG, " " + extra2);
                viewAll.setVisibility(View.INVISIBLE);
                cust_no.setVisibility(View.INVISIBLE);
                monthly_fees.setVisibility(View.INVISIBLE);
                balance_.setVisibility(View.INVISIBLE);
                buttonScanSn.setVisibility(View.VISIBLE);
                buttonScanVC.setVisibility(View.VISIBLE);
                buttonViewSTBs.setVisibility(View.VISIBLE);
                contact_no.setInputType(InputType.TYPE_CLASS_TEXT);
                buttonAdd.setId(R.id.add_stb);
                person_name.setHint("Serial No");
                contact_no.setHint("VC No");


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
        int custNo = Integer.parseInt(Custno);

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
    public void edit() {
        int id = extra;
        PersonInfo personInfo = dbHendler.getInfo(id);
        String name = personInfo.getName().toString().trim();
        String no = personInfo.getPhoneNumber().toString().trim();
        int custNo = personInfo.get_cust_no();
        String cUSTnO = Integer.toString(custNo);
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
    public void update(View view) {
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
        int custNo = Integer.parseInt(Custno);

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
        changeButton.setEnabled(false);
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
        if (v.getId() == R.id.buttonAdd) {
            addButtonClicked();
        }
        if (v.getId() == R.id.add_stb) {
            Log.d(TAG, "dfsdfsdfds");
            addNewStb();
        }
        if (v.getId() == R.id.btnScanSN) {
            Log.d(TAG, "seriea scan");
            int id =R.id.btnScanSN;
            scanBar(id);
        }
        if (v.getId() == R.id.btnScanVC) {
            Log.d(TAG, "vc scan");
            int id =R.id.btnScanVC;
            scanBar(id);
        }if (v.getId()==R.id.btn_stb_record){
            Intent i = new Intent(this, STBRecord.class);
            startActivity(i);

        }else {

        }
    }

    public void addNewStb() {
        String sn = person_name.getText().toString().trim();
        if (sn.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the Name", Toast.LENGTH_LONG).show();
            return;
        }
        String vc = contact_no.getText().toString().trim();
        if (vc.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter the Contact No.", Toast.LENGTH_LONG).show();
            return;
        }
        dbHendler.AddNewStb(sn, vc);
        person_name.setText("");
        contact_no.setText("");
    }

    //region For scanning bar code

    int buttonID;

    public void scanBar(int id) {
        buttonID =id;

        Log.d(TAG,""+id);
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(AddEditActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
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
                if(buttonID== R.id.btnScanSN) {
                    String contents = intent.getStringExtra("SCAN_RESULT");
                    String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                  //  Toast toast = Toast.makeText(this, "Content SN:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                 //   toast.show();
                    person_name.setText(contents);
                }if(buttonID==R.id.btnScanVC){
                    String contents = intent.getStringExtra("SCAN_RESULT");
                    String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                    Toast toast = Toast.makeText(this, "Content VC:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                    toast.show();
                    contact_no.setText(contents);

                }
            }
        }
    }
    //endregion
}