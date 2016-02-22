package com.example.crazyclock.tools;

import com.example.crazyclock.MainActivity;
import com.example.crazyclock.Atys.AlarmActivity;
import com.example.crazyclock.Atys.add;
import com.example.crazyclock.Atys.alarmReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MyListener implements OnCheckedChangeListener {

	private static MyListener instance = null;

	private MyListener() {
	}

	public static MyListener getInstance() {
		if (instance == null)
			instance = new MyListener();
		return instance;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		MainActivity a = new MainActivity();
		int mark = ((MySwitch) buttonView).getIndex();
		a.updateItemOpen(mark, isChecked);
	
	
	}
	

}
