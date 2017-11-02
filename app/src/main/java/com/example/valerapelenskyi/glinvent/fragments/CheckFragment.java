package com.example.valerapelenskyi.glinvent.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.valerapelenskyi.glinvent.R;
import com.example.valerapelenskyi.glinvent.database.sqlite.SQLiteConnect;
import com.example.valerapelenskyi.glinvent.model.Device;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckFragment extends Fragment implements View.OnClickListener {

    private Button btnScan;
    private Button btnSetChecked;
    private TextView tvNumber;
    private TextView etNumber;
    private TextView tvItem;
    private TextView tvOwner;
    private TextView tvLocation;
    private String TAG = "TAG_LOG";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CheckFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckFragment newInstance(String param1, String param2) {
        CheckFragment fragment = new CheckFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check, container, false);

        tvNumber = view.findViewById(R.id.tvNumber);
        etNumber = view.findViewById(R.id.etNumber);
        tvOwner = view.findViewById(R.id.tvOwner);
        tvItem = view.findViewById(R.id.tvItem);
        tvLocation = view.findViewById(R.id.tvLocation);

        btnScan = view.findViewById(R.id.btnScan);
        btnSetChecked = view.findViewById(R.id.btnSetChecked );
        btnSetChecked.setOnClickListener(this);
        btnScan.setOnClickListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScan:

            // tvUpdate.setText(mainActivityFragment.getURLRequest());
                IntentIntegrator scanIntegrator = new IntentIntegrator(this.getActivity());
                scanIntegrator.setTitle("GAMELOFT");
                scanIntegrator.initiateScan();



                break;
            case R.id.btnSetChecked:


                break;
        }
    }

    public Device findDevice(String number) {
        return SQLiteConnect.getInstance(getContext()).getItemFromSQLite(number);
    }


    public TextView getTvNumber() {
        return tvNumber;
    }

    public void setTvNumber(TextView tvNumber) {
        this.tvNumber = tvNumber;
    }

    public TextView getEtNumber() {
        return etNumber;
    }

    public void setEtNumber(TextView etNumber) {
        this.etNumber = etNumber;
    }

    public TextView getTvItem() {
        return tvItem;
    }

    public void setTvItem(TextView tvItem) {
        this.tvItem = tvItem;
    }

    public TextView getTvOwner() {
        return tvOwner;
    }

    public void setTvOwner(TextView tvOwner) {
        this.tvOwner = tvOwner;
    }

    public TextView getTvLocation() {
        return tvLocation;
    }

    public void setTvLocation(TextView tvLocation) {
        this.tvLocation = tvLocation;
    }
}
