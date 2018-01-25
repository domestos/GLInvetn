package com.example.valerapelenskyi.glinvent;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.valerapelenskyi.glinvent.database.mysql.MySQLConnect;
import com.example.valerapelenskyi.glinvent.database.sqlite.SQLiteConnect;
import com.example.valerapelenskyi.glinvent.fragments.CheckFragment;
import com.example.valerapelenskyi.glinvent.fragments.DeviceDetailFragment;
import com.example.valerapelenskyi.glinvent.fragments.DevicesListFragment;
import com.example.valerapelenskyi.glinvent.fragments.ManageFragment;
import com.example.valerapelenskyi.glinvent.fragments.QRScannerFragment;
import com.example.valerapelenskyi.glinvent.fragments.QRScannerListRecyclerViewAdapter;
import com.example.valerapelenskyi.glinvent.fragments.SyncListFragment;
import com.example.valerapelenskyi.glinvent.model.Device;
import com.example.valerapelenskyi.glinvent.model.constants.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DevicesListFragment.OnListFragmentInteractionListener,
        CheckFragment.OnFragmentInteractionListener,
        ManageFragment.OnFragmentInteractionListener,
        SyncListFragment.OnListFragmentInteractionListenerSync,
        QRScannerFragment.OnListFragmentInteractionListenerQRScanner {

    private static final String TAG_MAFRAGMENT = "MAFragment";
    private DevicesListFragment devicesListFragment;
    private ManageFragment manageFragment;
    private CheckFragment checkFragment;
    private SyncListFragment syncListFragment;
    private QRScannerFragment qrScannerFragment;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadUrlHost();
        Log.d(Const.TAG_LOG, "onCreate: full URL " + Const.server_url);

        // ============================
        devicesListFragment = new DevicesListFragment();
        manageFragment = new ManageFragment();
        checkFragment = new CheckFragment();
        syncListFragment = new SyncListFragment();
        qrScannerFragment = new QRScannerFragment();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_qrScanner) {
            fragmentTransaction.replace(R.id.nav_conteiner, qrScannerFragment);
        } /*else if (id == R.id.nav_inventorization) {
            fragmentTransaction.replace(R.id.nav_conteiner, checkFragment);
        } */
        else if (id == R.id.nav_need_to_sync) {
            fragmentTransaction.replace(R.id.nav_conteiner, syncListFragment);
        } else if (id == R.id.nav_manage) {
            fragmentTransaction.replace(R.id.nav_conteiner, manageFragment);
        }

//        else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        fragmentTransaction.addToBackStack("device");
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //==============================================================================================

    @Override
    public void onListFragmentInteraction(Device device) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DeviceDetailFragment deviceDetailFragment = DeviceDetailFragment.newInstance(device);
        fragmentTransaction.replace(R.id.nav_conteiner, deviceDetailFragment);
        fragmentTransaction.addToBackStack("device");
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteractionQRScanner(Device device) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DeviceDetailFragment deviceDetailFragment = DeviceDetailFragment.newInstance(device);
        fragmentTransaction.replace(R.id.nav_conteiner, deviceDetailFragment);
        fragmentTransaction.addToBackStack("device");
        fragmentTransaction.commit();
    }

    @Override
    public boolean setStatusInventory(final Device device, final QRScannerListRecyclerViewAdapter.ViewHolder holder) {
        showProgres(holder, true);
        if (device != null) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.update_status_invent_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                            int responseSuccess = getSuccess(response);
                            if (responseSuccess != 0) {
                                // inset to SQLite SATATUS_ONLINE
                                SQLiteConnect.getInstance(MainActivity.this).updateStatusInvent(device.getId(), Const.STATUS_SYNC_ONLINE);
                                Toast.makeText(MainActivity.this, "MYSQL and SQLite are Success ", Toast.LENGTH_LONG).show();

                                //update view item in list
                                device.setStatusInvent(Const.STATUS_FINED);
                                device.setStatusSync(Const.STATUS_SYNC_ONLINE);
                                holder.checkInventoryStatus(device);
                            }
                            showProgres(holder, false);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "MYSQL insert ERROR " + error.getMessage(), Toast.LENGTH_LONG).show();
                            SQLiteConnect.getInstance(MainActivity.this).updateStatusInvent(device.getId(), Const.STATUS_SYNC_OFFLINE);

                            //update view item in list
                            device.setStatusInvent(Const.STATUS_FINED);
                            device.setStatusSync(Const.STATUS_SYNC_OFFLINE);
                            holder.checkInventoryStatus(device);
                            showProgres(holder, false);

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    Log.d(Const.TAG_LOG, "getParams: id = " + device.getId());
                    params.put("id", String.valueOf(device.getId()));
                    params.put("method", "method_fined");
                    params.put("status_invent", Const.STATUS_FINED);
                    return params;
                }
            };
            MySQLConnect.getInstance(this).addToRequestque(stringRequest);
        }

        return true;
    }

    private void showProgres(QRScannerListRecyclerViewAdapter.ViewHolder holder, boolean show) {
        holder.btnOK.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        holder.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);

    }

    private int getSuccess(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            Log.d(Const.TAG_LOG, "getSuccess: " + jsonObject.get("success"));
            return (Integer) jsonObject.get("success");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onListFragmentInteractionSync(Device device) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void saveUrlHost(String text) {
        Log.d(Const.TAG_LOG, "saveUrlHost run: " + text);
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sharedPreferences.edit();
        spEdit.putString("URL", text);
        spEdit.commit();
    }


    public String loadUrlHost() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        Const.url_host = sharedPreferences.getString("URL", "");
        Const.concatUrl(Const.url_host);
        return Const.url_host;
    }


}