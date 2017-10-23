package com.chinaums.opensdk.download.listener;

import android.content.Context;
import android.os.Handler;

import com.chinaums.opensdk.exception.SessionMacKeyNotReachableException;
import com.chinaums.opensdk.exception.UserInvalidException;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.util.UmsLog;

import java.util.Map;
import java.util.Map.Entry;

public abstract class ResourceManagerCheckUserInvalidMultiListener extends
        ResourceManagerMultiListener implements
        ResourceManagerCheckUserInvalidListener {

    /**
     * myContext
     */
    private Context myContext = null;

    /**
     * myHandler
     */
    private Handler myHandler = null;

    /**
     * userInvalidAction
     */
    private Runnable userInvalidAction = null;

    public ResourceManagerCheckUserInvalidMultiListener(Context context,
                                                        Handler handler, Runnable userInvalidAction) {
        this.myContext = context;
        this.myHandler = handler;
        this.userInvalidAction = userInvalidAction;
    }

    @Override
    public void onUnableProcessError(String errorInfo, Exception e) {
        if (e == null
                || !((e instanceof UserInvalidException) || (e instanceof SessionMacKeyNotReachableException))) {
            onFinalError(errorInfo, e);
        } else {
            onUserInvalid(e);
        }
    }

    @Override
    public void onUserInvalid(Exception e) {
        beforeProcessUserInvalid();
        if (notNeedDefaultProcessUserInvalid()) {
            return;
        }
        if (myHandler != null && userInvalidAction != null) {
            myHandler.post(userInvalidAction);
            return;
        } else if (myHandler != null) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        OpenDelegateManager.getProcessDelegate()
                                .processUserInvalid(myContext);
                    } catch (Exception e2) {
                        UmsLog.e("", e2);
                    }
                }
            });
            return;
        } else {
            try {
                OpenDelegateManager.getProcessDelegate().processUserInvalid(
                        myContext);
            } catch (Exception e2) {
                UmsLog.e("", e2);
            }
        }
    }

    @Override
    public void onFinish() {
        if (getErrorCount().get() > 0) {
            for (Entry<String, ErrorResult> entry : getErrorMap().entrySet()) {
                if (entry.getValue() != null
                        && (entry.getValue().exception instanceof UserInvalidException || entry
                        .getValue().exception instanceof SessionMacKeyNotReachableException)) {
                    onUserInvalid(entry.getValue().exception);
                    return;
                }
            }
            onError(getCount().get(), getErrorCount().get(), getErrorMap());
        } else {
            onSuccess(getCount().get(), getSuccessCount().get(),
                    getIgnoreResouceErrorCount().get());
        }
    }

    protected abstract void beforeProcessUserInvalid();

    protected abstract boolean notNeedDefaultProcessUserInvalid();

    protected abstract void onSuccess(int count, int successCount,
                                      int ignoreErrorCount);

    protected abstract void onError(int count, int errorCount,
                                    Map<String, ErrorResult> errorMap);

    protected abstract void onFinalError(String errorInfo, Exception e);

}
