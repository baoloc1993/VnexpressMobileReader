package ngo.vnexpress.reader.RSS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.NameCategories;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.Adapters.GridNewsItemAdapter;
import ngo.vnexpress.reader.Adapters.ListNewsItemAdapter;
import ngo.vnexpress.reader.BasicFunctions.BasicFunctions;
import ngo.vnexpress.reader.libs.actionbarpulltorefresh.library.PullToRefreshLayout;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

public class LoadRSSFeedItemsService extends LoadRSSFeedItems {

	
	public LoadRSSFeedItemsService(ViewGroup viewGroup,
			PullToRefreshLayout pullToRefreshLayout) {
		super(viewGroup, pullToRefreshLayout);
		// TODO Auto-generated constructor stub
	}
	
	public LoadRSSFeedItemsService() {
		super(null, null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}
	/**
	 * getting all recent articles and showing them in listview
	 * */
	@Override
	protected String doInBackground(String... args) {
		MainActivity.newArticlePerCate.clear();
		
		 //newArticlePerCate 
		//Go through all categories inside an Asyntask
		for (final NameCategories name : NameCategories.values()){
			
			//Delay 1s to synchronize 2 threads
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MainActivity.nameCategory = name;
			
			
			if (BasicFunctions.isConnectingToInternet(MainActivity.activity.getApplicationContext())) {
				
				rssItems = rssParser.getRSSFeedItems(getRssUrl());
				Collections.reverse(rssItems);
			}
	
			// updating UI from Background Thread
			
			MainActivity.activity.runOnUiThread(new Runnable() {
	
				@Override
				public void run() {
					
					RSSDatabaseHandler rssDb = new RSSDatabaseHandler(
							MainActivity.activity);
					
					//Size of database before add new post
					int oldSizeDatabase = rssDb.getDatabaseSize();
	
					/**
					 * Updating parsed items into listview
					 * */
					
					// NO INTERNET -> RSSITEMS is emtpy
						for (RSSItem item : rssItems) {
							
							// ADD EACH ITEM INTO DATABASE
							WebSite site = new WebSite(
									item.getTitle(), 
									item.getLink(), 
									item.getDescription(), 
									item.getPubdate(), 
									item.getImgUrl());
							rssDb.addSite(site);
							 //Log.d("LINK","LINK + " + item.getLink());
						}
				
					//Size of database after add new post
					int newSizeDatabase = rssDb.getDatabaseSize();
					
					//The number of new article
					int newArticle = 0;
					MainActivity.numberNewPost += newSizeDatabase - oldSizeDatabase;
					newArticle = newSizeDatabase-oldSizeDatabase;
					

					MainActivity.newArticlePerCate.put(name, newArticle);
					
					Log.d("DEBUG", "CATE + " + MainActivity.nameCategory.toString() + "  " 
							+ String.valueOf(newSizeDatabase) + " - " + String.valueOf(oldSizeDatabase));
					// MainActivity.rssItems = rssItems;
				}
			});
		}
		return null;
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	@Override
	protected void onPostExecute(String args) {
		// dismiss the dialog after getting all products
		

	}
	
	public String getRssUrl(){
		return super.getRssUrl();
		
	}
	
}
