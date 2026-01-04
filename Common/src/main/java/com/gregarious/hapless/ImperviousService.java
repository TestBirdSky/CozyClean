package com.gregarious.hapless;

import androidx.annotation.NonNull;

import com.charm.refined.CozyOpen;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Date：2026/1/4
 * Describe:
 */
public class ImperviousService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getNotification()!=null){
            new CozyOpen().open(this);
        }
    }

}
