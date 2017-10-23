package com.chinaums.opensdk.load.process;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.cons.OpenConst.DynamicCallback;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.util.Base64Utils;
import com.chinaums.opensdk.util.ExifHelper;
import com.chinaums.opensdk.util.FileHelper;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 类 SystemOpenCameraProcessor 的实现描述：设备相机调用及图库选择
 */
public class SystemImageMediaProcessor extends AbsStdDynamicProcessor implements
        MediaScannerConnectionClient {

    private static final String LOG_TAG = SystemImageMediaProcessor.class
            .getSimpleName();
    private static final int DATA_URL = 0; // Return base64 encoded string
    private static final int FILE_URI = 1; // Return file uri
    // (content://media/external/images/media/2
    // for Android)
    private static final int NATIVE_URI = 2; // On Android, this is the same as
    // FILE_URI

    private static final int PHOTOLIBRARY = 0; // Choose image from picture
    // library (same as
    // SAVEDPHOTOALBUM for Android)
    private static final int CAMERA = 1; // Take picture from camera
    private static final int SAVEDPHOTOALBUM = 2; // Choose image from picture
    // library (same as
    // PHOTOLIBRARY for Android)

    private static final int PICTURE = 0; // allow selection of still pictures
    // only. DEFAULT. Will return format
    // specified via DestinationType
    private static final int VIDEO = 1; // allow selection of video only, ONLY
    // RETURNS URL
    private static final int ALLMEDIA = 2; // allow selection from all media
    // types

    private static final int JPEG = 0; // Take a picture of type JPEG
    private static final int PNG = 1; // Take a picture of type PNG

    private static final String GET_PICTURE = "Get Picture";
    private static final String GET_VIDEO = "Get Video";
    private static final String GET_All = "Get All";

    private static final int CROP_CAMERA = 100;
    // 上下文
    private Activity mContext;
    /**
     * 返回数据的格式，0：base64编码字符串的图像数据，1:图像文件的uri。默认0
     */
    private int destType = FILE_URI;
    /**
     * 图片来源。0：可以选择全部文件夹下的内容，1：摄像头，2：只能选则相册里的内容
     */
    private int sourceType = CAMERA;
    /**
     * 存储图像的质量
     */
    private int mQuality = 50; // Compression quality hint (0-100: 0=low quality
    // & high compression, 100=compress of max
    // quality)
    /**
     * 以像素为单位的图像缩放宽度
     */
    private int targetWidth = 0;
    /**
     * 以像素为单位的图像缩放高度
     */
    private int targetHeight = 0;
    /**
     * 返回图像文件的编码方式。0为jpg，1为png。
     */
    private int encodingType = JPEG;
    /**
     * 设置可选取的文件类型。0：图片，1：视频，2：all。该属性需要sourceType不为1的情况才有效。
     */
    private int mediaType = PICTURE;

    private boolean saveToPhotoAlbum; // Should the picture be saved to the
    // device's photo album
    /**
     * 在选择图标进行操作之前允许进行简单编辑，boolean类型，默认false
     */
    private boolean allowEdit = false; // Should we allow the user to crop the
    // image.
    private Uri imageUri; // Uri of captured image
    private int numPics;

    private MediaScannerConnection conn; // Used to update gallery app with
    // newly-written files
    private Uri scanMe; // Uri of image to be added to content store
    private Uri croppedUri;

    private ImageMediaWebRequestModel requestModel;
    private DynamicWebModel model;

    @Override
    public int getType() {
        return DynamicProcessorType.SYSTEM_OPEN_CAMERA;
    }

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent intent)
            throws Exception {
        // Get src and dest types from request code
        this.requestModel = (ImageMediaWebRequestModel) model.getRequestModel();
        this.model = model;
        // if camera crop
        if (null != intent.getExtras()
                && CROP_CAMERA == intent.getExtras().getInt("requestCode")) { // cropIntent.putExtra("requestCode",
            // CROP_CAMERA);
            if (resultCode == Activity.RESULT_OK) {
                // get the returned data
                Bundle extras = intent.getExtras();
                // get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                if (thePic == null) {
                    this.failPicture("Crop returned no data.");
                    return;
                }

                // now save the bitmap to a file
                OutputStream fOut = null;
                File temp_file = new File(getTempDirectoryPath(),
                        System.currentTimeMillis() + ".jpg");
                try {
                    temp_file.createNewFile();
                    fOut = new FileOutputStream(temp_file);
                    thePic.compress(Bitmap.CompressFormat.JPEG, this.mQuality,
                            fOut);
                    fOut.flush();
                    fOut.close();
                } catch (FileNotFoundException e) {
                    UmsLog.e("", e);
                } catch (IOException e) {
                    UmsLog.e("", e);
                }
                successPicture(Uri.fromFile(temp_file).toString(), FILE_URI);

            }// If cancelled
            else if (resultCode == Activity.RESULT_CANCELED) {
                this.failPicture("Camera cancelled.");
            }

            // If something else
            else {
                this.failPicture("Did not complete!");
            }

        }

        // If CAMERA
        if (sourceType == CAMERA) {
            // If image available
            if (resultCode == Activity.RESULT_OK) {
                try {
                    this.processResultFromCamera(destType, intent);
                } catch (IOException e) {
                    UmsLog.e("", e);
                    this.failPicture("Error capturing image.");
                }
            }

            // If cancelled
            else if (resultCode == Activity.RESULT_CANCELED) {
                this.failPicture("Camera cancelled.");
            }
            // If something else
            else {
                this.failPicture("Did not complete!");
            }
        }
        // If retrieving photo from library
        else if ((sourceType == PHOTOLIBRARY)
                || (sourceType == SAVEDPHOTOALBUM)) {
            if (resultCode == Activity.RESULT_OK && intent != null) {
                this.processResultFromGallery(destType, intent);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                this.failPicture("Selection cancelled.");
            } else {
                this.failPicture("Selection did not complete!");
            }
        }
    }

    /*
     * 返回数据的格式 图像文件的uri base64编码字符串的图像数据
     */
    private void successPicture(String data, int type) {
        SystemImageMediaResponse resp = new SystemImageMediaResponse();
        if (type == DATA_URL) {
            resp.setImgData(data);
        } else {
            resp.setImgUri(Base64Utils.encrypt(data));
        }
        setRespAndCallWeb(model,
                createSuccessResponse(JsonUtils.convert2Json(resp)));
    }

    /**
     * Applies all needed transformation to the image received from the camera.
     *
     * @param destType In which form should we return the image
     * @param intent   An Intent, which can return result data to the caller (various
     *                 data can be attached to Intent "extras").
     */
    private void processResultFromCamera(int destType, Intent intent)
            throws IOException {
        int rotate = 0;
        // Create an ExifHelper to save the exif data that is lost during
        // compression
        ExifHelper exif = new ExifHelper();
        try {
            if (this.encodingType == JPEG) {
                exif.createInFile(getTempDirectoryPath() + "/.Pic.jpg");
                exif.readExifData();
                rotate = exif.getOrientation();
            }
        } catch (IOException e) {
            UmsLog.e("", e);
        }

        Bitmap bitmap = null;
        Uri uri = null;

        // If sending base64 image back
        if (destType == DATA_URL) {
            bitmap = getScaledBitmap(FileHelper.stripFileProtocol(imageUri
                    .toString()));
            if (bitmap == null) {
                // Try to get the bitmap from intent.
                bitmap = (Bitmap) intent.getExtras().get("data");
            }

            // Double-check the bitmap.
            if (bitmap == null) {
                Log.d(LOG_TAG, "I either have a null image path or bitmap");
                this.failPicture("Unable to create bitmap!");
                return;
            }

            this.processPicture(bitmap);
            checkForDuplicateImage(DATA_URL);
        }

        // If sending filename back
        else if (destType == FILE_URI || destType == NATIVE_URI) {
            if (this.saveToPhotoAlbum) {
                Uri inputUri = getUriFromMediaStore();
                try {
                    // Just because we have a media URI doesn't mean we have a
                    // real file, we need to make it
                    uri = Uri.fromFile(new File(FileHelper.getRealPath(
                            inputUri, this.mContext)));
                } catch (NullPointerException e) {
                    uri = null;
                }
            } else {
                uri = Uri.fromFile(new File(getTempDirectoryPath(), System
                        .currentTimeMillis() + ".jpg"));
            }

            if (uri == null) {
                this.failPicture("Error capturing image - no media storage found.");
                return;
            }

            // If all this is true we shouldn't compress the image.
            if (this.targetHeight == -1 && this.targetWidth == -1
                    && this.mQuality == 100) {
                writeUncompressedImage(uri);
                this.successPicture(uri.toString(), FILE_URI);
                // this.callbackContext.success(uri.toString());
            } else {
                bitmap = getScaledBitmap(FileHelper.stripFileProtocol(imageUri
                        .toString()));
                // Add compressed version of captured image to returned media
                OutputStream os = this.mContext.getContentResolver()
                        .openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, this.mQuality, os);
                os.close();

                // Restore exif data to file
                if (this.encodingType == JPEG) {
                    String exifPath;
                    if (this.saveToPhotoAlbum) {
                        exifPath = FileHelper.getRealPath(uri, this.mContext);
                    } else {
                        exifPath = uri.getPath();
                    }
                    exif.createOutFile(exifPath);
                    exif.writeExifData();
                }
                if (this.allowEdit) {
                    performCrop(uri);
                } else {
                    // Send Uri back to JavaScript for viewing image
                    this.successPicture(uri.toString(), FILE_URI);
                }
            }
        } else {
            throw new IllegalStateException();
        }

        this.cleanup(FILE_URI, this.imageUri, uri, bitmap);
        bitmap = null;
    }

    /**
     * Applies all needed transformation to the image received from the gallery.
     *
     * @param destType In which form should we return the image
     * @param intent   An Intent, which can return result data to the caller (various
     *                 data can be attached to Intent "extras").
     */
    private void processResultFromGallery(int destType, Intent intent) {
        Uri uri = intent.getData();
        if (uri == null) {
            if (croppedUri != null) {
                uri = croppedUri;
            } else {
                this.failPicture("null data from photo library");
                return;
            }
        }
        int rotate = 0;

        // If you ask for video or all media type you will automatically get
        // back a file URI
        // and there will be no attempt to resize any returned data
        if (this.mediaType != PICTURE) {
            this.successPicture(uri.toString(), FILE_URI);
            // this.callbackContext.success(uri.toString());
        } else {
            // This is a special case to just return the path as no scaling,
            // rotating, nor compressing needs to be done
            if (this.targetHeight == -1 && this.targetWidth == -1
                    && (destType == FILE_URI || destType == NATIVE_URI)) {
                this.successPicture(uri.toString(), FILE_URI);
                // this.callbackContext.success(uri.toString());
            } else {
                String uriString = uri.toString();
                // Get the path to the image. Makes loading so much easier.
                String mimeType = FileHelper.getMimeType(uriString,
                        this.mContext);
                // If we don't have a valid image so quit.
                if (!("image/jpeg".equalsIgnoreCase(mimeType) || "image/png"
                        .equalsIgnoreCase(mimeType))) {
                    Log.d(LOG_TAG, "I either have a null image path or bitmap");
                    this.failPicture("Unable to retrieve path to picture!");
                    return;
                }
                Bitmap bitmap = null;
                try {
                    bitmap = getScaledBitmap(uriString);
                } catch (IOException e) {
                    UmsLog.e("", e);
                }
                if (bitmap == null) {
                    Log.d(LOG_TAG, "I either have a null image path or bitmap");
                    this.failPicture("Unable to create bitmap!");
                    return;
                }
                // If sending base64 image back
                if (destType == DATA_URL) {
                    this.processPicture(bitmap);
                }

                // If sending filename back
                else if (destType == FILE_URI || destType == NATIVE_URI) {
                    // Did we modify the image?
                    if ((this.targetHeight > 0 && this.targetWidth > 0)) {
                        try {
                            String modifiedPath = this.ouputModifiedBitmap(
                                    bitmap, uri);
                            // The modified image is cached by the app in order
                            // to get around this and not have to delete you
                            // application cache I'm adding the current system
                            // time to the end of the file url.
                            this.successPicture("file://" + modifiedPath + "?"
                                    + System.currentTimeMillis(), FILE_URI);
                            // this.callbackContext.success("file://" +
                            // modifiedPath + "?" + System.currentTimeMillis());
                        } catch (Exception e) {
                            UmsLog.e("", e);
                            this.failPicture("Error retrieving image.");
                        }
                    } else {
                        this.successPicture(uri.toString(), FILE_URI);
                        // this.callbackContext.success(uri.toString());
                    }
                }
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
                System.gc();
            }
        }
    }

    /**
     * In the special case where the default width, height and quality are
     * unchanged we just write the file out to disk saving the expensive
     * Bitmap.compress function.
     *
     * @param uri
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void writeUncompressedImage(Uri uri) throws FileNotFoundException,
            IOException {
        FileInputStream fis = new FileInputStream(
                FileHelper.stripFileProtocol(imageUri.toString()));
        OutputStream os = this.mContext.getContentResolver().openOutputStream(
                uri);
        byte[] buffer = new byte[4096];
        int len;
        while ((len = fis.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.flush();
        os.close();
        fis.close();
    }

    /**
     * Create entry in media store for image
     *
     * @return uri
     */
    private Uri getUriFromMediaStore() {
        ContentValues values = new ContentValues();
        values.put(android.provider.MediaStore.Images.Media.MIME_TYPE,
                "image/jpeg");
        Uri uri;
        try {
            uri = this.mContext
                    .getContentResolver()
                    .insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values);
        } catch (RuntimeException e) {
            Log.d(LOG_TAG, "Can't write to external media storage.");
            try {
                uri = this.mContext
                        .getContentResolver()
                        .insert(android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                                values);
            } catch (RuntimeException ex) {
                Log.d(LOG_TAG, "Can't write to internal media storage.");
                return null;
            }
        }
        return uri;
    }

    /**
     * Brings up the UI to perform crop on passed image URI
     *
     * @param picUri
     */
    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate output X and Y
            if (targetWidth > 0) {
                cropIntent.putExtra("outputX", targetWidth);
            }
            if (targetHeight > 0) {
                cropIntent.putExtra("outputY", targetHeight);
            }
            if (targetHeight > 0 && targetWidth > 0
                    && targetWidth == targetHeight) {
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
            }
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra("requestCode", CROP_CAMERA);
            // start the activity - we handle returning in onActivityResult
            if (this.mContext != null) {
                this.mContext.startActivityForResult(cropIntent,
                        DynamicProcessorType.SYSTEM_OPEN_CAMERA);
            }
        } catch (ActivityNotFoundException anfe) {
            Log.e(LOG_TAG, "Crop operation not supported on this device");
            // Send Uri back to JavaScript for viewing image
            this.successPicture(picUri.toString(), FILE_URI);
            // this.callbackContext.success(picUri.toString());
        }
    }

    /**
     * Cleans up after picture taking. Checking for duplicates and that kind of
     * stuff.
     *
     * @param newImage
     */
    private void cleanup(int imageType, Uri oldImage, Uri newImage,
                         Bitmap bitmap) {
        if (bitmap != null) {
            bitmap.recycle();
        }
        // Clean up initial camera-written image file.
        (new File(FileHelper.stripFileProtocol(oldImage.toString()))).delete();

        checkForDuplicateImage(imageType);
        // Scan for the gallery to update pic refs in gallery
        if (this.saveToPhotoAlbum && newImage != null) {
            this.scanForGallery(newImage);
        }

        System.gc();
    }

    private void scanForGallery(Uri newImage) {
        this.scanMe = newImage;
        if (this.conn != null) {
            this.conn.disconnect();
        }
        this.conn = new MediaScannerConnection(
                this.mContext.getApplicationContext(), this);
        conn.connect();
    }

    private String ouputModifiedBitmap(Bitmap bitmap, Uri uri)
            throws IOException {
        // Create an ExifHelper to save the exif data that is lost during
        // compression
        String modifiedPath = getTempDirectoryPath() + "/modified.jpg";

        OutputStream os = new FileOutputStream(modifiedPath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, this.mQuality, os);
        os.close();

        // Some content: URIs do not map to file paths (e.g. picasa).
        String realPath = FileHelper.getRealPath(uri, this.mContext);
        ExifHelper exif = new ExifHelper();
        if (realPath != null && this.encodingType == JPEG) {
            try {
                exif.createInFile(realPath);
                exif.readExifData();
                exif.createOutFile(modifiedPath);
                exif.writeExifData();
            } catch (IOException e) {
                UmsLog.e("", e);
            }
        }
        return modifiedPath;
    }

    /**
     * Compress bitmap using jpeg, convert to Base64 encoded string, and return
     * to JavaScript.
     *
     * @param bitmap
     */
    public void processPicture(Bitmap bitmap) {
        ByteArrayOutputStream jpeg_data = new ByteArrayOutputStream();
        try {
            if (bitmap.compress(CompressFormat.JPEG, mQuality, jpeg_data)) {
                byte[] code = jpeg_data.toByteArray();
                byte[] output = Base64.encode(code, Base64.NO_WRAP);
                String js_out = new String(output);
                // this.callbackContext.success(js_out);
                this.successPicture(js_out, DATA_URL);
                js_out = null;
                output = null;
                code = null;
            }
        } catch (Exception e) {
            this.failPicture("Error compressing image.");
        }
        jpeg_data = null;
    }

    /**
     * Used to find out if we are in a situation where the Camera Intent adds to
     * images to the content store. If we are using a FILE_URI and the number of
     * images in the DB increases by 2 we have a duplicate, when using a
     * DATA_URL the number is 1.
     *
     * @param type FILE_URI or DATA_URL
     */
    private void checkForDuplicateImage(int type) {
        int diff = 1;
        Uri contentStore = whichContentStore();
        Cursor cursor = queryImgDB(contentStore);
        int currentNumOfImages = cursor.getCount();

        if (type == FILE_URI && this.saveToPhotoAlbum) {
            diff = 2;
        }

        // delete the duplicate file if the difference is 2 for file URI or 1
        // for Data URL
        if ((currentNumOfImages - numPics) == diff) {
            cursor.moveToLast();
            int id = Integer.valueOf(cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Media._ID)));
            if (diff == 2) {
                id--;
            }
            Uri uri = Uri.parse(contentStore + "/" + id);
            this.mContext.getContentResolver().delete(uri, null, null);
            cursor.close();
        }
    }

    /**
     * Return a scaled bitmap based on the target width and height
     *
     * @return
     * @throws IOException
     */
    private Bitmap getScaledBitmap(String imageUrl) throws IOException {
        // If no new width or height were specified return the original bitmap
        if (this.targetWidth <= 0 && this.targetHeight <= 0) {
            return BitmapFactory.decodeStream(FileHelper
                    .getInputStreamFromUriString(imageUrl, mContext));
        }

        // figure out the original width and height of the image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                FileHelper.getInputStreamFromUriString(imageUrl, mContext),
                null, options);

        // CB-2292: WTF? Why is the width null?
        if (options.outWidth == 0 || options.outHeight == 0) {
            return null;
        }

        // determine the correct aspect ratio
        int[] widthHeight = calculateAspectRatio(options.outWidth,
                options.outHeight);

        // Load in the smallest bitmap possible that is closest to the size we
        // want
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth,
                options.outHeight, this.targetWidth, this.targetHeight);
        Bitmap unscaledBitmap = BitmapFactory
                .decodeStream(FileHelper.getInputStreamFromUriString(imageUrl,
                        this.mContext), null, options);
        if (unscaledBitmap == null) {
            return null;
        }

        return Bitmap.createScaledBitmap(unscaledBitmap, widthHeight[0],
                widthHeight[1], true);
    }

    /**
     * Maintain the aspect ratio so the resulting image does not look smooshed
     *
     * @param origWidth
     * @param origHeight
     * @return
     */
    public int[] calculateAspectRatio(int origWidth, int origHeight) {
        int newWidth = this.targetWidth;
        int newHeight = this.targetHeight;

        // If no new width or height were specified return the original bitmap
        if (newWidth <= 0 && newHeight <= 0) {
            newWidth = origWidth;
            newHeight = origHeight;
        }
        // Only the width was specified
        else if (newWidth > 0 && newHeight <= 0) {
            newHeight = (newWidth * origHeight) / origWidth;
        }
        // only the height was specified
        else if (newWidth <= 0 && newHeight > 0) {
            newWidth = (newHeight * origWidth) / origHeight;
        }
        // If the user specified both a positive width and height
        // (potentially different aspect ratio) then the width or height is
        // scaled so that the image fits while maintaining aspect ratio.
        // Alternatively, the specified width and height could have been
        // kept and Bitmap.SCALE_TO_FIT specified when scaling, but this
        // would result in whitespace in the new image.
        else {
            double newRatio = newWidth / (double) newHeight;
            double origRatio = origWidth / (double) origHeight;

            if (origRatio > newRatio) {
                newHeight = (newWidth * origHeight) / origWidth;
            } else if (origRatio < newRatio) {
                newWidth = (newHeight * origWidth) / origHeight;
            }
        }

        int[] retval = new int[2];
        retval[0] = newWidth;
        retval[1] = newHeight;
        return retval;
    }

    /**
     * Figure out what ratio we can load our image into memory at while still
     * being bigger than our desired width and height
     *
     * @param srcWidth
     * @param srcHeight
     * @param dstWidth
     * @param dstHeight
     * @return
     */
    public static int calculateSampleSize(int srcWidth, int srcHeight,
                                          int dstWidth, int dstHeight) {
        final float srcAspect = (float) srcWidth / (float) srcHeight;
        final float dstAspect = (float) dstWidth / (float) dstHeight;

        if (srcAspect > dstAspect) {
            return srcWidth / dstWidth;
        } else {
            return srcHeight / dstHeight;
        }
    }

    /**
     * Send error message to JavaScript.
     *
     * @param err
     */
    public void failPicture(String err) {
        setRespAndCallWeb(model,
                createErrorResponse(err, DynamicCallback.CALLBACK_STATE_ERROR));
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        // TODO Auto-generated method stub
        return new ImageMediaWebRequestModel(model.getRequestObj());
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        model.getHandler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    mContext = model.getActivity();
                    // 正常处理
                    ImageMediaWebRequestModel requestModel = (ImageMediaWebRequestModel) model
                            .getRequestModel();
                    if (requestModel == null)
                        throw new Exception(
                                " ImageMediaWebRequestModel == null");
                    getImageMedia(model);
                } catch (Exception e) {
                    // 异常处理
                    apiRunExceptionCallBack(model, e);
                }
            }
        });
    }

    /**
     * 获得图像媒体文件
     *
     * @param model
     * @throws Exception
     */
    private void getImageMedia(DynamicWebModel model) {

        if (this.targetWidth < 1) {
            this.targetWidth = -1;
        }
        if (this.targetHeight < 1) {
            this.targetHeight = -1;
        }
        // 1：摄像头
        if (sourceType == CAMERA) {
            this.takePicture(destType, encodingType);
        } else if ((sourceType == PHOTOLIBRARY)
                || (sourceType == SAVEDPHOTOALBUM)) {
            this.getImage(sourceType, destType, encodingType);
        }
    }

    /**
     * Take a picture with the camera. When an image is captured or the camera
     * view is cancelled, the result is returned in
     * CordovaActivity.onActivityResult, which forwards the result to
     * this.onActivityResult.
     * <p>
     * The image can either be returned as a base64 string or a URI that points
     * to the file. To display base64 string in an img tag, set the source to:
     * img.src="data:image/jpeg;base64,"+result; or to display URI in an img tag
     * img.src=result;
     *
     * @param returnType Set the type of image to return.
     */
    public void takePicture(int returnType, int encodingType) {
        // Save the number of images currently on disk for later
        this.numPics = queryImgDB(whichContentStore()).getCount();

        // Display camera
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        // Specify file so that large image is captured and returned
        File photo = createCaptureFile(encodingType);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        this.imageUri = Uri.fromFile(photo);

        if (this.mContext != null) {
            this.mContext.startActivityForResult(intent,
                    DynamicProcessorType.SYSTEM_OPEN_CAMERA);
        }
        // else
        // LOG.d(LOG_TAG,
        // "ERROR: You must use the CordovaInterface for this to work correctly. Please implement it in your activity");
    }

    /**
     * Creates a cursor that can be used to determine how many images we have.
     *
     * @return a cursor
     */
    private Cursor queryImgDB(Uri contentStore) {
        return this.mContext.getContentResolver().query(contentStore,
                new String[]{MediaStore.Images.Media._ID}, null, null, null);
    }

    /**
     * Determine if we are storing the images in internal or external storage
     *
     * @return Uri
     */
    private Uri whichContentStore() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else {
            return android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        }
    }

    /**
     * Create a file in the applications temporary directory based upon the
     * supplied encoding.
     *
     * @param encodingType of the image to be taken
     * @return a File object pointing to the temporary picture
     */
    private File createCaptureFile(int encodingType) {
        File photo = null;
        if (encodingType == JPEG) {
            photo = new File(getTempDirectoryPath(), ".Pic.jpg");
        } else if (encodingType == PNG) {
            photo = new File(getTempDirectoryPath(), ".Pic.png");
        } else {
            throw new IllegalArgumentException("Invalid Encoding Type: "
                    + encodingType);
        }
        return photo;
    }

    // --------------------------------------------------------------------------
    // LOCAL METHODS
    // --------------------------------------------------------------------------
    private String getTempDirectoryPath() {
        File cache = null;
        // SD Card Mounted
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            cache = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + "/Android/data/"
                    + this.mContext.getPackageName() + "/cache/");
        }
        // Use internal storage
        else {
            cache = this.mContext.getCacheDir();
        }

        // Create the cache directory if it doesn't exist
        cache.mkdirs();
        return cache.getAbsolutePath();
    }

    /**
     * Get image from photo library.
     *
     * @param srcType      The album to get image from.
     * @param returnType   Set the type of image to return.
     * @param encodingType
     */
    public void getImage(int srcType, int returnType, int encodingType) {
        Intent intent = new Intent();
        String title = GET_PICTURE;
        croppedUri = null;
        if (this.mediaType == PICTURE) {
            intent.setType("image/*");
            if (this.allowEdit) {
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra("crop", "true");
                if (targetWidth > 0) {
                    intent.putExtra("outputX", targetWidth);
                }
                if (targetHeight > 0) {
                    intent.putExtra("outputY", targetHeight);
                }
                if (targetHeight > 0 && targetWidth > 0
                        && targetWidth == targetHeight) {
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                }
                File photo = createCaptureFile(encodingType);
                croppedUri = Uri.fromFile(photo);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                        croppedUri);
            } else {
//				intent.setAction(Intent.ACTION_GET_CONTENT);
//				intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
        } else if (this.mediaType == VIDEO) {
            intent.setType("video/*");
            title = GET_VIDEO;
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else if (this.mediaType == ALLMEDIA) {
            // I wanted to make the type 'image/*, video/*' but this does not
            // work on all versions
            // of android so I had to go with the wildcard search.
            intent.setType("*/*");
            title = GET_All;
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        if (this.mContext != null) {
            this.mContext.startActivityForResult(
                    Intent.createChooser(intent, new String(title)),
                    DynamicProcessorType.SYSTEM_OPEN_CAMERA);
        }
    }

    private class ImageMediaWebRequestModel extends AbsWebRequestModel {

        public ImageMediaWebRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                JSONObject optJSONObject = getRequest().getJSONObject("data");
                mQuality = optJSONObject.getIntValue("quality");
                destType = optJSONObject.getIntValue("destinationType");
                sourceType = optJSONObject.getIntValue("sourceType");
                allowEdit = optJSONObject.getBoolean("allowEdit");
                encodingType = optJSONObject.getIntValue("encodingType");
                targetWidth = optJSONObject.getIntValue("targetWidth");
                targetHeight = optJSONObject.getIntValue("targetHeight");
                mediaType = optJSONObject.getIntValue("mediaType");
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }
    }

    @Override
    public DynamicRequestType getProcessorType() {
        // TODO Auto-generated method stub
        return DynamicRequestType.ASYNCHRONIZED;
    }

    @Override
    public void onMediaScannerConnected() {
        // TODO Auto-generated method stub
        try {
            this.conn.scanFile(this.scanMe.toString(), "image/*");
        } catch (java.lang.IllegalStateException e) {
            Log.e(LOG_TAG,
                    "Can't scan file in MediaScanner after taking picture");
        }
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        // TODO Auto-generated method stub
        this.conn.disconnect();
    }

    private class SystemImageMediaResponse {

        /**
         * imgData是base64编码的图片内容
         */
        private String imgData;

        /**
         * imgUri是获取的uri地址
         */
        private String imgUri;

        public String getImgData() {
            return imgData;
        }

        public void setImgData(String imgData) {
            this.imgData = imgData;
        }

        public String getImgUri() {
            return imgUri;
        }

        public void setImgUri(String imgUri) {
            this.imgUri = imgUri;
        }

    }
}
