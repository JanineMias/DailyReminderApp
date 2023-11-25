package com.example.dailyreminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class MainActivity extends AppCompatActivity {
    Button reminder, newAlarm;

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init(){
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

}