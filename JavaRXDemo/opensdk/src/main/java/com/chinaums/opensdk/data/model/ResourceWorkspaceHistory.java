package com.chinaums.opensdk.data.model;

import com.chinaums.opensdk.cons.DynamicResourceWorkspace;
import com.chinaums.opensdk.download.process.ResourceManager;
import com.chinaums.opensdk.manager.OpenHistoryDataManager;
import com.chinaums.opensdk.util.UmsLog;

import java.io.Serializable;

public class ResourceWorkspaceHistory implements Serializable {

    private static final long serialVersionUID = 5305927624357287118L;
    private static final String DEFAULT_VALUE_NULLS = "NULLS";
    private static final String DEFAULT_KEY = "activity_work_space";

    public static AbsResourceData getActivityWorkspace() {
        try {
            String ret = OpenHistoryDataManager.getHistoryStringData(DEFAULT_KEY,
                    DEFAULT_VALUE_NULLS);
            if (!DEFAULT_VALUE_NULLS.equals(ret))
                return ResourceManager.getInstance().getResourceSpace(ret);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return ResourceManager.getInstance().getResourceSpace(
                DynamicResourceWorkspace.SpaceA);
    }

    public static void setActivityWorkspace(DynamicResourceWorkspace workspace) {
        try {
            if (workspace == null) {
                workspace = DynamicResourceWorkspace.SpaceA;
            }
            OpenHistoryDataManager
                    .setHistoryData(DEFAULT_KEY, workspace.getValue());
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

}
