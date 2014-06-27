package ngo.vnexpress.reader.Fragments;
/**
 * Display the detail webview of the articles
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ngo.vnexpress.reader.Constant;
import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DisplayWebNewsFragment extends android.support.v4.app.Fragment {

	public static String KEY_SITE_LINK = "";
	public DisplayWebNewsFragment() {
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.preview_single_news_web_view,
				container, false);

		// Get SITE_LINK sent from other fragment
		Bundle args = this.getArguments();
		final String link = args.getString(KEY_SITE_LINK);
		// Log.d("DEBUG", "LINK = " + link);
		WebView webView = (WebView) rootView.findViewById(R.id.single_web_view);

		//Set parameter of webview
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setGeolocationEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		webView.setWebChromeClient(new WebChromeClient() {
		});

		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl(link);

		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		
		//Set the back button
		rootView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				Log.i(getTag(), "keyCode: " + keyCode);
				if (keyCode == KeyEvent.KEYCODE_BACK) {

					FragmentManager fragmentManager = getActivity()
							.getFragmentManager();

					// displayFullNewsFragment.setArguments(args);
					ListViewNewsLiveFragment list = new ListViewNewsLiveFragment();
					GridViewNewsLiveFragment grid = new GridViewNewsLiveFragment();
					// Log.d("DEBUG", "LIST = " + fragment_type);
					// Back to ListFragment
					if (MainActivity.curViewGroup == Constant.List) {
						list.setHasOptionsMenu(true);
						fragmentManager.beginTransaction()
								.replace(R.id.container, list).commit();
						MainActivity.currentFragment = Constant.List;
					}

					// Back to GridFragment
					if (MainActivity.curViewGroup == Constant.Grid) {
						Log.d("aaaa", "GRID");
						grid.setHasOptionsMenu(true);
						fragmentManager.beginTransaction()
								.replace(R.id.container, grid).commit();
						MainActivity.currentFragment = Constant.Grid;
					}
					// getFragmentManager().popBackStack(null,
					// FragmentManager.POP_BACK_STACK_INCLUSIVE);
					return true;
				} else {
					return false;
				}
			}
		});
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	//Share button
	
	public static void Share() throws IOException {

		/**
		 * Take screenshot
		 */
		Bitmap bitmap;
		View v = MainActivity.activity.getWindow().getDecorView();
		v.setDrawingCacheEnabled(true);
		bitmap = Bitmap.createBitmap(v.getDrawingCache());
		v.setDrawingCacheEnabled(false);

		OutputStream fout;
		File imageFile = new File(
				MainActivity.activity
						.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE),
				"share.png");

		fout = new FileOutputStream(imageFile);
		bitmap.compress(Bitmap.CompressFormat.PNG, 90, fout);
		fout.flush();
		fout.close();

		/**
		 * Share
		 */
		File filePath = new File(
				MainActivity.activity
						.getExternalFilesDir(Context.ACCESSIBILITY_SERVICE),
				"share.png");
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(filePath)); // optional//use													// image
		shareIntent.setType("image/jpeg");
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		MainActivity.activity.startActivity(Intent.createChooser(shareIntent,
				MainActivity.activity.getString(R.string.shareText)));
	}

}
