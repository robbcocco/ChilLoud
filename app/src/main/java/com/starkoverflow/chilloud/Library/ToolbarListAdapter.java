package com.starkoverflow.chilloud.Library;

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

public class ToolbarListAdapter extends RecyclerView.Adapter<ToolbarListAdapter.ViewHolder> {
    private ArrayList<LibraryFactory> library;
    private Context context;

    static final int EDIT_LIBRARY_DATA = 4;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout click;
        public TextView name;
        public TextView description;
        public ImageButton edit;
        public ViewHolder(LinearLayout v) {
            super(v);
            //v.setOnClickListener(this);
            click = v;
            name = (TextView) v.findViewById(R.id.toolbar_title);
            description = (TextView) v.findViewById(R.id.toolbar_description);
            edit = (ImageButton) v.findViewById(R.id.toolbar_row_options);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ToolbarListAdapter(ArrayList<LibraryFactory> library, Context context) {
        this.library=library;
        this.context=context;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(library.get(position).getName());
        holder.description.setText(library.get(position).getDescription());

        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).updateToolbarMenu(position);
            }
        });

        if (library.get(position).isEditable()) {
            holder.edit.setVisibility(View.VISIBLE);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddLibraryActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("library", library.get(position));
                    ((MainActivity) context).startActivityForResult(intent, EDIT_LIBRARY_DATA);
                }
            });
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return library.size();
    }
}
