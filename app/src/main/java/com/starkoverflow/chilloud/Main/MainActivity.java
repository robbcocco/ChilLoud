package com.starkoverflow.chilloud.Main;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.starkoverflow.chilloud.Device.AddDeviceActivity;
import com.starkoverflow.chilloud.Device.DrawerListAdapter;
import com.starkoverflow.chilloud.Library.ToolbarListAdapter;
import com.starkoverflow.chilloud.R;
import com.starkoverflow.chilloud.Device.DeviceFactory;
import com.starkoverflow.chilloud.Library.LibraryFactory;
import com.starkoverflow.chilloud.classes.OnViewGlobalLayoutListener;
import com.starkoverflow.chilloud.classes.PlaybackManager;
import com.starkoverflow.chilloud.classes.RecyclerItemClickListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static final int REQUEST_DEVICE_DATA = 1;
    static final String TAG = "Main";
    private Menu menu;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    SlidingTabLayout tabLayout;
    MenuItem expandIcon;
//    String dbList[] = {"Local", "NAS1", "NAS2", "Desktop"};
//    String deviceList[] = {"Local", "Desktop", "Chromecast"};

    private ImageView playPause;
    private AnimatedVectorDrawable playToPause;
    private AnimatedVectorDrawable pauseToPlay;
    private boolean play = true;

    private RecyclerView toolbarRecyclerView;
    private RecyclerView.Adapter toolbarAdapter;
    private RecyclerView.LayoutManager toolbarLayoutManager;
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

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return;
            }
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                return;
//            }
//            if (checkSelfPermission(Manifest.permission.INTERNET)
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
//                return;
//            }
        }

        LibraryFactory.makeMediaLibrary("Local", getContentResolver(), getApplicationContext());
        DeviceFactory.createDevice("Local", null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Local");
        setSupportActionBar(toolbar);
        toolbarRecyclerView = (RecyclerView) findViewById(R.id.toolbar_list);
        drawerRecyclerView = (RecyclerView) findViewById(R.id.drawer_list);

        PlaybackManager.setFooter(findViewById(R.id.footer));

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
        toolbarLayoutManager = new LinearLayoutManager(this);
        toolbarRecyclerView.setLayoutManager(toolbarLayoutManager);
        // specify an adapter (see also next example)
        toolbarAdapter = new ToolbarListAdapter(LibraryFactory.getLibrary());
        toolbarRecyclerView.setAdapter(toolbarAdapter);
        toolbarRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(), toolbarRecyclerView,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                        toolbar.setTitle(LibraryFactory.getLibrary().get(position).getName());
                        toggleExpandedMenus();
                        Snackbar.make(view, LibraryFactory.getLibrary().get(position).getName(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
//        LinearLayout expandableLayoutToolbar = (LinearLayout) findViewById(R.id.expandableLayout);
        toolbarRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new
                OnViewGlobalLayoutListener(this.findViewById(android.R.id.content).getRootView(), 192));

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
        drawerAdapter = new DrawerListAdapter(DeviceFactory.getDevices());
        drawerRecyclerView.setAdapter(drawerAdapter);
        drawerRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(), drawerRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        toggleDrawerMenu();
                        TextView drawerText = (TextView) findViewById(R.id.drawer_text);
                        ImageView drawerPicture = (ImageView) findViewById(R.id.drawer_picture);

                        drawerText.setText(DeviceFactory.getDevices().get(position).getName());
                        if (DeviceFactory.getDevices().get(position).getPicture() != null) {
                            drawerPicture.setImageBitmap(DeviceFactory.getDevices().get(position).getPicture());
                        } else {
                            drawerPicture.setImageDrawable(getDrawable(R.drawable.ic_phone));
                        }
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        TextView drawerSettings = (TextView) findViewById(R.id.drawer_list_settings);
        drawerSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddDeviceActivity.class), REQUEST_DEVICE_DATA);
            }
        });
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
        LinearLayout expandableLayout = (LinearLayout)
                findViewById(R.id.expandableLayout);
        LinearLayout expandableLayoutB = (LinearLayout)
                findViewById(R.id.expandableLayoutB);
        LinearLayout expandableLayoutSB = (LinearLayout)
                findViewById(R.id.expandableLayoutSB);
        if (expandableLayout.getVisibility() == View.VISIBLE) {
            expandIcon.setIcon(R.drawable.avd_checkable_expandcollapse_expanded_to_collapsed);
            expandableLayout.setVisibility(View.GONE);
            expandableLayoutB.setVisibility(View.GONE);
            expandableLayoutSB.setVisibility(View.VISIBLE);
        } else {
            expandIcon.setIcon(R.drawable.avd_checkable_expandcollapse_collapsed_to_expanded);
            expandableLayout.setVisibility(View.VISIBLE);
            expandableLayoutB.setVisibility(View.VISIBLE);
            expandableLayoutSB.setVisibility(View.GONE);
        }

        Drawable drawable = expandIcon.getIcon();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    public void toggleDrawerMenu() {
        LinearLayout expandableLayout = (LinearLayout) findViewById(R.id.expandableLayoutDrawer);
        ImageView icon = (ImageView) findViewById(R.id.drawer_icon);
        if (expandableLayout.getVisibility() == View.VISIBLE) {
            expandableLayout.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.avd_checkable_expandcollapse_expanded_to_collapsed);
        } else {
            icon.setImageResource(R.drawable.avd_checkable_expandcollapse_collapsed_to_expanded);
            expandableLayout.setVisibility(View.VISIBLE);
        }

        Drawable drawable = icon.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DEVICE_DATA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            DeviceFactory.createDevice((String) extras.get("name"), (Bitmap) extras.get("picture"));
            drawerAdapter.notifyItemInserted(DeviceFactory.getDevices().size() -1);

            LinearLayout expandableLayout = (LinearLayout) findViewById(R.id.expandableLayoutDrawer);
            Log.d(TAG, "onActivityResult: "+expandableLayout.getHeight());
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
}
