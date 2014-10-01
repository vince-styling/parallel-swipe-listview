package com.vincestyling.parallel_swipe_listview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class ParallelListView extends ListView {
	public ParallelListView(Context context) {
		super(context);
	}

	public ParallelListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ParallelListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void onPageScrolled(boolean isForward, float pageOffset) {
//		Log.e("", String.format("pageOffset : %f", pageOffset));

		int width = isForward ? getWidth() : -getWidth();
		if (isForward) pageOffset = 1 - pageOffset;

		final int count = getChildCount();
		for (int index = 0; index < count; index++) {
			final View child = getChildAt(index);

			float startX = width / count * index;
			float childLeft = startX * pageOffset;
//			Log.e("", String.format("index : %d, childLeft : %f, pageOffset : %f", index, childLeft, pageOffset));
			child.setTranslationX(childLeft);
//			if (child.getTranslationX() == 0) {
//				child.setTranslationX(childLeft);
//			} else {
//				child.setTranslationX(child.getTranslationX() - childLeft);
//			}
//			child.layout(childLeft, child.getTop(),
//					childLeft + child.getMeasuredWidth(),
//					child.getTop() + child.getMeasuredHeight());
		}
	}
}
