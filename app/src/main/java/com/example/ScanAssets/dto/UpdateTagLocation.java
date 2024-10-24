package com.example.ScanAssets.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UpdateTagLocation {

    @SerializedName("barcode_list")

    public List<String> barcode_list = new ArrayList<>();

    public UpdateTagLocation(List<String> barcodes)
    {
        this.barcode_list = barcodes;
    }

    public UpdateTagLocation()
    {

    }
}