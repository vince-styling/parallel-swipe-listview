package com.vincestyling.parallel_swipe_listview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class SelfViewPager extends ViewPager {
	public SelfViewPager(Context context) {
		super(context);
	}

	public SelfViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private boolean mIsDisableTouch;
	private float mLastX;
	private float mDeltaX;

	public void setIsDisableTouch(boolean isDisableTouch) {
		mIsDisableTouch = isDisableTouch;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mIsDisableTouch) return false;
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			mDeltaX = 0;
			mLastX = ev.getRawX();
//			Log.e("", String.format("Performing Touch : %f", mLastX));
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_MOVE:
				float x = ev.getRawX();
				mDeltaX += x - mLastX;
//				Log.e("", String.format("This Touch : %f, deltaX : %f", x, mDeltaX));
				mDeltaCallback.onTouchDeltaX(mDeltaX);
				mLastX = x;
		}
		return super.onTouchEvent(ev);
	}

	private TouchDeltaCallback mDeltaCallback;
	public void setTouchDeltaCallback(TouchDeltaCallback deltaCallback) {
		mDeltaCallback = deltaCallback;
	}
}
