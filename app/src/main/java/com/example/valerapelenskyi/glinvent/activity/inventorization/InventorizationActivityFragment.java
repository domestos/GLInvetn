package com.example.valerapelenskyi.glinvent.activity.inventorization;

import android.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.valerapelenskyi.glinvent.database.mysql.MySQLConnect;
import com.example.valerapelenskyi.glinvent.database.sqlite.SQLiteConnect;
import com.example.valerapelenskyi.glinvent.model.Device;
import com.example.valerapelenskyi.glinvent.model.constants.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by valera.pelenskyi on 24.10.17.
 */

public class InventorizationActivityFragment  extends Fragment{
    private List<Device> devices = new ArrayList<Device>();;
    private JSONObject jsonResponse;
    private boolean isWork = false; //інформує актівіті про стан AsyncTask

    private InventorizationActivity inventorizationActivity;
    public InventorizationActivity linkToActivity(InventorizationActivity inventorizationActivity) {
       return this.inventorizationActivity = inventorizationActivity;
    }


    public void showListView() {
        Log.d(Const.TAG_LOG, "run showListView ");
        devices = getAllItemFromSQLite();
        if(devices == null){
            //якщо в SQLite немає записів то скопіювати їх
            copyDataFromMySQLtoSQLite();
        }
    }


    private List<Device> getAllItemFromSQLite() {
        Log.d(Const.TAG_LOG, "run getAllItemFromSQLite ");
        devices = SQLiteConnect.getInstance(inventorizationActivity).getAllItemsFromSQLite();
        if(devices == null){
            inventorizationActivity.tvResponse.setText("SQLite база пуста. Скопіювати базу з MYSQL ?");
            Toast.makeText(inventorizationActivity, "SQLite => Tabele isEmpty", Toast.LENGTH_SHORT).show();
            return null;
        }
        return devices;
    }


    private JsonObjectRequest copyDataFromMySQLtoSQLite() {
        Log.d(Const.TAG_LOG, "run copyDataFromMySQLtoSQLite");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Const.server_url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        devices = getArrayDevices(response);
                        SQLiteConnect.getInstance(inventorizationActivity.getApplicationContext()).insertAllItemToSQList(devices);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        inventorizationActivity.tvResponse.setText("COPY DATA BASE from MYSQL to SQLite \n" +
                                " При спробі скопіювати базу даних сталася помилка: \n" +
                                ""+error.getMessage()+"\n перевірте зєднання з інтернетм або доступність бази за URL \n" +
                                Const.server_url);
                    }
                }
        );

        MySQLConnect.getInstance(inventorizationActivity.getApplicationContext()).addToRequestque(jsonObjectRequest);
        return jsonObjectRequest;
    }

    private ArrayList<Device> getArrayDevices(JSONObject response) {
        ArrayList<Device> devices = new ArrayList<Device>();
        try {
            if(response.get("success").equals(1)){
                JSONArray products = (JSONArray) response.get("products");
                for (int i=0; i < products.length(); i++){
                    JSONObject JO = (JSONObject) products.get(i);
                    devices.add(new Device(
                            JO.getInt("id"),
                            JO.getString("number"),
                            JO.getString("item"),
                            JO.getString("name_wks"),
                            JO.getString("owner"),
                            JO.getString("location"),
                            JO.getString("status_invent"),
                            JO.getInt("status_sync"),
                            JO.getString("description")
                    ))  ;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            inventorizationActivity.tvResponse.setText(e.getMessage());
        }
            return devices;
    }


    //======================== NEW CLASS ========================
    class IAFragment extends AsyncTask<String, String,String>{
        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }

    //======================== NEW CLASS COPY ========================

    class CopyDB extends AsyncTask<JSONObject,Void, JSONObject>{

        @Override
        protected void onPreExecute() {
            Log.d(Const.TAG_LOG, "run onPreExecute ");
            inventorizationActivity.showProgress(true);
        }

        @Override
        protected JSONObject doInBackground(JSONObject... JSONObjects) {
            Log.d(Const.TAG_LOG, "run doInBackground ");
            jsonResponse = JSONObjects[0];
          return jsonResponse;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            Log.d(Const.TAG_LOG, "run onPostExecute  jsonObject "+jsonObject.toString());
            inventorizationActivity.tvResponse.setText(jsonObject.toString());
            inventorizationActivity.showProgress(false);
        }

    }
// =============================GS==============================================================

    public boolean getIsWork() {
        return isWork;
    }

    public void setIsWork(boolean work) {
        isWork = work;
    }

}

