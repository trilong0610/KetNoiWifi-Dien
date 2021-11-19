package com.example.ketnoiwifi.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ketnoiwifi.R;
import com.example.ketnoiwifi.activitys.ManagerActivity;
import com.example.ketnoiwifi.model.Password;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class WifiMangerAdapter extends RecyclerView.Adapter<WifiMangerAdapter.ViewHolder>{
    private ArrayList<Password> passwords;
    private Context context;

    public WifiMangerAdapter(ArrayList<Password> passwords, Context c) {
        this.passwords = passwords;
        this.context = c;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_manager, parent, false);
        parent.scrollTo(0,0);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int temp = position;
        Password password = passwords.get(position);
//        Set Name Wifi
        holder.tvPassword.setText(password.getPassword());
//        Xoa mat khau
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler handler = new DatabaseHandler(context);
                handler.deleteStudent(password.getId());
                ManagerActivity.reloadDB(context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        public MaterialTextView tvPassword;
        public MaterialButton btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPassword = itemView.findViewById(R.id.tv_item_wifi_manager_password);
            btnDelete = itemView.findViewById(R.id.btn_item_wifi_manager_delete);
        }
    }
}
