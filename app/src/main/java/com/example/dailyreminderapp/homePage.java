package com.example.dailyreminderapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class homePage extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){

        // inflate layout for the fragment
        view = inflater.inflate(R.layout.fragment_home_page, container, false);

        init();
        return view;
    }
    public void init() {


    }

}