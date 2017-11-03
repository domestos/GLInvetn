package com.example.valerapelenskyi.glinvent.model.constants;

import com.example.valerapelenskyi.glinvent.model.Device;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by valera.pelenskyi on 26.10.17.
 */

public class Const {
    //  public static String   server_url = "http://varenikhome.ddns.net/PHPScript/db_get_all.php";
    public static List<Device> devices =  null;
    public static JSONObject JSONResponseFromMysql = null;
    public static String   server_url = "http://lvilwks0004.lvi.gameloft.org/PHPScript/db_get_all.php";

    public static String update_status_invent_url ="http://lvilwks0004.lvi.gameloft.org/PHPScript/db_update_status.php";
    public static String  TAG_LOG = "TAG_LOG";
    public static final int STATUS_SYNC_ONLINE = 0;
    public static final int STATUS_SYNC_OFFLINE = 1;
}
