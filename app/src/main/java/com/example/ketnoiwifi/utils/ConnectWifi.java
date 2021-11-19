package com.example.ketnoiwifi.utils;

import static com.example.ketnoiwifi.activitys.MainActivity.mConnectivityManager;
import static com.example.ketnoiwifi.activitys.MainActivity.wifiAdapter;
import static com.example.ketnoiwifi.activitys.MainActivity.wifiManager;
import static com.example.ketnoiwifi.activitys.MainActivity.wifis;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.ketnoiwifi.activitys.MainActivity;
import com.example.ketnoiwifi.model.Password;
import com.example.ketnoiwifi.model.Wifi;
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

    ConnectivityManager.NetworkCallback networkCallback;

//    WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);

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

    public void scanWifi(){ WifiUtils.withContext(context.getApplicationContext()).scanWifi(this::getScanResults).start();
//    CODE Cũ, tự viết
//        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context c, Intent intent) {
//                boolean success = intent.getBooleanExtra(
//                        WifiManager.EXTRA_RESULTS_UPDATED, false);
//                if (success) {
//                    if (flagScan)
//                        scanSuccess();
//                } else {
//                    // scan failure handling
//                    scanFailure();
//                }
//            }
//        };
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//        context.registerReceiver(wifiScanReceiver, intentFilter);
//        boolean success = wifiManager.startScan();
//        if (success) {
//
//        }
    }

    private void getScanResults(@NonNull final List<ScanResult> results)
    {
        if (results.isEmpty())
        {
            Log.i(TAG, "SCAN RESULTS IT'S EMPTY");
            return;
        }
        Log.i(TAG, "GOT SCAN RESULTS " + results);
        Toast.makeText(context, "Đã cập nhật danh sách wifi", Toast.LENGTH_SHORT).show();
        wifis.clear();
        for (ScanResult scanResult :
                results) {
            wifis.add(new Wifi(0,scanResult.SSID, ""));
            wifiAdapter.notifyDataSetChanged();
        }
    }

//    private void scanFailure() {
//        // handle failure: new scan did NOT succeed
//        // consider using old scan results: these are the OLD results!
//        List<ScanResult> results = wifiManager.getScanResults();
//        wifis.clear();
//        for (ScanResult scanResult :
//                results) {
//            wifis.add(new Wifi(scanResult.SSID,"TVNET@123",false));
//            wifiAdapter.notifyDataSetChanged();
//        }
//    }

    public String checkSSIDAvailable(String SSID, ArrayList<Wifi> wifis){
        for (Wifi wifi: wifis) {
            if (wifi.getSsid().equalsIgnoreCase(SSID))
                return wifi.getPassword();
        }
        return null;
    }


    public boolean _continuteLoop = true;
    public int _count = 0;
    public boolean flagBruceforce = true; // Đánh dấu cho phép cập nhật danh sách wifi
