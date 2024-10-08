package com.example.ScanAndGo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ScanAndGo.component.ListItemView;
import com.example.ScanAndGo.dto.ButtonItem;
import com.example.ScanAndGo.dto.ResponseCheckTag;
import com.example.ScanAndGo.dto.PostCheckTags;
import com.example.ScanAndGo.json.JsonTaskCheckTag;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CheckActivity extends BaseActivity{

    private List<ButtonItem> missingItemList = new ArrayList<>();

    private String[] missingListValues;

    private ListView missingListView;

    private TextView tvMissingCount;

    private TextView tvLocationName;

    public int type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        missingListView = (ListView)findViewById(R.id.lv_missing_items);

        tvMissingCount = (TextView)findViewById(R.id.tv_missing_count);

        tvLocationName = (TextView)findViewById(R.id.tv_location_name);

        CallAPI();

        Globals.unknownItems.clear();
    }

    public void CallAPI()
    {
        Globals g = (Globals) getApplication();

        String req = g.apiUrl + "inventory/detect/barcode";

        try {
            PostCheckTags model = new PostCheckTags();

            model.barcodes = Globals.tagsList;

            ResponseCheckTag response = new ResponseCheckTag();

            Gson gson = new Gson();
            String modelString = gson.toJson(model);

            response = new JsonTaskCheckTag().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, modelString).get();

            if (response != null) {

            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ListItemView missingAdapter = new ListItemView(this, missingItemList, null);
        // Set the adapter for the ListView
        missingListView.setAdapter(missingAdapter);

        tvMissingCount.setText(missingItemList.size() + " assets");
    }
}
