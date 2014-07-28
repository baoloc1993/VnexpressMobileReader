package ngo.vnexpress.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.analytics.tracking.android.EasyTracker;

import ngo.vnexpress.reader.Fragments.ListViewNewsLiveFragment;
import ngo.vnexpress.reader.Fragments.NavigationDrawerFragment;
import ngo.vnexpress.reader.Fragments.SocialNetworkFragment;
import ngo.vnexpress.reader.RSS.RSSDatabaseHandler;
import ngo.vnexpress.reader.RSS.RSSItem;
import ngo.vnexpress.reader.RSS.WebSite;
import ngo.vnexpress.reader.backgroundnotification.AdvertismentNotificationService;
import ngo.vnexpress.reader.backgroundnotification.NotificationService;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends FragmentActivity implements
NavigationDrawerFragment.NavigationDrawerCallbacks {
	/**
	 * Facebook
	 */
	public static WebSite currentWeb = null;

	public static UiLifecycleHelper uiHelper;
	public static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	public static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	public static boolean pendingPublishReauthorization = false;
	/**
	 * Static variables
	 */
	public static int versionCode = 0;
	public static boolean FirstOpen;
	public static Constant currentFragment = Constant.List;
	public static Constant curViewGroup = null;
	public static FragmentActivity activity = null;
	public static List<RSSItem> rssItems = new ArrayList<RSSItem>();
	public static int LIMITED_NUMBER = 100;
	public static NameCategories nameCategory = null;
	public static NameCategories nameCategoryService = null;
	
	public static boolean stopService = false;
//	public static int numberNewPost = 0;
	//public static HashMap<NameCategories, Integer> newArticlePerCate = new HashMap<NameCategories, Integer>();
	
	Intent notiService;
	Intent adService;
	/**
	 * Google Admob
	 */
	private AdView adView;
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private int mIconId;

	/**
	 * Screen's Size
	 */
	private static int screenHeight;
	private static int screenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		/**
		 * Get version Code
		 */
		try {
			versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			//RSSDatabaseHandler.DATABASE_VERSION = versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			
		}
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		//nameCategory = NameCategories.Homepage;
		//Start service
		stopService = true;
		notiService = new Intent(this,NotificationService.class);
		adService = new Intent(this, AdvertismentNotificationService.class);
		startService(adService);
		
		// Initialize Map
		if (NotificationService.newArticlePerCate.isEmpty()) {
			// Log.d("DEBUG", "INITIAL MAP");
			initializeMap();
		}

		
		setContentView(R.layout.activity_main);
		// try {
		//
		// PackageInfo info =
		// getPackageManager().getPackageInfo(getPackageName(),
		// PackageManager.GET_SIGNATURES);
		//
		// for (Signature signature : info.signatures)
		// {
		// MessageDigest md = MessageDigest.getInstance("SHA");
		// md.update(signature.toByteArray());
		// Log.d("KeyHash:", Base64.encodeToString(md.digest(),
		// Base64.DEFAULT));
		// }
		//
		// } catch (NameNotFoundException e) {
		// Log.e("name not found", e.toString());
		// } catch (NoSuchAlgorithmException e) {
		// Log.e("no such an algorithm", e.toString());
		// }
		
		/**
		 * Navigation Drawer
		 */
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		/**
		 * for FB sharing
		 */
		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);
		/**
		 * get screen's size;
		 */
		// Start background Service
		// Intent i=new Intent(this, NotificationService.class);
		// Create an ad.
		adView = (AdView) findViewById(R.id.adView);
		
		// Add the AdView to the view hierarchy. The view will have no size
		// until the ad is loaded.

		// Create an ad request. Check logcat output for the hashed device ID to
		// get test ads on a physical device.
		AdRequest adRequest = new AdRequest.Builder().build();
