package com.starkoverflow.chilloud.Artist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Library.LibraryFactory;

import java.util.ArrayList;

public class ArtistsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mLayoutManager;


    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_ARTISTS = "artists";

    public ArtistsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ArtistsFragment newInstance(ArrayList<LibraryFactory> library) {
        ArtistsFragment fragment = new ArtistsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_ARTISTS, library);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artists, container, false);

//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        ArrayList<LibraryFactory> library = getArguments().getParcelableArrayList(ARG_ARTISTS);
        ArrayList<Artist> artists = library.get(0).getArtists();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.artists_list);
        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new ArtistsListAdapter(artists);
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(
//                        getApplicationContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//                        //
//                    }
//                    @Override public void onLongItemClick(View view, int position) {
//                        // do whatever
//                    }
//                })
//        );

        return rootView;
    }
}
