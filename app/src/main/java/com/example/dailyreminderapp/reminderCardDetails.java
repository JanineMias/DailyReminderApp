package com.example.dailyreminderapp;
public class reminderCardDetails {
    String time;
    String meridiem;
    String title;
    String description;
    int id;


    public reminderCardDetails(int id, String time, String meridiem, String title, String description) {
        this.time = time;
        this.meridiem = meridiem;
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public String getID() {
        return String.valueOf(id);
    }

    public String getTime() {
        return time;
    }

    public String getMeridiem() {
        return meridiem;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
