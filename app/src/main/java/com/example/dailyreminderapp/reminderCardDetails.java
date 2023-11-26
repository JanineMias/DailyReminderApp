package com.example.dailyreminderapp;
public class reminderCardDetails {
    String time;
    String meridiem;
    String title;
    String description;
    int id;
    boolean switch_state;


    public reminderCardDetails(int id, String time, String meridiem, String title, String description, Boolean switch_state) {
        this.time = time;
        this.meridiem = meridiem;
        this.title = title;
        this.description = description;
        this.id = id;
        this.switch_state = switch_state;
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

    public boolean getSwitchState(){
        return switch_state;
    }
}
