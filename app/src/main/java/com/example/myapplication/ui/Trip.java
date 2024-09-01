package com.example.myapplication.ui;

public class Trip {
    private String type;
    private int emission;

    public Trip() {
        this.type = "";
        this.emission = 0;
    }

    public Trip(String type, int emission) {
        this.type = type;
        this.emission = emission;
    }

    public String getType() {
        return this.type;
    }

    public int getEmission() {
        return this.emission;
    }
}
