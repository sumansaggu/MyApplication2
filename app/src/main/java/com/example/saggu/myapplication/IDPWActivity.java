package com.example.saggu.myapplication;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class IDPWActivity extends AppCompatActivity {
    ListView listidpw;
    SimpleCursorAdapter cursorAdapter;
    DbHendler dbHendler;
    String TAG ="IDPW ACTIVITY";
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idpw);
        dbHendler = new DbHendler(this, null, null, 1);
        listidpw = (ListView) findViewById(R.id.list_idpw);
        registerForContextMenu(listidpw);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                MQPasswordSetDialog mqPasswordSetDialog = new MQPasswordSetDialog();
                IDPWFragment idpwFragment = new IDPWFragment();
                Bundle bundle = new Bundle();
                mqPasswordSetDialog.setArguments(bundle);
                bundle.putString("CALL", "newid");
                idpwFragment.show(fragmentManager, "MQPasswordDailog");
            }
        });
        createList();
    }


    //region Create all List
    public void createList() {
        try {
             cursor = dbHendler.getIDPW();
            if (cursor == null) {
                Toast.makeText(this, "Cursor is null", Toast.LENGTH_SHORT).show();
                return;
            }
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "Please add ID and Password here", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] columns = new String[]{
                    //  DbHendler.KEY_ID,
                    DbHendler.KEY_USERID,
                    //      DbHendler.KEY_USERPASSWORD

            };
            int[] boundTo = new int[]{
                    //      R.id.areaId,
                    R.id.txtID


            };
            cursorAdapter = new SimpleCursorAdapter(this,
                    R.layout.idpw_item_layout,
                    cursor,
                    columns,
                    boundTo,
                    0);
            listidpw.setAdapter(cursorAdapter);

        } catch (Exception ex) {

            Toast.makeText(this, "" + ex, Toast.LENGTH_SHORT).show();
            //  textView4.setText("There was an error!");
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Edit");
        menu.add("Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle() == "Edit") {
            int id = (int) menuInfo.id;
            FragmentManager fragmentManager = getFragmentManager();
            //  MQPasswordSetDialog mqPasswordSetDialog = new MQPasswordSetDialog();
            IDPWFragment idpwFragment = new IDPWFragment();
            Bundle bundle = new Bundle();
            idpwFragment.setArguments(bundle);
            bundle.putString("CALL", "editid");
            bundle.putInt("ID", id);
            idpwFragment.show(fragmentManager, "MQPasswordDailog");
        }
        if(item.getTitle()=="Delete"){
            int id = (int) menuInfo.id;
            dbHendler.deleteIDPW(id);
            cursorswap();
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    public void cursorswap() {
        Log.d(TAG, "cursorswap: ");
        try {
            cursor  = dbHendler.getIDPW();
            cursorAdapter.swapCursor(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
