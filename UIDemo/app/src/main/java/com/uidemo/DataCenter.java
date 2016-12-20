package com.uidemo;

import com.uidemo.bean.AppCellBean;
import com.uidemo.bean.AppGroupBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public class DataCenter {

    //    public static final List<AppCellBean>myApp = new ArrayList<>();
    public static final List<AppGroupBean> appGroup = new ArrayList<>();

    //    public static final List<AppCellBean>recommendApp = new ArrayList<>();
    static {


        AppGroupBean groupBean = new AppGroupBean();
        appGroup.add(groupBean);
        groupBean.name = "商户增值应用";
        groupBean.apps = new ArrayList<AppCellBean>();
        AppCellBean cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "商户付款";
        cellBean.resId = R.drawable.app_mer_pay;
        groupBean.apps.add(cellBean);
        cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "农产品收购";
        cellBean.resId = R.drawable.app_nongchanpingshougou;
        groupBean.apps.add(cellBean);
        cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "实时转账";
        cellBean.resId = R.drawable.app_real_time_transfer;
        groupBean.apps.add(cellBean);
        cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "物流小跟班";
        cellBean.resId = R.drawable.app_wuliuxiaogenban;
        groupBean.apps.add(cellBean);

        cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "IC卡服务";
        cellBean.resId = R.drawable.app_ic_service;
        groupBean.apps.add(cellBean);

        cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "手机充值";
        cellBean.resId = R.drawable.app_mobile_charge;
        groupBean.apps.add(cellBean);

        cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "信用卡还款";
        cellBean.resId = R.drawable.app_credit_payment;
        groupBean.apps.add(cellBean);

        cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "湖南人保";
        cellBean.inMyAppList = true;
        cellBean.resId = R.drawable.app_hunanrenbao;
        groupBean.apps.add(cellBean);

        cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "湖南助农取款";
        cellBean.inMyAppList = true;
        cellBean.resId = R.drawable.app_hunanzhumingshoukuan;
        groupBean.apps.add(cellBean);


        cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "易POS管理";
        cellBean.inMyAppList = true;
        cellBean.resId = R.drawable.app_mpos_manage;
        groupBean.apps.add(cellBean);

        cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "余额查询";
        cellBean.inMyAppList = true;
        cellBean.resId = R.drawable.app_balance_inquery;
        groupBean.apps.add(cellBean);


        groupBean = new AppGroupBean();
        appGroup.add(groupBean);
        groupBean.name = "行业应用";
        groupBean.apps = new ArrayList<AppCellBean>();
        cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "广告发布";
        cellBean.desc = "广告发布";
        cellBean.inRecommendList = true;
        cellBean.resId = R.drawable.app_adver_send;
        groupBean.apps.add(cellBean);
        cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "免费招聘";
        cellBean.desc = "免费招聘";
        cellBean.inRecommendList = true;
        cellBean.resId = R.drawable.app_mianfeizhaopingi;
        groupBean.apps.add(cellBean);
        cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "我的招聘";
        cellBean.desc = "我的招聘";
        cellBean.inRecommendList = true;
        cellBean.resId = R.drawable.app_wodezhaoping;
        groupBean.apps.add(cellBean);
        cellBean = new AppCellBean(groupBean.groupId);
        cellBean.name = "餐饮ERP";
        cellBean.desc = "餐饮ERP";
        cellBean.inRecommendList = true;
        cellBean.resId = R.drawable.app_canyingerp;
        groupBean.apps.add(cellBean);


    }

}
