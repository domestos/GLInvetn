package com.example.valerapelenskyi.glinvent.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.ETC1;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.valerapelenskyi.glinvent.MainActivity;
import com.example.valerapelenskyi.glinvent.R;
import com.example.valerapelenskyi.glinvent.database.mysql.MySQLConnect;
import com.example.valerapelenskyi.glinvent.database.sqlite.SQLiteConnect;
import com.example.valerapelenskyi.glinvent.model.Device;
import com.example.valerapelenskyi.glinvent.model.constants.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private final String TAG = "TAG_LOG";
    private OnFragmentInteractionListener mListener;

    private SQLiteConnect sqLiteConnect;
    private MySQLConnect mySQLConnect;


    private List<Device> devicesFromSQLite;
    private List<Device> devicesFromMySQL;
    private TextView tvRowInMYSQL;
    private TextView tvRowInSQLite;
    private TextView tvRowSYNC;
    private TextView tvInfotmDelete;
    private EditText etURL;
    private Button btnInsertToSQLite;
    private Button btnDeletSQLite;
    private Button btnSave;

    public ManageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ManageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageFragment newInstance(String param1) {
        ManageFragment fragment = new ManageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // get ALL Items from MYSQL
        mySQLConnect = MySQLConnect.getInstance(getContext());
        devicesFromMySQL = getAllItemsFromMySQL();

        // get ALL Items from SQLite
        sqLiteConnect = SQLiteConnect.getInstance(getContext());
        devicesFromSQLite = sqLiteConnect.getAllItemsFromSQLite();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        tvInfotmDelete = view.findViewById(R.id.tvInfotmDelete);
        tvRowInMYSQL = view.findViewById(R.id.tvRowInMYSQL);
        tvRowInSQLite = view.findViewById(R.id.tvRowInSQLite);
        tvRowSYNC = view.findViewById(R.id.tvRowSYNC);

        etURL = view.findViewById(R.id.etURL);
        etURL.setText(Const.url_host);

        btnInsertToSQLite = view.findViewById(R.id.btnIsertTOSQLite);
        btnInsertToSQLite.setOnClickListener(this);

        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        btnDeletSQLite = view.findViewById(R.id.btnDeleteSQLite);
        btnDeletSQLite.setOnClickListener(this);

        showCountRowSYNC();
        showCountRowInSQLite();
        showCountRowInMYSQL();
        return view;
    }

    private void showCountRowSYNC() {
        tvRowSYNC.setText(String.valueOf(sqLiteConnect.getNoSyncItemsFromSQLite().size()));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    private void saveUrlHost(String text) {
        if (mListener != null) {
            mListener.saveUrlHost(text);
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

    @Override
    public void onClick(View view) {
        //  copyDataFromMySQLtoSQLite();
        switch (view.getId()) {
            case R.id.btnIsertTOSQLite:
                if (devicesFromMySQL != null) {
                    SQLiteConnect.getInstance(getContext().getApplicationContext()).insertAllItemToSQList(devicesFromMySQL);
                    if (devicesFromSQLite.isEmpty()) {
                        devicesFromSQLite = SQLiteConnect.getInstance(getContext().getApplicationContext()).getAllItemsFromSQLite();
                    }
                    showCountRowInSQLite();
                }
                break;
            case R.id.btnSave:
                saveUrlHost(etURL.getText().toString());
                break;

            case R.id.btnDeleteSQLite:
                deleteAllFromSQLite();
                tvRowInSQLite.setText(String.valueOf(devicesFromSQLite.size()));
                break;

        }
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
        void saveUrlHost(String text);
    }
    //===================================================

    private List<Device> getAllItemsFromMySQL() {
        Log.d(Const.TAG_LOG, "run getAllItemsFromMySQL");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Const.server_url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //  Log.d(Const.TAG_LOG, response.toString());
                        devicesFromMySQL = getArrayDevices(response);

                        if (devicesFromMySQL != null) {
                            tvRowInMYSQL.setText(String.valueOf(devicesFromMySQL.size()));
                            Log.d(TAG, " result:  From MySQL =" + devicesFromMySQL.size());
                        } else {
                            Log.d(TAG, "result:   From MySQL NULL");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvRowInMYSQL.setTextColor(Color.RED);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            tvRowInMYSQL.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        }
                        tvRowInMYSQL.setText("URL is unavaible. \n Check URL or Internet connection ");
                        Toast.makeText(getContext(), "getAllItemsFromMySQL", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        MySQLConnect.getInstance(getContext().getApplicationContext()).addToRequestque(jsonObjectRequest);
        return devicesFromMySQL;
    }

    private void deleteAllFromSQLite() {
        if (SQLiteConnect.getInstance(getContext()).getNoSyncItemsFromSQLite().isEmpty()) {
            int result = SQLiteConnect.getInstance(getContext()).deleteALL();
            Toast.makeText(getContext(), result + " rows was deleted", Toast.LENGTH_LONG).show();
            devicesFromSQLite.clear();
            showCountRowInSQLite();
            Log.d(TAG, "deleteAllFromSQLite: result " + result);
        } else {
            tvInfotmDelete.setTextColor(Color.RED);
            Toast.makeText(getContext(), "SYNC LIST in NOT EMPTY", Toast.LENGTH_SHORT).show();
        }
    }


    // ============================= HELPER METHOD =================================================
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
                            Const.STATUS_SYNC_ONLINE,
                            JO.getString("description")
                    ));
                }
                //  Log.d(TAG, "getArrayDevices: count row = " + products.length());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return devices;
    }

    private void showCountRowInSQLite() {
        if (devicesFromSQLite.isEmpty()) {
            tvRowInSQLite.setText(String.valueOf(devicesFromSQLite.size()));
            btnInsertToSQLite.setVisibility(View.VISIBLE);
        } else {
            tvRowInSQLite.setText(String.valueOf(devicesFromSQLite.size()));
            btnInsertToSQLite.setVisibility(View.INVISIBLE);
        }
    }

    private void showCountRowInMYSQL() {
        if (getAllItemsFromMySQL() != null) {
            tvRowInMYSQL.setText(String.valueOf(devicesFromMySQL.size()));
        }
    }
//
//    private JsonObjectRequest copyDataFromMySQLtoSQLite() {
//        Log.d(Const.TAG_LOG, "run copyDataFromMySQLtoSQLite");
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Const.server_url,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        devicesFromMySQL = getArrayDevices(response);
//                        if (devicesFromMySQL != null) {
//                            SQLiteConnect.getInstance(getContext().getApplicationContext()).insertAllItemToSQList(devicesFromMySQL);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                    }
//                }
//        );
//
//        MySQLConnect.getInstance(getActivity().getApplicationContext()).addToRequestque(jsonObjectRequest);
//        return jsonObjectRequest;
//    }

}
