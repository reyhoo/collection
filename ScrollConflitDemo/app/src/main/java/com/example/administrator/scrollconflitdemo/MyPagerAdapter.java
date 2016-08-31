package com.example.administrator.scrollconflitdemo;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class MyPagerAdapter extends PagerAdapter {

    private List<ArrayList<String>> mItems;
    private List<View> mViews;

    private Context context;

    public MyPagerAdapter(Context context, List<ArrayList<String>> items) {
        this.context = context;
        setItems(items);
        initViews();
    }

    private void initViews() {
        mViews = new ArrayList<>();
        for (int i = 0; i < mItems.size(); i++) {
            ListView lv = new ListView(context);
            List<String> list = mItems.get(i);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);
            mViews.add(lv);
        }
    }

    private void setItems(List<ArrayList<String>> items) {
        if (items != null) {
            this.mItems = new ArrayList<>(items);
            if (this.mItems.size() > 0) {
                this.mItems.add(items.get(0));
                this.mItems.add(0, items.get(items.size() - 1));
            }
        } else {
            mItems = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = mViews.get(position);
        container.removeView(view);
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
