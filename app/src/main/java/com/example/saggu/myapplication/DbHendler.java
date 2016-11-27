package com.example.saggu.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DbHendler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VER = 3;
   //DATABASE NAME
    private static final String DATABASE_NAME = "myInfoManager.db";
    //table name
    private static final String TABLE_PERSON_INFO ="personInfo";
    //Table columns names
    public static final String KEY_ID =                 "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE_NO = "phone_no";

    public DbHendler(Context context, String name,
                     SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VER);
    }
    // Creating Tables
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PERSON_INFO + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT,"
                + KEY_PHONE_NO + " TEXT )";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON_INFO);
   // Create tables again
       onCreate(db);
    }
    public  Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERSON_INFO, new String[] {KEY_ID, KEY_NAME,
                KEY_PHONE_NO}, null, null, KEY_NAME, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        }
        else
        {
            return null;
        }
    }
    // Adding new contact
    public void addPerson(PersonInfo info){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, info.getName()); //person name
        values.put(KEY_PHONE_NO, info.getPhoneNumber());    // phone no.
        //Insert row
        db.insert(TABLE_PERSON_INFO, null, values);
        db.close(); //close database
    }
    public void deletePerson(int ID) {

        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_PERSON_INFO + " WHERE " + KEY_ID + "=\"" + ID + "\";");

        db.close();
    }
    // Getting All Contacts
    public List<PersonInfo> getAllContacts() {

        List<PersonInfo> contactList = new ArrayList<PersonInfo>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PERSON_INFO;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PersonInfo contact = new PersonInfo();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }
    //Updating Record //Updating single contact
    public int updateInfo(PersonInfo info){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, info.getName());
        values.put(KEY_PHONE_NO, info.getPhoneNumber());
        //updating row
        return db.update(TABLE_PERSON_INFO, values, KEY_ID + "=?",
                new String[]{String.valueOf(info.getID())});
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





       /*
       //Reading a Row  Getting single contact
    public PersonInfo getInfo(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERSON_INFO, new String[]{
                        KEY_ID,
                        KEY_NAME,
                        KEY_PHONE_NO
                },
                KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        PersonInfo info = new PersonInfo(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),cursor.getString(2));
        //return info
        return info;
    }
*/








}
