package com.starkoverflow.chilloud.Device;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starkoverflow.chilloud.Main.MainActivity;
import com.starkoverflow.chilloud.R;

import java.util.ArrayList;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {
    private ArrayList<DeviceFactory> devices;
    private Context context;

    static final int EDIT_DEVICE_DATA = 2;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout click;
        public TextView name;
        public ImageButton options;
        public ViewHolder(LinearLayout v) {
            super(v);
            //v.setOnClickListener(this);
            click = v;
            name = (TextView) v.findViewById(R.id.drawer_list_row);
            options = (ImageButton) v.findViewById(R.id.drawer_row_options);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DeviceListAdapter(ArrayList<DeviceFactory> devices, Context context) {
        this.devices=devices;
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DeviceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drawer_list_row, parent, false);
        // set the view's size, margins, paddings and layout parametersR.layout.drawer_list_row

        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(devices.get(position).getName());

        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).updateDrawerMenu(position);
            }
        });
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddDeviceActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("device", devices.get(position));
                ((MainActivity) context).startActivityForResult(intent, EDIT_DEVICE_DATA);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return devices.size();
    }
}
