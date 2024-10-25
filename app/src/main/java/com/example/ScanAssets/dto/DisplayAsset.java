package com.example.ScanAssets.dto;

import com.google.gson.annotations.SerializedName;

public class DisplayAsset {

    public int id;

    public String name;

    public String barcode;

    public String url;

    public String location_name;

    public DisplayAsset(int _id, String _name, String _barcode, String _location_name, String _url) {

        this.id = _id;
        this.name = _name;
        this.barcode = _barcode;
        this.location_name = _location_name;
        this.url = _url;
    }

}

