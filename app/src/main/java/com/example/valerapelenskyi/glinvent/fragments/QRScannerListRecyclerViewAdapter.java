package com.example.valerapelenskyi.glinvent.fragments;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.valerapelenskyi.glinvent.R;
import com.example.valerapelenskyi.glinvent.fragments.QRScannerFragment.OnListFragmentInteractionListenerQRScanner;
import com.example.valerapelenskyi.glinvent.model.Device;
import com.example.valerapelenskyi.glinvent.model.constants.Const;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Device } and makes a call to the
 * specified {@link OnListFragmentInteractionListenerQRScanner}.
 * TODO: Replace the implementation with code for your data type.
 */
public class QRScannerListRecyclerViewAdapter extends RecyclerView.Adapter<QRScannerListRecyclerViewAdapter.ViewHolder> {

    private List<Device> devices ;
    private final OnListFragmentInteractionListenerQRScanner mListener;

    public QRScannerListRecyclerViewAdapter(List<Device> devices, OnListFragmentInteractionListenerQRScanner listener) {
        this.devices = devices;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.device = devices.get(position);
        holder.item.setText(devices.get(position).getItem());
        holder.number.setText(devices.get(position).getNumber());
        holder.tvOwner.setText(devices.get(position).getOwner());
        holder.tvLocation.setText(devices.get(position).getLocation());
        holder.checkInventoryStatus(devices.get(position));
        holder.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mListener.setStatusInventory(holder.device, holder);


            }

        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteractionQRScanner(holder.device);
                }
            }
        });
    }






    @Override
    public int getItemCount() {
        return devices.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //   public final TextView mIdView;
        public final TextView item;
        public final TextView number;
        public final TextView tvOwner;
        public final TextView tvLocation;
        public final TextView tvMySQL;
        public final TextView tvSQLite;
        public final Button btnOK;
        public final ProgressBar progressBar;
        //        public final TextView description;
        public Device device;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //  mIdView =  view.findViewById(R.id.id);
            item = (TextView) view.findViewById(R.id.item);
            number = (TextView) view.findViewById(R.id.number);
            tvOwner = (TextView) view.findViewById(R.id.tvOwner);
            tvLocation = (TextView) view.findViewById(R.id.tvLocation);
            tvMySQL = view.findViewById(R.id.tvMySQL);
            tvSQLite = view.findViewById(R.id.tvSQLite);
            btnOK = view.findViewById(R.id.btnOK);
            progressBar = view.findViewById(R.id.pBar);

//            description = (TextView) view.findViewById(R.id.description);
        }


        public void checkInventoryStatus(Device device) {
            if(device.getStatusInvent().equals(Const.STATUS_FINED)){
                Log.d(Const.TAG_LOG, "btnSetChecked is: INVISIBLE");
                btnOK.setVisibility(View.GONE);
            }else {
                Log.d(Const.TAG_LOG, "btnSetChecked is: VISIBLE");
                btnOK.setVisibility(View.VISIBLE);
            }

            if(device.getStatusInvent().equals(Const.STATUS_FINED)){
                tvSQLite.setTextColor(Color.GREEN);
            }

            if(device.getStatusSync() == Const.STATUS_SYNC_ONLINE && device.getStatusInvent().equals(Const.STATUS_FINED) ){
              tvMySQL.setTextColor(Color.GREEN);
            }

            if(device.getStatusSync() == Const.STATUS_SYNC_OFFLINE && device.getStatusInvent().equals(Const.STATUS_FINED) ){
               tvMySQL.setTextColor(Color.RED);
            }

        }

        @Override
        public String toString() {
            return super.toString() + " '" + number.getText() + "'";
        }

        public void updateViewItem() {
           notifyDataSetChanged();
        }
    }
}
