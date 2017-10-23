package com.chinaums.opensdk.download.process;

import com.chinaums.opensdk.cons.DynamicAsynPrepareFlag;
import com.chinaums.opensdk.cons.DynamicResourceWorkspace;
import com.chinaums.opensdk.cons.OpenConst.DynamicBizName;
import com.chinaums.opensdk.data.model.ResourceListHistory;
import com.chinaums.opensdk.download.listener.ResourceManagerCheckUserInvalidListener;
import com.chinaums.opensdk.download.listener.ResourceManagerCheckUserInvalidMultiListener;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.download.listener.ResourceManagerMultiListener;
import com.chinaums.opensdk.download.listener.ResourceNetProcessListener;
import com.chinaums.opensdk.download.model.AdsPack;
import com.chinaums.opensdk.download.model.AppPack;
import com.chinaums.opensdk.download.model.AreaListPack.Area;
import com.chinaums.opensdk.download.model.BasePack.BizSearchIndex;
import com.chinaums.opensdk.download.model.BizPack;
import com.chinaums.opensdk.download.model.CategoryListPack.Category;
import com.chinaums.opensdk.download.model.ClientUpdateListPack.ClientUpdateRange;
import com.chinaums.opensdk.download.model.ConfApiLevelPack;
import com.chinaums.opensdk.download.model.ConfApiPack;
import com.chinaums.opensdk.download.model.ConfNavigationListPack;
import com.chinaums.opensdk.download.model.IconPack;
import com.chinaums.opensdk.download.model.LargeIconPack;
import com.chinaums.opensdk.download.model.Resource;
import com.chinaums.opensdk.download.model.SharePack;
import com.chinaums.opensdk.download.model.ShowPack;
import com.chinaums.opensdk.download.model.StdIconPack;
import com.chinaums.opensdk.event.model.DynamicResourceAsynErrorEvent;
import com.chinaums.opensdk.event.model.DynamicResourceAsynSuccessEvent;
import com.chinaums.opensdk.event.model.DynamicResourceSynErrorEvent;
import com.chinaums.opensdk.event.model.DynamicResourceSynSuccessEvent;
import com.chinaums.opensdk.manager.OpenDelegateDefined;
import com.chinaums.opensdk.manager.OpenDelegateDefined.IUpdatePersonalBizListResponse;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.manager.OpenDynamicBizHistoryManager;
import com.chinaums.opensdk.util.UmsConvertUtil;
import com.chinaums.opensdk.util.UmsEventBusUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 这个类是提供给外部的调用接口
 */
public class DynamicResourceManager {

    /**
     * 单例
     */
    private static DynamicResourceManager instance = new DynamicResourceManager();

    /**
     * 是否异步准备好
     */
    private volatile DynamicAsynPrepareFlag flag = DynamicAsynPrepareFlag.Ready;

    /**
     * 得到处理类
     */
    public static DynamicResourceManager getInstance() {
        return instance;
    }

