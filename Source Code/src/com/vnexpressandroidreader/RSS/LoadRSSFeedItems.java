package com.vnexpressandroidreader.RSS;

import java.util.ArrayList;
import java.util.List;

import com.example.myanmarnews.R;
import com.vnexpressandroidreader.Constant;
import com.vnexpressandroidreader.MainActivity;
import com.vnexpressandroidreader.Adapters.GridNewsItemAdapter;
import com.vnexpressandroidreader.Adapters.ListNewsItemAdapter;
import com.vnexpressandroidreader.BasicFunctions.BasicFunctions;
import com.vnexpressandroidreader.libs.actionbarpulltorefresh.library.PullToRefreshLayout;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

public class LoadRSSFeedItems extends AsyncTask<String, String, String> {

	
	List<RSSItem> rssItems = new ArrayList<RSSItem>();
	private ProgressDialog pDialog;
	RSSParser rssParser = new RSSParser();
	private ViewGroup viewGroup;
	private String currentViewGroup;
	private String LIST ="list";
	private String GRID = "grid";
	private PullToRefreshLayout pullToRefreshLayout;
	public LoadRSSFeedItems(ViewGroup viewGroup, PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated constructor stub
		this.viewGroup = viewGroup;
		this.pullToRefreshLayout = pullToRefreshLayout;
		if(MainActivity.currentFragment == Constant.ListLiveNews){
			currentViewGroup = LIST;
		}
		if(MainActivity.currentFragment == Constant.GridLiveNews){
			currentViewGroup = GRID;
		}
	}
	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (MainActivity.FirstOpen) {
			pDialog = new ProgressDialog(MainActivity.activity);
			pDialog.setMessage("Loading recent articles...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			MainActivity.FirstOpen = false;
		} else {
		}

	}

	/**
	 * getting all recent articles and showing them in listview
	 * */
	@Override
	protected String doInBackground(String... args) {
		// rss link url
		// String rss_url = args[0];
		// IF INTERNET CONNECTING, RETRIVE DATA FROM RSS LINK
		// list of rss items
		if (BasicFunctions.isConnectingToInternet(MainActivity.activity.getApplicationContext())) {
			rssItems = rssParser
					.getRSSFeedItems(MainActivity.activity.getString(R.string.rss_link));
		}

		// updating UI from Background Thread
		
		MainActivity.activity.runOnUiThread(new Runnable() {

			// InputStream input = null;

			@Override
			public void run() {
				RSSDatabaseHandler rssDb = new RSSDatabaseHandler(
						MainActivity.activity);

				/**
				 * Updating parsed items into listview
				 * */
				// rssDb.onCreate(rssDb);
				List<RSSItem> rssItemsDataBase = new ArrayList<RSSItem>();
				// NO INTERNET -> RSSITEMS is emtpy
					for (RSSItem item : rssItems) {

						// ADD EACH ITEM INTO DATABASE
						WebSite site = new WebSite(item.getTitle(), item
								.getLink(), item.getDescription(), item
								.getPubdate(), item.getImgUrl());
						rssDb.addSite(site);
						// Log.d("LINK",item.getLink());
					}
				

				// updating listview
				//Get All Website for Database
				List<WebSite> websites = rssDb.getAllSitesByID();
				for (WebSite website : websites) {
					RSSItem newItem = new RSSItem(website.getId(),
							website.getTitle(), website.getLink(),
							website.getDescription(), website
									.getPubDate(), website
									.getImageLink());

					// Add RSSItem to RSSItems
					rssItemsDataBase.add(newItem);
				}
				if(currentViewGroup == LIST){
				ListNewsItemAdapter adapter = new ListNewsItemAdapter(
						MainActivity.activity,
						R.layout.preview_single_news_list_layout,
						(ArrayList<RSSItem>) rssItemsDataBase);

				((ListView)viewGroup).setAdapter(adapter);
				}
				if(currentViewGroup == GRID){
					GridNewsItemAdapter adapter = new GridNewsItemAdapter(
							MainActivity.activity,
							R.layout.preview_single_news_grid_layout,
							(ArrayList<RSSItem>) rssItemsDataBase);

					((GridView)viewGroup).setAdapter(adapter);
					}

				// MainActivity.rssItems = rssItems;
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
		if (pDialog != null) {
			pDialog.dismiss();
		} else {

		}
		if (pullToRefreshLayout != null) {
			pullToRefreshLayout.setRefreshComplete();
		}

	}
}

