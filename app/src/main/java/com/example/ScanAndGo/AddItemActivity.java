package com.example.ScanAndGo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.ScanAndGo.dto.PostAsset;
import com.example.ScanAndGo.dto.StatusVM;
import com.example.ScanAndGo.json.JsonTaskPostAsset;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AddItemActivity extends BaseActivity{

    private static final int CAMERA_REQUEST = 1888 ;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    public ImageView assetImage;

    public int categoryId = 0;

    TextView locationName;
    TextView tvRfidTag;

    public EditText etAssetName;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
        }

        assetImage = findViewById(R.id.iv_asset_image);

        locationName = findViewById(R.id.tv_add_item_location);
        locationName.setText(Globals.selectedLocation.name);

        etAssetName = findViewById(R.id.et_asset_name);
        tvRfidTag = findViewById(R.id.tv_rfid);

        tvRfidTag.setText("RFID tag:" + Globals.tagsList.get(0));
    }

    public void OnBack(View view) {

        Globals.tagsList.clear();
        onBackPressed();

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied, inform the user
                Toast.makeText(this, "Camera permission is required to capture images", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress the bitmap to JPEG format with 100% quality
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public void OnAssetImageCapture(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            assetImage.setImageBitmap(photo);

            // Convert Bitmap to byte[]
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            // Encode byte[] to Base64 String
            String base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);

            // Store the Base64 string as needed
            Globals.selectedImage = base64String;
            Globals.mode = 1;

        }
    }

    public void OnAddAssets(View view) {

        if(Globals.selectedLocation.id > 0)
        {
            if(Globals.selectedImage.length() == 0)
            {
                Toast.makeText(this, "Selected assets doesn't have image.", Toast.LENGTH_SHORT).show();
                return ;
            }

            if( Globals.tagsList.size() == 0 )
            {
                Toast.makeText(this, "Tag doesn't exists.", Toast.LENGTH_SHORT).show();
                return ;
            }

            String rfid = Globals.tagsList.get(0);
            String name = etAssetName.getText().toString();

            PostAsset model = new PostAsset(rfid, 1, Globals.selectedImage, "", name, Globals.selectedLocation.id, 3 );

            Globals g = (Globals)getApplication();
            String req = g.apiUrl + "inventory/create";

            Gson gson = new Gson();
            String modelString = gson.toJson(model);

            try {

                StatusVM result = new JsonTaskPostAsset().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, modelString).get();

                if(result.status > 1)
                {
                    Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Item not added successfully!", Toast.LENGTH_SHORT).show();
                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "Selected location doesn't exists.", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnIdentificationItem(View view)
    {
        Globals.tagsList.clear();
        onBackPressed();
    }
}
