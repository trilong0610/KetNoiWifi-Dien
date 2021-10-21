package com.example.ketnoiwifi.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
//        Set Status Wifi
        if (wifi.getAvailable()){
            holder.tvStatus.setText("Sẵn sàng");
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.blue));
        }
        else {
            holder.tvStatus.setText("");
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.black));
        }
//        Set Color Icon Wifi
        if (wifi.getAvailable()) {
            holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.blue),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else {
            holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }
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
