package com.example.ScanAndGo;

import android.app.Application;
import android.os.AsyncTask;

import com.example.ScanAndGo.component.Connectivity;
import com.example.ScanAndGo.component.NetworkTask;
import com.example.ScanAndGo.dto.LocationVM;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Globals extends Application {

    public static int mode = 0;
    public static LocationVM selectedLocation = new LocationVM();
    public static boolean isLogin = false;
    public static String dns = "";
    public static Boolean dispoAPI = false;

    public static String url = "https://api.inventaire.scanandgo.nc/";

    public static List<LocationVM> locationList = new ArrayList<>();
    public static List<String> tagsList = new ArrayList<>();
    public static List<String> barcodeList = new ArrayList<>();
    public static List<String> unknownItems = new ArrayList<>();

    public static String apiUrl = url + "api/";
    public static int categoryId = 0;

    public static int selectedLocationId = 0;

    public static int checkedItem = 0;
    public static String nowBarCode;

    public static byte[] selectedImage;
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
