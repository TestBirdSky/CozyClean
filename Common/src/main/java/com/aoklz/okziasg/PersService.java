package com.aoklz.okziasg;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.charm.refined.cas.Authen;

public class PersService extends Service {
    public Authen helper1;

    public IBinder onBind(Intent intent) {
        return this.helper1.getIBinder();
    }

    public void onCreate() {
        super.onCreate();
        this.helper1 = new Authen(this);
    }
}