package com.example.valerapelenskyi.glinvent.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckFragment extends Fragment implements View.OnClickListener {
    private  Device device;
    private Button btnScan;
    private Button btnSetChecked;
    private Button btnSearch;
    private TextView tvNumber;
    private TextView etNumber;
    private TextView tvItem;
    private TextView tvSQLite;
    private TextView tvMySQL;
    private TextView tvOwner;
    private TextView tvLocation;
    private LinearLayout linearLayout;
    private String TAG = "TAG_LOG";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final int STATUS_SYNC_ONLINE = 0;
    private static final int STATUS_SYNC_OFFLINE = 1;
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

        linearLayout = view.findViewById(R.id.id_fined_details);
        linearLayout.setVisibility(View.INVISIBLE);

        tvNumber = view.findViewById(R.id.tvNumber);
        etNumber = view.findViewById(R.id.etNumber);
        tvOwner = view.findViewById(R.id.tvOwner);
        tvItem = view.findViewById(R.id.tvItem);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvSQLite = view.findViewById(R.id.tvSQLite);
        tvMySQL = view.findViewById(R.id.tvMySQL);
        tvSQLite.setTextColor(Color.BLACK);
        tvMySQL.setTextColor(Color.BLACK);

        btnScan = view.findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);

        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        btnSetChecked = view.findViewById(R.id.btnSetChecked );
        btnSetChecked.setOnClickListener(this);
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
                IntentIntegrator scanIntegrator = new IntentIntegrator(this.getActivity()) {
                    @Override
                    protected void startActivityForResult(Intent integrator, int code) {
                        CheckFragment.this.startActivityForResult(integrator, 312); // REQUEST_CODE override
                    }
                };
                scanIntegrator.initiateScan();
                break;
            case R.id.btnSearch:
                findNumber();
                break;
            case R.id.btnSetChecked:
                    updateItem(device);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        etNumber.setText( data.getStringExtra("SCAN_RESULT"));
        Log.d(TAG, "onActivityResult: Fragment  requestCode ="+requestCode+" resulte " +data.getStringExtra("SCAN_RESULT"));
        findNumber();
    }

    private void findNumber() {
        device = null;
        tvSQLite.setTextColor(Color.BLACK);
        tvMySQL.setTextColor(Color.BLACK);

        Log.d(TAG, "onClick: tvNumber = "+etNumber.getText().toString());

        device = SQLiteConnect.getInstance(getContext()).getItemFromSQLite(etNumber.getText().toString());
        if(device !=null) {
            linearLayout.setVisibility(View.VISIBLE);
            tvNumber.setText(device.getNumber());
            tvItem.setText(device.getItem());
            tvOwner.setText(device.getOwner());
            tvLocation.setText(device.getLocation());

            if(!device.getStatusInvent().isEmpty()){
                btnSetChecked.setVisibility(View.INVISIBLE);
            }else {
                btnSetChecked.setVisibility(View.VISIBLE);
            }

            if(device.getStatusInvent().equals("ok")){
                tvSQLite.setTextColor(Color.GREEN);
            }
            if(device.getStatusSync() == STATUS_SYNC_ONLINE && device.getStatusInvent().equals("ok") ){
                tvMySQL.setTextColor(Color.GREEN);
            }

            if(device.getStatusSync() == STATUS_SYNC_OFFLINE && device.getStatusInvent().equals("ok") ){
                tvMySQL.setTextColor(Color.RED);
            }
        }else {
            linearLayout.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "NOT FOUND. Result null", Toast.LENGTH_LONG).show();
        }

    }


    private void updateItem(final Device device1) {
        Log.d(TAG, "updateItem: ");
        if(device != null) {
            StringRequest  stringRequest = new StringRequest (Request.Method.POST, Const.update_status_invent_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                            int responseSuccess = getSuccess(response);
                            if(responseSuccess !=0){
                                // inset to SQLite SATATUS_ONLINE
                                SQLiteConnect.getInstance(getContext()).updateStatusInvent(device.getId(),STATUS_SYNC_ONLINE);
                            }
                            tvMySQL.setTextColor(Color.GREEN);
                            tvSQLite.setTextColor(Color.GREEN);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(),"ERROR "+error.getMessage(),Toast.LENGTH_LONG).show();
                            SQLiteConnect.getInstance(getContext()).updateStatusInvent(device.getId(),STATUS_SYNC_OFFLINE);
                            tvMySQL.setTextColor(Color.RED);
                            tvSQLite.setTextColor(Color.GREEN);
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params  = new HashMap<String, String>();
                    Log.d(TAG, "getParams: id = "+device1.getId());
                    params.put("id", String.valueOf(device1.getId()));
                    return params;
                }
            };

            MySQLConnect.getInstance(getContext()).addToRequestque(stringRequest);
        }

    }

    private int getSuccess(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            Log.d(TAG, "getSuccess: "+ jsonObject.get("success") );
            return (Integer) jsonObject.get("success") ;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Device findDevice(String number) {
        //return devise or null
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
