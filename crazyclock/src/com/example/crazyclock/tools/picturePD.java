package com.example.crazyclock.tools;


import java.io.File;
import java.io.FileInputStream;

import com.example.crazyclock.MainActivity;
import com.example.crazyclock.Atys.AlarmActivity;
import com.example.crazyclock.Atys.takePicture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class picturePD extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("123Dgsgdsgsgdgsdgdsgsdgsddsgsgsdgsdgs");
		AlarmActivity.isTwoPictureSame = false;
		String url_one = getSDCardPath()+"/CarzyAlarm/after.jpg";
		String url_two = getSDCardPath()+"/CarzyAlarm/before.jpg";
		System.out.println("Dgsgdsgsgdgsdgdsgsdgsddsgsgsdgsdgs");
		System.out.println("���ƶ�"+PictureContrast.similarity(url_one, url_two));
		if(PictureContrast.similarity(url_one, url_two)>0){
			AlarmActivity.vibarator.cancel();
			AlarmActivity.mMediaPlayer.stop();
			Toast.makeText(picturePD.this, "ͼƬһ�£�������ֹͣ��", Toast.LENGTH_LONG).show();
			startActivity(new Intent(picturePD.this,MainActivity.class));
		}else{
			Toast.makeText(picturePD.this, "ͼƬ��һ�£����������գ�", Toast.LENGTH_LONG).show();
			startActivity(new Intent(picturePD.this,
					takePicture.class));
		}
		this.finish();
	}
	public static String getSDCardPath() {
		File sdcardDir = null;
		// �ж�SDCard�Ƿ����
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}

}
