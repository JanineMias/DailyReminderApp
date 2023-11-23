package com.example.dailyreminderapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.zip.Inflater;

public class customAlarm extends Fragment {


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

        // goes back to homepage
        cancel = view.findViewById(R.id.btnCancel);

        // adds a new alarm
        check = view.findViewById(R.id.btnCheck);


    }
}