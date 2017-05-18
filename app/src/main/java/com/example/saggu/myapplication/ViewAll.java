package com.example.saggu.myapplication;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.tatarka.support.job.JobScheduler;


// TODO: 1/16/2017  prevent reverse engineering (will use minify)
// TODO: 1/25/2017  email support to be added    (crash reporting option will be used)
// TODO: 2/13/2017 start and stop date needed(cut option)
// TODO: 5/10/2017 filter list itmes from adaptor NOT requrey from sqlite
// TODO: 5/12/2017 pagerefresh to be added in webview and
// TODO: 5/12/2017 suggetions for searchAction
public class ViewAll extends AppCompatActivity implements Communicator, AdapterView.OnItemSelectedListener,AdapterView.OnItemClickListener
{

    SimpleCursorAdapter simpleCursorAdapter;
    SimpleCursorAdapter myAdapter;
    DbHendler dbHendler;
    ListView listViewCustomers;
    TextView textView4;
    String TAG = "MyApp_ViewAll";
    EditText searchBox;
    int backpress;
    String searchItem;

    private Cursor mCursor;
    Spinner spinner;
    List<Area> areas;
    List<String> items = new ArrayList<>();
    int areaId = 1;


    JobScheduler jobScheduler;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jobScheduler = JobScheduler.getInstance(this);

        setContentView(R.layout.activity_view_all);
        dbHendler = new DbHendler(this, null, null, 1);
        listViewCustomers = (ListView) findViewById(R.id.listView);
        listViewCustomers.setOnItemClickListener(this);
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
                if (searchBox.getText().toString().equals("logcust")) {
                    custmersToLog();
                    searchBox.setText("");
                }
                if (searchBox.getText().toString().equals("logstb")) {
                    stbToLog();
                    searchBox.setText("");
                }
                if (searchBox.getText().toString().equals("logout")) {
                //      signOut();
                    searchBox.setText("");
                }
                if (searchBox.getText().toString().equals("bulk")) {
                    dbHendler.insertBulkData();
                    searchBox.setText("");
                }
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Customers");


        if (isExternalStorageWritable() == false) {
            Toast.makeText(this, "SD Card not found", Toast.LENGTH_SHORT).show();
            toolbar.setTitle("SD Card not found");
        }





