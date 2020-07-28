package com.example.secroicy;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.fcm.MessagingService;

import org.jetbrains.annotations.Nullable;

public class NotificationsMessagingService extends MessagingService {

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.v("NOTIFICATION", "Serivce start ho gayi ha");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.v("NOTIFICATION", "Got a remote message 🎉");
    }
}
