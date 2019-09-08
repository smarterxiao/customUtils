package com.groot.basemodule.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import static com.groot.basemodule.utils.Constant.STANDARD_HEIGHT;
import static com.groot.basemodule.utils.Constant.STANDARD_WIDTH;

/**
 * author: 肖雷
 * created on: 2019/9/8
 * description: 计算屏幕宽高与标准尺寸的比例
 */
public class DensityUtils {
    private static float appDensity;//表示屏幕密度
    private static float appScaleDensity; //字体缩放比例，默认appDensity
    //这里是屏幕显示宽高
    private static int mDisplayWidth=-1;
    private  static int mDisplayHeight=-1;

    private static void getScare(Context context){
        //获取屏幕的宽高
        if(mDisplayWidth == -1 || mDisplayHeight == -1){
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (manager != null){
                DisplayMetrics displayMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(displayMetrics);
                if (displayMetrics.widthPixels > displayMetrics.heightPixels){
                    //横屏
                    mDisplayWidth = displayMetrics.heightPixels;
                    mDisplayHeight = displayMetrics.widthPixels;
                }else{
                    mDisplayWidth = displayMetrics.widthPixels;
                    mDisplayHeight = displayMetrics.heightPixels - StatusUtils.getStatusBarHeight(context);
                }
            }
        }

    }

    //获取水平方向的缩放比例
    public static float getHorizontalScale(Context context){
        if(mDisplayWidth==-1){
            getScare(context);
        }
        return mDisplayWidth / STANDARD_WIDTH;
    }

    //获取垂直方向的缩放比例
    public  static float getVerticalScale(Context context){
        if(mDisplayHeight==-1){
            getScare(context);
        }
        return mDisplayHeight / STANDARD_HEIGHT;
    }

    /**
     * 动态设置Density 设置像素密度
     * @param application  application
     * @param activity activity
     */
    public static void setDensity(final Application application, Activity activity){
        //获取当前app的屏幕显示信息
        DisplayMetrics displayMetrics = application.getResources().getDisplayMetrics();
        if (appDensity == 0){
            //初始化赋值操作
            appDensity = displayMetrics.density;
            appScaleDensity = displayMetrics.scaledDensity;

            //添加字体变化监听回调
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    //字体发生更改，重新对scaleDensity进行赋值
                    if (newConfig != null && newConfig.fontScale > 0){
                        appScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        //计算目标值density, scaleDensity, densityDpi
        float targetDensity = displayMetrics.widthPixels / STANDARD_WIDTH; // 1080 / 360 = 3.0
        float targetScaleDensity = targetDensity * (appScaleDensity / appDensity);
        int targetDensityDpi = (int) (targetDensity * 160);

        //替换Activity的density, scaleDensity, densityDpi
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        dm.density = targetDensity;
        dm.scaledDensity = targetScaleDensity;
        dm.densityDpi = targetDensityDpi;
    }
}