    /**
     * 进行异步处理 --下载并更新个性化列表 --下载并更新分类列表 --下载并更新地区列表 --下载并更新推荐列表
     */
    public void asynPrepare() throws Exception {
        if (flag.equals(DynamicAsynPrepareFlag.Runing)) {
            UmsLog.d("更新列表==========================================当前状态在运行，所以退出");
            return;
        }
        flag = DynamicAsynPrepareFlag.Runing;
        UmsLog.d("更新列表==========================================start");
        updateList(ResourceManager.getInstance().getResourceBackspace()
                        .getResourceWorkspace(),
                new ResourceManagerCheckUserInvalidMultiListener(null, null,
                        null) {

                    @Override
                    public void onProgress(Resource pack, String msg,
                                           int progress) {
                        UmsLog.d("更新列表--------进度---------->" + msg + "  "
                                + progress);
                    }

                    @Override
                    public void onTotalProgress(int progress) {
                        UmsLog.d("更新列表--------总进度---------->" + progress);
                    }

                    @Override
                    public void onSuccess(int count, int successCount,
                                          int ignoreErrorCount) {
                        try {
                            UmsLog.i("更新列表==========================================finish");
                            UmsLog.i("count=" + count);
                            UmsLog.i("success count=" + successCount);
                            UmsLog.i("ignoreErrorCount count="
                                    + ignoreErrorCount);
                            UmsLog.d("更新业务==========================================start");
                            refreshList(ResourceManager.getInstance()
                                            .getResourceBackspace()
                                            .getResourceWorkspace(),
                                    new ResourceManagerMultiListener() {

                                        @Override
                                        public void onProgress(Resource pack,
                                                               String msg, int progress) {
                                            UmsLog.d("更新业务--------进度---------->"
                                                    + msg + "  " + progress);
                                        }

                                        @Override
                                        public void onUnableProcessError(
                                                String errorInfo, Exception e) {
                                            flag = DynamicAsynPrepareFlag.Error;
                                            UmsLog.e(
                                                    "更新业务--------出现不可恢复错误---------->"
                                                            + errorInfo, e);
                                            UmsEventBusUtils
                                                    .post(new DynamicResourceAsynErrorEvent());
                                        }

                                        @Override
                                        public void onTotalProgress(
                                                int progress) {
                                            UmsLog.d("更新业务--------总进度---------->"
                                                    + progress);
                                        }

                                        @Override
                                        public void onFinish() {
                                            UmsLog.i("更新业务==========================================finish");
                                            UmsLog.i("count=" + this.getCount());
                                            UmsLog.i("success count="
                                                    + this.getSuccessCount());
                                            UmsLog.i("error count="
                                                    + this.getErrorCount());
                                            UmsLog.i("igerror count="
                                                    + this.getIgnoreResouceErrorCount());
                                            flag = DynamicAsynPrepareFlag.Success;
                                            // 发送异步更新完成通知
                                            UmsEventBusUtils
                                                    .post(new DynamicResourceAsynSuccessEvent());

                                        }
                                    });
                        } catch (Exception e) {
                            UmsLog.e("异步准备出错", e);
                        }
                    }

                    @Override
                    protected void onFinalError(String errorInfo, Exception e) {
                        UmsLog.i("更新列表出现不可恢复性错误=================================finish");
                        UmsLog.e(errorInfo, e);
                        flag = DynamicAsynPrepareFlag.Error;
                        UmsEventBusUtils
                                .post(new DynamicResourceAsynErrorEvent());
                    }

                    @Override
                    protected void onError(int count, int errorCount,
                                           Map<String, ErrorResult> errorMap) {
                        UmsLog.i("更新列表错误==========================================finish");
                        UmsLog.i("count=" + count);
                        UmsLog.i("errorCount count=" + errorCount);
                        flag = DynamicAsynPrepareFlag.Error;
                        UmsEventBusUtils
                                .post(new DynamicResourceAsynErrorEvent());
                    }

                    @Override
                    protected void beforeProcessUserInvalid() {
                        flag = DynamicAsynPrepareFlag.Error;
                    }

                    @Override
                    protected boolean notNeedDefaultProcessUserInvalid() {
                        return false;
                    }

                });
    }

    /**
     * 异步更新中途切换城市，在当前工作区间启动更新，下载期间弹loading对话框
     */
    public void synPrepare() throws Exception {
        UmsLog.d("更新列表==========================================start");
        updateList(ResourceManager.getInstance().getResourceBackspace()
                        .getResourceWorkspace(),
                new ResourceManagerCheckUserInvalidMultiListener(null, null,
                        null) {

                    @Override
                    public void onProgress(Resource pack, String msg,
                                           int progress) {
                        UmsLog.d("更新列表--------进度---------->" + msg + "  "
                                + progress);
                    }

                    @Override
                    public void onTotalProgress(int progress) {
                        UmsLog.d("更新列表--------总进度---------->" + progress);

                    }

                    @Override
                    protected void onSuccess(int count, int successCount,
                                             int ignoreErrorCount) {
                        try {
                            UmsLog.i("更新列表==========================================finish");
                            UmsLog.i("count=" + count);
                            UmsLog.i("success count=" + successCount);
                            UmsLog.i("ignoreErrorCount count="
                                    + ignoreErrorCount);
                            UmsLog.d("更新业务==========================================start");
                            refreshList(null,
                                    new ResourceManagerMultiListener() {

                                        @Override
                                        public void onProgress(Resource pack,
                                                               String msg, int progress) {
                                            UmsLog.d("更新业务--------进度---------->"
                                                    + msg + "  " + progress);

                                        }

                                        @Override
                                        public void onUnableProcessError(
                                                String errorInfo, Exception e) {
                                            UmsLog.e(
                                                    "更新业务--------出现不可恢复错误---------->"
                                                            + errorInfo, e);
                                            UmsEventBusUtils
                                                    .post(new DynamicResourceAsynErrorEvent());

                                        }

                                        @Override
                                        public void onTotalProgress(
                                                int progress) {
                                            UmsLog.d("更新业务--------总进度---------->"
                                                    + progress);

                                        }

                                        @Override
                                        public void onFinish() {
                                            UmsLog.i("更新业务==========================================finish");
                                            UmsLog.i("count=" + this.getCount());
                                            UmsLog.i("success count="
                                                    + this.getSuccessCount());
                                            UmsLog.i("error count="
                                                    + this.getErrorCount());
                                            UmsLog.i("igerror count="
                                                    + this.getIgnoreResouceErrorCount());
                                            // 发送异步更新完成通知
                                            UmsEventBusUtils
                                                    .post(new DynamicResourceSynSuccessEvent());

                                        }
                                    });
                        } catch (Exception e) {
                            UmsLog.e("同步准备出错", e);
                        }

                    }

                    @Override
                    protected void onFinalError(String errorInfo, Exception e) {
                        UmsLog.i("更新列表出现不可恢复性错误=================================finish");
                        UmsLog.e(errorInfo, e);
                        UmsEventBusUtils
                                .post(new DynamicResourceSynErrorEvent());

                    }

                    @Override
                    protected void onError(int count, int errorCount,
                                           Map<String, ErrorResult> errorMap) {
                        UmsLog.i("更新列表错误==========================================finish");
                        UmsLog.i("count=" + count);
                        UmsLog.i("errorCount count=" + errorCount);
                        UmsEventBusUtils
                                .post(new DynamicResourceSynErrorEvent());

                    }

                    @Override
                    protected void beforeProcessUserInvalid() {
                    }

                    @Override
                    protected boolean notNeedDefaultProcessUserInvalid() {
                        return false;
                    }

                });
    }

