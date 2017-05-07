package com.example.yaolei.swipemenulistviewdemo;

import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AbOnListViewListener{

    private SwipeMenuListView mSwipeListView;

    private List<ApplicationInfo> mAppList;
    private AppAdapter mAdapter;

    boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeListView = (SwipeMenuListView) findViewById(R.id.lv);
        // 打开关闭下拉刷新加载更多功能
        mSwipeListView.setPullRefreshEnable(true);
        mSwipeListView.setPullLoadEnable(true);
        // 设置进度条的样式
        mSwipeListView.getHeaderView().setHeaderProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        mSwipeListView.getHeaderView().setBackgroundColor(
                getResources().getColor(android.R.color.white));
        mSwipeListView.getFooterView().setHeaderProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        mSwipeListView.getFooterView().setBackgroundColor(
                getResources().getColor(android.R.color.white));
        mSwipeListView.setAbOnListViewListener(this);


        mAppList = getPackageManager().getInstalledApplications(0);




        mAdapter = new AppAdapter();
        mSwipeListView.setAdapter(mAdapter);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mSwipeListView.setMenuCreator(creator);

        // step 2. listener item click event
        mSwipeListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                ApplicationInfo item = mAppList.get(position);
                switch (index) {
                    case 0:
                        // open
//                        open(item);
                        Toast.makeText(MainActivity.this, "open", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // delete
                        Toast.makeText(MainActivity.this, "delete", Toast.LENGTH_SHORT).show();
////					delete(item);
//                        mAppList.remove(position);
//                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        mSwipeListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        mSwipeListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        mSwipeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mSwipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), position + " click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRefresh() {
        isLoading = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isLoading = false;
                mSwipeListView.stopRefresh();
                mSwipeListView.stopLoadMore();
            }
        },5000);
    }

    @Override
    public void onLoadMore() {
        isLoading = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isLoading = false;
                mSwipeListView.stopRefresh();
                mSwipeListView.stopLoadMore();
            }
        },5000);
    }


    class AppAdapter extends BaseSwipListAdapter {

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public ApplicationInfo getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            ApplicationInfo item = getItem(position);
            holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
            holder.tv_name.setText(item.loadLabel(getPackageManager()));
            holder.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "iv_icon_click", Toast.LENGTH_SHORT).show();
                }
            });
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this,"iv_icon_click",Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_name;

            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }

        @Override
        public boolean getSwipEnableByPosition(int position) {
//            if(position % 2 == 0){
//                return false;
//            }
            if(isLoading)return false;
            return true;
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
