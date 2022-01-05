package com.example.fypintelidea.core.enums;

public enum ScheduleIntervals {
    daily("days"),
    weekly ("weeks"),
    monthly ("months"),
    reading ("machine hours");

    private final String mValue;

    ScheduleIntervals(String value) {
        this.mValue = value;
    }

    public String id() {
        return mValue;
    }

    public static String fromApiName(String value) {
        for (ScheduleIntervals interval : values()) {
            if (interval.name().equals(value)) {
                return interval.mValue;
            }
        }
        return null;
    }
}
