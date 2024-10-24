package com.example.ScanAssets.dto;

import com.google.gson.annotations.SerializedName;

public class StatusVM {

    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;

    public StatusVM() {

    }

    public StatusVM(int status, String message){
        this.status = status;
        this.message = message;
    }
}
