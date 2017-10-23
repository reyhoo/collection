package com.chinaums.opensdk.download.process;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.chinaums.opensdk.cons.DynamicResourceWorkspace;
import com.chinaums.opensdk.data.model.AbsResourceData;
import com.chinaums.opensdk.data.model.ResourceDataWorkA;
import com.chinaums.opensdk.data.model.ResourceDataWorkB;
import com.chinaums.opensdk.download.listener.ResourceManagerCheckUserInvalidListener;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.download.listener.ResourceManagerMultiListener;
import com.chinaums.opensdk.download.listener.ResourceManagerMultiListener.ErrorResult;
import com.chinaums.opensdk.download.listener.ResourceProcessListener;
import com.chinaums.opensdk.download.model.AdsPack;
import com.chinaums.opensdk.download.model.AppPack;
import com.chinaums.opensdk.download.model.AreaListPack.Area;
import com.chinaums.opensdk.download.model.BasePack;
import com.chinaums.opensdk.download.model.BasePack.BizSearchIndex;
import com.chinaums.opensdk.download.model.BizListPack;
import com.chinaums.opensdk.download.model.BizPack;
import com.chinaums.opensdk.download.model.CategoryIconPack;
import com.chinaums.opensdk.download.model.CategoryListPack;
import com.chinaums.opensdk.download.model.CategoryListPack.Category;
import com.chinaums.opensdk.download.model.ClientUpdateListPack.ClientUpdateList;
import com.chinaums.opensdk.download.model.ConfPack;
import com.chinaums.opensdk.download.model.IconPack;
import com.chinaums.opensdk.download.model.LargeIconPack;
import com.chinaums.opensdk.download.model.Resource;
import com.chinaums.opensdk.download.model.SharePack;
import com.chinaums.opensdk.download.model.ShowPack;
import com.chinaums.opensdk.download.model.StdIconPack;
import com.chinaums.opensdk.download.service.ResourceMonitorService;
import com.chinaums.opensdk.event.model.DynamicResourceWorkspaceChangedEvent;
import com.chinaums.opensdk.exception.ResourceManagerListenerUndefinedException;
import com.chinaums.opensdk.exception.ResourceNotFoundException;
import com.chinaums.opensdk.manager.IOpenManager;
import com.chinaums.opensdk.manager.OpenDynamicBizHistoryManager;
import com.chinaums.opensdk.util.UmsConvertUtil;
import com.chinaums.opensdk.util.UmsEventBusUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ResourceManager implements IOpenManager {

    /**
     * 单例
     */
    private static ResourceManager instance = new ResourceManager();

    /**
     * 事务处理器
     */
    private ResourceAsyncProcessor processor = ResourceAsyncProcessor
            .getInstance();

    /**
     * resourceDataWorkA
     */
    private final AbsResourceData resourceDataWorkA = new ResourceDataWorkA();

    /**
     * resourceDataWorkB
     */
    private final AbsResourceData resourceDataWorkB = new ResourceDataWorkB();

    /**
     * resourceMonitorService
     */
    private ResourceMonitorService resourceMonitorService;

    private ResourceManager() {

    }

    public static ResourceManager getInstance() {
        return instance;
    }

    @Override
    public void init(Context context) {
        initResourceMonitorService(context);
    }

    @Override
    public void destroy() {

    }

    public AbsResourceData getResourceWorkspace() {
        return OpenDynamicBizHistoryManager.getInstance()
                .getResourceWorkspaceHistory().getActivityWorkspace();
    }

    public AbsResourceData getResourceSpace(String space) {
        if (UmsStringUtils.isBlank(space))
            return getResourceWorkspace();
        if (resourceDataWorkA.getResourceWorkspace().getValue().equals(space)) {
            return resourceDataWorkA;
        } else {
            return resourceDataWorkB;
        }
    }

    public AbsResourceData getResourceSpace(
            DynamicResourceWorkspace resourceWorkspace) {
        if (resourceWorkspace == null)
            return getResourceWorkspace();
        return getResourceSpace(resourceWorkspace.getValue());
    }

    public AbsResourceData getResourceBackspace() {
        if (getResourceWorkspace().getResourceWorkspace().equals(
                DynamicResourceWorkspace.SpaceA)) {
            return resourceDataWorkB;
        } else {
            return resourceDataWorkA;
        }
    }

    public void changeCurWorkspace() {
        OpenDynamicBizHistoryManager
                .getInstance()
                .getResourceWorkspaceHistory()
                .setActivityWorkspace(
                        getResourceBackspace().getResourceWorkspace());
    }

    /**
     * 根据编码得到所有相关的包
     */
    @SuppressWarnings("unchecked")
    public List<BasePack> getPacksByCode(DynamicResourceWorkspace space,
                                         String code) throws Exception {
        return (List<BasePack>) getResourceSpace(space).getBizListPack()
                .getArrayByClassTypeAndCode(BasePack.class, code);
    }

    /**
     * 根据获得实际可用的包
     */
    public BizPack getBizPackByCode(DynamicResourceWorkspace space, String code)
            throws Exception {
        return (BizPack) getResourceSpace(space).getBizListPack()
                .getByClassTypeAndCode(BizPack.class, code);
    }

    public SharePack getSharePackByCode(DynamicResourceWorkspace space,
                                        String code) throws Exception {
        return (SharePack) getResourceSpace(space).getBizListPack()
                .getByClassTypeAndCode(SharePack.class, code);
    }

    @SuppressWarnings("unchecked")
    public List<IconPack> getIconPacksByCode(DynamicResourceWorkspace space,
                                             String code) throws Exception {
        return (List<IconPack>) getResourceSpace(space).getBizListPack()
                .getArrayByClassTypeAndCode(IconPack.class, code);
    }

    public BizPack getBizPackByCode(DynamicResourceWorkspace space,
                                    BasePack pack) throws Exception {
        if (pack == null)
            throw new ResourceNotFoundException("当前对象为空.");
        return (BizPack) getResourceSpace(space).getBizListPack()
                .getByClassTypeAndCode(BizPack.class, pack.getCode());
    }

    public String getPersonalBizCodes(DynamicResourceWorkspace space)
            throws Exception {
        return getResourceSpace(space).getPersonalListPack().getBiz();
    }

    public String getRecommendBizCodes(DynamicResourceWorkspace space)
            throws Exception {
        return getResourceSpace(space).getRecommendBizListPack().getBiz();
    }

    public String getDisplayAdCodes(DynamicResourceWorkspace space)
            throws Exception {
        // 直接显示所有的广告内容
        return getResourceSpace(space).getBizListPack().getAdsCodes();
    }

    public String getDisplayBizCodes(DynamicResourceWorkspace space)
            throws Exception {
        // 显示所有的配置的可见的资源包
        return getResourceSpace(space).getBizListPack().getDisplayBizCodes();
    }

    public Category getRootCategories(DynamicResourceWorkspace space,
                                      List<? extends IconPack> displayBizIconPacks) throws Exception {
        Set<String> categoryCodeSet = new HashSet<String>();
        for (IconPack iconPack : displayBizIconPacks) {
            categoryCodeSet.addAll(iconPack.getSearchIndex().getSearchCodeSet(
                    BizSearchIndex.SEARCH_INDEX_KEY_CATEGORY));
        }
        return getResourceSpace(space).getCategoryListPack().getRootCategory(
                categoryCodeSet);
    }

    public Category getCategoryByCusCode(DynamicResourceWorkspace space,
                                         String cusCode) throws Exception {
        CategoryListPack pack = getResourceSpace(space).getCategoryListPack();
        if (pack == null)
            return null;
        return pack.searchByCusCode(cusCode);
    }

    public Area getArea(DynamicResourceWorkspace space) throws Exception {
        return getResourceSpace(space).getAreaListPack().getArea();
    }

    public Area getWholeArea(DynamicResourceWorkspace space) throws Exception {
        return getResourceSpace(space).getAreaListPack().getWholeArea();
    }

    public List<Area> getHotAreas(DynamicResourceWorkspace space)
            throws Exception {
        return getResourceSpace(space).getAreaListPack().getHotAreas();
    }

    public Area getAreaByName(DynamicResourceWorkspace space, String areaName)
            throws Exception {
        return getResourceSpace(space).getAreaListPack()
                .getAreaByName(areaName);
    }

    public Area getAreaByLocation(DynamicResourceWorkspace space,
                                  String province, String city, String district) throws Exception {
        return getResourceSpace(space).getAreaListPack().getAreaByLocation(
                province, city, district);
    }

    public Area getAreaByCode(DynamicResourceWorkspace space, String areaCode)
            throws Exception {
        return getResourceSpace(space).getAreaListPack()
                .getAreaByCode(areaCode);
    }

    public ClientUpdateList getClientUpdateList(DynamicResourceWorkspace space)
            throws Exception {
        return getResourceSpace(space).getClientUpdateListPack()
                .getClientUpdateList();
    }

    /**
     * 得到api等级
     */
    public ConfPack getConfPack(DynamicResourceWorkspace space,
                                Class<? extends ConfPack> clazz, String code) throws Exception {
        return (ConfPack) getResourceSpace(space).getBizListPack()
                .getArrayByClassTypeAndCode(clazz, code).get(0);
    }

    @SuppressWarnings("unchecked")
    public List<AppPack> getAppPacks(DynamicResourceWorkspace space)
            throws Exception {
        return (List<AppPack>) getResourceSpace(space).getBizListPack()
                .getArrayByClassType(AppPack.class);
    }

    /**
     * 得到要展示的icon包
     */
    public List<StdIconPack> getShowStdIconPacks(
            DynamicResourceWorkspace space, String bizCodes) throws Exception {
        List<StdIconPack> iconPacks = new ArrayList<StdIconPack>();
        Set<String> bizCodeSet = UmsConvertUtil.convertString2SortSet(bizCodes);
        if (getResourceSpace(space).getBizListPack().getBasePacks() == null) {
            return null;
        }
        for (String code : bizCodeSet) {
            for (BasePack pack : getResourceSpace(space).getBizListPack()
                    .getBasePacks()) {
                if (pack == null || !(pack instanceof StdIconPack)) {
                    continue;
                } else if (!pack.getCode().equals(code)) {
                    continue;
                } else if (!((StdIconPack) pack).isShow()) { // 判断pack是否需要显示
                    continue;
                }
                iconPacks.add((StdIconPack) pack);
            }
        }
        return iconPacks;
    }

    /**
     * 得到要展示的icon包
     */
    public List<LargeIconPack> getShowLargeIconPacks(
            DynamicResourceWorkspace space, String bizCodes) throws Exception {
        List<LargeIconPack> iconPacks = new ArrayList<LargeIconPack>();
        Set<String> bizCodeSet = UmsConvertUtil.convertString2SortSet(bizCodes);
        if (getResourceSpace(space).getBizListPack().getBasePacks() == null) {
            return null;
        }
        for (String code : bizCodeSet) {
            for (BasePack pack : getResourceSpace(space).getBizListPack()
                    .getBasePacks()) {
                if (pack == null || !(pack instanceof LargeIconPack)) {
                    continue;
                } else if (!pack.getCode().equals(code)) {
                    continue;
                } else if (!((LargeIconPack) pack).isShow()) { // 判断pack是否需要显示
                    continue;
                }
                iconPacks.add((LargeIconPack) pack);
            }
        }
        return iconPacks;
    }

    public Map<String, List<AdsPack>> getShowAdsPacks(
            DynamicResourceWorkspace space, String adsCodes) throws Exception {
        Map<String, List<AdsPack>> map = new HashMap<String, List<AdsPack>>();
        Set<String> adsCodeSet = UmsConvertUtil.convertString2SortSet(adsCodes);
        if (getResourceSpace(space).getBizListPack().getBasePacks() == null) {
            return null;
        }
        for (String code : adsCodeSet) {
            for (BasePack pack : getResourceSpace(space).getBizListPack()
                    .getBasePacks()) {
                if (pack == null || !(pack instanceof AdsPack)) {
                    continue;
                } else if (!pack.getCode().equals(code)) {
                    continue;
                } else if (!map.containsKey(((AdsPack) pack).getGroup())) {
                    map.put(((AdsPack) pack).getGroup(),
                            new ArrayList<AdsPack>());
                }
                map.get(((AdsPack) pack).getGroup()).add((AdsPack) pack);
            }
        }
        for (Map.Entry<String, List<AdsPack>> entry : map.entrySet()) {
            Collections.sort(entry.getValue(), new Comparator<AdsPack>() {
                @Override
                public int compare(AdsPack lhs, AdsPack rhs) {
                    if (lhs.getOrder() == null)
                        return -1;
                    if (rhs.getOrder() == null)
                        return 1;
                    int diff = lhs.getOrder() - rhs.getOrder();
                    if (diff > 0)
                        return 1;
                    if (diff < 0)
                        return -1;
                    else
                        return 0;
                }
            });
        }
        return map;
    }

    private List<BasePack> getDependent(BizListPack bizListPack, BasePack pack)
            throws Exception {
        try {
            UmsLog.i("获得编码为[" + pack != null ? pack.getCode() : "" + "]的依赖.");
            if (pack == null)
                throw new ResourceNotFoundException("当前对象为空.");
            Class<?>[] checkDependentClasses = pack.getDependentClasses();
            if (checkDependentClasses == null
                    || checkDependentClasses.length <= 0) {
                UmsLog.i("没有依赖的对象.");
                return null;
            }
            List<BasePack> packs = new ArrayList<BasePack>();
            for (BasePack basePack : bizListPack.getBasePacks()) {
                for (Class<?> clazz : checkDependentClasses) {
                    if (clazz == null)
                        continue;
                    if (!clazz.isInstance(basePack))
                        continue;
                    if (basePack.getCode().equals(pack.getCode())
                            && basePack.getClass().toString()
                            .equals(pack.getClass().toString()))
                        continue;
                    UmsLog.d("编码为[" + pack.getCode() + "]的业务依赖于编码为["
                            + basePack.getCode() + "]的业务.");
                    packs.add(basePack);
                }
            }
            return packs;
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateBizList(DynamicResourceWorkspace space,
                              ResourceManagerListener listener) throws Exception {
        update(getResourceSpace(space).getBizListPack(), listener);
    }

    public void updateCategoryList(DynamicResourceWorkspace space,
                                   ResourceManagerListener listener) throws Exception {
        update(getResourceSpace(space).getCategoryListPack(), listener);
    }

    public void updateAreaList(DynamicResourceWorkspace space,
                               ResourceManagerListener listener) throws Exception {
        update(getResourceSpace(space).getAreaListPack(), listener);
    }

    public void updateClientUpdateList(DynamicResourceWorkspace space,
                                       ResourceManagerListener listener) throws Exception {
        update(getResourceSpace(space).getClientUpdateListPack(), listener);
    }

    public void updatePersonalList(DynamicResourceWorkspace space,
                                   ResourceManagerCheckUserInvalidListener listener) throws Exception {
        update(getResourceSpace(space).getPersonalListPack(), listener);
    }

    public void updateRecommendBizList(DynamicResourceWorkspace space,
                                       ResourceManagerCheckUserInvalidListener listener) throws Exception {
        update(getResourceSpace(space).getRecommendBizListPack(), listener);
    }

    public void updateDisplayBizList(DynamicResourceWorkspace space,
                                     ResourceManagerCheckUserInvalidListener listener) throws Exception {
        update(getResourceSpace(space).getDisplayBizListPack(), listener);
    }

    public void updatePersonalListByHistory(DynamicResourceWorkspace space,
                                            ResourceManagerListener listener) throws Exception {
        getResourceSpace(space).getPersonalListPack().prepareByHistory(null,
                listener);
    }

    public void updateRecommendBizListByHistor(DynamicResourceWorkspace space,
                                               ResourceManagerListener listener) throws Exception {
        getResourceSpace(space).getRecommendBizListPack().prepareByHistory(
                null, listener);
    }

    public void updateDisplayBizListByHistor(DynamicResourceWorkspace space,
                                             ResourceManagerListener listener) throws Exception {
        getResourceSpace(space).getDisplayBizListPack().prepareByHistory(null,
                listener);
    }

    /**
     * 这个函数最终调用了PACK的prepare
     */
    private void update(Resource pack, ResourceManagerListener listener)
            throws Exception {
        try {
            UmsLog.i("对要加载的业务进行操作.");
            if (listener == null)
                throw new ResourceManagerListenerUndefinedException(
                        "更新业务时的监听未定义.");
            if (pack == null)
                throw new ResourceNotFoundException("要准备的业务未定义.");
            UmsLog.i("异步开始更新.");
            processor.process(pack, listener);
        } catch (Exception e) {
            listener.onError(pack, false, "对要展示的业务批量更新时失败.", e);
        }
    }

    /**
     * 这个函数最终调用了PACK的prepare
     */
    public void prepare(DynamicResourceWorkspace space, String code,
                        ResourceManagerMultiListener listener) throws Exception {
        try {
            UmsLog.i("对要加载的业务进行操作." + code);
            if (UmsStringUtils.isBlank(code))
                throw new ResourceNotFoundException("要加载业务的编码不存在.");
            List<BasePack> packs = getPacksByCode(space, code);
            if (packs == null || packs.isEmpty())
                throw new ResourceNotFoundException("要加载业务的编码不存在.");
            for (BasePack basePack : packs) {
                if (basePack instanceof ShowPack)
                    continue;
                prepare(space, basePack, listener);
                return;
            }
        } catch (Exception e) {
            listener.sendUnableProcessError("对要展示的业务批量更新时失败.", e);
        }
    }

    /**
     * 这个函数最终调用了PACK的refresh
     */
    public void prepare(DynamicResourceWorkspace space, BasePack pack,
                        ResourceManagerMultiListener listener) throws Exception {
        try {
            UmsLog.i("对要加载的业务进行操作.");
            if (listener == null)
                throw new ResourceManagerListenerUndefinedException(
                        "更新业务时的监听未定义.");
            if (pack == null)
                throw new ResourceNotFoundException("要准备的业务未定义.");
            List<BasePack> packs = getPacksByCode(space, pack.getCode());
            if (packs == null || packs.isEmpty())
                throw new ResourceNotFoundException("要加载业务的编码不存在.");
            BasePack bizPack = null;
            for (BasePack basePack : packs) {
                if (basePack instanceof ShowPack)
                    continue;
                bizPack = basePack;
                break;
            }
            if (bizPack == null)
                throw new ResourceNotFoundException("要准备的业务未定义.");
            List<BasePack> basePacks = getDependent(getResourceSpace(space)
                    .getBizListPack(), bizPack);
            if (basePacks != null) {
                UmsLog.i("要更新的业务个数为[" + basePacks.size() + "].");
                listener.doInit(basePacks.size() + 1);
            } else {
                listener.doInit(1);
            }
            UmsLog.i("异步开始更新.");
            processor.process(bizPack, listener);
            if (basePacks == null)
                return;
            for (BasePack basePack : basePacks) {
                processor.process(basePack, listener);
            }
        } catch (Exception e) {
            listener.sendUnableProcessError("对要展示的业务批量更新时失败.", e);
        }
    }

    /**
     * 这个函数最终调用了PACK的refresh
     */
    public void refresh(DynamicResourceWorkspace space,
                        ResourceManagerMultiListener listener,
                        List<? extends ShowPack> showPacks, boolean needStdIcon,
                        boolean needLargeIcon, boolean needAds, boolean useBackup,
                        boolean needPublic) throws Exception {
        String bizCodes = doConvertShowPackList2Codes(showPacks);
        refresh(space, listener, bizCodes, needStdIcon, needLargeIcon, needAds,
                useBackup, needPublic);
    }

    /**
     * 这个函数最终调用了PACK的refresh
     */
    public void refresh(DynamicResourceWorkspace space,
                        ResourceManagerMultiListener listener, String bizCodes,
                        boolean needStdIcon, boolean needLargeIcon, boolean needAds,
                        boolean useBackup, boolean needPublic) throws Exception {
        try {
            UmsLog.i("对要显示的业务进行批量更新操作.");
            Map<String, Integer> map = UmsConvertUtil
                    .convertString2Map(bizCodes);
            if (listener == null)
                throw new ResourceManagerListenerUndefinedException(
                        "更新业务时的监听未定义.");
            UmsLog.d("要更新的业务个数为["
                    + getResourceSpace(space).getBizListPack().getBasePacks()
                    .size() + "].");
            listener.doInit(getResourceSpace(space).getBizListPack()
                    .getBasePacks().size());
            UmsLog.i("异步开始更新.");
            for (BasePack pack : getResourceSpace(space).getBizListPack()
                    .getBasePacks()) {
                processor.refresh(pack, listener, useBackup, needStdIcon,
                        needLargeIcon, needAds, needPublic,
                        map.containsKey(pack.getCode()));
            }
        } catch (Exception e) {
            listener.sendUnableProcessError("对要展示的业务批量更新时失败.", e);
        }
    }

    /**
     * 这个函数最终调用了PACK的refresh
     */
    public void refreshCategories(DynamicResourceWorkspace space,
                                  ResourceManagerMultiListener listener, List<Category> categories,
                                  boolean useBackup) throws Exception {
        try {
            UmsLog.i("对要显示的分类图片进行批量更新操作.");
            if (listener == null)
                throw new ResourceManagerListenerUndefinedException(
                        "分类图片更新时的监听未定义.");
            List<CategoryIconPack> showPacks = getResourceSpace(space)
                    .getCategoryListPack().getCategoryIconPacks(categories);
            UmsLog.d("要更新的分类图片个数为[" + showPacks.size() + "].");
            listener.doInit(showPacks.size());
            UmsLog.i("异步开始更新.");
            for (CategoryIconPack pack : showPacks) {
                processor.refresh(pack, listener, useBackup);
            }
        } catch (Exception e) {
            listener.sendUnableProcessError("对要展示的分类图片更新时失败.", e);
        }
    }

    /**
     * 所有的图片资源的PACK排排队
     */
    private String doConvertShowPackList2Codes(
            List<? extends ShowPack> showPacks) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (showPacks == null)
            return sb.toString();
        for (ShowPack pack : showPacks) {
            sb.append(pack.getCode()).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 检查依赖是否都在监控范围内
     */
    public boolean checkDependent(DynamicResourceWorkspace space, BasePack pack)
            throws Exception {
        try {
            UmsLog.i("对编码为[" + pack != null ? pack.getCode() : ""
                    + "]的业务进行依赖校验.");
            List<BasePack> basePacks = getDependent(getResourceSpace(space)
                    .getBizListPack(), pack);
            if (basePacks == null || basePacks.isEmpty()) {
                UmsLog.i("没有依赖的对象.");
                return true;
            }
            for (BasePack basePack : basePacks) {
                UmsLog.d("编码为[" + pack.getCode() + "]的业务依赖于编码为["
                        + basePack.getCode() + "]的业务.");
                if (basePack.check()) {
                    UmsLog.d("校验成功.");
                } else {
                    UmsLog.d("校验失败.");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    public final ResourceMonitorService getResourceMonitorService() {
        return resourceMonitorService;
    }

    private void initResourceMonitorService(final Context context) {
        UmsLog.i("进行文件监控初始化操作.");
        Intent intent = new Intent(context, ResourceMonitorService.class);
        context.bindService(intent, new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                resourceMonitorService.removeFileMonitor();
                resourceMonitorService = null;
            }

            @Override
            public void onServiceConnected(ComponentName arg0, IBinder service) {
                resourceMonitorService = ((ResourceMonitorService.MyBinder) service)
                        .getService();
                resourceMonitorService.startFileMonitor();
                initResource(context);
            }
        }, Context.BIND_AUTO_CREATE);
        UmsLog.i("完成文件监控初始化.");
    }

    private void initResource(Context context) {
        UmsLog.i("进行初始化操作.");
        try {
            ResourceManagerMultiListener listener = new ResourceManagerMultiListener() {

                @Override
                public void onProgress(Resource pack, String msg, int progress) {

                }

                @Override
                public void onUnableProcessError(String errorInfo,
                                                 Exception e) {
                    UmsLog.e("===============根据历史资源包初始化异常终止", e);
                }

                @Override
                public void onTotalProgress(int progress) {

                }

                @Override
                public void onFinish() {
                    UmsLog.d("============================================根据历史资源包初始化完成.");
                    UmsLog.d(
                            "初始化总数[{}],成功初始化个数[{}],不可或略错误初始化个数[{}],可忽略的错误初始化个数[{}]",
                            getCount(), getSuccessCount(), getErrorCount(),
                            getIgnoreResouceErrorCount());
                    UmsLog.d("====================errorMap");
                    printErrorResult(getErrorMap());
                    UmsLog.d("====================igerrorMap");
                    printErrorResult(getIgnoreErrorMap());
                    UmsLog.d("==================================================");
                    UmsEventBusUtils
                            .post(new DynamicResourceWorkspaceChangedEvent());
                }
            };
            listener.doInit(4);
            initBizListResource(context, listener);
            initCategoryResource(context, listener);
            initAreaResource(context, listener);
            initClientUpdateResource(context, listener);
        } catch (Exception e) {
            UmsLog.e("根据历史资源初始化失败.", e);
        }
    }

    /**
     * 这个函数最终调用了PACK的prepareByHistory
     */
    private void initBizListResource(final Context context,
                                     final ResourceManagerMultiListener listener) {
        try {
            UmsLog.i("进行全业务列表初始化操作.");
            processor.processListWithHistory(getResourceWorkspace()
                    .getBizListPack(), context, new ResourceManagerListener() {

                @Override
                public void onUpdated(Resource pack) {
                    initBasePackResource(context,
                            new ResourceProcessListener() {

                                @Override
                                public void onUpdated() {
                                    listener.onUpdated(getResourceWorkspace()
                                            .getBizListPack());
                                }

                                @Override
                                public void onError(String errorInfo,
                                                    Exception e) {
                                    listener.onError(getResourceWorkspace()
                                                    .getBizListPack(), true, errorInfo,
                                            e);
                                }
                            });

                }

                @Override
                public void onProgress(Resource pack, String msg, int progress) {

                }

                @Override
                public void onError(Resource pack, boolean isIgnoreResouce,
                                    String errorInfo, Exception e) {
                    listener.onError(getResourceWorkspace().getBizListPack(),
                            true, errorInfo, e);
                }
            });
        } catch (Exception e) {
            listener.onError(getResourceWorkspace().getBizListPack(), true,
                    e.getMessage(), e);
        }
    }

    /**
     * 这个函数最终调用了PACK的prepareByHistory
     */
    private void initCategoryResource(final Context context,
                                      final ResourceManagerMultiListener listener) {
        try {
            UmsLog.i("进行全业务列表初始化操作.");
            processor.processListWithHistory(getResourceWorkspace()
                            .getCategoryListPack(), context,
                    new ResourceManagerListener() {

                        @Override
                        public void onUpdated(Resource pack) {
                            initCategoryIconPackResource(context,
                                    new ResourceProcessListener() {

                                        @Override
                                        public void onUpdated() {
                                            listener.onUpdated(getResourceWorkspace()
                                                    .getCategoryListPack());
                                        }

                                        @Override
                                        public void onError(String errorInfo,
                                                            Exception e) {
                                            listener.onError(
                                                    getResourceWorkspace()
                                                            .getCategoryListPack(),
                                                    true, errorInfo, e);

                                        }
                                    });

                        }

                        @Override
                        public void onProgress(Resource pack, String msg,
                                               int progress) {

                        }

                        @Override
                        public void onError(Resource pack,
                                            boolean isIgnoreResouce, String errorInfo,
                                            Exception e) {
                            listener.onError(getResourceWorkspace()
                                    .getCategoryListPack(), true, errorInfo, e);
                        }
                    });
        } catch (Exception e) {
            listener.onError(getResourceWorkspace().getCategoryListPack(),
                    true, e.getMessage(), e);
        }
    }

    /**
     * 这个函数最终调用了PACK的prepareByHistory
     */
    private void initAreaResource(Context context,
                                  final ResourceManagerMultiListener listener) {
        try {
            UmsLog.i("进行区域列表初始化操作.");
            processor.processListWithHistory(getResourceWorkspace()
                    .getAreaListPack(), context, new ResourceManagerListener() {

                @Override
                public void onUpdated(Resource pack) {
                    listener.onUpdated(pack);

                }

                @Override
                public void onProgress(Resource pack, String msg, int progress) {

                }

                @Override
                public void onError(Resource pack, boolean isIgnoreResouce,
                                    String errorInfo, Exception e) {
                    listener.onError(getResourceWorkspace().getAreaListPack(),
                            true, errorInfo, e);
                }
            });
        } catch (Exception e) {
            listener.onError(getResourceWorkspace().getAreaListPack(), true,
                    e.getMessage(), e);
        }
    }

    /**
     * 这个函数最终调用了PACK的prepareByHistory
     */
    private void initClientUpdateResource(Context context,
                                          final ResourceManagerMultiListener listener) {
        try {
            UmsLog.i("进行应用升级列表初始化操作.");
            processor.processListWithHistory(getResourceWorkspace()
                            .getClientUpdateListPack(), context,
                    new ResourceManagerListener() {

                        @Override
                        public void onUpdated(Resource pack) {
                            listener.onUpdated(pack);
                        }

                        @Override
                        public void onProgress(Resource pack, String msg,
                                               int progress) {

                        }

                        @Override
                        public void onError(Resource pack,
                                            boolean isIgnoreResouce, String errorInfo,
                                            Exception e) {
                            listener.onError(getResourceWorkspace()
                                            .getClientUpdateListPack(), true,
                                    errorInfo, e);
                        }
                    });
        } catch (Exception e) {
            listener.onError(getResourceWorkspace().getClientUpdateListPack(),
                    true, e.getMessage(), e);
        }
    }

    /**
     * 这个函数最终调用了PACK的prepareByHistory
     */
    private void initCategoryIconPackResource(Context context,
                                              final ResourceProcessListener listener) {
        try {
            List<CategoryIconPack> categoryIconPacks = getResourceWorkspace()
                    .getCategoryListPack().getAllCategoryIconPacks();
            if (categoryIconPacks == null || categoryIconPacks.isEmpty()) {
                listener.onUpdated();
                return;
            }
            ResourceManagerMultiListener categoryIconMultiListener = new ResourceManagerMultiListener() {

                @Override
                public void onProgress(Resource pack, String msg, int progress) {

                }

                @Override
                public void onUnableProcessError(String errorInfo,
                                                 Exception e) {
                    UmsLog.e(
                            "**************************************初始化分类icon异常终止",
                            e);
                    listener.onError(errorInfo, e);
                }

                @Override
                public void onTotalProgress(int progress) {

                }

                @Override
                public void onFinish() {
                    UmsLog.d("**************************************初始化分类icon完成");
                    UmsLog.d(
                            "初始化总数[{}],成功初始化个数[{}],不可或略错误初始化个数[{}],可忽略的错误初始化个数[{}]",
                            getCount(), getSuccessCount(), getErrorCount(),
                            getIgnoreResouceErrorCount());
                    UmsLog.d("*********************errorMap");
                    printErrorResult(getErrorMap());
                    UmsLog.d("*********************igerrorMap");
                    printErrorResult(getIgnoreErrorMap());
                    UmsLog.d("**************************************");
                    listener.onUpdated();
                }
            };
            categoryIconMultiListener.doInit(categoryIconPacks.size());
            for (CategoryIconPack iconPack : categoryIconPacks) {
                processor.processListWithHistory(iconPack, context,
                        categoryIconMultiListener);
            }
        } catch (Exception e) {
            listener.onError(e.getMessage(), e);
        }
    }

    /**
     * 这个函数最终调用了PACK的prepareByHistory
     */
    private void initBasePackResource(Context context,
                                      final ResourceProcessListener listener) {
        try {
            // 获取全资源列表中的所有的资源包和各种图标的BasePack
            List<BasePack> basePacks = getResourceWorkspace().getBizListPack()
                    .getBasePacks();
            ResourceManagerMultiListener basePackMultiListener = new ResourceManagerMultiListener() {

                @Override
                public void onProgress(Resource pack, String msg, int progress) {

                }

                @Override
                public void onUnableProcessError(String errorInfo,
                                                 Exception e) {
                    UmsLog.e("===============初始化业务资源包异常终止", e);
                    listener.onError(errorInfo, e);
                }

                @Override
                public void onTotalProgress(int progress) {

                }

                @Override
                public void onFinish() {
                    UmsLog.d("--------------------------------------初始化业务资源包完成");
                    UmsLog.d(
                            "初始化总数[{}],成功初始化个数[{}],不可或略错误初始化个数[{}],可忽略的错误初始化个数[{}]",
                            getCount(), getSuccessCount(), getErrorCount(),
                            getIgnoreResouceErrorCount());
                    UmsLog.d("--------------------errorMap");
                    printErrorResult(getErrorMap());
                    UmsLog.d("--------------------igerrorMap");
                    printErrorResult(getIgnoreErrorMap());
                    UmsLog.d("--------------------------------------");
                    listener.onUpdated();
                }
            };
            basePackMultiListener.doInit(basePacks.size());
            for (BasePack basePack : basePacks) {
                processor.processListWithHistory(basePack, context,
                        basePackMultiListener);
            }
        } catch (Exception e) {
            listener.onError(e.getMessage(), e);
        }
    }

    /**
     * 错误日志打印
     */
    private void printErrorResult(Map<String, ErrorResult> map) {
        if (map == null)
            return;
        for (Entry<String, ErrorResult> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                UmsLog.e("errkey:[{}],errInfo:[{}]", entry.getKey(),
                        entry.getValue().errorInfo);
            } else {
                UmsLog.e("errkey:[{}]", entry.getKey());
            }
        }
    }

}