    /**
     * 进行异步处理
     */
    public synchronized void changeWorkspace() throws Exception {
        if (!flag.equals(DynamicAsynPrepareFlag.Success))
            return;
        ResourceManager.getInstance().changeCurWorkspace();
        flag = DynamicAsynPrepareFlag.Ready;
    }

    public final DynamicAsynPrepareFlag getFlag() {
        return flag;
    }

    public void updateList(DynamicResourceWorkspace space,
                           final ResourceManagerCheckUserInvalidMultiListener listener)
            throws Exception {
        try {
            OpenDelegateManager.getProcessDelegate().getDynamicUmsNet()
                    .updateList(space, listener);
        } catch (Exception e) {
            listener.sendUnableProcessError("对要展示的业务批量更新时失败.", e);
        }
    }

    public void updateListByHistory(DynamicResourceWorkspace space,
                                    final ResourceManagerMultiListener listener) throws Exception {
        try {
            OpenDelegateManager.getProcessDelegate().getDynamicUmsNet()
                    .updateListByHistory(space, listener);
        } catch (Exception e) {
            listener.sendUnableProcessError("对要展示的业务批量更新时失败.", e);
        }
    }

    public void updatePersonal(
            List<? extends ShowPack> list,
            final ResourceNetProcessListener<IUpdatePersonalBizListResponse> listener)
            throws Exception {
        Set<String> set = new LinkedHashSet<String>();
        if (list != null && !list.isEmpty()) {
            for (ShowPack pack : list) {
                set.add(pack.getCode());
            }
        }
        final String biz = UmsStringUtils.join(set.toArray(), ",");
        final String lastBiz = ResourceListHistory.getPersonalBizCodes();
        ResourceListHistory.setPersonalBizCodes(biz);
        ResourceNetProcessListener<IUpdatePersonalBizListResponse> tempListener = new ResourceNetProcessListener<OpenDelegateDefined.IUpdatePersonalBizListResponse>() {

            @Override
            public void onUpdated(IUpdatePersonalBizListResponse t) {
                if (t == null) {
                    onError("未获得个性化列表.", new Exception("未获得个性化列表"));
                } else {
                    listener.onUpdated(t);
                }
            }

            @Override
            public void onError(String errorInfo, Exception e) {
                ResourceListHistory.setPersonalBizCodes(lastBiz);
                listener.onError(errorInfo, e);
            }
        };
        OpenDelegateManager.getProcessDelegate().getDynamicUmsNet()
                .updatePersonalBizList(biz, tempListener);
    }

    public void refreshList(DynamicResourceWorkspace space,
                            final ResourceManagerMultiListener listener) throws Exception {
        try {
            OpenDelegateManager.getProcessDelegate().getDynamicUmsNet()
                    .refreshList(space, listener);
        } catch (Exception e) {
            listener.sendUnableProcessError("对要展示的业务批量更新时失败.", e);
        }
    }

    /**
     * 得到最后一次的个性化列表
     */
    public List<StdIconPack> getLastPersonalList() throws Exception {
        return ResourceManager.getInstance().getShowStdIconPacks(null,
                ResourceManager.getInstance().getPersonalBizCodes(null));
    }

    /**
     * 刷新最后一次的个性化列表
     */
    public void refreshLastPersonalList(ResourceManagerMultiListener listener)
            throws Exception {
        ResourceManager.getInstance().refresh(null, listener,
                ResourceManager.getInstance().getPersonalBizCodes(null), true,
                false, false, false, false);
    }

