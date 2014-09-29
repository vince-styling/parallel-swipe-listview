package com.vincestyling.parallel_swipe_listview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends FragmentActivity {
	protected List<FragmentCreator> menuList;

	private TabIndicator pageIndicator;
	private ViewPager viewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		menuList = new LinkedList<FragmentCreator>();
		menuList.add(new FragmentCreator(RecentView.class, R.string.pager_item_recent));
		menuList.add(new FragmentCreator(RecentView.class, R.string.pager_item_contacts));
		menuList.add(new FragmentCreator(RecentView.class, R.string.pager_item_directory));


		viewPager = (ViewPager) findViewById(R.id.mainContent);

		PagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return menuList.size();
			}

			@Override
			public Fragment getItem(int position) {
				return menuList.get(position).newInstance();
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return getResources().getString(menuList.get(position).getTitleResId());
			}
		};

		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				MainActivity.this.onPageScrolled(position, positionOffset);
			}
		});
		viewPager.setAdapter(adapter);


		pageIndicator = (TabIndicator) findViewById(R.id.pageIndicator);
		pageIndicator.setViewPager(viewPager);
	}

	public void onPageScrolled(int position, float positionOffset) {
		pageIndicator.onPageScrolled(position, positionOffset);
	}

	private class FragmentCreator {
		private Class fragClass;
		private int titleResId;

		public FragmentCreator(Class fragClass, int titleResId) {
			this.fragClass = fragClass;
			this.titleResId = titleResId;
		}

		public Fragment newInstance() {
			try {
				return (Fragment) fragClass.newInstance();
			} catch (Exception e) {}
			return null;
		}

		public Class getFragClass() {
			return fragClass;
		}

		public int getTitleResId() {
			return titleResId;
		}
	}
}
