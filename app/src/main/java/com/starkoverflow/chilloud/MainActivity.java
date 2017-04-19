package com.starkoverflow.chilloud;

import android.Manifest;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.starkoverflow.chilloud.classes.DrawerListAdapter;
import com.starkoverflow.chilloud.classes.LibraryFactory;
import com.starkoverflow.chilloud.classes.ToolbarListAdapter;
import com.starkoverflow.chilloud.classes.RecyclerItemClickListener;
import com.starkoverflow.chilloud.classes.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Main";
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

//    private static ArrayList<LibraryFactory> libraryFactoryList;

//    private Sections
    private View mHeaderView;
    private View mToolbarView;
    private int mBaseTranslationY;
//    private ViewPager mPager;
//    private NavigationAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                return;
        }}
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(dbList[0]);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.toolbar_list);
        drawerRecyclerView = (RecyclerView) findViewById(R.id.drawer_list);

        Log.d(TAG, "onCreate: makeSongList started");
        LibraryFactory.makeSongList(getContentResolver());
        Log.d(TAG, "onCreate: makeSongList done");
//        libraryFactoryList = new ArrayList<LibraryFactory>();
//        getSongList();
//        Collections.sort(libraryFactoryList, new Comparator<LibraryFactory>(){
//            public int compare(LibraryFactory a, LibraryFactory b){
//                return a.getTitle().compareTo(b.getTitle());
//            }
//        });

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
        // set default section to fragment 2
        mViewPager.setCurrentItem(1);

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

//    public void getSongList() {
//        ContentResolver musicResolver = getContentResolver();
//        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
//
//        if(musicCursor!=null && musicCursor.moveToFirst()){
//            //get columns
//            int titleColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media.TITLE);
//            int idColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media._ID);
//            int artistColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media.ARTIST);
//            //add songs to list
//            do {
//                long thisId = musicCursor.getLong(idColumn);
//                String thisTitle = musicCursor.getString(titleColumn);
//                String thisArtist = musicCursor.getString(artistColumn);
//                libraryFactoryList.add(new LibraryFactory(thisId, thisTitle, thisArtist));
//            }
//            while (musicCursor.moveToNext());
//        }
//    }
//    public static ArrayList<LibraryFactory> getSongList() {
//        return libraryFactoryList;
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
