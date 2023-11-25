package com.example.dailyreminderapp;

import static android.database.sqlite.SQLiteDatabase.openDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

    int reminderID;
    boolean isEdit;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){
        Bundle args = getArguments();

        isEdit = args.getBoolean("isEdit");
        reminderID = args.getInt("ID");

        // inflate layout for the fragment
        view = inflater.inflate(R.layout.fragment_custom_alarm, container, false);

        databaseInit(requireContext());
        init();

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
            deleteReminder(reminderID);
            backToHome();
        });

        // adds a new alarm
        check = view.findViewById(R.id.btnCheck);
        check.setOnClickListener(v -> {

            if (isEdit){
                editReminder(reminderID,
                            alarmName.getText().toString(),
                            alarmDesc.getText().toString(),
                            timeFormatter(picHours.getValue(),
                            picMinutes.getValue()),
                            time[picAmPm.getValue()],
                            snooze.isChecked(),
                        0);
            }

            else {
                addReminder(alarmName.getText().toString(),
                            alarmDesc.getText().toString(),
                            timeFormatter(picHours.getValue(),
                            picMinutes.getValue()),
                            time[picAmPm.getValue()],
                            snooze.isChecked(),
                        0);
            }


            backToHome();
        });

        retrieveReminder(reminderID);
    }

    String timeFormatter(int hours, int minutes){
        String _hours = Integer.toString(hours);
        String _minutes = Integer.toString(minutes);

        if (hours < 10){
            _hours = "0"+_hours;
        }

        if (minutes < 10){
            _minutes = "0"+_minutes;
        }

        return _hours + ":" + _minutes;
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

    //RETURNS HOURS FROM FORMATTED TEXT;
    int getHours(String time){
        int colonIndex = time.indexOf(':');

        String hoursPart = time.substring(0, colonIndex);

        return Integer.parseInt(hoursPart);
    }

    int getMinutes(String time){
        int colonIndex = time.indexOf(':');

        String minutesPart = time.substring(colonIndex + 1);

        return Integer.parseInt(minutesPart);
    }
    int getMeridiem(String[] array, String targetValue) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equalsIgnoreCase(targetValue)) {
                return i;
            }
        }
        return -1;
    }

    void retrieveReminder(int reminderID){
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + "reminder_id = ?";
        cursor = db.rawQuery(query, new String[]{Integer.toString(reminderID)});

        try {
            if (cursor.moveToFirst()) {
                alarmName.setText(cursor.getString(1));
                alarmDesc.setText(cursor.getString(2));
                picHours.setValue(getHours(cursor.getString(3)));
                picMinutes.setValue(getMinutes(cursor.getString(3)));
                picAmPm.setValue(getMeridiem(time, cursor.getString(4)));

                boolean snoozeState = (cursor.getInt(5) > 0);

                snooze.setChecked(snoozeState);
                snoozeMinutes.setText(cursor.getInt(6));
            }

        } catch (Exception e) {
            Log.d("ERROR", e.toString());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        cursor.close();
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