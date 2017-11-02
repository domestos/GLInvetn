package com.example.valerapelenskyi.glinvent.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.valerapelenskyi.glinvent.R;
import com.example.valerapelenskyi.glinvent.model.Device;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
        EditText edLocation = view.findViewById(R.id.edLocation);
        EditText edDescription = view.findViewById(R.id.edDescription);
        tvNumber.setText(mDevice.getNumber());
        tvItem.setText(mDevice.getItem());
        edOwner.setText(mDevice.getOwner());
        edLocation.setText(mDevice.getLocation());
        edDescription.setText(mDevice.getDescription());
        return  view;
    }

}
