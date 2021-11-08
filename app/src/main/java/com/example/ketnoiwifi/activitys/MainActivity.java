package com.example.ketnoiwifi.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ketnoiwifi.R;
import com.example.ketnoiwifi.utils.ConnectWifi;
import com.example.ketnoiwifi.utils.Wifi;
import com.example.ketnoiwifi.utils.WifiAdapter;
import com.thanosfisherman.wifiutils.WifiUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static ConnectivityManager mConnectivityManager;
    public static WifiManager wifiManager;
    private RecyclerView rcvListWifi;
    public static WifiAdapter wifiAdapter;
    public Context context;
    public static ArrayList<Wifi> wifis;
    ConnectWifi connectWifi;
    ImageView ivScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConnectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        requestPermissions();
        mappingView();
        mappingActionView();
    }

    private void mappingView() {
        rcvListWifi = findViewById(R.id.rcv_main_listWifi);
        ivScan = findViewById(R.id.iv_main_scan);
        wifis = new ArrayList<>();


//        wifis.add(new Wifi("TVNET-VNPT_5G","TVNET@12",true));
//        wifis.add(new Wifi("TVNET-VNPT_2.4G","TVNET@123",true));
        wifis.add(new Wifi("Thanh Dien","31071999",true));

//        wifis.add(new Wifi("TVNET","TVNET@123",false));
        context = getApplication();
        wifiAdapter = new WifiAdapter(wifis,getApplicationContext());
        rcvListWifi.setAdapter(wifiAdapter);
        rcvListWifi.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        connectWifi = new ConnectWifi(this);
        connectWifi.scanWifi();
    }

    private void mappingActionView(){
        ivScan.setOnClickListener(this::onClick);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED){
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CHANGE_NETWORK_STATE}, 104);

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS)
                != PackageManager.PERMISSION_GRANTED){
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_SETTINGS}, 105);

        }

}

    @Override
    public void onClick(View v) {
        if (v == ivScan){
            Toast.makeText(this, "Đang cập nhật Wifi, vui lòng chờ", Toast.LENGTH_SHORT).show();
            connectWifi.setFlagScan(true); //Cho phép cập nhật danh sách wifi
            connectWifi.scanWifi();

        }
    }


}