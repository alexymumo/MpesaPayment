package com.alexmumo.xpressway.models;

public class Complains {
    private String message, name, type;

    public Complains() {

    }

    public Complains(String message, String name, String type) {
        this.message = message;
        this.name = name;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
