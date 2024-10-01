package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("customer_id")

    public int customer_id;

    @SerializedName("username")

    public String username;

    @SerializedName("password")

    public String password;

    public Login()
    {

    }
    public Login(int _customer_id, String _username, String _password)
    {
        customer_id = _customer_id;
        username = _username;
        password = _password;
    }
}
