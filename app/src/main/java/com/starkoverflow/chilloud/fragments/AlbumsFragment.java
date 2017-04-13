package com.starkoverflow.chilloud.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.classes.AlbumsListAdapter;
import com.starkoverflow.chilloud.classes.ArtistsListAdapter;
import com.starkoverflow.chilloud.classes.RecyclerItemClickListener;

public class AlbumsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mLayoutManager;
    String albumsList[] = {"Madre (Single)", "Benefit", "How to be a human being", "Pink Guy", "Mainstream", "Legendary Tales", "Queen"};
    boolean cardExpandedState[] = {false, false, false, false, false, false, false};

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public AlbumsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AlbumsFragment newInstance(int sectionNumber) {
        AlbumsFragment fragment = new AlbumsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albums, container, false);

//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.albums_list);
        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getActivity(), 3);

        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(false);

        mAdapter = new AlbumsListAdapter(albumsList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // If card is expanded, collapse it, else expand it
                        LinearLayout collapsedCard = (LinearLayout) view.findViewById(R.id.collapsed_album_card);
                        LinearLayout expandedCard = (LinearLayout) view.findViewById(R.id.expanded_album_card);
                        if (cardExpandedState[position]) {
                            cardExpandedState[position] = false;
                            collapsedCard.setVisibility(View.VISIBLE);
                            expandedCard.setVisibility(View.GONE);
                        } else {
                            cardExpandedState[position] = true;
                            collapsedCard.setVisibility(View.GONE);
                            expandedCard.setVisibility(View.VISIBLE);
                        }
                        // Set expanded card to span one full row if expanded
                        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                if (cardExpandedState[position])
                                    return mLayoutManager.getSpanCount();
                                else
                                    return 1;
                            }
                        });
                        mLayoutManager.requestLayout();
//                        mRecyclerView.smoothScrollToPosition(position);
                        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 0);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        return rootView;
    }
}
