package com.example.valerapelenskyi.glinvent;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.valerapelenskyi.glinvent.fragments.CheckFragment;
import com.example.valerapelenskyi.glinvent.fragments.DeviceDetailFragment;
import com.example.valerapelenskyi.glinvent.fragments.DevicesListFragment;
import com.example.valerapelenskyi.glinvent.fragments.ManageFragment;
import com.example.valerapelenskyi.glinvent.fragments.QRScannerFragment;
import com.example.valerapelenskyi.glinvent.fragments.SyncListFragment;
import com.example.valerapelenskyi.glinvent.model.Device;
import com.example.valerapelenskyi.glinvent.model.constants.Const;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DevicesListFragment.OnListFragmentInteractionListener,
        CheckFragment.OnFragmentInteractionListener,
        ManageFragment.OnFragmentInteractionListener,
        SyncListFragment.OnListFragmentInteractionListenerSync,
        QRScannerFragment.OnListFragmentInteractionListenerQRScanner{

    private static final String TAG_MAFRAGMENT = "MAFragment";
    public MainActivityFragment maFragment ;
    private DevicesListFragment devicesListFragment;
    private ManageFragment manageFragment;
    private CheckFragment checkFragment;
    private SyncListFragment syncListFragment;
    private QRScannerFragment qrScannerFragment;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Const.TAG_LOG, "onCreate: loadUrlHost()="+loadUrlHost()+" full URL "+Const.server_url);
        loadUrlHost();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ====init Fragment===========
        maFragment = getMAFragment();
        maFragment.linkToActivity(this);
        // ============================
        devicesListFragment =  new DevicesListFragment();
        manageFragment =new ManageFragment();
        checkFragment = new CheckFragment();
        syncListFragment = new SyncListFragment();
        qrScannerFragment = new QRScannerFragment();


        prepareApp();

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
        } else if (id == R.id.nav_inventorization) {
            fragmentTransaction.replace(R.id.nav_conteiner, checkFragment);
        } else if (id == R.id.nav_need_to_sync) {
            fragmentTransaction.replace(R.id.nav_conteiner, syncListFragment);
        } else if (id == R.id.nav_manage) {
            fragmentTransaction.replace(R.id.nav_conteiner, manageFragment);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        fragmentTransaction.addToBackStack("device");
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //==============================================================================================
    public MainActivityFragment getMAFragment() {
        maFragment = (MainActivityFragment) getFragmentManager().findFragmentByTag(TAG_MAFRAGMENT);
        if(maFragment == null){
            maFragment = new MainActivityFragment();
            getFragmentManager().beginTransaction().add(maFragment, TAG_MAFRAGMENT).commit();
         }
        return maFragment;
    }

    @Override
    public void onListFragmentInteraction(Device device) {
        FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
        DeviceDetailFragment deviceDetailFragment = DeviceDetailFragment.newInstance(device);
        fragmentTransaction.replace(R.id.nav_conteiner, deviceDetailFragment);
        fragmentTransaction.addToBackStack("device");
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteractionQRScanner(Device device) {
        FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
        DeviceDetailFragment deviceDetailFragment = DeviceDetailFragment.newInstance(device);
        fragmentTransaction.replace(R.id.nav_conteiner, deviceDetailFragment);
        fragmentTransaction.addToBackStack("device");
        fragmentTransaction.commit();
        }

    @Override
    public void onListFragmentInteractionSync(Device device) {

            }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

        @Override
        public void saveUrlHost(String  text) {
            Log.d(Const.TAG_LOG, "saveUrlHost run: "+text);
            sharedPreferences  =  getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor spEdit =  sharedPreferences.edit();
            spEdit.putString("URL", text);
            spEdit.commit();
        }


    public String loadUrlHost(){
        sharedPreferences = getPreferences(MODE_PRIVATE);
        Const.url_host = sharedPreferences.getString("URL","");
        Const.concatUrl(Const.url_host);
        return Const.url_host;
    }

// ==================================== preparation of APP =========================================
// Get the results From QRScan
// THIS METHOD COMUNICATION WITH CHECKFRAGMENT
//
//@Override
//public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//       super.onActivityResult(requestCode,resultCode,data);
//    if (result != null) {
//        if (result.getContents() == null) {
//            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
//        } else {
//            // INSERT RESULT INTO Device of CheckFragment.class
//            Device device = checkFragment.findDevice(result.getContents());
//
//            if(device!=null) {
//                checkFragment.setDevice(device);
//
//                checkFragment.getEtNumber().setText(checkFragment.getDevice().getNumber());
//                checkFragment.getTvNumber().setText(checkFragment.getDevice().getNumber());
//                checkFragment.getTvItem().setText(checkFragment.getDevice().getItem());
//                checkFragment.getTvOwner().setText(checkFragment.getDevice().getOwner());
//                checkFragment.getTvLocation().setText(checkFragment.getDevice().getLocation());
//            }else{
//                Toast.makeText(this, "NO FOUND OR DATA BASE IS EMPTY", Toast.LENGTH_LONG).show();
//            }
//
//         //   etNumber.setText(result.getContents());
//            // connectToURLFragment.startGetJSON(Const.URL_ADDRESS + "'" + etInventNumber.getText().toString() + "'");
//
//           Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//        }
//    } else {
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//}

    private void prepareApp() {

        // 1) need check URL
        // 2) check avaible URL
        // 3) get all Items From MYSQL
        // 4) get all Items From SQLite
        // 5) compare counts bouth DB
        // 6) check statusSync if need to do sync, check the dateUpdate




    }


        }