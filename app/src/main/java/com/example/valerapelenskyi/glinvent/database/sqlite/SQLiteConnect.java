package com.example.valerapelenskyi.glinvent.database.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.valerapelenskyi.glinvent.model.Device;
import com.example.valerapelenskyi.glinvent.model.constants.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by valera.pelenskyi on 26.10.17.
 */

public class SQLiteConnect {

    private static SQLiteConnect sqLiteConnect;
    private static Context mCtx;
    private static DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    // ================= Singelton Methods =====================================================
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

    // ================= getAllItemsFromSQLite =====================================================
    public List<Device> getAllItemsFromSQLite(){
        Log.d(Const.TAG_LOG, "SQLiteConnect getAllItemsFromSQLite");
       List<Device> devices = new ArrayList<Device>();
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME, null,null,null,null,null, null);

        if(cursor.moveToFirst()){
            Log.d(Const.TAG_LOG, "table has "+String.valueOf(cursor.getCount())+" rows");
            for (cursor.isFirst(); !cursor.isAfterLast();cursor.moveToNext()) {
                devices.add(
                        new Device(
                            cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID)),
                            cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NUMBER)),
                            cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ITEM)),
                            cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME_WKS)),
                            cursor.getString(cursor.getColumnIndex(DBHelper.KEY_OWNER)),
                            cursor.getString(cursor.getColumnIndex(DBHelper.KEY_LOCATION)),
                            cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION))
                        )
                );
            }
        }else{
            Log.d(Const.TAG_LOG, "row = "+String.valueOf(cursor.getCount()));
        }

        cursor.close();
        return devices;
    }

    // ================= insertAllItemToSQList =====================================================
    public void insertAllItemToSQList(List<Device> devices) {
        Log.d(Const.TAG_LOG, "run insertAllItemToSQList");
        String sql = "INSERT INTO "+ DBHelper.TABLE_NAME  + " VALUES(?,?,?,?,?,?,?);";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
        if(devices !=null) {
            sqLiteDatabase.beginTransaction();
            Log.d(Const.TAG_LOG, "beginTransaction ");
            try {
                for (int i = 0; i < devices.size(); i++) {
                    Log.e(Const.TAG_LOG, "insert  "+devices.get(i).getId()+" || "+devices.get(i).getNumber()+" ||  "+devices.get(i).getOwner());
                    sqLiteStatement.clearBindings();
                    sqLiteStatement.bindLong(1, devices.get(i).getId());
                    sqLiteStatement.bindString(2, devices.get(i).getNumber());
                    sqLiteStatement.bindString(3, devices.get(i).getItem());
                    sqLiteStatement.bindString(4, devices.get(i).getName_wks());
                    sqLiteStatement.bindString(5, devices.get(i).getOwner());
                    sqLiteStatement.bindString(6, devices.get(i).getLocation());
                    sqLiteStatement.bindString(7, devices.get(i).getDescription());
                    sqLiteStatement.execute();
                }
            sqLiteDatabase.setTransactionSuccessful();

            } finally {
                sqLiteDatabase.endTransaction();
                Log.d(Const.TAG_LOG, "endTransaction ");
            }
            //return true;
        }else{
            Log.d(Const.TAG_LOG,"Can't insert. Devices is Empty");
          //  return false;
        }//end IF


    }

}
