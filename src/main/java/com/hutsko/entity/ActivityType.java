package com.hutsko.entity;

public enum ActivityType {
    PROGRAMMING("программирование"),
    MATH("математика"),
    READING("чтение"),
    GUITAR("гитара");

    ActivityType(String name) {
        this.name = name;
    }

    private final String name;

    @SuppressWarnings("UnusedReturnValue")
    public static ActivityType forString(String name) {
        ActivityType[] activityTypes = values();
        int length = activityTypes.length;

        for (ActivityType type : activityTypes) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid ActivityType name: " + name);
    }

    public String getName() {
        return name;
    }
}
