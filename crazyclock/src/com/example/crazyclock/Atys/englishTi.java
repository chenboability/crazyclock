package com.example.crazyclock.Atys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.example.crazyclock.R;
import com.example.crazyclock.data.word;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class englishTi extends Activity {
	String[] items;
	TextView showHowMany, english, a, b, c, d;
	word newti;
	int choose;
	boolean isRight = false;
	static int count = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_english);

		showHowMany = (TextView) findViewById(R.id.showHowMany);
		newti = readOnAssets();
		items = new String[] { newti.getA(), newti.getB(), newti.getC(),
				newti.getD() };

		showHowMany.setText("你还需要正确选择" + count + "个单词，才可以关闭闹钟！");

		new AlertDialog.Builder(englishTi.this).setTitle(newti.getEnglish())
				.setCancelable(false)
				.setSingleChoiceItems(items, -1, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if ((which + 1) == newti.getRightAnswer()) {
							System.out.println("ture");
							isRight = true;
						} else {
							System.out.println("false");
							isRight = false;
						}
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						if (isRight) {
							count--;
						}
						if (count > 0) {
							startActivity(new Intent(englishTi.this,
									englishTi.class));
							englishTi.this.finish();
						} else {
							englishTi.this.finish();
							AlarmActivity.vibarator.cancel();
							AlarmActivity.mMediaPlayer.stop();
							count = 5;
						}
					}
				}).create().show();

	}

	private word readOnAssets() {
		word nowWord = null;
		try {
			InputStream inputStream = getResources().getAssets().open(
					"english.txt");
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "GB2312");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			String info = "";
			int random = (int) (Math.random() * 199);
			int now = 0;
			System.out.println(random);
			while ((info = bufferedReader.readLine()) != null) {
				if (now != random)
					now++;
				else {
					String[] split = info.split(" ");

					nowWord = new word(split[0], split[1], split[2], split[3],
							split[4], Integer.parseInt(split[5]));

					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nowWord;
	}

}
