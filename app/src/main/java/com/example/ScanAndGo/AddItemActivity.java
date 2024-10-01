package com.example.ScanAndGo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.ScanAndGo.dto.Category;
import com.example.ScanAndGo.dto.CheckItem;
import com.example.ScanAndGo.dto.PostAsset;
import com.example.ScanAndGo.dto.StatusVM;
import com.example.ScanAndGo.json.JsonTaskGetCategoryList;
import com.example.ScanAndGo.json.JsonTaskPostAsset;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddItemActivity extends BaseActivity{

    private static final int CAMERA_REQUEST = 1888 ;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    public ImageView assetImage;

    public int categoryId = 0;

    public Spinner categoryList;

    TextView locationName;
    TextView tvRfidTag;

    public ArrayList<CheckItem> itemLists = new ArrayList<>();

    public EditText etAssetName;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
        }

        Intent intent = getIntent();

        assetImage = findViewById(R.id.iv_asset_image);

        locationName = findViewById(R.id.tv_add_item_location);
        locationName.setText(Globals.selectedLocation.name);

        etAssetName = findViewById(R.id.et_asset_name);
        tvRfidTag = findViewById(R.id.tv_rfid);

        tvRfidTag.setText("RFID tag:" + Globals.tagsList.get(0));
    }

    public void OnBack(View view) {

        startActivityForResult(new Intent(getApplicationContext(), FirstSceneActivity.class), 0);
    }

    public void onReadCategory()
    {
        String req = Globals.apiUrl + "category/read";

        try {

            List<Category> categories = new ArrayList<>();

            categories = new JsonTaskGetCategoryList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            Collections.sort(categories);

            if (categories != null) {

                List<Category> finalCategories = categories;
                ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, finalCategories) {
                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                        textView.setText(finalCategories.get(position).getName()); // Assuming getName() returns the category name
                        return textView;
                    }
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView textView = (TextView) super.getView(position, convertView, parent);
                        textView.setText(getItem(position).getName()); // Assuming getName() returns the item name
                        return textView;
                    }
                };

                // Specify the layout to use when the list of choices appears
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                categoryList.setAdapter(categoryAdapter);
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        categoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Retrieve the selected category
                Category selectedCategory = (Category) parentView.getItemAtPosition(position);

                // Call API to fetch items based on the selected category
                if (selectedCategory != null) {
                    categoryId = selectedCategory.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if nothing is selected
            }
        });
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            assetImage.setImageBitmap(photo);

            // Store the image in a byte array (optional, for further use)
            Globals.selectedImage = bitmapToByteArray(photo);
            Globals.mode = 1;
        }
    }

    public void OnAddAssets(View view) {

        if(Globals.selectedLocation.id > 0 && categoryId > 0)
        {
            String rfid = Globals.tagsList.get(0);
            String barcode = Globals.barcodeList.get(0);
            String name = etAssetName.getText().toString();

            PostAsset model = new PostAsset(Globals.selectedLocation.id, categoryId, name, rfid, barcode, Globals.selectedImage );

            Globals g = (Globals)getApplication();
            String req = g.apiUrl + "user/signin";

            Gson gson = new Gson();
            String modelString = gson.toJson(model);

            try {

                StatusVM result = new JsonTaskPostAsset().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, modelString).get();

                if(result.status == 1)
                {

                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void OnIdentificationItem(View view) {
        startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
    }
}