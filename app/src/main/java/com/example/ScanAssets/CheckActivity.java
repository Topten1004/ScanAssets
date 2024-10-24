package com.example.ScanAssets;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ScanAssets.component.ListItemView;
import com.example.ScanAssets.dto.Asset;
import com.example.ScanAssets.dto.AssetsItem;
import com.example.ScanAssets.dto.CheckBarCode;
import com.example.ScanAssets.dto.GetAsset;
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
                }

            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else {

            String req = Globals.apiUrl + "inventory/read/bybarcode";

            try {

                PostGetAssets model = new PostGetAssets();

                tvLocationName.setText(Globals.selectedLocation.name);
                model.barcode_list = Globals.tagsList;

                List<GetAsset> response = new ArrayList<>();

                Gson gson = new Gson();
                String modelString = gson.toJson(model);

                response = new JsonTaskGetAsset().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, modelString).get();

                if (response != null) {

                    for(int i = 0; i < response.size(); i++)
                    {
                        GetAsset selected = response.get(i);

                        AssetsItem temp = new AssetsItem(selected.id, selected.asset_name, "", selected.url);
                        missingItemList.add(temp);
                    }
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
            tvLocationName.setText("No data for this tag: " + Globals.tagsList.get(Globals.selectedTagId));
            missingListView.setVisibility(View.GONE);
        }

        if (Globals.mode == 3)
        {
            wrongListView.setVisibility(View.GONE);
        }

        if(Globals.mode == 2)
        {
            tvMissingCount.setText( String.valueOf(missingItemList.size()));
            tvWrongLocationCount.setText( String.valueOf(wrongItemList.size()));

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

        for (int i = 0; i < wrongItemList.size(); i++ )
            model.barcode_list.add(wrongItemList.get(i).barcode);

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
    }

    public void OnBackSearchScreen(View view) {

        Globals.tagsList.clear();
        onBackPressed();
    }
}