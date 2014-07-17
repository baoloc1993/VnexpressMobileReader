package ngo.vnexpress.reader.BasicFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ngo.vnexpress.reader.Constant;
import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import ngo.vnexpress.reader.Fragments.DisplaySwipeViewNewsFragment;
import ngo.vnexpress.reader.RSS.LoadRSSFeedItems;
import ngo.vnexpress.reader.RSS.LoadSingleItems;
import ngo.vnexpress.reader.RSS.RSSDatabaseHandler;
import ngo.vnexpress.reader.RSS.RSSItem;
import ngo.vnexpress.reader.RSS.RSSParser;
import ngo.vnexpress.reader.RSS.WebSite;
import ngo.vnexpress.reader.libs.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import ngo.vnexpress.reader.libs.actionbarpulltorefresh.library.Options;
import ngo.vnexpress.reader.libs.actionbarpulltorefresh.library.PullToRefreshLayout;
import ngo.vnexpress.reader.libs.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

/**
 *
 * @author Fabio Ngo Class store basic functions ( most-used functions)
 */
public class BasicFunctions {
	/*
	 * Resize Image View to the size
	 */
	public static void ResizeImageView(int width, int height,
			ImageView imageView) {
		Drawable drawable = imageView.getDrawable();
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height,
				true);
		imageView.setImageBitmap(scaledBitmap);
	}

	public static void ResizeImageView(int size, ImageView imageView) {
		Drawable drawable = imageView.getDrawable();
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, size, size,
				true);
		imageView.setImageBitmap(scaledBitmap);
	}

	/**
	 * Convert DP to PX
	 */
	public static int dpToPx(int dp, Context context) {
		float density = context.getResources().getDisplayMetrics().density;
		return Math.round(dp * density);
	}

	public static void IniPullToRefresh(final Activity activity,
			ViewGroup viewGroup, View view, final TimerTask timerTask,
			final PullToRefreshLayout mPullToRefreshLayout) {

		ActionBarPullToRefresh.from(activity).insertLayoutInto(viewGroup)
		// We need to insert the PullToRefreshLayout into the Fragment's
		// ViewGroup
		.theseChildrenArePullable(view)
		// We need to mark the ListView and it's Empty View as pullable
		// This is because they are not direct children of the
		// ViewGroup
		.options(
				Options.create()
				.refreshingText("Fetching News...")
				.pullText("Pull me down to update!")
				.releaseText("Release to update!!!")
				.titleTextColor(android.R.color.black)
				.progressBarColor(
						android.R.color.holo_orange_light)
						.headerBackgroundColor(
								android.R.color.holo_blue_light)
								.progressBarStyle(
										Options.PROGRESS_BAR_STYLE_OUTSIDE)
										.build()).listener(new OnRefreshListener() {
					@Override
					public void onRefreshStarted(View view) {
						if (isConnectingToInternet(activity
								.getApplicationContext())) {
							Timer timer = new Timer();
							TimerTask task = new TimerTask() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									timerTask.run();
								}
							};

							timer.schedule(task, 1000);
						} else {

							Toast.makeText(
									activity.getApplicationContext(),
									"INTERNET IS NOT AVAILABLE. THE OLD DATA WILL BE USED ",
									Toast.LENGTH_LONG).show();

							mPullToRefreshLayout.setRefreshComplete();
						}
					}
				}).setup(mPullToRefreshLayout);
	}

	// CHECK IF IS CONNECTION TO INTERNET OR NOT
	public static boolean isConnectingToInternet(Context context) {

		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (NetworkInfo element : info) {
					if (element.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}

		}
		return false;
	}

	/**
	 * Return OnItemClickListener which lead to detailed articles after clicking
	 */
	public static OnItemClickListener createOnItemClickListener() {
		OnItemClickListener onItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				new LoadSingleItems(parent, position, id).execute();
				//

			};
		};
		return onItemClickListener;
	}


}
