package ngo.vnexpress.reader;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import io.fabric.sdk.android.Fabric;
import ngo.vnexpress.reader.adapters.ListNewsItemAdapter;
import ngo.vnexpress.reader.items.RSSItem;
import ngo.vnexpress.reader.items.StraightTimesItem;
import ngo.vnexpress.reader.managers.RSSItemManager;
import ngo.vnexpress.reader.rss.LoadRSSFeedItems;
import ngo.vnexpress.reader.views.LoadingRecyclerView;
import ngo.vnexpress.reader.views.NavigationViewWrapper;

public class MainActivity extends AppCompatActivity implements LoadRSSFeedItems.OnRSSFeedItemLoadedListener{
    public static final List<String> PERMISSIONS = Collections.singletonList("publish_actions");
    public static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    /**
     * Facebook
     */
    public static Class<? extends RSSItem> currentSource = StraightTimesItem.class;
    public static File fileDir;
    public static boolean pendingPublishReauthorization = false;
    /**
     * Static variables
     */
    public static int versionCode = 0;
    public static boolean stopService = false;
    /**
     * Screen's Size
     */
    public static int screenHeight;
    public static int screenWidth;
    public UiLifecycleHelper uiHelper;
    Intent notificationService;
    Intent adService;
    private RecyclerView mRecyclerView;
    private LoadingRecyclerView loadingRecyclerView;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationViewWrapper navigationViewWrapper;
    private DrawerLayout drawerLayout;

    /**
     * @return the standard size for rendering item
     */
    public static int getStandardSize() {
        return Math.min(screenWidth, screenHeight);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        /*
          Get version Code
         */
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(),
                    0).versionCode;
            // RSSDatabaseHandler.DATABASE_VERSION = versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block

        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        fileDir = this.getApplicationContext().getExternalFilesDir(null);

        setContentView(R.layout.activity_main);


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationViewWrapper = new NavigationViewWrapper(navigationView);
        navigationViewWrapper.iniNavigationView(this);

        iniToolbar();
        iniRecyclerView();
//        mRecyclerView.setNestedScrollingEnabled(false);
        // specify an adapter (see also next example)
        //
        //Default Data
// Create an ad.
        /*
      Google Admob
     */
        AdView adView = findViewById(R.id.adView);

        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
//        AdRequest adRequest = new AdRequest.Builder().build();
        // Start loading the ad in the background.
//        adView.loadAd(adRequest);
//        rssItems = RSSItemManager.getInstance().getItemByCurrentCategory();
//        mAdapter = new ListNewsItemAdapter(rssItems);
//        mRecyclerView.setAdapter(mAdapter);
        initializeTab();
        changeSource(StraightTimesItem.class);


        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
        // Start background Service
        // Intent i=new Intent(this, NotificationService.class);


        // Get the width and length of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;


        // COPY FILE FROM ASSET FOLDER TO MEMORY
        // copyAssets();

    }

    private void iniToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }


    }

    private void iniRecyclerView() {
        loadingRecyclerView = findViewById(R.id.loading_recycle_view);
        mRecyclerView = loadingRecyclerView.findViewById(R.id.list_news);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void initializeTab() {
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tag = (String) tab.getTag();
                RSSItemManager.getInstance(currentSource).setCurrentCategory(tag);
                new LoadRSSFeedItems(loadingRecyclerView, MainActivity.this).execute();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String tag = (String) tab.getTag();
                RSSItemManager.getInstance(currentSource).setCurrentCategory(tag);

            }
        });


    }

    public void changeSource(Class<? extends RSSItem> newSource) {
        currentSource = newSource;
        updateTab();
        updateToolbar();
        navigationViewWrapper.updateNavigationView();
        drawerLayout.closeDrawers();
        mRecyclerView.removeAllViews();

    }

    private void updateTab() {
        Set<String> titles = RSSItemManager.getInstance(MainActivity.currentSource).getCategories();
        tabLayout.removeAllTabs();
        for (String title : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title).setTag(title));
        }
        tabLayout.getTabAt(0).select();
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(RSSItemManager.getInstance(MainActivity.currentSource).getHeaderBgColor()));
        new LoadRSSFeedItems(loadingRecyclerView, MainActivity.this).execute();
    }

    private void updateToolbar() {
        RSSItemManager rssItemManager = RSSItemManager.getInstance(MainActivity.currentSource);
        int bgColor = Color.parseColor(rssItemManager.getHeaderBgColor());
        int txtColor = Color.parseColor(rssItemManager.getToolbarTextColor());
        toolbar.setBackgroundColor(bgColor);
        toolbar.setSubtitleTextColor(txtColor);
        toolbar.setTitleTextColor(txtColor);
        toolbar.getNavigationIcon().mutate().setColorFilter(txtColor, PorterDuff.Mode.SRC_IN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(bgColor);
        }

        setTitle(rssItemManager.getSource());
    }

    @Override
    public void onStart() {
        super.onStart();

//		EasyTracker.getInstance(this).activityStart(this); // Add this method.
    }

    @Override
    public void onStop() {
        super.onStop();
        if (stopService) {
            stopService = false;
            startService(notificationService);
        }
//		EasyTracker.getInstance(this).activityStop(this); // Add this method.
    }

    /**
     * Called before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        // Destroy the AdView.

        // if (adView != null) {
        // adView.destroy();
        //
        // }
//        uiHelper.onDestroy();
//        // Start Service
//        stopService = false;
//        startService(notificationService);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    /**
     * Create Menu in Action Bar 1: Switch layout 2: Share on Facebook
     */

    // Create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * for Facebook sharing
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data,
                new FacebookDialog.Callback() {
                    @Override
                    public void onComplete(
                            FacebookDialog.PendingCall pendingCall, Bundle data) {
                        // Log.i("Activity", "Success!");
                    }

                    @Override
                    public void onError(FacebookDialog.PendingCall pendingCall,
                                        Exception error, Bundle data) {
                        // Log.e("Activity", String.format("Error: %s",
                        // error.toString()));
                    }
                });
    }

    @Override
    public void onPause() {
        stopService = false;
        // if (adView != null) {
        // adView.pause();
        //
        // }
        uiHelper.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        stopService = true;
        super.onResume();
        // if (adView != null) {
        // adView.resume();
        //
        // }
        uiHelper.onResume();
    }

    @Override
    public void onRSSFeedItemLoaded(List<RSSItem> rssItems) {
        ListNewsItemAdapter adapter = new ListNewsItemAdapter(
                rssItems, getSupportFragmentManager());
        loadingRecyclerView.recyclerView.setAdapter(adapter);
    }
}
