package com.example.ketnoiwifi.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ketnoiwifi.R;
import com.example.ketnoiwifi.activitys.MainActivity;
import com.google.android.material.textview.MaterialTextView;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;
import com.thanosfisherman.wifiutils.wifiRemove.RemoveErrorCode;
import com.thanosfisherman.wifiutils.wifiRemove.RemoveSuccessListener;

import java.util.ArrayList;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {
    private ArrayList<Wifi> Wifis;
    private Context context;
    private ArrayList<Wifi> wifis;

    public WifiAdapter(ArrayList<Wifi> Wifis, Context c) {
        this.Wifis = Wifis;
        this.context = c;
        this.wifis = new ArrayList<>();
        this.wifis.add(new Wifi("cam tien-5g","ancaphoai",true));
        this.wifis.add(new Wifi("Thanh","31071999",true));
        this.wifis.add(new Wifi("Thanh Dien","1234566",true));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi, parent, false);
        parent.scrollTo(0,0);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wifi wifi = Wifis.get(position);
//        Set Name Wifi
        holder.tvName.setText(wifi.getSsid());

        if (wifi.getAvailable())
            holder.tvName.setTextColor(ContextCompat.getColor(context,R.color.blue));
        else
            holder.tvName.setTextColor(ContextCompat.getColor(context,R.color.black));


//        Set Status Wifi
        if (wifi.getAvailable())
            holder.tvStatus.setVisibility(View.VISIBLE);
        else
            holder.tvStatus.setVisibility(View.INVISIBLE);

//        Set Color Icon Wifi
        if (wifi.getAvailable()) {
            holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.blue),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else {
            holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }
//        Connect to Wifi
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectWifi connectWifi = new ConnectWifi(context);
                // Kiểm tra ssid đã có mật khẩu tại DB chưa
                // Nếu có thì kết nối
                // Nếu chưa có thì bắt đầu thử từng mật khẩu
                String passwordFromDB = connectWifi.checkSSIDAvailable(context,wifi.getSsid(),wifis);
                if (passwordFromDB != null){
                    // Xóa mật khẩu cũ đã lưu
                    WifiUtils.withContext(context).remove(wifi.getSsid(), new RemoveSuccessListener() {
                        @Override
                        public void success() {
                            Log.i("Status Wifi","Remove success: " + wifi.getSsid());
                        }

                        @Override
                        public void failed(@NonNull RemoveErrorCode errorCode) {
                            Log.i("Status Wifi","Remove failed: " + wifi.getSsid());

                        }
                    });
//                    Bắt đầu kết nối
                    WifiUtils.withContext(context)
                            .connectWith(wifi.getSsid(), passwordFromDB)
                            .setTimeout(15000)
                            .onConnectionResult(new ConnectionSuccessListener() {
                                @Override
                                public void success() {
                                    Toast.makeText(context, "Kết nối thành công!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void failed(@NonNull ConnectionErrorCode errorCode) {
                                    connectWifi.checkPassWifi(context,wifi.getSsid(),wifis);
                                    Toast.makeText(context, "Kết nối thất bại, đang thử lại..." + errorCode.toString(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .start();
                }
                else
                    connectWifi.checkPassWifi(context,wifi.getSsid(),wifis);
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
////                    connectWifi.connectToWifi(wifi.getSsid(),wifi.getPassword());
//                else
//                    connectWifi.connectToWifi10(wifi.getSsid(),wifi.getPassword());
////                connectWifi.connectWiFi(wifi.getSsid(),wifi.getPassword(),"WPA");
            }
        });
    }

    @Override
    public int getItemCount() {
        return Wifis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public MaterialTextView tvName;
        public MaterialTextView tvStatus;
        public ImageView ivIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_wifi_name);
            tvStatus = itemView.findViewById(R.id.tv_item_wifi_status);
            ivIcon = itemView.findViewById(R.id.iv_item_wifi_icon);
        }
    }
}
