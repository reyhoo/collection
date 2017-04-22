package com.example.administrator.touchshow;

import android.content.Context;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/11/11.
 */

public class MyLinearLayout extends LinearLayout {

    public static final String TAG = "MyLinearLayout_";
    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String action = "";


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";


                break;

            case MotionEvent.ACTION_MOVE:

                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";

                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";

                break;
        }
        Log.i(TAG,"onTouchEvent:"+action);
        return super.onTouchEvent(event);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        String action = "";


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";


                break;

            case MotionEvent.ACTION_MOVE:

                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";

                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";

                break;
        }
        Log.i(TAG,"dispatchTouchEvent:"+action);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        String action = "";


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";


                break;

            case MotionEvent.ACTION_MOVE:

                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";

                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";

                break;
        }
        Log.i(TAG,"onInterceptTouchEvent:"+action);
        return super.onInterceptTouchEvent(event);
//        return true;
    }
}
