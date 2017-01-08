package com.example.saggu.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

// TODO: 1/6/2017 customer with same cust no problem
public class ViewAll extends AppCompatActivity implements Communicator{

    SimpleCursorAdapter simpleCursorAdapter;
    DbHendler dbHendler;
    ListView listViewCustomers;
    TextView textView4;
    String TAG = "MyApp_ViewAll";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        dbHendler = new DbHendler(this, null, null, 1);
        listViewCustomers = (ListView) findViewById(R.id.listView);
        textView4 = (TextView) findViewById(R.id.textView4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Customers");
        checkApi();
        displayProductList();
        registerForContextMenu(listViewCustomers);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Reciept");
        menu.add("Information");
        menu.add("Edit");
        menu.add("Delete");
    }

    //region context menu items
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
            Intent intent = new Intent(this, AddEditActivity.class);
            intent.putExtra("ID", id);
            startActivity(intent);


        } else if (item.getTitle() == "Information") {
            android.app.FragmentManager manager = getFragmentManager();
            Bundle bundle = new Bundle();
            DialogFeesDetail dialogFeesDetail = new DialogFeesDetail();
            dialogFeesDetail.setArguments(bundle);
            int id = (int) menuInfo.id;
            bundle.putInt("ID", id);
            dialogFeesDetail.show(manager, "FeeDetailDialog");
            Toast.makeText(getApplicationContext(), "Selected For " + menuInfo.id, Toast.LENGTH_LONG).show();

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

    //region Create List
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

    public void dialogClosed() {
        displayProductList();
    }

    public void delete(int id) {

        dbHendler.deletePerson(id);
        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
        //  Toast.makeText(getApplicationContext(), "ID " + menuInfo.id + ", position " + menuInfo.position, Toast.LENGTH_SHORT).show();
        //If your ListView's content was created by attaching it to a database cursor,
        // the ID property of the AdapterContextMenuInfo object is the database ID corresponding to the ListItem.
        displayProductList();
    }

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
        if (id == R.id.total_balance) {
           int sum= dbHendler.totalBalance();

        }
        if (id == R.id.colection_btw_two_dates) {
            dbHendler.colectionBwtwoDates();
        }
        if (id == R.id.add_customer) {
            startActivity(new Intent(this, AddEditActivity.class));
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

        return super.onOptionsItemSelected(item);
    }    //endregion

    public int checkApi() {
        String currntVersion = Build.VERSION.RELEASE;
        int currentRealease = Build.VERSION.SDK_INT;
        Log.d(TAG, "OS:" + currntVersion + " API:" + currentRealease);
        return currentRealease;
    }


    @Override
    public void respond(String data) {

        android.app.FragmentManager manager = getFragmentManager();
       DialogReciept dialogReciept= (DialogReciept) manager.findFragmentByTag("dialog");

        dialogReciept.changeText(data);

    }
}