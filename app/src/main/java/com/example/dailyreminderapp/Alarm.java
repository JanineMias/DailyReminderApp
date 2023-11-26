package com.example.dailyreminderapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.File;

public class Alarm extends BroadcastReceiver {
    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("NOTIF", "BUILDING NOTIF");

        int id = intent.getIntExtra("ID", 0);
        String title = intent.getStringExtra("Title");
        String description = intent.getStringExtra("Description");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ReminderNotification")
                .setSmallIcon(R.drawable.clock)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(id, builder.build());

        Log.e("NOTIF", "NOTIF BUILT");

    }
}