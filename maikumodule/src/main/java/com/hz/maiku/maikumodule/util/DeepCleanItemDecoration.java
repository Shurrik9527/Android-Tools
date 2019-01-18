package com.hz.maiku.maikumodule.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author heguogui
 * update by Shurrik at 2018-12-29 for GridManager 2 cols
 */
public class DeepCleanItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public DeepCleanItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;
        outRect.bottom = mSpace;
        if (parent.getChildAdapterPosition(view)%3 == 0 ) {
            outRect.left = mSpace;
        }
    }

}