//		adView.setAdSize(AdSize.BANNER);
//		adView.setAdUnitId(getString(R.string.ad_unit_id));
		// Start loading the ad in the background.
		adView.loadAd(adRequest);

		/**
		 * get screen's size;
		 */

		// Get the width and length of the screen
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenHeight = displayMetrics.heightPixels;
		screenWidth = displayMetrics.widthPixels;
		activity = this;
		/**
		 * Hompage at start
		 */
		
		// COPY FILE FROM ASSET FOLDER TO MEMORY
		// copyAssets();
		
                                  	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// Action when one of items in drawer fragment clicked
		FragmentManager fragmentManager = getFragmentManager();
		switch (position) {
		case 0:
			nameCategory = NameCategories.Homepage;
			mTitle = getString(R.string.title_home_page);
			mIconId = R.drawable.home;
			break;
		case 1:
			nameCategory = NameCategories.News;
			mTitle = getString(R.string.title_news);
			mIconId = R.drawable.news;
			break;
		case 2:
			nameCategory = NameCategories.Life;
			mTitle = getString(R.string.title_life);
			mIconId = R.drawable.life;
			break;
		case 3:
			nameCategory = NameCategories.World;
			mTitle = getString(R.string.title_world);
			mIconId = R.drawable.world;
			break;
		case 4:
			nameCategory = NameCategories.Business;
			mTitle = getString(R.string.title_business);
			mIconId = R.drawable.business;
			break;
		case 5:
			nameCategory = NameCategories.Entertainment;
			mTitle = getString(R.string.title_entertainment);
			mIconId = R.drawable.entertainment;
			break;
		case 6:
			nameCategory = NameCategories.Sports;
			mTitle = getString(R.string.title_sports);
			mIconId = R.drawable.sport;
			break;
		case 7:
			nameCategory = NameCategories.Laws;
			mTitle = getString(R.string.title_laws);
			mIconId = R.drawable.law;
			break;
		case 8:
			nameCategory = NameCategories.Travelling;
			mTitle = getString(R.string.title_travelling);
			mIconId = R.drawable.travelling;
			break;
		case 9:
			nameCategory = NameCategories.Science;
			mTitle = getString(R.string.title_science);
			mIconId = R.drawable.science;
			break;
		case 10:
			nameCategory = NameCategories.Digital;
			mTitle = getString(R.string.title_digital);
			mIconId = R.drawable.digital;
			break;
		case 11:
			nameCategory = NameCategories.Car;
			mTitle = getString(R.string.title_cars);
			mIconId = R.drawable.car;
			break;
		case 12:
			nameCategory = NameCategories.Social;
			mTitle = getString(R.string.title_social);
			mIconId = R.drawable.social;
			break;
		case 13:
			nameCategory = NameCategories.Chat;
			mTitle = getString(R.string.title_chat);
			mIconId = R.drawable.chat;
			break;
		case 14:
			nameCategory = NameCategories.Funny;
			mTitle = getString(R.string.title_funny);
			mIconId = R.drawable.funny;
			break;
		case 15:
			nameCategory = NameCategories.About;
			mTitle = getString(R.string.title_about);
			mIconId = R.drawable.about;
			break;
		default:
			nameCategory = NameCategories.Homepage;
			mTitle = getString(R.string.title_home_page);
			mIconId = R.drawable.vnexpress;
			break;

		}

		// Go to conresponding fragment
		curViewGroup = Constant.List;
		if (nameCategory != NameCategories.About) {
			fragmentManager.beginTransaction()
			.replace(R.id.container, new ListViewNewsLiveFragment())
			.commit();
		} else {
			fragmentManager.beginTransaction()
			.replace(R.id.container, new SocialNetworkFragment())
			.commit();
		}

	}

	// Set title on ActionBar
	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(mIconId);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);

		actionBar.setTitle(mTitle);
	}

	/**
	 * Create Menu in Action Bar 1: Switch layout 2: Share on Facebook
	 */

	// Create menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			if (currentFragment == Constant.Grid
					|| currentFragment == Constant.List) {
				getMenuInflater().inflate(R.menu.news, menu);
			}
			if (currentFragment == Constant.Web) {
				getMenuInflater().inflate(R.menu.webview, menu);
			}
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}

	/**
	 * @return the standard size for rendering item
	 */
	public static int getStandardSize() {
		return Math.min(screenWidth, screenHeight);
	}

	public void initializeMap() {
		// Log.d("DEBUG", "MAP INI");

		for (NameCategories name : NameCategories.values()) {
			if (!NotificationService.newArticlePerCate.containsKey(name)) {
				// Log.d("MAIN ACTIVITY", "CATE ");
				NotificationService.newArticlePerCate.put(name, 0);
			}
		}
	}
	@Override
	  public void onStart() {
	    super.onStart();
	    
	    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	  }
	@Override
	public void onResume() {
		stopService = true;
		super.onResume();
		if (adView != null) {
			adView.resume();
			
		}
		uiHelper.onResume();
	}

	@Override
	public void onPause() {
		stopService = false;
		if (adView != null) {
			adView.pause();
			
		}
		uiHelper.onPause();
		super.onPause();
	}

	/** Called before the activity is destroyed. */
	@Override
	public void onDestroy() {
		// Destroy the AdView.
		
		if (adView != null) {
			adView.destroy();

		}
		uiHelper.onDestroy();
		//Start Service
		stopService = false;
		startService(notiService);
		super.onDestroy();
	}
	@Override
	  public void onStop() {
	    super.onStop();
	    if(stopService) {
	    	stopService = false;
	    	startService(notiService);
	    }
	    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
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
					public void onError(FacebookDialog.PendingCall pendingCall,
							Exception error, Bundle data) {
						// Log.e("Activity", String.format("Error: %s",
						// error.toString()));
					}

					@Override
					public void onComplete(
							FacebookDialog.PendingCall pendingCall, Bundle data) {
						// Log.i("Activity", "Success!");
					}
				});
	}

}
