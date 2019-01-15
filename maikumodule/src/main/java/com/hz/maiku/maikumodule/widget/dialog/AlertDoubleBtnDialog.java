package com.hz.maiku.maikumodule.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hz.maiku.maikumodule.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/9
 * @email 252774645@qq.com
 */
public class AlertDoubleBtnDialog extends Dialog implements View.OnClickListener{

    private String title;
    private String content;
    private AlertDoubleBtnDialog.ConfirmListener confirmListener;
    private AlertDoubleBtnDialog.CancelListener cancelListener;
    protected TextView titleTv;
    private TextView contentTv;
    private TextView confirmTv;
    private TextView cancleTv;
    private String btnStr;

    public AlertDoubleBtnDialog(Context context, String title, String content, String btnstr, AlertDoubleBtnDialog.ConfirmListener confirmListener) {
        super(context, R.style.common_alert_dialog);
        this.title = title;
        this.content = content;
        this.confirmListener = confirmListener;
        this.btnStr=btnstr;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_double_dialog);
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
        titleTv = (TextView) findViewById(R.id.title);
        contentTv = (TextView) findViewById(R.id.content);
        confirmTv = (TextView) findViewById(R.id.dialog_confirm_tv);
        cancleTv =(TextView)findViewById(R.id.dialog_cancle_tv);
        confirmTv.setOnClickListener(this);
        cancleTv.setOnClickListener(this);
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
        if(view.getId()==R.id.dialog_confirm_tv){
            confirmListener.callback();
        }
        dismiss();
    }

    public interface ConfirmListener {
        void callback();
    }

    public interface CancelListener {
        void callback();
    }

}