//    public void checkPassWifi(String SSID, ArrayList<String> passwords){
//        if (_count < passwords.size() && _continuteLoop){
//            // Bat dau ket noi
//            Toast.makeText(context.getApplicationContext(), "Đang thử mật khẩu " + _count, Toast.LENGTH_SHORT).show();
//            //Xoa mat khau
//            WifiUtils.withContext(context).remove(SSID, new RemoveSuccessListener() {
//                @Override
//                public void success() {
//                    Log.i("Status Wifi","Remove success 1: " + SSID);
//
//                }
//
//                @Override
//                public void failed(@NonNull RemoveErrorCode errorCode) {
//                    Log.i("Status Wifi","Remove failed 1: " + SSID);
//                }
//            });
////            Ngừng lặp
//            _continuteLoop = false;
//
//            Log.e("Status Wifi","Đang thử mật khẩu " + _count);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                WifiUtils.withContext(context)
//                        .connectWith(SSID, passwords.get(_count))
//                        .setTimeout(15000)
//                        .onConnectionResult(new ConnectionSuccessListener() {
//                            @Override
//                            public void success() {
//                                Toast.makeText(context, "Kết nối thành công!", Toast.LENGTH_SHORT).show();
//                                //                                    flagBruceforce = false; // Dung vong lap
//                                return;
//                            }
//
//                            @Override
//                            public void failed(@NonNull ConnectionErrorCode errorCode) {
//                                Toast.makeText(context, "Kết nối thất bại, đang thử lại...\n" + errorCode, Toast.LENGTH_SHORT).show();
//                                _continuteLoop = true; // Ttiếp tục lặp dò pass
//                                _count++;
//                                if (_continuteLoop)
//                                    checkPassWifi(SSID, passwords);
//                                return;
//                            }
//                        })
//                        .start();
//            }
//            else {
//                try {
//                    Log.e(TAG,"connection wifi pre Q");
//                    WifiConfiguration wifiConfig = new WifiConfiguration();
//                    wifiConfig.SSID = "\"" + SSID + "\"";
//                    wifiConfig.preSharedKey = "\"" + passwords.get(_count) + "\"";
//                    int netId = wifiManager.addNetwork(wifiConfig);
//                    wifiManager.disconnect();
//                    wifiManager.enableNetwork(netId, true);
//                    wifiManager.reconnect();
//
//                } catch ( Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        else
//            _count = 0;
//    }
    public void checkPassWifi(String SSID){

        DatabaseHandler handler = new DatabaseHandler(context);
        ArrayList<Password> passwords = handler.getAllPassword();

        Log.e("ConnectWifiList", String.valueOf(passwords.size()));
            if (_count < passwords.size()){

                // Bat dau ket noi
                Toast.makeText(context, "Bắt đầu dò mật khẩu...", Toast.LENGTH_SHORT).show();                //Xoa mat khau
                WifiUtils.withContext(context).remove(SSID, new RemoveSuccessListener() {
                    @Override
                    public void success() {
                        Log.i("Status Wifi","Remove success 1: " + SSID);

                    }

                    @Override
                    public void failed(@NonNull RemoveErrorCode errorCode) {
                        Log.i("Status Wifi","Remove failed 1: " + SSID);
                    }
                });
    //            Ngừng lặp
//                _continuteLoop = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MainActivity.progressMain.setVisibility(View.VISIBLE);
                    ArrayList<Password> finalPasswords = passwords;
                    WifiUtils.withContext(context)
                            .connectWith(SSID, passwords.get(_count).getPassword())
                            .setTimeout(8000)
                            .onConnectionResult(new ConnectionSuccessListener() {
                                @Override
                                public void success() {
                                    MainActivity.progressMain.setVisibility(View.INVISIBLE);
                                    showDialog(context, "Kết nối thành công","Mật khẩu: " + passwords.get(_count).getPassword());
                                    //                                    flagBruceforce = false; // Dung vong lap
                                    return;
                                }

                                @Override
                                public void failed(@NonNull ConnectionErrorCode errorCode) {
                                    MainActivity.progressMain.setVisibility(View.INVISIBLE);
                                    Toast.makeText(context, "Kết nối thất bại, đang thử lại...\n" + errorCode, Toast.LENGTH_SHORT).show();
                                    _continuteLoop = true; // Ttiếp tục lặp dò pass
                                    _count++;
                                    if (_continuteLoop)
                                        checkPassWifi(SSID);
                                    return;
                                }
                            })
                            .start();
                }
                else {
                    try {
                        MainActivity.progressMain.setVisibility(View.VISIBLE);
                        WifiConfiguration wifiConfig = new WifiConfiguration();
                        wifiConfig.SSID = "\"" + SSID + "\"";
                        wifiConfig.preSharedKey = "\"" + passwords.get(_count).getPassword() + "\"";
                        int netId = wifiManager.addNetwork(wifiConfig);
                        wifiManager.disconnect();
                        wifiManager.enableNetwork(netId, true);
                        wifiManager.reconnect();

                    } catch ( Exception e) {
                        e.printStackTrace();
                    }
                }
                // Sau 15s kiểm tra xem đã kết nối wifi chưa
                // Nếu đã kết nối thì thông báo
                // Nếu chưa thì thử mật khẩu tiếp theo
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (WifiUtils.withContext(context).isWifiConnected()){
                            // Ket noi thanh cong
                            MainActivity.progressMain.setVisibility(View.INVISIBLE);
                            showDialog(context, "Kết nối thành công","Mật khẩu: " + passwords.get(_count).getPassword());
                        }
                        else {
                            if (_count == passwords.size() - 1){
                                MainActivity.progressMain.setVisibility(View.INVISIBLE);
                                Toast.makeText(context, "Không thể dò được mật khẩu!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                MainActivity.progressMain.setVisibility(View.INVISIBLE);
                                Toast.makeText(context, "Kết nối thất bại, đang thử mật khẩu khác...", Toast.LENGTH_SHORT).show();
                                _count++;
                                checkPassWifi(SSID);
                            }

                        }

                    }
                },8200);
            }
            else {
                _count = 0;
            }
        }


    public void stopCheckPassWifi(){
        flagBruceforce = false;
    }

    private void showDialog(Context c,String tittle, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(c).create(); //Use context
        alertDialog.setTitle(tittle);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}

