package ngo.vnexpress.reader.Fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.BasicFunctions.BasicFunctions;
import ngo.vnexpress.reader.RSS.LoadRSSFeedItems;
import ngo.vnexpress.reader.RSS.RSSFeed;
import ngo.vnexpress.reader.RSS.RSSItem;
import ngo.vnexpress.reader.RSS.RSSParser;
import ngo.vnexpress.reader.libs.actionbarpulltorefresh.library.PullToRefreshLayout;

public class GridViewNewsLiveFragment extends Fragment {
	public static GridView gridNews;
	 
    // Array list for list view
    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String,String>>();
 
    RSSParser rssParser = new RSSParser();
     
    List<RSSItem> rssItems = MainActivity.rssItems;
 //
    RSSFeed rssFeed;


	private PullToRefreshLayout mPullToRefreshLayout;
     

	public GridViewNewsLiveFragment() {
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid_news_layout, container, false);
        
        gridNews = (GridView)rootView.findViewById(R.id.gridNews);
        
     
 
        
        
        /**
         * Calling a backgroung thread will loads recent articles of a website
         * @param rss url of website
         * */
        new LoadRSSFeedItems(gridNews,mPullToRefreshLayout).execute();
         
       
        gridNews.setOnItemClickListener(BasicFunctions.createOnItemClickListener());
        
      
        return rootView;
    }
	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	super.onViewCreated(view, savedInstanceState);
    	
    	gridNews = (GridView)view.findViewById(R.id.gridNews);
    	
    	

		// We need to create a PullToRefreshLayout manually
		
		
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new LoadRSSFeedItems(gridNews,mPullToRefreshLayout).execute();
			}
		};
		mPullToRefreshLayout = new PullToRefreshLayout(view.getContext());
		// We can now setup the PullToRefreshLayout
		BasicFunctions.IniPullToRefresh(getActivity(), (ViewGroup) view, (View)gridNews, timerTask,mPullToRefreshLayout);
    }
	 
	
}