package com.uidemo.bean;

/**
 * Created by Administrator on 2016/10/19.
 */

public class AppCellBean implements Comparable<AppCellBean>{
    static int i;
    public int cellId;
    public String name;
    public String desc;
    public int resId;
    public int groupId;

    public int order;

    public boolean inMyAppList=false;
    public boolean inRecommendList=false;
    public AppCellBean(int groupId){
        cellId = ++ i;
        order = cellId;
        this.groupId = groupId;
    }


    @Override
    public int compareTo(AppCellBean o) {
        int result = o.order-order;
        if(result<0){
            return 1;
        }
        return -1;
    }
}
