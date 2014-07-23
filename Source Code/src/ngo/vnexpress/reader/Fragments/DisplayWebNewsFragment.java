package ngo.vnexpress.reader.Fragments;

/**
 * Display the detail webview of the articles
 */
import ngo.vnexpress.reader.Constant;
import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class DisplayWebNewsFragment extends android.support.v4.app.Fragment {

	public static String KEY_SITE_LINK = "";
	private ProgressDialog progressDiaLog;
	private ProgressBar progressBar;
	public DisplayWebNewsFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.preview_single_news_web_view,
				container, false);
		/**
		 * Progress Bar
		 * 
		 */
		progressBar = (ProgressBar)rootView.findViewById(R.id.webProgressBar);
		
		
		// Get SITE_LINK sent from other fragment
		Bundle args = this.getArguments();
		final String link = args.getString(KEY_SITE_LINK);

		// Log.d("DEBUG", "LINK = " + link);
		final WebView webView = (WebView) rootView
				.findViewById(R.id.single_web_view);

		// Set parameter of webview
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setGeolocationEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		webView.setWebChromeClient(new MyWebViewClient() {
		});

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// do your stuff here

			}
		});
		webView.loadUrl(link);

		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();

		// Set the back button
		rootView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				// Log.i(getTag(), "keyCode: " + keyCode);
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
						// Log.d("aaaa", "GRID");
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
//	public void setValue(int progress) {
//		
//					// item selected
//					progressBar.setVisibility(View.VISIBLE);
//					progressBar.setProgress(progress);
//	}
	private class MyWebViewClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			progressBar.setProgress(newProgress);
			super.onProgressChanged(view, newProgress);
		}
	}
}
