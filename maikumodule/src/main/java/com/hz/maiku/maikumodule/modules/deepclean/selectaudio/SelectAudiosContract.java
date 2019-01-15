package com.hz.maiku.maikumodule.modules.deepclean.selectaudio;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.AudioBean;


import java.util.List;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class SelectAudiosContract {

    public interface View extends BaseView<SelectAudiosContract.Presenter> {
        //显示当前运行的进行数据
        void showAudiosData(List<AudioBean> mlists);
        void reflashView();
        void cleanSuccess(long mSize);
        void startAudios(String url,int pos);
    }

    public interface Presenter extends BasePresenter {
        void delectAudiosList(List<AudioBean> mlists, long mSize);
        void getAudios();
    }
}
