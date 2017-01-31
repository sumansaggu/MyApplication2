package com.example.saggu.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;


// TODO: 1/12/2017  fees adjustment needed // done but with negetive entry
// TODO: 1/16/2017  prevent reverse engineering
// TODO: 1/25/2017  email support to be added 
// TODO: 1/25/2017 problem with decimal entry
// TODO: 1/30/2017 problem with stb status change
public class ViewAll extends AppCompatActivity implements Communicator {

    SimpleCursorAdapter simpleCursorAdapter;
    DbHendler dbHendler;
    ListView listViewCustomers;
    TextView textView4;
    String TAG = "MyApp_ViewAll";
    EditText searchBox;
    int backpress;
    String searchItem;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the FirebaseAnalytics instance.
      mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_view_all);

        dbHendler = new DbHendler(this, null, null, 1);
        listViewCustomers = (ListView) findViewById(R.id.listView);
        textView4 = (TextView) findViewById(R.id.textView4);
        searchBox = (EditText) findViewById(R.id.search_box);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                displaySearchList();

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchBox.getText().toString().equals("")) {
                    displayProductList();
                }
                if (searchBox.getText().toString().equals("checkmonth")) {
                    dbHendler.checkmonthchange(getApplicationContext());
                    searchBox.setText("");
                }
                if (searchBox.getText().toString().equals("@endofmonth@")) {
                    dbHendler.endOfMonth(getApplicationContext());
                    searchBox.setText("");
                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Customers");
        checkApi();
        displayProductList();
        registerForContextMenu(listViewCustomers);
    }


    //region context menu items
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Reciept");
        menu.add("Detail");
        menu.add("Other Information");
        menu.add("Edit");
        menu.add("Delete");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        // Get extra info about list item that was long-pressed
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle() == "Delete") {
            DeleteAlert myAlert = new DeleteAlert();
            myAlert.show(getFragmentManager(), "DeleteAlert");
            int id = (int) menuInfo.id;
            Bundle bundle = new Bundle();
            myAlert.setArguments(bundle);
            bundle.putInt("ID", id);


        } else if (item.getTitle() == "Edit") {
            int id = (int) menuInfo.id;
            Intent intent = new Intent(this, CustAddEditActivity.class);
            intent.putExtra("editcustomer","editcustomer");
            intent.putExtra("ID", id);
            startActivity(intent);


        } else if (item.getTitle() == "Detail") {
            android.app.FragmentManager manager = getFragmentManager();
            Bundle bundle = new Bundle();
            DialogFeesDetail dialogFeesDetail = new DialogFeesDetail();
            dialogFeesDetail.setArguments(bundle);
            int id = (int) menuInfo.id;
            bundle.putInt("ID", id);
            dialogFeesDetail.show(manager, "FeeDetailDialog");
            Toast.makeText(getApplicationContext(), "Selected For " + menuInfo.id, Toast.LENGTH_LONG).show();

        }else if(item.getTitle()=="Other Information"){
            android.app.FragmentManager manager= getFragmentManager();
            Bundle bundle = new Bundle();
            DialogSTB dialogSTB = new DialogSTB();
            dialogSTB.show(manager,"DialogSTB");


        } else if (item.getTitle() == "Reciept") {
            android.app.FragmentManager manager = getFragmentManager();
            Bundle bundle = new Bundle();
            DialogReciept dialog = new DialogReciept();
            dialog.setArguments(bundle);
            int id = (int) menuInfo.id;
            bundle.putInt("ID", id);
            dialog.show(manager, "dialog");
        }
        return true;
    }
    //endregion

    //region Create all List
    public void displayProductList() {
        try {
            Cursor cursor = dbHendler.getAllProducts();
            if (cursor == null) {
                textView4.setText("Unable to generate cursor.");
                return;
            }
            if (cursor.getCount() == 0) {
                textView4.setText("No Customer in the Database.");
                return;
            }
            String[] columns = new String[]{
                    //DbHendler.KEY_ID,
                    DbHendler.KEY_NAME,
                    DbHendler.KEY_PHONE_NO,
                    DbHendler.KEY_CUST_NO,
                    DbHendler.KEY_FEES,
                    DbHendler.KEY_BALANCE
            };
            int[] boundTo = new int[]{
                    //R.id.pId,
                    R.id.pName,
                    R.id.pMob,
                    R.id.cNo,
                    R.id.cFees,
                    R.id.cBalance
            };
            simpleCursorAdapter = new SimpleCursorAdapter(this,
                    R.layout.layout_list,
                    cursor,
                    columns,
                    boundTo,
                    0);
            listViewCustomers.setAdapter(simpleCursorAdapter);

        } catch (Exception ex) {
            textView4.setText("There was an error!");
        }
    }

    //endregion

    //region Create Search  List
    public void displaySearchList() {
        searchItem = searchBox.getText().toString();
        try {
            Cursor cursor = dbHendler.searchPersonToList(searchItem);
            if (cursor == null) {
                textView4.setText("Unable to generate cursor.");
                return;
            }
            if (cursor.getCount() == 0) {
                textView4.setText("No Customer Found");
                return;
            } else {
                textView4.setText("");
                String[] columns = new String[]{
                        //DbHendler.KEY_ID,
                        DbHendler.KEY_NAME,
                        DbHendler.KEY_PHONE_NO,
                        DbHendler.KEY_CUST_NO,
                        DbHendler.KEY_FEES,
                        DbHendler.KEY_BALANCE
                };
                int[] boundTo = new int[]{
                        //R.id.pId,
                        R.id.pName,
                        R.id.pMob,
                        R.id.cNo,
                        R.id.cFees,
                        R.id.cBalance
                };
                simpleCursorAdapter = new SimpleCursorAdapter(this,
                        R.layout.layout_list,
                        cursor,
                        columns,
                        boundTo,
                        0);
                listViewCustomers.setAdapter(simpleCursorAdapter);
            }
        } catch (Exception ex) {
            textView4.setText("There was an error!");
        }
    }

    //endregion

    //region larger balance list
    public void getLargerBalance() {
        try {
            Cursor cursor = dbHendler.getLargerBalance();
            if (cursor == null) {
                textView4.setText("Unable to generate cursor.");
                return;
            }
            if (cursor.getCount() == 0) {
                textView4.setText("No Customer in the Database.");
                return;
            }
            String[] columns = new String[]{
                    //DbHendler.KEY_ID,
                    DbHendler.KEY_NAME,
                    DbHendler.KEY_PHONE_NO,
                    DbHendler.KEY_CUST_NO,
                    DbHendler.KEY_FEES,
                    DbHendler.KEY_BALANCE
            };
            int[] boundTo = new int[]{
                    //R.id.pId,
                    R.id.pName,
                    R.id.pMob,
                    R.id.cNo,
                    R.id.cFees,
                    R.id.cBalance
            };
            simpleCursorAdapter = new SimpleCursorAdapter(this,
                    R.layout.layout_list,
                    cursor,
                    columns,
                    boundTo,
                    0);
            listViewCustomers.setAdapter(simpleCursorAdapter);

        } catch (Exception ex) {
            textView4.setText("There was an error!");
        }
    }

    //endregion

    //region recreate list on dialog closed
    public void dialogClosed() {
        displayProductList();
    }
    //endregion

    //region delete person
    public void delete(int id) {

        dbHendler.deletePerson(id);
        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
        //  Toast.makeText(getApplicationContext(), "ID " + menuInfo.id + ", position " + menuInfo.position, Toast.LENGTH_SHORT).show();
        //If your ListView's content was created by attaching it to a database cursor,
        // the ID property of the AdapterContextMenuInfo object is the database ID corresponding to the ListItem.
        displayProductList();
    }
    //endregion

    //region call to backup and restore functions
    public void backupDb() {
        String db = "myInfoManager.db";
        Log.d(TAG, "called");
        dbHendler.copyDbToExternalStorage(this.getApplicationContext(), db);

    }

    public void restoreDB() {
        String db = "myInfoManager.db";
        Log.d(TAG, "called");
        dbHendler.restoreDBfile(this.getApplicationContext(), db);
    }
    //endregion

    //region OptionMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_all_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.large_balance) {
            getLargerBalance();
            return true;
        }
        if (id == R.id.by_number) {
            // int sum = dbHendler.totalBalance();
            displayProductList();
            return true;
        }
        if (id == R.id.colection_btw_two_dates) {
            Intent intent = new Intent(this, BtwTwoDates.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.add_customer) {
            startActivity(new Intent(this, CustAddEditActivity.class));
            return true;
        }
        if (id == R.id.manage_stb) {
            Intent intent = new Intent(this, STBRecord.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.backup_database) {
            backupDb();
            return true;
        }
        if (id == R.id.restore_database) {
            RestoreDbAlert restoreDbAlert = new RestoreDbAlert();
            restoreDbAlert.show(getFragmentManager(), "restoreDbAlert");
            return true;
        }
        if (id == R.id.add_stb) {
            Intent intent = new Intent(this, CustAddEditActivity.class);
            intent.putExtra("addstb", R.id.add_stb);
            Log.d(TAG, "add stb" + R.id.add_stb);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }    //endregion

    //region Reading searched item to log
    public void search() {


        Log.d("Reading: ", "Reading searched item..");
        List<PersonInfo> personInfos = dbHendler.searchPerson();

        for (PersonInfo info : personInfos) {
            String log = "Id: " + info.getID() + " ,Name: " + info.getName() + " ,Phone: " + info.getPhoneNumber()
                    + " Customer: " + info.get_cust_no() + " Fees: " + info.get_fees();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
    }
    //endregion


    @Override
    public void respond(String data) {

        android.app.FragmentManager manager = getFragmentManager();
        DialogReciept dialogReciept = (DialogReciept) manager.findFragmentByTag("dialog");
        dialogReciept.changeText(data);

    }

    @Override
    public void respond2(String date2) {

    }

    public int checkApi() {
        String currntVersion = Build.VERSION.RELEASE;
        int currentRealease = Build.VERSION.SDK_INT;
        Log.d(TAG, "OS:" + currntVersion + " API:" + currentRealease);
        return currentRealease;
    }


    @Override
    protected void onResume() {
        super.onResume();
        backpress = 0;
    }

    @Override
    public void onBackPressed() {
        backpress = (backpress + 1);
        if (backpress == 1) {
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        }
        if (backpress > 1) {
            this.finishAffinity();
            super.onBackPressed();
        }
    }
}