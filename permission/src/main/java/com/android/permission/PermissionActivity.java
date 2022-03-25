package com.android.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.android.permission.aspect.IPermission;

import static com.android.permission.Constants.*;

/**
 * @author jiatianlong
 */
public class PermissionActivity extends Activity {
    private String[] permissions;
    private int requestCode;
    private static IPermission mIPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        overridePendingTransition(0, 0);
        //设置成1像素
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.width = 1;
        layoutParams.height = 1;
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        window.setAttributes(layoutParams);

        Bundle bundle = getIntent().getExtras();
        permissions = bundle.getStringArray(Constants.PERMISSIONS);
        requestCode = bundle.getInt(Constants.REQUEST_CODE);

        //判空
        if (permissions == null || permissions.length == 0 || requestCode == 0||mIPermission==null) {
            Log.w(TAG,11111111+"");
            finish();
            return;
        }

        // 如果有未授权的，申请权限
        if (!PermissionHelper.hasPermissions(this,permissions)){
            requestPermissions(permissions, requestCode);
            Log.w(TAG,"22222222222222222222");
            return;
        }

        // 如果全都授权了，回调，退出
        Log.w(TAG,"3333333333333");
        mIPermission.grant();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    public static void startPermissionActivity(Context context, Bundle bundle, IPermission iPermission) {
        Intent intent = new Intent();
        intent.setClass(context, PermissionActivity.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        mIPermission = iPermission;
        context.startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != this.requestCode) {
            Log.e(TAG,"requestCode 不匹配:"+requestCode);
        }
        Log.w(TAG,"444444444444444444");

        boolean isGrant = PermissionHelper.isPermissionGrant(grantResults);
        // 授权成功
        if (isGrant) {
            mIPermission.grant();
            Log.w(TAG,"5555555555555555");
            this.finish();
            return;
        }
        boolean isDenied = PermissionHelper.shouldShowRequestPermissionRationale(this,permissions);
        Log.w(TAG,"666666666666666");
        // 授权拒绝，
        if (isDenied){
            mIPermission.denied();
            Log.w(TAG,"7777777777777");
            this.finish();
            return;
        }
        Log.w(TAG,"88888888888888");
        // 授权取消，下次点击依旧授权
        mIPermission.cancel();
        finish();
    }
}