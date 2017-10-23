package com.chinaums.opensdk.download.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.chinaums.opensdk.cons.DynamicBizShowState;
import com.chinaums.opensdk.download.process.ResourceManager;
import com.chinaums.opensdk.download.listener.ResourceManagerListener;
import com.chinaums.opensdk.util.UmsFileUtils;


@SuppressWarnings({"serial"})
public abstract class IconPack extends ShowPack {

    /**
     * 是否展示
     */
    private boolean show;

    /**
     * @return
     */
    protected abstract Bitmap getDefaultBitMap();

    @Override
    public Bitmap getBitMap() {
        try {
            byte[] data = UmsFileUtils.readFile(getResOriginalPath(),
                    getResOriginalFileName());
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (Exception e) {
            return getDefaultBitMap();
        }
    }

    @Override
    public void onError(String msg, Exception e,
                        final ResourceManagerListener listener) throws Exception {
        log(Log.ERROR, msg, e);
        stopResourceMonitorWatch();
        UmsFileUtils.removeAllFile(getResOriginalPath());
        UmsFileUtils.removeAllFile(getResProcessPath());
        listener.onError(this, true, msg, e);
    }

    @Override
    protected boolean initPack() throws Exception {
        return true;
    }

    @Override
    public String getStatus() {
        BizPack pack;
        try {
            pack = ResourceManager.getInstance().getBizPackByCode(
                    getResourceWorkspace(), getCode());
            if (pack == null)
                return DynamicBizShowState.UNKNOWN.toString();
        } catch (Exception e) {
            return DynamicBizShowState.UNKNOWN.toString();
        }
        return pack.getStatus();
    }
    
    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

}
