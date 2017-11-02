package com.example.valerapelenskyi.glinvent;


import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.valerapelenskyi.glinvent.fragments.CheckFragment;
import com.example.valerapelenskyi.glinvent.fragments.DeviceDetailFragment;
import com.example.valerapelenskyi.glinvent.fragments.DevicesListFragment;
import com.example.valerapelenskyi.glinvent.fragments.ManageFragment;
import com.example.valerapelenskyi.glinvent.model.Device;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DevicesListFragment.OnListFragmentInteractionListener,
        CheckFragment.OnFragmentInteractionListener,
        ManageFragment.OnFragmentInteractionListener {

    private static final String TAG_MAFRAGMENT = "MAFragment";
    public MainActivityFragment maFragment ;
    private DevicesListFragment devicesListFragment;
    private ManageFragment manageFragment;
    private CheckFragment checkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        prepareApp();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

            fragmentTransaction.replace(R.id.nav_conteiner, devicesListFragment);

        } else if (id == R.id.nav_inventorization) {

            fragmentTransaction.replace(R.id.nav_conteiner, checkFragment);
//            opent new Intent
//            Intent intent = new Intent(this, InventorizationActivity.class);
//            startActivity(intent);

        } else if (id == R.id.nav_need_to_sync) {
            // opent new Intent

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
    public void onFragmentInteraction(Uri uri) {

    }


// ==================================== preparation of APP =========================================
// Get the results From QRScan
// THIS METHOD COMUNICATION WITH CHECKFRAGMENT

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode,resultCode,data);
    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    if (result != null) {
        if (result.getContents() == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            Device device =  checkFragment.findDevice(result.getContents());

            checkFragment.getEtNumber().setText(result.getContents().toString());
            checkFragment.getTvNumber().setText(result.getContents().toString());

            checkFragment.getTvItem().setText(device.getItem());
           checkFragment.getTvOwner().setText(device.getOwner());
            checkFragment.getTvLocation().setText(device.getLocation());


         //   etNumber.setText(result.getContents());
            // connectToURLFragment.startGetJSON(Const.URL_ADDRESS + "'" + etInventNumber.getText().toString() + "'");

           Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
        }
    } else {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

    private void prepareApp() {

        // 1) need check URL
        // 2) check avaible URL
        // 3) get all Items From MYSQL
        // 4) get all Items From SQLite
        // 5) compare counts bouth DB
        // 6) check statusSync if need to do sync, check the dateUpdate




    }
}
