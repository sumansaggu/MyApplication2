package com.example.saggu.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DbHendler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    String TAG = "MyApp_dbhendler";
    private static final int DATABASE_VER = 7;


    //DATABASE NAME
    private static final String DATABASE_NAME = "myInfoManager.db";


    //table name
    private static final String TABLE_PERSON_INFO = "personInfo";

    //Table columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE_NO = "phone_no";
    public static final String KEY_CUST_NO = "cust_no";
    public static final String KEY_FEES = "fees";
    public static final String KEY_BALANCE = "balance";


    public DbHendler(Context context, String name,
                     SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VER);
    }

    // Creating Tables
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PERSON_INFO + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT,"
                + KEY_PHONE_NO + " TEXT, " + KEY_CUST_NO + " INTEGER, " + KEY_FEES + " INTEGER, " + KEY_BALANCE + " INTEGER " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON_INFO);
        // Create tables again
        onCreate(db);
    }


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
    // Getting All Contacts to log
    public List<PersonInfo> getAllContacts() {
        List<PersonInfo> contactList = new ArrayList<PersonInfo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PERSON_INFO +" ORDER BY " + KEY_CUST_NO + " ASC" ;
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


    //Updating a Record
    public int updateInfo(PersonInfo personInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, personInfo.getName());
        values.put(KEY_PHONE_NO, personInfo.getPhoneNumber());
        values.put(KEY_CUST_NO,personInfo.get_cust_no());
        values.put(KEY_FEES, personInfo.get_fees());
        values.put(KEY_BALANCE, personInfo.get_balance());

        //updating row

        return db.update(TABLE_PERSON_INFO, values, KEY_ID + "=?", new String[]{String.valueOf(personInfo.getID())});

    }


    //
    public void deletePerson(int ID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERSON_INFO + " WHERE " + KEY_ID + "=\"" + ID + "\";");
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
                cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4),cursor.getInt(5));

        String name = info.getName().toString().trim();           //only to
        String phone = info.getPhoneNumber().toString().trim();
        int custNo =  info.get_cust_no();
        int fees =info.get_fees();                          // show
        Log.d("TAG", name + "  " + phone + " selected for edit");  // the log for selected item


        //return info
        return info;
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