    /**
     * 刷新个性化列表
     */
    public void refreshPersonalList(ResourceManagerMultiListener listener,
                                    List<IconPack> personalListIconPacks) throws Exception {
        ResourceManager.getInstance().refresh(null, listener,
                personalListIconPacks, true, false, false, false, false);
    }

    /**
     * 刷新最后一次的个性化列表
     */
    public void updateAndRefreshPersonalList(List<? extends ShowPack> list,
                                             final ResourceManagerCheckUserInvalidMultiListener listener)
            throws Exception {
        updatePersonal(
                list,
                new ResourceNetProcessListener<OpenDelegateDefined.IUpdatePersonalBizListResponse>() {

                    @Override
                    public void onUpdated(IUpdatePersonalBizListResponse t) {
                        try {
                            updateAndRefreshPersonalList(listener);
                        } catch (Exception e) {
                            listener.sendUnableProcessError(e.getMessage(), e);
                        }
                    }

                    @Override
                    public void onError(String errorInfo, Exception e) {
                        listener.sendUnableProcessError(errorInfo, e);
                    }
                });
    }

    /**
     * 刷新最后一次的个性化列表
     */
    public void updateAndRefreshPersonalList(
            final ResourceManagerCheckUserInvalidMultiListener listener)
            throws Exception {
        ResourceManager.getInstance().updatePersonalList(null,
                new ResourceManagerCheckUserInvalidListener() {

                    @Override
                    public void onUpdated(Resource pack) {
                        try {
                            refreshLastPersonalList(listener);
                        } catch (Exception e) {
                            listener.sendUnableProcessError("获得个性化列表异常", e);
                        }
                    }

                    @Override
                    public void onProgress(Resource pack, String msg,
                                           int progress) {

                    }

                    @Override
                    public void onError(Resource pack, boolean isIgnoreResouce,
                                        String errorInfo, Exception e) {
                        listener.sendUnableProcessError(errorInfo, e);
                    }

                    @Override
                    public void onUserInvalid(Exception exception) {
                        listener.sendUnableProcessError(exception.getMessage(),
                                exception);
                    }
                });
    }

    /**
     * 得到最后一次的推荐列表
     */
    public List<LargeIconPack> getLastRecommendList() throws Exception {
        return ResourceManager.getInstance().getShowLargeIconPacks(null,
                ResourceManager.getInstance().getRecommendBizCodes(null));
    }

    /**
     * 刷新最后一次的推荐列表
     */
    public void refreshLastRecommendList(ResourceManagerMultiListener listener)
            throws Exception {
        ResourceManager.getInstance().refresh(null, listener,
                ResourceManager.getInstance().getRecommendBizCodes(null),
                false, true, false, false, false);
    }

    /**
     * 刷新推荐列表
     */
    public void refreshRecommendList(ResourceManagerMultiListener listener,
                                     List<LargeIconPack> recommendListIconPacks) throws Exception {
        ResourceManager.getInstance().refresh(null, listener,
                recommendListIconPacks, false, true, false, false, false);
    }

    /**
     * 刷新最后一次的推荐列表
     */
    public void updateAndRefreshRecommendList(
            final ResourceManagerCheckUserInvalidMultiListener listener)
            throws Exception {
        ResourceManager.getInstance().updateRecommendBizList(null,
                new ResourceManagerCheckUserInvalidListener() {

                    @Override
                    public void onUpdated(Resource pack) {
                        try {
                            refreshLastRecommendList(listener);
                        } catch (Exception e) {
                            listener.sendUnableProcessError("获得推荐列表异常", e);
                        }
                    }

                    @Override
                    public void onProgress(Resource pack, String msg,
                                           int progress) {

                    }

                    @Override
                    public void onError(Resource pack, boolean isIgnoreResouce,
                                        String errorInfo, Exception e) {
                        listener.sendUnableProcessError(errorInfo, e);
                    }

                    @Override
                    public void onUserInvalid(Exception exception) {
                        listener.sendUnableProcessError(exception.getMessage(),
                                exception);
                    }
                });
    }

    /**
     * 得到最后一次的广告列表
     */
    public Map<String, List<AdsPack>> getLastAdsList() throws Exception {
        return ResourceManager.getInstance().getShowAdsPacks(null,
                ResourceManager.getInstance().getDisplayAdCodes(null));
    }

    /**
     * 刷新广告列表
     */
    public void refreshLastAdsList(ResourceManagerMultiListener listener)
            throws Exception {
        ResourceManager.getInstance().refresh(null, listener,
                ResourceManager.getInstance().getDisplayAdCodes(null), false,
                false, true, false, false);
    }

