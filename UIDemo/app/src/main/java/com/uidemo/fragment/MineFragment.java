package com.uidemo.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.uidemo.MineAdapter;
import com.uidemo.R;
import com.uidemo.RoundImageView;
import com.uidemo.bean.ListItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */

public class MineFragment extends BaseFragment {


    private int mPicLayoutWidth;
    private LinearLayout mPicLayout;
    private RoundImageView mPicIv;
    private Bitmap mPicBitmap;

    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_2_0_0, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mPicBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mine_default_my_pic);
        mPicLayout = (LinearLayout) view.findViewById(R.id.my_pic_layout);
        mPicIv = (RoundImageView) view.findViewById(R.id.my_pic_iv);
        mListView = (ListView) view.findViewById(R.id.mine_listview);


        mListView.setAdapter(new MineAdapter(getActivity(),getShowList()));
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

            }
        });
    }


    private List<ListItemInfo> getShowList(){
        List<ListItemInfo> listItemInfos= new ArrayList<>();

        ListItemInfo listItemInfo = new ListItemInfo();
        listItemInfo.function = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), R.string.myInfomation, Toast.LENGTH_SHORT).show();
            }
        };
        listItemInfo.iconResId = R.drawable.mine_my_info;
        listItemInfo.titleResId = R.string.myInfomation;
        listItemInfos.add(listItemInfo);


        listItemInfo = new ListItemInfo();
        listItemInfo.function = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), R.string.myQuota, Toast.LENGTH_SHORT).show();
            }
        };
        listItemInfo.iconResId = R.drawable.mine_shoukuanerdu;
        listItemInfo.titleResId = R.string.myQuota;
        listItemInfos.add(listItemInfo);


        listItemInfo = new ListItemInfo();
        listItemInfo.function = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), R.string.cashierManager, Toast.LENGTH_SHORT).show();
            }
        };
        listItemInfo.iconResId = R.drawable.mine_cashier;
        listItemInfo.titleResId = R.string.cashierManager;
        listItemInfos.add(listItemInfo);



        listItemInfo = new ListItemInfo();
        listItemInfo.function = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), R.string.modifyPSW, Toast.LENGTH_SHORT).show();
            }
        };
        listItemInfo.iconResId = R.drawable.mine_modify_pwd;
        listItemInfo.titleResId = R.string.modifyPSW;
        listItemInfos.add(listItemInfo);


        listItemInfo = new ListItemInfo();
        listItemInfo.function = new Runnable() {
            @Override
            public void run() {
                //程序
                Toast.makeText(getActivity(), R.string.myPosUpgrade, Toast.LENGTH_SHORT).show();
            }
        };
        listItemInfo.iconResId = R.drawable.mine_mpos_update;
        listItemInfo.titleResId = R.string.myPosUpgrade;
        listItemInfos.add(listItemInfo);


        listItemInfo = new ListItemInfo();
        listItemInfo.function = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), R.string.mySound, Toast.LENGTH_SHORT).show();
            }
        };
        listItemInfo.iconResId = R.drawable.mine_compaint;
        listItemInfo.titleResId = R.string.mySound;
        listItemInfos.add(listItemInfo);


        listItemInfo = new ListItemInfo();
        listItemInfo.function = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), R.string.mposMinePageDefaultHomePageSetting, Toast.LENGTH_SHORT).show();
            }
        };
        listItemInfo.iconResId = R.drawable.mine_default_page_setting;
        listItemInfo.titleResId = R.string.mposMinePageDefaultHomePageSetting;
        listItemInfo.rightText = getString(R.string.mposHomePageMerService);
        listItemInfos.add(listItemInfo);


        listItemInfo = new ListItemInfo();
        listItemInfo.function = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), R.string.aboutUMS, Toast.LENGTH_SHORT).show();
            }
        };
        listItemInfo.iconResId = R.drawable.mine_about_ums;
        listItemInfo.titleResId = R.string.aboutUMS;
        listItemInfos.add(listItemInfo);

        return listItemInfos;
    }
    @Override
    public void onDestroyView() {
        if (mPicBitmap != null || !mPicBitmap.isRecycled()) {
            mPicBitmap.recycle();
        }
        super.onDestroyView();
    }
}
