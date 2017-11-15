package com.example.valerapelenskyi.glinvent.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.valerapelenskyi.glinvent.R;
import com.example.valerapelenskyi.glinvent.database.mysql.MySQLConnect;
import com.example.valerapelenskyi.glinvent.database.sqlite.SQLiteConnect;
import com.example.valerapelenskyi.glinvent.model.Device;
import com.example.valerapelenskyi.glinvent.model.constants.Const;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListenerQRScanner}
 * interface.
 */
public class QRScannerFragment extends Fragment implements View.OnClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private SyncListRecyclerViewAdapter syncAdapter;
    private EditText etNumber;
    private Button btnSearch;
    private Button btnScan;
    private int mColumnCount = 1;
    private OnListFragmentInteractionListenerQRScanner mListener;
    private Button btnSyncAll;
    private Device device;
    private List<Device> devices;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QRScannerFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static QRScannerFragment newInstance(int columnCount) {
        QRScannerFragment fragment = new QRScannerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_scanner, container, false);

        // Set the adapter
        if (view.findViewById(R.id.list) instanceof RecyclerView) {
            etNumber = view.findViewById(R.id.etNumber);
            btnSearch = view.findViewById(R.id.btnSearch);
            btnScan = view.findViewById(R.id.btnScan);
            recyclerView = view.findViewById(R.id.list);
            final Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            btnScan.setOnClickListener(this);
            btnSearch.setOnClickListener(this);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListenerQRScanner) {
            mListener = (OnListFragmentInteractionListenerQRScanner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnScan:
                IntentIntegrator integrator = new IntentIntegrator(this.getActivity()) {
                    @Override
                    protected void startActivityForResult(Intent integrator, int code) {
                        QRScannerFragment.this.startActivityForResult(integrator, 312); // REQUEST_CODE override
                    }
                };
                integrator.initiateScan();
                break;
            case R.id.btnSearch:
                findNumber();
                break;

        }

    }

    private void findNumber() {
        device = SQLiteConnect.getInstance(getContext()).getItemFromSQLite(etNumber.getText().toString());
        if (device != null) {
            if (devices == null) {
                devices = new ArrayList<Device>();
            }
            devices.clear();
            devices.add(device);
            recyclerView.setAdapter(new QRScannerListRecyclerViewAdapter(devices, mListener));

        } else {
            Toast.makeText(getContext(), "No find", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            etNumber.setText(data.getStringExtra("SCAN_RESULT"));
            Log.d(TAG, "onActivityResult: Fragment  requestCode =" + requestCode + " resulte " + data.getStringExtra("SCAN_RESULT"));
            findNumber();
        } else {
            Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListenerQRScanner {
        // TODO: Update argument type and name
        void onListFragmentInteractionQRScanner(Device device);
    }


    private List<Device> getNoSyncItems() {
        Log.d(Const.TAG_LOG, "run getNoSyncItems ");
        devices = SQLiteConnect.getInstance(getContext()).getNoSyncItemsFromSQLite();

        if (devices == null) {
            //getContext().tvResponse.setText("SQLite база пуста. Скопіювати базу з MYSQL ?");
            Toast.makeText(getContext(), "SQLite => Tabele isEmpty", Toast.LENGTH_SHORT).show();
            //  return null;
            //     copyDataFromMySQLtoSQLite();

        }
        return devices;
    }

    private ArrayList<Device> getArrayDevices(JSONObject response) {
        ArrayList<Device> devices = new ArrayList<Device>();
        try {
            if (response.get("success").equals(1)) {
                JSONArray products = (JSONArray) response.get("products");
                for (int i = 0; i < products.length(); i++) {
                    JSONObject JO = (JSONObject) products.get(i);
                    devices.add(new Device(
                            JO.getInt("id"),
                            JO.getString("number"),
                            JO.getString("item"),
                            JO.getString("name_wks"),
                            JO.getString("owner"),
                            JO.getString("location"),
                            JO.getString("status_invent"),
                            JO.getInt("status_sync"),
                            JO.getString("description")
                    ));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return devices;
    }

    private void updateItem(final Device device, final RecyclerView.Adapter adapter) {
        if (device != null) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.update_status_invent_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //   Toast.makeText(getActivity(),device.getNumber()+response.toString(),Toast.LENGTH_SHORT).show();

                            int responseSuccess = getSuccess(response);
                            if (responseSuccess != 0) {
                                // inset to SQLite SATATUS_ONLINE
                                SQLiteConnect.getInstance(getContext()).updateStatusInvent(device.getId(), Const.STATUS_SYNC_ONLINE);
                                syncAdapter.setDevices(getNoSyncItems());
                                adapter.notifyDataSetChanged();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "ERROR " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", String.valueOf(device.getId()));
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
            Log.d(TAG, "getSuccess: " + jsonObject.get("success"));
            return (Integer) jsonObject.get("success");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
