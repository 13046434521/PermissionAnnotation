package com.android.permissionutils;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.permission.anotation.Permission;
import com.android.permission.anotation.PermissionCancel;
import com.android.permission.anotation.PermissionDeny;

public class MainActivity extends AppCompatActivity {
    private int i =0;
    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTest();
            }
        });
    }

    @Permission(value = {Manifest.permission.CAMERA,Manifest.permission.INTERNET,Manifest.permission.WRITE_EXTERNAL_STORAGE},requestCode = 200)
    public void onTest(){
        Log.e("Permission_Annotation","Permission:onTest");
        Toast.makeText(MainActivity.this,"Permission",Toast.LENGTH_SHORT).show();
    }

    @PermissionCancel
    public void onAA(){
        Log.e("Permission_Annotation","PermissionCancel:onAA");
        Toast.makeText(MainActivity.this,"PermissionCancel",Toast.LENGTH_SHORT).show();
    }

    @PermissionDeny
    public void onAnBB(){
        Log.e("Permission_Annotation","PermissionDeny:onAnBB");
        Toast.makeText(MainActivity.this,"PermissionDeny",Toast.LENGTH_SHORT).show();
    }
}