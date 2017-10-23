package com.chinaums.opensdk.weex.utils;

import android.os.Environment;

/**
 * @version 1.0
 * @description 常量
 */

public interface AppletConstant {

    interface Path {
        public String STORAGE = Environment.getExternalStorageDirectory().getPath();

        public String ROOT = STORAGE + "/chinaums/applet/";

        public String SOUND = ROOT + "sound/";

        public String SAVED = ROOT + "saved/";

        public String PHOTO = ROOT + "photo/";

        public String VEDIO = ROOT + "video/";
    }

    public interface Number {
        public float MAX_BRIGHTNESS = 100;

    }

    public interface Code {
        public int CAPTURE_IMAGE_RESULT = 100;

        public int CAPTURE_VIDEO_RESULT = CAPTURE_IMAGE_RESULT + 1;

        public int PICK_IMAGE_RESULT = CAPTURE_VIDEO_RESULT + 1;

        public int SELECTE_LINKMAN_RESULT = PICK_IMAGE_RESULT + 1;

        public int REQUEST_PERMISSION_RESULT = SELECTE_LINKMAN_RESULT + 1;
    }

    public interface Suffix {
        public String VEDIO = ".mov";

        public String AUDIO = ".3gp";

        public String JPG = ".jpg";
    }

    public interface Prefix {
        public String IMAGE = "img_";

        public String VEDIO = "vid_";
    }

    public interface Regex {
        /**
         * 判断是否适合法手机号码的正则表达式
         */
        public String PHONE_NUM = "^1\\d{10}$";
    }
}
