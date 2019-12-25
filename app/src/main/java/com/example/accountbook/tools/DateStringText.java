package com.example.accountbook.tools;

public class DateStringText {

    private int year;
    private int day;
    private int month;
    private int minute;
    private int hour;

    public DateStringText(int year, int day, int month, int minute, int hour) {
        this.year = year;
        this.day = day;
        this.month = month;
        this.minute = minute;
        this.hour = hour;
    }

    public DateStringText() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return year + "-" +
                month + "-" +
                day + " " +
                hour+ ":" +
                minute ;
    }
}