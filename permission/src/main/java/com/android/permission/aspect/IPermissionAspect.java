package com.android.permission.aspect;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.permission.Constants;
import com.android.permission.PermissionActivity;
import com.android.permission.PermissionHelper;
import com.android.permission.anotation.Permission;
import com.android.permission.anotation.PermissionCancel;
import com.android.permission.anotation.PermissionDeny;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import static com.android.permission.Constants.TAG;

/**
 * @author：TianLong
 * @date：2022/3/24 14:06
 * @detail：
 */
@Aspect
public class IPermissionAspect  {
    @Pointcut ("execution(@com.android.permission.anotation.Permission * *(..)) && @annotation(permission)")
    public void permissionCheck(Permission permission){}

    @Around("permissionCheck(permission)")
    public void permissionImplement(ProceedingJoinPoint point,Permission permission) throws Throwable {
        if (permission == null){
            throw new IllegalAccessException("permission is null");
        }
        Object object = point.getThis();

        Activity activity;
        if (object instanceof Activity){
            activity = (Activity) point.getThis();
        }else if (object instanceof Fragment){
            activity = ((Fragment) point.getThis()).getActivity();
        }else{
            throw new RuntimeException("context is not Activity or Fragment");
        }

        Bundle bundle = new Bundle();
        bundle.putStringArray(Constants.PERMISSIONS,permission.value());
        bundle.putInt(Constants.REQUEST_CODE,permission.requestCode());
        PermissionActivity.startPermissionActivity(activity, bundle, new IPermission() {
            @Override
            public void grant() {
                try {
                    Log.w(TAG,"grant");
                    point.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void denied() {
                Log.w(TAG,"denied");
                PermissionInvoke.invokeAnnotation(object,PermissionDeny.class);

            }

            @Override
            public void cancel() {
                Log.w(TAG,"cancel");
                PermissionInvoke.invokeAnnotation(object,PermissionCancel.class);
                if (object instanceof Context){
                    PermissionHelper.launchPermissionSettings((Context) object);
                }else if (object instanceof Activity){
                    PermissionHelper.launchPermissionSettings((Activity) object);
                }
            }
        });
    }

    @Pointcut ("execution(@com.android.permission.anotation.PermissionCancel * *(..)) && @annotation(permission)")
    public void permissionCancel(PermissionCancel permission){

    }


    @Around("permissionCancel(permission)")
    public void permissionCancleImplement(ProceedingJoinPoint point,PermissionCancel permission) throws Throwable {
        point.proceed();
    }

    @Pointcut ("execution(@com.android.permission.anotation.PermissionDeny * *(..)) && @annotation(permission)")
    public void permissionDeny(PermissionDeny permission){

    }


    @Around("permissionDeny(permission)")
    public void permissionCancleImplement(ProceedingJoinPoint point, PermissionDeny permission) throws Throwable {
        point.proceed();
    }
}
