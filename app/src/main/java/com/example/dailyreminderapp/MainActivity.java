package com.example.dailyreminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button reminder, newAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        reminder = findViewById(R.id.btnReminder);

        reminder.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, homePage.class,null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });
        newAlarm = findViewById(R.id.btnNewAlarm);
        newAlarm.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, customAlarm.class,null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });
    }


}