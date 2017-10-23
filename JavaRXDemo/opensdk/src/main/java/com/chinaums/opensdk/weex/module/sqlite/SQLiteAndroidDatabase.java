package com.chinaums.opensdk.weex.module.sqlite;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Base64;

import com.chinaums.opensdk.util.UmsLog;
import com.taobao.weex.bridge.JSCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLiteAndroidDatabase {

    private static final Pattern FIRST_WORD = Pattern.compile("^\\s*(\\S+)",
            (int) Pattern.CASE_INSENSITIVE);

    private static final Pattern WHERE_CLAUSE = Pattern.compile("\\s+WHERE\\s+(.+)$",
            (int) Pattern.CASE_INSENSITIVE);

    private static final Pattern UPDATE_TABLE_NAME = Pattern.compile("^\\s*UPDATE\\s+(\\S+)",
            (int) Pattern.CASE_INSENSITIVE);

    private static final Pattern DELETE_TABLE_NAME = Pattern.compile("^\\s*DELETE\\s+FROM\\s+(\\S+)",
            (int) Pattern.CASE_INSENSITIVE);

    /**
     * dbFile
     */
    private File dbFile;

    /**
     * mydb
     */
    private SQLiteDatabase mydb;

    public void open(File dbfile) throws Exception {
        dbFile = dbfile;
        mydb = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
    }

    public void closeDatabaseNow() {
        if (mydb != null) {
            mydb.close();
            mydb = null;
        }
    }

    public void bugWorkaround() throws Exception {
        this.closeDatabaseNow();
        this.open(dbFile);
    }

    /**
     * Executes a batch request and sends the results via cbc.
     *
     * @param queryarr   Array of query strings
     * @param jsonparams Array of JSON query parameters
     * @param queryIDs   Array of query ids
     * @param cbc        Callback context from Cordova API
     */
    @SuppressLint("NewApi")
    void executeSqlBatch(String[] queryarr, JSONArray[] jsonparams,
                         String[] queryIDs, JSCallback cbc, UmsSQLiteModule module) {
        if (mydb == null) {
            module.callBySimple(false, cbc);
            return;
        }
        String query;
        String query_id;
        int len = queryarr.length;
        JSONArray batchResults = new JSONArray();
        for (int i = 0; i < len; i++) {
            int rowsAffectedCompat = 0;
            boolean needRowsAffectedCompat = false;
            query_id = queryIDs[i];
            JSONObject queryResult = null;
            String errorMessage = "unknown";
            try {
                boolean needRawQuery = true;
                query = queryarr[i];
                QueryType queryType = getQueryType(query);
                if (queryType == QueryType.update || queryType == queryType.delete) {
                    if (android.os.Build.VERSION.SDK_INT >= 11) {
                        SQLiteStatement myStatement = mydb.compileStatement(query);
                        if (jsonparams != null) {
                            bindArgsToStatement(myStatement, jsonparams[i]);
                        }
                        int rowsAffected = -1;
                        try {
                            rowsAffected = myStatement.executeUpdateDelete();
                            needRawQuery = false;
                        } catch (SQLiteException ex) {
                            ex.printStackTrace();
                            errorMessage = ex.getMessage();
                            UmsLog.v("SQLiteStatement.executeUpdateDelete(): Error=" + errorMessage);
                            needRawQuery = false;
                        } catch (Exception ex) {
                            UmsLog.v("SQLiteStatement.executeUpdateDelete(): Error=" + ex.getMessage());
                        }
                        if (rowsAffected != -1) {
                            queryResult = new JSONObject();
                            queryResult.put("rowsAffected", rowsAffected);
                        }
                    } else {
                        rowsAffectedCompat = countRowsAffectedCompat(queryType, query, jsonparams, mydb, i);
                        needRowsAffectedCompat = true;
                    }
                }

                if (queryType == QueryType.insert && jsonparams != null) {
                    needRawQuery = false;
                    SQLiteStatement myStatement = mydb.compileStatement(query);
                    bindArgsToStatement(myStatement, jsonparams[i]);
                    long insertId = -1;
                    try {
                        insertId = myStatement.executeInsert();
                        queryResult = new JSONObject();
                        if (insertId != -1) {
                            queryResult.put("insertId", insertId);
                            queryResult.put("rowsAffected", 1);
                        } else {
                            queryResult.put("rowsAffected", 0);
                        }
                    } catch (SQLiteException ex) {
                        ex.printStackTrace();
                        errorMessage = ex.getMessage();
                        UmsLog.v("SQLiteDatabase.executeInsert(): Error=" + errorMessage);
                    }
                }
                if (queryType == QueryType.begin) {
                    needRawQuery = false;
                    try {
                        mydb.beginTransaction();
                        queryResult = new JSONObject();
                        queryResult.put("rowsAffected", 0);
                    } catch (SQLiteException ex) {
                        ex.printStackTrace();
                        errorMessage = ex.getMessage();
                        UmsLog.v("SQLiteDatabase.beginTransaction(): Error=" + errorMessage);
                    }
                }
                if (queryType == QueryType.commit) {
                    needRawQuery = false;
                    try {
                        mydb.setTransactionSuccessful();
                        mydb.endTransaction();
                        queryResult = new JSONObject();
                        queryResult.put("rowsAffected", 0);
                    } catch (SQLiteException ex) {
                        ex.printStackTrace();
                        errorMessage = ex.getMessage();
                        UmsLog.v("SQLiteDatabase.setTransactionSuccessful/endTransaction(): Error=" + errorMessage);
                    }
                }
                if (queryType == QueryType.rollback) {
                    needRawQuery = false;
                    try {
                        mydb.endTransaction();
                        queryResult = new JSONObject();
                        queryResult.put("rowsAffected", 0);
                    } catch (SQLiteException ex) {
                        ex.printStackTrace();
                        errorMessage = ex.getMessage();
                        UmsLog.v("SQLiteDatabase.endTransaction(): Error=" + errorMessage);
                    }
                }
                if (needRawQuery) {
                    queryResult = this.executeSqlStatementQuery(mydb, query, jsonparams[i], cbc);
                    if (needRowsAffectedCompat) {
                        queryResult.put("rowsAffected", rowsAffectedCompat);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                errorMessage = ex.getMessage();
                UmsLog.v("SQLiteAndroidDatabase.executeSql[Batch](): Error=" + errorMessage);
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
                UmsLog.v("SQLiteAndroidDatabase.executeSql[Batch](): Error=" + ex.getMessage());
            }
        }
        module.callBySimple(true, cbc);
        //cbc.success(batchResults);
    }

    private int countRowsAffectedCompat(QueryType queryType, String query, JSONArray[] jsonparams,
                                        SQLiteDatabase mydb, int i) throws JSONException {
        Matcher whereMatcher = WHERE_CLAUSE.matcher(query);
        String where = "";
        int pos = 0;
        while (whereMatcher.find(pos)) {
            where = " WHERE " + whereMatcher.group(1);
            pos = whereMatcher.start(1);
        }
        int numQuestionMarks = 0;
        for (int j = 0; j < where.length(); j++) {
            if (where.charAt(j) == '?') {
                numQuestionMarks++;
            }
        }
        JSONArray subParams = null;
        if (jsonparams != null) {
            JSONArray origArray = jsonparams[i];
            subParams = new JSONArray();
            int startPos = origArray.length() - numQuestionMarks;
            for (int j = startPos; j < origArray.length(); j++) {
                subParams.put(j - startPos, origArray.get(j));
            }
        }
        if (queryType == QueryType.update) {
            Matcher tableMatcher = UPDATE_TABLE_NAME.matcher(query);
            if (tableMatcher.find()) {
                String table = tableMatcher.group(1);
                try {
                    SQLiteStatement statement = mydb.compileStatement(
                            "SELECT count(*) FROM " + table + where);
                    if (subParams != null) {
                        bindArgsToStatement(statement, subParams);
                    }
                    return (int) statement.simpleQueryForLong();
                } catch (Exception e) {
                    UmsLog.e("uncaught", e);
                }
            }
        } else {
            Matcher tableMatcher = DELETE_TABLE_NAME.matcher(query);
            if (tableMatcher.find()) {
                String table = tableMatcher.group(1);
                try {
                    SQLiteStatement statement = mydb.compileStatement(
                            "SELECT count(*) FROM " + table + where);
                    bindArgsToStatement(statement, subParams);
                    return (int) statement.simpleQueryForLong();
                } catch (Exception e) {
                    UmsLog.e("uncaught", e);
                }
            }
        }
        return 0;
    }

    private void bindArgsToStatement(SQLiteStatement myStatement, JSONArray sqlArgs) throws JSONException {
        for (int i = 0; i < sqlArgs.length(); i++) {
            if (sqlArgs.get(i) instanceof Float || sqlArgs.get(i) instanceof Double) {
                myStatement.bindDouble(i + 1, sqlArgs.getDouble(i));
            } else if (sqlArgs.get(i) instanceof Number) {
                myStatement.bindLong(i + 1, sqlArgs.getLong(i));
            } else if (sqlArgs.isNull(i)) {
                myStatement.bindNull(i + 1);
            } else {
                myStatement.bindString(i + 1, sqlArgs.getString(i));
            }
        }
    }

    private JSONObject executeSqlStatementQuery(SQLiteDatabase mydb,
                                                String query, JSONArray paramsAsJson,
                                                JSCallback cbc) throws Exception {
        JSONObject rowsResult = new JSONObject();
        Cursor cur = null;
        try {
            String[] params = null;
            params = new String[paramsAsJson.length()];
            for (int j = 0; j < paramsAsJson.length(); j++) {
                if (paramsAsJson.isNull(j))
                    params[j] = "";
                else
                    params[j] = paramsAsJson.getString(j);
            }
            cur = mydb.rawQuery(query, params);
        } catch (Exception ex) {
            ex.printStackTrace();
            String errorMessage = ex.getMessage();
            UmsLog.v("SQLiteAndroidDatabase.executeSql[Batch](): Error=" + errorMessage);
            throw ex;
        }
        if (cur != null && cur.moveToFirst()) {
            JSONArray rowsArrayResult = new JSONArray();
            String key = "";
            int colCount = cur.getColumnCount();
            do {
                JSONObject row = new JSONObject();
                try {
                    for (int i = 0; i < colCount; ++i) {
                        key = cur.getColumnName(i);
                        if (android.os.Build.VERSION.SDK_INT >= 11) {
                            try {
                                bindPostHoneycomb(row, key, cur, i);
                            } catch (Exception ex) {
                                bindPreHoneycomb(row, key, cur, i);
                            }
                        } else {
                            bindPreHoneycomb(row, key, cur, i);
                        }
                    }
                    rowsArrayResult.put(row);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cur.moveToNext());
            try {
                rowsResult.put("rows", rowsArrayResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (cur != null) {
            cur.close();
        }
        return rowsResult;
    }

    @SuppressLint("NewApi")
    private void bindPostHoneycomb(JSONObject row, String key, Cursor cur, int i) throws JSONException {
        int curType = cur.getType(i);
        switch (curType) {
            case Cursor.FIELD_TYPE_NULL:
                row.put(key, JSONObject.NULL);
                break;
            case Cursor.FIELD_TYPE_INTEGER:
                row.put(key, cur.getLong(i));
                break;
            case Cursor.FIELD_TYPE_FLOAT:
                row.put(key, cur.getDouble(i));
                break;
            case Cursor.FIELD_TYPE_BLOB:
                row.put(key, new String(Base64.encode(cur.getBlob(i), Base64.DEFAULT)));
                break;
            case Cursor.FIELD_TYPE_STRING:
            default:
                row.put(key, cur.getString(i));
                break;
        }
    }

    private void bindPreHoneycomb(JSONObject row, String key, Cursor cursor, int i) throws JSONException {
        SQLiteCursor sqLiteCursor = (SQLiteCursor) cursor;
        CursorWindow cursorWindow = sqLiteCursor.getWindow();
        int pos = cursor.getPosition();
        if (cursorWindow.isNull(pos, i)) {
            row.put(key, JSONObject.NULL);
        } else if (cursorWindow.isLong(pos, i)) {
            row.put(key, cursor.getLong(i));
        } else if (cursorWindow.isFloat(pos, i)) {
            row.put(key, cursor.getDouble(i));
        } else if (cursorWindow.isBlob(pos, i)) {
            row.put(key, new String(Base64.encode(cursor.getBlob(i), Base64.DEFAULT)));
        } else {
            row.put(key, cursor.getString(i));
        }
    }

    static QueryType getQueryType(String query) {
        Matcher matcher = FIRST_WORD.matcher(query);
        if (matcher.find()) {
            try {
                return QueryType.valueOf(matcher.group(1).toLowerCase());
            } catch (IllegalArgumentException ignore) {

            }
        }
        return QueryType.other;
    }

}
