package com.example.valerapelenskyi.glinvent.database.sqlite;

import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.valerapelenskyi.glinvent.model.Device;
import com.example.valerapelenskyi.glinvent.model.constants.Const;

import java.util.ArrayList;

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
        sqLiteDatabase = getSqLiteDatabase();
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

    //=================Methods ==============================
    public ArrayList<Device> getAllItems(){
        Log.d(Const.TAG_LOG, "SQLiteConnect getAllItems");
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME, null,null,null,null,null, null);

        if(cursor.moveToFirst()){
            Log.d(Const.TAG_LOG, "row true= "+String.valueOf(cursor.getCount()));
            ArrayList<Device>  devices = new ArrayList<Device>();
            devices.add(new Device(1, "250515/6/1"));

            cursor.close();
            return devices;
        }else{
            Log.d(Const.TAG_LOG, "row = "+String.valueOf(cursor.getCount()));
            //=============припустимо база не пуста
            ArrayList<Device>  devices = new ArrayList<Device>();
            devices.add(new Device(1, "250515/6/1"));
            //=====================================
            cursor.close();
            return devices;
        }




    }

}
