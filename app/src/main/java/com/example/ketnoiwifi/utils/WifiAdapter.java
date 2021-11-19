package com.example.ketnoiwifi.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ketnoiwifi.R;
import com.example.ketnoiwifi.model.Wifi;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {
    private ArrayList<Wifi> Wifis;
    private Context context;
    private ConnectWifi connectWifi;

    public WifiAdapter(ArrayList<Wifi> Wifis, Context c) {
        this.Wifis = Wifis;
        this.context = c;
        connectWifi = new ConnectWifi(c);
//        this.wifis.add(new Wifi("Thanh Dien","31071999",true));

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

        holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);

//        Connect to Wifi
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "Đang kết nối với " + wifi.getSsid(), Toast.LENGTH_SHORT).show();
                connectWifi.checkPassWifi(wifi.getSsid());
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
