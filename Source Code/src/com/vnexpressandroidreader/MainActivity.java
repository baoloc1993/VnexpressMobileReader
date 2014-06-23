package com.vnexpressandroidreader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myanmarnews.R;
import com.vnexpressandroidreader.Fragments.ListViewNewsLiveFragment;
import com.vnexpressandroidreader.Fragments.NavigationDrawerFragment;
import com.vnexpressandroidreader.Fragments.SocialNetworkFragment;
import com.vnexpressandroidreader.RSS.RSSItem;

public class MainActivity extends FragmentActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	public static boolean FirstOpen;
	public static Constant currentFragment = Constant.List;
	public static Constant curViewGroup;
	public static Activity activity;
	public static List<RSSItem> rssItems = new ArrayList<RSSItem>();
	public static int LIMITED_NUMBER = 100;
	public static NameCategories nameCategory;
	
	
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
	/**
	 * Screen's Size
	 */
	private static int screenHeight;
	private static int screenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/**
		 * get screen's size;
		 */
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenHeight = displayMetrics.heightPixels;
		screenWidth = displayMetrics.widthPixels;
		activity = this;
		/**
		 * Navigation Drawer
		 */
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		//COPY FILE FROM ASSET FOLDER TO MEMORY
		//copyAssets();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		switch (position) {
		case 0:
			nameCategory = NameCategories.Homepage;
			break;
		case 1:
			nameCategory = NameCategories.News;
			break;
		case 2:
			nameCategory = NameCategories.Life;
			break;
		case 3:
			nameCategory = NameCategories.World;
			break;
		case 4:
			nameCategory = NameCategories.Business;
			break;
		case 5:
			nameCategory = NameCategories.Entertainment;
			break;
		case 6:
			nameCategory = NameCategories.Sports;
			break;
		case 7:
			nameCategory = NameCategories.Laws;
			break;
		case 8:
			nameCategory = NameCategories.Travelling;
			break;
		case 9:
			nameCategory = NameCategories.Science;
			break;
		case 10:
			nameCategory = NameCategories.Digital;
			break;
		case 11:
			nameCategory = NameCategories.Car;
			break;
		case 12:
			nameCategory = NameCategories.Social;
			break;
		case 13:
			nameCategory = NameCategories.Chat;
			break;
		case 14:
			nameCategory = NameCategories.Funny;
			break;	
		case 15:
			nameCategory = NameCategories.About;
			break;
		default:
			nameCategory = NameCategories.Homepage;
			break;
			
		}
		
		curViewGroup = Constant.List;
		if(nameCategory!=NameCategories.About){
			fragmentManager.beginTransaction().replace(R.id.container, new ListViewNewsLiveFragment()).commit();
		} else{
			fragmentManager.beginTransaction().replace(R.id.container, new SocialNetworkFragment()).commit();
		}

	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_home_page);
			break;
		case 2:
			mTitle = getString(R.string.title_news);
			break;
		case 3:
			mTitle = getString(R.string.title_life);
			break;
		case 4:
			mTitle = getString(R.string.title_world);
			break;
		case 5:
			mTitle = getString(R.string.title_business);
			break;
		case 6:
			mTitle = getString(R.string.title_entertainment);
			break;
		case 7:
			mTitle = getString(R.string.title_sports);
			break;
		case 8:
			mTitle = getString(R.string.title_laws);
			break;
		case 9:
			mTitle = getString(R.string.title_travelling);
			break;
		case 10:
			mTitle = getString(R.string.title_science);
			break;
		case 11:
			mTitle = getString(R.string.title_digital);
			break;
		case 12:
			mTitle = getString(R.string.title_cars);
			break;
		case 13:
			mTitle = getString(R.string.title_social);
			break;
		case 14:
			mTitle = getString(R.string.title_chat);
			break;
		case 15:
			mTitle = getString(R.string.title_funny);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		 actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		 actionBar.setDisplayShowTitleEnabled(true);
		 actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * @return the standard size for rendering item
	 */
	public static int getStandardSize() {
		return Math.min(screenWidth, screenHeight);
	}
	private void copyAssets() {
	    AssetManager assetManager = getAssets();
	    String[] files = null;
	    try {
	        files = assetManager.list("");
	    } catch (IOException e) {
	        Log.e("tag", "Failed to get asset file list.", e);
	    }
	    for(String filename : files) {
	        InputStream in = null;
	        OutputStream out = null;
	        try {
	          in = assetManager.open(filename);
	          File outFile = new File(getExternalFilesDir(getApplicationContext().ACCESSIBILITY_SERVICE).toString()
	        		  , filename);
	          out = new FileOutputStream(outFile);
	          copyFile(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          out = null;
	        } catch(IOException e) {
	            Log.e("tag", "Failed to copy asset file: " + filename, e);
	        }       
	    }
	}
	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}

}
