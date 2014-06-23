package com.vnexpressandroidreader.Fragments;

import com.example.myanmarnews.R;
import com.vnexpressandroidreader.Constant;
import com.vnexpressandroidreader.MainActivity;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DisplayWebNewsFragment extends android.support.v4.app.Fragment {

	public static String KEY_SITE_LINK = "";
	private MainActivity myContext;

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

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setGeolocationEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDomStorageEnabled(true);

		webView.setWebChromeClient(new WebChromeClient() {
		});

		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl(link);

		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				Log.i(getTag(), "keyCode: " + keyCode);
				if (keyCode == KeyEvent.KEYCODE_BACK) {

					Bundle args = new Bundle();
					// String fragment_type = args.getString(ARG_TYPE_FRAGMENT);

					FragmentManager fragmentManager = getActivity()
							.getFragmentManager();

					// displayFullNewsFragment.setArguments(args);
					ListViewNewsLiveFragment list = new ListViewNewsLiveFragment();
					GridViewNewsLiveFragment grid = new GridViewNewsLiveFragment();
					// Log.d("DEBUG", "LIST = " + fragment_type);
					// Back to ListFragment
					if (MainActivity.curViewGroup == Constant.List) {
						fragmentManager.beginTransaction()
								.replace(R.id.container, list).commit();
					}

					// Back to GridFragment
					if (MainActivity.curViewGroup == Constant.Grid) {
						Log.d("aaaa", "GRID");
						fragmentManager.beginTransaction()
								.replace(R.id.container, grid).commit();
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
		myContext = (MainActivity) activity;
		super.onAttach(activity);
	}

}
