package ngo.vnexpress.reader.RSS;

/**
 * Load RSSITEM from RSS link.
 * Run on foreground service
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.Adapters.GridNewsItemAdapter;
import ngo.vnexpress.reader.Adapters.ListNewsItemAdapter;
import ngo.vnexpress.reader.BasicFunctions.BasicFunctions;
import ngo.vnexpress.reader.libs.actionbarpulltorefresh.library.PullToRefreshLayout;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

public class LoadRSSFeedItems extends AsyncTask<String, String, String> {

	List<RSSItem> rssItems = new ArrayList<RSSItem>();
	private ProgressDialog pDialog;
	RSSParser rssParser = new RSSParser();
	private ViewGroup viewGroup;

	private PullToRefreshLayout pullToRefreshLayout;

	public LoadRSSFeedItems(ViewGroup viewGroup,
			PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated constructor stub
		this.viewGroup = viewGroup;
		this.pullToRefreshLayout = pullToRefreshLayout;

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
		if (BasicFunctions.isConnectingToInternet(MainActivity.activity
				.getApplicationContext())) {
			// Log.d("DEBUG", "CATE = " + getRssUrl());
			rssItems = rssParser.getRSSFeedItems(getRssUrl());
			Collections.reverse(rssItems);
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
					WebSite site = new WebSite(item.getTitle(), item.getLink(),
							item.getDescription(), item.getPubdate(), item
							.getImgUrl());
					rssDb.addSite(site);
					// Log.d("LINK",item.getLink());
				}

				// updating listview
				// Get All Website for Database
				List<WebSite> websites = rssDb.getAllSitesByID();
				for (WebSite website : websites) {
					RSSItem newItem = new RSSItem(website.getId(), website
							.getTitle(), website.getLink(), website
							.getDescription(), website.getPubDate(), website
							.getImageLink());

					// Add RSSItem to RSSItems
					rssItemsDataBase.add(newItem);
					// Log.d("DATABASE", "TABLE = " +
					// rssItemsDataBase.get(0).getTitle());
				}
				if (viewGroup instanceof ListView) {

					ListNewsItemAdapter adapter = new ListNewsItemAdapter(
							MainActivity.activity,
							R.layout.preview_single_news_list_layout,
							(ArrayList<RSSItem>) rssItemsDataBase);

					((ListView) viewGroup).setAdapter(adapter);
				}
				if (viewGroup instanceof GridView) {
					GridNewsItemAdapter adapter = new GridNewsItemAdapter(
							MainActivity.activity,
							R.layout.preview_single_news_grid_layout,
							(ArrayList<RSSItem>) rssItemsDataBase);

					((GridView) viewGroup).setAdapter(adapter);
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

	protected String getRssUrl() {
		String url_name;
		switch (MainActivity.nameCategory) {

		case Homepage:
			url_name = MainActivity.activity.getString(R.string.rss_home_page);
			break;
		case Business:
			url_name = MainActivity.activity.getString(R.string.rss_business);
			break;
		case Car:
			url_name = MainActivity.activity.getString(R.string.rss_cars);
			break;
		case Chat:
			url_name = MainActivity.activity.getString(R.string.rss_chat);
			break;
		case Digital:
			url_name = MainActivity.activity.getString(R.string.rss_digital);
			break;
		case Entertainment:
			url_name = MainActivity.activity
			.getString(R.string.rss_entertainment);
			break;
		case Sports:
			url_name = MainActivity.activity.getString(R.string.rss_sports);
			break;
		case Funny:
			url_name = MainActivity.activity.getString(R.string.rss_funny);
			break;
		case Laws:
			url_name = MainActivity.activity.getString(R.string.rss_laws);
			break;
		case Life:
			url_name = MainActivity.activity.getString(R.string.rss_life);
			break;
		case News:
			url_name = MainActivity.activity.getString(R.string.rss_news);
			break;
		case Science:
			url_name = MainActivity.activity.getString(R.string.rss_science);
			break;
		case Social:
			url_name = MainActivity.activity.getString(R.string.rss_social);
			break;
		case Travelling:
			url_name = MainActivity.activity.getString(R.string.rss_travelling);
			break;
		case World:
			url_name = MainActivity.activity.getString(R.string.rss_world);
			break;
		default:
			url_name = MainActivity.activity.getString(R.string.rss_home_page);

		}
		return url_name;
	}
}
