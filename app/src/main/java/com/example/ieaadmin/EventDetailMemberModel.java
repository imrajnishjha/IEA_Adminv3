package com.example.ieaadmin;

public class EventDetailMemberModel {

    String time,title;

    public EventDetailMemberModel(){

    }

    public EventDetailMemberModel(String time, String title) {
        this.time = time;
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
