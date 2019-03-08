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
    private static final int DATABASE_VER = 36;


    private static final File DATABASE_FILE_PATH = Environment.getExternalStorageDirectory();
    //DATABASE NAME
    private static final String DATABASE_NAME = "myInfoManager.db";


    //table name
    private static final String TABLE_PERSON_INFO = "personInfo";
    private static final String TABLE_FEES = "fees";
    private static final String TABLE_STB = "stbRecord";
    public static final String TABLE_EXTRAS = "extras";
    public static final String TABLE_AREA = "area";
    private static final String TABLE_MONTH_END = "month";
    private static final String TABLE_IDPW = "idpw";

    //Table columns names
    public static final String KEY_ID = "_id";           //common for customer, fees, extras table

    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE_NO = "phone_no";
    public static final String KEY_CUST_NO = "cust_no";
    public static final String KEY_FEES = "fees";
    public static final String KEY_BALANCE = "balance";
    public static final String KEY_AREA = "area";
    public static final String KEY_STBID = "stbid";
    public static final String KEY_STARTDATE = "startdate";
    public static final String KEY_STOPDATE = "stopdate";
    public static final String KEY_CONSTATUS = "constatus";
    public static final String KEY_NOC = "no_connections";
    public static final String KEY_PERSONEXTRA1 = "col_13";
    public static final String KEY_PERSONEXTRA2 = "col_14";
    public static final String KEY_NICKNAME = "col_15";
    public static final String KEY_PERSONEXTRA4 = "col_16";
    public static final String KEY_PERSONEXTRA5 = "col_17";
    public static final String KEY_PERSONEXTRA6 = "col_18";


    public static final String KEY_SN = "serialNo";// stb record
    public static final String KEY_VC = "vcNo";
    public static final String KEY_STATUS = "status";
    public static final String KEY_ASSIGNED = "assigned";
    public static final String KEY_STBEXTRA1 = "col_6";
    public static final String KEY_STBEXTRA2 = "col_7";


    public static final String KEY_NO = "NO";                //for fees table
    public static final String KEY_RECIEPT = "reciept";
    public static final String KEY_DATE = "recieved_on";
    public static final String KEY_REMARK = "remark";
    public static final String KEY_FEESEXTRA1 = "col_6";
    public static final String KEY_FEESEXTRA2 = "col_7";


    public static final String KEY_MONTH_ENDED = "month";   //table extra

    public static final String KEY_AREANO = "no";
    public static final String KEY_AREANAME = "area_name";
    public static final String KEY_AREAEXTRA1 = "col_4";
    public static final String KEY_AREAEXTRA2 = "col_5";


    private static final String KEY_MONTH_STATUS = "monthstatus";  //table month
    private static final String KEY_MONTHEXTRA1 = "col_3";
    private static final String KEY_MONTHEXTRA2 = "col_4";


    public static final String KEY_USERID = "no";
    public static final String KEY_USERPASSWORD = "area_name";
    public static final String KEY_IDPWEXTRA1 = "col_4";
    public static final String KEY_IDPWEXTRA2 = "col_5";


    String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PERSON_INFO + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT,"
            + KEY_PHONE_NO + " TEXT, "
            + KEY_CUST_NO + " REAL, "
            + KEY_FEES + " INTEGER, "
            + KEY_BALANCE + " INTEGER, "
            + KEY_AREA + " INTEGER DEFAULT 1, "
            + KEY_STBID + " INTEGER,  "
            + KEY_STARTDATE + " DATETIME DEFAULT 'N/A' , "
            + KEY_STOPDATE + " DATETIME DEFAULT 'N/A', "
            + KEY_CONSTATUS + " TEXT DEFAULT 'ACTIVE', "
            + KEY_NOC + " INTEGER DEFAULT 1, "
            + KEY_PERSONEXTRA1 + " INTEGER DEFAULT 0 , "
            + KEY_PERSONEXTRA2 + " INTEGER DEFAULT 0, "
            + KEY_NICKNAME + " TEXT DEFAULT 'N/A' , "
            + KEY_PERSONEXTRA4 + " TEXT DEFAULT 'N/A' , "
            + KEY_PERSONEXTRA5 + " REAL DEFAULT 0, "
            + KEY_PERSONEXTRA6 + " REAL DEFAULT 0, "


            + " FOREIGN KEY ( " + KEY_STBID + " ) REFERENCES " + TABLE_STB + " ( " + KEY_ID + ") ON DELETE SET NULL " + " )";


    String CREATE_FEES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FEES + "("
            + KEY_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ID + " INTEGER, "
            + KEY_RECIEPT + " INTEGER, "
            + KEY_DATE + " DATETIME, "
            + KEY_REMARK + " TEXT,  "
            + KEY_FEESEXTRA1 + " TEXT DEFAULT 'N/A', "
            + KEY_FEESEXTRA2 + " INTEGER DEFAULT 0 )";

    String CREATE_STB_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_STB + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_SN + " TEXT, "
            + KEY_VC + " TEXT, "
            + KEY_STATUS + " TEXT, "
            + KEY_ASSIGNED + " INTEGER DEFAULT 0, "
            + KEY_STBEXTRA1 + " TEXT DEFAULT 'N/A' , "
            + KEY_STBEXTRA2 + " INTEGER DEFAULT 0 )";

    String CREATE_EXTRAS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EXTRAS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_MONTH_ENDED + " DATETIME " + ")";


    String CREATE_MONTHEND_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MONTH_END + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_MONTH_STATUS + " TEXT, "
            + KEY_MONTHEXTRA1 + " TEXT DEFAULT 'N/A', "
            + KEY_MONTHEXTRA2 + " INTEGER DEFAULT 0 "
            + " )";
    String CREATE_AREA_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_AREA + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_AREANO + " INTEGER, "
            + KEY_AREANAME + " TEXT, "
            + KEY_AREAEXTRA1 + " TEXT DEFAULT 'N/A', "
            + KEY_AREAEXTRA2 + " INTEGER DEFAULT 0"
            + ")";
    String CREATE_IDPW_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_IDPW + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_USERID + " TEXT, "
            + KEY_USERPASSWORD + " TEXT, "
            + KEY_AREAEXTRA1 + " TEXT DEFAULT 'N/A', "
            + KEY_AREAEXTRA2 + " INTEGER DEFAULT 0"
            + ")";

    String DEFAULT_ROW_FOR_MONTH_TABLE = "INSERT INTO " + TABLE_MONTH_END + " VALUES(1, 'DONE', 'default' ,0)";
    String DEFAULT_ROW_FOR_AREA_TABLE = " INSERT INTO " + TABLE_AREA + " VALUES(1,1, 'All','default',0)";

    private static String path = externalStrorage() + "/myInfoManager.db";
    //endregion


    public DbHendler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, path, factory, DATABASE_VER);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }


    // Creating Tables
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_FEES_TABLE);
        db.execSQL(CREATE_STB_TABLE);
        db.execSQL(CREATE_EXTRAS_TABLE);
        db.execSQL(CREATE_AREA_TABLE);
        db.execSQL(CREATE_MONTHEND_TABLE);
        db.execSQL(CREATE_IDPW_TABLE);
        try {
            db.execSQL(DEFAULT_ROW_FOR_MONTH_TABLE);
            db.execSQL(DEFAULT_ROW_FOR_AREA_TABLE);
        } catch (RuntimeException ex) {
            Log.d(TAG, "onCreate: " + ex);
        }

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
        db.execSQL("ALTER TABLE " + TABLE_MONTH_END + " RENAME TO TempOldTableMonthEnd");

        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_FEES_TABLE);
        db.execSQL(CREATE_EXTRAS_TABLE);
        db.execSQL(CREATE_STB_TABLE);
        db.execSQL(CREATE_AREA_TABLE);
        db.execSQL(CREATE_MONTHEND_TABLE);


        db.execSQL("INSERT INTO " + TABLE_PERSON_INFO + "(" + KEY_ID + ", " + KEY_NAME + ", " + KEY_PHONE_NO + ", " + KEY_CUST_NO + ", " + KEY_FEES + ", " + KEY_BALANCE + ", " + KEY_AREA + ", " + KEY_STBID + ", " + KEY_PERSONEXTRA1 + ", " + KEY_PERSONEXTRA2 + ", " + KEY_NICKNAME + ", " + KEY_PERSONEXTRA4 + ", " + KEY_PERSONEXTRA5 + ", " + KEY_PERSONEXTRA6 + ") " +
                "SELECT " + KEY_ID + ", " + KEY_NAME + ", " + KEY_PHONE_NO + ", " + KEY_CUST_NO + ", " + KEY_FEES + ", " + KEY_BALANCE + ", " + KEY_AREA + ", " + KEY_STBID + ", " + KEY_PERSONEXTRA1 + ", " + KEY_PERSONEXTRA2 + ", " + KEY_NICKNAME + ", " + KEY_PERSONEXTRA4 + ", " + KEY_PERSONEXTRA5 + ", " + KEY_PERSONEXTRA6 + " FROM TempOldTablePerson");

        db.execSQL("INSERT INTO " + TABLE_FEES + "(" + KEY_NO + ", " + KEY_ID + ", " + KEY_RECIEPT + ", " + KEY_DATE + ", " + KEY_REMARK + ", " + KEY_FEESEXTRA1 + ", " + KEY_FEESEXTRA2 + ") " +
                "SELECT " + KEY_NO + ", " + KEY_ID + ", " + KEY_RECIEPT + ", " + KEY_DATE + ", " + KEY_REMARK + ", " + KEY_FEESEXTRA1 + ", " + KEY_FEESEXTRA2 + "  FROM TempOldTableFees");

        db.execSQL("INSERT INTO " + TABLE_STB + "(" + KEY_ID + ", " + KEY_SN + ", " + KEY_VC + ", " + KEY_STATUS + ", " + KEY_ASSIGNED + ", " + KEY_STBEXTRA1 + ", " + KEY_STBEXTRA2 + ") " +
                "SELECT " + KEY_ID + ", " + KEY_SN + ", " + KEY_VC + ", " + KEY_STATUS + ", " + KEY_ASSIGNED + ", " + KEY_STBEXTRA1 + ", " + KEY_STBEXTRA2 + "  FROM TempOldTableSTB");
        // "SELECT *  FROM TempOldTableSTB");
        db.execSQL("INSERT INTO " + TABLE_EXTRAS + "(" + KEY_ID + ", " + KEY_MONTH_ENDED + ") " +
                "SELECT " + KEY_ID + ", " + KEY_MONTH_ENDED + "  FROM TempOldTableExtras");

        db.execSQL("INSERT INTO " + TABLE_AREA + "(" + KEY_ID + ", " + KEY_AREANO + ", " + KEY_AREANAME + ", " + KEY_AREAEXTRA1 + ", " + KEY_AREAEXTRA2 + ") " +
                "SELECT " + KEY_ID + ", " + KEY_AREANO + ", " + KEY_AREANAME + ", " + KEY_AREAEXTRA1 + ", " + KEY_AREAEXTRA2 + "  FROM TempOldTableArea");

        db.execSQL("INSERT INTO " + TABLE_MONTH_END + "(" + KEY_ID + ", " + KEY_MONTH_STATUS + ", " + KEY_MONTHEXTRA1 + ", " + KEY_MONTHEXTRA2 + ") " +
                " SELECT " + KEY_ID + ", " + KEY_MONTH_STATUS + ", " + KEY_MONTHEXTRA1 + ", " + KEY_MONTHEXTRA2 + " FROM  TempOldTableMonthEnd");


        db.execSQL("DROP TABLE TempOldTablePerson");
        db.execSQL("DROP TABLE TempOldTableFees");
        db.execSQL("DROP TABLE TempOldTableSTB");
        db.execSQL("DROP TABLE TempOldTableExtras");
        db.execSQL("DROP TABLE TempOldTableArea");
        db.execSQL("DROP TABLE TempOldTableMonthEnd");
        //  Log.d(TAG," table deleted");
        //  db.execSQL(CREATE_FEES_TABLE);
        //  Log.d(TAG,"table created");
        // Create tables again
        onCreate(db);
    }

    private static String externalStrorage() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/MyCableData");
        if (!folder.exists()) {
            folder.mkdir();
        }
        String path = new String(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyCableData");
        return path;
    }

    public String monthFlag() {
        String flag = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String columns[] = {KEY_ID, KEY_MONTH_STATUS};
        Cursor cursor = db.query(TABLE_MONTH_END, columns, null, null, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                flag = cursor.getString(cursor.getColumnIndex(KEY_MONTH_STATUS));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    // getting to list view
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String columns[] = {KEY_ID, KEY_NAME, KEY_PHONE_NO, KEY_CUST_NO, KEY_FEES, KEY_BALANCE, KEY_STBID};
        Cursor cursor = db.query(TABLE_PERSON_INFO, columns, null, null, null, null, KEY_CUST_NO);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }

    public Cursor getAllFromCustAndSTB(int areaId) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query;
        if (areaId == 1) {
            query = "SELECT personinfo._id,name,phone_no,cust_no,constatus, fees, balance, serialNo, " + KEY_NICKNAME + " FROM PERSONINFO LEFT JOIN STBRECORD ON STBRECORD._ID = PERSONINFO.STBID ORDER BY cust_no ASC ";
        } else {
            query = "SELECT personinfo._id,name,phone_no,cust_no,constatus, fees,balance, serialNo, " + KEY_NICKNAME + " FROM PERSONINFO LEFT JOIN STBRECORD ON STBRECORD._ID = PERSONINFO.STBID WHERE personInfo.area = " + areaId + " ORDER BY cust_no ASC ";
        }
        //    Log.d(TAG, "GET ALL:" + query);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }


    }

    public Cursor getAreasToList() {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM AREA";
        //  Log.d(TAG, "" + query);
        Cursor cursor = db.rawQuery(query, null);
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

    public Cursor getUnAssignedSTBs() { // for STB Dialog to assign
        SQLiteDatabase db = this.getReadableDatabase();
        String columns[] = {KEY_ID, KEY_SN, KEY_VC, KEY_STATUS, KEY_ASSIGNED};
        // String where=KEY_ASSIGNED >=0;
        Cursor cursor = db.query(TABLE_STB, columns, KEY_ASSIGNED + " = " + 0, null, null, null, KEY_ID);
        if (cursor != null) {
            countSTBsUA = cursor.getCount();
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }


    //region serarch person to list
    public Cursor searchPersonToList(String namesearch) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT personinfo._id,name,phone_no,cust_no,constatus, fees,balance, serialNo, " + KEY_NICKNAME + " FROM " + TABLE_PERSON_INFO + " LEFT JOIN " + TABLE_STB + " ON STBRECORD._ID = PERSONINFO.STBID  WHERE " + KEY_NAME + "  LIKE '%" + namesearch + "%';";
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
    public Cursor searchPersonByNickName(String namesearch) {
        Log.d(TAG, "searchPersonByNickName: executed");
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT personinfo._id,name,phone_no,cust_no,constatus, fees,balance, serialNo, " + KEY_NICKNAME + " FROM " + TABLE_PERSON_INFO + " LEFT JOIN " + TABLE_STB + " ON STBRECORD._ID = PERSONINFO.STBID  WHERE " + KEY_NICKNAME + "  LIKE '%" + namesearch + "%';";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }//region serarch person to list

    public Cursor searchBySTBSN(String namesearch) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT personinfo._id,name,phone_no,cust_no,constatus, fees,balance, serialNo, " + KEY_NICKNAME + " FROM " + TABLE_PERSON_INFO + " LEFT JOIN " + TABLE_STB + " ON STBRECORD._ID = PERSONINFO.STBID  WHERE " + KEY_SN + "  LIKE '%" + namesearch + "%';";
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
    public Cursor searchPersonByMobileNo(String namesearch) {
        Log.d(TAG, "searchPersonByNickName: executed");
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT personinfo._id,name,phone_no,cust_no,constatus, fees,balance, serialNo, " + KEY_NICKNAME + " FROM " + TABLE_PERSON_INFO + " LEFT JOIN " + TABLE_STB + " ON STBRECORD._ID = PERSONINFO.STBID  WHERE " + KEY_PHONE_NO + "  LIKE '%" + namesearch + "%';";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }




    //region serarch person to list
    public Cursor searchSTBToList(String STBsearch) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_STB + " WHERE " + KEY_SN + "  LIKE '%" + STBsearch + "%';";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }
    //endregion

    //region serarch STB to list
    public Cursor getLargerBalance() {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT personinfo._id,name,phone_no,cust_no,constatus,fees,balance, " + KEY_NICKNAME + ", serialNo " +
                "FROM " + TABLE_PERSON_INFO +
                " LEFT JOIN " + TABLE_STB + " ON STBRECORD._ID = PERSONINFO.STBID " +
                "ORDER BY " + KEY_BALANCE + " DESC ;";

        Log.d(TAG, "getLargerBalance: " + query);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }
    //endregion
    public Cursor listforBtwtwoDates(String from, String to) {
        String selectQuery = "SELECT "+TABLE_FEES+"."+KEY_ID+", "+KEY_RECIEPT+",curBalance, "+KEY_DATE+", "+KEY_REMARK+", "+KEY_NAME+", "+TABLE_PERSON_INFO+"._id"+
                " FROM " + TABLE_FEES +
                " LEFT JOIN " + TABLE_PERSON_INFO + " ON " + TABLE_PERSON_INFO + "._id =" + TABLE_FEES + "._id" +
                " WHERE " + KEY_DATE + " BETWEEN '" + from + "' AND '" + to+"'" ;
        Log.d(TAG, "listforBtwtwoDates: " + selectQuery);                                                                                                                           //   LEFT JOIN STBRECORD ON STBRECORD._ID = PERSONINFO.STBID ORDER BY cust_no ASC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;

        } else {
            return null;
        }


    }


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
        String[] columns = {KEY_NO, KEY_ID, KEY_DATE, KEY_RECIEPT, "lBalance", "curBalance", KEY_REMARK};
        // String[] selArgs = {custmor};

        Cursor cursor = db.query(TABLE_FEES, columns, KEY_ID + " = '" + custmor + "'", null, null, null, KEY_DATE + " ASC");

        if (cursor != null) {
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }
    }

    //endregion
    public List<Fees> getFeesForMsg(int id) {
        List<Fees> feesDetail = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_NO, KEY_ID, KEY_DATE, KEY_RECIEPT, "lBalance", "curBalance", KEY_REMARK};
        Cursor cursor = db.query(TABLE_FEES, columns, KEY_ID + " = '" + id + "'", null, null, null, KEY_DATE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Fees fees = new Fees();
                fees.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                fees.setFees(cursor.getInt(cursor.getColumnIndex(KEY_RECIEPT)));
                feesDetail.add(fees);
            } while (cursor.moveToNext());
        } else Log.d(TAG, "getFeesForMsg: cursor is null");

        return feesDetail;
    }

    public Cursor getIDPW() {

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_ID, KEY_USERID, KEY_USERPASSWORD};
        Cursor cursor = db.query(TABLE_IDPW, columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> list = new ArrayList<String>();
                list.add(cursor.getString(cursor.getColumnIndex(KEY_USERID)));
                list.add(cursor.getString(cursor.getColumnIndex(KEY_USERPASSWORD)));
                Log.d(TAG, "getIDPW: " + list);
            } while (cursor.moveToNext());
        } else Log.d(TAG, "getIDPW: else");
        return cursor;
    }

    public Cursor getIDPWforEdit(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_ID, KEY_USERID, KEY_USERPASSWORD};
        Cursor cursor = db.query(TABLE_IDPW, columns, KEY_ID + " = '" + id + "'", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> list = new ArrayList<String>();
                list.add(cursor.getString(cursor.getColumnIndex(KEY_USERID)));
                list.add(cursor.getString(cursor.getColumnIndex(KEY_USERPASSWORD)));
                Log.d(TAG, "getIDPWforEdit: " + list);

            } while (cursor.moveToNext());
        }
        return cursor;
    }


    public void setIDPW(String id, String pw) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERID, id);
        values.put(KEY_USERPASSWORD, pw);
        db.insert(TABLE_IDPW, null, values);
        db.close();
    }

    public void updateIDPW(int id, String userid, String userpw) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERID, userid);
        values.put(KEY_USERPASSWORD, userpw);
        db.update(TABLE_IDPW, values, KEY_ID + " =?", new String[]{String.valueOf(id)});
        db.close();
    }


    //region addingh new person
    public void addPerson(PersonInfo personInfo, Context context) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, personInfo.getName());
        values.put(KEY_PHONE_NO, personInfo.getPhoneNumber());
        values.put(KEY_CUST_NO, personInfo.get_cust_no());
        values.put(KEY_FEES, personInfo.get_fees());
        values.put(KEY_BALANCE, personInfo.get_balance());
        values.put(KEY_AREA, personInfo.get_area());
        values.put(KEY_STARTDATE, personInfo.get_startdate());
        values.put(KEY_NICKNAME, personInfo.get_nName());
        //Insert row
        db.insert(TABLE_PERSON_INFO, null, values);
        db.close(); //close database
        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();

    }

    public void addPersonImport(PersonInfo personInfo, Context context) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, personInfo.getName());
        values.put(KEY_PHONE_NO, personInfo.getPhoneNumber());
        values.put(KEY_CUST_NO, personInfo.get_cust_no());
        values.put(KEY_FEES, personInfo.get_fees());
        values.put(KEY_BALANCE, personInfo.get_balance());
        values.put(KEY_STBID, personInfo.getStbID());
        values.put(KEY_NICKNAME, personInfo.get_nName());
        //Insert row
        db.insert(TABLE_PERSON_INFO, null, values);
        db.close(); //close database


    }


    //endregion
    public void AddArea(int areano, String areaname) {
        Log.d(TAG, areano + " " + areaname);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AREANO, areano);
        values.put(KEY_AREANAME, areaname);
        db.insert(TABLE_AREA, null, values);
        db.close();
    }

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
        values.put(KEY_DATE, fees.getDate());
        values.put(KEY_RECIEPT, fees.getFees());
        values.put("lBalance", fees.get_lBalance());
        values.put("curBalance", fees.get_curBalance());
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
        values.put(KEY_AREA, personInfo.get_area());
        values.put(KEY_STARTDATE, personInfo.get_startdate());
        values.put(KEY_NICKNAME, personInfo.get_nName());
        //updating row
        return db.update(TABLE_PERSON_INFO, values, KEY_ID + "=?", new String[]{String.valueOf(personInfo.getID())});
    }

    //endregion
    public String changeArea(Area area) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AREANO, area.get_areaNo());
        values.put(KEY_AREANAME, area.get_areaName());
        db.update(TABLE_AREA, values, KEY_ID + " =? ", new String[]{String.valueOf(area.get_id())});

        return area.get_areaName(); // only for toast in the activity
    }

    public void monthFlagChange(String flag) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MONTH_STATUS, flag);
        db.update(TABLE_MONTH_END, values, KEY_ID + "=?", new String[]{String.valueOf(1)});
    }

    public void conCutStart(int id, String newstatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONSTATUS, newstatus);

        db.update(TABLE_PERSON_INFO, values, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }


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

    public int unAssignSTB(String SN) {                           //FROM STB TABLE
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        int assigned = 0;
        values.put(KEY_ASSIGNED, assigned);
        return db.update(TABLE_STB, values, KEY_SN + " =? ", new String[]{String.valueOf(SN)});
    }

    public String getAssignedSN(Context context, int id) {
        String SN = "";

        try {
            Log.d(TAG, "" + id);
            SQLiteDatabase db = getWritableDatabase();
            String query = "SELECT SERIALNO FROM PERSONINFO INNER JOIN STBRECORD ON STBRECORD._ID = PERSONINFO.STBID WHERE PERSONINFO._ID = " + id + " ";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor != null) {
                cursor.moveToFirst();
                SN = cursor.getString(cursor.getColumnIndex(KEY_SN));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Not Any assigned STB", Toast.LENGTH_SHORT).show();

        }
        return SN;
    }

    public int SetStbID(int custid, int stbId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STBID, stbId);
        return db.update(TABLE_PERSON_INFO, values, KEY_ID + " =? ", new String[]{String.valueOf(custid)});
    }

    public int unSetId(int id) {    // from customer table
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STBID, (byte[]) null);
        return db.update(TABLE_PERSON_INFO, values, KEY_ID + " =? ", new String[]{String.valueOf(id)});

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

    public int getStbIdFromCust(int custID) {  // to set assigned to 0 from stb table
        int stbId = 0;
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT " + KEY_STBID + " FROM " + TABLE_PERSON_INFO + " WHERE " + KEY_ID + " = " + custID;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            stbId = cursor.getInt(cursor.getColumnIndex(KEY_STBID));
        }
        return stbId;
    }

    //region delete a person

    public void deletePerson(int custID, int stbID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERSON_INFO + " WHERE " + KEY_ID + "=\"" + custID + "\";");
        db.execSQL("DELETE FROM " + TABLE_FEES + " WHERE " + KEY_ID + "=\"" + custID + "\"");
        db.execSQL("UPDATE " + TABLE_STB + " SET " + KEY_ASSIGNED + " = 0 WHERE  " + KEY_ID + " =  " + stbID);
        db.close();
    }
    //endregion

    public void deleteSTB(int stbId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STB + " WHERE " + KEY_ID + "=\"" + stbId + "\";");
        db.close();
    }

    public void deleteIDPW(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_IDPW + " WHERE " + KEY_ID + "=\"" + id + "\";");
        db.close();

    }


    public void insertBulkData() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_PERSON_INFO + "( " + KEY_NAME + ", " + KEY_PHONE_NO + ", " + KEY_CUST_NO + ", " + KEY_FEES + ", " + KEY_BALANCE + ", " + KEY_STARTDATE + ", " + KEY_AREA + ", " + KEY_NICKNAME + " ) VALUES " +
                "('Ram', '55446', 2, 280, 560, '2017-5-3', 1, 'nickname ')," +
                "('Sham', '89681', 3, 280, 560,'2017-5-4', 1, 'nickname')," +
                "('Makhan', '888', 4, 280, 280,'2017-5-7', 1, 'nickname'), " +
                "('Raj', '999', 5, 230, 460,'2017-5-9', 1, 'nickname'), " +
                "('Mehar', '666', 6, 230, 460,'2017-5-11', 1, 'nickname'), " +
                "('Roshan', '999', 7, 230, 460,'2017-5-13', 1, 'nickname'), " +
                "('Sapna', '999', 11, 230, 460, '2017-5-17',1, 'nickname'), " +
                "('Sandeep ', '999', 15, 230, 460,'2017-5-20', 1, 'nickname'), " +
                "('Raju', '999', 17, 230, 460, '2017-5-22',1, 'nickname'), " +
                "('Keshav', '999', 19, 230, 460,'2017-5-25', 1, 'nickname') "

        );
        db.execSQL("INSERT INTO " + TABLE_STB + "( " + KEY_SN + ", " + KEY_VC + ", " + KEY_STATUS + " ) VALUES " +
                "('AAAAAAAAA', '7895365689', 'ON'), " +
                "('BBBBBBBBB', '5678569957', 'ON'), " +
                "('CCCCCCCCC', '6784568426', 'ON'), " +
                "('DDDDDDDDD', '1235563458', 'ON'), " +
                "('EEEEEEEEE', '1292568124', 'ON'), " +
                "('FFFFFFFFF', '3452568521', 'ON'), " +
                "('GGGGGGGGG', '4321325691', 'ON'), " +
                "('HHHHHHHHH', '6793586985', 'ON'), " +
                "('IIIIIIIII', '6547569258', 'ON'), " +
                "('JJJJJJJJJ', '4321259631', 'ON'), " +
                "('KKKKKKKKK', '7657569675', 'ON') "
        );


    }


    //region Reading a Row  Getting single contact
    public String conStatus(int id) {
        String status = "";
        SQLiteDatabase db = getWritableDatabase();
        String[] columns = {KEY_ID, KEY_CONSTATUS};

        Cursor cursor = db.query(TABLE_PERSON_INFO, columns,
                KEY_ID + " =? ",
                new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        status = cursor.getString(cursor.getColumnIndex(KEY_CONSTATUS));
        return status;
    }

    public PersonInfo getCustInfo(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String columns[] = {KEY_ID, KEY_NAME, KEY_PHONE_NO, KEY_CUST_NO, KEY_FEES, KEY_BALANCE, KEY_AREA, KEY_NICKNAME, KEY_STARTDATE};
        Cursor cursor = db.query(TABLE_PERSON_INFO, columns, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        PersonInfo info = new PersonInfo(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))),
                cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_PHONE_NO)),
                cursor.getFloat(cursor.getColumnIndex(KEY_CUST_NO)),
                cursor.getInt(cursor.getColumnIndex(KEY_FEES)),
                cursor.getInt(cursor.getColumnIndex(KEY_BALANCE)),
                cursor.getInt(cursor.getColumnIndex(KEY_AREA)),
                cursor.getString(cursor.getColumnIndex(KEY_STARTDATE)),
                cursor.getString(cursor.getColumnIndex(KEY_NICKNAME))
        );
        return info;
    }
    //endregion

    public Area getArea(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String columns[] = {KEY_ID, KEY_AREANO, KEY_AREANAME};
        Cursor cursor = db.query(TABLE_AREA, columns, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Area area = new Area(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                cursor.getInt(cursor.getColumnIndex(KEY_AREANO)),
                cursor.getString(cursor.getColumnIndex(KEY_AREANAME)));

        return area;
    }

   /* public PersonInfo personNameStartdate(int id) {


        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_ID, KEY_NAME,KEY_STARTDATE};
        Cursor cursor = db.query(TABLE_PERSON_INFO, columns, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            PersonInfo personInfo = new PersonInfo(cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_STARTDATE))
            );








    } return personInfo;

*/

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

    public int getSTBID(int id) {
        int stbID = 0;
        SQLiteDatabase db = getReadableDatabase();
        String columns[] = {KEY_ID, KEY_STBID};
        Cursor cursor = db.query(TABLE_PERSON_INFO, columns, KEY_ID + " =? ", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            stbID = cursor.getInt(cursor.getColumnIndex(KEY_STBID));
        }

        return stbID;
    }

    public void getSTBPosition() {
        SQLiteDatabase db = getReadableDatabase();
        String colunms[] = {KEY_ID};
        Cursor cursor;

    }


    //region end of month


    public void endOfMonth(Context context) {
        String selectQuery = "SELECT  * FROM " + TABLE_PERSON_INFO + " WHERE " + KEY_CONSTATUS + " = 'ACTIVE'";
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
            Toast.makeText(context, "Fees updated for all", Toast.LENGTH_LONG).show();
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

    public void copyDbToExternalStorage(Context context) {
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        String formattedDate = df.format(calender.getTime());
        Log.d(TAG, "" + formattedDate);
        File folder = new File(Environment.getExternalStorageDirectory() + "/MyBackup");


        if (!folder.exists()) {
            folder.mkdir();
            Toast.makeText(context, "Backup Directory Created", Toast.LENGTH_SHORT).show();
        }


        try {
            File name = new File(Environment.getExternalStorageDirectory(), "/MyCableData/" + DbHendler.DATABASE_NAME);
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
            Log.e(TAG, "ghfhfghfgh " + e.toString());
        }


    }
    //endregion

    //region Restore database
    public void restoreDBfile(Context context, String dbName) {
        // Calendar calender = Calendar.getInstance();
        // SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        // String formattedDate = df.format(calender.getTime());
        try {
            File db = new File(Environment.getExternalStorageDirectory(), "/MyCableData/" + DbHendler.DATABASE_NAME);
            File sdcardFile = new File(Environment.getExternalStorageDirectory(), "/" + DbHendler.DATABASE_NAME); //myInfoManager.db
            db.createNewFile();
            InputStream inputStream = null;
            OutputStream outputStream = null;
            inputStream = new FileInputStream(sdcardFile);
            outputStream = new FileOutputStream(db);
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
                personinfo.setBalance(cursor.getInt(cursor.getColumnIndex(KEY_BALANCE)));
                personinfo.setArea(cursor.getInt(cursor.getColumnIndex(KEY_AREA)));
                // Adding contact to list
                contactList.add(personinfo);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    //endregion

    //region Getting All STB to log
    public List<STB> getAllStbs() {
        Log.d(TAG, "callled");
        List<STB> stbList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STB;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                STB stb = new STB();
                stb.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                stb.setSerialNo(cursor.getString(cursor.getColumnIndex(KEY_SN)));
                stb.setVcNo(cursor.getString(cursor.getColumnIndex(KEY_VC)));


                // Adding contact to list
                stbList.add(stb);
            } while (cursor.moveToNext());
        }
        // return contact list
        return stbList;
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
                fees.setFees(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbHendler.KEY_FEES))));
                fees.setDate(cursor.getString(cursor.getColumnIndex(DbHendler.KEY_DATE)));

                // Adding fees to list
                feeslist.add(fees);
            } while (cursor.moveToNext());
        }
        // return contact list
        return feeslist;
    }

    //endregion

    public String getAreaName(int areaID) {
        String areaname = null;
        String query = " SELECT * FROM " + TABLE_AREA + " WHERE " + KEY_ID + " = " + areaID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                areaname = (cursor.getString(cursor.getColumnIndex(KEY_AREANAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return areaname;


    }

    public List<Area> getAllAreas() {
        List<Area> areasList = new ArrayList<Area>();
        String query = "SELECT * FROM " + TABLE_AREA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                Area area = new Area();
                area.set_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                area.set_areaNo(cursor.getInt(cursor.getColumnIndex(KEY_AREANO)));
                area.set_areaName(cursor.getString(cursor.getColumnIndex(KEY_AREANAME)));
                areasList.add(area);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return areasList;
    }

    public int getAreaID(String item) {
        int areaId = 0;
        String query = "SELECT * FROM " + TABLE_AREA + " WHERE " + KEY_AREANAME + " = '" + item + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            areaId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        }
        return areaId;
    }


    public void createIDPWTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        onCreate(db);
    }


}

