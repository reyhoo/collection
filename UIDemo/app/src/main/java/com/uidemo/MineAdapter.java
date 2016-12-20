package com.uidemo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uidemo.bean.ListItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */

public class MineAdapter extends BaseAdapter {
    private int itemHeight;
    private Activity context;
    private LayoutInflater mInflater;
    private int mItemMarginLeft;
    private List<ListItemInfo> listItemInfos;

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewHolder vh = (ViewHolder) v.getTag();
            listItemInfos.get(vh.position).function.run();
        }
    };

    public MineAdapter(Activity context, List<ListItemInfo> listItemInfos) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.listItemInfos = listItemInfos;
        computeSize();
    }

    private void computeSize() {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        itemHeight = 104 * dm.heightPixels / 1334;
        mItemMarginLeft = 46 * dm.widthPixels / 750;

    }

    @Override
    public int getCount() {
        return listItemInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder vh = null;
        if (v == null) {
            v = mInflater.inflate(R.layout.mine_item, null);
            vh = new ViewHolder();
            v.setTag(vh);
            vh.mainLayout = (LinearLayout) v.findViewById(R.id.mine_item_main_layout);
            vh.iconIv = (ImageView) v.findViewById(R.id.mine_item_icon_iv);
            vh.nameTv = (TextView) v.findViewById(R.id.mine_item_name_tv);
            vh.rightTv = (TextView) v.findViewById(R.id.mine_item_right_tv);

            vh.dividerView = v.findViewById(R.id.mine_item_divider_view);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vh.dividerView.getLayoutParams();
            params.leftMargin = mItemMarginLeft;
            vh.dividerView.setLayoutParams(params);
            params = (LinearLayout.LayoutParams) vh.mainLayout.getLayoutParams();
            params.height = itemHeight;
            vh.mainLayout.setLayoutParams(params);
            //#dddddd
        } else {
            vh = (ViewHolder) v.getTag();
        }

        vh.position = position;
        vh.mainLayout.setOnClickListener(mListener);
        vh.mainLayout.setTag(vh);
        if (position == listItemInfos.size() - 1) {
            vh.dividerView.setVisibility(View.GONE);
        } else {
            vh.dividerView.setVisibility(View.VISIBLE);
        }

        ListItemInfo itemInfo = listItemInfos.get(position);
        vh.iconIv.setImageResource(itemInfo.iconResId);
        vh.nameTv.setText(itemInfo.titleResId);
        vh.rightTv.setText(itemInfo.rightText);
        return v;
    }

    private class ViewHolder {
        LinearLayout mainLayout;
        ImageView iconIv;
        TextView nameTv;
        TextView rightTv;
        View dividerView;
        int position;
    }
}
