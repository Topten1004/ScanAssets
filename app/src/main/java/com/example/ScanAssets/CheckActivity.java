package com.example.ScanAssets;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ScanAssets.component.ListItemView;
import com.example.ScanAssets.dto.AssetsItem;
import com.example.ScanAssets.dto.CheckAsset;
import com.example.ScanAssets.dto.CheckBarCode;
import com.example.ScanAssets.dto.MessageVM;
import com.example.ScanAssets.dto.PostCheckTags;
import com.example.ScanAssets.dto.PostGetAssets;
import com.example.ScanAssets.dto.UpdateTagLocation;
import com.example.ScanAssets.json.JsonTaskCheckTag;
import com.example.ScanAssets.json.JsonTaskGetAsset;
import com.example.ScanAssets.json.JsonTaskUpdateTag;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CheckActivity extends BaseActivity{

    public List<AssetsItem> unknownItemList = new ArrayList<>();
    public List<AssetsItem> missingItemList = new ArrayList<>();
    public List<AssetsItem> wrongItemList = new ArrayList<>();

    private ListView missingListView;

    private ListView wrongListView;

    private TextView tvMissingCount;

    private TextView tvWrongLocationCount;

    private TextView tvLocationName;

    private TextView tvUnknownCount;

    private LinearLayout llWrongLocation;

    private LinearLayout llMissingLocation;

    private LinearLayout llUnknownLocation;


    public int type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        missingListView = (ListView)findViewById(R.id.lv_missing_items);

        wrongListView = (ListView)findViewById(R.id.lv_wrong_items);

        tvMissingCount = (TextView)findViewById(R.id.tv_missing_count);

        tvLocationName = (TextView)findViewById(R.id.tv_location_name);

        llWrongLocation = (LinearLayout) findViewById(R.id.lv_wrong_location);

        llMissingLocation = (LinearLayout) findViewById(R.id.lv_missing_location);

        llUnknownLocation = (LinearLayout) findViewById(R.id.lv_unknown_location);

        tvWrongLocationCount = (TextView) findViewById(R.id.tv_wrong_count);

        tvUnknownCount = (TextView) findViewById(R.id.tv_unknown_count);

        if(Globals.mode != 2)
        {
            llWrongLocation.setVisibility(View.GONE);
            llMissingLocation.setVisibility(View.GONE);
            llUnknownLocation.setVisibility(View.GONE);
        }

        CallAPI();
    }

    public void CallAPI()
    {

        if (Globals.mode == 2)
        {
            String req = Globals.apiUrl + "inventory/detect/barcode";

            try {

                PostCheckTags model = new PostCheckTags();

                tvLocationName.setText(Globals.selectedLocation.name);

                model.location_id = Globals.selectedLocation.id;

                if (Globals.mode == 2)
                    model.barcode_list = Globals.tagsList;


                CheckBarCode response = new CheckBarCode();

                Gson gson = new Gson();
                String modelString = gson.toJson(model);

                response = new JsonTaskCheckTag().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, modelString).get();

                if (response != null) {
                    wrongItemList = response.wrongList;
                    missingItemList = response.missingList;
                    unknownItemList = response.unknownList;

                    for (AssetsItem item : wrongItemList) {
                        Log.d("CheckActivity", "Wrong Item URL: " + item.url);
                    }
                    for (AssetsItem item : missingItemList) {
                        Log.d("CheckActivity", "Missing Item URL: " + item.url);
                    }
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else if(Globals.mode == 3) {

            String req = Globals.apiUrl + "inventory/read/bybarcode";

            try {

                PostGetAssets model = new PostGetAssets();

                tvLocationName.setText(Globals.selectedLocation.name);
                model.barcode_list = Globals.tagsList;

                CheckAsset response = new CheckAsset();

                Gson gson = new Gson();
                String modelString = gson.toJson(model);

                response = new JsonTaskGetAsset().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, modelString).get();

                if (response != null) {

                    missingItemList = response.missingList;
                    unknownItemList = response.unknownList;
                }

            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        ListItemView missingAdapter = new ListItemView(this, missingItemList);
        missingListView.setAdapter(missingAdapter);

        ListItemView wrongAdapter = new ListItemView(this, wrongItemList);
        wrongListView.setAdapter(wrongAdapter);

        if (missingItemList.size() == 0)
        {
            missingListView.setVisibility(View.GONE);
        }

        if (Globals.mode == 3)
        {
            wrongListView.setVisibility(View.GONE);
            tvUnknownCount.setText(String.valueOf(unknownItemList.size()) + "/" + String.valueOf(Globals.tagsList.size()));

            llUnknownLocation.setVisibility(View.VISIBLE);
        }

        if(Globals.mode == 2)
        {
            tvMissingCount.setText( String.valueOf(missingItemList.size()) + "/" + String.valueOf(Globals.tagsList.size()));
            tvWrongLocationCount.setText( String.valueOf(wrongItemList.size()) +"/" + String.valueOf(Globals.tagsList.size()));
            tvUnknownCount.setText(String.valueOf(unknownItemList.size()) + "/" + String.valueOf(Globals.tagsList.size()));

            if(missingItemList.size() == 0)
            {
                missingListView.setVisibility(View.GONE);
            }

            if(wrongItemList.size() == 0)
            {
                wrongListView.setVisibility(View.GONE);
            }
        }
    }

    public void OnFixWrongLocation(View view)
    {
        String req = Globals.apiUrl + "inventory/update/bybarcode?location_id=" + Globals.selectedLocation.id;

        UpdateTagLocation model = new UpdateTagLocation();

        MessageVM response = new MessageVM();

        if (Globals.selectedWrongTagsList.size() > 0)
        {
            model.barcode_list = Globals.selectedWrongTagsList;

            Gson gson = new Gson();
            String modelString = gson.toJson(model);

            try {
                response = new JsonTaskUpdateTag().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, modelString).get();

                if (response != null) {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show();
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            Toast.makeText(this, "There is no barcode to update", Toast.LENGTH_SHORT).show();
        }

        CallAPI();

        updateAdapters();
    }

    private void updateAdapters() {
        // Update missing items adapter
        ListItemView missingAdapter = new ListItemView(this, missingItemList);
        missingListView.setAdapter(missingAdapter);

        // Update wrong items adapter
        ListItemView wrongAdapter = new ListItemView(this, wrongItemList);
        wrongListView.setAdapter(wrongAdapter);

        // Show or hide missingListView based on the size of missingItemList
        missingListView.setVisibility(missingItemList.size() == 0 ? View.GONE : View.VISIBLE);

        // Show or hide wrongListView based on the size of wrongItemList
        wrongListView.setVisibility(wrongItemList.size() == 0 ? View.GONE : View.VISIBLE);

        // Update TextView counts
        tvMissingCount.setText(String.format("%d/%d", missingItemList.size(), Globals.tagsList.size()));
        tvWrongLocationCount.setText(String.format("%d/%d", wrongItemList.size(), Globals.tagsList.size()));
        tvUnknownCount.setText(String.format("%d/%d", unknownItemList.size(), Globals.tagsList.size()));
    }


    public void OnBackSearchScreen(View view) {

        Globals.tagsList.clear();
        onBackPressed();
    }
}