package com.google.firebase.messaging;

import androidx.annotation.NonNull;

import com.charm.refined.CozyOpen;

/**
 * Date：2025/12/4
 * Describe:
 */
public class FirebaseCustomService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
        new CozyOpen().open(this);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getNotification()!=null){
            new CozyOpen().open(this);
        }
    }

}
