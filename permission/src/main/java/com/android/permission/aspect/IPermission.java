package com.android.permission.aspect;

/**
 * @author：TianLong
 * @date：2022/3/25 14:58
 * @detail：
 */
public interface IPermission {
    void grant();

    void denied();

    void cancel();
}
