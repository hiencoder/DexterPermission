package com.example.gmo.dexterpermission;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gmo.dexterpermission.utils.PermissionUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnCamera, btnLocation, btnCall, btnAll;
    TextView tvStatusCamera, tvStatusLocation, tvStatusCall, tvStatusAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCamera = findViewById(R.id.btn_camera);
        btnLocation = findViewById(R.id.btn_location);
        btnCall = findViewById(R.id.btn_call);
        btnAll = findViewById(R.id.btn_all);
        tvStatusCamera = findViewById(R.id.tv_status_camera);
        tvStatusCall = findViewById(R.id.tv_status_call);
        tvStatusLocation = findViewById(R.id.tv_status_location);
        tvStatusAll = findViewById(R.id.tv_status_all);

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
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()){
                            /*Nếu tất cả được cấp quyền*/
                            tvStatusAll.setText("Permission are granted!");
                        }
                        /*Nếu 1 trong các quyền bị từ chối*/
                        if (report.isAnyPermissionPermanentlyDenied()){
                            /*Hiển thị dialog gọi đến setting app*/
                            showSettingDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                tvStatusAll.setText("Error");
            }
        }).onSameThread()
        .check();
    }

    /*Request permission call*/
    private void requestCallPermission() {
        if (!PermissionUtils.checkPermissionStorage(this)){
            //Nếu quyền call chưa được check
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()){
                                tvStatusCall.setText("Permission is granted!");
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).withErrorListener(new PermissionRequestErrorListener() {
                @Override
                public void onError(DexterError error) {
                    tvStatusCall.setText("error");
                }
            })
                    .onSameThread()
                    .check();
        }
    }

    /*Gọi đến phần setting để cấp quyền cho app*/
    private void showSettingDialog() {
        /*Tạo dialog*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Vào phần cài đặt quyền cho ứng dụng?");
        builder.setPositiveButton("GOTO SETTING", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Đóng dialog
                dialog.cancel();
                Log.d("Which", "onClick: " + which);
                gotoSettingPermission();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    /*Đến màn hình setting permission cho app*/
    private void gotoSettingPermission() {
        //Tạo intent
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",getPackageName(),null);
        intent.setData(uri);
        startActivityForResult(intent,110);
    }

    /*Request permission location*/
    private void requestLocationPermission() {
        if (!PermissionUtils.checkPermissionLocation(this)){
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            tvStatusLocation.setText("Permission is granted!");
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            showSettingDialog();
                            tvStatusLocation.setText("Denied");
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    /*Request permission camera*/
    private void requestCameraPermission() {
        if (!PermissionUtils.checkPermissionCamera(this)){
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            tvStatusCamera.setText("Permission is granted!");
                            openCamera();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            showSettingDialog();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    }).check();
        }
    }

    /*Open camera*/
    private void openCamera() {
        /*Intent call open camera*/
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,114);
    }
}