    /**
     * 刷新广告列表
     */
    public void refreshAdsList(ResourceManagerMultiListener listener,
                               Map<String, List<AdsPack>> adsPacks) throws Exception {
        if (adsPacks == null)
            listener.onFinish();
        List<AdsPack> packs = new ArrayList<AdsPack>();
        for (Map.Entry<String, List<AdsPack>> entry : adsPacks.entrySet()) {
            packs.addAll(entry.getValue());
        }
        ResourceManager.getInstance().refresh(null, listener, packs, false,
                false, true, false, false);
    }

    /**
     * 刷新最后一次的个性化列表
     */
    public void updateAndRefreshAdsList(
            final ResourceManagerCheckUserInvalidMultiListener listener)
            throws Exception {
        ResourceManager.getInstance().updateDisplayBizList(null,
                new ResourceManagerCheckUserInvalidListener() {

                    @Override
                    public void onUpdated(Resource pack) {
                        try {
                            refreshLastAdsList(listener);
                        } catch (Exception e) {
                            listener.sendUnableProcessError("获得推荐列表异常", e);
                        }
                    }

                    @Override
                    public void onProgress(Resource pack, String msg,
                                           int progress) {

                    }

                    @Override
                    public void onError(Resource pack, boolean isIgnoreResouce,
                                        String errorInfo, Exception e) {
                        listener.sendUnableProcessError(errorInfo, e);
                    }

                    @Override
                    public void onUserInvalid(Exception exception) {
                        listener.sendUnableProcessError(exception.getMessage(),
                                exception);
                    }
                });
    }

    /**
     * 根据展示业务得到分类列表
     */
    public Category getRootCategories() throws Exception {
        String displayBiz = ResourceManager.getInstance().getDisplayBizCodes(
                null);
        return getRootCategories(displayBiz);
    }

    /**
     * 根据展示业务得到分类列表
     */
    public Category getRootCategories(String displayBiz) throws Exception {
        List<StdIconPack> displayBizIconPacks = ResourceManager.getInstance()
                .getShowStdIconPacks(null, displayBiz);
        return getRootCategories(displayBizIconPacks);
    }

    /**
     * 根据展示业务得到分类列表
     */
    public Category getRootCategories(
            List<? extends IconPack> displayBizIconPacks) throws Exception {
        return ResourceManager.getInstance().getRootCategories(null,
                displayBizIconPacks);
    }

    /**
     * 获取指定分类
     */
    public Category getCategoryByCusCode(String cusCode) throws Exception {
        return ResourceManager.getInstance()
                .getCategoryByCusCode(null, cusCode);
    }

    /**
     * 刷新分类列表的icon
     */
    public void refreshCategories(ResourceManagerMultiListener listener)
            throws Exception {
        Category categories = getRootCategories();
        refreshCategories(listener, categories.getChildren());
    }

    /**
     * 刷新分类列表的icon
     */
    public void refreshCategories(ResourceManagerMultiListener listener,
                                  List<Category> categories) throws Exception {
        ResourceManager.getInstance().refreshCategories(null, listener,
                categories, false);
    }

    /**
     * 更新分类列表并根据展示业务刷新分类列表icon
     */
    public void updateAndRefreshCategoriesAndDisplayBiz(
            final ResourceManagerCheckUserInvalidMultiListener listener)
            throws Exception {
        listener.doInit(1);
        ResourceManager.getInstance().updateDisplayBizList(null,
                new ResourceManagerCheckUserInvalidListener() {

                    @Override
                    public void onUpdated(Resource pack) {
                        try {
                            updateAndRefreshCategories(listener);
                        } catch (Exception e) {
                            listener.sendUnableProcessError("获得推荐列表异常", e);
                        }
                    }

                    @Override
                    public void onProgress(Resource pack, String msg,
                                           int progress) {

                    }

                    @Override
                    public void onError(Resource pack, boolean isIgnoreResouce,
                                        String errorInfo, Exception e) {
                        listener.sendUnableProcessError(errorInfo, e);
                    }

                    @Override
                    public void onUserInvalid(Exception exception) {
                        listener.sendUnableProcessError(exception.getMessage(),
                                exception);
                    }
                });
    }

    /**
     * 更新分类列表并根据展示业务刷新分类列表icon
     */
    public void updateAndRefreshCategories(ResourceManagerMultiListener listener)
            throws Exception {
        String displayBiz = ResourceManager.getInstance().getDisplayBizCodes(
                null);
        updateAndRefreshCategories(listener, displayBiz);
    }

    /**
     * 更新分类列表并根据展示业务刷新分类列表icon
     */
    public void updateAndRefreshCategories(
            ResourceManagerMultiListener listener, String displayBiz)
            throws Exception {
        List<StdIconPack> displayBizIconPacks = ResourceManager.getInstance()
                .getShowStdIconPacks(null, displayBiz);
        updateAndRefreshCategories(listener, displayBizIconPacks);
    }

