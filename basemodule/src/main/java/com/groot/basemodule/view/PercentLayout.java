package com.groot.basemodule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.groot.basemodule.utils.DensityUtils;

/**
 * author: 肖雷
 * created on: 2019/9/8
 * description:
 */
public class PercentLayout extends RelativeLayout {
    private boolean flag;

    public PercentLayout(Context context) {
        super(context);
    }

    public PercentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!flag){
            float scaleX = DensityUtils.getHorizontalScale(getContext());
            float scaleY = DensityUtils.getVerticalScale(getContext());

            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                params.width = (int) (params.width * scaleX);
                params.height = (int) (params.height * scaleY);
                params.leftMargin = (int)(params.leftMargin * scaleX);
                params.rightMargin = (int)(params.rightMargin * scaleX);
                params.topMargin = (int)(params.topMargin * scaleY);
                params.bottomMargin = (int)(params.bottomMargin * scaleY);
            }
            flag = true;
        }

        invalidate();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
