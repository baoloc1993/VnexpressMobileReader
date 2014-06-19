package com.example.myanmarnews.Fragments;

import imageLoader.ImageLoader;

import java.util.List;

import com.example.myanmarnews.Constant;
import com.example.myanmarnews.MainActivity;
import com.example.myanmarnews.R;
//import com.example.myanmarnews.RSS.HomePageDatabaseHandler;
import com.example.myanmarnews.RSS.RSSDatabaseHandler;
import com.example.myanmarnews.RSS.RSSItem;
import com.example.myanmarnews.RSS.WebSite;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
//import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
	
	
	private static com.example.myanmarnews.MainActivity myContext;
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
			if (id == R.id.action_settings) {
				return true;
			}
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

				return PlaceholderFragment.newInstance(position+1);
			}

			@Override
			public int getCount() {
				// Show 3 total pages.
				//RSSDatabaseHandler rssDb = new RSSDatabaseHandler(myContext);
				
				return size;
			}

		}

		/**
		 * A placeholder fragment containing a simple view.
		 */
		public static class PlaceholderFragment extends android.support.v4.app.Fragment {

			/**
			 * Returns a new instance of this fragment for the given section number.
			 */
			public static PlaceholderFragment newInstance(int sectionNumber) {
				PlaceholderFragment fragment = new PlaceholderFragment();
				Bundle args = new Bundle();
				
				args.putInt(ARG_ID, sectionNumber);
				fragment.setArguments(args);
				return fragment;
			}

			public PlaceholderFragment() {
			}

			private int id ;
			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container,
					Bundle savedInstanceState) {
				View rootView = inflater.inflate(R.layout.display_web_page, container,
						false);
		    	//Get Data from previous fragment
	    		Bundle bundle = this.getArguments();
		        int position = bundle.getInt(ARG_ID);
		        //int id = 0;
		        
		       // size = bundle.getInt(ARG_SIZE);
		        Log.d("value of position in placeholder fragment", "TEST " + String.valueOf(position));
		        //Log.d("value of ID in placeholder fragment", String.valueOf(DisplayFullNewsFragment.id+position-1));
		        //Log.d("value of SIZE in placeholder fragment", String.valueOf(size));
		        
		       // id = DisplayFullNewsFragment.id +position -1;
		        //Get data from database
		        RSSDatabaseHandler rssDb = new RSSDatabaseHandler(getActivity());
		        //List<WebSite> websites = rssDb.getAllSitesByID();
//		        Log.d("value of SIZE OF DATABASE", "TEST = " + String.valueOf(DisplayFullNewsFragment.size));
//		        Log.d("value of SIZE OF DATABASE", "USING FUNCTION TEST = " + String.valueOf(rssDb.getDatabaseSize()));
		        
		        
		        	WebSite website = rssDb.getSiteById(position);
		    		
	    			final RSSItem item = new RSSItem(
	    					website.getId(),
	    					website.getTitle(),
	    					website.getLink(),
	    					website.getDescription(),
	    					website.getPubDate(),
	    					 website.getImageLink());
		        
    			//Log.d("DEBUG", "TITLE = " + item.getTitle());
		        //Get ID Layout
		        TextView page_title = (TextView) rootView.findViewById(R.id.page_title);
			    TextView page_public_date = (TextView) rootView.findViewById(R.id.page_public_date);
			    ImageView page_image = (ImageView) rootView.findViewById(R.id.page_image);
			    TextView page_content = (TextView) rootView.findViewById(R.id.page_content);
			    Button go_to_website = (Button) rootView.findViewById(R.id.go_to_website_button);
			    
			    
			    //Get information of current News

			    page_title.setText(item.getTitle());
			    page_public_date.setText(item.getPubdate());
			    page_content.setText(item.getDescription());
			    
			    Log.d("TEXTVIEW", "TITLE = " + item.getTitle());
			    //Set information for ImageView
			   ImageLoader imgLoader = new ImageLoader(getActivity());
				// Loader image - will be shown before loading image
		        int loader = R.drawable.image_not_found;
		         
				imgLoader.DisplayImage(item.getImgUrl(), loader, page_image);
				
			    //Set Information for button
			   go_to_website.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Bundle args = new Bundle();
					//Go to webView
					args.putString(DisplaySingleNewsFragment.KEY_SITE_LINK, item.getLink());
					android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
	                DisplaySingleNewsFragment displaySingleNewsFragment = new DisplaySingleNewsFragment();
	                displaySingleNewsFragment.setArguments(args);
	                
	                //Go to DisplayFullNewsFragment
	    	        fragmentManager.beginTransaction().replace(R.id.container, displaySingleNewsFragment).commit();
					
				}
			});
			   rootView.setFocusableInTouchMode(true);
				rootView.requestFocus();
				rootView.setOnKeyListener(new View.OnKeyListener() {
				       

						@Override
						public boolean onKey(View v, int keyCode, KeyEvent event) {
							// TODO Auto-generated method stub
							Log.i(getTag(), "keyCode: " + keyCode);
				            if( keyCode == KeyEvent.KEYCODE_BACK ) {
				                 
				            	Bundle args = new Bundle();
				            	//String fragment_type = args.getString(ARG_TYPE_FRAGMENT);
				                
				                FragmentManager fragmentManager = getActivity().getFragmentManager();
				                
				                //displayFullNewsFragment.setArguments(args);
				                ListViewNewsLiveFragment list = new ListViewNewsLiveFragment();
				                GridViewNewsLiveFragment grid = new GridViewNewsLiveFragment();
				                //Log.d("DEBUG", "LIST = " + fragment_type);
				                //Back to ListFragment
				                if (MainActivity.curViewGroup == Constant.List){
				                	fragmentManager.beginTransaction().replace(R.id.container, list).commit();
				                } 
				                
				                //Back to GridFragment
				                if (MainActivity.curViewGroup == Constant.Grid){
				                	Log.d("aaaa", "GRID");
				                	fragmentManager.beginTransaction().replace(R.id.container, grid).commit();
				                }
				              //  getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				                return true;
				            } else {
				                return false;
				            }
						}
				    });
				return rootView;
			}
			
		}
		
		@Override
		public void onAttach(Activity activity) {
		    myContext=(MainActivity) activity;
		    super.onAttach(activity);
		}
		
		

}
