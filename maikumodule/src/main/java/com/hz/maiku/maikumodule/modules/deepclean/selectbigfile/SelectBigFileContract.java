package com.hz.maiku.maikumodule.modules.deepclean.selectbigfile;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.BigFileBean;


import java.util.List;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class SelectBigFileContract {

    public interface View extends BaseView<Presenter> {
        //显示当前运行的进行数据
        void showData(List<BigFileBean> mlists);
        void reflashView();
        void cleanSuccess(long mSize);
    }

    public interface Presenter extends BasePresenter {
        void delectBigFileList(List<BigFileBean> mlists, long mSize);
        void getBigFiles();
    }
}
