package com.aoklz.kizjag.liskag;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.charm.refined.cas.AccounSync;

public class StylSetviz extends Service {
    public AccounSync f36275e8;


    public IBinder onBind(Intent intent) {
        return this.f36275e8.getSyncAdapterBinder();
    }

    public void onCreate() {
        super.onCreate();
        this.f36275e8 = new AccounSync(getApplicationContext(), true);
    }
}