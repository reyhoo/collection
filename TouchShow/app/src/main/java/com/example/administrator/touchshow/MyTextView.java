package com.example.administrator.touchshow;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/11.
 */

public class MyTextView extends TextView {

    public static final String TAG = "MyTextView_";
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        return true;
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


}
