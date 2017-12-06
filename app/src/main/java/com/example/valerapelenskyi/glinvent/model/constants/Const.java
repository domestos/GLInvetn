package com.example.valerapelenskyi.glinvent.model.constants;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.valerapelenskyi.glinvent.model.Device;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by valera.pelenskyi on 26.10.17.
 */

public class Const extends Application {
//work

    public static String url_host ="";
    public static String server_url = "http://"+url_host+"/PHPScript/db_get_all.php";
    public static String update_status_invent_url ="http://"+url_host+"/PHPScript/db_update_status.php";
    public static String update_item="http://"+url_host+"/PHPScript/db_update.php";


    public static String  TAG_LOG = "TAG_LOG";
    public static final int STATUS_SYNC_ONLINE = 0;
    public static final int STATUS_SYNC_OFFLINE = 1;
    public static final String STATUS_FINED= "ok";



    public static void concatUrl(String url_host) {
        server_url = "http://"+url_host+"/PHPScript/db_get_all.php";
        update_status_invent_url ="http://"+url_host+"/PHPScript/db_update_status.php";
        update_item="http://"+url_host+"/PHPScript/db_update.php";
    }
}
