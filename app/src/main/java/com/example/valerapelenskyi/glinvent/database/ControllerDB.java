package com.example.valerapelenskyi.glinvent.database;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.valerapelenskyi.glinvent.R;
import com.example.valerapelenskyi.glinvent.database.mysql.MySQLConnect;
import com.example.valerapelenskyi.glinvent.database.sqlite.SQLiteConnect;
import com.example.valerapelenskyi.glinvent.model.constants.Const;
import org.json.JSONObject;

/**
 * Created by valera.pelenskyi on 26.10.17.
 */

public class ControllerDB extends Application {
    Context context;
    JSONObject JO;

    public ControllerDB(Context context){
        this.context  = context;

    }
    // ==================================   SQLite =================================================
    //перевіряємо чи створена таблиця SQLit і чи вона не пуста
/*
    // Екземпляр класу  DBHelper неохідно створити, щоб отримати доступ до управління DataBase
    private  DBHelper dbHelper = new DBHelper(getApplicationContext());
    //Підключаємося до БД. Отримуємо бвзу даних та методи роботи з нею (Insert, Update, Delete)
    private  SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
    // Обєкт який працює з полями таблиці та її значеннями
    private  ContentValues contentValues = new ContentValues();*/


    //=================================== MYSQL ====================================================




    // ========================== GET ALL ITEMS FROM MYSQL =========================================
    // running in the Thread so need waiting when method will be finished
    public JSONObject getJsonObjectRequest() {
        Log.d(Const.TAG_LOG, "run getJsonObjectRequest");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Const.server_url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                 //       Log.d(Const.TAG_LOG, " JSONObject response = "+response.toString());
                       JO =  response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getMessage();
                        JO =  null;
                    }
                }
        );

        MySQLConnect.getInstance(context.getApplicationContext()).addToRequestque(jsonObjectRequest);
        return   JO  ;

    }


    //========================== INSERT ALL ITEM to SQLite =========================================
    public void insertAllItems(JSONObject jsonResponseFromMysql) {
        if(jsonResponseFromMysql !=null) {
            Log.d(Const.TAG_LOG, "run insertAllItems and jsonResponseFromMysql =  " + jsonResponseFromMysql.toString());
        }else {
            Log.d(Const.TAG_LOG, "run insertAllItems and jsonResponseFromMysql = null " );
        }
        SQLiteDatabase sqLiteConnect =   SQLiteConnect.getInstance(context.getApplicationContext()).getSqLiteDatabase();
        Log.d(Const.TAG_LOG, "version databese  = " + sqLiteConnect.getVersion());

    }

}
