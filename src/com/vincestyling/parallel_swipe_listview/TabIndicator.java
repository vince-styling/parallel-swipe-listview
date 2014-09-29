package com.vincestyling.parallel_swipe_listview;

import android.content.Context;
import android.graphics.*;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TabIndicator extends View {
	private Paint mPaint;

	private ViewPager mViewPager;
	private int mScrollingToPage;
	private float mPageOffset;
	private Bitmap mIndicator;

	public TabIndicator(Context context) {
		this(context, null);
	}

	public TabIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) return;

		mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setAntiAlias(true);
		mPaint.setFilterBitmap(true);
		mPaint.setColor(getResources().getColor(R.color.frag_tab_item_text));
		mPaint.setTextSize(getResources().getDimension(R.dimen.frag_tab_item_text));

		mIndicator = BitmapFactory.decodeResource(getResources(), R.drawable.tab_indicator);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mViewPager == null) return;
		final int count = getCount();
		if (count == 0) return;

		Rect areaRect = new Rect();
		areaRect.left = getPaddingLeft();
		areaRect.right = getWidth() - getPaddingRight();
		areaRect.top = getPaddingTop();
		areaRect.bottom = getHeight() - getPaddingBottom();

		int tabWidth = areaRect.width() / count;

		Rect tabRect = new Rect(areaRect);
		tabRect.top = tabRect.height() - mIndicator.getHeight();
		tabRect.left += (mScrollingToPage + mPageOffset) * tabWidth + (tabWidth - mIndicator.getWidth()) / 2;
		canvas.drawBitmap(mIndicator, tabRect.left, tabRect.top, mPaint);

		for (int pos = 0; pos < count; pos++) {
			tabRect.set(areaRect);
			tabRect.left += pos * tabWidth;
			tabRect.right = tabRect.left + tabWidth;

			String pageTitle = getPageTitle(pos);

			RectF bounds = new RectF(tabRect);
			bounds.right = mPaint.measureText(pageTitle, 0, pageTitle.length());
			bounds.bottom = mPaint.descent() - mPaint.ascent();

			bounds.left += (tabRect.width() - bounds.right) / 2.0f;
			bounds.top += (tabRect.height() - bounds.bottom) / 2.0f;

			canvas.drawText(pageTitle, bounds.left, bounds.top - mPaint.ascent(), mPaint);
		}
	}

	public boolean onTouchEvent(MotionEvent ev) {
		if (super.onTouchEvent(ev)) return true;
		if (mViewPager == null) return false;
		final int count = getCount();
		if (count == 0) return false;

		switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:

				Rect areaRect = new Rect();
				areaRect.left = getPaddingLeft();
				areaRect.right = getWidth() - getPaddingRight();
				areaRect.top = getPaddingTop();
				areaRect.bottom = getHeight() - getPaddingBottom();

				int btnWidth = areaRect.width() / count;

				for (int pos = 0; pos < count; pos++) {
					RectF tabRect = new RectF(areaRect);
					tabRect.left += pos * btnWidth;
					tabRect.right = tabRect.left + btnWidth;

					if (tabRect.contains(ev.getX(), ev.getY())) {
						setCurrentItem(pos);
						return true;
					}
				}
				break;
		}

		return true;
	}

	public void setViewPager(ViewPager view) {
		if (mViewPager == view) return;
		mViewPager = view;
		invalidate();
	}

	private void setCurrentItem(int item) {
		if (mViewPager == null) {
			throw new IllegalStateException("ViewPager has not been bound.");
		}
		mViewPager.setCurrentItem(item);
		invalidate();
	}

	public void onPageScrolled(int position, float positionOffset) {
		if (positionOffset == 0) return;
		mPageOffset = positionOffset;
		mScrollingToPage = position;
		invalidate();
	}

	private int getCount() {
		return mViewPager.getAdapter().getCount();
	}

	private String getPageTitle(int position) {
		return (String) mViewPager.getAdapter().getPageTitle(position);
	}
}
