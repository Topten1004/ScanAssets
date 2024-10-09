package com.example.ScanAndGo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ScanAndGo.component.ListItemView;
import com.example.ScanAndGo.dto.AssetsItem;
import com.example.ScanAndGo.dto.PostCheckTags;
import com.example.ScanAndGo.json.JsonTaskCheckTag;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CheckActivity extends BaseActivity{

    public List<AssetsItem> missingItemList = new ArrayList<>();

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

    }

    public void CallAPI()
    {
        String req = Globals.apiUrl + "inventory/detect/barcode";

        try {

            PostCheckTags model = new PostCheckTags();

            tvLocationName.setText(Globals.selectedLocation.name);

            model.location_id = Globals.selectedLocation.id;

            if (Globals.mode == 2)
                model.barcode_list = Globals.tagsList;

            List<AssetsItem> response = new ArrayList<>();

            Gson gson = new Gson();
            String modelString = gson.toJson(model);

            response = new JsonTaskCheckTag().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req, modelString).get();

            if (response != null) {

                missingItemList = response;
            }

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ListItemView missingAdapter = new ListItemView(this, missingItemList);
        // Set the adapter for the ListView
        missingListView.setAdapter(missingAdapter);

        tvMissingCount.setText( missingItemList.size() + " assets");
    }
}
