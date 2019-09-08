package com.groot.basemodule.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * author: 肖雷
 * created on: 2019/9/8
 * description: 状态栏工具
 */
public class StatusUtils {

    /**
     * h获取状态栏高度
     * @param context  context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context){
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0){
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }


    /**
     * 添加沉浸式状态栏
     * @param context Activity
     */
    public static void setStatusState(Activity context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = context.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色透明
            window.setStatusBarColor(Color.TRANSPARENT);

            int visibility = window.getDecorView().getSystemUiVisibility();
            //布局内容全屏展示
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            //隐藏虚拟导航栏
            visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            //防止内容区域大小发生变化
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(visibility);
        }else {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    /**
     * 平移状态栏的高度
     * @param context context
     * @param view view
     */
    public  static void setHeightAndPadding(Context context, View view){
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height += getStatusBarHeight(context);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context), view.getPaddingRight(), view.getPaddingBottom());
    }
}
