package com.example.ketnoiwifi.utils;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ketnoiwifi.R;
import com.example.ketnoiwifi.model.Wifi;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {
    private ArrayList<Wifi> Wifis;
    private Context context;

    public WifiAdapter(ArrayList<Wifi> Wifis, Context c) {
        this.Wifis = Wifis;
        this.context = c;
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
<<<<<<< Updated upstream
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
                    connectWifi.connectToWifi(wifi.getSsid(),wifi.getPassword());
                else
                    connectWifi.connectToWifi10(wifi.getSsid(),wifi.getPassword());
=======
                connectWifi.connectWiFi(wifi.getSsid(),wifi.getPassword(),"WPA");
>>>>>>> Stashed changes
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
