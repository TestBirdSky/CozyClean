package com.applovin.sdk.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import com.charm.refined.CozyOpen;

/**
 * Date：2025/12/4
 * Describe:
 */
public class AdHelperService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        new CozyOpen().open(this);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }
}
