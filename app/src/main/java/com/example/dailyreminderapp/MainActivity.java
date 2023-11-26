package com.example.dailyreminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class MainActivity extends AppCompatActivity {
    Button reminder, newAlarm;

    public static FragmentManager fragmentManager;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        init();
    }

    void init(){

        context = MainActivity.this;

        fragmentManager = getSupportFragmentManager();

        reminder = findViewById(R.id.btnReminder);

        reminder.setOnClickListener(v -> {

            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, homePage.class,null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });
        newAlarm = findViewById(R.id.btnNewAlarm);
        newAlarm.setOnClickListener(v -> {
            openCustomAlarm(0);
        });
    }

    public void openCustomAlarm(int id) {
        if (id > 0) {
            openFragment(true, 1);
        } else {
            openFragment(false, 0);
        }
    }

    private void openFragment(boolean isEdit, int id) {
        Bundle args = new Bundle();
        args.putBoolean("isEdit", isEdit);
        args.putInt("ID", id);

        customAlarm fragment = new customAlarm();
        fragment.setArguments(args);

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment, null)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Reminder Channel";
            String description = "Channel for reminder notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("ReminderNotification", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}