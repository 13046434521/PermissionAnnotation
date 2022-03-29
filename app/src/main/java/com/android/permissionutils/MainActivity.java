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
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Permission_Annotation",this.toString());
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPermission();
            }
        });
    }

    @Permission(value = {Manifest.permission.CAMERA,Manifest.permission.INTERNET,Manifest.permission.WRITE_EXTERNAL_STORAGE},requestCode = 200)
    public void onPermission(){
        Log.e("Permission_Annotation","Permission:onPermission");
        Toast.makeText(MainActivity.this,"Permission",Toast.LENGTH_SHORT).show();
    }

    @PermissionCancel
    public void onCancel(){
        Log.e("Permission_Annotation","PermissionCancel:onCancel");
        Toast.makeText(MainActivity.this,"PermissionCancel",Toast.LENGTH_SHORT).show();
    }

    @PermissionDeny
    public void onDeny(){
        Log.e("Permission_Annotation","PermissionDeny:onDeny");
        Toast.makeText(MainActivity.this,"PermissionDeny",Toast.LENGTH_SHORT).show();
    }
}