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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.examples.HtmlToPlainText;

import ngo.vnexpress.reader.Constant;
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
//		 URL url = null;
//		try {
//			url = new URL(link);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		   HttpURLConnection urlConnection = null;
//		try {
//			urlConnection = (HttpURLConnection) url.openConnection();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		   try {
//		     InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//		     result = convertStreamToString(in);
//		     //result = getContent(result);
//		     //Spanned a = Html.fromHtml(result);
//		     TextView tv = (TextView)rootView.findViewById(R.id.httpresponse);
//		     tv.setText(result);
//		     
//		   } catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally {
//		     urlConnection.disconnect();
//		   }
//		 
			    
		webView.loadUrl(link);
		//   webView.loadData(result, "text/html; charset=UTF-8",null);
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

	private String getContent(String input) {
		// TODO Auto-generated method stub
		
		String openTag = "<div class=\"title_news\">";
		
		String closeTag = "<!-- Javascript Parser -->";
		int openIndex = input.indexOf(openTag);
		int closeIndex = input.indexOf(closeTag);
		String result = input.substring(openIndex, closeIndex);
		
		return result;
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
//	private static String convertStreamToString(InputStream is) {
//	    /*
//	     * To convert the InputStream to String we use the BufferedReader.readLine()
//	     * method. We iterate until the BufferedReader return null which means
//	     * there's no more data to read. Each line will appended to a StringBuilder
//	     * and returned as String.
//	     */
//	    BufferedReader reader = null;
//		try {
//			reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	    StringBuilder sb = new StringBuilder();
//
//	    String line = null;
//	    try {
//	        while ((line = reader.readLine()) != null) {
//	            sb.append(line + "\n");
//	        }
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    } finally {
//	        try {
//	            is.close();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	    }
//	    return sb.toString();
//	}
}