    /**
     * 更新分类列表并根据展示业务刷新分类列表icon
     */
    public void updateAndRefreshCategories(
            final ResourceManagerMultiListener listener,
            final List<? extends IconPack> displayBizIconPacks)
            throws Exception {
        listener.doInit(1);
        ResourceManager.getInstance().updateCategoryList(null,
                new ResourceManagerListener() {

                    @Override
                    public void onUpdated(Resource pack) {
                        try {
                            Category categories = getRootCategories(displayBizIconPacks);
                            refreshCategories(listener,
                                    categories.getChildren());
                        } catch (Exception e) {
                            listener.sendUnableProcessError("获得分类列表异常", e);
                        }
                    }

                    @Override
                    public void onProgress(Resource pack, String msg,
                                           int progress) {

                    }

                    @Override
                    public void onError(Resource pack, boolean isIgnoreResource,
                                        String errorInfo, Exception e) {
                        listener.sendUnableProcessError(errorInfo, e);
                    }
                });
    }

    /**
     * 得到待展示列表
     */
    public List<? extends IconPack> getLastAllDisplayBizList() throws Exception {
        String displayBiz = ResourceManager.getInstance().getDisplayBizCodes(
                null);
        List<StdIconPack> displayBizIconPacks = ResourceManager.getInstance()
                .getShowStdIconPacks(null, displayBiz);
        return displayBizIconPacks;
    }

    /**
     * 刷新待展示列表
     */
    public void refreshLastAllDisplayBizList(
            ResourceManagerMultiListener listener) throws Exception {
        ResourceManager.getInstance().refresh(null, listener,
                ResourceManager.getInstance().getDisplayBizCodes(null), true,
                true, true, false, false);
    }

    /**
     * 刷新最后一次的待展示列表
     */
    public void updateAndRefreshDisplayBizList(
            final ResourceManagerCheckUserInvalidMultiListener listener)
            throws Exception {
        ResourceManager.getInstance().updateDisplayBizList(null,
                new ResourceManagerCheckUserInvalidListener() {

                    @Override
                    public void onUpdated(Resource pack) {
                        try {
                            refreshLastAllDisplayBizList(listener);
                        } catch (Exception e) {
                            listener.sendUnableProcessError("获得待展示列表异常", e);
                        }

                    }

                    @Override
                    public void onProgress(Resource pack, String msg,
                                           int progress) {

                    }

                    @Override
                    public void onError(Resource pack, boolean isIgnoreResouce,
                                        String errorInfo, Exception e) {
                        listener.sendUnableProcessError(errorInfo, e);
                    }

                    @Override
                    public void onUserInvalid(Exception exception) {
                        listener.sendUnableProcessError(exception.getMessage(),
                                exception);
                    }
                });
    }

    /**
     * 获取图标
     */
    public List<? extends IconPack> getIconPackByCategory(String categoryCode)
            throws Exception {
        String displayBiz = ResourceManager.getInstance().getDisplayBizCodes(
                null);
        return getIconPackByCategory(categoryCode, displayBiz);
    }

    /**
     * 获取图标
     */
    public List<? extends IconPack> getHistoryIconPackByCategory(
            String categoryCode) throws Exception {
        String displayBiz = OpenDynamicBizHistoryManager.getInstance()
                .getResourceListHistory().getDisplayBizCodes();
        return getIconPackByCategory(categoryCode, displayBiz);
    }

    /**
     * 获取图标
     */
    public List<? extends IconPack> getIconPackByCategory(String categoryCode,
                                                          String displayBiz) throws Exception {
        List<StdIconPack> displayBizIconPacks = ResourceManager.getInstance()
                .getShowStdIconPacks(null, displayBiz);
        return getIconPackByCategory(categoryCode, displayBizIconPacks);
    }

    /**
     * 获取图标
     */
    public List<? extends IconPack> getIconPackByCategory(String categoryCode,
                                                          List<? extends IconPack> displayBizIconPacks) throws Exception {
        List<IconPack> list = new ArrayList<IconPack>();
        if (displayBizIconPacks == null) {
            return list;
        }
        for (IconPack iconPack : displayBizIconPacks) {
            if (iconPack.getSearchIndex() != null
                    && iconPack.getSearchIndex().contains(
                    BizSearchIndex.SEARCH_INDEX_KEY_CATEGORY,
                    categoryCode)) {
                list.add(iconPack);
            }
        }
        return list;
    }

    /**
     * 得到某一个执行业务
     */
    public BizPack getBiz(String code) throws Exception {
        return ResourceManager.getInstance().getBizPackByCode(null, code);
    }

