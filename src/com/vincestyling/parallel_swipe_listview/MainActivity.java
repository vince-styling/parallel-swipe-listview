package com.vincestyling.parallel_swipe_listview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ListView;

import java.util.*;

public class MainActivity extends FragmentActivity implements TouchDeltaCallback, ViewPager.PageTransformer, View.OnClickListener {
	protected List<FragmentCreator> menuList;

	private FragmentPagerAdapter adapter;
	private SelfViewPager viewPager;

	private TabIndicator pageIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		menuList = new LinkedList<FragmentCreator>();
		menuList.add(new FragmentCreator(RecentView.class, R.string.pager_item_recent));
		menuList.add(new FragmentCreator(ContactsView.class, R.string.pager_item_contacts));
		menuList.add(new FragmentCreator(DirectoryView.class, R.string.pager_item_directory));


		viewPager = (SelfViewPager) findViewById(R.id.mainContent);
		viewPager.setTouchDeltaCallback(this);

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
				switch (state) {
					case ViewPager.SCROLL_STATE_DRAGGING:
//						Log.e("", "dragging");
						break;
					case ViewPager.SCROLL_STATE_SETTLING:
						viewPager.setIsDisableTouch(true);
//						Log.e("", "settling");
						break;
					case ViewPager.SCROLL_STATE_IDLE:
//						Log.e("", "idle");
						mCurrentPosition = viewPager.getCurrentItem();
						viewPager.setIsDisableTouch(false);
						break;
				}
//				if (state == ViewPager.SCROLL_STATE_IDLE) {
//					Log.e("", String.format("lastPositionOffset : %f", lastPositionOffset));
//					lastPositionOffset = 0;
//				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//				Log.e("", String.format("position : %d, positionOffsetPixels : %d", position, positionOffsetPixels));
				MainActivity.this.onPageScrolled(position, positionOffset);
			}

			@Override
			public void onPageSelected(int position) {
//				Log.e("", String.format("onPageSelected position : %d", position));
//				mCurrentPosition = position;
			}
		});
		viewPager.setPageTransformer(false, this);
		viewPager.setAdapter(adapter);


		pageIndicator = (TabIndicator) findViewById(R.id.pageIndicator);
		pageIndicator.setViewPager(viewPager);

		findViewById(R.id.btnPrintLog).setOnClickListener(this);
	}

	Map<Integer, List<String>> logMap = new LinkedHashMap<Integer, List<String>>(3);
	SparseArray<Float> lastMap = new SparseArray<Float>(3);

	private static final float MIN_SCALE = 0.85f;
//	private static final float MIN_ALPHA = 0.5f;

	@Override
	public void transformPage(View page, float position) {
//		int pageWidth = page.getWidth();
//		int pageHeight = page.getHeight();

		if (position < -1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
//			view.setAlpha(0);

		} else if (position <= 1) { // [-1,1]
			List<String> list = logMap.get(Integer.valueOf(page.getTag().toString()));
			if (list == null) logMap.put(Integer.valueOf(page.getTag().toString()), list = new ArrayList<String>());
			list.add(String.valueOf(position));

//			// Modify the default slide transition to shrink the page as well
//			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
//			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
//			float horzMargin = pageWidth * (1 - scaleFactor) / 2;

			if (position < 0) {
//				list.add("positive");
//				page.setTranslationX(horzMargin - vertMargin / 2);
				if (isForward == 2) {
//					Log.e("", "positive backward");
//					Log.e("", String.format("backward view : %d, position %s", Integer.valueOf(page.getTag().toString()), position));
					adjustListView(page, position);
				}
			} else if (position > 0) {
//				list.add("negative");
//				page.setTranslationX(-horzMargin + vertMargin / 2);
				if (isForward == 1) {
//					Log.e("", "negative forward");
//					Log.e("", String.format("forward view : %d, position %s", Integer.valueOf(page.getTag().toString()), position));
					adjustListView(page, position);
				}
			} else {
//				Log.e("", String.format("else view : %d, position %s", Integer.valueOf(page.getTag().toString()), position));
				adjustListView(page, 0);
			}


//			// Scale the page down (between MIN_SCALE and 1)
//			page.setScaleX(scaleFactor);
//			page.setScaleY(scaleFactor);
//
//			// Fade the page relative to its size.
//			page.setAlpha(MIN_ALPHA +
//					(scaleFactor - MIN_SCALE) /
//							(1 - MIN_SCALE) * (1 - MIN_ALPHA));

		} else { // (1,+Infinity]
			// This page is way off-screen to the right.
//			view.setAlpha(0);
		}
	}

	private void adjustListView(View page, float position) {
		if (Integer.valueOf(page.getTag().toString()) == mCurrentPosition) return;
//		Log.e("", String.format("view : %d, position %s, mCurrentPosition : %d", Integer.valueOf(page.getTag().toString()), position, mCurrentPosition));

//		int pageWidth = page.getWidth();
//		float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
//		float horzMargin = pageWidth * (1 - scaleFactor) / 2;
//		Log.e("", String.format("view : %d, position %s, horzMargin : %s", Integer.valueOf(page.getTag().toString()), position, horzMargin));

		ListView lsvContent = (ListView) page.findViewById(R.id.lsvContent);
		final int childCount = lsvContent.getChildCount();
		float offset = .5f * lsvContent.getWidth();
		for (int i = 0; i < childCount; i++) {
			View childView = lsvContent.getChildAt(i);
			float tranX = i * offset * position;
			childView.setTranslationX(tranX);
		}
	}

