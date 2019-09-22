package com.example.sanjay.cabadmin.RecyclerUI;

public class Journey {
    private long time;
    private String to_location;
    private String from_location;
    private int trip_cost;
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTo_location() {
        return to_location;
    }

    public void setTo_location(String to_location) {
        this.to_location = to_location;
    }

    public String getFrom_location() {
        return from_location;
    }

    public void setFrom_location(String from_location) {
        this.from_location = from_location;
    }

    public int getTrip_cost() {
        return trip_cost;
    }

    public void setTrip_cost(int trip_cost) {
        this.trip_cost = trip_cost;
    }
}
