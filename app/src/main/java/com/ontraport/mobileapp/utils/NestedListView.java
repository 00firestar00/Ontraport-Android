package com.ontraport.mobileapp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

public class NestedListView extends ListView {

    private int oldCount = 0;

    public NestedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getCount() != oldCount) {
            if (getCount() > 1) {
                int height = getChildAt(0).getHeight() + 1;
                android.view.ViewGroup.LayoutParams params = getLayoutParams();
                params.height = getCount() * height;
                setLayoutParams(params);
            }
            oldCount = getCount();
        }
        super.onDraw(canvas);
    }
}
