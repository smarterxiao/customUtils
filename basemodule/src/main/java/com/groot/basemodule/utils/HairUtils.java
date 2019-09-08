package com.groot.basemodule.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * author: 肖雷
 * created on: 2019/9/8
 * description:
 */
public class HairUtils {
    /**
     * * @see #LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT 全屏模式，内容下移，非全屏不受影响
     * * @see #LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES 允许内容去延伸进刘海区
     * * @see #LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER 不允许内容延伸进刘海区
     */
    public static void hairShow(Window window, int mode,Context context) {

        if (hasDisplayCutout(window,context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.layoutInDisplayCutoutMode = mode;
                window.setAttributes(params);
            }else{

            }
        }

    }

    private static boolean hasDisplayCutout(Window window,Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            DisplayCutout displayCutout;
            View rootView = window.getDecorView();
            WindowInsets insets = rootView.getRootWindowInsets();
            if (insets != null) {
                displayCutout = insets.getDisplayCutout();
                if (displayCutout != null) {
                    return displayCutout.getBoundingRects() != null && displayCutout.getBoundingRects().size() > 0 && displayCutout.getSafeInsetTop() > 0;
                }
            }
            return false;
        }else {
            return hasNotchInHuaWeiScreen(context) || hasNotchAtVivo(context) || hasNotchInOppo(context);
        }
    }


    /**
     * 判断华为是否有刘海
     * @param context
     * @return
     */
    public static boolean hasNotchInHuaWeiScreen(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "hasNotchInScreen Exception");
        }
        return ret;
    }

    /**
     * 获取刘海尺寸：width、height,int[0]值为刘海宽度 int[1]值为刘海高度。
     * @param context
     * @return
     */
    public static int[] getNotchSize(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("test", "getNotchSize ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("test", "getNotchSize NoSuchMethodException");
        } catch (Exception e) {
            Log.e("test", "getNotchSize Exception");
        }
        return ret;
    }

    /**
     * 设置使用刘海区域
     * @param window
     */
    public static void setFullScreenWindowLayoutInDisplayCutout(Window window) {
        if (window == null) {
            return;
        }

        try {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Constructor con=layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
            Object layoutParamsExObj=con.newInstance(layoutParams);
            Method method=layoutParamsExCls.getMethod("addHwFlags", int.class);
            method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT);
        } catch (Exception e) {
            Log.e("test", "other Exception");
        }
    }

    /*刘海屏全屏显示FLAG*/
    public static final int FLAG_NOTCH_SUPPORT = 0x00010000;

    /**
     * 设置应用窗口在华为刘海屏手机不使用刘海
     *
     * @param window 应用页面window对象
     */
    public static void setNotFullScreenWindowLayoutInDisplayCutout(Window window) {
        if (window == null) {
            return;
        }
        try {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Constructor con = layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
            Object layoutParamsExObj = con.newInstance(layoutParams);
            Method method = layoutParamsExCls.getMethod("clearHwFlags", int.class);
            method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT);
        } catch (Exception e) {
            Log.e("test", "hw clear notch screen flag api error");
        }
    }

    /*
     * 判断该 OPPO 手机是否为刘海屏手机
     * @param context
     * @return
     */
    public static boolean hasNotchInOppo(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }
    /**
     * Vivo判断是否有刘海， Vivo的刘海高度小于等于状态栏高度
     */
    public static final int VIVO_NOTCH = 0x00000020;//是否有刘海
    public static final int VIVO_FILLET = 0x00000008;//是否有圆角

    public static boolean hasNotchAtVivo(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "hasNotchAtVivo ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "hasNotchAtVivo NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "hasNotchAtVivo Exception");
        } finally {
            return ret;
        }
    }

}
