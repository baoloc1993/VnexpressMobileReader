package com.vnexpressandroidreader.Fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.myanmarnews.R;
import com.vnexpressandroidreader.Adapters.SocialNetworkItemAdapter;
import com.vnexpressandroidreader.Items.SocialNetworkItem;

public class SocialNetworkFragment extends Fragment {
	static Bundle savedInstanceState;
	private static int position = 0;
	GridView socialNetworkHeader;
	ArrayList<SocialNetworkItem> socialNetworkItems;
	WebView webView;

	public SocialNetworkFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SocialNetworkFragment.savedInstanceState = savedInstanceState;
		View rootView = inflater.inflate(R.layout.social_network_layout,
				container, false);

		/**
		 * Webview to show webpage
		 * 
		 */
		webView = (WebView) rootView.findViewById(R.id.webView);
		webView.setPadding(0, 0, 0, 0);
		webView.setWebChromeClient(new MyWebViewClient());
		webView.setWebViewClient(new WebViewClient());

		webView.getSettings().setJavaScriptEnabled(true);
		// OR, you can also load from an HTML string:

		/**
		 * Social Network headers
		 */
		socialNetworkItems = new ArrayList<SocialNetworkItem>();
		/**
		 * 
		 * FACEBOOK
		 */
		socialNetworkItems.add(new SocialNetworkItem("http://m.facebook.com",
				R.drawable.facebook_icon, Color.rgb(70, 110, 169)));
		/**
		 * GOOGLE+
		 */
		socialNetworkItems.add(new SocialNetworkItem(
				"http://plus.google.com/app/basic",
				R.drawable.google_plus_icon, Color.rgb(228, 96, 68)));
		/**
		 * TWITTER
		 */
		socialNetworkItems.add(new SocialNetworkItem("http://twitter.com",
				R.drawable.twitter_icon, Color.rgb(0, 172, 237)));

		/**
		 * Show social network header in gridview
		 */
		socialNetworkHeader = (GridView) rootView
				.findViewById(R.id.socialNetworkHeader);
		// Set the number of column in grid view.
		// It depends on the number of items to ensure that
		// there is one row in gridview
		socialNetworkHeader.setNumColumns(socialNetworkItems.size());
		socialNetworkHeader.setAdapter(new SocialNetworkItemAdapter(
				getActivity(), R.layout.single_social_network_header,
				socialNetworkItems));
		socialNetworkHeader.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SocialNetworkFragment.position = position;
				// Get URL to load in webview
				String url = socialNetworkItems.get(position).getUrl();
				webView.loadUrl(url);
			}
		});
		// Perform the click of the fist social network to make webview load
		// default url
		socialNetworkHeader.performItemClick(socialNetworkHeader, 0,
				socialNetworkHeader.getItemIdAtPosition(0));
		return rootView;
	}

	private class MyWebViewClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			SocialNetworkFragment.this.setValue(newProgress);
			super.onProgressChanged(view, newProgress);
		}
	}

	public void setValue(int progress) {
		if (socialNetworkHeader != null) {
			for (int i = 0; i < socialNetworkItems.size(); i++) {
				View v = socialNetworkHeader.getChildAt(i);
				// Item not selected
				ProgressBar progressBar = (ProgressBar) v
						.findViewById(R.id.progressBar);
				if (i != position) {
					progressBar.setVisibility(View.INVISIBLE);
				} else {
					// item selected
					progressBar.setVisibility(View.VISIBLE);
					progressBar.setProgress(progress);
				}

			}

		}

	}

}
