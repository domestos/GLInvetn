package com.example.valerapelenskyi.glinvent.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.valerapelenskyi.glinvent.model.constants.Const.STATUS_SYNC_OFFLINE;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListenerSync}
 * interface.
 */
public class SyncListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private SyncListRecyclerViewAdapter syncAdapter;
    private int mColumnCount = 1;
    private OnListFragmentInteractionListenerSync mListener;
    private Button btnSyncAll;
    private List<Device> devices ;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SyncListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SyncListFragment newInstance(int columnCount) {
        SyncListFragment fragment = new SyncListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showListView();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view.findViewById(R.id.list) instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            syncAdapter = new SyncListRecyclerViewAdapter(getNoSyncItems(), mListener);
            recyclerView.setAdapter(syncAdapter);


            btnSyncAll = view.findViewById(R.id.btnSyncAll);
            btnSyncAll.setText("SYNC ALL ( "+getNoSyncItems().size()+" )");
            btnSyncAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    syncAll(getNoSyncItems(), recyclerView.getAdapter() );

                }
            });
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListenerSync) {
            mListener = (OnListFragmentInteractionListenerSync) context;
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
    public interface OnListFragmentInteractionListenerSync {
        // TODO: Update argument type and name
        void onListFragmentInteractionSync(Device device);
    }

    //need get list of devices
    public void showListView() {
        Log.d(Const.TAG_LOG, "run showListView ");
        devices = getAllItemFromSQLite();
        if(devices == null){
            //якщо в SQLite немає записів то скопіювати їх
            copyDataFromMySQLtoSQLite();
        }
    }

    private List<Device> getAllItemFromSQLite() {
        Log.d(Const.TAG_LOG, "run getAllItemFromSQLite ");
        devices = SQLiteConnect.getInstance(getContext()).getAllItemsFromSQLite();

        if(devices.size() <=0){
            //getContext().tvResponse.setText("SQLite база пуста. Скопіювати базу з MYSQL ?");
            Toast.makeText(getContext(), "SQLite => Tabele isEmpty", Toast.LENGTH_SHORT).show();
          //  return null;
            copyDataFromMySQLtoSQLite();

        }
        return devices;
    }

    private List<Device> getNoSyncItems() {
        Log.d(Const.TAG_LOG, "run getNoSyncItems ");
        devices = SQLiteConnect.getInstance(getContext()).getNoSyncItemsFromSQLite();

        if(devices==null){
            //getContext().tvResponse.setText("SQLite база пуста. Скопіювати базу з MYSQL ?");
            Toast.makeText(getContext(), "SQLite => Tabele isEmpty", Toast.LENGTH_SHORT).show();
            //  return null;
       //     copyDataFromMySQLtoSQLite();

        }
        return devices;
    }

    private JsonObjectRequest copyDataFromMySQLtoSQLite() {
        Log.d(Const.TAG_LOG, "run copyDataFromMySQLtoSQLite");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Const.server_url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        devices = getArrayDevices(response);
                        try {

                            SQLiteConnect.getInstance(getContext()).insertAllItemToSQList(devices);
                        }catch (RuntimeException e){
                            Log.e(Const.TAG_LOG, "catch " + e.getMessage());
                        }

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "COPY DATA BASE from MYSQL to SQLite", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        MySQLConnect.getInstance(getContext().getApplicationContext()).addToRequestque(jsonObjectRequest);
        return jsonObjectRequest;
    }

    private ArrayList<Device> getArrayDevices(JSONObject response) {
        ArrayList<Device> devices = new ArrayList<Device>();
        try {
            if(response.get("success").equals(1)){
                JSONArray products = (JSONArray) response.get("products");
                for (int i=0; i < products.length(); i++){
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
                    ))  ;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return devices;
    }

    private void syncAll(List<Device> noSyncItems, RecyclerView.Adapter adapter) {
        for (int i=0; noSyncItems.size()>i; i++){
        updateItem(noSyncItems.get(i), adapter);
        }
    }

    private void updateItem(final Device device, final RecyclerView.Adapter adapter) {
        if(device != null) {
            StringRequest stringRequest = new StringRequest (Request.Method.POST, Const.update_status_invent_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                         //   Toast.makeText(getActivity(),device.getNumber()+response.toString(),Toast.LENGTH_SHORT).show();

                            int responseSuccess = getSuccess(response);
                            if(responseSuccess !=0){
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
                            Toast.makeText(getActivity(),"ERROR "+error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params  = new HashMap<String, String>();
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
            Log.d(TAG, "getSuccess: "+ jsonObject.get("success") );
            return (Integer) jsonObject.get("success") ;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
