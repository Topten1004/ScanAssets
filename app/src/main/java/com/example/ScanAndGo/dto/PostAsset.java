package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class PostAsset {

    public PostAsset(String _barcode, int _status, String _imageData, String _comment, String _name, int _location_id, int _customer_id)
    {
        this.barcode = _barcode;
        this.status = _status;
        this.photo = _imageData;
        this.comment = _comment;
        this.asset_name = _name;
        this.location_id = _location_id;
        this.customer_id = _customer_id;

    }

    @SerializedName("barcode")
    public String barcode;

    @SerializedName("status")
    public int status;

    @SerializedName("photo")
    public String photo;

    @SerializedName("comment")
    public String comment;

    @SerializedName("asset_name")
    public String asset_name;

    @SerializedName("location_id")
    public int location_id;

    @SerializedName("customer_id")
    public int customer_id;

    public PostAsset() {

    }
}
