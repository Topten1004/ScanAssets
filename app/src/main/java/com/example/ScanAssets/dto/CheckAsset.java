package com.example.ScanAssets.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CheckAsset {


    @SerializedName("missingList")
    public List<AssetsItem> missingList = new ArrayList<>();

    @SerializedName("unknownList")
    public List<AssetsItem> unknownList = new ArrayList<>();


    public CheckAsset()
    {

    }

    public CheckAsset( List<AssetsItem> _missingList, List<AssetsItem> _unknownList)
    {
        this.missingList = _missingList;
        this.unknownList = _unknownList;
    }
}