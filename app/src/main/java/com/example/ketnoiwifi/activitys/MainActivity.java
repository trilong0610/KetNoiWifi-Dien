package com.example.ketnoiwifi.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ketnoiwifi.R;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rcvListWifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mapping();
    }

    private void Mapping() {
        rcvListWifi = findViewById(R.id.rcv_main_listWifi);
    }
}