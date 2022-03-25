/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author TianLong
 */
public final class PermissionHelper {
    public static final int PERMISSION_CODE = 0;
    public static final int CAMERA_PERMISSION_CODE = 1;
    public static final int STORAGE_PERMISSION_CODE = 2;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final String STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    /**
     * 是否有相机权限
     *
     * @param activity
     * @return
     */
    public static boolean hasCameraPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, CAMERA_PERMISSION)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 请求相机权限
     *
     * @param activity
     */
    public static void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity, new String[]{CAMERA_PERMISSION}, CAMERA_PERMISSION_CODE);
    }

    /**
     * 是否有内存卡写入权限
     *
     * @param activity
     * @return
     */
    public static boolean hasStoragePermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, STORAGE_PERMISSION)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 请求内存卡写入权限
     *
     * @param activity
     */
    public static void requestStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity, new String[]{STORAGE_PERMISSION}, STORAGE_PERMISSION_CODE
        );
    }

    /**
     * 请求权限
     *
     * @param activity
     * @param permissions 权限名称
     * @param requestcode 请求码
     */
    public static void requestPermission(Activity activity, String[] permissions, int requestcode) {
        ActivityCompat.requestPermissions(activity, permissions, requestcode);
    }

    /**
     * 是否有权限
     *
     * @param activity
     * @param permission 权限名
     * @return
     */
    public static boolean hasPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 是否有权限
     *
     * @param activity
     * @param permissions 权限名
     * @return
     */
    public static boolean hasPermissions(Activity activity, String[] permissions) {
        for (String permission :permissions){
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }

        return true;
    }

    /**
     * 展示申请权限的解释
     * @param activity
     * @return
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity,String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    /**
     * 当用户点击了不再提示，这种情况要考虑到才行
     * @param activity
     * @param permissions
     * @return
     */

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 授权是否全部成功
     * @param gantedResult
     * @return
     */
    public static boolean isPermissionGrant(int... gantedResult) {
        if (gantedResult == null || gantedResult.length <= 0) {
            return false;
        }

        for (int permissionValue : gantedResult) {
            if (permissionValue != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 注解反射
     * @param object
     * @param annotationClass
     */
    public static void invokeAnnotation(Object object, Class annotationClass) {
        // 获取 object 的 Class对象
        Class<?> objectClass = object.getClass();

        // 遍历 所有的方法
        Method[] methods = objectClass.getDeclaredMethods();

        for (Method method : methods) {
            Log.w("Permission_Annotation",method+"--"+object );
            // 获取 private 方法
            method.setAccessible(true);

            // 判断方法 是否有被 annotationClass注解的方法
            boolean annotationPresent = method.isAnnotationPresent(annotationClass);

            if (annotationPresent) {
                Log.e("Permission_Annotation","去你妈的:"+method+"--"+object );
                // 当前方法 代表包含了 annotationClass注解的
                try {
                    method.invoke(object);
                } catch (IllegalAccessException e) {
                    Log.w("Permission_Annotation",e.getMessage());
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    Log.w("Permission_Annotation",e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 打开设置界面
     *
     * @param context
     */
    public static void launchPermissionSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }
}
