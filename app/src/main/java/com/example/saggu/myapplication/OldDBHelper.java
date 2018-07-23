package com.example.saggu.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

public class OldDBHelper extends SQLiteOpenHelper {



    private static String path = externalStrorage() + "/jatindercable.db";



    public OldDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    private static String externalStrorage() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/JatinderCable");
        if (!folder.exists()) {
            folder.mkdir();
        }
        String path = new String(Environment.getExternalStorageDirectory().getAbsolutePath() + "/JatinderCable");
        return path;
    }




}



