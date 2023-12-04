package com.example.dailyreminderapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;

public class homePage extends Fragment {
    public final String DATABASE_NAME = "REMINDER";
    public final String TABLE_NAME = "reminder";
    SQLiteDatabase db;

    Cursor cursor;

    View view;

    ArrayList<reminderCardDetails> reminderCard = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){

        // inflate layout for the fragment
        view = inflater.inflate(R.layout.fragment_home_page, container, false);

        databaseInit(requireContext());
        retrieveAllReminders();

        RecyclerView recyclerView = view.findViewById(R.id.reminderList);

        reminderArrayAdapter adapter = new reminderArrayAdapter(MainActivity.context, reminderCard);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.context));

        return view;
    }

    void retrieveAllReminders(){
        String query = "SELECT * FROM " + TABLE_NAME;
        cursor = db.rawQuery(query, null);

        try {
            while (cursor.moveToNext()) {
                reminderCard.add(
                        new reminderCardDetails(cursor.getInt(0),
                                                cursor.getString(3),
                                                cursor.getString(4),
                                                cursor.getString(1),
                                                cursor.getString(2),
                                                cursor.getInt(5) > 0)
                );
            }

        } catch (Exception e) {
            Log.d("ERROR", e.toString());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    void databaseInit(Context context){
        File dbDirectory = context.getDir("databases", Context.MODE_PRIVATE);

        db = SQLiteDatabase.openOrCreateDatabase(new File(dbDirectory, DATABASE_NAME), null);
        db.execSQL(""+
                "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(reminder_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "reminder_name TEXT, " +
                "reminder_description TEXT, " +
                "reminder_time TEXT, " +
                "meridiem TEXT, " +
                "switch_state INTEGER);"
        );
    }
}