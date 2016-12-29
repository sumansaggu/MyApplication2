package com.example.saggu.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DbHendler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    String TAG = "MyApp_dbhendler";
    private static final int DATABASE_VER = 11;


    //DATABASE NAME
    private static final String DATABASE_NAME = "myInfoManager.db";


    //table name
    private static final String TABLE_PERSON_INFO = "personInfo";
    private static final String TABLE_FEES = "fees";

    //Table columns names
    public static final String KEY_ID = "_id";           //common for customer and fees table
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE_NO = "phone_no";
    public static final String KEY_CUST_NO = "cust_no";
    public static final String KEY_FEES = "fees";
    public static final String KEY_BALANCE = "balance";
    public static final String KEY_NO = "NO";                //for
    public static final String KEY_RECIEPT = "reciept";       //fees
    public static final String KEY_DATE = "recieved_on";      //table

    String CREATE_FEES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FEES + "("
            + KEY_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ID + " INTEGER, " +
            KEY_RECIEPT + " INTEGER, " + KEY_DATE + " DATETIME " + ")";

    String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PERSON_INFO + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT,"
            + KEY_PHONE_NO + " TEXT, " + KEY_CUST_NO + " INTEGER, " + KEY_FEES + " INTEGER, " + KEY_BALANCE + " INTEGER " + ")";


    public DbHendler(Context context, String name,
                     SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VER);
    }

    // Creating Tables
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_FEES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL("DROP TABLE FEES_TABLE" );
        //  Log.d(TAG," table deleted");
        //  db.execSQL(CREATE_FEES_TABLE);
        //  Log.d(TAG,"table created");
        // Create tables again
        onCreate(db);
    }

    // getting to list view
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERSON_INFO, new String[]{KEY_ID, KEY_NAME,
                KEY_PHONE_NO, KEY_CUST_NO, KEY_FEES, KEY_BALANCE}, null, null, KEY_CUST_NO, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }

    public Cursor getFeesToList(int id) {
        String custmor = "" + id;
        Log.d(TAG, custmor);
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_NO, KEY_ID, KEY_RECIEPT, KEY_DATE};
        String[] selArgs = {custmor};

        Cursor cursor = db.query(TABLE_FEES, columns, KEY_ID + " = '" + custmor + "'", null, null, null, KEY_DATE + " ASC");

        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }


    // Getting All Contacts to log
    public List<PersonInfo> getAllContacts() {
        List<PersonInfo> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PERSON_INFO + " ORDER BY " + KEY_CUST_NO + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PersonInfo personinfo = new PersonInfo();
                personinfo.setID(Integer.parseInt(cursor.getString(0)));
                personinfo.setName(cursor.getString(1));
                personinfo.setPhoneNumber(cursor.getString(2));
                personinfo.setCustNo(Integer.parseInt(cursor.getString(3)));
                personinfo.setFees(Integer.parseInt(cursor.getString(4)));
                // Adding contact to list
                contactList.add(personinfo);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public List<Fees> viewFees() {
        List<Fees> feeslist = new ArrayList<Fees>();
        String selectQuery = "SELECT  * FROM " + TABLE_FEES;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Fees fees = new Fees();
                fees.setNo(Integer.parseInt(cursor.getString(0)));
                fees.setID(Integer.parseInt(cursor.getString(1)));
                fees.setFees(Integer.parseInt(cursor.getString(2)));
                fees.setDate(cursor.getString(3));
                // Adding fees to list
                feeslist.add(fees);
            } while (cursor.moveToNext());
        }
        // return contact list
        return feeslist;
    }

    // Adding new contact
    public void addPerson(PersonInfo personInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, personInfo.getName());               //person name
        values.put(KEY_PHONE_NO, personInfo.getPhoneNumber());    // phone no.
        values.put(KEY_CUST_NO, personInfo.get_cust_no());        // customer no.
        values.put(KEY_FEES, personInfo.get_fees());              // fees
        values.put(KEY_BALANCE, personInfo.get_balance());        // balance
        //Insert row
        db.insert(TABLE_PERSON_INFO, null, values);
        db.close(); //close database
    }


    // ADD FEES TO FEES TABLE
    public void addFees(Fees fees) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, fees.getId());
        values.put(KEY_RECIEPT, fees.getFees());
        values.put(KEY_DATE, fees.getDate());
        db.insert(TABLE_FEES, null, values);
        db.close();
        Log.d(TAG, "fees addedddd");
    }


    //Updating a Record
    public int updateInfo(PersonInfo personInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, personInfo.getName());
        values.put(KEY_PHONE_NO, personInfo.getPhoneNumber());
        values.put(KEY_CUST_NO, personInfo.get_cust_no());
        values.put(KEY_FEES, personInfo.get_fees());
        values.put(KEY_BALANCE, personInfo.get_balance());
        //updating row
        return db.update(TABLE_PERSON_INFO, values, KEY_ID + "=?", new String[]{String.valueOf(personInfo.getID())});

    }


    //Updating a Record
    public int updateBalance(PersonInfo personInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BALANCE, personInfo.get_balance());
        //updating row
        return db.update(TABLE_PERSON_INFO, values, KEY_ID + "=?", new String[]{String.valueOf(personInfo.getID())});
    }


    // delete a person
    public void deletePerson(int ID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERSON_INFO + " WHERE " + KEY_ID + "=\"" + ID + "\";");
        db.execSQL("DELETE FROM " + TABLE_FEES + " WHERE " + KEY_ID + "=\"" + ID + "\"");
        db.close();
    }


    //Reading a Row  Getting single contact
    public PersonInfo getInfo(int id) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERSON_INFO, new String[]{
                        KEY_ID,
                        KEY_NAME,
                        KEY_PHONE_NO,
                        KEY_CUST_NO,
                        KEY_FEES,
                        KEY_BALANCE

                },
                KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        PersonInfo info = new PersonInfo(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));

        String name = info.getName().toString().trim();           //only to
        String phone = info.getPhoneNumber().toString().trim();
        int custNo = info.get_cust_no();
        int fees = info.get_fees();                          // show
        Log.d("TAG", name + "  " + phone + " selected for edit");  // the log for selected item


        //return info
        return info;
    }


    public void endOfMonth() {
        String selectQuery = "SELECT  * FROM " + TABLE_PERSON_INFO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {
                int id_column = cursor.getColumnIndex(KEY_ID);
                int id = cursor.getInt(id_column);
                int feesColumn = cursor.getColumnIndex(KEY_FEES);
                int i = cursor.getInt(feesColumn);
                int balanceColumn = cursor.getColumnIndex(KEY_BALANCE);
                int j = cursor.getInt(balanceColumn);

                int k = i + j;
                // Adding contact to list
                Log.d(TAG, "ID:" + id + " " + i + " " + j + "=" + k);
                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_BALANCE, k);
                db.update(TABLE_PERSON_INFO, contentValues, KEY_ID + "=?", new String[]{String.valueOf(id)});
            } while (cursor.moveToNext());
        }
        cursor.close();


    }


    /**
     * Copy the local DB file of an application to the root of external storage directory
     *
     * @param context the Context of application
     * @param dbName  The name of the DB
     */
    public void copyDbToExternalStorage(Context context, String dbName) {
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String formattedDate = df.format(calender.getTime());

        File folder = new File(Environment.getExternalStorageDirectory() + "/MyBackup");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
            Toast.makeText(context, "Backup Directory Created", Toast.LENGTH_LONG).show();
        }
        if (success) {
            // Do something on success
        } else {
            // Do something else on failure
        }

        try {
            File name = context.getDatabasePath(dbName);
            File sdcardFile = new File(Environment.getExternalStorageDirectory(), "/MyBackup/" + formattedDate);//The name of output file
            sdcardFile.createNewFile();
            InputStream inputStream = null;
            OutputStream outputStream = null;
            inputStream = new FileInputStream(name);
            outputStream = new FileOutputStream(sdcardFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            Log.d(TAG,"Backup created at"+ sdcardFile);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void restoreDBfile(Context context, String dbName) {
       // Calendar calender = Calendar.getInstance();
       // SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
       // String formattedDate = df.format(calender.getTime());

        File fromFolder = new File(Environment.getExternalStorageDirectory() + "/MyBackup");


        try {
            File name = context.getDatabasePath(dbName);
            File sdcardFile = new File(Environment.getExternalStorageDirectory(), "/MyBackup/" + DbHendler.DATABASE_NAME); //myInfoManager.db
            name.createNewFile();
            InputStream inputStream = null;
            OutputStream outputStream = null;
            inputStream = new FileInputStream(sdcardFile);
            outputStream = new FileOutputStream(name);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}

 /* public String getData(){

        String [] colums = new String[]{KEY_ID, KEY_NAME, KEY_PHONE_NO};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_PERSON_INFO, colums, null, null, null, null, null);
        String result = "";

        int iRow = c.getColumnIndex(KEY_ID);
        int iName = c.getColumnIndex(KEY_NAME);
        int iContact = c.getColumnIndex(KEY_PHONE_NO);

        for (c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
            result = result + c.getString(iRow) + " " + c.getString(iName) + " " + c.getString(iContact) + "\n";
        }
        db.close();
        return result;
    }
*/


/*
// Getting contacts Count
    public int getCount(){
        String counttotal = "SELECT * FROM "+ TABLE_PERSON_INFO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(counttotal, null);
        cursor.close();
        //return count
        return cursor.getCount();
    }*/


/*
// Deleting single record
    public void deletePerson(PersonInfo info){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_PERSON_INFO, KEY_ID + "=?",
                new String[]{String.valueOf(info.getID())});
        db.close();
    }

*/


/*//print the database as string
    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PERSON_INFO + "WHERE 1";


        //CURSOR POINT TO LOCATION IN RESULT
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("name")) != null) ;{

            }
        }
        db.close();
       return dbString;

    }*/
/*public void deleteallfees() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_FEES);
        db.close();
        Log.d(TAG, "deleted");
    }*/



