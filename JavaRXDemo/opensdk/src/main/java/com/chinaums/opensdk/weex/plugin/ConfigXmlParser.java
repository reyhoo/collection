package com.chinaums.opensdk.weex.plugin;

import android.content.Context;
import android.text.TextUtils;
import android.util.Xml;

import com.chinaums.opensdk.util.UmsLog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ConfigXmlParser {

    private boolean mInsideFeature = false;
    private boolean mOnLoad = false;
    private String mName = "";
    private String mPluginClass = "";
    private String mParamType = "";
    private String mCategory = Constants.CATEGORY_MODULE;

    private HashMap<String, PluginEntry> mComponents = new HashMap<>(20);
    private HashMap<String, PluginEntry> mModules = new HashMap<>(20);


    public HashMap<String, PluginEntry> getPluginModules() {
        return mModules;
    }

    public HashMap<String, PluginEntry> getPluginComponents() {
        return mComponents;
    }

    /**
     * parse the applet_plugin_config.xml found in your app's assets to getCompassInfo the plugin info.
     */
    public synchronized void parse(Context context) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            InputStream io = getAssetsStream(context, "applet_plugin_config.xml");
            parser.setInput(io, "utf-8");
            parse(parser);
        } catch (Exception e) {
            UmsLog.i("parse applet_plugin_config.xml exception!");
        }
    }

    /**
     * parse the applet_plugin_config.xml found to get the plugin info.
     */
    public synchronized void parse(Context context, String filename) {
        try {
            if (context == null || filename == null) {
                return;
            }
            File file = new File(filename);
            if (!file.exists()) {
                return;
            }
            FileInputStream inputStream = new FileInputStream(file);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "utf-8");
            parse(parser);
        } catch (Exception e) {
            UmsLog.i("parse applet_plugin_config.xml exception!");
        }
    }

    /**
     * 得到Assets里面相应的文件流
     *
     * @param fileName
     * @return
     */
    private InputStream getAssetsStream(Context context, String fileName) {
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    private void parse(XmlPullParser xml) {
        int eventType = -1;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                handleStartTag(xml);
            } else if (eventType == XmlPullParser.END_TAG) {
                handleEndTag(xml);
            }
            try {
                eventType = xml.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleStartTag(XmlPullParser xml) {
        String strNode = xml.getName();
        if (strNode.equals(Constants.TAG_FEATURE)) {
            mInsideFeature = true;
        } else if (mInsideFeature && strNode.equals(Constants.TAG_PARAM)) {
            mParamType = xml.getAttributeValue(null, Constants.ATTR_NAME);
            if (mParamType.equals(Constants.ATTR_ANDROID_PACKAGE))
                mPluginClass = xml.getAttributeValue(null, Constants.ATTR_VALUE);
            else if (mParamType.equals(Constants.ATTR_ONLOAD))
                mOnLoad = "true".equalsIgnoreCase(xml.getAttributeValue(null, Constants.ATTR_VALUE));
            else if (mParamType.equals(Constants.ATTR_CATEGORY))
                mCategory = xml.getAttributeValue(null, Constants.ATTR_VALUE);
            else if (mParamType.equals(Constants.ATTR_API))
                mName = xml.getAttributeValue(null, Constants.ATTR_VALUE);
        }
    }

    private void handleEndTag(XmlPullParser xml) {
        String strNode = xml.getName();
        if (strNode.equals(Constants.TAG_FEATURE)) {
            if (TextUtils.equals(Constants.CATEGORY_MODULE, mCategory)) {
                UmsLog.d(">>Bing: add module name: " + mName + ",class:" + mPluginClass);
                mModules.put(mName, new PluginEntry(mName, mPluginClass, mOnLoad, Constants.CATEGORY_MODULE));
            } else if (TextUtils.equals(Constants.CATEGORY_COMPONENT, mCategory)) {
                UmsLog.d(">>Bing: add component name: " + mName + ",class:" + mPluginClass);
                mComponents.put(mName, new PluginEntry(mName, mPluginClass, mOnLoad, Constants.CATEGORY_COMPONENT));
            }
            mPluginClass = "";
            mInsideFeature = false;
            mOnLoad = false;
            mCategory = Constants.CATEGORY_MODULE;
            mName = "";
            mParamType = "";
        }
    }

}
