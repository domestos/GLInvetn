package com.example.valerapelenskyi.glinvent.inventorization;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by valera.pelenskyi on 24.10.17.
 */

public class InventorizationActivityFragment  extends Fragment{

    private InventorizationActivity inventorizationActivity;

    public InventorizationActivity linkToActivity(InventorizationActivity inventorizationActivity) {
        return this.inventorizationActivity = inventorizationActivity;
    }

    //======================== NEW CLASS ========================
    class IAFragment extends AsyncTask<String, String,String>{
        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
}

