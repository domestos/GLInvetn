package com.example.valerapelenskyi.glinvent.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.valerapelenskyi.glinvent.R;
import com.example.valerapelenskyi.glinvent.database.mysql.MySQLConnect;
import com.example.valerapelenskyi.glinvent.database.sqlite.SQLiteConnect;
import com.example.valerapelenskyi.glinvent.model.Device;
import com.example.valerapelenskyi.glinvent.model.constants.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceDetailFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String[] arrayLocation = {"", "QA Red", "Administration", "BB", "Meeting Room", "QA Black", "QA Green", "QA White", "SMU", "Server Room", "Test room", "Training Room", "WAA", "Warehouse"};
    private  TextView tvNumber;
    private TextView tvItem;
    private EditText edOwner;
    private EditText edDescription;
    private Spinner spLocation;
    private Button btnSave;
    private static final String ARG_DEVICE = "device";

    // TODO: Rename and change types of parameters
    private Device mDevice;


    public DeviceDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mDevice Device
     * @return A new instance of fragment DeviceDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceDetailFragment newInstance(Device mDevice) {
        DeviceDetailFragment fragment = new DeviceDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DEVICE, mDevice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDevice = getArguments().getParcelable(ARG_DEVICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_device, container, false);
       tvNumber = view.findViewById(R.id.tvNumber);
         tvItem = view.findViewById(R.id.tvItem);
         edOwner = view.findViewById(R.id.edOwner);
        //EditText edLocation = view.findViewById(R.id.edLocation);
         edDescription = view.findViewById(R.id.edDescription);
         spLocation = (Spinner) view.findViewById(R.id.spLocation);
         btnSave = (Button) view.findViewById(R.id.btnSave);


        btnSave.setOnClickListener(this);
        tvNumber.setText(mDevice.getNumber());
        tvItem.setText(mDevice.getItem());
        edOwner.setText(mDevice.getOwner());
       // edLocation.setText(mDevice.getLocation());
        edDescription.setText(mDevice.getDescription());


        ArrayAdapter<String> locations = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayLocation);
        locations.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spLocation.setAdapter(locations);


        setSelectItem(spLocation, mDevice);
        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                mDevice.setLocation(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return  view;
    }
    private void setSelectItem(Spinner spLocation, Device mDevice) {
            Log.d("TAG_location", "select Item");
            mDevice.getLocation();

            int i = 0;
            String localtion;
            String chek;
            while (arrayLocation.length > i) {
                localtion = mDevice.getLocation();
                chek = arrayLocation[i];

                if (localtion.equals(chek)) {
                    spLocation.setSelection(i);
                }
                Log.d("TAG_location", localtion + " = " + arrayLocation[i]);
                i++;
            }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSave:
             mDevice.setOwner(edOwner.getText().toString());
             mDevice.setDescription(edDescription.getText().toString());
                Log.d(Const.TAG_LOG, "onClick: location = "+mDevice.getLocation()+" owner = "+mDevice.getOwner());
             editItem(mDevice);
            break;

        }
    }

    private void editItem(final Device mDevice) {
        if(mDevice != null) {
            StringRequest stringRequest = new StringRequest (Request.Method.POST, Const.update_item,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                            int responseSuccess = getSuccess(response);
                            if(responseSuccess !=0){
                                // inset to SQLite SATATUS_ONLINE
                                SQLiteConnect.getInstance(getContext()).updateItem(mDevice,Const.STATUS_SYNC_ONLINE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(),"ERROR "+error.getMessage(),Toast.LENGTH_LONG).show();
                            SQLiteConnect.getInstance(getContext()).updateItem(mDevice,Const.STATUS_SYNC_OFFLINE);
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params  = new HashMap<String, String>();
                    params.put("id", String.valueOf(mDevice.getId()));
                    params.put("owner", mDevice.getOwner());
                    params.put("location", mDevice.getLocation());
                    params.put("description", mDevice.getDescription());
                    return params;
                }
            };

            MySQLConnect.getInstance(getContext()).addToRequestque(stringRequest);
        }

    }




    private void updateItem(final Device device1) {

    }

    private int getSuccess(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            Log.d(Const.TAG_LOG, "getSuccess: "+ jsonObject.get("success") );
            return (Integer) jsonObject.get("success") ;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
