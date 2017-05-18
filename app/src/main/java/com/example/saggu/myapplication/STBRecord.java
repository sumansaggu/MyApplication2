package com.example.saggu.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;


public class STBRecord extends AppCompatActivity {

    SimpleCursorAdapter simpleCursorAdapter;
    MySimpleCursorAdaptor adaptor;
    DbHendler dbHendler;
    ListView listViewStb;
    TextView totalSTBs;
    int stbcount;
    String TAG = "MyApp_STBRecord";
    RadioButton radioButton;
    private Cursor mCursor;
    String search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stbrecord);
        dbHendler = new DbHendler(this, null, null, 1);
        listViewStb = (ListView) findViewById(R.id.list_view_stb);
        radioButton = (RadioButton) findViewById(R.id.radioBtn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        totalSTBs = (TextView) findViewById(R.id.total_stbs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        registerForContextMenu(listViewStb);
        displaySTBList();
        stbcount = dbHendler.countSTBs;
        totalSTBs.setText("Total STBs: " + stbcount);

        //     radioButton.setVisibility(View.INVISIBLE);
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
       adaptor = new MySimpleCursorAdaptor(this,
                    R.layout.stb_list_item,
                    cursor,
                    columns,
                    boundTo,
                    0);
            listViewStb.setAdapter(adaptor);

        } catch (Exception ex) {
            //   textView4.setText("There was an error!");
        }
    }

    //endregion
    //region Create Search List
    public void searchSTBList() {

        try {
            Cursor cursor = dbHendler.searchSTBToList(search);
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
      adaptor = new MySimpleCursorAdaptor(this,
                    R.layout.stb_list_item,
                    cursor,
                    columns,
                    boundTo,
                    0);
            listViewStb.setAdapter(adaptor);

        } catch (Exception ex) {
            //   textView4.setText("There was an error!");
        }
    }
    //endregion

    //region Option Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stb_record, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered

                search = newText;
                searchSTBList();
                Log.d(TAG, "onQueryTextChange: " + newText);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_stb) {
            Intent intent = new Intent(this, StbAddEditActivity.class);
            intent.putExtra("add_stb", R.id.add_stb);
            //  Log.d(TAG, "add stb" + R.id.add_stb);
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
       // menu.add("Delete");
        menu.add("MQ");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        // Get extra info about list item that was long-pressed
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle() == "Edit") {
            int id = (int) menuInfo.id;
            Intent intent = new Intent(this, StbAddEditActivity.class);
            intent.putExtra("ID", id);
            Log.d(TAG, "" + id);
            startActivity(intent);

        }
        if (item.getTitle() == "Delete") {
            int stbId = (int) menuInfo.id;
            dbHendler.deleteSTB(stbId);
            refreshListView();
        }
        if (item.getTitle() == "MQ") {
            int stbId = (int) menuInfo.id;

            STB stb = dbHendler.getSTBInfo(stbId);
            String sn= stb.getSerialNo();
            Log.d(TAG, "onContextItemSelected: "+sn);
            Intent intent = new Intent(this, MQWebViewActivity.class);
            intent.putExtra("CALLINGACTIVITY", "STBRECORD");
            intent.putExtra("SN", sn);
            startActivity(intent);
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ViewAll.class);
        startActivity(i);
    }

    public class MySimpleCursorAdaptor extends SimpleCursorAdapter {
        public MySimpleCursorAdaptor(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }


        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.stb_list_item, parent, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            // Find fields to populate in inflated template
            TextView sn = (TextView) view.findViewById(R.id.stb_sn);
            TextView vc = (TextView) view.findViewById(R.id.stb_vc);
            TextView sts = (TextView) view.findViewById(R.id.stb_status);
            RadioButton radiobtn = (RadioButton) view.findViewById(R.id.radioBtn);
            radiobtn.setVisibility(View.INVISIBLE);

            // Extract properties from cursor

            String serial = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_SN));
            String Vcard = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_VC));
            String status = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_STATUS));
            sn.setText(serial);
            vc.setText(Vcard);
            sts.setText(status);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            RadioButton radiobtn = (RadioButton) view.findViewById(R.id.radioBtn);
            radiobtn.setVisibility(View.INVISIBLE);
            return view;
        }
    }

    public void refreshListView() {
        Log.d(TAG, "dialg closed");
        try {
            mCursor = dbHendler.getAllSTBs();
            adaptor.swapCursor(mCursor);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

