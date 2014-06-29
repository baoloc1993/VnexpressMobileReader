package ngo.vnexpress.reader.RSS;

/**
 * Load RSSITEM from RSS link.
 * Run on background service
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import backgroundnotification.NotificationService;
import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.NameCategories;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.Adapters.GridNewsItemAdapter;
import ngo.vnexpress.reader.Adapters.ListNewsItemAdapter;
import ngo.vnexpress.reader.BasicFunctions.BasicFunctions;
import ngo.vnexpress.reader.libs.actionbarpulltorefresh.library.PullToRefreshLayout;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

public class LoadRSSFeedItemsService extends LoadRSSFeedItems implements ServiceConnection{

	public LoadRSSFeedItemsService(ViewGroup viewGroup,
			PullToRefreshLayout pullToRefreshLayout) {
		super(viewGroup, pullToRefreshLayout);
		// TODO Auto-generated constructor stub
	}
	
	public LoadRSSFeedItemsService() {
		
		super(null, null);
		//MainActivity.numberNewPost= 0;
		//MainActivity.check = false;
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
		// rss link url
		// String rss_url = args[0];
		// IF INTERNET CONNECTING, RETRIVE DATA FROM RSS LINK
		// list of rss items
		
		//Reset number of new post
		
		
		//Go through all categories inside an Asyntask
		for (NameCategories name : NameCategories.values()){
			 try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			MainActivity.nameCategory = name;
		//	Log.d("DEBUG", "CATE + " + MainActivity.nameCategory.toString());
			if (BasicFunctions.isConnectingToInternet(MainActivity.activity.getApplicationContext())) {
				
				rssItems = rssParser.getRSSFeedItems(getRssUrl());
				
				Collections.reverse(rssItems);
			}
	
			// updating UI from Background Thread
			
			MainActivity.activity.runOnUiThread(new Runnable() {
	
				@Override
				public void run() {
					int oldSizeDatabase = 0;
					int newSizeDatabase = 0;
					RSSDatabaseHandler rssDb = new RSSDatabaseHandler(
							MainActivity.activity);
					
					//Size of database before add new post
					oldSizeDatabase = rssDb.getDatabaseSize();
	
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
					newSizeDatabase = rssDb.getDatabaseSize();
					
					//The number of new post
					MainActivity.numberNewPost += newSizeDatabase - oldSizeDatabase; 
//					Log.d("DEBUG", "CATE + " + MainActivity.nameCategory.toString() + "  " 
//							+ String.valueOf(newSizeDatabase) + " " + String.valueOf(oldSizeDatabase) + " " +  String.valueOf(MainActivity.numberNewPost));
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
		//MainActivity.check = true;
		//Log.d ("DEBUG", "POST");
	//	NotificationService.
	}
	
	public String getRssUrl(){
		return super.getRssUrl();
		
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		
	}
	
}
