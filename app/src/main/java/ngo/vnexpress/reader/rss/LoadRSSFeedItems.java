package ngo.vnexpress.reader.rss;

/**
 * Load RSSITEM from RSS link.
 * Run on foreground service
 */

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.basic.BasicFunctions;
import ngo.vnexpress.reader.items.RSSItem;
import ngo.vnexpress.reader.managers.RSSItemManager;
import ngo.vnexpress.reader.views.LoadingComponent;

public class LoadRSSFeedItems extends AsyncTask<String, String, String> {

    private List<RSSItem> rssItems = new ArrayList<>();
    private RSSParser rssParser = new RSSParser();
    private LoadingComponent itemsPlaceHolder;
    private OnRSSFeedItemLoadedListener onRSSFeedItemLoadedListener;

    public LoadRSSFeedItems(LoadingComponent itemsPlaceHolder, OnRSSFeedItemLoadedListener onRSSFeedItemLoadedListener) {
        // TODO Auto-generated constructor stub
        this.itemsPlaceHolder = itemsPlaceHolder;
        this.onRSSFeedItemLoadedListener = onRSSFeedItemLoadedListener;
    }

    /**
     * getting all recent articles and showing them in list view
     */
    @Override
    protected String doInBackground(String... args) {
        // rss link url
        // String rss_url = args[0];
        // IF INTERNET CONNECTING, RETRIVE DATA FROM RSS LINK
        // list of rss items
        if (BasicFunctions.isConnectingToInternet()) {
            // Log.d("DEBUG", "CATE = " + MainActivity.nameCategory.toString() +
            // " " + getRssUrl());
            rssParser.getRSSFeedItems(getRssUrl());
//			Collections.reverse(rssItems);
        }

        // updating UI from Background Thread
        rssItems = RSSItemManager.getInstance(MainActivity.currentSource).getItemByCurrentCategory();
        // InputStream input = null;
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        itemsPlaceHolder.showLoading();

    }

    /**
     * After completing background task Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String args) {
        onRSSFeedItemLoadedListener.onRSSFeedItemLoaded(rssItems);
        // dismiss the dialog after getting all products
        itemsPlaceHolder.hideLoading();

    }

    private String getRssUrl() {


        return RSSItemManager.getInstance(MainActivity.currentSource).getCurrentUrl();
    }

    public interface OnRSSFeedItemLoadedListener {
        void onRSSFeedItemLoaded(List<RSSItem> rssItems);
    }
}
