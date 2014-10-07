package com.vincestyling.parallel_swipe_listview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends FragmentActivity implements TouchDeltaCallback, ViewPager.OnPageChangeListener, ViewPager.PageTransformer {
	private TabIndicator pageIndicator;
	private SelfViewPager viewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		viewPager = (SelfViewPager) findViewById(R.id.mainContent);
		viewPager.setPageTransformer(false, this);
		viewPager.setOnPageChangeListener(this);
		viewPager.setTouchDeltaCallback(this);

		final List<FragmentCreator> menuList = new LinkedList<FragmentCreator>();
		menuList.add(new FragmentCreator(RecentView.class, R.string.pager_item_recent));
		menuList.add(new FragmentCreator(ContactsView.class, R.string.pager_item_contacts));
		menuList.add(new FragmentCreator(DirectoryView.class, R.string.pager_item_directory));
		viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
		});

		pageIndicator = (TabIndicator) findViewById(R.id.pageIndicator);
		pageIndicator.setViewPager(viewPager);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		switch (state) {
			case ViewPager.SCROLL_STATE_SETTLING:
				viewPager.setIsDisableTouch(true);
				break;
			case ViewPager.SCROLL_STATE_IDLE:
				startingPosition = viewPager.getCurrentItem();
				viewPager.setIsDisableTouch(false);
				break;
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//		Log.e("", String.format("position : %d, positionOffsetPixels : %d", position, positionOffsetPixels));
		pageIndicator.onPageScrolled(position, positionOffset);
	}

	@Override
	public void onPageSelected(int position) {
//		Log.e("", String.format("onPageSelected position : %d", position));
//		startingPosition = position;
	}

	@Override
	public void transformPage(View page, float position) {
		if (position >= -1 && position <= 1) { // [-1,1]
			if (position < 0) {
				if (scrollDirection == SCROLL_DIRECTION_BACKWARD) {
//					Log.e("", String.format("backward view : %d, position %s", Integer.valueOf(page.getTag().toString()), position));
					transformListView(page, position);
				}
			} else if (position > 0) {
				if (scrollDirection == SCROLL_DIRECTION_FORWARD) {
//					Log.e("", String.format("forward view : %d, position %s", Integer.valueOf(page.getTag().toString()), position));
					transformListView(page, position);
				}
			} else {
//				Log.e("", String.format("else view : %d, position %s", Integer.valueOf(page.getTag().toString()), position));
				transformListView(page, 0);
			}
		}
	}

	private void transformListView(View page, float position) {
		if (Integer.valueOf(page.getTag().toString()) == startingPosition) return;
//		Log.e("", String.format("view : %d, position %s, startingPosition : %d", Integer.valueOf(page.getTag().toString()), position, startingPosition));

		ListView lsvContent = (ListView) page.findViewById(R.id.lsvContent);
		final int childCount = lsvContent.getChildCount();
		float offset = .5f * lsvContent.getWidth();
		for (int i = 0; i < childCount; i++) {
			View childView = lsvContent.getChildAt(i);
			float tranX = i * offset * position;
			childView.setTranslationX(tranX);
		}
	}

	private int startingPosition, scrollDirection;
	private final int SCROLL_DIRECTION_FORWARD  = 1;
	private final int SCROLL_DIRECTION_BACKWARD = 2;
	private final int SCROLL_DIRECTION_UNCHANGE = 0;

	@Override
	public void onTouchDeltaX(float deltaX) {
		if (deltaX < 0) { // next page
			scrollDirection = SCROLL_DIRECTION_FORWARD;
//			Log.e("", "forward");
		}
		else if (deltaX > 0) { // previous page
			scrollDirection = SCROLL_DIRECTION_BACKWARD;
//			Log.e("", "backward");
		} else {
			scrollDirection = SCROLL_DIRECTION_UNCHANGE;
		}
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
