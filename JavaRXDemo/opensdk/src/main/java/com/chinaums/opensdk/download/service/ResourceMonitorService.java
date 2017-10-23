package com.chinaums.opensdk.download.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.FileObserver;
import android.os.IBinder;

import com.chinaums.opensdk.util.UmsFileUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ResourceMonitorService extends Service {

    /**
     * binder
     */
    private final IBinder binder = new MyBinder();

    /**
     * mFileObserverList
     */
    private ConcurrentLinkedQueue<FileListener> mFileObserverList;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UmsLog.i("文件监控服务开始启动.");
        mFileObserverList = new ConcurrentLinkedQueue<ResourceMonitorService.FileListener>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UmsLog.i("文件监控服务开始停止.");
        removeFileMonitor();
        mFileObserverList = null;
    }

    public boolean removeFileMonitor() {
        try {
            UmsLog.d("移除所有挂载的文件监听开始.");
            stopFileMonitor();
            UmsLog.d("清空所有挂载的文件监听.");
            mFileObserverList.clear();
            UmsLog.d("移除所有挂载的文件监听完毕.");
            return true;
        } catch (Exception e) {
            UmsLog.e("移除所有挂载的文件监听失败", e);
        }
        return false;
    }

    /**
     * 在文件监控中查看路径是否存在，如果存在，则对其路径和子路径下的监控启用监听
     */
    public boolean checkHasFileMonitorAndStartWatching(String filePath) {
        try {
            UmsLog.d("查看文件路径是否存在监控并进行迭代启用开始. filePath:{}", filePath);
            filePath = formatFilePath(filePath);
            List<FileListener> list = getFileListenersByFilePath(filePath);
            if (list == null || list.isEmpty()) {
                UmsLog.d("查看文件路径是否存在监控并进行迭代启用结束，当前没有包含在监控中. filePath:{}", filePath);
                return false;
            }
            for (FileListener fileListener : list) {
                fileListener.startWatching();
            }
            UmsLog.d("查看文件路径是否存在监控并进行迭代启用成功结束. 启用个数:{} filePath:{}", list.size(), filePath);
            return true;
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return false;
    }

    public boolean startFileMonitor() {
        try {
            UmsLog.d("启动所有挂载的文件监听开始.");
            for (FileListener fileListener : mFileObserverList) {
                UmsLog.v("要启动的挂载文件监听在[{}]", fileListener.getMonitorFilePath());
                fileListener.startWatching();
            }
            UmsLog.d("启动所有挂载的文件监听完毕.");
            return true;
        } catch (Exception e) {
            UmsLog.e("启动所有挂载的文件监听失败", e);
        }
        return false;
    }

    public boolean stopFileMonitor() {
        try {
            UmsLog.d("停止所有挂载的文件监听开始.");
            for (FileListener fileListener : mFileObserverList) {
                UmsLog.v("要停止的挂载文件监听在[{}]", fileListener.getMonitorFilePath());
                fileListener.stopWatching();
            }
            UmsLog.d("停止所有挂载的文件监听完毕.");
            return true;
        } catch (Exception e) {
            UmsLog.e("停止所有挂载的文件监听失败", e);
        }
        return false;
    }

    /**
     * 启动文件监控，该文件监控会先尝试迭代移除原文件监控，并迭代添加新监控。
     */
    public boolean startFileMonitor(String filePath) {
        boolean isSuccess = false;
        try {
            filePath = formatFilePath(filePath);
            UmsLog.d("启动指定挂载的文件监听开始.路径为[{}]", filePath);
            // 先执行删除监控操作，因为担心有迭代文件没有加到监控中
            UmsLog.d("启动指定挂载的文件监听进行中,删除及停用操作开始.路径为[{}]", filePath);
            removeFileMonitor(filePath, true);
            UmsLog.d("启动指定挂载的文件监听进行中,删除及停用操作完成.路径为[{}]", filePath);
            addFileMonitor(filePath);
            UmsLog.d("启动指定挂载的文件监听完成.路径为[{}]。", filePath);
            return isSuccess;
        } catch (Exception e) {
            UmsLog.e("启动挂载的文件监听失败", e);
        }
        return isSuccess;
    }

    public boolean removeFileMonitor(String filePath) {
        return removeFileMonitor(filePath, false);
    }

    private boolean removeFileMonitor(String filePath,
                                      boolean isStartFileMonitor) {
        try {
            filePath = formatFilePath(filePath);
            if (!isStartFileMonitor) {
                UmsLog.d("迭代移除指定挂载的文件监听开始.路径为[{}]", filePath);
            }
            List<FileListener> list = getFileListenersByFilePath(filePath);
            for (FileListener listener : list) {
                listener.stopWatching();
                if (mFileObserverList.contains(listener)) {
                    mFileObserverList.remove(listener);
                }
            }
            if (!isStartFileMonitor) {
                UmsLog.d("迭代移除指定挂载的文件监听完毕.路径为[{}]", filePath);
            }
            return true;
        } catch (Exception e) {
            UmsLog.e("迭代移除指定挂载的文件监听失败", e);
        }
        return false;
    }

    public boolean stopFileMonitor(String filePath) {
        try {
            filePath = formatFilePath(filePath);
            UmsLog.d("停止指定挂载的文件监听开始.路径为[{}]", filePath);
            List<FileListener> list = getFileListenersByFilePath(filePath);
            for (FileListener listener : list) {
                listener.stopWatching();
            }
            UmsLog.d("停止指定挂载的文件监听完成.路径为[{}]", filePath);
            return true;
        } catch (Exception e) {
            UmsLog.e("停止指定挂载的文件监听失败", e);
        }
        return false;
    }

    private FileListener getFileListenerByFilePath(String dir) throws Exception {
        for (FileListener fileListener : mFileObserverList) {
            if (!fileListener.getMonitorFilePath().equals(dir))
                continue;
            return fileListener;
        }
        return null;
    }

    private List<FileListener> getFileListenersByFilePath(String dir)
            throws Exception {
        List<FileListener> fileListeners = new ArrayList<FileListener>();
        for (FileListener fileListener : mFileObserverList) {
            if (!fileListener.getMonitorFilePath().startsWith(dir))
                continue;
            fileListeners.add(fileListener);
        }
        return fileListeners;
    }

    /**
     * 添加监听并启动监听
     */
    private void addFileMonitor(String dir) {
        try {
            UmsLog.v("迭代启动指定挂载的文件监听监听开始.路径为[{}]", dir);
            File file = new File(dir);
            if (!file.exists() || !file.isDirectory()) {
                return;
            }
            dir = formatFilePath(dir);
            createFileListener(dir);
            File[] childFiles = file.listFiles();
            for (File childFile : childFiles) {
                if (!childFile.isDirectory()) {
                    addFileMonitor(childFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            UmsLog.e("迭代启动指定挂载的文件监听出错。", e);
        }
    }

    private FileListener createFileListener(String dir) {
        FileListener fileListener = null;
        try {
            UmsLog.v("创建指定挂载的文件监听监听开始.路径为[{}]", dir);
            fileListener = getFileListenerByFilePath(dir);
            if (fileListener == null) {
                fileListener = new FileListener(dir);
                mFileObserverList.add(fileListener);
            }
            fileListener.startWatching();
            UmsLog.v("创建指定挂载的文件监听监听完毕.路径为[{}]", dir);
            return fileListener;
        } catch (Exception e) {
            UmsLog.e("创建指定挂载的文件监听监听出错.路径为[{}]", e, dir);
        }
        return null;
    }

    /**
     * 对文件路径进行格式化，防止比对时，出现错误的情况
     */
    private String formatFilePath(String filePath) {
        if (UmsStringUtils.isBlank(filePath)) return filePath;
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        return filePath;
    }

    private class FileListener extends FileObserver {

        /**
         * mPath
         */
        private String mPath;

        public FileListener(String path, int mask) {
            super(path, mask);
            this.mPath = path;
        }

        public FileListener(String path) {
            super(path, FileObserver.CLOSE_WRITE | FileObserver.MOVE_SELF
                    | FileObserver.MOVED_FROM | FileObserver.MOVED_TO
                    | FileObserver.MODIFY | FileObserver.DELETE
                    | FileObserver.DELETE_SELF);
            this.mPath = path;
        }

        @Override
        public void onEvent(int event, String path) {
            final int action = event & FileObserver.ALL_EVENTS;
            switch (action) {
                case FileObserver.CLOSE_WRITE:
                case FileObserver.MOVE_SELF:
                case FileObserver.MOVED_FROM:
                case FileObserver.MOVED_TO:
                case FileObserver.MODIFY:
                case FileObserver.DELETE:
                case FileObserver.DELETE_SELF:
                    UmsLog.v("发现异常,路径为[{}]", this.mPath);
                    deleteFileAndRemoveWatching(path);
                    UmsLog.v("发现异常,完成停止监控、移除和删除文件操作.路径为[{}]", this.mPath);
            }
        }

        private void deleteFileAndRemoveWatching(String path) {
            try {
                UmsLog.v("发现异常,删除文件并移除文件监控及文件开始,路径为[{}]", mPath);
                removeFileMonitor(mPath);
                UmsFileUtils.removeAllFile(getMonitorFilePath());
                UmsLog.v("发现异常,删除文件并移除文件监控及文件完毕,路径为[{}]", mPath);
            } catch (Exception e) {
                UmsLog.e("发现异常,删除文件并移除文件监控及文件出错,路径为[{}]", e, mPath);
            }
        }

        public String getMonitorFilePath() {
            return this.mPath;
        }

    }

    public class MyBinder extends Binder {
        public ResourceMonitorService getService() {
            return ResourceMonitorService.this;
        }
    }

}