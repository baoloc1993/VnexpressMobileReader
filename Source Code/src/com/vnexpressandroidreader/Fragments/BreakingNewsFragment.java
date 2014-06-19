package com.vnexpressandroidreader.Fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.myanmarnews.R;
import com.vnexpressandroidreader.Items.NewsItem;
import com.vnexpressandroidreader.RSS.RSSDatabaseHandler;
import com.vnexpressandroidreader.RSS.RSSFeed;
import com.vnexpressandroidreader.RSS.RSSParser;
import com.vnexpressandroidreader.RSS.WebSite;

public class BreakingNewsFragment extends Fragment {
	// /**
	// * Returns a new instance of this fragment for the given section
	// * number.
	// */
	// public BreakingNews newInstance(int sectionNumber) {
	// PlaceholderFragment fragment = new PlaceholderFragment();
	// Bundle args = new Bundle();
	// args.putInt(ARG_SECTION_NUMBER, sectionNumber);
	// fragment.setArguments(args);
	// return fragment;
	// }

	// Progress Dialog
	private ProgressDialog pDialog;
	
	Thread runOnUiThread;

	// Array list for list view
	ArrayList<HashMap<String, String>> rssFeedList;

	RSSParser rssParser = new RSSParser();

	RSSFeed rssFeed;

	// button add new website
	ImageButton btnAddSite;

	// array to trace sqlite ids
	String[] sqliteIds;

	public static String TAG_ID = "id";
	public static String TAG_TITLE = "title";
	public static String TAG_LINK = "link";

	// List view
	ListView lv;

	public BreakingNewsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//inflate layout
		View rootView = inflater.inflate(R.layout.list_news_layout, container,
				false);
		// Hashmap for ListView
		rssFeedList = new ArrayList<HashMap<String, String>>();
		lv = (ListView) rootView.findViewById(R.id.listNews);
		loadStoreSites load = new loadStoreSites();
//		load.onPreExecute();

		// selecting single ListView item
		

		// Launching new screen on Selecting Single ListItem
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});

		/**
		 * Calling a background thread which will load web sites stored in
		 * SQLite database
		 * */
		load.doInBackground(sqliteIds);
		return rootView;
	}

	/**
	 * Response from AddNewSiteActivity.java if response is 100 means new site
	 * is added to sqlite reload this activity again to show newly added website
	 * in listview
	 * */
	/*
	 * @Override public void onActivityResult(int requestCode, int resultCode,
	 * Intent data) { super.onActivityResult(requestCode, resultCode, data); //
	 * if result code 100 if (resultCode == 100) { // reload this screen again
	 * Intent intent = getIntent(); finish(); startActivity(intent); } }
	 */
	/**
	 * Building a context menu for listview Long press on List row to see
	 * context menu
	 * */
	/*
	 * @Override public void onCreateContextMenu(ContextMenu menu, View v,
	 * ContextMenuInfo menuInfo) { if (v.getId()==R.id.list) {
	 * menu.setHeaderTitle("Delete"); menu.add(Menu.NONE, 0, 0, "Delete Feed");
	 * } }
	 */
	/**
	 * Responding to context menu selected option
	 * */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		// check for selected option
		if (menuItemIndex == 0) {
			// user selected delete
			// delete the feed
			RSSDatabaseHandler rssDb = new RSSDatabaseHandler(getActivity());
			WebSite site = new WebSite();
			site.setId(Integer.parseInt(sqliteIds[info.position]));
			rssDb.deleteSite(site);
			getActivity().getFragmentManager().beginTransaction()
					.replace(R.id.container, this).commit();
		}

		return true;
	}

	/**
	 * Background Async Task to get RSS data from URL
	 * */
	
	 class loadStoreSites extends AsyncTask<String, String, String> {
	
	 /**
	 * Before starting background thread Show Progress Dialog
	 * */
		 @Override
		 protected void onPreExecute() {
			 super.onPreExecute();
			 
			 pDialog = new ProgressDialog(
					 getActivity());
			 pDialog.setMessage("Loading websites ...");
			 pDialog.setIndeterminate(false);
			 pDialog.setCancelable(false);
			 pDialog.show();
			 
		 }
	/**
	 * getting all stored website from SQLite
	 * */
		
		@Override
		protected String doInBackground	(String...params ) {
			//Thread runOnUiThread;
			// updating UI from Background Thread
			 
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					RSSDatabaseHandler rssDb = new RSSDatabaseHandler(getActivity());
					
					
				
					
					List<WebSite> siteList = rssDb.getAllSitesByID();
	
					sqliteIds = new String[siteList.size()];
					
	
					// loop through each website
					Log.d("TEST", String.valueOf(siteList.size()));
					for (int i = 0; i < siteList.size(); i++) {
	
						//Get each website in site list
						WebSite s = siteList.get(i);
	
						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();
	
						// adding each child node to HashMap key => value
						map.put(TAG_ID, s.getId().toString());
						map.put(TAG_TITLE, s.getTitle());
						map.put(TAG_LINK, s.getLink());

	
						// adding HashList to ArrayList
						rssFeedList.add(map);
	
						// add sqlite id to array
						// used when deleting a website from sqlite
						sqliteIds[i] = s.getId().toString();
					}
					/**
					 * Updating list view with websites
					 * */
					ListAdapter adapter = new SimpleAdapter(
							getActivity(),
							rssFeedList,
							R.layout.preview_single_news_list_layout,
							new String[] { TAG_ID,
									TAG_TITLE, TAG_LINK }, 
							new int[] {
									R.id.sqlite_id, R.id.title, R.id.rss_url });
					// updating listview
					
					lv.setAdapter(adapter);
					registerForContextMenu(lv);
				}
			});
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String args) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
		}
		
	 }

}
// @Override
// public void onAttach(Activity activity) {
// super.onAttach(activity);
// ((MainActivity) activity).onSectionAttached(
// getArguments().getInt(ARG_SECTION_NUMBER));
// }

