package com.example.ScanAndGo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ScanAndGo.component.ListItemView;
import com.example.ScanAndGo.dto.AssetsItem;
import com.example.ScanAndGo.dto.CheckBarCode;
import com.example.ScanAndGo.dto.GetAsset;
import com.example.ScanAndGo.dto.MessageVM;
import com.example.ScanAndGo.dto.PostCheckTags;
import com.example.ScanAndGo.dto.UpdateTagLocation;
import com.example.ScanAndGo.json.JsonTaskCheckTag;
import com.example.ScanAndGo.json.JsonTaskGetAsset;
import com.example.ScanAndGo.json.JsonTaskUpdateTag;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CheckActivity extends BaseActivity{

    public List<AssetsItem> missingItemList = new ArrayList<>();
    public List<AssetsItem> wrongItemList = new ArrayList<>();

    private ListView missingListView;

    private TextView tvMissingCount;

    private TextView tvLocationName;

    private LinearLayout llWrongLocation;

    private LinearLayout llMissingLocation;

    private TextView tvWrongLocationCount;

    public int type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        missingListView = (ListView)findViewById(R.id.lv_missing_items);

        tvMissingCount = (TextView)findViewById(R.id.tv_missing_count);

        tvLocationName = (TextView)findViewById(R.id.tv_location_name);

        llWrongLocation = (LinearLayout) findViewById(R.id.lv_wrong_location);

        llMissingLocation = (LinearLayout) findViewById(R.id.lv_missing_location);

        tvWrongLocationCount = (TextView) findViewById(R.id.tv_wrong_count);

        if(Globals.mode != 2)
        {
            llWrongLocation.setVisibility(View.GONE);
            llMissingLocation.setVisibility(View.GONE);
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
                }

            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        else {
            String req = Globals.apiUrl + "inventory/read/bybarcode?barcode=" + Globals.tagsList.get(0);

            try {

                GetAsset response = new GetAsset();

                response = new JsonTaskGetAsset().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req).get();

                if (response != null) {

                    AssetsItem temp = new AssetsItem(response.id, Globals.tagsList.get(0), response.url);
                    missingItemList.add(temp);

                }

            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            ListItemView missingAdapter = new ListItemView(this, missingItemList);
            missingListView.setAdapter(missingAdapter);

            tvMissingCount.setText( missingItemList.size());
            tvWrongLocationCount.setText( wrongItemList.size());
        }

        ListItemView missingAdapter = new ListItemView(this, missingItemList);
        missingListView.setAdapter(missingAdapter);

        if(Globals.mode == 2)
        {
            tvMissingCount.setText( String.valueOf(missingItemList.size()));
            tvWrongLocationCount.setText( String.valueOf(wrongItemList.size()));
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
}