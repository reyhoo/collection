package com.uidemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/10/14.
 */

public class MineActivity extends Activity {

    private int mPicLayoutWidth;
    private LinearLayout mPicLayout;
    private RoundImageView mPicIv;
    private Bitmap mPicBitmap;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mine_2_0_0);
        mPicBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mine_default_my_pic);
        mPicLayout = (LinearLayout) findViewById(R.id.my_pic_layout);
        mPicIv = (RoundImageView) findViewById(R.id.my_pic_iv);
        mListView = (ListView) findViewById(R.id.mine_listview);


        mListView.setAdapter(new MineAdapter(this,null));
        mPicLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPicLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mPicLayoutWidth = mPicLayout.getWidth();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mPicLayout.getLayoutParams();
                params.height = mPicLayoutWidth;
                mPicLayout.requestLayout();
            }
        });
        mPicIv.setImageBitmap(mPicBitmap);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class< ? extends  Activity> cls = MerchantServiceActivity.class;
                int mode =position % 3;
                if(mode == 0){
                    cls = CashierActivity.class;
                }else if(mode == 2){
                    cls = GoodAppActivity.class;
                }
                Intent intent = new Intent(getApplicationContext(),cls);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        if(mPicBitmap!=null || !mPicBitmap.isRecycled()){
            mPicBitmap.recycle();
        }
        super.onDestroy();
    }
}
