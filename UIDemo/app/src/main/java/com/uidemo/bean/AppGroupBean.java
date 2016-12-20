package com.uidemo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 */

public class AppGroupBean {
    private static int i;
    public int groupId;
    public List<AppCellBean> apps;
    public String name;
    public AppGroupBean(){
        groupId = ++ i;
    }
}
