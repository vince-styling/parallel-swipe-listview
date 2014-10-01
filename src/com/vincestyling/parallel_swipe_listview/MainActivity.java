package com.vincestyling.parallel_swipe_listview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends FragmentActivity {
	protected List<FragmentCreator> menuList;

	private FragmentPagerAdapter adapter;
	private ViewPager viewPager;

	private TabIndicator pageIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		menuList = new LinkedList<FragmentCreator>();
		menuList.add(new FragmentCreator(RecentView.class, R.string.pager_item_recent));
		menuList.add(new FragmentCreator(RecentView.class, R.string.pager_item_contacts));
		menuList.add(new FragmentCreator(RecentView.class, R.string.pager_item_directory));


		viewPager = (ViewPager) findViewById(R.id.mainContent);

		adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
			public void onPageScrollStateChanged(int state) {
//				switch (state) {
//					case ViewPager.SCROLL_STATE_DRAGGING:
//						Log.e("", "dragging");
//						break;
//					case ViewPager.SCROLL_STATE_SETTLING:
//						Log.e("", "settling");
//						break;
//					case ViewPager.SCROLL_STATE_IDLE:
//						Log.e("", "idle");
//						break;
//				}
				if (state == ViewPager.SCROLL_STATE_IDLE) {
//					Log.e("", String.format("lastPositionOffset : %f", lastPositionOffset));
					lastPositionOffset = 0;
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//				Log.e("", String.format("positionOffsetPixels : %d", positionOffsetPixels));
				MainActivity.this.onPageScrolled(position, positionOffset);
			}
		});
		viewPager.setAdapter(adapter);


		pageIndicator = (TabIndicator) findViewById(R.id.pageIndicator);
		pageIndicator.setViewPager(viewPager);
	}

	private float lastPositionOffset;

	public void onPageScrolled(int position, float positionOffset) {
		if (positionOffset == 0 || positionOffset == lastPositionOffset) return;
		pageIndicator.onPageScrolled(position, positionOffset);

//		Log.e("", String.format("position : %d, positionOffset : %f, direction : %s",
//				position, positionOffset, positionOffset > lastPositionOffset ? "Forward" : "Backward"));

		boolean isForward = positionOffset > lastPositionOffset;
		if (isForward) position++;
		RecentView frag = (RecentView) adapter.instantiateItem(viewPager, position);
		frag.onPageScrolled(isForward, positionOffset);

		lastPositionOffset = positionOffset;
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
