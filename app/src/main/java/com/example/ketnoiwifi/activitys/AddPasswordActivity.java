package com.example.ketnoiwifi.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ketnoiwifi.R;
import com.example.ketnoiwifi.model.Password;
import com.example.ketnoiwifi.utils.DatabaseHandler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class AddPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout txtPassword;
    private MaterialButton btnAdd;
    DatabaseHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);
        Mapping();
    }

    private void Mapping() {
        txtPassword = findViewById(R.id.txt_add_password_password);
        btnAdd = findViewById(R.id.btn_add_password_add);
        handler = new DatabaseHandler(getApplicationContext());
        btnAdd.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        if (v == btnAdd){
            String password = txtPassword.getEditText().getText().toString().trim();
            int sizePassword = handler.getAllPassword().size();
            if (password != null){
                Log.e("sizePassword",String.valueOf(sizePassword));
            handler.addPassword(new Password(1,password));
            Toast.makeText(this,"Đã thêm mật khẩu", Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
        }
    }
}