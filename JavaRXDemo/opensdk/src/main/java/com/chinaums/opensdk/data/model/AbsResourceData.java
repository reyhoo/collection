package com.chinaums.opensdk.data.model;

import com.chinaums.opensdk.cons.DynamicResourceWorkspace;
import com.chinaums.opensdk.download.model.AreaListPack;
import com.chinaums.opensdk.download.model.BizListPack;
import com.chinaums.opensdk.download.model.CategoryListPack;
import com.chinaums.opensdk.download.model.ClientUpdateListPack;
import com.chinaums.opensdk.download.model.DisplayBizListPack;
import com.chinaums.opensdk.download.model.PersonalListPack;
import com.chinaums.opensdk.download.model.RecommendBizListPack;

public abstract class AbsResourceData {

    public abstract DynamicResourceWorkspace getResourceWorkspace();

    /**
     * 全业务列表
     */
    private final BizListPack bizListPack = new BizListPack(
            getResourceWorkspace());

    /**
     * 分类列表
     */
    private final CategoryListPack categoryListPack = new CategoryListPack(
            getResourceWorkspace());

    /**
     * 地区列表
     */
    private final AreaListPack areaListPack = new AreaListPack(
            getResourceWorkspace());

    /**
     * 客户端升级列表
     */
    private final ClientUpdateListPack clientUpdateListPack = new ClientUpdateListPack(
            getResourceWorkspace());

    /**
     * 个性化列表
     */
    private final PersonalListPack personalListPack = new PersonalListPack();

    /**
     * 推荐列表
     */
    private final RecommendBizListPack recommendBizListPack = new RecommendBizListPack();

    /**
     * 显示列表
     */
    private final DisplayBizListPack displayBizListPack = new DisplayBizListPack();

    public final BizListPack getBizListPack() {
        return bizListPack;
    }

    public final CategoryListPack getCategoryListPack() {
        return categoryListPack;
    }

    public final AreaListPack getAreaListPack() {
        return areaListPack;
    }

    public final PersonalListPack getPersonalListPack() {
        return personalListPack;
    }

    public final RecommendBizListPack getRecommendBizListPack() {
        return recommendBizListPack;
    }

    public final DisplayBizListPack getDisplayBizListPack() {
        return displayBizListPack;
    }

    public final String getPersonalListPackCodes() {
        return null;
    }

    public final String getRecommendBizListPackCodes() {
        return null;
    }

    public final String getDisplayBizListPackBizCodes() {
        return null;
    }

    public final String getDisplayBizListPackAdsCodes() {
        return null;
    }

    public final ClientUpdateListPack getClientUpdateListPack() {
        return clientUpdateListPack;
    }

}
