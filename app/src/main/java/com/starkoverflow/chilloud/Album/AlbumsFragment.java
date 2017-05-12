package com.starkoverflow.chilloud.Album;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Library.LibraryFactory;

import java.util.ArrayList;

public class AlbumsFragment extends Fragment {

    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static GridLayoutManager mLayoutManager;
    static boolean[] cardExpandedState;
//    static ArrayList<Boolean> cardExpandedState = new ArrayList<Boolean>();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_ALBUMS = "albums";

    public AlbumsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AlbumsFragment newInstance(ArrayList<LibraryFactory> library) {
        AlbumsFragment fragment = new AlbumsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_ALBUMS, library);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albums, container, false);

        ArrayList<LibraryFactory> library = getArguments().getParcelableArrayList(ARG_ALBUMS);
        ArrayList<Album> albums = library.get(0).getAlbums();

        cardExpandedState = new boolean[albums.size()];
        for (int i = 0; i< albums.size(); i++) {
            cardExpandedState[i]=false;
        }
//        cardExpandedState.clear();
//        for (int i = 0; i< albums.size(); i++) {
//            cardExpandedState.add(false);
//        }

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
        mAdapter = new AlbumsListAdapter(albums);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public static void adjustSpanSize(int position) {
        if (cardExpandedState[position]) {
            cardExpandedState[position]=false;
        } else {
            cardExpandedState[position]=true;

            // Scroll to card when expanding
//            View test = mRecyclerView.getChildAt(position);
//            View test = mRecyclerView.getLayoutManager().findViewByPosition(position);
//            RecyclerView test2 = (RecyclerView) test.findViewById(R.id.album_songs_list);
//            test2.smoothScrollToPosition(0);

//            mRecyclerView.smoothScrollToPosition(position);
//            ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 0);
        }

        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Log.d("Adjust grid A ", position + "");
                if (cardExpandedState[position])
                    return mLayoutManager.getSpanCount();
                else
                    return 1;
            }
        });
        mLayoutManager.requestLayout();
    }

    public static void adjustSpanSizeB() {
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                View v = mRecyclerView.getChildAt(position);
                Log.d("Adjust grid B ", position + " " + v.getId());
                CardView collapsedCard = (CardView) v.findViewById(R.id.collapsed_album_card);
                Log.d("Adjust grid B ", " " + collapsedCard.getId());
                if (collapsedCard.getVisibility() == View.GONE)
                    return mLayoutManager.getSpanCount();
                else
                    return 1;
            }
        });
        mLayoutManager.requestLayout();
    }
}
