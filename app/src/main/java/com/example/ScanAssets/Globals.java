package com.example.ScanAssets;

import android.app.Application;
import android.os.AsyncTask;

import com.example.ScanAssets.component.Connectivity;
import com.example.ScanAssets.component.NetworkTask;
import com.example.ScanAssets.dto.LocationVM;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Globals extends Application {

    public static int mode = 0;
    public static LocationVM selectedLocation = new LocationVM();
    public static boolean isLogin = false;
    public static String dns = "";
    public static Boolean dispoAPI = false;

    public static String url = "https://api.scanassets.com/";

    public static List<LocationVM> locationList = new ArrayList<>();
    public static List<String> tagsList = new ArrayList<>();
    public static List<String> barcodeList = new ArrayList<>();
    public static List<String> unknownItems = new ArrayList<>();

    public static List<String> selectedWrongTagsList = new ArrayList<>();

    public static String apiUrl = url + "api/";

    public static String nowBarCode;

    public static String selectedImage;

    public static int selectedTagId = 0;

    @SuppressWarnings("deprecation")

    public boolean isNetworkConnected() {
        if(Connectivity.isConnected(getApplicationContext())){
            dispoAPI = false;
            try {
                dispoAPI = new NetworkTask().executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR,dns).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        return  false;
    }
}
