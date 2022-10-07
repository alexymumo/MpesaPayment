package com.alexmumo.xpressway.models;

/*
* Model for Trip
* */
public class Trip {
    public String vehicle_class, vehicle_definition, exit, entrance;

    public Trip() {

    }

    public Trip(String vehicle_class, String vehicle_definition, String exit, String entrance) {
        this.vehicle_class = vehicle_class;
        this.vehicle_definition = vehicle_definition;
        this.exit = exit;
        this.entrance = entrance;
    }
    // getters and setters
    public String getVehicle_definition() {
        return vehicle_definition;
    }

    public void setVehicle_definition(String vehicle_definition) {
        this.vehicle_definition = vehicle_definition;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setVehicle_class(String vehicle_class) {
        this.vehicle_class = vehicle_class;
    }

    public String getVehicle_class() {
        return vehicle_class;
    }

    public String getExit() {
        return exit;
    }

    public void setExit(String exit) {
        this.exit = exit;
    }
}
