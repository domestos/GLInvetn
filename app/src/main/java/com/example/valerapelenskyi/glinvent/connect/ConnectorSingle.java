package com.example.valerapelenskyi.glinvent.connect;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by valera.pelenskyi on 25.10.17.
 * Singelton class
 */

public class ConnectorSingle {

    private static ConnectorSingle connectorSingle;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private ConnectorSingle(Context context){
        mCtx = context;
        requestQueue = getFequestQue();

    }


    public RequestQueue getFequestQue(){

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return  requestQueue;
    }

    public static  synchronized ConnectorSingle getInstance(Context context){
        if(connectorSingle == null){
            connectorSingle = new ConnectorSingle(context);
        }
            return connectorSingle;
    }

    public<T> void  addToRequestque(Request<T> request) {
        requestQueue.add(request);
    }
}
