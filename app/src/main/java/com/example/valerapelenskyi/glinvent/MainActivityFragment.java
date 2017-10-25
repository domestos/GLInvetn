package com.example.valerapelenskyi.glinvent;

import android.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by valera.pelenskyi on 24.10.17.
 */

public class MainActivityFragment extends Fragment{

    private MainActivity activity;
    private MAFAsyncTask mafAsyncTask;
    private final String TAG_LOG = "TAG_LOG";


    public MainActivity linkToActivity(MainActivity mainActivity) {
        return this.activity = mainActivity;
    }


//======================== NEW CLASS ===================================
    class MAFAsyncTask extends AsyncTask<String, String ,String>{


        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG_LOG, "run MAFAsyncTask doInBackground");
            return null;
        }
    }//end class MAFAsyncTask

}//end class MainActivityFragment
