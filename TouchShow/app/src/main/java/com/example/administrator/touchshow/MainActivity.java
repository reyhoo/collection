package com.example.administrator.touchshow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity_";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyTextView tv = (MyTextView) findViewById(R.id.tv);
        //设置enable为false,onTouch不会执行。
//        tv.setEnabled(false);
        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
                Log.i("MyTextView_","onTouch:"+action);
                return false;
            }
        });
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

}
