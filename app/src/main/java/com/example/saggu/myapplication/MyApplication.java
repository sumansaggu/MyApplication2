package com.example.saggu.myapplication;

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.*;
import org.acra.annotation.*;

/**
 * Created by Saggu on 3/21/2017. this class is for ACRA CREATE BY ME FOR TESTING
 */


    @ReportsCrashes(
            formUri = "http://www.backendofyourchoice.com/reportpath"
    )
    public class MyApplication extends Application{
        @Override
        protected void attachBaseContext(Context base) {
            super.attachBaseContext(base);

            // The following line triggers the initialization of ACRA
            ACRA.init(this);
        }
    }

