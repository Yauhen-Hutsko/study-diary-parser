package com.hutsko.entity;

public class Duration {
    private int hours;
    private int minutes;

    public Duration(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }
    public int getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        return (((hours !=0)? hours + "h ":"") + ((minutes != 0)? minutes + "m":"")).trim();
    }
}
