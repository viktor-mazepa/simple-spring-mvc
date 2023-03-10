package com.mazasoft.springcourse.common;

public enum Action {
    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    DIVISION;


    public static Action fromString(String text) {
        for (Action b : Action.values()) {
            if (b.toString().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
