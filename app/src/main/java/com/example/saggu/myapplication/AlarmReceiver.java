package com.example.saggu.myapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Saggu on 3/5/2017.
 */


public class AlarmReceiver extends BroadcastReceiver {
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    DbHendler dbHendler;
    String TAG = "Alarm Reciever";
    String done = "DONE";
    String notDone = "NOTDONE";


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onRecieve called ");

        String day = intent.getStringExtra("id");
        int date = Integer.parseInt(day);


        dbHendler = new DbHendler(context, null, null, 1);
        String flag = dbHendler.monthFlag();
        Log.d(TAG, flag);
        Log.d(TAG, "Day is " + date);


        if (date == 1 && flag.equals(notDone)) {
            dbHendler.copyDbToExternalStorage(context);
            dbHendler.monthFlagChange(done);
            String fl = dbHendler.monthFlag();
            Log.d(TAG, "flag changed to " + fl);
            Log.d(TAG, "balance will be updated");
            dbHendler.endOfMonth(context);

        }

        if (date > 1 && flag.equals(done)) {
            dbHendler.monthFlagChange(notDone);
            String fl = dbHendler.monthFlag();
            Log.d(TAG, "flag changed to " + fl);

        }





        /*//Canceling the repeating alarm
        Intent intentAlarm = new Intent(context, AlarmReceiver.class);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);*/


    }


}
