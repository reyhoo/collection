package com.chinaums.opensdk.data.model;

import java.io.Serializable;

public class ResourcePreload implements Serializable {

    private static final long serialVersionUID = -1591242228507296923L;

    /**
     * 默认个性化列表
     */
    private String personalBizCodes;

    /**
     * 待展示业务列表
     */
    private String displayBizCodes;

    /**
     * 展示的广告列表
     */
    private String displayAdsCodes;

    /**
     * 推荐列表
     */
    private String recommendBizCodes;

    /**
     * 全业务列表签名
     */
    private String bizlistSign;

    /**
     * 分类列表签名
     */
    private String categorySign;

    /**
     * 区域列表签名
     */
    private String areaSign;

    /**
     * 客户端更新列表签名
     */
    private String clientUpdateSign;

    public ResourcePreload(String personalBizCodes, String displayBizCodes,
                           String displayAdsCodes, String recommendBizCodes,
                           String bizlistSign, String categorySign, String areaSign,
                           String clientUpdateSign) {
        this.personalBizCodes = personalBizCodes;
        this.displayBizCodes = displayBizCodes;
        this.displayAdsCodes = displayAdsCodes;
        this.recommendBizCodes = recommendBizCodes;
        this.bizlistSign = bizlistSign;
        this.categorySign = categorySign;
        this.areaSign = areaSign;
        this.clientUpdateSign = clientUpdateSign;
    }

    public String getPersonalBizCodes() {
        return personalBizCodes;
    }

    public void setPersonalBizCodes(String personalBizCodes) {
        this.personalBizCodes = personalBizCodes;
    }

    public String getDisplayBizCodes() {
        return displayBizCodes;
    }

    public void setDisplayBizCodes(String displayBizCodes) {
        this.displayBizCodes = displayBizCodes;
    }

    public String getDisplayAdsCodes() {
        return displayAdsCodes;
    }

    public void setDisplayAdsCodes(String displayAdsCodes) {
        this.displayAdsCodes = displayAdsCodes;
    }

    public String getRecommendBizCodes() {
        return recommendBizCodes;
    }

    public void setRecommendBizCodes(String recommendBizCodes) {
        this.recommendBizCodes = recommendBizCodes;
    }

    public String getBizlistSign() {
        return bizlistSign;
    }

    public void setBizlistSign(String bizlistSign) {
        this.bizlistSign = bizlistSign;
    }

    public String getCategorySign() {
        return categorySign;
    }

    public void setCategorySign(String categorySign) {
        this.categorySign = categorySign;
    }

    public String getAreaSign() {
        return areaSign;
    }

    public void setAreaSign(String areaSign) {
        this.areaSign = areaSign;
    }

    public String getClientUpdateSign() {
        return clientUpdateSign;
    }

    public void setClientUpdateSign(String clientUpdateSign) {
        this.clientUpdateSign = clientUpdateSign;
    }

}
