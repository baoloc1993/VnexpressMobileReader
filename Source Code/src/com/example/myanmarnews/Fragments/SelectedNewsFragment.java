package com.example.myanmarnews.Fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myanmarnews.R;
import com.example.myanmarnews.Items.NewsItem;

public class SelectedNewsFragment extends Fragment {
	private ListView listNews;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */

	public SelectedNewsFragment() {
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_news_layout, container, false);
        listNews = (ListView)rootView.findViewById(R.id.listNews);
        ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
        for(int i=0;i<10;i++){
	        newsItems.add(new NewsItem(
	        		"Title " + String.valueOf(newsItems.size()+1),
	        		R.drawable.ic_launcher,
	        		"a asldkasd asdlkjasldj asldkjasjd aslkdalkjsd qwqw 1212 3lkas asldka1oijd Content " + String.valueOf(newsItems.size()+1),
					"Time Stamp "+ String.valueOf(newsItems.size()+1)
	        ));
        }
       // listNews.setAdapter(new ListNewsItemAdapter(rootView.getContext(), R.layout.preview_single_news_layout, newsItems));
        return rootView;
    }
	// @Override
	// public void onAttach(Activity activity) {
	// super.onAttach(activity);
	// ((MainActivity) activity).onSectionAttached(
	// getArguments().getInt(ARG_SECTION_NUMBER));
	// }
}