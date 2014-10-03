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

	public void onPageScrolled(boolean isForward, float deltaX) {
//		Log.e("", String.format("deltaX : %f", deltaX));

		final int width = isForward ? getWidth() : -getWidth();
		final int count = getChildCount();
		for (int index = 0; index < count; index++) {
			final View child = getChildAt(index);

			float childLeft = width + deltaX * (count - index);
			if (isForward) {
				if (childLeft < 0) childLeft = 0;
			} else {
				if (childLeft > 0) childLeft = 0;
			}
//			Log.e("", String.format("index : %d, childLeft : %f, deltaX : %f", index, childLeft, deltaX));
			child.setTranslationX(childLeft);
//			child.layout(childLeft, child.getTop(),
//					childLeft + child.getMeasuredWidth(),
//					child.getTop() + child.getMeasuredHeight());
		}
	}
}
