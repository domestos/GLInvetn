package com.example.valerapelenskyi.glinvent.inventorization;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.valerapelenskyi.glinvent.R;

public class InventorizationActivity extends AppCompatActivity {

    private static final String TAG_FRAGMENT = "iventFragment";
    private InventorizationActivityFragment inventFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventorization);

        inventFragment = getInventorizationFragment();
        inventFragment.linkToActivity(this);

    }

    public InventorizationActivityFragment getInventorizationFragment() {
        inventFragment = (InventorizationActivityFragment) getFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if(inventFragment == null){
            inventFragment = new InventorizationActivityFragment();
            getFragmentManager().beginTransaction().add(inventFragment, TAG_FRAGMENT).commit();
        }

        return inventFragment;
    }
}
