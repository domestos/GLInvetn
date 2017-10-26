package com.example.valerapelenskyi.glinvent.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by valera.pelenskyi on 26.10.17.
 */

public class SQLiteConnect {

    private static SQLiteConnect sqLiteConnect;
    private static Context mCtx;
    private static DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;


    private SQLiteConnect(Context context) {
        mCtx = context;
        dbHelper = getDbHelper();
    }

    public static  synchronized SQLiteConnect getInstance(Context context){
        if(sqLiteConnect == null){
            sqLiteConnect = new SQLiteConnect(context);
        }
        return sqLiteConnect;
    }

    private DBHelper getDbHelper() {
        if(dbHelper == null){
            dbHelper = new DBHelper(mCtx.getApplicationContext());
        }
        return dbHelper;
    }

    public SQLiteDatabase getSqLiteDatabase() {
        if(dbHelper != null){
            sqLiteDatabase = dbHelper.getWritableDatabase();
        }
        return sqLiteDatabase;
    }
}
