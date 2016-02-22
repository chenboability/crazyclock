package com.example.crazyclock.Atys;

import java.io.FileNotFoundException;

import com.example.crazyclock.MainActivity;

import com.example.crazyclock.R;
import com.example.crazyclock.tools.GetImageActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class BigImage extends Activity {
	ImageView big;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_bigimage);
		big = (ImageView) findViewById(R.id.big);
		String file = this.getIntent().getExtras()
				.getString(MainActivity.BIG_IMAGE);
		Uri cameraImgUri = null;
		if (file.equals(add.FACE)) {
			cameraImgUri = Uri.parse("file:///sdcard/CarzyAlarm/big.jpg");
		} else if (file.equals(add.BEFORE)) {
			cameraImgUri = Uri.parse("file:///sdcard/CarzyAlarm/before.jpg");
		}
		ContentResolver resolver = getContentResolver();
		byte[] mContent;
		try {
			mContent = GetImageActivity.readStream(resolver.openInputStream(Uri
					.parse(cameraImgUri.toString())));
			Bitmap b = GetImageActivity.getPicFromBytes(mContent, null);
			big.setImageBitmap(b);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// big.setImageURI(Uri.parse(file));
		big.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