//	public void transformPage(View page, float position) {
////		if (((int) Math.abs(position)) == mCurrentPosition) return;
////		Log.e("", String.format("transformPage position : %d", mCurrentPosition));
////		Log.e("", String.format("view : %d, position %s", Integer.valueOf(page.getTag().toString()), position));
//
//
//		if (position < -1) { // [-Infinity,-1)
//			// This page is way off-screen to the left.
//		} else if (position <= 1) { // [-1,1]
//			// Modify the default slide transition to shrink the page as well
//			ListView listView = (ListView) page.findViewById(R.id.lsvContent);
//			int childCount = listView.getChildCount();
//			float offset = .5f * listView.getWidth();
//			for (int i = 0; i < childCount; i++) {
//				View childView = listView.getChildAt(i);
//				childView.setTranslationX(i * offset * position);
//			}
//		} else { // (1,+Infinity]
//			// This page is way off-screen to the right.
//		}
//
//
////		float absPos = Math.abs(position);
////		if (absPos >= 0 && absPos <= 1) {
////			List<Float> list = logMap.get(Integer.valueOf(page.getTag().toString()));
////			if (list == null) logMap.put(Integer.valueOf(page.getTag().toString()), list = new ArrayList<Float>());
////			list.add(position);
////
////			Float lastPos = lastMap.get(Integer.valueOf(page.getTag().toString()));
////			if (lastPos == null) {
////				lastMap.put(Integer.valueOf(page.getTag().toString()), absPos);
////				return;
////			}
////
////			if (lastPos > absPos) { // the coming page.
////				ListView listView = (ListView) page.findViewById(R.id.lsvContent);
////				int childCount = listView.getChildCount();
////				float offset = .5f * listView.getWidth();
////				for (int i = 0; i < childCount; i++) {
////					View childView = listView.getChildAt(i);
////					childView.setTranslationX(i * offset * position);
////				}
////
////				if (absPos == 1 || absPos == 0) {
////					lastMap.delete(Integer.valueOf(page.getTag().toString()));
////				}
////			}
////		}
//	}

	private int mCurrentPosition;
//	private float lastPositionOffset;
	private int isForward;

	@Override
	public void onTouchDeltaX(float deltaX) {
//		int oprPosition = 0;
		if (deltaX < 0) { // next page
//			oprPosition = mCurrentPosition + 1;
//			if (oprPosition == adapter.getCount()) return;
			isForward = 1;
//			Log.e("", "forward");
		}
		else if (deltaX > 0) { // previous page
//			if (mCurrentPosition == 0) return;
//			oprPosition = mCurrentPosition - 1;
			isForward = 2;
//			Log.e("", "backward");
		} else {
			isForward = 0;
		}
//		Log.e("", String.format("oprPosition : %d, deltaX : %f, isForward : %b", oprPosition, deltaX, isForward));
//		RecentView frag = (RecentView) adapter.instantiateItem(viewPager, oprPosition);
//		frag.onPageScrolled(isForward, deltaX);
	}

	public void onPageScrolled(int position, float positionOffset) {
//		if (positionOffset == 0 || positionOffset == lastPositionOffset) return;
		pageIndicator.onPageScrolled(position, positionOffset);

//		Log.e("", String.format("position : %d, positionOffset : %f", position, positionOffset));

//		boolean isForward = positionOffset > lastPositionOffset;
//		if (isForward) position++;
//		RecentView frag = (RecentView) adapter.instantiateItem(viewPager, position);
//		frag.onPageScrolled(isForward, positionOffset);

//		lastPositionOffset = positionOffset;
	}

	@Override
	public void onClick(View v) {
		for (Integer viewHashCode : logMap.keySet()) {
			List<String> list = logMap.get(viewHashCode);
			for (String item : list) {
				Log.e("", String.format("view[%d] position : %s", viewHashCode, item));
			}
			for (int i = 0; i < 10; i++) {
				Log.e("", "---");
			}
		}
		logMap.clear();
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
