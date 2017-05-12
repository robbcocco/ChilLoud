package com.starkoverflow.chilloud.classes;

import android.view.View;
import android.view.ViewTreeObserver;

public class OnViewGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
    private int maxHeight;
    private View view;

    public OnViewGlobalLayoutListener(View view, int maxHeight) {
        this.view=view;
        this.maxHeight=maxHeight;
    }

    @Override
    public void onGlobalLayout() {
        if (view.getHeight() > maxHeight)
            view.getLayoutParams().height = maxHeight;
    }
}