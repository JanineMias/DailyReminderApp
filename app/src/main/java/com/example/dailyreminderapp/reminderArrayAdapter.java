package com.example.dailyreminderapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class reminderArrayAdapter extends RecyclerView.Adapter<reminderArrayAdapter.MyViewHolder> {
    Context context;
    ArrayList<reminderCardDetails> reminderCardDetail;

    public reminderArrayAdapter(Context context, ArrayList<reminderCardDetails> reminderCardDetail){
        this.context = context;
        this.reminderCardDetail = reminderCardDetail;
    }

    @NonNull
    @Override
    public reminderArrayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reminder, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull reminderArrayAdapter.MyViewHolder holder, int position) {
        holder.time.setText(reminderCardDetail.get(position).getTime());
        holder.meridiem.setText(reminderCardDetail.get(position).getMeridiem());
        holder.title.setText(reminderCardDetail.get(position).getTitle());
        holder.description.setText(reminderCardDetail.get(position).getDescription());
        holder.id.setText(reminderCardDetail.get(position).getID());
    }

    @Override
    public int getItemCount() {
        return reminderCardDetail.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView id, time, meridiem, title, description;
        Switch cardState;

        LinearLayout reminder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.cardID);
            time = itemView.findViewById(R.id.reminderTime);
            meridiem = itemView.findViewById(R.id.reminderMeridiem);
            title = itemView.findViewById(R.id.reminderTitle);
            description = itemView.findViewById(R.id.reminderDescription);
            cardState = itemView.findViewById(R.id.reminderSwitch);
            reminder = itemView.findViewById(R.id.reminderDetails);

            reminder.setOnClickListener(v -> {

                Bundle args = new Bundle();
                args.putBoolean("isEdit", true);
                args.putInt("ID", Integer.parseInt(id.getText().toString()));

                customAlarm fragment = new customAlarm();
                fragment.setArguments(args);

                MainActivity.fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, fragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }
}

