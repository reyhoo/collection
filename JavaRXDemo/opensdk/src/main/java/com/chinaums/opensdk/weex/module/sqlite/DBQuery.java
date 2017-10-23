package com.chinaums.opensdk.weex.module.sqlite;

import com.taobao.weex.bridge.JSCallback;

import org.json.JSONArray;


public class DBQuery {

    /**
     * stop
     */
    final public boolean stop;

    /**
     * close
     */
    final public boolean close;

    /**
     * delete
     */
    final public boolean delete;

    /**
     * queries
     */
    final public String[] queries;

    /**
     * queryIDs
     */
    final public String[] queryIDs;

    /**
     * jsonparams
     */
    final public JSONArray[] jsonparams;

    /**
     * cbc
     */
    final public JSCallback cbc;

    DBQuery(String[] myqueries, String[] qids, JSONArray[] params, JSCallback c) {
        this.stop = false;
        this.close = false;
        this.delete = false;
        this.queries = myqueries;
        this.queryIDs = qids;
        this.jsonparams = params;
        this.cbc = c;
    }

    DBQuery(boolean delete, JSCallback cbc) {
        this.stop = true;
        this.close = true;
        this.delete = delete;
        this.queries = null;
        this.queryIDs = null;
        this.jsonparams = null;
        this.cbc = cbc;
    }

    DBQuery() {
        this.stop = true;
        this.close = false;
        this.delete = false;
        this.queries = null;
        this.queryIDs = null;
        this.jsonparams = null;
        this.cbc = null;
    }

}
