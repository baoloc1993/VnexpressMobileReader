package ngo.vnexpress.reader.rss;

/**
 * Load RSSITEM from RSS link.
 * Run on foreground service
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.adapters.ListNewsItemAdapter;
import ngo.vnexpress.reader.basic.BasicFunctions;
import ngo.vnexpress.reader.items.RSSItem;
import ngo.vnexpress.reader.managers.RSSItemManager;
import ngo.vnexpress.reader.views.LoadingComponent;
import ngo.vnexpress.reader.views.LoadingRecyclerView;

public class LoadRSSFeedItems extends AsyncTask<String, String, String> {

	List<RSSItem> rssItems = new ArrayList<>();
	private ProgressDialog pDialog;
	RSSParser rssParser = new RSSParser();
	private LoadingComponent itemsPlaceHolder;


	public LoadRSSFeedItems(LoadingComponent itemsPlaceHolder
			) {
		// TODO Auto-generated constructor stub
		this.itemsPlaceHolder = itemsPlaceHolder;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		itemsPlaceHolder.showLoading();

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
		if (BasicFunctions.isConnectingToInternet(MainActivity.activity)) {
			// Log.d("DEBUG", "CATE = " + MainActivity.nameCategory.toString() +
			// " " + getRssUrl());
			rssParser.getRSSFeedItems(getRssUrl());
//			Collections.reverse(rssItems);
		}

		// updating UI from Background Thread

		// InputStream input = null;
		MainActivity.activity.runOnUiThread(() -> {
            // RSSDatabaseHandler rssDb = new RSSDatabaseHandler(
            // MainActivity.activity);
            rssItems = RSSItemManager.getInstance(MainActivity.currentSource).getItemByCurrentCategory();
            if (itemsPlaceHolder instanceof LoadingRecyclerView) {

                ListNewsItemAdapter adapter = new ListNewsItemAdapter(

						rssItems);

                ((LoadingRecyclerView) itemsPlaceHolder).recyclerView.setAdapter(adapter);
            }


            // MainActivity.rssItems = rssItems;
        });
		return null;
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	@Override
	protected void onPostExecute(String args) {
		// dismiss the dialog after getting all products
		itemsPlaceHolder.hideLoading();

	}

	protected String getRssUrl() {


		return RSSItemManager.getInstance(MainActivity.currentSource).getCurrentUrl();
	}
}
