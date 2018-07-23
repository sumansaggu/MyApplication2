package com.example.saggu.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class DetailFeesActivity extends AppCompatActivity implements View.OnClickListener {
    SimpleCursorAdapter simpleCursorAdapter;
    DbHendler dbHendler;
    ListView listViewFees;
    String TAG = "MyApp_ViewFees";
    TextView textview;
    int id;
    TextView custname, startDate;
    ImageButton sms, whatsApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fees_detail);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("ID");

        dbHendler = new DbHendler(this, null, null, 1);
        listViewFees = (ListView) findViewById(R.id.fees_list);
        custname = (TextView) findViewById(R.id.cust_name_in_dialog);
        startDate = (TextView) findViewById(R.id.strt_date_in_dailog);
        sms = (ImageButton) findViewById(R.id.sendSms);
        whatsApp = (ImageButton) findViewById(R.id.whatsApp);
        sms.setOnClickListener(this);
        whatsApp.setOnClickListener(this);

        PersonInfo info = dbHendler.getCustInfo(id);
        String name = info.getName().toString().trim();
        // String startdat= String.valueOf(info.get_startdate().toString());
        String startdat = info.get_startdate().toString().trim();
        //    getDialog().setTitle(name);
        custname.setText(name);
        startDate.setText(startdat);

        displayFeeDeatail();
    }

    public void displayFeeDeatail() {
        try {

            Cursor cursor = dbHendler.getFeesToList(id);
            if (cursor == null) {
                textview.setText("Unable to generate cursor.");
                return;
            }

            String[] columns = new String[]{

                    //  DbHendler.KEY_NAME,
                    //  DbHendler.KEY_PHONE_NO,

                    DbHendler.KEY_RECIEPT,
                    DbHendler.KEY_DATE,
                    DbHendler.KEY_REMARK
            };
            int[] boundTo = new int[]{

                    R.id.feesInList,
                    R.id.dateInList,
                    R.id.remarkInList
            };
            simpleCursorAdapter = new SimpleCursorAdapter(this,
                    R.layout.layout_fees_singleitem,
                    cursor,
                    columns,
                    boundTo,
                    0);
            listViewFees.setAdapter(simpleCursorAdapter);

        } catch (Exception ex) {
            Log.d(TAG, "error");
            //  textview.setText("There was an error!");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sendSms) {
            PersonInfo info = dbHendler.getCustInfo(id);
            String name = info.getName();
            String mobNo = info.getPhoneNumber();

            int balance = info.get_balance();
            String y = "";
            List<Fees> detail = dbHendler.getFeesForMsg(id);

            for (Fees fees : detail) {

                String x = "Rs." + fees.getFees() + " " + fees.getDate() + ", ";
                //    Log.d("single entry ", x);
                y = y.concat(x);
            }
            String z = "Payment due Rs. " + balance + ", " + y;
            sendSMS(z, mobNo);
            //  send(y, mobNo);

            Toast.makeText(this, "clicked" + v, Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.whatsApp) {
            Toast.makeText(this, "clicked" + v, Toast.LENGTH_SHORT).show();
        }

    }

    public void sendSMS(String detail, String mobNo) {
        Uri uri = Uri.parse("smsto:" + "(+91)" + mobNo);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", detail);
        startActivity(it);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "on activity result", Toast.LENGTH_SHORT).show();


    }


}
