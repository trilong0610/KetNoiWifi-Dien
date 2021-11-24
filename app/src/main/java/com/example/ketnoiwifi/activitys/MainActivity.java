package com.example.ketnoiwifi.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ketnoiwifi.R;
import com.example.ketnoiwifi.model.Wifi;
import com.example.ketnoiwifi.utils.ConnectWifi;
import com.example.ketnoiwifi.utils.WifiAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static ConnectivityManager mConnectivityManager;
    public static WifiManager wifiManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private RecyclerView rcvListWifi;
    public static WifiAdapter wifiAdapter;
    public Context context;
    private TextView txtTittle;
    public static ArrayList<Wifi> wifis;
    public static LinearProgressIndicator progressMain;
    ConnectWifi connectWifi;
    ImageView ivScan;

//    BottomSheet thông báo
// get the bottom sheet view

    // init the bottom sheet behavior
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConnectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // Yêu cầu cấp quyền truy cập vị trí
        requestPermissions();

        mappingView();

        mappingActionView();
    }

    private void mappingView() {
        rcvListWifi = findViewById(R.id.rcv_main_listWifi); // Danh sách để hiển thị wifi

        ivScan = findViewById(R.id.iv_main_scan); // Nút scan

        txtTittle = findViewById(R.id.txt_main_tittle); // "Kết nối wifi"

        progressMain = findViewById(R.id.progress_linear_main);

        wifis = new ArrayList<>();

//        wifis.add(new Wifi("TVNET-VNPT_5G","TVNET@12",true));
//        wifis.add(new Wifi("TVNET-VNPT_2.4G","TVNET@123",true));
//        wifis.add(new Wifi("TVNET","TVNET@123",false));
        context = getApplication();
        wifiAdapter = new WifiAdapter(wifis,MainActivity.this);
        rcvListWifi.setAdapter(wifiAdapter);
        rcvListWifi.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        connectWifi = new ConnectWifi(this);

    }

    private void mappingActionView(){
        ivScan.setOnClickListener(this::onClick);
        txtTittle.setOnClickListener(this::onClick);
    }

    private void requestPermissions(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED  ){
            requestPermissions(new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return ;
        }
    }


    private int countTap = 0; // Đủ 10 lần nhấp chuyển vào Activity Manager Password
    @Override
    public void onClick(View v) {
        if (v == ivScan){
            Toast.makeText(this, "Đang cập nhật Wifi, vui lòng chờ", Toast.LENGTH_SHORT).show();
            connectWifi.setFlagScan(true); //Cho phép cập nhật danh sách wifi
            connectWifi.scanWifi();

        }
        if (v == txtTittle){
            countTap++;
            Log.e("countTap",String.valueOf(countTap));
            if (countTap >= 5){
                Intent intent = new Intent(this,ManagerActivity.class);
                startActivity(intent);
                countTap = 0;
            }
        }
    }

    public void showDialog(String tittle, String message){
        new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("Dialog")
                .setMessage("Lorem ipsum dolor ....")
                .setPositiveButton("Ok", /* listener = */ null)
                .setNegativeButton("Cancel", /* listener = */ null)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    // Permission Denied
                    Toast.makeText( this,"Vui lòng cấp quyền vị trí" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}