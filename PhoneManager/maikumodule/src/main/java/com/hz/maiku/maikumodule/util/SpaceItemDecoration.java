package com.hz.maiku.maikumodule.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/18
 * @email 252774645@qq.com
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration{

    int mSpace;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mSpace;
        }
    }

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }
}
