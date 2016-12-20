package com.uidemo.recycleview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.uidemo.AppManageActivity;
import com.uidemo.R;
import com.uidemo.bean.AppCellBean;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/4/12.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements MyItemTouchCallback.ItemTouchAdapter {

    private Context context;
    private int src;
    private List<AppCellBean> results;
    private int mCurrentMode = AppManageActivity.MODE_NORMAL;

    public RecyclerAdapter(int src, List<AppCellBean> results) {
        this.results = results;
        this.src = src;
    }

    public void setShowMode(int mode) {
        mCurrentMode = mode;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(src, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.imageView.setImageResource(results.get(position).resId);
        holder.textView.setText(results.get(position).name);
        if (mCurrentMode == AppManageActivity.MODE_NORMAL) {
            holder.borderView.setBackgroundColor(Color.TRANSPARENT);
            holder.itemView.setBackgroundResource(R.drawable.app_cell_bg_selector);
//            holder.itemView.setBackgroundColor(Color.RED);
            holder.itemView.setClickable(true);
        } else {
            holder.borderView.setBackgroundResource(R.drawable.dash_line_bg);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            holder.itemView.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
//        if (fromPosition==results.size()-1 || toPosition==results.size()-1){
//            return;
//        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(results, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(results, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        results.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;
        public View borderView;

        public MyViewHolder(View itemView) {
            super(itemView);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.height = (int) (width / 4d * 180 / 186);
            layoutParams.width = width / 4;
            itemView.setLayoutParams(layoutParams);
            textView = (TextView) itemView.findViewById(R.id.cell_tv);
            imageView = (ImageView) itemView.findViewById(R.id.cell_iv);
            borderView = itemView.findViewById(R.id.cell_border_view);
        }
    }
}
