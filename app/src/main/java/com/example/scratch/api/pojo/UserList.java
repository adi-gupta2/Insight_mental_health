package com.example.scratch.api.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserList {

    public List<Datum> data = new ArrayList();

    public class Datum {

        @SerializedName("application")
        public String application;
        @SerializedName("hour")
        public String hour;
        @SerializedName("minutes")
        public String minutes;
        @SerializedName("seconds")
        public String seconds;

    }
}