package com.chinaums.opensdk.weex.plugin;

import android.content.Context;
import android.util.Log;

import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;


public class PluginManager {

    private static HashMap<String, PluginEntry> sComponents = new HashMap<>();
    private static HashMap<String, PluginEntry> sModules = new HashMap<>();
    private static HashMap<String, PluginEntry> sExtComponents = new HashMap<>();
    private static HashMap<String, PluginEntry> sExtModules = new HashMap<>();

    public static void init(Context context) {
        loadDefaultConfig(context);
        File fromFile = new File("/sdcard/weextest/classes.jar");
        File toFile = new File("/data/data/com.weex.sample/classes.jar");
        copyfile(fromFile, toFile, true);
        //loadExtendConfig(context);
        registerComponents(sComponents);
        registerModules(sModules);
        try {
            File jarFile = new File("/data/data/com.weex.sample/classes.jar");
            if (jarFile.exists()) {
                // DexClassLoader cl = new DexClassLoader(jarFile.toString(), "/data/data/com.weex.sample", null, ClassLoader.getSystemClassLoader());
                DexClassLoader cl = new DexClassLoader(jarFile.toString(), "/data/data/com.weex.sample", null, context.getClassLoader());
                Class c = cl.loadClass("com.chinaums.applet.module.stream.UmsStreamModule");
                //Class clazz = Class.forName(className);
                WXSDKEngine.registerModule("ums_stream", c, false);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (WXException e) {
            e.printStackTrace();
        }
    }

    public static void registerComponent(String name, String className) {
        try {
            if (name == null || className == null) {
                return;
            }
            Class clazz = Class.forName(className);
            WXSDKEngine.registerComponent(name, clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (WXException e) {
            e.printStackTrace();
        }
    }

    public static void registerModule(String name, String className, boolean global) {
        if (name == null || className == null) {
            return;
        }
        try {
            Class clazz = Class.forName(className);
            WXSDKEngine.registerModule(name, clazz, global);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (WXException e) {
            e.printStackTrace();
        }
    }

    public static void registerExtModule(String name, String className, boolean global) {
        if (name == null || className == null) {
            return;
        }
        try {
            File jarFile = new File("/sdcard/weextest/applet-api-release.aar");
            if (jarFile.exists()) {
                DexClassLoader cl = new DexClassLoader(jarFile.toString(), "/sdcard/weextest", null, ClassLoader.getSystemClassLoader());
                Class<?> c = cl.loadClass("com.chinaums.applet.module.stream.UmsStreamModule");
                Class clazz = Class.forName(className);
                WXSDKEngine.registerModule("ums_stream", clazz, false);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (WXException e) {
            e.printStackTrace();
        }
    }

    private static void registerComponents(HashMap<String, PluginEntry> components) {
        for (Map.Entry<String, PluginEntry> component : components.entrySet()) {
            registerComponent(component.getKey(), component.getValue().mPluginClass);
        }
    }

    private static void registerModules(HashMap<String, PluginEntry> modules) {
        for (Map.Entry<String, PluginEntry> module : modules.entrySet()) {
            registerModule(module.getKey(), module.getValue().mPluginClass, module.getValue().mOnload);
        }
    }

    private static void loadDefaultConfig(Context context) {
        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(context);
        sComponents = parser.getPluginComponents();
        sModules = parser.getPluginModules();
    }

    private static void loadExtendConfig(Context context) {
        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(context, "/data/data/com.weex.sample/applet-api-release.aar");//外部扩展的API
        sExtComponents = parser.getPluginComponents();
        sExtModules = parser.getPluginModules();
    }

    public static void copyfile(File fromFile, File toFile,Boolean rewrite ) {
        if (!fromFile.exists()) {
            return;
        }
        if (!fromFile.isFile()) {
            return;
        }
        if (!fromFile.canRead()) {
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }
        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); //将内容写到新文件当中
            }
            fosfrom.close();
            fosto.close();
        } catch (Exception ex) {
            Log.e("readfile", ex.getMessage());
        }
    }

}
