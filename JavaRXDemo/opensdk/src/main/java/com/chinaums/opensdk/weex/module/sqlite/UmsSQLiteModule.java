package com.chinaums.opensdk.weex.module.sqlite;

import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.weex.ExecutorManager;
import com.chinaums.opensdk.weex.module.UmsBasicModule;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class UmsSQLiteModule extends UmsBasicModule {

    /**
     * dbrmap
     */
    static public ConcurrentHashMap<String, DBRunner> dbrmap = new ConcurrentHashMap<String, DBRunner>();


    @JSMethod
    public void runSql(String actionAsString, String args, JSCallback cbc) {
        Action action;
        try {
            action = Action.valueOf(actionAsString);
        } catch (IllegalArgumentException e) {
            UmsLog.e("unexpected error", e.getMessage().toString());
            callBySimple(false, cbc);
            return;
        }
        try {
            JSONArray jsonArray = new JSONArray(args);
            executeAndPossiblyThrow(action, jsonArray, cbc);
        } catch (JSONException e) {
            UmsLog.e("unexpected error", e);
            callBySimple(false, cbc);
        }
    }

    private void executeAndPossiblyThrow(Action action, JSONArray args, JSCallback cbc)
            throws JSONException {
        JSONObject o;
        String dbname;
        switch (action) {
            case open:
                o = args.getJSONObject(0);
                dbname = o.getString("name");
                this.startDatabase(dbname, o, cbc);
                break;
            case close:
                o = args.getJSONObject(0);
                dbname = o.getString("path");
                this.closeDatabase(dbname, cbc);
                break;
            case delete:
                o = args.getJSONObject(0);
                dbname = o.getString("path");
                deleteDatabase(dbname, cbc);
                break;
            case executeSqlBatch:
            case backgroundExecuteSqlBatch:
                String[] queries;
                String[] queryIDs = null;
                JSONArray jsonArr;
                JSONArray[] jsonparams = null;
                JSONObject allargs = args.getJSONObject(0);
                JSONObject dbargs = allargs.getJSONObject("dbargs");
                dbname = dbargs.getString("dbname");
                JSONArray txargs = allargs.getJSONArray("executes");
                if (txargs.isNull(0)) {
                    queries = new String[0];
                } else {
                    int len = txargs.length();
                    queries = new String[len];
                    queryIDs = new String[len];
                    jsonparams = new JSONArray[len];
                    for (int i = 0; i < len; i++) {
                        JSONObject a = txargs.getJSONObject(i);
                        queries[i] = a.getString("sql");
                        queryIDs[i] = a.getString("qid");
                        jsonArr = a.getJSONArray("params");
                        jsonparams[i] = jsonArr;
                    }
                }
                DBQuery q = new DBQuery(queries, queryIDs, jsonparams, cbc);
                DBRunner r = dbrmap.get(dbname);
                if (r != null) {
                    try {
                        r.q.put(q);
                    } catch (Exception e) {
                        UmsLog.e("couldn't add to queue", e);
                        callBySimple(false, cbc);
                    }
                } else {
                    callBySimple(false, cbc);
                }
                break;
        }
    }

    public void onDestroy() {
        while (!dbrmap.isEmpty()) {
            String dbname = dbrmap.keySet().iterator().next();
            this.closeDatabaseNow(dbname);
            DBRunner r = dbrmap.get(dbname);
            try {
                r.q.put(new DBQuery());
            } catch (Exception e) {
                UmsLog.e("couldn't stop db thread", e);
            }
            dbrmap.remove(dbname);
        }
    }

    private void startDatabase(String dbname, JSONObject options, JSCallback cbc) {
        DBRunner r = dbrmap.get(dbname);
        if (r != null) {
            callBySimple(true, cbc);
        } else {
            r = new DBRunner(dbname, options, cbc, this);
            dbrmap.put(dbname, r);
            ExecutorManager.getInstance().execute(r);
        }
    }

    public SQLiteAndroidDatabase openDatabase(String dbname, JSCallback cbc, boolean old_impl) throws Exception {
        try {
            File dbfile;
            String databasePath = getActivity().getDatabasePath(dbname).getAbsolutePath();
            if (databasePath != null) {
                dbfile = new File(databasePath, dbname);
            } else {
                dbfile = getActivity().getDatabasePath(dbname);
            }
            if (!dbfile.exists()) {
                dbfile.getParentFile().mkdirs();
            }
            UmsLog.v("Open sqlite db: " + dbfile.getAbsolutePath());

            SQLiteAndroidDatabase mydb = old_impl ? new SQLiteAndroidDatabase() : new SQLiteDatabaseNDK();
            mydb.open(dbfile);
            if (cbc != null)
                callBySimple(true, cbc);
            return mydb;
        } catch (Exception e) {
            if (cbc != null)
                callBySimple(false, cbc);
            throw e;
        }
    }

    private void closeDatabase(String dbname, JSCallback cbc) {
        DBRunner r = dbrmap.get(dbname);
        if (r != null) {
            try {
                r.q.put(new DBQuery(false, cbc));
            } catch (Exception e) {
                if (cbc != null) {
                    callBySimple(false, cbc);
                }
                UmsLog.e("couldn't close database", e);
            }
        } else {
            if (cbc != null) {
                callBySimple(true, cbc);
            }
        }
    }

    public void closeDatabaseNow(String dbname) {
        DBRunner r = dbrmap.get(dbname);
        if (r != null) {
            SQLiteAndroidDatabase mydb = r.mydb;
            if (mydb != null)
                mydb.closeDatabaseNow();
        }
    }

    private void deleteDatabase(String dbname, JSCallback cbc) {
        DBRunner r = dbrmap.get(dbname);
        if (r != null) {
            try {
                r.q.put(new DBQuery(true, cbc));
            } catch (Exception e) {
                if (cbc != null) {
                    callBySimple(false, cbc);
                }
                UmsLog.e("couldn't close database", e);
            }
        } else {
            boolean deleteResult = this.deleteDatabaseNow(dbname);
            if (deleteResult) {
                callBySimple(true, cbc);
            } else {
                callBySimple(false, cbc);
            }
        }
    }

    public boolean deleteDatabaseNow(String dbname) {
        File dbfile = getActivity().getDatabasePath(dbname);
        try {
            return getActivity().deleteDatabase(dbfile.getAbsolutePath());
        } catch (Exception e) {
            UmsLog.e("couldn't delete database", e);
            return false;
        }
    }

}
