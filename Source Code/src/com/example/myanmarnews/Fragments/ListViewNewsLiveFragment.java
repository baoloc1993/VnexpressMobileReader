package com.example.myanmarnews.Fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.myanmarnews.MainActivity;
import com.example.myanmarnews.R;
import com.example.myanmarnews.BasicFunctions.BasicFunctions;
import com.example.myanmarnews.RSS.LoadRSSFeedItems;
import com.example.myanmarnews.RSS.RSSFeed;
import com.example.myanmarnews.RSS.RSSItem;
import com.example.myanmarnews.RSS.RSSParser;
import com.example.myanmarnews.libs.actionbarpulltorefresh.library.PullToRefreshLayout;

public class ListViewNewsLiveFragment extends Fragment {
	public static ListView listNews;
	private PullToRefreshLayout mPullToRefreshLayout;
	ViewGroup viewGroup;
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	

	// Array list for list view
	ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String, String>>();

	RSSParser rssParser = new RSSParser();

	// List<RSSItem> rssItems = MainActivity.rssItems;
	List<RSSItem> rssItems = new ArrayList<RSSItem>();

	RSSFeed rssFeed;

	// private static String TAG_TITLE = "title";
	// private static String TAG_LINK = "link";
	// private static String TAG_DESRIPTION = "description";
	// private static String TAG_PUB_DATE = "pubDate";
	// private static String TAG_GUID = "guid"; // not used
	// private static String TAG_IMAGE = "image";

	byte[] img;


	public ListViewNewsLiveFragment() {
	}

	@Override        

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.list_news_layout, container,
				false);

		listNews = (ListView) rootView.findViewById(R.id.listNews);
		MainActivity.FirstOpen = true;
		// gridNews = (GridView)rootView.findViewById(R.id.gridNews);
		// get fragment data
		// Fragment fragment = getActivity();

		
		/**
		 * Calling a backgroung thread will loads recent articles of a website
		 * 
		 * @param rss
		 *            url of website
		 * */
		new LoadRSSFeedItems(listNews,mPullToRefreshLayout).execute();

		// selecting single ListView item
		// ListView lv = getListView();
		// Launching new screen on Selecting Single ListItem
		listNews.setOnItemClickListener(BasicFunctions.createOnItemClickListener());
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		listNews = (ListView) view.findViewById(R.id.listNews);
		
		// We need to create a PullToRefreshLayout manually
		mPullToRefreshLayout = new PullToRefreshLayout(view.getContext());
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				new LoadRSSFeedItems(listNews,mPullToRefreshLayout).execute();
			}
		};
		// We can now setup the PullToRefreshLayout
		BasicFunctions.IniPullToRefresh(getActivity(), (ViewGroup) view,
				(View) listNews, timerTask, mPullToRefreshLayout);
	}





}
