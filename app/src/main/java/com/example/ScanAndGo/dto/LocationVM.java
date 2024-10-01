package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class LocationVM {

    @SerializedName("id")
    public int id;

    @SerializedName("name")

    public String name;

    public LocationVM() {

    }

    public LocationVM(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name; // This ensures the name is displayed in the Spinner
    }
}
