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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
public class ViewAll extends AppCompatActivity implements Communicator, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    SimpleCursorAdapter simpleCursorAdapter;
    SimpleCursorAdapter myAdapter;
    DbHendler dbHendler;
    ListView listViewCustomers;
    String TAG = "MyApp_ViewAll";
    EditText searchBox;
    int backpress;
    String searchItem = "";

    private Cursor mCursor;
    Spinner spinnerArea, spinnerSearchBy;
    List<Area> areas;
    List<String> itemsArea = new ArrayList<>();
    List<String> itemsSearchby = new ArrayList<>();
    int areaId = 1;
    int searchBy = 0;
    String searchText = "";


    JobScheduler jobScheduler;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private String path;
    private File dir;
    private File file;
    private PdfPCell cell;
    //use to set background color
    BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
    BaseColor myColor1 = WebColors.getRGBColor("#757575");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        jobScheduler = JobScheduler.getInstance(this);

        setContentView(R.layout.activity_view_all);
        // recovering the instance state
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

                searchText = String.valueOf(s);
                createList(searchText);

                try {
                    myAdapter.swapCursor(mCursor);
                } catch (Exception e) {
                    Log.d(TAG, "onTextChanged: " + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchBox.getText().toString().equals("pdf")) {

                    searchBox.setText("");

                }
                if (searchBox.getText().toString().equals("@idpw")) {
                    dbHendler.createIDPWTable();
                    searchBox.setText("");

                }
                if (searchBox.getText().toString().equals("checkmonth")) {
                    dbHendler.checkmonthchange(getApplicationContext());

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
                if (searchBox.getText().toString().equals("import")) {
                    dbHendler.close();
                    goToImportValues();
                    searchBox.setText("");
                }
                if (searchBox.getText().toString().equals("deletedb")) {
                    deletedb();
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

        //dbHendler.getAllFromCustAndSTB();
        createList("");
        registerForContextMenu(listViewCustomers);
        spinnerArea = (Spinner) findViewById(R.id.spinnerByArea);
        spinnerArea.setOnItemSelectedListener(this);
        spinnerSearchBy = (Spinner) findViewById(R.id.spinnerSearchByName);
        spinnerSearchBy.setOnItemSelectedListener(this);
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
        menu.add("MQ");
        menu.add("Call");
        menu.add("Message");
        menu.add("Start/Cut");
        menu.add("Edit");
        menu.add("Set STB");
        menu.add("Delete");


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

            Bundle bundle = new Bundle();
            myAlert.setArguments(bundle);
            bundle.putInt("CUSTID", custid);
            bundle.putInt("STBID", stbId);
            myAlert.show(getFragmentManager(), "DeleteAlert");

        } else if (item.getTitle() == "Start/Cut") {
            int custId = (int) menuInfo.id;
            String status = dbHendler.conStatus(custId);

            CutStartDialog cutStartDialog = new CutStartDialog();
            Bundle bundle = new Bundle();
            cutStartDialog.setArguments(bundle);
            bundle.putInt("CUSTID", custId);
            bundle.putString("STATUS", status);
            cutStartDialog.show(getFragmentManager(), "CutStartDialog");

        } else if (item.getTitle() == "Edit") {
            int id = (int) menuInfo.id;
            Intent intent = new Intent(this, CustAddEditActivity.class);
            intent.putExtra("editcustomer", "editcustomer");
            intent.putExtra("ID", id);
            startActivity(intent);

        } else if (item.getTitle() == "Set STB") {
            int custid = (int) menuInfo.id;
            android.app.FragmentManager manager = getFragmentManager();
            PersonInfo personInfo = new PersonInfo();
            long stbId = dbHendler.getSTBID(custid);
            Bundle bundle = new Bundle();
            bundle.putInt("CUSTID", custid);
            bundle.putLong("STBID", stbId);
            DialogSTB dialogSTB = new DialogSTB();
            dialogSTB.setArguments(bundle);
            dialogSTB.show(manager, "DialogSTB");


        }/* else if (item.getTitle() == "Detail") {
            android.app.FragmentManager manager = getFragmentManager();
            Bundle bundle = new Bundle();
            DialogFeesDetail dialogFeesDetail = new DialogFeesDetail();
            dialogFeesDetail.setArguments(bundle);
            int id = (int) menuInfo.id;
            bundle.putInt("ID", id);
            dialogFeesDetail.show(manager, "FeeDetailDialog");
            // Toast.makeText(getApplicationContext(), "Selected For " + menuInfo.id, Toast.LENGTH_LONG).show();
*/ else if (item.getTitle() == "Detail") {
            int id = (int) menuInfo.id;
            Intent intent = new Intent(this, DetailFeesActivity.class);
            intent.putExtra("ID", id);
            startActivity(intent);


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

            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + "+91" + contact));
            startActivity(i);

        } else if (item.getTitle() == "MQ") {
            int custId = (int) menuInfo.id;
            String sn = dbHendler.getAssignedSN(this, custId);
            Intent intent = new Intent(this, MQWebViewActivity.class);
            intent.putExtra("CALLINGACTIVITY", "VIEWALL");
            intent.putExtra("SN", sn);
            startActivity(intent);

        } else if (item.getTitle() == "Message") {
            int custId = (int) menuInfo.id;
            PersonInfo info = dbHendler.getCustInfo(custId);
            String name = info.getName();
            String mobNo = info.getPhoneNumber();

            int balance = info.get_balance();
            String y = "";
            List<Fees> detail = dbHendler.getFeesForMsg(custId);

            for (Fees fees : detail) {

                String x = "Rs." + fees.getFees() + " " + fees.getDate() + ", ";
                //    Log.d("single entry ", x);
                y = y.concat(x);
            }
            String z = "Payment due Rs. " + balance + ", " + y;

        }

        return true;
    }


    public void send(String detail, String mobNo) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, detail);
        //   sendIntent.putExtra("jid", mobNo + "@s.whatsapp.net"); //phone number without "+" prefix
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_all_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.large_balance) {
            //  getLargerBalance();
            createList("largeBalance");
            myAdapter.swapCursor(mCursor);
            return true;
        }
        if (id == R.id.by_number) {
            // int sum = dbHendler.totalBalance();
            createList("");
            return true;
        }
        if (id == R.id.colection_btw_two_dates) {
            Intent intent = new Intent(this, BtwTwoDates.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.create_pdf){
            try {
                createPdf2();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
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

            startActivity(intent);
            return true;
        }
        if (id == R.id.area) {
            Intent intent = new Intent(this, AreaList.class);
            startActivity(intent);
        }
        if (id == R.id.set_password) {
            Toast.makeText(this, "Set password here", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getFragmentManager();
            PasswordSetDialog passwordSetDialog = new PasswordSetDialog();
            Bundle bundle = new Bundle();
            passwordSetDialog.setArguments(bundle);
            bundle.putString("CALL", "SET_PASSWORD");
            passwordSetDialog.show(fragmentManager, "PasswordDailog");
        }
        if (id == R.id.set_MQpassword) {
            Toast.makeText(this, "Set MQ password here", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getFragmentManager();
            MQPasswordSetDialog mqPasswordSetDialog = new MQPasswordSetDialog();
            Bundle bundle = new Bundle();
            mqPasswordSetDialog.setArguments(bundle);
            bundle.putString("CALL", "SET_MQPASSWORD");
            mqPasswordSetDialog.show(fragmentManager, "MQPasswordDailog");

        }
        if (id == R.id.importdata) {
            Intent intent = new Intent(this, ImportActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }    //endregion

    //</editor-fold>
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String sn = ((TextView) view.findViewById(R.id.vc_mac)).getText().toString();

    }


    public void createList(String s) {
        try {
            if (s.equals("")) {
                //
                mCursor = dbHendler.getAllFromCustAndSTB(areaId);

            } else if (!s.equals("") && !s.equals("largeBalance")) {
                //     Log.d(TAG, "createList: 2");
                if (searchBy == 0) {
                    mCursor = dbHendler.searchPersonToList(s);
                } else if (searchBy == 1) {
                    mCursor = dbHendler.searchPersonByNickName(s);
                } else if (searchBy == 2) {
                    mCursor = dbHendler.searchBySTBSN(s);
                } else if (searchBy == 3) {
                    mCursor = dbHendler.searchPersonByMobileNo(s);
                }
            } else if (s.equals("largeBalance")) {
                //    Log.d(TAG, "createList: 3");
                mCursor = dbHendler.getLargerBalance();
            } else if (!s.equals("") && !(searchBy == 0)) {
                //     Log.d(TAG, "createList: 4");
                mCursor = dbHendler.searchPersonByNickName(s);
            }
            if (mCursor == null) {
                Toast.makeText(this, "Unable to generate cursor", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mCursor.getCount() == 0) {
                //   Toast.makeText(this, "No Customer in the Database", Toast.LENGTH_SHORT).show();

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
                    DbHendler.KEY_CONSTATUS,
                    DbHendler.KEY_NICKNAME
            };
            int[] boundTo = new int[]{
                    //R.id.pId,
                    R.id.pName,
                    R.id.pMob,
                    R.id.cNo,
                    R.id.cFees,
                    R.id.cBalance,
                    R.id.vc_mac,
                    R.id.cStatus,
                    R.id.txtnickname
            };
            myAdapter = new MySimpleCursorAdapter(this,
                    R.layout.layout_list,
                    mCursor,
                    columns,
                    boundTo,
                    0);
            listViewCustomers.setAdapter(myAdapter);
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, "" + ex, Toast.LENGTH_LONG).show();
        }
    }


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
            TextView nickname = (TextView) view.findViewById(R.id.txtnickname);


            // Extract properties from cursor
            //   int id = cursor.getInt(cursor.getColumnIndex(DbHendler.KEY_ID));
            String mname = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_NAME));
            String mmobile = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_PHONE_NO));
            String mconno = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_CUST_NO));
            String msn = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_SN));
            String mstatus = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_CONSTATUS));
            String mfees = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_FEES));
            String mbalance = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_BALANCE));
            String mnickname = cursor.getString(cursor.getColumnIndex(DbHendler.KEY_NICKNAME));

            name.setText(mname);
            mobile.setText(mmobile);
            conNo.setText(mconno);
            sn.setText(msn);

            if (mstatus.equals("ACTIVE")) {
                status.setText(" ");
            } else status.setText("C");

            fees.setText(mfees);
            balance.setText(mbalance);
            nickname.setText(mnickname);


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


    //region recreate list on dialog closed
    public void refreshListView() {

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
        createList("");
    }

    //endregion
    public void changeConStatus(int cutID, String newStatus) {
        dbHendler.conCutStart(cutID, newStatus);

    }

    //region call to backup and restore functions
    public void backupDb() {

        dbHendler.copyDbToExternalStorage(this.getApplicationContext());

    }


    private void deletedb() {
        File dbfile = new File(Environment.getExternalStorageDirectory(), "/MyCableData/myInfoManager.db");
        dbfile.delete();
        Toast.makeText(this, "Database deleted", Toast.LENGTH_SHORT).show();
    }

    public void restoreDB() {
        String db = "myInfoManager.db";
        dbHendler.restoreDBfile(this.getApplicationContext(), db);
    }
    //endregion


    public void loadSpinnerData() {

        // Spinner Drop down elements
        areas = dbHendler.getAllAreas();
        for (Area area : areas) {
            String singleitem = area.get_areaName();
            itemsArea.add(singleitem);
        }

        itemsSearchby.add("Full Name");
        itemsSearchby.add("Nick Name");
        itemsSearchby.add("STB Serial");
        itemsSearchby.add("Mobile No.");
        // Creating adapter for spinnerArea
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsArea);
        ArrayAdapter<String> searchByAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsSearchby);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchByAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinnerArea
        spinnerArea.setAdapter(dataAdapter);
        spinnerSearchBy.setAdapter(searchByAdaptor);


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

            try {
                MQWebViewActivity.mqactivity.finish();   // to finish Oracle activity on backpress
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        createList(searchBox.getText().toString());

        Log.d(TAG, "onResume: ");
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        if (parent.getId() == R.id.spinnerByArea) {
            String item = parent.getItemAtPosition(position).toString();
            areaId = dbHendler.getAreaID(item);
            createList("");
        } else if (parent.getId() == R.id.spinnerSearchByName) {
            searchBy = position;
            createList(searchText);
        }
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


    public void goToImportValues() {
        Intent intent = new Intent(this, ImportActivity.class);
        startActivity(intent);
        finish();
    }


    public void createPDF() throws FileNotFoundException, DocumentException {

        //create document file
        Document doc = new Document();
        try {

            Log.e("PDFCreator", "PDF Path: " + path);
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            file = new File(dir, "Trinity PDF" + sdf.format(Calendar.getInstance().getTime()) + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();
            //create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
           /* Drawable myImage = MainActivity.this.getResources().getDrawable(R.drawable.trinity);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();*/
            try {
                //bgImage = Image.getInstance(bitmapdata);
                // bgImage.setAbsolutePosition(330f, 642f);
                //cell.addElement(bgImage);
                // pt.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.addElement(new Paragraph("Trinity Tuts"));

                cell.addElement(new Paragraph(""));
                cell.addElement(new Paragraph(""));
                pt.addCell(cell);
                cell = new PdfPCell(new Paragraph(""));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);
                cell = new PdfPCell();
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);
                PdfPTable table = new PdfPTable(6);

                float[] columnWidth = new float[]{6, 30, 30, 20, 20, 30};
                table.setWidths(columnWidth);


                cell = new PdfPCell();


                cell.setBackgroundColor(myColor);
                cell.setColspan(6);
                cell.addElement(pTable);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setColspan(6);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(6);

                cell.setBackgroundColor(myColor1);

                cell = new PdfPCell(new Phrase("#"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Header 1"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Header 2"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Header 3"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Header 4"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Header 5"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                //table.setHeaderRows(3);
                cell = new PdfPCell();
                cell.setColspan(6);

                for (int i = 1; i <= 10; i++) {
                    table.addCell(String.valueOf(i));
                    table.addCell("Header 1 row " + i);
                    table.addCell("Header 2 row " + i);
                    table.addCell("Header 3 row " + i);
                    table.addCell("Header 4 row " + i);
                    table.addCell("Header 5 row " + i);

                }

                PdfPTable ftable = new PdfPTable(6);
                ftable.setWidthPercentage(100);
                float[] columnWidthaa = new float[]{30, 10, 30, 10, 30, 10};
                ftable.setWidths(columnWidthaa);
                cell = new PdfPCell();
                cell.setColspan(6);
                cell.setBackgroundColor(myColor1);
                cell = new PdfPCell(new Phrase("Total Nunber"));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Paragraph("Footer"));
                cell.setColspan(6);
                ftable.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(6);
                cell.addElement(ftable);
                table.addCell(cell);
                doc.add(table);
                Toast.makeText(getApplicationContext(), "created PDF", Toast.LENGTH_LONG).show();
            } catch (DocumentException de) {
                Log.e("PDFCreator", "DocumentException:" + de);
            } finally {
                doc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String LOG_TAG = "PDF GENERATOR";

    File myFile;
    String timeStamp;
    private void createPdf2() throws FileNotFoundException, DocumentException {

        //  File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        File pdfFolder = new File(Environment.getExternalStorageDirectory(), "MyReports");
        Log.d(TAG, "createPdf2: " + pdfFolder);
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i(LOG_TAG, "Pdf Directory created");
        }

        //Create time stamp
        Date date = new Date();
        timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(date);

        myFile = new File(pdfFolder + "/" + timeStamp + ".pdf");
        Log.d(TAG, "createPdf2: " + myFile);

        OutputStream output = new FileOutputStream(myFile);

        //Step 1
        Document document = new Document(PageSize.A4);

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();
        document.add(addTitle(areaId));
        PdfPTable table = new PdfPTable(7); // columns.
        table.setWidthPercentage(100);

        float[] columnWidths = {
                1f, // no
                6f, //name
                2f, //phone
                1f, //connections
                1f, //fees
                1f, //total
                1.5f  //date
        };
        table.setWidths(columnWidths);
        table.addCell("No.");
        table.addCell("Name");
        table.addCell("Mobile");
        table.addCell("NoC");
        table.addCell("Fees");
        table.addCell("Total");
        table.addCell("Date");
        table.setHeaderRows(1);
        Cursor cursor = dbHendler.getAllFromCustAndSTB(areaId);
        int totalfees = 0;
        if (cursor.moveToFirst()) {

            do {
                PdfPCell custno = new PdfPCell(new Paragraph(cursor.getString(cursor.getColumnIndex(DbHendler.KEY_CUST_NO))));
                PdfPCell name = new PdfPCell(new Paragraph(cursor.getString(cursor.getColumnIndex(DbHendler.KEY_NAME))));
                PdfPCell mobile = new PdfPCell(new Paragraph(cursor.getString(cursor.getColumnIndex(DbHendler.KEY_PHONE_NO))));
                PdfPCell NoC = new PdfPCell(new Paragraph("1"));
                PdfPCell fees = new PdfPCell(new Paragraph(cursor.getString(cursor.getColumnIndex(DbHendler.KEY_FEES))));
                PdfPCell total = new PdfPCell(new Paragraph(cursor.getString(cursor.getColumnIndex(DbHendler.KEY_BALANCE))));
                PdfPCell rdate = new PdfPCell(new Paragraph(""));

                table.addCell(custno);
                table.addCell(name);
                table.addCell(mobile);
                table.addCell(NoC);
                table.addCell(fees);
                table.addCell(total);
                table.addCell(rdate);
                int addfees = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbHendler.KEY_BALANCE)));
                if(addfees>0){ //not add advance collected fees
                    totalfees = totalfees +addfees;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("Total");
        table.addCell(String.valueOf(totalfees));
        table.addCell("");
        document.add(table);
        //Step 4 Add content
        //document.add(new Paragraph("HEEELO"));
        //document.add(new Paragraph("MEEEELO"));
        //Step 5: Close the document
        document.close();
        viewPdf();
    }
    public  Paragraph addTitle(int areaid){
      Area area=  dbHendler.getArea(areaid);
        String areaname =area.get_areaName();

        Font fontbold = FontFactory.getFont("Times-Roman", 20, Font.BOLD);
        Paragraph p = new Paragraph("Area: "+areaname+" ("+timeStamp+")", fontbold);
        p.setSpacingAfter(20);
        p.setAlignment(1); // Center
        return p;
    }
    private void viewPdf() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


}
