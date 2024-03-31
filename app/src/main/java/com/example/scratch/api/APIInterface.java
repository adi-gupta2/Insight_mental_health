package com.example.scratch.api;

import com.example.scratch.api.pojo.MultipleResource;
import com.example.scratch.api.pojo.UserList;
import com.example.scratch.api.pojo.User;


import java.util.List;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {
        @GET("/api/unknown")
        Call<MultipleResource> doGetListResources();

        @POST("/api/users")
        Call<User> createUser(@Body List<User> userRequest);

        @GET("/api/users?")
        Call<UserList> doGetUserList(@Query("page") String page);

        @FormUrlEncoded
        @POST("/api/users?")
        Call<UserList> doCreateUserWithField(@Field("username") String username);
}

