package com.starkoverflow.chilloud.Device;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.starkoverflow.chilloud.R;

import java.util.ArrayList;

public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.ViewHolder> {
    private ArrayList<DeviceFactory> devices;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            //v.setOnClickListener(this);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DrawerListAdapter(ArrayList<DeviceFactory> devices) {
        this.devices=devices;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DrawerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_list_row, parent, false);
        // set the view's size, margins, paddings and layout parametersR.layout.drawer_list_row

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(devices.get(position).getName());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return devices.size();
    }
}
