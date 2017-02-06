package com.example.saggu.myapplication;

import android.app.Dialog;
import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Saggu on 12/20/2016.
 */

public class DialogFeesDetail extends DialogFragment {
    SimpleCursorAdapter simpleCursorAdapter;
    DbHendler dbHendler;
    ListView listViewFees;
    String TAG = "MyApp_ViewFees";
    TextView textview;
    int id;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fees_detail, null);
        dbHendler = new DbHendler(this.getActivity(), null, null, 1);
        listViewFees = (ListView) view.findViewById(R.id.fees_list);

        Bundle bundle = getArguments();
        id = bundle.getInt("ID");
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
            textview.setText("There was an error!");
        }
    }
}
