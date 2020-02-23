package com.alfredposclient.adapter;

import java.util.Collections;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class HomePageViewPagerAdapter extends PagerAdapter {
	private List<View> pageViews;

	public HomePageViewPagerAdapter(List<View> pageViews) {
		if (pageViews == null)
			pageViews = Collections.emptyList();
		this.pageViews = pageViews;
	}

	@Override
	public int getCount() {
		return pageViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(pageViews.get(arg1));
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		View view = pageViews.get(arg1);
		try {
			if (view.getParent() != null) {
				((ViewPager) view.getParent()).removeView(view);
			}
			((ViewPager) arg0).addView(view, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;

	}
}
