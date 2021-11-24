package com.example.ketnoiwifi.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ketnoiwifi.R;
import com.example.ketnoiwifi.model.Password;
import com.example.ketnoiwifi.utils.DatabaseHandler;
import com.example.ketnoiwifi.utils.WifiMangerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ManagerActivity extends AppCompatActivity {
    public static ArrayList<Password> passwordList = new ArrayList<>();
    public static DatabaseHandler handler;
    public static RecyclerView rcvListWifi;
    FloatingActionButton btnPassword;
    public static WifiMangerAdapter wifiMangerAdapter;
    private final int REQUEST_ADD_PASSWORD = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        Mapping();
    }

    private void Mapping() {
        handler = new DatabaseHandler(this);
        passwordList = handler.getAllPassword();
        rcvListWifi = findViewById(R.id.rcv_manager_listWifi);
        btnPassword = findViewById(R.id.btn_manager_add_password);
        wifiMangerAdapter = new WifiMangerAdapter(passwordList,this);
        rcvListWifi.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcvListWifi.setAdapter(wifiMangerAdapter);
        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ManagerActivity.this,AddPasswordActivity.class),REQUEST_ADD_PASSWORD);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ADD_PASSWORD && resultCode == RESULT_OK){
            reloadDB(this);

        }
    }

    public static void reloadDB(Context c){
        DatabaseHandler handler = new DatabaseHandler(c);
        passwordList = handler.getAllPassword();
        Log.e("reloadDB", String.valueOf(passwordList.size()));
        wifiMangerAdapter = new WifiMangerAdapter(passwordList,c);
        rcvListWifi.setLayoutManager(new LinearLayoutManager(c));
        rcvListWifi.setAdapter(wifiMangerAdapter);
    }

}