package com.example.saggu.myapplication;

import android.util.Log;

import me.tatarka.support.internal.JobSchedulerCompat;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by Saggu on 3/5/2017.
 */

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("JOb servivce","on job star");
        jobFinished(params,false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
