package com.hz.maiku.maikumodule.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hz.maiku.maikumodule.R;


/**
 * [类功能说明]
 * 确认弹框
 * @author HeGuoGui
 * @version 2.1.0
 * @time 2017/7/25 0025
 */
public class AlertSingleDialog extends Dialog implements View.OnClickListener {

    private String title;
    private String content;
    private ConfirmListener confirmListener;
    protected TextView titleTv;
    private TextView contentTv;
    private TextView confirmTv;
    private String btnStr;

    public AlertSingleDialog(Context context, String title, String content, String btnstr, ConfirmListener confirmListener) {
        super(context, R.style.common_alert_dialog);
        this.title = title;
        this.content = content;
        this.confirmListener = confirmListener;
        this.btnStr=btnstr;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_single_dialog);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        titleTv = (TextView) findViewById(R.id.title);
        contentTv = (TextView) findViewById(R.id.content);
        confirmTv = (TextView) findViewById(R.id.confirm_tv);
        confirmTv.setOnClickListener(this);

        if(TextUtils.isEmpty(title)){
            titleTv.setVisibility(View.GONE);
        }else {
            titleTv.setText(title);
            titleTv.setVisibility(View.VISIBLE);
        }

        if(!TextUtils.isEmpty(btnStr)){
            confirmTv.setText(btnStr);
        }
        contentTv.setText(content);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.confirm_tv){
            dismiss();
        }
    }

    public interface ConfirmListener {
        void callback();
    }

    public interface CancelListener {
        void callback();
    }

}
