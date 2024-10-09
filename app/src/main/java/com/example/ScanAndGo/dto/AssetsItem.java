package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class AssetsItem {

    @SerializedName("id")
    public int id;

    @SerializedName("barcode")
    public String barcode;

    @SerializedName("url")
    public String url;

    public AssetsItem(int _id, String _barcode, String _url) {

        this.id = _id;
        this.barcode = _barcode;
        this.url = _url;
    }
}
