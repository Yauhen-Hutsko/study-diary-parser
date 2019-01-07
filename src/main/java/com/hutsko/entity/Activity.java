package com.hutsko.entity;

import java.util.Objects;

public class Activity {
    private String name;
    private Duration duration ;

    public Activity(String name) {
        this.name = name;
        duration = new Duration(0,0);
    }

    public Activity(String name, Duration duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public Duration getDuration() {
        return duration;
    }

    public void add(Duration duration){
        this.duration.add(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        Activity activity = (Activity) o;
        return Objects.equals(name, activity.name) &&
                duration.equals(activity.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, duration);
    }

    @Override
    public String toString() {
        return duration == null ? "Cannot be calculated" : duration.toString();
    }
}
