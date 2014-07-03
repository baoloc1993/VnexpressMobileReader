package ngo.vnexpress.reader.Fragments;

/**
 * Display the ListView of articles
 * Each article has:
 *****	Title
 *		Description
 *		Image
 *		PublicDate
 */
import java.util.TimerTask;

import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.BasicFunctions.BasicFunctions;
import ngo.vnexpress.reader.RSS.LoadRSSFeedItems;
import ngo.vnexpress.reader.libs.actionbarpulltorefresh.library.PullToRefreshLayout;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListViewNewsLiveFragment extends Fragment {
	public static ListView listNews;
	private PullToRefreshLayout mPullToRefreshLayout;
	ViewGroup viewGroup;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */

	public ListViewNewsLiveFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.list_news_layout, container,
				false);

		listNews = (ListView) rootView.findViewById(R.id.listNews);
		MainActivity.FirstOpen = true;

		/**
		 * Calling a backgroung thread will loads recent articles of a website
		 *
		 * @param rss
		 *            url of website
		 * */
		new LoadRSSFeedItems(listNews, mPullToRefreshLayout).execute();

		// selecting single ListView item
		// ListView lv = getListView();
		// Launching new screen on Selecting Single ListItem
		listNews.setOnItemClickListener(BasicFunctions
				.createOnItemClickListener());
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
				new LoadRSSFeedItems(listNews, mPullToRefreshLayout).execute();
			}
		};
		// We can now setup the PullToRefreshLayout
		BasicFunctions.IniPullToRefresh(getActivity(), (ViewGroup) view,
				(View) listNews, timerTask, mPullToRefreshLayout);
	}

}
