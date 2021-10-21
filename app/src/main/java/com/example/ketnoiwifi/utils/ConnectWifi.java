package com.example.ketnoiwifi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class ConnectWifi {
    private Context context;

    public ConnectWifi() {
    }

    public ConnectWifi(Context context) {
        this.context = context;
    }

    public boolean connectToWifi(String networkSSID, String networkPass) {
        NetworkCapabilities networkCapabilities = null;
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wifiConfig = new WifiConfiguration();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null) {
            networkCapabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        }
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        if (networkCapabilities.toString().toUpperCase().contains("WEP")) { // WEP Network.
            Toast.makeText(context, "WEP Network", Toast.LENGTH_SHORT).show();

            wifiConfig.wepKeys[0] = String.format("\"%s\"", networkPass);
            ;
            wifiConfig.wepTxKeyIndex = 0;
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else if (networkCapabilities.toString().toUpperCase().contains("WPA")) { // WPA Network
            Toast.makeText(context, "WPA Network", Toast.LENGTH_SHORT).show();
            wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);
            ;
        } else { // OPEN Network.
            Toast.makeText(context, "OPEN Network", Toast.LENGTH_SHORT).show();
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }


        int netId = wifiManager.addNetwork(wifiConfig);//
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        return wifiManager.reconnect();

    }
}
