package com.chinaums.opensdk.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.chinaums.opensdk.load.process.ActiveLocationProcessor;
import com.chinaums.opensdk.load.process.FastPayExtProcessor;
import com.chinaums.opensdk.load.process.GetEncryptPinProcessor;
import com.chinaums.opensdk.load.process.GetLocationProcessor;
import com.chinaums.opensdk.load.process.GetNetTypeProcessor;
import com.chinaums.opensdk.load.process.GetSystemInfoProcessor;
import com.chinaums.opensdk.load.process.GetUserInfoProcessor;
import com.chinaums.opensdk.load.process.GetXmsmkTokenProcessor;
import com.chinaums.opensdk.load.process.HistoryDataProcessor;
import com.chinaums.opensdk.load.process.IDynamicProcessor;
import com.chinaums.opensdk.load.process.NotificationAlertProcessor;
import com.chinaums.opensdk.load.process.PageBack3005Processor;
import com.chinaums.opensdk.load.process.PageBackProcessor;
import com.chinaums.opensdk.load.process.PageForwardProcessor;
import com.chinaums.opensdk.load.process.PageReturnPorcessor;
import com.chinaums.opensdk.load.process.PageShowShareViewProcessor;
import com.chinaums.opensdk.load.process.PopListProcessor;
import com.chinaums.opensdk.load.process.PrintLogProcessor;
import com.chinaums.opensdk.load.process.PublicSetResultProcessor;
import com.chinaums.opensdk.load.process.RegisterExtProcessorProcessor;
import com.chinaums.opensdk.load.process.ScanBarCodeProcessor;
import com.chinaums.opensdk.load.process.SetTitleProcessor;
import com.chinaums.opensdk.load.process.ShowPayCenterExtProcessor;
import com.chinaums.opensdk.load.process.SystemCopyPorcessor;
import com.chinaums.opensdk.load.process.SystemFileProcessor;
import com.chinaums.opensdk.load.process.SystemGetSoundStatePorcessor;
import com.chinaums.opensdk.load.process.SystemImageMediaProcessor;
import com.chinaums.opensdk.load.process.SystemOpenContactsProcessor;
import com.chinaums.opensdk.load.process.SystemOpenTelProcessor;
import com.chinaums.opensdk.load.process.SystemOpenWebPageProcessor;
import com.chinaums.opensdk.load.process.SystemRefreshWebViewProcessor;
import com.chinaums.opensdk.load.process.UmengEventProcessor;
import com.chinaums.opensdk.load.process.UploadFileProcessor;
import com.chinaums.opensdk.util.UmsLog;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

@SuppressLint("UseSparseArrays")
public class OpenDynamicWebProcessorManager implements IOpenManager {

    /**
     * 唯一实例
     */
    private static final OpenDynamicWebProcessorManager instance = new OpenDynamicWebProcessorManager();

    /**
     * 基本API
     */
    private final Map<Integer, IDynamicProcessor> processorMap = new ConcurrentHashMap<Integer, IDynamicProcessor>();

    public static OpenDynamicWebProcessorManager getInstance() {
        return instance;
    }

    @Override
    public void init(Context context) {
        processorMap.clear();
        ArrayList<IDynamicProcessor> processorList = new ArrayList<IDynamicProcessor>();
        processorList.add(new UploadFileProcessor());
        processorList.add(new GetEncryptPinProcessor());
        processorList.add(new GetLocationProcessor());
        processorList.add(new GetNetTypeProcessor());
        processorList.add(new GetSystemInfoProcessor());
        processorList.add(new GetUserInfoProcessor());
        processorList.add(new GetXmsmkTokenProcessor());
        processorList.add(new HistoryDataProcessor());
        processorList.add(new NotificationAlertProcessor());
        processorList.add(new PageBackProcessor());
        processorList.add(new PageForwardProcessor());
        processorList.add(new PageReturnPorcessor());
        processorList.add(new PopListProcessor());
        processorList.add(new PrintLogProcessor());
        processorList.add(new PublicSetResultProcessor());
        processorList.add(new ScanBarCodeProcessor());
        processorList.add(new SystemFileProcessor());
        processorList.add(new SystemImageMediaProcessor());
        processorList.add(new SystemOpenContactsProcessor());
        processorList.add(new SystemOpenTelProcessor());
        processorList.add(new SystemOpenWebPageProcessor());
        processorList.add(new RegisterExtProcessorProcessor());
        processorList.add(new PageBack3005Processor());
        processorList.add(new SetTitleProcessor());
        processorList.add(new SystemRefreshWebViewProcessor());
        processorList.add(new UmengEventProcessor());
        processorList.add(new PageShowShareViewProcessor());
        processorList.add(new SystemCopyPorcessor());
        processorList.add(new SystemGetSoundStatePorcessor());
        processorList.add(new ActiveLocationProcessor());
        //添加第三方调用支付方法（type3007）
        processorList.add(new ShowPayCenterExtProcessor());
        //添加调用POS通远程快捷支付方法（type3101）
        processorList.add(new FastPayExtProcessor());
        for (IDynamicProcessor processor : processorList) {
            processorMap.put(processor.getType(), processor);
        }
    }

    @Override
    public void destroy() {
        processorMap.clear();
    }

    public IDynamicProcessor getDynamicProcessor(int type) {
        if (processorMap.containsKey(type)) {
            return processorMap.get(type);
        }
        return null;
    }

    /**
     * 根据activity执行api的销毁方法
     */
    public void handleDestroyProcessorByActivity(Activity activity)
            throws Exception {
        for (Entry<Integer, IDynamicProcessor> entry : processorMap.entrySet()) {
            UmsLog.d("执行type为【{}】的扩展processor撤销方法", entry.getKey());
            IDynamicProcessor processor = entry.getValue();
            if (processor == null) {
                UmsLog.d("type为【{}】的扩展processor为空", entry.getKey());
                continue;
            }
            processor.onDestory(activity);
            UmsLog.d("type为【{}】的扩展processor的撤销方法", entry.getKey());
        }
    }

}
