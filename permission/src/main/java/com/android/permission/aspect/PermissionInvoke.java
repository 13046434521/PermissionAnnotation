package com.android.permission.aspect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author：TianLong
 * @date：2022/3/25 16:40
 * @detail：
 */
public class PermissionInvoke {
    public static void invokeAnnotation(Object object, Class annotationClass){
        Class clazz = object.getClass();

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method:methods) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(annotationClass)){
                try {
                    method.invoke(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void invokeAnnotations(Object object, Class annotationClass, String[] permission){
        Class clazz = object.getClass();

        Method[] methods = clazz.getMethods();
        for (Method method:methods) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(annotationClass)){
                try {
                    method.invoke(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
