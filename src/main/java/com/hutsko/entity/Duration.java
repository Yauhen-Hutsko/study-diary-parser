package com.hutsko.entity;

import java.util.Objects;

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

    public Duration add(Duration dur) {
        int h = hours + dur.hours;
        int m = minutes + dur.minutes;
        while (m >= 60) {
            m = m - 60;
            h += 1;
        }
        hours = h;
        minutes = m;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Duration)) return false;
        Duration duration = (Duration) o;
        return hours == duration.hours &&
                minutes == duration.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes);
    }

    @Override
    public String toString() {
        if (hours == 0 && minutes == 0) {
            return "0";
        }
        return (((hours != 0) ? hours + "h " : "") + ((minutes != 0) ? minutes + "m" : "")).trim();
    }
}