        // dbHendler.getAllFromCustAndSTB();
        displayProductList();
        registerForContextMenu(listViewCustomers);
        spinner = (Spinner) findViewById(R.id.spinnerByArea);
        spinner.setOnItemSelectedListener(this);
        loadSpinnerData();
        scheduleAlarm();

    }


    public void scheduleAlarm() {

        Calendar calendarNOW = Calendar.getInstance();
        calendarNOW.getTimeInMillis();

        //  Log.d(TAG, calendarNOW.getTime().toString());
        int today = Integer.parseInt(getDate(calendarNOW.getTime()));
        //  Log.d(TAG, "Today is " + today);

        Calendar calendar2 = Calendar.getInstance();
        //  calendar2.set(Calendar.DAY_OF_MONTH, 6);
        //   calendar2.set(Calendar.MINUTE,44);
        //  Log.d(TAG, calendar2.getTime().toString());
        //  int fd2 = Integer.parseInt(getDate(calendar2.getTime()));
        //  Log.d(TAG, "day is " + fd2);

        Intent intentAlarm = new Intent(this, AlarmReceiver.class);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        intentAlarm.putExtra("id", getDate(calendarNOW.getTime()));

        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendarNOW.getTimeInMillis(), pendingIntent);
    }

    public String getDate(Date time) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd");
        String formattedDate = df.format(time);
        return formattedDate;
    }




    //<editor-fold desc="Context Menu">
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Reciept");
        menu.add("Detail");
        menu.add("Edit");
        menu.add("START/CUT");
        menu.add("SET STB");
        menu.add("Delete");
        menu.add("Call");
        menu.add("MQ");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        // Get extra info about list item that was long-pressed
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getTitle() == "Delete") {
            DeleteAlert myAlert = new DeleteAlert();
            int custid = (int) menuInfo.id;
            int stbId = dbHendler.getStbIdFromCust(custid);
            Log.d(TAG,"STB ID " + stbId);
            Bundle bundle = new Bundle();
            myAlert.setArguments(bundle);
            bundle.putInt("CUSTID", custid);
            bundle.putInt("STBID", stbId);
            myAlert.show(getFragmentManager(), "DeleteAlert");

        }
        else if(item.getTitle()=="START/CUT"){
            int custId = (int) menuInfo.id;
            String status = dbHendler.conStatus(custId);

            CutStartDialog cutStartDialog = new CutStartDialog();
            Bundle bundle =new Bundle();
            cutStartDialog.setArguments(bundle);
            bundle.putInt("CUSTID", custId);
            bundle.putString("STATUS",status);
            cutStartDialog.show(getFragmentManager(),"CutStartDialog");

        }else if (item.getTitle() == "Edit") {
            int id = (int) menuInfo.id;
            Intent intent = new Intent(this, CustAddEditActivity.class);
            intent.putExtra("editcustomer", "editcustomer");
            intent.putExtra("ID", id);
            startActivity(intent);
        } else if (item.getTitle() == "SET STB") {
            int id = (int) menuInfo.id;
            android.app.FragmentManager manager = getFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putInt("CUSTID", id);
            DialogSTB dialogSTB = new DialogSTB();
            dialogSTB.setArguments(bundle);
            dialogSTB.show(manager, "DialogSTB");


        } else if (item.getTitle() == "Detail") {
            android.app.FragmentManager manager = getFragmentManager();
            Bundle bundle = new Bundle();
            DialogFeesDetail dialogFeesDetail = new DialogFeesDetail();
            dialogFeesDetail.setArguments(bundle);
            int id = (int) menuInfo.id;
            bundle.putInt("ID", id);
            dialogFeesDetail.show(manager, "FeeDetailDialog");
            // Toast.makeText(getApplicationContext(), "Selected For " + menuInfo.id, Toast.LENGTH_LONG).show();

        } else if (item.getTitle() == "Reciept") {
            android.app.FragmentManager manager = getFragmentManager();
            Bundle bundle = new Bundle();
            DialogReciept dialog = new DialogReciept();
            dialog.setArguments(bundle);
            int id = (int) menuInfo.id;
            bundle.putInt("ID", id);

            dialog.show(manager, "dialog");

        } else if (item.getTitle() == "Call") {
            int custId = (int) menuInfo.id;
            PersonInfo info = dbHendler.getCustInfo(custId);
            String contact = info.getPhoneNumber();
            Log.d(TAG, contact);
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + "+91" + contact));
            startActivity(i);

        }else if(item.getTitle()=="MQ"){
            int custId = (int) menuInfo.id;
            String sn = dbHendler.getAssignedSN(this,custId);
            Intent intent = new Intent(this, MQWebViewActivity.class);
            intent.putExtra("CALLINGACTIVITY", "VIEWALL");
            intent.putExtra("SN",sn);
            startActivity(intent);

        }

        return true;
    }
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
        if (id == R.id.area) {
            Intent intent = new Intent(this, AreaList.class);
            startActivity(intent);
        }if(id==R.id.set_password){
            Toast.makeText(this, "Set password here", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getFragmentManager();
            PasswordSetDialog passwordSetDialog = new PasswordSetDialog();
            Bundle bundle = new Bundle();
            passwordSetDialog.setArguments(bundle);
            bundle.putString("CALL","SET_PASSWORD");
            passwordSetDialog.show(fragmentManager,"PasswordDailog");
        }if(id==R.id.set_MQpassword) {
            Toast.makeText(this, "Set MQ password here", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getFragmentManager();
            MQPasswordSetDialog mqPasswordSetDialog = new MQPasswordSetDialog();
            Bundle bundle = new Bundle();
            mqPasswordSetDialog.setArguments(bundle);
            bundle.putString("CALL","SET_MQPASSWORD");
            mqPasswordSetDialog.show(fragmentManager, "MQPasswordDailog");
        }
        return super.onOptionsItemSelected(item);
    }    //endregion

    //</editor-fold>
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String sn =((TextView)view.findViewById(R.id.vc_mac)).getText().toString();

    }


    //region Create all List
    public void displayProductList() {
        try {
            Cursor cursor = dbHendler.getAllFromCustAndSTB(areaId);
            if (cursor == null) {
                Toast.makeText(this, "Unable to generate cursor", Toast.LENGTH_SHORT).show();
                return;
            }
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No Customer in the Database", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] columns = new String[]{
                    //DbHendler.KEY_ID,
                    DbHendler.KEY_NAME,
                    DbHendler.KEY_PHONE_NO,
                    DbHendler.KEY_CUST_NO,
                    DbHendler.KEY_FEES,
                    DbHendler.KEY_BALANCE,
                    DbHendler.KEY_SN,
                    DbHendler.KEY_CONSTATUS
            };
            int[] boundTo = new int[]{
                    //R.id.pId,
                    R.id.pName,
                    R.id.pMob,
                    R.id.cNo,
                    R.id.cFees,
                    R.id.cBalance,
                    R.id.vc_mac,
                    R.id.cStatus
            };
            myAdapter = new MySimpleCursorAdapter(this,
                    R.layout.layout_list,
                    cursor,
                    columns,
                    boundTo,
                    0);
            listViewCustomers.setAdapter(myAdapter);
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, "" + ex, Toast.LENGTH_LONG).show();
        }
    }





    //endregion

    private class MySimpleCursorAdapter extends SimpleCursorAdapter {
        public MySimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        // The newView method is used to inflate a new view and return it,
        // you don't bind any data to the view at this point.
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_list, parent, false);
            return view;
        }

        @Override
        // The bindView method is used to bind all data to a given view
        // such as setting the text on a TextView.
        public void bindView(View view, Context context, Cursor cursor) {

            // Find fields to populate in inflated template
            TextView name = (TextView) view.findViewById(R.id.pName);
            TextView mobile = (TextView) view.findViewById(R.id.pMob);
            TextView conNo = (TextView) view.findViewById(R.id.cNo);
            TextView sn = (TextView) view.findViewById(R.id.vc_mac);
            TextView status = (TextView) view.findViewById(R.id.cStatus);
            TextView fees = (TextView) view.findViewById(R.id.cFees);
            TextView balance = (TextView) view.findViewById(R.id.cBalance);


            // Extract properties from cursor
         //   int id = cursor.getInt(cursor.getColumnIndex(DbHendler.KEY_ID));
            String mname = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_NAME));
            String mmobile = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_PHONE_NO));
            String mconno = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_CUST_NO));
            String msn = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_SN));
            String mstatus = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_CONSTATUS));
            String mfees = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_FEES));
            String mbalance = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_BALANCE));
            name.setText(mname);
            mobile.setText(mmobile);
            conNo.setText(mconno);
            sn.setText(msn);

            if(mstatus.equals("ACTIVE")){status.setText(" ");
            }else status.setText("C");

            fees.setText(mfees);
            balance.setText(mbalance);



        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //get reference to the row
            View view = super.getView(position, convertView, parent);



            //check for odd or even to set alternate colors to the row background
            /*if (position % 2 == 0) {
                view.setBackgroundColor(Color.rgb(238, 233, 233));
            } else {
                view.setBackgroundColor(Color.rgb(255, 255, 255));
            }*/
            return view;
        }

    }




    //region Create Search  List
    public void displaySearchList() {
        searchItem = searchBox.getText().toString();
        try {
            Cursor cursor = dbHendler.searchPersonToList(searchItem);
            if (cursor == null) {
                //    textView4.setText("Unable to generate cursor.");
                return;
            }
            if (cursor.getCount() == 0) {
                //  textView4.setText("No Customer Found");
                return;
            } else {
                //   textView4.setText("");
                String[] columns = new String[]{
                        //DbHendler.KEY_ID,
                        DbHendler.KEY_NAME,
                        DbHendler.KEY_PHONE_NO,
                        DbHendler.KEY_CUST_NO,
                        DbHendler.KEY_FEES,
                        DbHendler.KEY_BALANCE,
                        DbHendler.KEY_SN
                };
                int[] boundTo = new int[]{
                        //R.id.pId,
                        R.id.pName,
                        R.id.pMob,
                        R.id.cNo,
                        R.id.cFees,
                        R.id.cBalance,
                        R.id.vc_mac
                };
                myAdapter = new MySimpleCursorAdapter(this,
                        R.layout.layout_list,
                        cursor,
                        columns,
                        boundTo,
                        0);
                listViewCustomers.setAdapter(myAdapter);
            }
        } catch (Exception ex) {
            Log.d(TAG, "" + ex);
//            textView4.setText("There was an error!");
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
                    DbHendler.KEY_BALANCE,
                    DbHendler.KEY_SN
            };
            int[] boundTo = new int[]{
                    //R.id.pId,
                    R.id.pName,
                    R.id.pMob,
                    R.id.cNo,
                    R.id.cFees,
                    R.id.cBalance,
                    R.id.vc_mac
            };
            myAdapter = new MySimpleCursorAdapter(this,
                    R.layout.layout_list,
                    cursor,
                    columns,
                    boundTo,
                    0);
            listViewCustomers.setAdapter(myAdapter);

        } catch (Exception ex) {
            textView4.setText("There was an error!");
        }
    }

    //endregion

    //region recreate list on dialog closed
    public void refreshListView() {
        Log.d(TAG, "dialg closed");
        mCursor = dbHendler.getAllFromCustAndSTB(areaId);
        myAdapter.swapCursor(mCursor);
    }
    //endregion

    //region delete person
    public void delete(int custid, int stbid) {
        dbHendler.deletePerson(custid, stbid);
        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
        //  Toast.makeText(getApplicationContext(), "ID " + menuInfo.id + ", position " + menuInfo.position, Toast.LENGTH_SHORT).show();
        //If your ListView's content was created by attaching it to a database cursor,
        // the ID property of the AdapterContextMenuInfo object is the database ID corresponding to the ListItem.
        displayProductList();
    }
    //endregion
    public void changeConStatus(int cutID,String newStatus){
        dbHendler.conCutStart(cutID, newStatus);

    }

    //region call to backup and restore functions
    public void backupDb() {
        String db = "myInfoManager.db";
        dbHendler.copyDbToExternalStorage(this.getApplicationContext());

    }

    public void restoreDB() {
        String db = "myInfoManager.db";
        dbHendler.restoreDBfile(this.getApplicationContext(), db);
    }
    //endregion


    private void loadSpinnerData() {

        // Spinner Drop down elements
        areas = dbHendler.getAllAreas();
        for (Area area : areas) {
            String singleitem = area.get_areaName();
            items.add(singleitem);
            items.add(singleitem);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }


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

    public void custmersToLog() {
        // Reading all contacts
        Log.d("Reading: ", "Reading all customer..");
        List<PersonInfo> personInfos = dbHendler.getAllContacts();

        for (PersonInfo info : personInfos) {
            String log = "Id: " + info.getID() + " ,Name: " + info.getName() + " ,Phone: " + info.getPhoneNumber()
                    + " Customer: " + info.get_cust_no() + " Fees: " + info.get_fees() + " Balance: " + info.get_balance() + " Area: " + info.get_area();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
    }

    public void stbToLog() {
        // Reading all contacts
        Log.d("Reading: ", "Reading all stbs..");
        List<STB> stbs = dbHendler.getAllStbs();

        for (STB stb : stbs) {
            String log = "Id: " + stb.getId() + " ,SN: " + stb.getSerialNo() + " ,VC: " + stb.getVcNo();

            // Writing Contacts to log
            Log.d("STB ", log);
        }
    }


    @Override
    public void onBackPressed() {
        backpress = (backpress + 1);
        if (backpress == 1) {
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        }
        if (backpress > 1) {

            //this.finishAffinity();
            //finish();
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            finishAffinity();


            super.onBackPressed();
        }
    }

    @Override
    public void respond(String data) {

        android.app.FragmentManager manager = getFragmentManager();
        DialogReciept dialogReciept = (DialogReciept) manager.findFragmentByTag("dialog");
        dialogReciept.changeText(data);

    }

    @Override
    public void respond2(String date2) {

    }



    @Override
    protected void onResume() {
        super.onResume();
        backpress = 0;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        String item = parent.getItemAtPosition(position).toString();
        areaId = dbHendler.getAreaID(item);
        displayProductList();
        Log.d(TAG, "" + areaId);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }







}
