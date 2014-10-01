package com.vincestyling.parallel_swipe_listview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class RecentView extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.listview, null);
	}

	private ParallelListView lsvContent;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		lsvContent = (ParallelListView) view.findViewById(R.id.lsvContent);
		initDataset();

		lsvContent.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				return recentList.size();
			}

			@Override
			public Recent getItem(int position) {
				return recentList.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = getActivity().getLayoutInflater().inflate(R.layout.recent_item, null);
				}

				TextView txvTitle = (TextView) convertView.findViewById(R.id.txvTitle);
				TextView txvPhone = (TextView) convertView.findViewById(R.id.txvPhone);
				ImageView imvState = (ImageView) convertView.findViewById(R.id.imvState);
				TextView txvDailTime = (TextView) convertView.findViewById(R.id.txvDailTime);

				txvTitle.setText(getItem(position).title);
				txvPhone.setText(getItem(position).phone);
				txvDailTime.setText(getItem(position).dailTime);
				switch (getItem(position).state) {
					case 1:
						imvState.setImageResource(R.drawable.ic_call_log_missed_call);
						break;
					case 2:
						imvState.setImageResource(R.drawable.ic_call_log_outgoing_call);
						break;
					case 3:
						imvState.setImageResource(R.drawable.ic_call_log_incoming_call);
						break;
				}

				return convertView;
			}
		});
	}

	public void onPageScrolled(boolean isForward, float pageOffset) {
		if (lsvContent != null) {
			lsvContent.onPageScrolled(isForward, pageOffset);
		}
	}

	List<Recent> recentList = new ArrayList<Recent>();
	final Random random = new Random();

	private void initDataset() {
		int size = 30 + random.nextInt(50);
		for (int i = 0; i < size; i++) {
			recentList.add(new Recent());
		}
	}

	private class Recent {
		String title;
		String phone;
		String dailTime;
		int state;
		private Recent() {
			title = getRandomName();
			phone = getRandomPhone();
			dailTime = getRandomTime();
			state = 1 + random.nextInt(3);
		}
	}

	private String getRandomTime() {
		Calendar cledr = Calendar.getInstance();
		cledr.add(Calendar.HOUR, -random.nextInt(5 * 24));
		if (isSameDay(cledr, Calendar.getInstance())) {
			return new SimpleDateFormat("hh:mm").format(cledr.getTime());
		}
		return new SimpleDateFormat("M月d日").format(cledr.getTime());
	}

	private boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
				cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
				cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}

	private String getRandomName() {
		String[] names = "李真荣#李祯#李振波#李振德#李振海#李振兴#李震#李争鸣#李征#李正荣#李志华#李志娟#李志林#李志梅#李志平#李志容#李志新#李志勇#李治富#李治辉#李致宏#李智权#李智勇#李中霞#李中衍#李忠法#李忠辉#李忠友#李钟#李周#李椎#李卓林#李子川#李子森#李宗安#李宗彬#李宗辉#李宗棠#李宗玮#李祖凡#力小广#历宏宇#历志坚#厉和#厉金岳#厉丽#立青如彬#利永胡#励常林#励东#励刚#励海江#励建国#励建明#励武华#励小兵#励旭平#励银岳#郦丽#郦凌晖#郦旭琴#郦谊君#连斌#连锦兰#连凯#连青龙#连儒#连伟平#连晓丹#连迎青#连营#廉翠#廉冬英#练慧洁#练荣桥#练少芳#练书耀#练秀焕#练秀慧#练燕琼#练梓权#练祖兴#梁策#梁爱连#梁爱莲#梁碧岩#梁冰天#梁彩虹#梁彩琼#梁成磊#梁达英#梁大岳#梁丹#梁定锦#梁东海#梁杜#梁谷晨#梁光兴#梁桂华#梁国培#梁海#梁海明#梁海荣#梁海文#梁海欣#梁恒#梁红娟#梁焕锦#梁会东#梁慧#梁伙进#梁家贤#梁建辉#梁建明#梁建伟#梁建阳#梁建英#梁剑楷#梁洁#梁洁桃#梁介中#梁金梅#梁金平#梁锦华#梁锦辉#梁锦泉#梁锦荣#梁静#梁菊云#梁君#梁君雄#梁来均#梁兰#梁蓝廷#梁莉华#梁立军#梁立信".split("#");
		return names[random.nextInt(names.length)];
	}

	private String getRandomPhone() {
		return String.format("13%d%04d%04d",
				(int) Math.floor(9 * random.nextDouble()),
				(int) Math.floor(9999 * random.nextDouble()),
				(int) Math.floor(9999 * random.nextDouble()));
	}
}
