package com.chinaums.opensdk.weex.module.sqlite;

import com.chinaums.opensdk.util.UmsLog;
import com.taobao.weex.bridge.JSCallback;

import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class DBRunner implements Runnable {

    /**
     * dbname
     */
    final private String dbname;

    /**
     * oldImpl
     */
    private boolean oldImpl;

    /**
     * bugWorkaround
     */
    private boolean bugWorkaround;

    /**
     * q
     */
    final public BlockingQueue<DBQuery> q;

    /**
     * openCbc
     */
    final private JSCallback openCbc;

    /**
     * mydb
     */
    public SQLiteAndroidDatabase mydb;

    /**
     * module
     */
    private UmsSQLiteModule module;

    DBRunner(final String dbname, JSONObject options, JSCallback cbc, UmsSQLiteModule module) {
        this.dbname = dbname;
        this.oldImpl = options.has("androidOldDatabaseImplementation");
        UmsLog.v("Android db implementation: " + (oldImpl ? "OLD" : "sqlite4java (NDK)"));
        this.bugWorkaround = this.oldImpl && options.has("androidBugWorkaround");
        if (this.bugWorkaround)
            UmsLog.v("Android db closing/locking workaround applied");
        this.q = new LinkedBlockingQueue<DBQuery>();
        this.openCbc = cbc;
        this.module = module;
    }

    public void run() {
        try {
            this.mydb = module.openDatabase(dbname, this.openCbc, this.oldImpl);
        } catch (Exception e) {
            UmsLog.e("unexpected error, stopping db thread", e);
            module.dbrmap.remove(dbname);
            return;
        }
        DBQuery dbq = null;
        try {
            dbq = q.take();
            while (!dbq.stop) {
                mydb.executeSqlBatch(dbq.queries, dbq.jsonparams, dbq.queryIDs, dbq.cbc, module);
                if (this.bugWorkaround && dbq.queries.length == 1 && dbq.queries[0] == "COMMIT")
                    mydb.bugWorkaround();
                dbq = q.take();
            }
        } catch (Exception e) {
            UmsLog.e("unexpected error", e);
        }

        if (dbq != null && dbq.close) {
            try {
                module.closeDatabaseNow(dbname);
                module.dbrmap.remove(dbname);
                if (!dbq.delete) {
                    module.callBySimple(true, dbq.cbc);
                } else {
                    try {
                        boolean deleteResult = module.deleteDatabaseNow(dbname);
                        if (deleteResult) {
                            module.callBySimple(true, dbq.cbc);
                        } else {
                            module.callBySimple(false, dbq.cbc);
                        }
                    } catch (Exception e) {
                        UmsLog.e("couldn't delete database", e);
                        module.callBySimple(false, dbq.cbc);
                    }
                }
            } catch (Exception e) {
                UmsLog.e("couldn't close database", e.getMessage().toString());
                if (dbq.cbc != null) {
                    module.callBySimple(false, dbq.cbc);
                }
            }
        }
    }
}
