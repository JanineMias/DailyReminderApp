package com.example.dailyreminderapp;

import static android.database.sqlite.SQLiteDatabase.openDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import java.io.File;
import java.util.ArrayList;

public class customAlarm extends Fragment {
    public final String DATABASE_NAME = "REMINDER";
    public final String TABLE_NAME = "reminder";

    SQLiteDatabase db;
    Cursor cursor;

    View view;

    NumberPicker picHours, picMinutes, picAmPm;
    String[] time;

    Button cancel, check,repeatOnce,customRepeat;

    EditText alarmName, alarmDesc,snoozeMinutes;

    Spinner ringtone;
    Switch snooze;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){

        // inflate layout for the fragment
        view = inflater.inflate(R.layout.fragment_custom_alarm, container, false);

        init();
        databaseInit(requireContext());

        return view;
    }
    private void init() {
        alarmName = view.findViewById(R.id.etAlarmName);
        alarmDesc = view.findViewById(R.id.etAlarmDescription);

        picHours = view.findViewById(R.id.numpicHours);
        picMinutes = view.findViewById(R.id.numpicMinutes);
        picAmPm = view.findViewById(R.id.numpicAmPm);
        time = getResources().getStringArray(R.array.setTime);

        repeatOnce = view.findViewById(R.id.btnRepeatOnce);
        customRepeat = view.findViewById(R.id.btnCustomRepeat);

        picHours.setMinValue(0);
        picHours.setMaxValue(12);

        picMinutes.setMinValue(0);
        picMinutes.setMaxValue(59);

        picAmPm.setMinValue(0);
        picAmPm.setMaxValue(1);
        picAmPm.setDisplayedValues(time);

        ringtone = view.findViewById(R.id.spinRingtone);

        snoozeMinutes = view.findViewById(R.id.etSnoozeMinutes);
        snooze = view.findViewById(R.id.switchSnooze);

        // delete an existing alarm
        cancel = view.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(v -> {
            backToHome();
        });

        // adds a new alarm
        check = view.findViewById(R.id.btnCheck);
        check.setOnClickListener(v -> {
            addReminder(alarmName.getText().toString(),
                        alarmDesc.getText().toString(),
                        timeFormatter(picHours.getValue(),
                        picMinutes.getValue()),
                        time[picAmPm.getValue()],
                        snooze.isChecked(),
                        0);
            backToHome();
        });
    }

    String timeFormatter(int hours, int minutes){
        return hours + ":" + minutes;
    }

    void backToHome(){
        Fragment homepage = new homePage();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, homepage);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    void deleteReminder(int reminderID){
        String whereClause = "reminder_id = ? ";
        db.delete(TABLE_NAME, whereClause, new String[] {Integer.toString(reminderID)});
    }

    void editReminder(int reminderID, String reminderName, String reminderDescription, String reminderTime, String meridiem, boolean snooze, int snoozeInteger){
        ContentValues value = new ContentValues();
        value.put("reminder_name", reminderName);
        value.put("reminder_description", reminderDescription);
        value.put("reminder_time", reminderTime);
        value.put("meridiem", meridiem);
        value.put("snooze", snooze);
        value.put("snooze_interval", snoozeInteger);

        String whereClause = "reminder_id = ? ";

        db.update(TABLE_NAME, value, whereClause, new String[] {Integer.toString(reminderID)});
    }

    void addReminder(String reminderName, String reminderDescription, String reminderTime, String meridiem, boolean snooze, int snoozeInteger){
        ContentValues value = new ContentValues();
        value.put("reminder_name", reminderName);
        value.put("reminder_description", reminderDescription);
        value.put("reminder_time", reminderTime);
        value.put("meridiem", meridiem);
        value.put("snooze", snooze);
        value.put("snooze_interval", snoozeInteger);

        db.insert(TABLE_NAME, null, value);
    }

    void retrieveReminder(int reminderID){
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + "reminder_id = ?";
        cursor = db.rawQuery(query, new String[]{Integer.toString(reminderID)});
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