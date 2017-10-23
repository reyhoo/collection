package com.chinaums.opensdk.weex.module.sqlite;

import com.chinaums.opensdk.util.UmsLog;
import com.taobao.weex.bridge.JSCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.liteglue.SQLColumnType;
import io.liteglue.SQLiteConnection;
import io.liteglue.SQLiteConnector;
import io.liteglue.SQLiteOpenFlags;
import io.liteglue.SQLiteStatement;


public class SQLiteDatabaseNDK extends SQLiteAndroidDatabase {

    /**
     * mydb
     */
    public SQLiteConnection mydb;

    /**
     * connector
     */
    static private SQLiteConnector connector = new SQLiteConnector();

    @Override
    public void open(File dbFile) throws Exception {
        mydb = connector.newSQLiteConnection(dbFile.getAbsolutePath(),
                SQLiteOpenFlags.READWRITE | SQLiteOpenFlags.CREATE);
    }

    @Override
    public void closeDatabaseNow() {
        try {
            if (mydb != null)
                mydb.dispose();
        } catch (Exception e) {
            UmsLog.e("couldn't close database, ignoring", e);
        }
    }

    @Override
    public void bugWorkaround() {

    }

    @Override
    public void executeSqlBatch(String[] queryarr, JSONArray[] jsonparams,
                          String[] queryIDs, JSCallback cbc, UmsSQLiteModule module) {
        if (mydb == null) {
            module.callBySimple(false, cbc);
            return;
        }
        int len = queryarr.length;
        JSONArray batchResults = new JSONArray();
        for (int i = 0; i < len; i++) {
            String query_id = queryIDs[i];
            JSONObject queryResult = null;
            String errorMessage = "unknown";
            try {
                String query = queryarr[i];
                long lastTotal = mydb.getTotalChanges();
                queryResult = this.executeSqlStatementNDK(query, jsonparams[i], cbc);
                long newTotal = mydb.getTotalChanges();
                long rowsAffected = newTotal - lastTotal;
                queryResult.put("rowsAffected", rowsAffected);
                if (rowsAffected > 0) {
                    long insertId = mydb.getLastInsertRowid();
                    if (insertId > 0) {
                        queryResult.put("insertId", insertId);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                errorMessage = ex.getMessage();
                UmsLog.v("SQLitePlugin.executeSql[Batch](): Error=" + errorMessage);
            }
            try {
                if (queryResult != null) {
                    JSONObject r = new JSONObject();
                    r.put("qid", query_id);
                    r.put("type", "success");
                    r.put("result", queryResult);
                    batchResults.put(r);
                } else {
                    JSONObject r = new JSONObject();
                    r.put("qid", query_id);
                    r.put("type", "error");
                    JSONObject er = new JSONObject();
                    er.put("message", errorMessage);
                    r.put("result", er);
                    batchResults.put(r);
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
                UmsLog.v("SQLitePlugin.executeSql[Batch](): Error=" + ex.getMessage());
            }
        }
        module.callBySimple(true, cbc);
    }

    private JSONObject executeSqlStatementNDK(String query, JSONArray paramsAsJson,
                                              JSCallback cbc) throws Exception {
        JSONObject rowsResult = new JSONObject();
        boolean hasRows = false;
        SQLiteStatement myStatement = mydb.prepareStatement(query);
        try {
            for (int i = 0; i < paramsAsJson.length(); ++i) {
                if (paramsAsJson.isNull(i)) {
                    myStatement.bindNull(i + 1);
                } else {
                    Object p = paramsAsJson.get(i);
                    if (p instanceof Float || p instanceof Double)
                        myStatement.bindDouble(i + 1, paramsAsJson.getDouble(i));
                    else if (p instanceof Number)
                        myStatement.bindLong(i + 1, paramsAsJson.getLong(i));
                    else
                        myStatement.bindTextNativeString(i + 1, paramsAsJson.getString(i));
                }
            }
            hasRows = myStatement.step();
        } catch (Exception ex) {
            ex.printStackTrace();
            String errorMessage = ex.getMessage();
            UmsLog.v("SQLitePlugin.executeSql[Batch](): Error=" + errorMessage);
            myStatement.dispose();
            throw ex;
        }
        if (hasRows) {
            JSONArray rowsArrayResult = new JSONArray();
            String key = "";
            int colCount = myStatement.getColumnCount();
            do {
                JSONObject row = new JSONObject();
                try {
                    for (int i = 0; i < colCount; ++i) {
                        key = myStatement.getColumnName(i);
                        switch (myStatement.getColumnType(i)) {
                            case SQLColumnType.NULL:
                                row.put(key, JSONObject.NULL);
                                break;
                            case SQLColumnType.REAL:
                                row.put(key, myStatement.getColumnDouble(i));
                                break;
                            case SQLColumnType.INTEGER:
                                row.put(key, myStatement.getColumnLong(i));
                                break;
                            case SQLColumnType.BLOB:
                            case SQLColumnType.TEXT:
                            default:
                                row.put(key, myStatement.getColumnTextNativeString(i));
                        }
                    }
                    rowsArrayResult.put(row);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (myStatement.step());
            try {
                rowsResult.put("rows", rowsArrayResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        myStatement.dispose();
        return rowsResult;
    }

}