    /**
     * 得到某一个执行业务
     */
    public SharePack getShare(String code) throws Exception {
        return ResourceManager.getInstance().getSharePackByCode(null, code);
    }

    /**
     * 更新某一个执行业务及其依赖
     */
    public void prepareBiz(ResourceManagerMultiListener listener, String code)
            throws Exception {
        ResourceManager.getInstance().prepare(null, code, listener);
    }

    /**
     * 获取API级别
     */
    public int getProcessLevel(String process) throws Exception {
        return getProcessLevel(process, null);
    }

    /**
     * 获取API级别
     */
    public int getProcessLevel(String process, String action) throws Exception {
        String code = DynamicBizName.CONF_API_LEVEL;
        ConfApiLevelPack pack;
        pack = (ConfApiLevelPack) ResourceManager.getInstance().getConfPack(
                null, ConfApiLevelPack.class, code);
        return pack.getLevel(process, action);
    }

    /**
     * 判断是否是黑名单URL
     */
    public boolean isBlackUrl(String url) throws Exception {
        String code = DynamicBizName.CONF_NAVIGATION_LIST;
        ConfNavigationListPack pack;
        pack = (ConfNavigationListPack) ResourceManager.getInstance()
                .getConfPack(null, ConfNavigationListPack.class, code);
        return pack.isBlackUrl(url);
    }

    /**
     * 获取注入JS
     */
    public String getApiJs() throws Exception {
        String code = DynamicBizName.CONF_API;
        ConfApiPack pack = (ConfApiPack) ResourceManager.getInstance()
                .getConfPack(null, ConfApiPack.class, code);
        return pack.getApiJsStr();
    }

    /**
     * 得到区域列表
     */
    public Area getArea() throws Exception {
        return ResourceManager.getInstance().getArea(null);
    }

    /**
     * 整个地区信息
     */
    public Area getWholeArea() throws Exception {
        return ResourceManager.getInstance().getWholeArea(null);
    }

    /**
     * 获取热门地区信息
     */
    public List<Area> getHotAreas() throws Exception {
        return ResourceManager.getInstance().getHotAreas(null);
    }

    /**
     * 获取地区信息
     */
    public Area getAreaByName(String areaName) throws Exception {
        return ResourceManager.getInstance().getAreaByName(null, areaName);
    }

    /**
     * 获取地区信息
     */
    public Area getAreaByLocation(String province, String city, String district)
            throws Exception {
        return ResourceManager.getInstance().getAreaByLocation(null, province,
                city, district);
    }

    /**
     * 获取地区信息
     */
    public Area getAreaByCode(String areaCode) throws Exception {
        return ResourceManager.getInstance().getAreaByCode(null, areaCode);
    }

    /**
     * 更新区域列表,这个函数最终调用了PACK的prepare
     */
    public void updateArea(final ResourceManagerListener listener)
            throws Exception {
        ResourceManager.getInstance().updateAreaList(null, listener);
    }

    /**
     * 得到版本更新列表
     */
    public ClientUpdateRange getClientUpdateList() throws Exception {
        return ResourceManager.getInstance().getClientUpdateList(null)
                .getANDROIDPHONE();
    }

    /**
     * 更新版本更新列表,这个函数最终调用了PACK的prepare
     */
    public void updateClientUpdateList(final ResourceManagerListener listener)
            throws Exception {
        ResourceManager.getInstance().updateClientUpdateList(null, listener);
    }

    /**
     * 更新业务列表,这个函数最终调用了PACK的prepare
     */
    public void updateBizlist(DynamicResourceWorkspace space,
                              ResourceManagerListener listener) throws Exception {
        ResourceManager.getInstance().updateBizList(space, listener);
    }

    /**
     * 更新客户端更新列表
     */
    public void updateClientUpdateList(DynamicResourceWorkspace space,
                                       ResourceManagerListener listener) throws Exception {
        ResourceManager.getInstance().updateClientUpdateList(space, listener);
    }

    /**
     * 更新待展示列表,这个函数最终调用了PACK的prepare
     */
    public void updateDisplayBizList(DynamicResourceWorkspace space,
                                     ResourceManagerCheckUserInvalidMultiListener listener)
            throws Exception {
        ResourceManager.getInstance().updateDisplayBizList(space, listener);
    }

    /**
     * 更新区域列表,这个函数最终调用了PACK的prepare
     */
    public void updateAreaList(DynamicResourceWorkspace space,
                               ResourceManagerListener listener) throws Exception {
        ResourceManager.getInstance().updateAreaList(space, listener);
    }

