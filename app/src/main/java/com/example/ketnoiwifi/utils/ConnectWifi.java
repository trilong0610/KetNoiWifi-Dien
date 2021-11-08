package com.example.ketnoiwifi.utils;

import static com.example.ketnoiwifi.activitys.MainActivity.mConnectivityManager;
import static com.example.ketnoiwifi.activitys.MainActivity.wifiAdapter;
import static com.example.ketnoiwifi.activitys.MainActivity.wifiManager;
import static com.example.ketnoiwifi.activitys.MainActivity.wifis;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
//import android.net.wifi.WifiNetworkSpecifier;
import android.net.wifi.WifiNetworkSpecifier;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.ketnoiwifi.activitys.MainActivity;
import com.example.ketnoiwifi.utils.Wifi;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;
import com.thanosfisherman.wifiutils.wifiRemove.RemoveErrorCode;
import com.thanosfisherman.wifiutils.wifiRemove.RemoveSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class ConnectWifi extends Application {
    private Context context;
    private String TAG = "Connect Wifi";
    private boolean flagScan = false; // Đánh dấu cho phép cập nhật danh sách wifi
    private boolean flagBruceforce = true; // Đánh dấu cho phép cập nhật danh sách wifi

    ConnectivityManager.NetworkCallback networkCallback;

//    WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);

    public ConnectWifi() {
    }

    public ConnectWifi(Context context) {
        this.context = context;
    }

    public void setFlagScan(boolean flagScan) {
        this.flagScan = flagScan;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void connectToWifi10(String ssid, String password) {
        Log.e(TAG,"connection wifi  Q");

            WifiNetworkSpecifier wifiNetworkSpecifier = new WifiNetworkSpecifier.Builder()
                    .setSsid( ssid )
                    .setWpa2Passphrase(password)
                    .build();

            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .setNetworkSpecifier(wifiNetworkSpecifier)
                    .build();

            networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);

                    mConnectivityManager.bindProcessToNetwork(network);
                    Log.e(TAG,"onAvailable");
                }

                @Override
                public void onLosing(@NonNull Network network, int maxMsToLive) {
                    super.onLosing(network, maxMsToLive);
                    Log.e(TAG,"onLosing");
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    Log.e(TAG, "losing active connection");
                }

                @Override
                public void onUnavailable() {
                    super.onUnavailable();
                    Log.e(TAG,"onUnavailable");
                }
            };
            mConnectivityManager.requestNetwork(networkRequest,networkCallback);

    }

    public void connectToWifi(String ssid, String password) {
            try {
                Log.e(TAG,"connection wifi pre Q");
                WifiConfiguration wifiConfig = new WifiConfiguration();
                wifiConfig.SSID = "\"" + ssid + "\"";
                wifiConfig.preSharedKey = "\"" + password + "\"";
                int netId = wifiManager.addNetwork(wifiConfig);
                wifiManager.disconnect();
                wifiManager.enableNetwork(netId, true);
                wifiManager.reconnect();

            } catch ( Exception e) {
                e.printStackTrace();
            }
        }

    public void scanWifi(){
        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    if (flagScan)
                        scanSuccess();
                } else {
                    // scan failure handling
                    scanFailure();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(wifiScanReceiver, intentFilter);
        boolean success = wifiManager.startScan();
        if (!success) {
            // scan failure handling
            scanFailure();
        }
        else
            scanSuccess();
    }

    private void scanSuccess() {
        List<ScanResult> results = wifiManager.getScanResults();
        if (flagScan){
            Toast.makeText(context, "Đã cập nhật danh sách wifi", Toast.LENGTH_SHORT).show();
            wifis.clear();
            for (ScanResult scanResult :
                    results) {
                wifis.add(new Wifi(scanResult.SSID,"TVNET@123",false));
                wifiAdapter.notifyDataSetChanged();
            }
            setFlagScan(false); //Ngừng cập nhật danh sách wifi
        }

    }

    private void scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        List<ScanResult> results = wifiManager.getScanResults();
        wifis.clear();
        for (ScanResult scanResult :
                results) {
            wifis.add(new Wifi(scanResult.SSID,"TVNET@123",false));
            wifiAdapter.notifyDataSetChanged();
        }
    }

    public String checkSSIDAvailable(Context context,String SSID, ArrayList<Wifi> wifis){
        for (Wifi wifi: wifis) {
            if (wifi.getSsid().equals(SSID))
                return wifi.getPassword();
        }
        return null;
    }
    public boolean _continuteLoop = true;
    public void checkPassWifi(Context context,String SSID, ArrayList<Wifi> wifis){
        for (int i = 0; i < wifis.size(); i++) {
            if (_continuteLoop){
                //Xoa mat khau
                WifiUtils.withContext(context).remove(SSID, new RemoveSuccessListener() {
                    @Override
                    public void success() {
                        Log.i("Status Wifi","Remove success: " + SSID);

                    }

                    @Override
                    public void failed(@NonNull RemoveErrorCode errorCode) {
                        Log.i("Status Wifi","Remove failed: " + SSID);
                    }
                });
                _continuteLoop = false;
                WifiUtils.withContext(context)
                        .connectWith(SSID, wifis.get(i).getPassword())
                        .setTimeout(5000)
                        .onConnectionResult(new ConnectionSuccessListener() {
                            @Override
                            public void success() {
                                Toast.makeText(context, "Kết nối thành công!", Toast.LENGTH_SHORT).show();
                                flagBruceforce = false; // Dung vong lap
                                stopCheckPassWifi();
                                return;
                            }

                            @Override
                            public void failed(@NonNull ConnectionErrorCode errorCode) {
                                Toast.makeText(context, "Kết nối thất bại, đang thử lại!", Toast.LENGTH_SHORT).show();
                                _continuteLoop = true;
                            }
                        })
                        .start();
                Log.e("Status Wifi",wifis.get(i).getPassword());

            }
        }


    }

    public void stopCheckPassWifi(){
        flagBruceforce = false;
    }

    }

