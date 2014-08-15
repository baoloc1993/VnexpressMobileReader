package ngo.vnexpress.reader.Fragments;

/**
 * Display the detail webview of the articles
 */
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ngo.vnexpress.reader.Constant;
import ngo.vnexpress.reader.LoadWeb;
import ngo.vnexpress.reader.MainActivity;
import ngo.vnexpress.reader.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DisplayWebNewsFragment extends android.support.v4.app.Fragment {
	public static String htmlContent = "";
	public static String KEY_SITE_LINK = "";
	private ProgressDialog progressDiaLog;
	private ProgressBar progressBar;
	private String result;

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
		progressBar = (ProgressBar) rootView.findViewById(R.id.webProgressBar);

		// Get SITE_LINK sent from other fragment
		Bundle args = this.getArguments();
		final String link = args.getString(KEY_SITE_LINK);

		// Log.d("DEBUG", "LINK = " + link);
		final WebView webView = (WebView) rootView
				.findViewById(R.id.single_web_view);

		// Set parameter of webview
		webView.getSettings().setJavaScriptEnabled(true);

		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				progressBar.setProgress(newProgress);
				super.onProgressChanged(view, newProgress);
			}

		});
		webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				progressBar.setVisibility(View.INVISIBLE);
			}
		});
		new LoadWeb(link, webView).execute();

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

	public void setValue(int progress) {

		// item selected
		progressBar.setVisibility(View.VISIBLE);
		progressBar.setProgress(progress);

	}

}
