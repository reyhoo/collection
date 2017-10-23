package com.chinaums.opensdk.load.process;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.cons.OpenConst.DynamicCallback;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.util.Base64Utils;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.InputStream;

/**
 * UploadFileProcessor 文件上传API
 */
public class UploadFileProcessor extends AbsStdDynamicProcessor {

    /**
     * context
     */
    private Context context;

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        try {
            context = model.getActivity();
            final UploadFileWebRequestModel requestModel = (UploadFileWebRequestModel) model
                    .getRequestModel();
            if (requestModel == null)
                throw new Exception("我去，怎么没有，肯定哪里错了.");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    uploadFile(model, requestModel);
                }
            }).start();
        } catch (Exception e) {
            apiRunExceptionCallBack(model, e);
        }
    }

    private void uploadFile(DynamicWebModel model,
                            UploadFileWebRequestModel requestModel) {
        try {
            File file = new File(
                    getAbsoluteImagePath(Uri.parse(requestModel.fileUri)));
            FileBody fileBody = new FileBody(file, requestModel.mimeType,
                    "utf-8");
            MultipartEntity me = new MultipartEntity();
            me.addPart("file", fileBody);
            HttpPost post = new HttpPost(requestModel.getServerUri());
            post.addHeader("charset", HTTP.UTF_8);
            post.setEntity(me);
            HttpClient client = new DefaultHttpClient();
            HttpResponse resp = client.execute(post);
            if (resp.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = resp.getEntity();
                int contentLength = (int) httpEntity.getContentLength();
                byte[] payload = new byte[0];
                if (contentLength > 0) {
                    payload = readStream(httpEntity.getContent(), contentLength);
                }
                JSONObject respObj = JSON.parseObject(new String(payload,
                        "UTF-8"));
                UploadFileResponse uploadFileResponse = new UploadFileResponse();
                uploadFileResponse
                        .setFileUrl(respObj.getString("fileHttpPath"));
                uploadFileResponse.setErrCode(respObj.getString("errCode"));
                uploadFileResponse.setErrInfo(respObj.getString("errInfo"));
                setRespAndCallWeb(model,
                        createSuccessResponse(JsonUtils
                                .convert2Json(uploadFileResponse)));
            } else {
                setRespAndCallWeb(
                        model,
                        createErrorResponse("网络数据出错",
                                DynamicCallback.CALLBACK_STATE_ERROR));
            }
        } catch (Exception e) {
            UmsLog.e("", e);
            setRespAndCallWeb(
                    model,
                    createErrorResponse("网络数据出错",
                            DynamicCallback.CALLBACK_STATE_ERROR));
        }
    }

    private byte[] readStream(InputStream is, int contentLength)
            throws Exception {
        byte[] result = new byte[contentLength];
        int read = 0, pos = 0;
        while (pos < contentLength) {
            int len = contentLength - pos;
            if (len > 1024)
                len = 1024;
            read = is.read(result, pos, len);
            if (read < 0)
                break;
            pos += read;
        }
        return result;
    }

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {

    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new UploadFileWebRequestModel(model.getRequestObj());
    }

    @Override
    public int getType() {
        return DynamicProcessorType.UPLOAD_FILE;
    }

    private class UploadFileWebRequestModel extends AbsWebRequestModel {

        /**
         * 操作事件
         */
        private String action;

        /**
         * 要上传文件在客户端中的地址，一般该地址是通过调用获取图形媒体或本地文件获得。
         */
        private String fileUri;

        /**
         * 要上传文件对应的报文请求uri地址
         */
        private String serverUri;

        /**
         * 发送请求报文时，放在报文体中用于标明文件内容的key。如果没填，默认为fileData。
         */
        private String fileKey;

        /**
         * 存储到远程服务器后的文件后缀，用于下载时显示。
         */
        private String fileType;

        /**
         * 存储到远程服务器后的文件名称，用于下载时显示；
         */
        private String fileName;

        /**
         * 存储到远程服务器后的mime类型，用于下载时填充到HTTP的Content-type中，方便表明类型，如果没有传，由服务器自动生成
         */
        private String mimeType;

        public UploadFileWebRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                JSONObject data = getRequest().getJSONObject("data");
                action = getRequest().getJSONObject("info").getString("action");
                fileUri = Base64Utils.decryptBase64(data.getString("fileUri"));
                serverUri = data.getString("serverUri");
                JSONObject options = data.getJSONObject("options");
                fileKey = options.getString("fileKey");
                fileType = options.getString("fileType");
                fileName = options.getString("fileName");
                mimeType = options.getString("mimeType");
                if (UmsStringUtils.isBlank(fileUri)
                        || UmsStringUtils.isBlank(serverUri))
                    throw new Exception("API运行参数不全");
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }

        public String getAction() {
            return action;
        }

        public String getFileUri() {
            return fileUri;
        }

        public String getServerUri() {
            return serverUri;
        }

        public String getFileKey() {
            return fileKey;
        }

        public String getFileType() {
            return fileType;
        }

        public String getFileName() {
            return fileName;
        }

        public String getMimeType() {
            return mimeType;
        }

    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

    /**
     * 获取图片路径
     */
    public String getAbsoluteImagePath(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj,
                null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private class UploadFileResponse {

        /**
         * errCode为0000代表成功
         */
        private String errCode;

        /**
         * errInfo为errCode的描述
         */
        private String errInfo;

        /**
         * fileUrl是获取的url地址
         */
        private String fileUrl;

        public String getErrCode() {
            return errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public String getErrInfo() {
            return errInfo;
        }

        public void setErrInfo(String errInfo) {
            this.errInfo = errInfo;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

    }
}
