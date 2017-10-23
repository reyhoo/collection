package com.chinaums.opensdk.download.model;

import com.chinaums.opensdk.cons.DynamicResourceWorkspace;
import com.chinaums.opensdk.cons.OpenConst.DynamicDownloadConf;
import com.chinaums.opensdk.data.model.ResourceSignHistory;
import com.chinaums.opensdk.download.process.AreaListParseProcess;
import com.chinaums.opensdk.manager.OpenConfigManager;
import com.chinaums.opensdk.manager.OpenDynamicBizHistoryManager;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class AreaListPack extends AbsListPack {

    /**
     * 城市级别area
     */
    private Area cityLevelArea;

    /**
     * Area根节点
     */
    private List<Area> areas;

    /**
     * 热门城市
     */
    private List<Area> hotAreas = new ArrayList<Area>();

    public AreaListPack(DynamicResourceWorkspace resourceWorkspace) {
        super(resourceWorkspace);
    }

    @Override
    protected String getPreloadResSignForUpdateSign() throws Exception {
        // 获取预安装的asset目录下的地区文件签名
        return OpenDynamicBizHistoryManager.getInstance().getResourcePreload()
                .getAreaSign();
    }

    @Override
    protected String getHistoryResSign() throws Exception {
        // 获取最后一次使用的地区列表的签名，放在SP里面
        return ResourceSignHistory.getAreaSign();
    }

    @Override
    protected void updateSignHistory(String sign) throws Exception {
        // 设置最后一次使用的地区列表的签名，放在SP里面
        ResourceSignHistory.setAreaSign(sign);
    }

    @Override
    protected String getSignUrl() {
        // 地区列表文件sign文件下载目录
        return OpenConfigManager
                .getProperty(OpenConfigManager.DOWNLOAD_FILE_AREA_SIGN_URL);
    }

    @Override
    public String getResUrl() {
        return OpenConfigManager
                .getProperty(OpenConfigManager.DOWNLOAD_FILE_AREA_URL);
    }

    @Override
    protected String getPrintLog(String msg) {
        return this.getClass().toString() + " [地区列表] " + msg;
    }

    @Override
    protected String getResOriginalPathSuffix() {
        // eg."/download/list/area"
        return DynamicDownloadConf.AREA_ORIGINAL_FOLDER;
    }

    @Override
    protected String getPreloadResPath() throws Exception {
        // eg."/list/area"
        return DynamicDownloadConf.AREA_FOLDER;
    }

    @Override
    protected String getResProcessPathSuffix() {
        // eg."/process/list/area"
        return DynamicDownloadConf.AREA_PROCESS_FOLDER;
    }

    @Override
    protected String getResOriginalFileName() {
        // eg."area.zip"
        return DynamicDownloadConf.AREA_ORIGINAL_FILE_NAME;
    }

    @Override
    protected String getResProcessFileName() {
        // eg."area.json"
        return DynamicDownloadConf.AREA_PROCESS_FILE_NAME;
    }

    @Override
    protected boolean initPack() throws Exception {
        //eg."/data/data/com.chinaums.xmsmk/bizspacea/process/list/area/area.json"
        areas = AreaListParseProcess.getInstance().parseJsontoArea(
                getResProcessPath() + File.separator + getResProcessFileName());
        Area area = (areas == null || areas.isEmpty()) ? null : areas.get(0);
        addHotAreas(area);
        cityLevelArea = area == null ? null : area.clone();
        return true;
    }

    /**
     * 获取市级及以上Area
     */
    public Area getArea() throws CloneNotSupportedException {
        return cityLevelArea;
    }

    public Area getWholeArea() {
        Area area = (areas == null || areas.isEmpty()) ? null : areas.get(0);
        return area;
    }

    public void addHotAreas(Area area) {
        if (area == null) {
            return;
        }
        if ("1".equals(area.getAreaHot())) {
            hotAreas.add(area);
        }
        Iterator<Area> iter = area.getChildren().iterator();
        while (iter.hasNext()) {
            addHotAreas(iter.next());
        }
    }

    public List<Area> getHotAreas() {
        return hotAreas;
    }

    public Area getAreaByName(String areaName) {
        return getAreaByName(areas, areaName);
    }

    private Area getAreaByName(List<Area> nodeAreas, String areaName) {
        if (nodeAreas == null) {
            return null;
        }
        Iterator<Area> iter = nodeAreas.iterator();
        while (iter.hasNext()) {
            Area iterArea = iter.next();
            if (iterArea.getAreaName().contains(areaName)
                    || areaName.contains(iterArea.getAreaName())) {
                return iterArea;
            }
            Area resultArea = getAreaByName(iterArea.getChildren(), areaName);
            if (resultArea == null) {
                continue;
            } else {
                return resultArea;
            }
        }
        return null;
    }

    public Area getAreaByLocation(String province, String city, String district) {
        Area countryArea = getWholeArea();
        if (countryArea == null || UmsStringUtils.isBlank(province)
                || UmsStringUtils.isBlank(city)) {
            return null;
        }
        Area provinceArea = null;
        for (Area tempArea : countryArea.getChildren()) {
            if (tempArea.getAreaName().contains(province)
                    || province.contains(tempArea.getAreaName())) {
                provinceArea = tempArea;
                break;
            }
        }
        if (provinceArea == null) {
            return null;
        }
        Area cityArea = null;
        for (Area tempArea : provinceArea.getChildren()) {
            if (tempArea.getAreaName().contains(city)
                    || city.contains(tempArea.getAreaName())) {
                cityArea = tempArea;
                break;
            }
        }
        if (cityArea == null) {
            return provinceArea;
        }
        if (UmsStringUtils.isBlank(district)) {
            return cityArea;
        }
        Area districtArea = null;
        for (Area tempArea : cityArea.getChildren()) {
            if (tempArea.getAreaName().contains(district)
                    || district.contains(tempArea.getAreaName())) {
                districtArea = tempArea;
                break;
            }
        }
        return districtArea == null ? cityArea : districtArea;
    }

    public Area getAreaByCode(String areaCode) {
        return getAreaByCode(areas, areaCode);
    }

    private Area getAreaByCode(List<Area> nodeAreas, String areaCode) {
        if (nodeAreas == null) {
            return null;
        }
        Iterator<Area> iter = nodeAreas.iterator();
        while (iter.hasNext()) {
            Area iterArea = iter.next();
            if (iterArea.getAreaCode().equals(areaCode)) {
                return iterArea;
            }
            Area resultArea = getAreaByCode(iterArea.getChildren(), areaCode);
            if (resultArea == null) {
                continue;
            } else {
                return resultArea;
            }
        }
        return null;
    }

    public static class Area implements Cloneable {

        /**
         * areaCode
         */
        private String areaCode;

        /**
         * areaName
         */
        private String areaName;

        /**
         * areaOfficialName
         */
        private String areaOfficialName;

        /**
         * areaLanguage
         */
        private AreaLanguage areaLanguage;

        /**
         * areaType
         */
        private String areaType;

        /**
         * children
         */
        private List<Area> children;

        /**
         * parent:这个字段的原始文件数据中没有
         */
        private Area parent;

        /**
         * areaHot
         */
        private String areaHot;

        /**
         * 仅复制市级及以上城市，并指定父节点
         */
        @Override
        public Area clone() throws CloneNotSupportedException {
            Area area = (Area) super.clone();
            area.setAreaLanguage(this.areaLanguage != null ? (AreaLanguage) this.areaLanguage
                    .clone() : area.getAreaLanguage());
            if (children == null || children.isEmpty()) {
                area.setChildren(new ArrayList<Area>());
                return area;
            }
            if (getAreaType().equalsIgnoreCase("city") && children != null
                    && !children.isEmpty()) {
                Iterator<Area> iter = children.iterator();
                while (iter.hasNext()) {
                    Area childArea = iter.next();
                    childArea.parent = this;
                }
                area.setChildren(new ArrayList<Area>());
                return area;
            }
            List<Area> copyChildren = new ArrayList<Area>(children.size());
            Iterator<Area> iter = children.iterator();
            while (iter.hasNext()) {
                Area childArea = iter.next();
                Area cloneChildArea = childArea.clone();
                childArea.parent = this;
                cloneChildArea.parent = area;
                copyChildren.add(cloneChildArea);
            }
            area.setChildren(copyChildren);
            return area;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public String getAreaName() {
            return areaName;
        }

        public AreaLanguage getAreaLanguage() {
            return areaLanguage;
        }

        public String getAreaType() {
            return areaType;
        }

        public List<Area> getChildren() {
            return children;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public void setAreaLanguage(AreaLanguage areaLanguage) {
            this.areaLanguage = areaLanguage;
        }

        public void setAreaType(String areaType) {
            this.areaType = areaType;
        }

        public void setChildren(List<Area> children) {
            this.children = children;
        }

        public Area getParent() {
            return parent;
        }

        public void setParent(Area parent) {
            this.parent = parent;
        }

        public String getAreaOfficialName() {
            return areaOfficialName;
        }

        public void setAreaOfficialName(String areaOfficialName) {
            this.areaOfficialName = areaOfficialName;
        }

        public String getAreaHot() {
            return areaHot;
        }

        public void setAreaHot(String areaHot) {
            this.areaHot = areaHot;
        }

    }


    public static class AreaLanguage implements Cloneable {

        /**
         * pinyin
         */
        private String pinyin;

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

    }

}