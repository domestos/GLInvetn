package com.example.valerapelenskyi.glinvent.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.valerapelenskyi.glinvent.model.constants.Const;

/**
 * Created by valera.pelenskyi on 25.10.17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION =2;
    public static final String DATABASE_NAME="wp_gameloft";
    public static final String TABLE_NAME="wp_inventory";
    public static final String KEY_ID = "_id";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_ITEM="item";
    public static final String KEY_NAME_WKS="name_wks";
    public static final String KEY_OWNER="owner";
    public static final String KEY_LOCATION="location";
    public static final String KEY_STATUS_INVENT="status_INVENT";
    public static final String KEY_STATUS_SYNC="status_sync";
    public static final String KEY_DESCRIPTION="description";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(Const.TAG_LOG, "Was create Table "+TABLE_NAME);

        sqLiteDatabase.execSQL(" create table "+TABLE_NAME+
                "("+
                    KEY_ID+" integer primary key, "+
                    KEY_NUMBER+" text, "+
                    KEY_ITEM+" text, "+
                    KEY_NAME_WKS+" text, "+
                    KEY_OWNER+" text, "+
                    KEY_LOCATION+" text, "+
                    KEY_STATUS_INVENT+" text, "+
                    KEY_STATUS_SYNC+" integer, "+
                    KEY_DESCRIPTION +" text "+
                ") "
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("drop table if exists "+ TABLE_NAME);
            onCreate(sqLiteDatabase);
    }

    public void dropTable(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("drop table if exists "+ TABLE_NAME);
    }

}
