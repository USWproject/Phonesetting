package com.ulan.timetable.model;

/**
 * Created by Ulan on 07.09.2018.
 */
public class Week {


    private String subject, fragment, fromtime, totime, time, mode;
    private int id, color, fromtime_h, fromtime_m, totime_h, totime_m;

    public Week() {}

    public Week(String subject, String fromtime, String totime, int color, String mode, int fromtime_h, int fromtime_m, int totime_h, int totime_m) {
        this.subject = subject;
        this.fromtime = fromtime;
        this.fromtime_h = fromtime_h;
        this.fromtime_m = fromtime_m;
        this.totime = totime;
        this.totime_h = totime_h;
        this.totime_m = totime_m;
        this.color = color;
        this.mode = mode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public String getFromTime() {
        return fromtime;
    }

    public void setFromTime(String fromtime) {
        this.fromtime = fromtime;
    }

    public void setFromtime_h(int fromtime_h) { this.fromtime_h = fromtime_h;}

    public int getFromtime_h() { return fromtime_h;}

    public void setFromtime_m(int fromtime_m) { this.fromtime_m = fromtime_m;}

    public int getFromtime_m() { return fromtime_m;}

    public String getToTime() {
        return totime;
    }

    public void setToTime(String totime) {
        this.totime = totime;
    }

    public void setTotime_h(int totime_h) { this.totime_h = totime_h;}

    public int getTotime_h() { return totime_h;}

    public void setTotime_m(int totime_m) { this.totime_m = totime_m;}

    public int getTotime_m() { return totime_m;}

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String toString() {
        return subject;
    }

    public void setMode(String mode) {this.mode = mode;}

    public String getMode() {return mode;}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
