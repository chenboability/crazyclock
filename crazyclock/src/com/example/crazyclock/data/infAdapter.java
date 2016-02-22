package com.example.crazyclock.data;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import com.example.crazyclock.R;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class infAdapter extends BaseAdapter {

	private Context context;
	private List<infitem> infLists;
	private LayoutInflater infInflater;

	public infAdapter(Context context, List<infitem> lists) {
		super();
		this.context = context;
		this.infLists = lists;
	}

	@Override
	public int getCount() {
		return infLists.size();
	}

	@Override
	public Object getItem(int position) {
		return infLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			infInflater = LayoutInflater.from(parent.getContext());
			convertView = infInflater.inflate(R.layout.inf_item, null);
			viewHolder = new ViewHolder();

			viewHolder.showMode = (TextView) convertView.findViewById(R.id.mode);
			viewHolder.showDate = (TextView) convertView
					.findViewById(R.id.show_infdate);
			viewHolder.showText = (TextView) convertView
					.findViewById(R.id.text);
			viewHolder.showTime = (TextView) convertView
					.findViewById(R.id.show_inftime);
			

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			;
		}

		String monthStr = infLists.get(position).getMonth()+"";
		String dayStr = infLists.get(position).getDay()+"";
		String hourStr = infLists.get(position).getHour()+""; 
		String minuteStr = infLists.get(position).getMinute()+"";
		String textStr = infLists.get(position).getText();
		String modeStr = infLists.get(position).getMode();
		
		String sDate = monthStr+"ÔÂ"+dayStr+"ÈÕ";
		String sTime = hourStr+":"+minuteStr;
	
		viewHolder.showDate.setText(sDate);
		viewHolder.showMode.setText(modeStr);
		viewHolder.showTime.setText(sTime);
		viewHolder.showText.setText(textStr);
	

		return convertView;
	}

	static class ViewHolder {

		public TextView showMode;
		public TextView showDate;
		public TextView showTime;
		public TextView showText;
	}
}