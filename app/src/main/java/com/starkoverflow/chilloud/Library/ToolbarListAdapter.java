package com.starkoverflow.chilloud.Library;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starkoverflow.chilloud.R;

import java.util.ArrayList;

public class ToolbarListAdapter extends RecyclerView.Adapter<ToolbarListAdapter.ViewHolder> {
    private ArrayList<LibraryFactory> library;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle;
        public ViewHolder(LinearLayout v) {
            super(v);
            //v.setOnClickListener(this);
            mTitle = (TextView) v.findViewById(R.id.toolbar_title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ToolbarListAdapter(ArrayList<LibraryFactory> library) {
        this.library=library;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ToolbarListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.toolbar_list_row, parent, false);
        // set the view's size, margins, paddings and layout parametersR.layout.drawer_list_row

        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTitle.setText(library.get(position).getName());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return library.size();
    }
}
