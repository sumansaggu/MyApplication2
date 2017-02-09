package com.example.saggu.myapplication;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Saggu on 1/25/2017.
 */

public class DialogSTB extends DialogFragment {

    SimpleCursorAdapter simpleCursorAdapter;
    DbHendler dbHendler;
    ListView listViewStb;
    String TAG = "MyApp_DialogSTB";
    TextView stbcountUA;
    int stbcountUa;
    int custId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_stb, null);
        dbHendler = new DbHendler(this.getActivity(), null, null, 1);
        stbcountUA = (TextView) view.findViewById(R.id.totalStbsUnAssigned);
        listViewStb = (ListView) view.findViewById(R.id.stb_list_dialog);

        registerForContextMenu(listViewStb);
        Bundle bundle = getArguments();
        custId = bundle.getInt("CUSTID");
        displaySTBList();
        stbcountUa= dbHendler.countSTBsUA;
        stbcountUA.setText("STBs:"+stbcountUa);
        return view;
    }

    //region Create all List
    public void displaySTBList() {
        try {
            Cursor cursor = dbHendler.getUnAssignedSTBs();
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


    //region context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //   super.onCreateContextMenu(menu, v, menuInfo);
        MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onContextItemSelected(item);
                return true;
            }
        };
        menu.add("Assign STB");
        menu.add("Release STB");

        for (int i = 0, n = menu.size(); i < n; i++)
            menu.getItem(i).setOnMenuItemClickListener(listener);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle() == "Assign STB") {
            //entry in assigned column
            int stbId = (int) info.id;
            int assigned = custId;
            Log.d(TAG, "stb id:" + stbId);
            Log.d(TAG, "Cust Id:" + custId);
            if (dbHendler.getSTBID(custId) > 0) {
                Toast.makeText(this.getActivity(), "Already Assigned STB", Toast.LENGTH_LONG).show();
            } else {
                dbHendler.assignSTB(new STB(stbId, assigned));
                dbHendler.SetStbID(custId, stbId);
                Toast.makeText(this.getActivity(), "Now Assigned STB", Toast.LENGTH_LONG).show();
                ViewAll activity = (ViewAll) getActivity();
                activity.dialogClosed();
                dismiss();
            }


        }
        if (item.getTitle() == "Release STB") {


        }
        return true;
    }
    //endregion
}
