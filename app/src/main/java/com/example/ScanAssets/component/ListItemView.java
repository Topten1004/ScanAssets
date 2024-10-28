package com.example.ScanAssets.component;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ScanAssets.Globals;
import com.example.ScanAssets.R;
import com.example.ScanAssets.dto.AssetsItem;

import java.util.List;

public class ListItemView extends ArrayAdapter<AssetsItem> {

    private SparseBooleanArray selectedItems = new SparseBooleanArray();

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

        // Load image using Glide
        Glide.with(this.getContext()).load(item.url).into(assetImage);

        // Set text fields
        tvRfid.setText(item.barcode);
        tvName.setText(item.name);
        tvLocationName.setText(item.location_name);

        // Check if the item is selected and update background
        convertView.setActivated(selectedItems.get(position, false));

        // Toggle selection on click
        View finalConvertView = convertView;
        convertView.setOnClickListener(v -> {
            if (selectedItems.get(position, false)) {
                // Item is selected, so unselect it and remove from selectedWrongTagsList
                selectedItems.delete(position);
                Globals.selectedWrongTagsList.remove(item.barcode); // Remove tag from global list
                finalConvertView.setActivated(false);
            } else {
                // Item is not selected, so select it and add to selectedWrongTagsList
                selectedItems.put(position, true);
                Globals.selectedWrongTagsList.add(item.barcode); // Add tag to global list
                finalConvertView.setActivated(true);
            }
            notifyDataSetChanged(); // Refresh list to update backgrounds
        });

        return convertView;
    }
}