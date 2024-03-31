package com.example.scratch.api.pojo;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("application")
    public String application;
    @SerializedName("time")
    public String time;

    public User(String application, String time) {
        this.application = application;
        this.time = time;
    }
    public String getApplication() {
        return this.application;
    }

    // getter method for returning the ID of the TextView 1
    public String getTime() {
        return this.time;
    }
}