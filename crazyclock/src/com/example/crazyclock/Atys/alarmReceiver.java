package com.example.crazyclock.Atys;

import com.example.crazyclock.configure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class alarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, AlarmActivity.class);
		Bundle bundle = intent.getExtras();
		if(bundle.getInt(configure.VIBRATE) == configure.VIVRATE_TRUE){
			i.putExtra(configure.VIBRATE, configure.VIVRATE_TRUE);
		}else{
			i.putExtra(configure.VIBRATE, configure.VIVRATE_FALSE);
		}
		i.putExtra(configure.LABEL, bundle.getString(configure.LABEL));
		i.putExtra(configure.MUSIC, bundle.getString(configure.MUSIC));
		i.putExtra(configure.MARK, bundle.getInt(configure.MARK));
		i.putExtra(configure.REPEAT, bundle.getString(configure.REPEAT));
		i.putExtra(configure.MODE, bundle.getInt(configure.MODE));
		i.putExtra(configure.HOUR, bundle.getInt(configure.HOUR));
		i.putExtra(configure.MINUTE, bundle.getInt(configure.MINUTE));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startActivity(i);
	}

}
