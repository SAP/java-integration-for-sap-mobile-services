package com.sap.mobile.services.client.push;

public enum InterruptionLevel {
    PASSIVE("passive"),
    ACTIVE("active"),
    TIME_SENSITIVE("time-sensitive"),
    CRITICAL("critical");

    private String level;

    private InterruptionLevel(String level) {
        this.level = level;
    }

    public String toString() {
        return this.level;
    }

}
