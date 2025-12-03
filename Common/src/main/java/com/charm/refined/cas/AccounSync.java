package com.charm.refined.cas;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;


/**
 * Date：2025/12/3
 * Describe:
 */
public class AccounSync extends AbstractThreadedSyncAdapter {
    public static final Handler f36274f8 = new Handler(Looper.getMainLooper());

    public class RunnableImpl implements Runnable {
        public final /* synthetic */ Account f36276e8;

        public RunnableImpl(Account account) {
            this.f36276e8 = account;
        }

        public void run() {
            Bundle bundle = new Bundle();
            bundle.putBoolean("expedited", true);
            bundle.putBoolean("force", true);
            bundle.putBoolean("reset", true);
            ContentResolver.requestSync(this.f36276e8, HandlerHelper.e(AccounSync.this.getContext()), bundle);
        }
    }

    public AccounSync(Context context, boolean z) {
        super(context, z);
    }

    public void onPerformSync(Account account, Bundle bundle, String str, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        boolean z = bundle.getBoolean("reset");
        f36274f8.removeCallbacksAndMessages("token");
        if (z) {
            syncResult.stats.numIoExceptions = 0;
            Bundle bundle2 = new Bundle();
            bundle2.putBoolean("expedited", true);
            bundle2.putBoolean("force", true);
            bundle2.putBoolean("reset", false);
            ContentResolver.requestSync(account, HandlerHelper.e(getContext()), bundle2);
            return;
        }
        syncResult.stats.numIoExceptions = 1;
        f36274f8.postAtTime(new RunnableImpl(account), "token", SystemClock.uptimeMillis() + 20000);
    }
}