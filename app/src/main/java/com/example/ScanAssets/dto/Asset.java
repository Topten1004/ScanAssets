package com.example.ScanAssets.dto;

import com.google.gson.annotations.SerializedName;

public class Asset  implements  Comparable<Asset> {

    @SerializedName("id")
    public int id;

    @SerializedName("name")

    public String name;

    @SerializedName("barcode")

    public String barcode;

    @SerializedName("photo")

    public byte[] photo;

    @Override
    public int compareTo(Asset otherItem) {
        // Compare by id
        return Integer.compare(this.id, otherItem.id);
    }


    public Asset()
    {

    }

    public String getName()
    {
        return this.name;
    }

    public int getId()
    {
        return this.id;
    }

    public Asset(int id, String name, String barcode, byte[] photo) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.photo = photo;
    }
}
