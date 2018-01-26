package com.example.valerapelenskyi.glinvent.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.example.valerapelenskyi.glinvent.model.User;
import com.example.valerapelenskyi.glinvent.model.constants.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceDetailFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String[] arrayLocation = {"", "QA Red", "Administration", "BB", "Meeting Room", "QA Amber", "QA Black", "QA Green", "QA White", "SMU", "Server Room", "Test room", "Training Room", "WAA", "Warehouse"};
    private TextView tvNumber;
    private ProgressBar progressBar;
    private TextView tvItem;
    private EditText edOwner;
    private EditText edDescription;
    private Spinner spLocation;
    private Button btnSave;
    private ListView listSearchUser;
    private ArrayAdapter<String> adapterUsers;
    private static final String ARG_DEVICE = "device";
    private static final String ARG_USERS = "users";
    private String[] arrayUsers;
    private List<User> allUsers;

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
        allUsers  = (ArrayList<User>) SQLiteConnect.getInstance(getContext()).getAllUsersFromSQLite();
        arrayUsers = getArrayFtomList(allUsers);
        Log.d(Const.TAG_LOG, "onCreate: " +allUsers.size());
        Log.d(Const.TAG_LOG, "onCreate: " +arrayUsers.length);
        if (getArguments() != null) {
            mDevice = getArguments().getParcelable(ARG_DEVICE);
        }
    }

    private String[] getArrayFtomList(List<User> allUsers) {
        String[] users = new String[allUsers.size()];
        for (int i=0; allUsers.size() > i;i++){
            users[i]  = allUsers.get(i).getName().toString();
           // Log.d(Const.TAG_LOG, "getArrayFtomList: "+allUsers.get(i).getName().toString());
        }
        return users  ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_detail_device, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        listSearchUser = view.findViewById(R.id.listSearchUsers);
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

        if(arrayUsers == null){
            arrayUsers[0] = "none";
        }
        adapterUsers = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayUsers);


        listSearchUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(Const.TAG_LOG, "onItemClick: "+adapterView.getItemAtPosition(i));
                view.setSelected(true);
                edOwner.setText(""+adapterView.getItemAtPosition(i));
            }
        });

        listSearchUser.setAdapter(adapterUsers);

        edOwner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapterUsers.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }






        });


        ArrayAdapter<String> adapterLocations = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayLocation);
        adapterLocations.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spLocation.setAdapter(adapterLocations);

        setSelectItem(spLocation, mDevice);

        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             //  Toast.makeText(getContext(), adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                mDevice.setLocation(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }




        });

        showProgress(false);
        return view;
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
       //     Log.d("TAG_location", localtion + " = " + arrayLocation[i]);
            i++;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                mDevice.setOwner(edOwner.getText().toString());
                mDevice.setDescription(edDescription.getText().toString());
                Log.d(Const.TAG_LOG, "onClick: location = " + mDevice.getLocation() + " owner = " + mDevice.getOwner());
                editItem(mDevice);
                break;
        }
    }

    public void showProgress(boolean show) {
        btnSave.setEnabled(!show);
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void editItem(final Device mDevice) {
        if (mDevice != null) {
            showProgress(true);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.update_item,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(), response.toString(), LENGTH_LONG).show();
                            int responseSuccess = getSuccess(response);
                            if (responseSuccess != 0) {
                                // inset to SQLite SATATUS_ONLINE
                                SQLiteConnect.getInstance(getContext()).updateItem(mDevice, Const.STATUS_SYNC_ONLINE);
                            }
                            showProgress(false);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "MYSQL ERROR " + error.getMessage(), LENGTH_LONG).show();
                            SQLiteConnect.getInstance(getContext()).updateItem(mDevice, Const.STATUS_SYNC_OFFLINE);
                            showProgress(false);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
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
            Log.d(Const.TAG_LOG, "getSuccess: " + jsonObject.get("success"));
            return (Integer) jsonObject.get("success");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
