package com.vnexpressandroidreader.Fragments;

import com.example.myanmarnews.R;
//import com.example.myanmarnews.RSS.HomePageDatabaseHandler;


import com.vnexpressandroidreader.MainActivity;
import com.vnexpressandroidreader.RSS.RSSDatabaseHandler;
import com.vnexpressandroidreader.RSS.WebSite;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
//import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class DisplayFullNewsFragment extends Fragment {
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	//PagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	
	//private Fragment fragmentManager;
	//private ArrayList<RSSItem> rssItems = new ArrayList<RSSItem>();
	
	public static String ARG_ID = "";
	public static String ARG_TITLE = "";
	public static final String ARG_SIZE = null;
	//public static final String ARG_TYPE_FRAGMENT = null;
	
	private static int size = 0;
	private static int id = 0;
	
	
	private static com.vnexpressandroidreader.MainActivity myContext;
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	
	//private ArrayList<RSSItem> rssItems = (ArrayList<RSSItem>) MainActivity.rssItems;

	
	
	public DisplayFullNewsFragment(){
		
		super();
		
	}
	
	
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
			

	        View rootView = inflater.inflate(R.layout.swipe_view_layout, container, false);
	        RSSDatabaseHandler rssDb = new RSSDatabaseHandler(getActivity());
	        size = rssDb.getDatabaseSize();

	        
	     // Create the adapter that will return a fragment for each of the three
			// primary sections of the activity.
	        Bundle bundle = this.getArguments();
	        id = bundle.getInt(ARG_ID);
	        
	        PagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(myContext.getSupportFragmentManager());

	        
	       
			// Set up the ViewPager with the sections adapter.
	        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
			
			mViewPager.setAdapter(mSectionsPagerAdapter);
			
			//id start from 0. Position start from 1
			 Log.d("Value of ID", "TEST = " + String.valueOf(id));
			mViewPager.setCurrentItem(id-1);
			return rootView;
	        
	 }
	 @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			return super.onOptionsItemSelected(item);
		}

		/**
		 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
		 * one of the sections/tabs/pages.
		 */
		public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

			public SectionsPagerAdapter(android.support.v4.app.FragmentManager fragmentManager) {
				super(fragmentManager);
			}

			@Override
			public android.support.v4.app.Fragment getItem(int position) {
				// getItem is called to instantiate the fragment for the given page.
					// below).
				// Return a PlaceholderFragment (defined as a static inner class
				//Log.d("SECTIONPAGER", "CALLED");
				Bundle arg = new Bundle();
				RSSDatabaseHandler rssDb = new RSSDatabaseHandler(getActivity());
		        //List<WebSite> websites = rssDb.getAllSitesByID();
//		        Log.d("value of SIZE OF DATABASE", "TEST = " + String.valueOf(DisplayFullNewsFragment.size));
//		        Log.d("value of SIZE OF DATABASE", "USING FUNCTION TEST = " + String.valueOf(rssDb.getDatabaseSize()));
		        
		        //POSITION START AT 0, ID IN DATABASE START AT 1	
	        	WebSite website = rssDb.getSiteById(position+1);
	        	DisplayWebNewsFragment f = new DisplayWebNewsFragment();
	        	arg.putString(DisplayWebNewsFragment.KEY_SITE_LINK, website.getLink());
	        	f.setArguments(arg);
				return f;
			}

			@Override
			public int getCount() {
				// Show 3 total pages.
				//RSSDatabaseHandler rssDb = new RSSDatabaseHandler(myContext);
				
				return size;
			}

		}

		
		
		@Override
		public void onAttach(Activity activity) {
		    myContext=(MainActivity) activity;
		    super.onAttach(activity);
		}
		
		

}
