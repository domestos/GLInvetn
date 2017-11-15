package com.example.valerapelenskyi.glinvent.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.valerapelenskyi.glinvent.R;
import com.example.valerapelenskyi.glinvent.model.Device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String[] arrayLocation = {"", "QA Red", "Administration", "BB", "Meeting Room", "QA Black", "QA Green", "QA White", "SMU", "Server Room", "Test room", "Training Room", "WAA", "Warehouse"};

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
        TextView tvNumber = view.findViewById(R.id.tvNumber);
        TextView tvItem = view.findViewById(R.id.tvItem);
        EditText edOwner = view.findViewById(R.id.edOwner);
        //EditText edLocation = view.findViewById(R.id.edLocation);
        EditText edDescription = view.findViewById(R.id.edDescription);
        Spinner spLocation = (Spinner) view.findViewById(R.id.spLocation);


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
}
