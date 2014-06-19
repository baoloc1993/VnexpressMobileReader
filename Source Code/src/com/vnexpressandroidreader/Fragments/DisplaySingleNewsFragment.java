package com.vnexpressandroidreader.Fragments;

import com.example.myanmarnews.R;
import com.vnexpressandroidreader.MainActivity;
import com.vnexpressandroidreader.RSS.RSSDatabaseHandler;
import com.vnexpressandroidreader.RSS.WebSite;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DisplaySingleNewsFragment extends Fragment {
	
	public static String KEY_SITE_LINK = "";
	private MainActivity myContext;
	public DisplaySingleNewsFragment(){
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.preview_single_news_web_view, container,
				false);
		
		//Get SITE_LINK sent from other fragment
		Bundle args = this.getArguments();
		final String link = args.getString(KEY_SITE_LINK);
		//Log.d("DEBUG", "LINK = " + link);
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
					//Log.i(getTag(), "keyCode: " + keyCode);
		            if( keyCode == KeyEvent.KEYCODE_BACK ) {
		                  //  Log.i(getTag(), "onKey Back listener is working!!!");
		            	RSSDatabaseHandler RssDb = new RSSDatabaseHandler(getActivity());
		            	WebSite website = RssDb.getSiteByLink(link);
		            	
		            	Bundle args = new Bundle();
		                args.putInt(DisplayFullNewsFragment.ARG_ID, website.getId());
		                
		                FragmentManager fragmentManager = getActivity().getFragmentManager();
		                DisplayFullNewsFragment displayFullNewsFragment = new DisplayFullNewsFragment();
		                displayFullNewsFragment.setArguments(args);
		                
		                //Go to DisplayFullNewsFragment
		    	        fragmentManager.beginTransaction().replace(R.id.container, displayFullNewsFragment).commit();
		              //  getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
	    myContext=(MainActivity) activity;
	    super.onAttach(activity);
	}
	
	
}
