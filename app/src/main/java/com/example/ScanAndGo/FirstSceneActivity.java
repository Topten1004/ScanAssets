package com.example.ScanAndGo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ScanAndGo.dto.LocationVM;
import com.example.ScanAndGo.json.JsonTaskGetLocationItemList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class FirstSceneActivity extends BaseActivity{

    AutoCompleteTextView tvLocationName;

    public void btnLogOut(View v)
    {
        Globals.isLogin = false;
        startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_scene);

        tvLocationName = findViewById(R.id.tb_location_name);

        OnLoadLocations();
        if (!Globals.isLogin)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }
    }

    public void OnLoadLocations()
    {
        String req = Globals.apiUrl + "building/read?customer_id=3";
        try {

            List<LocationVM> locations = new ArrayList<>();

            locations = new JsonTaskGetLocationItemList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

            if (locations != null) {

                Globals.locationList = locations;
            }

            List<String> locationNames = new ArrayList<>();
            for (LocationVM location : locations) {
                locationNames.add(location.name);
            }

            // Set up the ArrayAdapter with the location names
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, locationNames);

            // Set the adapter for the AutoCompleteTextView
            tvLocationName.setAdapter(adapter);

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void OnIdentification(View view) {

        Globals.mode = 1;
        if (tvLocationName.getText().length() > 0) {
            boolean flag = false;
            LocationVM location = new LocationVM();

            String locationName = tvLocationName.getText().toString();

            for (int i = 0; i < Globals.locationList.size(); i++) {
                LocationVM temp = Globals.locationList.get(i);

                if (temp.name.equalsIgnoreCase(locationName)) {
                    flag = true;

                    location.id = temp.id;
                    location.name = temp.name;

                    break;
                }
            }

            if (!flag) {
                Toast.makeText(this, "Location name is not correct", Toast.LENGTH_SHORT).show();
                return;
            }

            Globals.selectedLocation = location;

            startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
        }
        else {
            Toast.makeText(this, "Please input the location name", Toast.LENGTH_SHORT).show();
        }
    }


    public void OnCheck(View view) {

        Globals.mode = 2;
        if (tvLocationName.getText().length() > 0) {
            boolean flag = false;
            LocationVM location = new LocationVM();

            String locationName = tvLocationName.getText().toString();

            for (int i = 0; i < Globals.locationList.size(); i++) {
                LocationVM temp = Globals.locationList.get(i);

                if (temp.name.equalsIgnoreCase(locationName)) {
                    flag = true;

                    location.id = temp.id;
                    location.name = temp.name;

                    break;
                }
            }

            if (!flag) {
                Toast.makeText(this, "Location name is not correct", Toast.LENGTH_SHORT).show();
                return;
            }

            Globals.selectedLocation = location;

            startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
        }
        else {
            Toast.makeText(this, "Please input the location name", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnControlAsset(View view) {

        Globals.mode = 3;
        startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
    }
}
