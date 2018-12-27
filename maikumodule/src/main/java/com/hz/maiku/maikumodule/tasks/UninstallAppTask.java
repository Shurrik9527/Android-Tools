package com.hz.maiku.maikumodule.tasks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.hz.maiku.maikumodule.bean.AppManagerBean;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/25
 * @email 252774645@qq.com
 */
public class UninstallAppTask extends AsyncTask<String,Void,Boolean> {

    private static  final  String TAG = UninstallAppTask.class.getName();
    private Context mContext;
    private List<AppManagerBean> mlists;
    public UninstallAppTask(Context mContext, List<AppManagerBean> lists) {
        this.mContext = mContext;
        this.mlists =lists;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        for (int i=0;i<mlists.size();i++){
            AppManagerBean bean =mlists.get(i);
            if(bean.isSelect()){
                Uri uri = Uri.parse("package:" + bean.getApplicationInfo().packageName);
                //创建Intent意图
                Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                //执行卸载程序
                mContext.startActivity(intent);
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
