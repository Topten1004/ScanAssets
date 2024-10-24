package com.example.ScanAssets.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PostGetAssets {

    @SerializedName("barcode_list")

    public List<String> barcode_list = new ArrayList<>();

    public PostGetAssets()
    {
        barcode_list = new ArrayList<>();
    }

    public PostGetAssets(List<String> barcodes)
    {
        this.barcode_list = barcodes;
    }

}