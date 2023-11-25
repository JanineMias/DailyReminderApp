package com.example.dailyreminderapp;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

public class homePage extends Fragment {
    public final String DATABASE_NAME = "REMINDER";
    public final String TABLE_NAME = "reminder";
    SQLiteDatabase db;

    Cursor cursor;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){

        // inflate layout for the fragment
        view = inflater.inflate(R.layout.fragment_home_page, container, false);

        init();
        databaseInit(requireContext());

        return view;
    }
    public void init() {

    }

    void retrieveAllReminders(){

    }

    void databaseInit(Context context){
        File dbDirectory = context.getDir("databases", Context.MODE_PRIVATE);

        db = SQLiteDatabase.openOrCreateDatabase(new File(dbDirectory, DATABASE_NAME), null);
        db.execSQL(""+
                "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(reminder_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, reminder_name TEXT, reminder_description TEXT, reminder_time TEXT, meridiem TEXT, snooze BOOLEAN, snooze_interval INTEGER);"
        );
    }
}