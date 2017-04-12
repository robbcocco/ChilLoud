package com.starkoverflow.chilloud;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.starkoverflow.chilloud.classes.DrawerListAdapter;
import com.starkoverflow.chilloud.classes.ToolbarListAdapter;
import com.starkoverflow.chilloud.classes.RecyclerItemClickListener;
import com.starkoverflow.chilloud.classes.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ObservableScrollViewCallbacks {

    private Menu menu;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    SlidingTabLayout tabLayout;
    MenuItem expandIcon;
    String dbList[] = {"Local", "NAS1", "NAS2", "Desktop"};
    String deviceList[] = {"Local", "Desktop", "Chromecast"};

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView drawerRecyclerView;
    private RecyclerView.Adapter drawerAdapter;
    private RecyclerView.LayoutManager drawerLayoutManager;

//    private Sections
//    private View mHeaderView;
//    private View mToolbarView;
//    private int mBaseTranslationY;
//    private ViewPager mPager;
//    private NavigationAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(dbList[0]);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.toolbar_list);
        drawerRecyclerView = (RecyclerView) findViewById(R.id.drawer_list);

        // Sliding Tab Layout
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // Get reference to the tabLayout, setDistributedEvenly(true)
        // will make all tabs have the same width
        tabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setDistributeEvenly(true);
        // setCustomColorizer(), sets the color of the tab's indicator
        tabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer(){
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });
        // initialize the tablayout's viewPager
        tabLayout.setViewPager(mViewPager);

        // FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Respond to toolbar clicks
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleExpandedMenus();
            }
        });

        // Settings in the expanded toolbar
        Button ems = (Button) findViewById(R.id.expanded_menu_settings);
        ems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Open Settings", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // List in the expanded toolbar
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new ToolbarListAdapter(dbList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                        toolbar.setTitle(dbList[position]);
                        toggleExpandedMenus();
                        switch (position) {
                            case 0:
                                Snackbar.make(view, dbList[position], Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                break;
                            case 1:
                                Snackbar.make(view, dbList[position], Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                break;
                            case 2:
                                Snackbar.make(view, dbList[position], Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                break;
                            case 3:
                                Snackbar.make(view, dbList[position], Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                break;
                        }
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        // Navigation drawer
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerSlideAnimationEnabled(false);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Respond to header clicks
        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDrawerMenu();
            }
        });

        // List in the expanded drawer header
        // use a linear layout manager
        drawerLayoutManager = new LinearLayoutManager(this);
        drawerRecyclerView.setLayoutManager(drawerLayoutManager);
        // specify an adapter (see also next example)
        drawerAdapter = new DrawerListAdapter(deviceList);
        drawerRecyclerView.setAdapter(drawerAdapter);
        drawerRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(), drawerRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        toggleDrawerMenu();
                        TextView drawerText = (TextView) findViewById(R.id.drawer_text);
                        drawerText.setText(deviceList[position]);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        expandIcon = menu.findItem(R.id.action_expand);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        }
        if (searchView != null) {
            // Display result in different activity
//            ComponentName cn = new ComponentName(this, SearchResultActivity.class);
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
            // Display result in this activity
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_expand) {
            toggleExpandedMenus();
        }

        return super.onOptionsItemSelected(item);
    }

    public void toggleExpandedMenus() {
        ExpandableRelativeLayout expandableLayout = (ExpandableRelativeLayout)
                findViewById(R.id.expandableLayout);
        ExpandableRelativeLayout expandableLayoutB = (ExpandableRelativeLayout)
                findViewById(R.id.expandableLayoutB);
        ExpandableRelativeLayout expandableLayoutSB = (ExpandableRelativeLayout)
                findViewById(R.id.expandableLayoutSB);
        expandableLayout.toggle();
        expandableLayoutB.toggle();
        if (expandableLayout.isExpanded()) {
            expandIcon.setIcon(R.drawable.ic_expand_close);
            expandableLayoutSB.expand();
        }
        else {
            expandIcon.setIcon(R.drawable.ic_expand_open);
            expandableLayoutSB.collapse();
        }
        Drawable drawable = expandIcon.getIcon();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    public void toggleDrawerMenu() {
        ExpandableRelativeLayout expandableLayout = (ExpandableRelativeLayout)
                findViewById(R.id.expandableLayoutDrawer);
        expandableLayout.toggle();
        ImageView icon = (ImageView) findViewById(R.id.drawer_icon);
        if (expandableLayout.isExpanded()) {
            icon.setImageResource(R.drawable.ic_expand_close);
        }
        else {
            icon.setImageResource(R.drawable.ic_expand_open);
        }
        Drawable drawable = icon.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll,
                                boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

//    Nice ctrl+v
//    @Override
//    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
//        if (dragging) {
//            int toolbarHeight = mToolbarView.getHeight();
//            float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
//            if (firstScroll) {
//                if (-toolbarHeight < currentHeaderTranslationY) {
//                    mBaseTranslationY = scrollY;
//                }
//            }
//            float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
//            ViewPropertyAnimator.animate(mHeaderView).cancel();
//            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
//        }
//    }
//
//    @Override
//    public void onDownMotionEvent() {
//    }
//
//    @Override
//    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
//        mBaseTranslationY = 0;
//
//        Fragment fragment = getCurrentFragment();
//        if (fragment == null) {
//            return;
//        }
//        View view = fragment.getView();
//        if (view == null) {
//            return;
//        }
//
//        // ObservableXxxViews have same API
//        // but currently they don't have any common interfaces.
//        adjustToolbar(scrollState, view);
//    }
//
//    private void adjustToolbar(ScrollState scrollState, View view) {
//        int toolbarHeight = mToolbarView.getHeight();
//        final Scrollable scrollView = (Scrollable) view.findViewById(R.id.scroll);
//        if (scrollView == null) {
//            return;
//        }
//        int scrollY = scrollView.getCurrentScrollY();
//        if (scrollState == ScrollState.DOWN) {
//            showToolbar();
//        } else if (scrollState == ScrollState.UP) {
//            if (toolbarHeight <= scrollY) {
//                hideToolbar();
//            } else {
//                showToolbar();
//            }
//        } else {
//            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
//            if (toolbarIsShown() || toolbarIsHidden()) {
//                // Toolbar is completely moved, so just keep its state
//                // and propagate it to other pages
//                propagateToolbarState(toolbarIsShown());
//            } else {
//                // Toolbar is moving but doesn't know which to move:
//                // you can change this to hideToolbar()
//                showToolbar();
//            }
//        }
//    }
//
//    private Fragment getCurrentFragment() {
//        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
//    }
//
//    private void propagateToolbarState(boolean isShown) {
//        int toolbarHeight = mToolbarView.getHeight();
//
//        // Set scrollY for the fragments that are not created yet
//        mPagerAdapter.setScrollY(isShown ? 0 : toolbarHeight);
//
//        // Set scrollY for the active fragments
//        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
//            // Skip current item
//            if (i == mPager.getCurrentItem()) {
//                continue;
//            }
//
//            // Skip destroyed or not created item
//            Fragment f = mPagerAdapter.getItemAt(i);
//            if (f == null) {
//                continue;
//            }
//
//            View view = f.getView();
//            if (view == null) {
//                continue;
//            }
//            propagateToolbarState(isShown, view, toolbarHeight);
//        }
//    }
//
//    private void propagateToolbarState(boolean isShown, View view, int toolbarHeight) {
//        Scrollable scrollView = (Scrollable) view.findViewById(R.id.scroll);
//        if (scrollView == null) {
//            return;
//        }
//        if (isShown) {
//            // Scroll up
//            if (0 < scrollView.getCurrentScrollY()) {
//                scrollView.scrollVerticallyTo(0);
//            }
//        } else {
//            // Scroll down (to hide padding)
//            if (scrollView.getCurrentScrollY() < toolbarHeight) {
//                scrollView.scrollVerticallyTo(toolbarHeight);
//            }
//        }
//    }
//
//    private boolean toolbarIsShown() {
//        return ViewHelper.getTranslationY(mHeaderView) == 0;
//    }
//
//    private boolean toolbarIsHidden() {
//        return ViewHelper.getTranslationY(mHeaderView) == -mToolbarView.getHeight();
//    }
//
//    private void showToolbar() {
//        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
//        if (headerTranslationY != 0) {
//            ViewPropertyAnimator.animate(mHeaderView).cancel();
//            ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
//        }
//        propagateToolbarState(true);
//    }
//
//    private void hideToolbar() {
//        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
//        int toolbarHeight = mToolbarView.getHeight();
//        if (headerTranslationY != -toolbarHeight) {
//            ViewPropertyAnimator.animate(mHeaderView).cancel();
//            ViewPropertyAnimator.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
//        }
//        propagateToolbarState(false);
//    }
//
//    /**
//     * This adapter provides two types of fragments as an example.
//     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
//     */
//    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {
//
//        private static final String[] TITLES = new String[]{"Artist", "Album"};
//
//        private int mScrollY;
//
//        public NavigationAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        public void setScrollY(int scrollY) {
//            mScrollY = scrollY;
//        }
//
//        @Override
//        protected Fragment createItem(int position) {
//            // Initialize fragments.
//            // Please be sure to pass scroll position to each fragments using setArguments.
//            Fragment f;
//            final int pattern = position % 4;
//            switch (pattern) {
//                case 0: {
//                    f = new Artist();
////                    f = new ViewPagerTabScrollViewFragment();
////                    if (0 <= mScrollY) {
////                        Bundle args = new Bundle();
////                        args.putInt(ViewPagerTabScrollViewFragment.ARG_SCROLL_Y, mScrollY);
////                        f.setArguments(args);
////                    }
//                    break;
//                }
//                case 1: {
//                    f = new Album();
////                    f = new ViewPagerTabListViewFragment();
////                    if (0 < mScrollY) {
////                        Bundle args = new Bundle();
////                        args.putInt(ViewPagerTabListViewFragment.ARG_INITIAL_POSITION, 1);
////                        f.setArguments(args);
////                    }
//                    break;
//                }
////                case 2: {
////                    f = new ViewPagerTabRecyclerViewFragment();
////                    if (0 < mScrollY) {
////                        Bundle args = new Bundle();
////                        args.putInt(ViewPagerTabRecyclerViewFragment.ARG_INITIAL_POSITION, 1);
////                        f.setArguments(args);
////                    }
////                    break;
////                }
////                case 3:
//                default: {
//                    f = new Album();
////                    f = new ViewPagerTabGridViewFragment();
////                    if (0 < mScrollY) {
////                        Bundle args = new Bundle();
////                        args.putInt(ViewPagerTabGridViewFragment.ARG_INITIAL_POSITION, 1);
////                        f.setArguments(args);
////                    }
//                    break;
//                }
//            }
//            return f;
//        }
//
//        @Override
//        public int getCount() {
//            return TITLES.length;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return TITLES[position];
//        }
//    }
}
