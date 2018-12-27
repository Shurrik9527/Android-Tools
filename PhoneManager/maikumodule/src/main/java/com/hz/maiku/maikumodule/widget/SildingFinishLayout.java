package com.hz.maiku.maikumodule.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * 自定义可以滑动的RelativeLayout, 类似于IOS的滑动删除页面效果，当我们要使用
 * 此功能的时候，需要将该Activity的顶层布局设置为SildingFinishLayout，
 */
public class SildingFinishLayout extends RelativeLayout {


    private final String TAG = SildingFinishLayout.class.getName();

    /**
     * SildingFinishLayout布局的父布局
     */
    private ViewGroup mParentView;

    /**
     * 滑动的最小距离
     */
    private int mTouchSlop;
    /**
     * 按下点的X坐标
     */
    private int downX;
    /**
     * 按下点的Y坐标
     */
    private int downY;
    /**
     * 临时存储X坐标
     */
    private int tempX;
    /**
     * 滑动类
     */
    private Scroller mScroller;
    /**
     * SildingFinishLayout的宽度
     */
    private int viewWidth;
    /**
     * 记录是否正在滑动
     */
    private boolean isSilding;
    /**
     * 是否完成滑动
     */
    private boolean isFinish;

    private OnSildingFinishListener onSildingFinishListener;

    private int size; //按下时范围(处于这个范围内就启用切换事件，目的是使当用户从左右边界点击时才响应)
    private boolean isIntercept = false; //是否拦截触摸事件

    public SildingFinishLayout(Context context) {
        super(context);
        init(context);
    }

    public SildingFinishLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public SildingFinishLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            // 获取SildingFinishLayout所在布局的父布局
            mParentView = (ViewGroup) this.getParent();
            viewWidth = this.getWidth();
            size = viewWidth;
        }
    }


    /**
     * 设置OnSildingFinishListener, 在onSildingFinish()方法中finish Activity
     *
     * @param onSildingFinishListener
     */
    public void setOnSildingFinishListener(
            OnSildingFinishListener onSildingFinishListener) {
        this.onSildingFinishListener = onSildingFinishListener;
    }

    //是否拦截事件，如果不拦截事件，对于有滚动的控件的界面将出现问题(相冲突)
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float downX = ev.getRawX();
        if (downX < size) {
            isIntercept = true;
            return false;
        } else {
            isIntercept = false;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isIntercept) {//不拦截事件时 不处理
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int) event.getRawX();
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getRawX();
                int deltaX = tempX - moveX;
                tempX = moveX;
                if (Math.abs(moveX - downX) > mTouchSlop && Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
                    isSilding = true;
                }
                if (moveX - downX >= 0 && isSilding) {
                    mParentView.scrollBy(deltaX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                isSilding = false;
                if (mParentView.getScrollX() <= -viewWidth / 4) {
                    scrollToRight();
                    isFinish = true;
                } else {
                    scrollOrigin();
                    isFinish = false;
                }
                break;
        }
        return true;
    }


    /**
     * 滚动出界面至右侧
     */
    private void scrollToRight() {
        final int delta = (viewWidth + mParentView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        mScroller.startScroll(mParentView.getScrollX(), 0, -delta + 1, 0, Math.abs(delta));
        postInvalidate();
    }

    /**
     * 滚动到起始位置
     */
    private void scrollOrigin() {
        int delta = mParentView.getScrollX();
        mScroller.startScroll(mParentView.getScrollX(), 0, -delta, 0, Math.abs(delta));
        postInvalidate();
    }


    @Override
    public void computeScroll() {
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (mScroller.computeScrollOffset()) {
            mParentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

            if (mScroller.isFinished()) {
                if (onSildingFinishListener != null && isFinish) {
                    onSildingFinishListener.onSildingFinish();
                }
            }
        }
    }


    public interface OnSildingFinishListener {
        public void onSildingFinish();
    }
}

