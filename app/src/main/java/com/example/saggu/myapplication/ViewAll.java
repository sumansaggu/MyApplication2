package com.example.saggu.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class ViewAll extends AppCompatActivity {

    SimpleCursorAdapter simpleCursorAdapter;
    DbHendler dbHendler;
    ListView lvProducts;
    TextView textView4;
    String TAG = "MyApp_ViewAll";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        dbHendler = new DbHendler(this, null, null, 1);
        lvProducts = (ListView) findViewById(R.id.listView);
        textView4 = (TextView) findViewById(R.id.textView4);
        displayProductList();
        registerForContextMenu(lvProducts);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Reciept");
        menu.add("Information");
        menu.add("Edit");
        menu.add("Delete");

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        // Get extra info about list item that was long-pressed
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle() == "Delete") {
            dbHendler.deletePerson((int) menuInfo.id);
            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "ID " + menuInfo.id + ", position " + menuInfo.position, Toast.LENGTH_SHORT).show();
            //If your ListView's content was created by attaching it to a database cursor,
            // the ID property of the AdapterContextMenuInfo object is the database ID corresponding to the ListItem.
            displayProductList();


        } else if (item.getTitle() == "Edit") {
            int id = (int) menuInfo.id;
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("ID", id);
            startActivity(intent);


        } else if (item.getTitle() == "Information") {

            //PersonInfo personInfo = dbHendler.getInfo((int) menuInfo.id);
            // String name = personInfo.getName().toString().trim();
            //String no   = personInfo.getPhoneNumber().toString().trim();
            Toast.makeText(getApplicationContext(), "Selected For " + menuInfo.id, Toast.LENGTH_LONG).show();

        } else if (item.getTitle() == "Reciept") {

            android.app.FragmentManager manager = getFragmentManager();
            Bundle bundle = new Bundle();
            Dialog dialog = new Dialog();
            dialog.setArguments(bundle);
            int id = (int) menuInfo.id;
            bundle.putInt("ID", id);

            dialog.show(manager, "dialog");


        }

        return true;
    }

    public void showDialog(View view) {


    }

    public void displayProductList() {
        try {
            Cursor cursor = dbHendler.getAllProducts();
            if (cursor == null) {
                textView4.setText("Unable to generate cursor.");
                return;
            }
            /*if (cursor.getCount() == 0)
            {
                textView4.setText("No Products in the Database.");
                return;
            }*/
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
            lvProducts.setAdapter(simpleCursorAdapter);
        } catch (Exception ex) {
            textView4.setText("There was an error!");
        }
    }

 /* lvProducts.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        Toast.makeText(getApplicationContext(), "You clicked " + position , Toast.LENGTH_SHORT).show();                    }
                }
        );*/
}