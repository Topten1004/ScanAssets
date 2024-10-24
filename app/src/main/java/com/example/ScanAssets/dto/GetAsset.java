package com.example.ScanAssets.dto;

import com.google.gson.annotations.SerializedName;

public class GetAsset {


    @SerializedName("id")
    public int id;

    @SerializedName("asset_name")
    public String asset_name;

    @SerializedName("location_name")
    public String location_name;

    @SerializedName("url")
    public String url;

    public GetAsset() {

    }

    public GetAsset(int _id, String _asset_name, String _location_name, String _url) {

        this.id = _id;
        this.asset_name = _asset_name;
        this.location_name = _location_name;
        this.url = _url;
    }
}
