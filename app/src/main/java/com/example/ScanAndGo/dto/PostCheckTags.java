package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PostCheckTags {

    @SerializedName("location_id")
    public int locationId;

    @SerializedName("barcode_list")

    public List<String> barcodes = new ArrayList<>();

    public PostCheckTags()
    {
        barcodes = new ArrayList<>();
    }

    public PostCheckTags(int _locationId, List<String> barcodes)
    {
        this.locationId = _locationId;
        this.barcodes = barcodes;
    }
}
