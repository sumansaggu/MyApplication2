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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DbHendler extends SQLiteOpenHelper {

    //region All Static variables
    // Database Version
    String TAG = "MyApp_dbhendler";
    private static final int DATABASE_VER = 28;


    //DATABASE NAME
    private static final String DATABASE_NAME = "myInfoManager.db";


    //table name
    private static final String TABLE_PERSON_INFO = "personInfo";
    private static final String TABLE_FEES = "fees";
    private static final String TABLE_STB = "stbRecord";
    public static final String TABLE_EXTRAS = "extras";
    public static final String TABLE_AREA = "area";


    //Table columns names
    public static final String KEY_ID = "_id";           //common for customer, fees, extras table

    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE_NO = "phone_no";
    public static final String KEY_CUST_NO = "cust_no";
    public static final String KEY_FEES = "fees";
    public static final String KEY_BALANCE = "balance";
    public static final String KEY_AREA = "area";
    public static final String KEY_STBID = "stbid";

    public static final String KEY_SN = "serialNo";// stb record
    public static final String KEY_VC = "vcNo";
    public static final String KEY_STATUS = "status";
    public static final String KEY_ASSIGNED = "assigned";


    public static final String KEY_NO = "NO";                //for fees table
    public static final String KEY_RECIEPT = "reciept";
    public static final String KEY_DATE = "recieved_on";
    public static final String KEY_REMARK = "remark";

    public static final String KEY_MONTH_ENDED = "month";   //table extra

    public static final String KEY_AREANO = "no";
    public static final String KEY_AREANAME = "area_name";


    String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PERSON_INFO + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT,"
            + KEY_PHONE_NO + " TEXT, " + KEY_CUST_NO + " REAL, " + KEY_FEES + " INTEGER, " + KEY_BALANCE + " INTEGER, " + KEY_AREA + " INTEGER, " + KEY_STBID + " INTEGER DEFAULT 0 " + ")";

    String CREATE_FEES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FEES + "("
            + KEY_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ID + " INTEGER, " +
            KEY_RECIEPT + " INTEGER, " + KEY_DATE + " DATETIME, " + KEY_REMARK + " TEXT " + ")";

    String CREATE_STB_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_STB + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_SN + " TEXT, " + KEY_VC + " TEXT, " + KEY_STATUS + " TEXT, " + KEY_ASSIGNED + " INTEGER DEFAULT 0 " + ")";

    String CREATE_EXTRAS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EXTRAS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MONTH_ENDED + " DATETIME " + ")";

    String CREATE_AREA_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_AREA + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_AREANO + " INTEGER, " + KEY_AREANAME + " TEXT " + ")";

    //endregion


    public DbHendler(Context context, String name,
                     SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VER);
    }

    // Creating Tables
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_FEES_TABLE);
        db.execSQL(CREATE_STB_TABLE);
        db.execSQL(CREATE_EXTRAS_TABLE);
        db.execSQL(CREATE_AREA_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_FEES_TABLE);
        db.execSQL(CREATE_EXTRAS_TABLE);
        db.execSQL(CREATE_STB_TABLE);
        db.execSQL(CREATE_AREA_TABLE);

        db.execSQL("ALTER TABLE " + TABLE_PERSON_INFO + " RENAME TO TempOldTablePerson"); // rename temporary
        db.execSQL("ALTER TABLE " + TABLE_FEES + " RENAME TO TempOldTableFees");
        db.execSQL("ALTER TABLE " + TABLE_STB + " RENAME TO TempOldTableSTB");
        db.execSQL("ALTER TABLE " + TABLE_EXTRAS + " RENAME TO TempOldTableExtras");
        db.execSQL("ALTER TABLE " + TABLE_AREA + " RENAME TO TempOldTableArea");

        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_FEES_TABLE);
        db.execSQL(CREATE_EXTRAS_TABLE);
        db.execSQL(CREATE_STB_TABLE);
        db.execSQL(CREATE_AREA_TABLE);


        db.execSQL("INSERT INTO " + TABLE_PERSON_INFO + "(" + KEY_ID + ", " + KEY_NAME + ", " + KEY_PHONE_NO + ", " + KEY_CUST_NO + ", " + KEY_FEES + ", " + KEY_BALANCE + ", " + KEY_AREA + ") " +
                "SELECT " + KEY_ID + ", " + KEY_NAME + ", " + KEY_PHONE_NO + ", " + KEY_CUST_NO + ", " + KEY_FEES + ", " + KEY_BALANCE + ", " + KEY_AREA + " FROM TempOldTablePerson");

        db.execSQL("INSERT INTO " + TABLE_FEES + "(" + KEY_NO + ", " + KEY_ID + ", " + KEY_RECIEPT + ", " + KEY_DATE + ") " +
                "SELECT " + KEY_NO + ", " + KEY_ID + ", " + KEY_RECIEPT + ", " + KEY_DATE + "  FROM TempOldTableFees");

        db.execSQL("INSERT INTO " + TABLE_STB + "(" + KEY_ID + ", " + KEY_SN + ", " + KEY_VC + ", " + KEY_STATUS + ") " +
                "SELECT " + KEY_ID + ", " + KEY_SN + ", " + KEY_VC + ", " + KEY_STATUS + "  FROM TempOldTableSTB");

        db.execSQL("INSERT INTO " + TABLE_EXTRAS + "(" + KEY_ID + ", " + KEY_MONTH_ENDED + ") " +
                "SELECT " + KEY_ID + ", " + KEY_MONTH_ENDED + "  FROM TempOldTableExtras");

        db.execSQL("INSERT INTO " + TABLE_AREA + "(" + KEY_ID + ", " + KEY_AREANO + ", " + KEY_AREANAME + ") " +
                "SELECT " + KEY_ID + ", " + KEY_AREANO + ", " + KEY_AREANAME + "  FROM TempOldTableArea");


        db.execSQL("DROP TABLE TempOldTablePerson");
        db.execSQL("DROP TABLE TempOldTableFees");
        db.execSQL("DROP TABLE TempOldTableSTB");
        db.execSQL("DROP TABLE TempOldTableExtras");
        db.execSQL("DROP TABLE TempOldTableArea");
        //  Log.d(TAG," table deleted");
        //  db.execSQL(CREATE_FEES_TABLE);
        //  Log.d(TAG,"table created");
        // Create tables again
        onCreate(db);
    }


    // getting to list view
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String columns[] = {KEY_ID, KEY_NAME, KEY_PHONE_NO, KEY_CUST_NO, KEY_FEES, KEY_BALANCE};
        Cursor cursor = db.query(TABLE_PERSON_INFO, columns, null, null, null, null, KEY_CUST_NO);


        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }

    // getting to list view

    int countSTBs;
    int countSTBsUA;

    public Cursor getAllSTBs() {
        SQLiteDatabase db = this.getReadableDatabase();
        String columns[] = {KEY_ID, KEY_SN, KEY_VC, KEY_STATUS, KEY_ASSIGNED};
        Cursor cursor = db.query(TABLE_STB, columns, null, null, null, null, KEY_ID);

        if (cursor != null) {
            countSTBs = cursor.getCount();
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }

    public Cursor getUnAssignedSTBs() {
        SQLiteDatabase db = this.getReadableDatabase();
        String columns[] = {KEY_ID, KEY_SN, KEY_VC, KEY_STATUS, KEY_ASSIGNED};
        // String where=KEY_ASSIGNED >=0;
        Cursor cursor = db.query(TABLE_STB, columns, KEY_ASSIGNED + " = " + 0, null, null, null, KEY_ID);


        if (cursor != null) {
            countSTBsUA = cursor.getCount();
            Log.d(TAG, "" + countSTBsUA);
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }


    //region serarch person to list
    public Cursor searchPersonToList(String namesearch) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PERSON_INFO + " WHERE " + KEY_NAME + "  LIKE '%" + namesearch + "%';";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }

    //endregion
    //region serarch person to list
    public Cursor getLargerBalance() {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_PERSON_INFO + " ORDER BY " + KEY_BALANCE + " DESC ;";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }
    //endregion


    //region serarch person
    public List<PersonInfo> searchPerson() {
        List<PersonInfo> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PERSON_INFO + " WHERE " + KEY_NAME + "  LIKE '%yy%';";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                PersonInfo personinfo = new PersonInfo();
                personinfo.setID(Integer.parseInt(cursor.getString(0)));
                personinfo.setName(cursor.getString(1));
                personinfo.setPhoneNumber(cursor.getString(2));
                personinfo.setCustNo(Integer.parseInt(cursor.getString(3)));
                personinfo.setFees(Integer.parseInt(cursor.getString(4)));
                // Adding contact to list
                list.add(personinfo);
            } while (cursor.moveToNext());
        }
        return list;
    }
    //endregion

    //region getFeesToList
    public Cursor getFeesToList(int id) {
        String custmor = "" + id;
        Log.d(TAG, custmor);
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_NO, KEY_ID, KEY_RECIEPT, KEY_DATE, KEY_REMARK};
        String[] selArgs = {custmor};

        Cursor cursor = db.query(TABLE_FEES, columns, KEY_ID + " = '" + custmor + "'", null, null, null, KEY_DATE + " ASC");

        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }
    //endregion


    //region addingh new person
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
    //endregion

    public void AddNewStb(STB stb) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SN, stb.getSerialNo());
        values.put(KEY_VC, stb.getVcNo());
        values.put(KEY_STATUS, stb.getStatus());
        db.insert(TABLE_STB, null, values);
        db.close();

    }

    //region ADD FEES TO FEES TABLE
    public void addFees(Fees fees) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, fees.getId());
        values.put(KEY_RECIEPT, fees.getFees());
        values.put(KEY_DATE, fees.getDate());
        values.put(KEY_REMARK, fees.getRemark());
        db.insert(TABLE_FEES, null, values);
        db.close();
    }
    //endregion

    //region Updating Person
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
    //endregion

    public int updateStbRecord(STB stb) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SN, stb.getSerialNo());
        values.put(KEY_VC, stb.getVcNo());
        values.put(KEY_STATUS, stb.getStatus());
        return db.update(TABLE_STB, values, KEY_ID + "=?", new String[]{String.valueOf(stb.getId())});

    }

    public int assignSTB(STB stb) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ASSIGNED, stb.getAssigned());
        return db.update(TABLE_STB, values, KEY_ID + " =? ", new String[]{String.valueOf(stb.getId())});

    }

    public int stbID(int custid, int stbId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STBID, stbId);
        return db.update(TABLE_PERSON_INFO, values, KEY_ID + " =? ", new String[]{String.valueOf(custid)});
    }



    //region Updating Balance
    public int updateBalance(PersonInfo personInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BALANCE, personInfo.get_balance());
        //updating row
        return db.update(TABLE_PERSON_INFO, values, KEY_ID + "=?", new String[]{String.valueOf(personInfo.getID())});
    }
    //endregion

    //region delete a person

    public void deletePerson(int ID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERSON_INFO + " WHERE " + KEY_ID + "=\"" + ID + "\";");
        db.execSQL("DELETE FROM " + TABLE_FEES + " WHERE " + KEY_ID + "=\"" + ID + "\"");
        db.close();
    }
    //endregion

    //region Reading a Row  Getting single contact

    public PersonInfo getCustInfo(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String columns[] = {KEY_ID, KEY_NAME, KEY_PHONE_NO, KEY_CUST_NO, KEY_FEES, KEY_BALANCE};
        Cursor cursor = db.query(TABLE_PERSON_INFO, columns, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        PersonInfo info = new PersonInfo(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getFloat(3),
                cursor.getInt(4),
                cursor.getInt(5));

        // String name = info.getName().toString().trim();           //only to
        //  String phone = info.getPhoneNumber().toString().trim();
        //  int custNo = info.get_cust_no();
        //  int fees = info.get_fees();                          // show
        //  Log.d("TAG", name + "  " + phone + " selected for edit");  // the log for selected item
        //return info
        return info;
    }
    //endregion


    public STB getSTBInfo(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] columns = {KEY_ID, KEY_SN, KEY_VC, KEY_STATUS};
        Cursor cursor = db.query(TABLE_STB, columns, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        STB stb = new STB(cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));
        return stb;
    }
    public int getSTBID(int id)
    {
        int stbID=0;
        SQLiteDatabase db = getReadableDatabase();
        String columns[] = {KEY_ID, KEY_STBID};
        Cursor cursor = db.query(TABLE_PERSON_INFO, columns, KEY_ID + " =? ", new String[]{String.valueOf(id)},null,null,null,null);
           if(cursor !=null){
              cursor.moveToFirst();
               int stbIDColumn = cursor.getColumnIndex(KEY_STBID);
                stbID= cursor.getInt(stbIDColumn);
          }
        return stbID;
    }


    //region end of month
    public void endOfMonth(Context context) {
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
            Toast.makeText(context, "Month ended fees updated for all", Toast.LENGTH_LONG).show();
            entryTomonthTable();
        }

        cursor.close();
    }

    public void entryTomonthTable() {
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(calender.getTime());
        ContentValues values = new ContentValues();
        String selectQuery = "SELECT  * FROM " + TABLE_EXTRAS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //int id_column = cursor.getColumnIndex(KEY_ID);
        // int id = cursor.getInt(id_column);
        values.put(KEY_MONTH_ENDED, formattedDate);
        db.insert(TABLE_EXTRAS, null, values);
        Log.d(TAG, "add to extra table");
    }

    public void checkmonthchange(Context Context) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_EXTRAS;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToLast()) {
            int id = (Integer.parseInt(cursor.getString(0)));
            String month = (cursor.getString(1));
            Toast.makeText(Context, "Month was ended at: " + month, Toast.LENGTH_LONG).show();
        }
        cursor.close();

    }
    //endregion

    //region backupdatabase

    public void copyDbToExternalStorage(Context context, String dbName) {
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        String formattedDate = df.format(calender.getTime());
        Log.d(TAG, "" + formattedDate);
        File folder = new File(Environment.getExternalStorageDirectory() + "/MyBackup");

        if (!folder.exists()) {
            folder.mkdir();
            Toast.makeText(context, "Backup Directory Created", Toast.LENGTH_LONG).show();
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
            Toast.makeText(context, "Database backup created at" + sdcardFile, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "ghfhfghfgh" + e.toString());
        }


    }
    //endregion

    //region Restore database
    public void restoreDBfile(Context context, String dbName) {
        // Calendar calender = Calendar.getInstance();
        // SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        // String formattedDate = df.format(calender.getTime());
        try {
            File name = context.getDatabasePath(dbName);
            File sdcardFile = new File(Environment.getExternalStorageDirectory(), "/" + DbHendler.DATABASE_NAME); //myInfoManager.db
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
            Toast.makeText(context, "Database restored", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            // Log.e(TAG, e.toString());
            Toast.makeText(context, " " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    //endregion

    //region get total balance
    public int totalBalance() {

        String selectQuery = "SELECT " + KEY_BALANCE + " FROM " + TABLE_PERSON_INFO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int sum = 0;
        // looping through all rows
        if (cursor.moveToFirst()) {
            do {
                int balanceColumn = cursor.getColumnIndex(KEY_BALANCE);
                int balance = cursor.getInt(balanceColumn);
                sum = balance + sum;
            } while (cursor.moveToNext());
            Log.d(TAG, "" + sum);
        }
        cursor.close();
        return sum;
    }
    //endregion

    //region cellecton between dates
    public int colectionBwtwoDates(String from, String to) {
        String selectQuery = "SELECT * FROM " + TABLE_FEES + " WHERE " + KEY_DATE + " BETWEEN '" + from + "' AND '" + to + "' ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows
        Log.d(TAG, selectQuery);
        int sum = 0;

        if (cursor.moveToFirst()) {
            do {
                int datecolumn = cursor.getColumnIndex(KEY_DATE);
                String date = cursor.getString(datecolumn);
                int feesColumn = cursor.getColumnIndex(KEY_RECIEPT);
                int fees = cursor.getInt(feesColumn);
                Log.d(TAG, "DATE: " + date + " " + "fees: " + fees);
                sum = fees + sum;

            } while (cursor.moveToNext());
            Log.d(TAG, "" + sum);

        }
        cursor.close();
        return sum;
    }

    //endregion
    //region Getting All Contacts to log
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
                personinfo.setCustNo(Float.parseFloat(cursor.getString(3)));
                personinfo.setFees(Integer.parseInt(cursor.getString(4)));
                // Adding contact to list
                contactList.add(personinfo);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }
    //endregion

    //region view fees table to log
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


    //endregion


}

