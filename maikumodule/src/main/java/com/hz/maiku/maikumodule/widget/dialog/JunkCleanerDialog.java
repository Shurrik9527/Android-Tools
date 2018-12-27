package com.hz.maiku.maikumodule.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.widget.JunkCleanerView;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 清理垃圾弹框
 * @date 2018/9/11
 * @email 252774645@qq.com
 */
public class JunkCleanerDialog extends Dialog {

    private JunkCleanerView mJunkCleanerView;
    private View mContentView;
    private DismissListener mDismissListener;
    public JunkCleanerDialog(@NonNull Context context ,DismissListener dismissListener) {
        this(context, R.style.junk_finish_dialog);
        this.mDismissListener =dismissListener;
    }

    public JunkCleanerDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);

        mContentView = LayoutInflater.from(context)
                .inflate(R.layout.junkcleaner_dialog_layout, null);
        mJunkCleanerView = (JunkCleanerView) mContentView.findViewById(R.id.junkcleaner_jcv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(false);
        setContentView(mContentView);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mJunkCleanerView.startAnimation();
        mJunkCleanerView.setAnimationListener(new JunkCleanerView.IAnimationListener() {
            @Override
            public void onDustbinFadeIn() {

            }

            @Override
            public void onProcessRecycler() {

            }

            @Override
            public void onShakeDustbin() {

            }

            @Override
            public void onDustbinFadeOut() {

            }

            @Override
            public void onAnimationFinish() {
                if (isShowing()) {
                    try {
                        dismiss();
                        if(mDismissListener!=null){
                            mDismissListener.callBack();
                        }
                    }catch (Exception e){

                    }
                }
            }
        });
    }


    public interface  DismissListener {
        void callBack();
    }
}
