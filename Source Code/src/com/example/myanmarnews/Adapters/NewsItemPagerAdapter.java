package com.example.myanmarnews.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class NewsItemPagerAdapter extends FragmentPagerAdapter {

	public NewsItemPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		return null;
		// TODO Auto-generated method stub
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below).
		//return DisplayFullNewsFragment.newInstance(position + 1);
		//return DisplayFullNewsFragment.newInstance(position) + 1);
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	
//	@Override
//	public CharSequence getPageTitle(int position) {
//		Locale l = Locale.getDefault();
//		switch (position) {
//		case 0:
//			return getString(R.string.title_section1).toUpperCase(l);
//		case 1:
//			return getString(R.string.title_section2).toUpperCase(l);
//		case 2:
//			return getString(R.string.title_section3).toUpperCase(l);
//		}
//		return null;
//	}

}
