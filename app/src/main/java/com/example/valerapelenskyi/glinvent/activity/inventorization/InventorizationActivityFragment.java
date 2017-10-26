package com.example.valerapelenskyi.glinvent.activity.inventorization;

import android.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.valerapelenskyi.glinvent.database.ControllerDB;
import com.example.valerapelenskyi.glinvent.database.mysql.MySQLConnect;
import com.example.valerapelenskyi.glinvent.model.constants.Const;

import org.json.JSONObject;

/**
 * Created by valera.pelenskyi on 24.10.17.
 */

public class InventorizationActivityFragment  extends Fragment{



    private JSONObject jsonResponse;
    private boolean isWork = false; //інформує актівіті про стан AsyncTask

    private InventorizationActivity inventorizationActivity;
    public InventorizationActivity linkToActivity(InventorizationActivity inventorizationActivity) {
       return this.inventorizationActivity = inventorizationActivity;
    }



    public void runCopyDB() {
         ControllerDB  controllerDB = new ControllerDB(inventorizationActivity.getApplicationContext());
        Log.d(Const.TAG_LOG, "run runCopyDB ");
        jsonResponse =  controllerDB.getJsonObjectRequest();
        if(jsonResponse !=null){inventorizationActivity.tvResponse.setText(jsonResponse.toString());}else{
            inventorizationActivity.tvResponse.setText("null");
        }
        //new CopyDB().execute(jsonResponse);
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






}

