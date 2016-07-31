package com.d.dao.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

/**
 * Created by dao on 7/31/16.
 */
public class SlideRelativeLayout extends RelativeLayout {

    public static final String TAG = SlideRelativeLayout.class.getSimpleName();
    private CheckBox mCheckBox;
    private RelativeLayout mContentSlide;
    private int mOffset;

    public SlideRelativeLayout(Context context) {
        super(context);
    }

    public SlideRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCheckBox = (CheckBox) findViewById(R.id.item_checkbox);
        mContentSlide = (RelativeLayout) findViewById(R.id.item_content_rl);
        setOffset(35);//要移动的距离
    }

    public void setOffset(int offset) {
        mOffset = (int) (getContext().getResources().getDisplayMetrics().density * offset + 0.5f);
    }

    public void openAnimation() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(0, 1);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                int endX = (int) (-mOffset * fraction);
                doAnimationSet(endX, fraction);
            }
        });
        valueAnimator.start();
    }

    public void doAnimationSet(int dx, float fraction) {
        mContentSlide.scrollTo(dx, 0);
        mCheckBox.setScaleX(fraction);
        mCheckBox.setScaleY(fraction);
        mCheckBox.setAlpha(fraction * 255);
    }

    public void closeAnimation() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(150);
        valueAnimator.setIntValues(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                int endX = (int) (-mOffset * (1 - fraction));
                doAnimationSet(endX, (1 - fraction));
            }
        });
        valueAnimator.start();
    }

    public void open() {
        mCheckBox.scrollTo(-mOffset, 0);
    }

    public void close() {
        mCheckBox.scrollTo(0, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
