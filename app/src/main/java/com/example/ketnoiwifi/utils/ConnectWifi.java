package com.example.ketnoiwifi.utils;

import static com.example.ketnoiwifi.activitys.MainActivity.wifiAdapter;
import static com.example.ketnoiwifi.activitys.MainActivity.wifiManager;
import static com.example.ketnoiwifi.activitys.MainActivity.wifis;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    public void scanWifi(){ WifiUtils.withContext(context.getApplicationContext()).scanWifi(this::getScanResults).start(); }

    private void getScanResults(@NonNull final List<ScanResult> results)
    {
        if (results.isEmpty())
        {
            Log.i(TAG, "SCAN RESULTS IT'S EMPTY");
            return;
        }
        Log.i(TAG, "GOT SCAN RESULTS " + results);
        Toast.makeText(context, "Đã cập nhật danh sách wifi", Toast.LENGTH_SHORT).show();

        // Xóa danh sách wifi
        wifis.clear();
        // chạy vòng lặp thêm từng wifi từ kết quả scan từ results
        for (ScanResult scanResult :
                results) {
            wifis.add(new Wifi(0,scanResult.SSID, ""));
            wifiAdapter.notifyDataSetChanged();
        }
    }

    public boolean _continuteLoop = true;
    public int _count = 0;

    public void checkPassWifi(String SSID){

        DatabaseHandler handler = new DatabaseHandler(context);
        ArrayList<Password> passwords = handler.getAllPassword();
        // Kiểm tra nếu index pass hiện tại < tổng wifi thì tiếp tục dò
         if (_count < passwords.size()){
                // Bat dau ket noi
                Toast.makeText(context, "Bắt đầu dò mật khẩu...", Toast.LENGTH_SHORT).show();
                //Xoa mat khau đã lưu của ssid wifi cần dò pass
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

//                Kiểm tra version android hiện tại
//             Android >= 10
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MainActivity.progressMain.setVisibility(View.VISIBLE);
                    ArrayList<Password> finalPasswords = passwords;
                    WifiUtils.withContext(context)
                            .connectWith(SSID, passwords.get(_count).getPassword())
                            .setTimeout(8000) // Thời gian chờ kết nối wifi
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
                                    _continuteLoop = true; // Tiếp tục lặp dò pass
                                    _count++;
                                    if (_continuteLoop)
                                        checkPassWifi(SSID);
                                    return;
                                }
                            })
                            .start();
                }
//                Android < 10
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
                // Sau 8200ms kiểm tra xem đã kết nối wifi chưa
                // Nếu đã kết nối thì thông báo
                // Nếu chưa thì thử mật khẩu tiếp theo
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Ket noi thanh cong
                        if (WifiUtils.withContext(context).isWifiConnected()){
                            MainActivity.progressMain.setVisibility(View.INVISIBLE);
                            showDialog(context, "Kết nối thành công","Mật khẩu: " + passwords.get(_count).getPassword());
                        }
//                        Kết nối thất bại
//                        Nếu đã là mật khẩu cuối cùng => ngừng dò và thông báo
//                        Nếu chưa là mật khẩu cuối cùng => tiếp tục dò
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
                },8200); // Thời gian chờ kết nối wifi
            }
            else {
                _count = 0;
            }
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

