package com.example.gmo.dexterpermission.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.support.v4.app.ActivityCompat;

/**
 * Created by GMO on 4/16/2018.
 */

public class PermissionUtils {
    /*
    *Check quyền camera*/
    public static boolean checkPermissionCamera(Context context) {
        if (VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            /*Nếu chưa cấp quyền camera*/
            return false;
        }
        return true;
    }

    /*Check quyền gọi*/
    public static boolean checkPermissionCall(Context context){
        if (VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(context,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    /*Check location*/
    public static boolean checkPermissionLocation(Context context){
        if (VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

}
