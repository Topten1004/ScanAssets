package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class PostAsset {

    public PostAsset(int _location_id, int _category_id, String _name, String _rfid, String _barcode, byte[] _imageData)
    {
        this.locationId = _location_id;
        this.categoryId = _category_id;
        this.name = _name;
        this.rfid = _rfid;
        this.barcode = _barcode;
        this.imageData = _imageData;
    }

    @SerializedName("locationId")
    public int locationId;

    @SerializedName("categoryId")
    public int categoryId;

    @SerializedName("name")
    public String name;

    @SerializedName("rfid")
    public String rfid;

    @SerializedName("barcode")
    public String barcode;

    @SerializedName("imgData")
    public byte[] imageData;


    public PostAsset() {

    }
}
