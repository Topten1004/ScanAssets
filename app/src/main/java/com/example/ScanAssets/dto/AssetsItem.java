package com.example.ScanAssets.dto;

import com.google.gson.annotations.SerializedName;

public class AssetsItem {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("location_name")
    public String location_name;

    @SerializedName("barcode")
    public String barcode;

    @SerializedName("url")
    public String url;

    public AssetsItem(int _id, String _name, String _location_name, String _barcode, String _url) {

        this.id = _id;
        this.name = _name;
        this.location_name = _location_name;
        this.barcode = _barcode;
        this.url = _url;
    }
}
