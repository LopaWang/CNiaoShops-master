package com.chhd.cniaoshops.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.chhd.cniaoshops.R;

/**
 * Created by Andy on 16/6/24.
 */
public class ProgressView extends View {

    int defaultColor = Color.WHITE;
    Paint progressPaint = null;
    Paint progressCircle = null;
    int currentProgress = 0;
    int totalProgress = 0;
    boolean isHide = false;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressView, defStyleAttr, 0);
            defaultColor = array.getColor(R.styleable.ProgressView_progress_color, Color.WHITE);
        }
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        progressPaint = new Paint();
        progressPaint.setColor(defaultColor);
        progressCircle = new Paint();
        progressCircle.setColor(defaultColor);
        progressCircle.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));
    }

    int viewWidth = 0;
    int viewHeight = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        viewHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentProgress <= 100 && isHide) {
            isHide = false;
            this.setAlpha(1);
        }
        canvas.drawRect(0, 0, (float) (viewWidth * (currentProgress / 100.0)), viewHeight, progressPaint);
        canvas.drawCircle((float) (viewWidth * (currentProgress / 100.0)) - viewHeight / 2, viewHeight / 2, viewHeight, progressCircle);
        if (currentProgress >= 100) {
            hideSelf();
        }
    }

    private void hideSelf() {
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewCompat.animate(ProgressView.this).alpha(0);
                isHide = true;
                ProgressView.this.currentProgress = 0;
            }
        }, 100);

    }

    public int getDefaultColor() {
        return defaultColor;
    }

    ValueAnimator animator;

    public void setProgress(int progress) {
        totalProgress = progress;
        if (animator != null) {
            if (animator.isRunning()) {
                animator.cancel();
            }
        }
        animator = ValueAnimator.ofInt(currentProgress, totalProgress);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentProgress = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

}
