package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CheckBarCode {

    @SerializedName("wrongList")
    public List<AssetsItem> wrongList = new ArrayList<>();

    @SerializedName("missingList")
    public List<AssetsItem> missingList = new ArrayList<>();


    public CheckBarCode()
    {

    }

    public CheckBarCode(List<AssetsItem> _wrongList, List<AssetsItem> _missingList)
    {
        this.missingList = _missingList;
        this.wrongList = _wrongList;
    }
}
