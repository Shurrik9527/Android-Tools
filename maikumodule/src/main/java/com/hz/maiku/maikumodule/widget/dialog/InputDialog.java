package com.hz.maiku.maikumodule.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hz.maiku.maikumodule.R;


/**
 * [类功能说明]
 * 取消 确认弹框
 * @author HeGuoGui
 * @version 3.0.0
 * @time 2018/12/25 0025
 */
public class InputDialog extends Dialog implements View.OnClickListener {

    private String title;
    private String content;
    private ConfirmListener confirmListener;
    protected TextView titleTv;
    private EditText contentTv;
    private Button confirmTv;
    private Button cancelTv;


    public InputDialog(Context context, String title, String content, ConfirmListener confirmListener) {
        super(context, R.style.common_alert_dialog);
        this.title = title;
        this.content = content;
        this.confirmListener = confirmListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_input_dialog);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        titleTv = (TextView) findViewById(R.id.title);
        contentTv = (EditText) findViewById(R.id.content_et);
        confirmTv = (Button) findViewById(R.id.confirm_tv);
        cancelTv = (Button) findViewById(R.id.cancel_tv);

        confirmTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);

        if(TextUtils.isEmpty(title)){
            titleTv.setVisibility(View.GONE);
        }else {
            titleTv.setText(title);
            titleTv.setVisibility(View.VISIBLE);
        }

        confirmTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content=contentTv.getText().toString().trim();
                if(TextUtils.isEmpty(content) ||content.length() < 6){
                    confirmTv.setEnabled(false);
                }else{
                    confirmTv.setEnabled(true);
                }
            }
        });



    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.confirm_tv){
            String content=null;
            if(contentTv!=null){
                content =contentTv.getText().toString().trim();
            }
            confirmListener.callback(content);
        }else {
            dismiss();
        }
    }

    public interface ConfirmListener {
        void callback(String content);
    }

}
