package com.example.ScanAndGo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ScanAndGo.dto.LocationVM;


public class FirstSceneActivity extends BaseActivity{

    EditText tvLocationName;

    public void btnLogOut(View v)
    {
        Globals g = (Globals) getApplication();

        g.isLogin = false;

        startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_scene);

        Globals g = (Globals)getApplication();

        tvLocationName = findViewById(R.id.tb_location_name);

        if (g.isLogin == false)
        {
            startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 0);
        }
    }

    public void OnIdentification(View view) {

        if(tvLocationName.getText().length() > 0)
        {
            LocationVM location = new LocationVM();

            location.id = 1;
            location.name = tvLocationName.getText().toString();

            Globals.selectedLocation = location;
            Globals.mode = 1;
            startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
        }
        else {
            Toast.makeText(this, "Please input the location name", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnCheck(View view) {

        Globals.mode = 2;
        startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
    }

    public void OnControlAsset(View view) {

        Globals.mode = 3;
        startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 0);
    }
}
