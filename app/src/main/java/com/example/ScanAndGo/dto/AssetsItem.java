package com.example.ScanAndGo.dto;

public class AssetsItem {

    public int id;

    public byte[] photo;

    public String rfid;

    public AssetsItem(int _id, String _rfid, byte[] _photo) {

        this.id = _id;
        this.rfid = _rfid;
        this.photo = _photo;
    }

}
