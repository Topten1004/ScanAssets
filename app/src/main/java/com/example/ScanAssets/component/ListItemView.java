package com.example.ScanAssets.component;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ScanAssets.R;
import com.example.ScanAssets.dto.AssetsItem;

import java.util.List;

public class ListItemView extends ArrayAdapter<AssetsItem> {

    public int id;

    public String barcode;

    public String url;

    public String name;

    public String location_name;

    public ListItemView(@NonNull Context context, @NonNull List<AssetsItem> objects) {

        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view, parent, false);
        }

        AssetsItem item = getItem(position);

        ImageView assetImage = convertView.findViewById(R.id.iv_missing_asset_image);
        TextView tvRfid = convertView.findViewById(R.id.tv_asset_rfid);
        TextView tvName = convertView.findViewById(R.id.tv_list_asset_name);
        TextView tvLocationName = convertView.findViewById(R.id.tv_list_location_name);

        id = item.id;

        Glide.with(this.getContext())
                .load(item.url)
                .into(assetImage);

        barcode = item.barcode;

        name = item.name;

        location_name = item.location_name;

        tvRfid.setText(barcode);
        tvName.setText(name);
        tvLocationName.setText(location_name);

        return convertView;
    }
}
