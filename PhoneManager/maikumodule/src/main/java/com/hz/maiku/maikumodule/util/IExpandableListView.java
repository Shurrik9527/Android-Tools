package com.hz.maiku.maikumodule.util;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 扩展约束
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public interface IExpandableListView<T> {
    boolean isExpanded();
    void setExpanded(boolean expanded);
    List<T> getSubItems();
    int getLevel();
}
