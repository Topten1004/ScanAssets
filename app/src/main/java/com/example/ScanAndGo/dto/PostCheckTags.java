package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PostCheckTags {

    @SerializedName("location_id")
    public int location_id;

    @SerializedName("barcode_list")

    public List<String> barcode_list = new ArrayList<>();

    public PostCheckTags()
    {
        barcode_list = new ArrayList<>();
    }

    public PostCheckTags(int _locationId, List<String> barcodes)
    {
        this.location_id = _locationId;
        this.barcode_list = barcodes;
    }
}
