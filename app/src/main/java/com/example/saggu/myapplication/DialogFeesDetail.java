package com.example.saggu.myapplication;


import android.app.DialogFragment;

import android.database.Cursor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Saggu on 12/20/2016.
 */

public class DialogFeesDetail extends DialogFragment implements View.OnClickListener{
    SimpleCursorAdapter simpleCursorAdapter;
    DbHendler dbHendler;
    ListView listViewFees;
    String TAG = "MyApp_ViewFees";
    TextView textview;
    int id;
    TextView custname, startDate;
    Button sms,whatsApp;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_fees_detail, null);
        dbHendler = new DbHendler(this.getActivity(), null, null, 1);
        listViewFees = (ListView) view.findViewById(R.id.fees_list);
        custname = (TextView) view.findViewById(R.id.cust_name_in_dialog);
        startDate = (TextView) view.findViewById(R.id.strt_date_in_dailog);
      //  sms = (Button) view.findViewById(R.id.sendSms);
     //   whatsApp = (Button) view.findViewById(R.id.whatsApp);


        Bundle bundle = getArguments();
        id = bundle.getInt("ID");
        PersonInfo info = dbHendler.getCustInfo(id);
        String name = info.getName().toString().trim();
        // String startdat= String.valueOf(info.get_startdate().toString());
        String startdat = info.get_startdate().toString().trim();
        //    getDialog().setTitle(name);
        custname.setText(name);
        startDate.setText(startdat);

        displayFeeDeatail();


        return view;
    }


    public void displayFeeDeatail() {
        try {

            Cursor cursor = dbHendler.getFeesToList(id);
            if (cursor == null) {
                textview.setText("Unable to generate cursor.");
                return;
            }
            /*if (cursor.getCount() == 0)
            {
                textView4.setText("No Products in the Database.");
                return;
            }*/
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
            simpleCursorAdapter = new SimpleCursorAdapter(this.getActivity(),
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
        if (v.getId()==R.id.sendSms){
            Toast.makeText(this.getActivity(), "clicked"+v, Toast.LENGTH_SHORT).show();
        }
        if (v.getId()==R.id.whatsApp){
            Toast.makeText(this.getActivity(), "clicked"+v, Toast.LENGTH_SHORT).show();
        }

    }
}
