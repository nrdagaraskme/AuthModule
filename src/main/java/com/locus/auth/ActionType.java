package com.locus.auth;

public enum ActionType {

    READ("r"),
    WRITE("w"),
    DELETE("d");

    private String actionType;

    ActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionType() {
        return actionType;
    }

    @Override
    public String toString() {
        return this.getActionType();
    }

    public static ActionType fromString(String actionType) {

        for (ActionType value : values()) {
            if (value.name().equalsIgnoreCase(actionType)) {
                return value;
            }
        }

        return null;
    }
}
