package com.example.gmo.dexterpermission;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gmo.dexterpermission.utils.PermissionUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnCamera, btnLocation, btnCall, btnAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCamera = findViewById(R.id.btn_camera);
        btnLocation = findViewById(R.id.btn_location);
        btnCall = findViewById(R.id.btn_call);
        btnAll = findViewById(R.id.btn_all);

        btnCamera.setOnClickListener(this);
        btnLocation.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        btnAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_camera:
                requestCameraPermission();
                break;
            case R.id.btn_location:
                requestLocationPermission();
                break;
            case R.id.btn_call:
                requestCallPermission();
                break;
            case R.id.btn_all:
                requestPermissionAll();
                break;
        }
    }

    /*Request multi permission*/
    private void requestPermissionAll() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CALL_PHONE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                });
    }

    /*Request permission call*/
    private void requestCallPermission() {
        if (PermissionUtils.checkPermissionCall(this)){

        }
    }

    /*Request permission location*/
    private void requestLocationPermission() {

    }

    /*Request permission camera*/
    private void requestCameraPermission() {

    }
}
