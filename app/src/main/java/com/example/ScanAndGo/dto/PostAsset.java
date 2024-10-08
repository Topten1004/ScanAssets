package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class PostAsset {

    public PostAsset(String _barcode, int _status, byte[] _imageData, String _comment, String _name, int _location_id, int _customer_id)
    {
        this.barcode = _barcode;
        this.status = _status;
        this.photo = _imageData;
        this.comment = _comment;
        this.name = _name;
        this.locationId = _location_id;
        this.customerId = _customer_id;

    }

    @SerializedName("barcode")
    public String barcode;

    @SerializedName("status")
    public int status;

    @SerializedName("photo")
    public byte[] photo;

    @SerializedName("comment")
    public String comment;

    @SerializedName("asset_name")
    public String name;

    @SerializedName("location_id")
    public int locationId;

    @SerializedName("customer_id")
    public int customerId;

    public PostAsset() {

    }
}
