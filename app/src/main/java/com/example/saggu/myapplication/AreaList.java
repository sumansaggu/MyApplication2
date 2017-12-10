package com.example.saggu.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class AreaList extends AppCompatActivity {
    SimpleCursorAdapter simpleCursorAdapter;
    DbHendler dbHendler;
    ListView listViewArea;
    String tag = "AreaList ";
    private Cursor mCursor;
    static final String bundle_stbid= "editstb";
    String TAG = "Area List";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_list);
        dbHendler = new DbHendler(this, null, null, 1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Area List");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AreaAddEdit.class);
                startActivity(intent);
            }
        });
        listViewArea = (ListView) findViewById(R.id.listViewArea);
        displayAreaList();
        registerForContextMenu(listViewArea);


    }

    //region Create all List
    public void displayAreaList() {
        try {
            Cursor cursor = dbHendler.getAreasToList();
            if (cursor == null) {
                Toast.makeText(this, "Cursor is null", Toast.LENGTH_SHORT).show();
                return;
            }
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No Record Found", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] columns = new String[]{
                  //  DbHendler.KEY_ID,
                    DbHendler.KEY_AREANO,
                    DbHendler.KEY_AREANAME

            };
            int[] boundTo = new int[]{
              //      R.id.areaId,
                    R.id.areaNoInList,
                    R.id.areaNameInList

            };
            simpleCursorAdapter = new SimpleCursorAdapter(this,
                    R.layout.area_list_item,
                    cursor,
                    columns,
                    boundTo,
                    0);
            listViewArea.setAdapter(simpleCursorAdapter);

        } catch (Exception ex) {
            Log.d(tag, "" + ex);
            Toast.makeText(this, "" + ex, Toast.LENGTH_SHORT).show();
            //  textView4.setText("There was an error!");
        }
    }

    //endregion
    public void cursorswap() {

        try {
            mCursor = dbHendler.getAreasToList();
            simpleCursorAdapter.swapCursor(mCursor);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        cursorswap();
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Edit");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle() == "Edit") {
            int id = (int) menuInfo.id;
            Intent intent = new Intent(this,AreaAddEdit.class);
            intent.putExtra("editArea", "editarea");
            intent.putExtra(bundle_stbid, id);
            Log.d(TAG, "onContextItemSelected: "+id);
            startActivity(intent);
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.area_add_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_area) {
            Log.d(TAG, "onOptionsItemSelected: ");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}