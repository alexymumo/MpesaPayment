package com.alexmumo.xpressway.models;

public class LogBook {
    private String logbookUrl;

    public LogBook(String logbookUrl) {
        this.logbookUrl = logbookUrl;
    }

    public LogBook() {
    }
    public String getLogbookUrl() {
        return logbookUrl;
    }

    public void setLogbookUrl(String logbookUrl) {
        this.logbookUrl = logbookUrl;
    }

}
