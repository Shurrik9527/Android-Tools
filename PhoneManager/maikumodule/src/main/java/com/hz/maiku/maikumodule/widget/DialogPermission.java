package com.hz.maiku.maikumodule.widget;

import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.widget.dialog.BaseDialog;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 授权弹框
 * @date 2018/9/13
 * @email 252774645@qq.com
 */
public class DialogPermission extends BaseDialog {

    private TextView mBtnPermission;
    private onClickListener mOnClickListener;
    private onClickListener  mOnClickCloseListener;
    private TextView close_btn;
    public DialogPermission(Context context) {
        super(context);
    }

    @Override
    protected float setWidthScale() {
        return 0.9f;
    }

    @Override
    protected AnimatorSet setEnterAnim() {
        return null;
    }

    @Override
    protected AnimatorSet setExitAnim() {
        return null;
    }

    @Override
    protected void init() {

        setCanceledOnTouchOutside(false);
        setCancelable(false);
        close_btn =findViewById(R.id.btn_permission_close);
        mBtnPermission = (TextView) findViewById(R.id.btn_permission);
        mBtnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    dismiss();
                    mOnClickListener.onClick();
                }
            }
        });
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickCloseListener != null) {
                    dismiss();
                    mOnClickCloseListener.onClick();
                }
            }
        });
    }

    public void setOnClickListener(onClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnClickColseListener(onClickListener onClickListener) {
        mOnClickCloseListener = onClickListener;
    }

    public interface onClickListener {
        void onClick();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.applock_permission_dialog;
    }

}
