package com.example.ketnoiwifi.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.ketnoiwifi.R;
import com.example.ketnoiwifi.model.Wifi;
import com.example.ketnoiwifi.utils.WifiAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rcvListWifi;
    private WifiAdapter wifiAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();
        mappingView();
    }

    private void mappingView() {


        rcvListWifi = findViewById(R.id.rcv_main_listWifi);
        ArrayList<Wifi> wifis = new ArrayList<>();
        wifis.add(new Wifi("TVNET_5G","TVNET@123",true));
        wifis.add(new Wifi("TVNET_2.4G","TVNET@123",true));
        wifis.add(new Wifi("TVNET","TVNET@123",false));
        wifiAdapter = new WifiAdapter(wifis,this);
        rcvListWifi.setAdapter(wifiAdapter);
        rcvListWifi.setLayoutManager(new LinearLayoutManager(this));
    }

    private void requestPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED){
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 100);

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED){
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 101);

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED){
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CHANGE_WIFI_STATE}, 102);

        }

}
}