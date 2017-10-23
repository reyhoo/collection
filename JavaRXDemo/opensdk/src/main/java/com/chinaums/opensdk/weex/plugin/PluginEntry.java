package com.chinaums.opensdk.weex.plugin;

public final class PluginEntry {

    /**
     * The name of the service that this plugin implements
     */
    public final String mService;

    /**
     * The plugin class name that implements the service.
     */
    public final String mPluginClass;

    /**
     * Flag that indicates the plugin object should be created when PluginManager is initialized.
     */
    public final boolean mOnload;

    /**
     * The plugin category, "module" or "component".
     */
    public final String mCategory;

    public PluginEntry(String service, String pluginClass, boolean onload, String category) {
        this.mService = service;
        this.mCategory = category;
        this.mPluginClass = pluginClass;
        this.mOnload = onload;
    }
    
}
