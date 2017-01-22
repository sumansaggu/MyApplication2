package com.example.saggu.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class STBRecord extends AppCompatActivity {

    SimpleCursorAdapter simpleCursorAdapter;
    DbHendler dbHendler;
    ListView listViewStb;
    String TAG = "MyApp_STBRecord";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stbrecord);
        dbHendler = new DbHendler(this, null, null, 1);
        listViewStb = (ListView) findViewById(R.id.list_view_stb);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage STBs");
        registerForContextMenu(listViewStb);
        displaySTBList();
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
                    DbHendler.KEY_VC
            };
            int[] boundTo = new int[]{
                    R.id.stb_sn,
                    R.id.stb_vc
            };
            simpleCursorAdapter = new SimpleCursorAdapter(this,
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


    //region Option Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stb_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_stb) {
            Intent intent = new Intent(this, AddEditActivity.class);
            intent.putExtra("addstb", R.id.add_stb);
            Log.d(TAG, "add stb" +R.id.add_stb);
            startActivity(intent);
            return true;
        }
        if (id == R.id.scan_stb) {
            Intent intent = new Intent(this, ScannerActivity.class);
            //intent.putExtra("addstb", R.id.add_stb);
           // Log.d(TAG, "add stb" +R.id.add_stb);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Edit");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        // Get extra info about list item that was long-pressed
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle() == "Edit") {

            /*DeleteAlert myAlert = new DeleteAlert();
            myAlert.show(getFragmentManager(), "DeleteAlert");
            int id = (int) menuInfo.id;
            Bundle bundle = new Bundle();
            myAlert.setArguments(bundle);
            bundle.putInt("ID", id);*/


        }
        return true;
    }
}
