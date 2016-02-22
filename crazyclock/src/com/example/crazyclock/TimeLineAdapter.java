package com.example.crazyclock;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;











import com.example.crazyclock.data.items;
import com.example.crazyclock.tools.MyListener;
import com.example.crazyclock.tools.MySwitch;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class TimeLineAdapter extends BaseAdapter {

	private Context context;
	private List<items> lists;
	private LayoutInflater inflater;

	public TimeLineAdapter(Context context, List<items> lists) {
		super();
		this.context = context;
		this.lists = lists;
	}

	@Override
	public int getCount() {

		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.listview_item, null);
			viewHolder = new ViewHolder();

			viewHolder.showStatus = (TextView) convertView
					.findViewById(R.id.show_status);
			viewHolder.showAmPm = (TextView) convertView
					.findViewById(R.id.show_ampm);
			viewHolder.showTime = (TextView) convertView
					.findViewById(R.id.show_time);
			viewHolder.showRepeat = (TextView) convertView
					.findViewById(R.id.show_repeat);
			viewHolder.status = (MySwitch) convertView
					.findViewById(R.id.toggleButton);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//单击状态按钮，改变数据库
		items i = (items) getItem(position);
		viewHolder.status.setIndex(i.getMark());
		viewHolder.status.setOnCheckedChangeListener(MyListener.getInstance());
		
		String statusStr;
		if (lists.get(position).getStatus() == configure.STATUS_FALSE) { // 不可用
			statusStr = "OFF";
			viewHolder.status.setChecked(false);
		} else { // 可用
			statusStr = getStatus(position);
			viewHolder.status.setChecked(true);
		}
		String repeatStr = lists.get(position).getRepeat();
		String timeStr = lists.get(position).getHour() + ":"
				+ lists.get(position).getMinute();
		

		String ampmStr;
		if (lists.get(position).getHour() > 12) {
			ampmStr = "pm";
		} else {
			ampmStr = "am";
		}

		viewHolder.showStatus.setText(statusStr);
		viewHolder.showRepeat.setText(repeatStr);
		viewHolder.showTime.setText(timeStr);
		viewHolder.showAmPm.setText(ampmStr);
		

		return convertView;
	}

	private String getStatus(int position) {
		String statusStr = null;
		Calendar cc = Calendar.getInstance();
		int hour1 = cc.get(Calendar.HOUR_OF_DAY);
		int minute1 = cc.get(Calendar.MINUTE);

		int setHour = lists.get(position).getHour();
		int setMinute = lists.get(position).getMinute();

		if (setHour == hour1) {
			if (setMinute > minute1)
				statusStr = "闹钟将于" + (setMinute - minute1) + "分钟后开启";
			else if (setMinute == minute1)
				statusStr = "闹钟将于小于1分钟后开启";
			else if (setMinute < minute1)
				statusStr = "闹钟将于23小时" + (60 - (minute1 - setMinute)) + "分钟后开启";
		} else if (setHour > hour1) {
			if (setMinute > minute1)
				statusStr = "闹钟将于" + (setHour - hour1) + "小时"
						+ (setMinute - minute1) + "分钟后开启";
			else if (setMinute == minute1)
				statusStr = "闹钟将于" + (setHour - hour1) + "小时后开启";
			else if (setMinute < minute1)
				statusStr = "闹钟将于" + (setHour - hour1 - 1) + "小时"
						+ (60 - (minute1 - setMinute)) + "分钟后开启";

		} else if (setHour < hour1) {
			if (setMinute > minute1)
				statusStr = "闹钟将于" + (24 - (hour1 - setHour)) + "小时"
						+ (setMinute - minute1) + "分钟后开启";
			else if (setMinute == minute1)
				statusStr = "闹钟将于" + (24 - (hour1 - setHour)) + "小时后开启";
			else if (setMinute < minute1)
				statusStr = "闹钟将于" + (24 - (hour1 - setHour) - 1) + "小时"
						+ (60 - (minute1 - setMinute)) + "分钟后开启";

		}

		return statusStr;

	}

	static class ViewHolder {

		public TextView showStatus;
		public TextView showRepeat;
		public TextView showTime;
		public TextView showAmPm;
		public MySwitch status;
	}
}