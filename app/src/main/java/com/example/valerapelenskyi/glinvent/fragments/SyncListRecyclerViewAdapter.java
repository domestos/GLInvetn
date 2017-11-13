package com.example.valerapelenskyi.glinvent.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.valerapelenskyi.glinvent.R;
import com.example.valerapelenskyi.glinvent.fragments.SyncListFragment.OnListFragmentInteractionListenerSync;
import com.example.valerapelenskyi.glinvent.model.Device;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Device } and makes a call to the
 * specified {@link OnListFragmentInteractionListenerSync}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SyncListRecyclerViewAdapter extends RecyclerView.Adapter<SyncListRecyclerViewAdapter.ViewHolder> {

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    private List<Device> devices ;
    private final SyncListFragment.OnListFragmentInteractionListenerSync mListener;

    public SyncListRecyclerViewAdapter(List<Device> devices, OnListFragmentInteractionListenerSync listener) {
        this.devices = devices;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
          holder.device = devices.get(position);
        //  holder.mIdView.setText(String.valueOf(devices.get(position).getId()));
          holder.item.setText(devices.get(position).getItem());
          holder.number.setText(devices.get(position).getNumber());
//        holder.owner.setText(devices.get(position).getOwner());
//        holder.location.setText(devices.get(position).getLocation());
//        holder.description.setText(devices.get(position).getDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteractionSync(holder.device);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(devices != null){
            return devices.size();
        }else {
            return 0;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
     //   public final TextView mIdView;
        public final TextView item;
        public final TextView number;
//        public final TextView owner;
//        public final TextView location;
//        public final TextView description;
        public Device device;

        public ViewHolder(View view) {
            super(view);
            mView = view;
          //  mIdView =  view.findViewById(R.id.id);
            item = (TextView) view.findViewById(R.id.item);
            number = (TextView) view.findViewById(R.id.number);
//            owner = (TextView) view.findViewById(R.id.owner);
//            location = (TextView) view.findViewById(R.id.location);
//            description = (TextView) view.findViewById(R.id.description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + number.getText() + "'";
        }
    }
}
