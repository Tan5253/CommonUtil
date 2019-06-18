package com.tan.libcommon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class InterceptFrameLayout extends FrameLayout {
    private boolean isIntercepted;

    public InterceptFrameLayout(Context context) {
        super(context);
    }

    public InterceptFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setIntercepted(boolean intercepted) {
        isIntercepted = intercepted;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(isIntercepted);
        return super.dispatchTouchEvent(ev);
    }
}