    /**
     * 更新分类列表,这个函数最终调用了PACK的prepare
     */
    public void updateCategoryList(DynamicResourceWorkspace space,
                                   ResourceManagerListener listener) throws Exception {
        ResourceManager.getInstance().updateCategoryList(space, listener);
    }

    /**
     * 根据历史更新待展示列表,这个函数最终调用了PACK的prepareByHistory
     */
    public void updateDisplayBizListByHistory(DynamicResourceWorkspace space,
                                              ResourceManagerListener listener) throws Exception {
        ResourceManager.getInstance().updateDisplayBizListByHistor(space,
                listener);
    }

    /**
     * 刷新公共业务资源,这个函数最终调用了PACK的refresh
     */
    public void refreshPublicList(DynamicResourceWorkspace space,
                                  final ResourceManagerMultiListener listener) throws Exception {
        try {
            ResourceManager.getInstance().refresh(space, listener, "", false,
                    false, false, true, true);
        } catch (Exception e) {
            listener.sendUnableProcessError("对要展示的业务批量更新时失败.", e);
        }
    }

    /**
     * 对需要展示的业务批量更新,这个函数最终调用了PACK的refresh
     */
    public void refreshDisplayBizList(DynamicResourceWorkspace space,
                                      final ResourceManagerMultiListener listener) throws Exception {
        try {
            Set<String> displayBizSet = UmsConvertUtil
                    .convertString2SortSet(ResourceManager.getInstance()
                            .getDisplayBizCodes(space));
            Set<String> displayAdsSet = UmsConvertUtil
                    .convertString2SortSet(ResourceManager.getInstance()
                            .getDisplayAdCodes(space));
            displayBizSet.addAll(displayAdsSet);
            ResourceManager.getInstance().refresh(space, listener,
                    UmsConvertUtil.convertSet2String(displayBizSet), true,
                    true, true, true, true);
        } catch (Exception e) {
            listener.sendUnableProcessError("对要展示的业务批量更新时失败.", e);
        }
    }

    /**
     * 对需要展示的业务批量更新,这个函数最终调用了PACK的prepareByHistory
     */
    public void updateGeneralHistoryList(DynamicResourceWorkspace space,
                                         final ResourceManagerMultiListener listener) {
        try {
            ResourceManager.getInstance().updatePersonalListByHistory(space,
                    listener);
            ResourceManager.getInstance().updateRecommendBizListByHistor(space,
                    listener);
            ResourceManager.getInstance().updateDisplayBizListByHistor(space,
                    listener);
        } catch (Exception e) {
            listener.sendUnableProcessError("对要展示的业务批量更新时失败.", e);
        }
    }

    /**
     * 对需要展示的业务批量更新,这个函数最终调用了PACK的prepare
     */
    public void updateGeneralList(DynamicResourceWorkspace space,
                                  final ResourceManagerCheckUserInvalidMultiListener listener) {
        try {
            ResourceManager.getInstance().updateBizList(space, listener);
            ResourceManager.getInstance().updateCategoryList(space, listener);
            ResourceManager.getInstance().updateAreaList(space, listener);
            ResourceManager.getInstance().updateClientUpdateList(space,
                    listener);
            ResourceManager.getInstance().updatePersonalList(space, listener);
            ResourceManager.getInstance().updateRecommendBizList(space,
                    listener);
            ResourceManager.getInstance().updateDisplayBizList(space, listener);
        } catch (Exception e) {
            listener.sendUnableProcessError("对要展示的业务批量更新时失败.", e);
        }

    }

    /**
     * 对需要展示的业务批量刷新:这个函数最终调用了PACK的refresh
     */
    public void refreshGeneralList(DynamicResourceWorkspace space,
                                   final ResourceManagerMultiListener listener) {
        try {
            Set<String> personalSet = UmsConvertUtil
                    .convertString2SortSet(ResourceManager.getInstance()
                            .getPersonalBizCodes(space));
            Set<String> recommentSet = UmsConvertUtil
                    .convertString2SortSet(ResourceManager.getInstance()
                            .getRecommendBizCodes(space));
            Set<String> adsSet = UmsConvertUtil
                    .convertString2SortSet(ResourceManager.getInstance()
                            .getDisplayAdCodes(space));
            personalSet.addAll(recommentSet);
            personalSet.addAll(adsSet);
            ResourceManager.getInstance().refresh(space, listener,
                    UmsConvertUtil.convertSet2String(personalSet), true, true,
                    true, true, true);
        } catch (Exception e) {
            listener.sendUnableProcessError("对要展示的业务批量更新时失败.", e);
        }
    }

    /**
     * 获取当前工作空间下AppPack类型的业务
     */
    public List<AppPack> getAppPacks() throws Exception {
        return ResourceManager.getInstance().getAppPacks(null);
    }

}
