package com.example.administrator.scrollconflitdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MyFragmentAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "MyFragmentAdapter_";
    private List<ArrayList<String>> items;

    public MyFragmentAdapter(List<ArrayList<String>> items, FragmentManager fm) {
        super(fm);
        setItems(items);
    }

    public void setItems(List<ArrayList<String>> items) {
        if (items == null) {
            this.items = new ArrayList<>();
        } else {
            this.items = new ArrayList<>(items);
            if (this.items.size() > 0) {
                this.items.add(items.get(0));
                this.items.add(0, items.get(items.size() - 1));
            }

        }

    }

    @Override
    public Fragment getItem(int position) {
        Log.i(TAG, TAG + "getItem:" + position);
        ArrayList<String> list = items.get(position);
        return MyFragment.getInstance(list);
    }


    @Override
    public int getCount() {
        return items.size();
    }
}
