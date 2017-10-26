package com.example.valerapelenskyi.glinvent.activity.inventorization;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.valerapelenskyi.glinvent.R;
import com.example.valerapelenskyi.glinvent.database.ControllerDB;
import com.example.valerapelenskyi.glinvent.database.mysql.MySQLConnect;
import com.example.valerapelenskyi.glinvent.database.sqlite.DBHelper;
import com.example.valerapelenskyi.glinvent.model.constants.Const;


import org.json.JSONObject;

public class InventorizationActivity extends AppCompatActivity {

    private static final String TAG_FRAGMENT = "iventFragment";
    private ProgressBar progressBar;
    private InventorizationActivityFragment inventFragment;
    private Button btnTestConnect;
    public TextView tvResponse;



    // =================================== SQLite ====================================================


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventorization);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnTestConnect = (Button) findViewById(R.id.btnTestConnect);
        tvResponse  = (TextView) findViewById(R.id.tvResponse);
        btnTestConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getStringResponse();
                getJsonObjectRequest();
            }
        });

        inventFragment = getInventorizationFragment();
        inventFragment.linkToActivity(this);
        inventFragment.runCopyDB();
        showProgress(false);
    }

    // Fragment запускає цей метод (показує стан AsyncTasks)
    public void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public InventorizationActivityFragment getInventorizationFragment() {
        inventFragment = (InventorizationActivityFragment) getFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if(inventFragment == null){
            inventFragment = new InventorizationActivityFragment();
            getFragmentManager().beginTransaction().add(inventFragment, TAG_FRAGMENT).commit();
        }

        return inventFragment;
    }

    // =================================== Volley ====================================================
    public Object getJsonObjectRequest() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Const.server_url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        tvResponse.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvResponse.setText(error.getMessage());
                    }
                }
        );

        MySQLConnect.getInstance(getApplicationContext()).addToRequestque(jsonObjectRequest);
        return jsonObjectRequest;
    }

    private void getStringResponse() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                tvResponse.setText(response);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvResponse.setText("Erorr .... "+error.getMessage());
                error.printStackTrace();
            }
        });

        MySQLConnect.getInstance(getApplicationContext()).addToRequestque(stringRequest);
    }

    // =================================== SQLite ====================================================


}
