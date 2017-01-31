package com.example.saggu.myapplication;

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
 * Created by Saggu on 1/25/2017.
 */

public class DialogSTB extends DialogFragment {

    SimpleCursorAdapter simpleCursorAdapter;
    DbHendler dbHendler;
    ListView listViewStb;
    String TAG = "MyApp_ViewFees";
    TextView textview;
    int id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_stb, null);
        dbHendler = new DbHendler(this.getActivity(), null, null, 1);
        listViewStb = (ListView) view.findViewById(R.id.stb_list_dialog);
        Bundle bundle = getArguments();
//        id = bundle.getInt("ID");
         displaySTBList();
        return view;
    }

    //region Create all List
    public void displaySTBList() {
        try {
            Cursor cursor = dbHendler.getAllSTBs();
            if (cursor == null) {
                //  textView4.setText("Unable to generate cursor.");
                return;
            }
            if (cursor.getCount() == 0) {
                //   textView4.setText("No Customer in the Database.");
                return;
            }
            String[] columns = new String[]{
                    DbHendler.KEY_SN,
                    DbHendler.KEY_VC,
                    DbHendler.KEY_STATUS
            };
            int[] boundTo = new int[]{
                    R.id.stb_sn,
                    R.id.stb_vc,
                    R.id.stb_status
            };
            simpleCursorAdapter = new SimpleCursorAdapter(this.getActivity(),
                    R.layout.stb_list_item,
                    cursor,
                    columns,
                    boundTo,
                    0);
            listViewStb.setAdapter(simpleCursorAdapter);

        } catch (Exception ex) {
            //   textView4.setText("There was an error!");
        }
    }
    //endregion
}
