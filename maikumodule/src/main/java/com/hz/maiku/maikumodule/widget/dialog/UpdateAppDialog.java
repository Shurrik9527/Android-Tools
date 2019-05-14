package com.hz.maiku.maikumodule.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hz.maiku.maikumodule.R;


/**
 * [类功能说明]
 * 确认弹框
 * @author HeGuoGui
 * @version 2.1.0
 * @time 2017/7/25 0025
 */
public class UpdateAppDialog extends Dialog implements View.OnClickListener {

    private String content;
    private ConfirmListener confirmListener;
    protected TextView titleTv;
    private TextView contentTv;
    private TextView confirmTv;
    private TextView cancleTv;
    private LinearLayout canclell;
    private boolean mIsTrue;

    public UpdateAppDialog(Context context,String content,boolean isTrue,ConfirmListener confirmListener) {
        super(context, R.style.common_alert_dialog);
        this.content = content;
        this.confirmListener = confirmListener;
        this.mIsTrue=isTrue;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_updateapp_dialog);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);

        titleTv = (TextView) findViewById(R.id.dialog_title);
        contentTv = (TextView) findViewById(R.id.dialog_content);
        confirmTv = (TextView) findViewById(R.id.dialog_confirm_tv);
        cancleTv =(TextView)findViewById(R.id.dialog_cancle_tv);
        canclell =(LinearLayout)findViewById(R.id.dialog_cancle_ll);
        confirmTv.setOnClickListener(this);
        cancleTv.setOnClickListener(this);
        if(mIsTrue){
            canclell.setVisibility(View.GONE);
        }else {
            canclell.setVisibility(View.VISIBLE);
        }

        if(!TextUtils.isEmpty(content)){
            content =content.replaceAll(" ","\n");
            contentTv.setText(content);
        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.dialog_confirm_tv){
            confirmListener.callback();
            dismiss();
        }else if(view.getId()==R.id.dialog_cancle_tv){
            dismiss();
        }
    }

    public interface ConfirmListener {
        void callback();
    }

}
