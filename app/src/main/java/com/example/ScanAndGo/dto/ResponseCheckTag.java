package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseCheckTag {

    @SerializedName("missing_list")
    public List<String> missingList = new ArrayList<>();

    public ResponseCheckTag() {
    }

    public ResponseCheckTag(List<String> _missingLists) {
        this.missingList = _missingLists;
    }
}
