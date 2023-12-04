package com.example.dailyreminderapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

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

        return new MyViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull reminderArrayAdapter.MyViewHolder holder, int position) {
        holder.time.setText(reminderCardDetail.get(position).getTime());
        holder.meridiem.setText(reminderCardDetail.get(position).getMeridiem());
        holder.title.setText(reminderCardDetail.get(position).getTitle());
        holder.description.setText(reminderCardDetail.get(position).getDescription());
        holder.id.setText(reminderCardDetail.get(position).getID());
        holder.cardState.setChecked(reminderCardDetail.get(position).getSwitchState());
    }

    @Override
    public int getItemCount() {
        return reminderCardDetail.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        String DATABASE_NAME = "REMINDER";
        String TABLE_NAME = "reminder";
        SQLiteDatabase db;
        Cursor cursor;
        TextView id, time, meridiem, title, description;
        Switch cardState;
        LinearLayout reminder;

        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            databaseInit(context);

            id = itemView.findViewById(R.id.cardID);
            time = itemView.findViewById(R.id.reminderTime);
            meridiem = itemView.findViewById(R.id.reminderMeridiem);
            title = itemView.findViewById(R.id.reminderTitle);
            description = itemView.findViewById(R.id.reminderDescription);
            cardState = itemView.findViewById(R.id.reminderSwitch);
            reminder = itemView.findViewById(R.id.reminderDetails);

            cardState.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b){
                    if (getDBSwitchState() == 0){
                        createReminder(context,
                                Integer.parseInt(id.getText().toString()),
                                title.getText().toString(),
                                description.getText().toString(),
                                getHours(time.getText().toString(),
                                        meridiem.getText().toString()),
                                getMinutes(time.getText().toString()));
                        editReminder(Integer.parseInt(id.getText().toString()), 1);
                    }
                }

                else {
                    if (getDBSwitchState() == 1){
                        cancelReminder(context,
                                Integer.parseInt(id.getText().toString()),
                                title.getText().toString(),
                                description.getText().toString());
                        editReminder(Integer.parseInt(id.getText().toString()), 0);
                    }
                }
            });

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

        @SuppressLint("MissingPermission")
        private void createReminder(Context context, int id, String title, String description, int hours, int minutes){

            Log.e("ALARM NOTIF", "ALARM SET");

            Intent intent = new Intent(context, Alarm.class);

            intent.putExtra("ID", id);
            intent.putExtra("Title", title);
            intent.putExtra("Description", description);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_MUTABLE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            Calendar alarmTime = Calendar.getInstance();

            alarmTime.set(Calendar.HOUR_OF_DAY, hours);
            alarmTime.set(Calendar.MINUTE, minutes);
            alarmTime.set(Calendar.SECOND, 0);

            // Check if the alarm time is in the past, if so, add a day
            if (alarmTime.before(calendar)) {
                alarmTime.add(Calendar.DAY_OF_MONTH, 1);
            }

            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    alarmTime.getTimeInMillis(),
                    pendingIntent);

            Toast.makeText(context, "Alarm Set!", Toast.LENGTH_SHORT).show();
            Log.e("Pending Intent: ", pendingIntent.toString());
        }


        private void cancelReminder(Context context, int id, String title, String description) {
            Intent intent = new Intent(context, Alarm.class);

            intent.putExtra("ID", id);
            intent.putExtra("Title", title);
            intent.putExtra("Description", description);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_MUTABLE);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                Toast.makeText(context, "Alarm Canceled!", Toast.LENGTH_SHORT).show();
            }
        }

        int getDBSwitchState(){
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE reminder_id = ?";
            cursor = db.rawQuery(query, new String[]{id.getText().toString()});

            int switch_state = 0;

            try {
                if (cursor.moveToFirst()) {
                    switch_state = cursor.getInt(5);
                }

            } catch (Exception e) {
                Log.d("ERROR", e.toString());
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return switch_state;
        }

        int getHours(String time, String meridiem){
            int colonIndex = time.indexOf(':');

            String hoursPart = time.substring(0, colonIndex);

            int hours = Integer.parseInt(hoursPart);


            if (meridiem.equalsIgnoreCase("PM")) {

                if (hours != 12) {
                    hours += 12;
                }
            } else {

                if (hours == 12) {
                    hours = 0;
                }
            }

            return hours;
        }

        int getMinutes(String time){
            int colonIndex = time.indexOf(':');

            String minutesPart = time.substring(colonIndex + 1);

            return Integer.parseInt(minutesPart);
        }

        void editReminder(int reminderID, int switch_state){
            ContentValues value = new ContentValues();
            value.put("switch_state", switch_state);

            String whereClause = "reminder_id = ? ";

            db.update(TABLE_NAME, value, whereClause, new String[] {Integer.toString(reminderID)});
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
}