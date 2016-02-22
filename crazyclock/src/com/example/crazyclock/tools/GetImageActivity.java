package com.example.crazyclock.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import com.example.crazyclock.R;
import com.example.crazyclock.Atys.add;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class GetImageActivity extends Activity implements OnClickListener {
          //存放拍照的
	public static final String IMAGE_FILE_LOCATION = "file:///sdcard/CarzyAlarm/temp.jpg";
	public static final String IMAGE_FILE_LOCATION_BEFORE = "file:///sdcard/CarzyAlarm/temp_before.jpg";
	public static final String IMAGE_FILE_LOCATION_AFTER = "file:///sdcard/CarzyAlarm/temp_after.jpg";
	
	private Uri cameraImgUri;
	private final int CAMERA_WITH_DATA = 1001;
	private final int PHOTO_PICKED_WITH_DATA = 1002;
	private byte[] mContent;
	private Context context;
	public static String bORaORf;
	Button get_pic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.frist_layout);
		this.context = this;
		findViewById(R.id.take_photo).setOnClickListener(this);
		get_pic = (Button) findViewById(R.id.get_pic);
		get_pic.setOnClickListener(this);

		Bundle bundle = this.getIntent().getExtras();
		bORaORf = (String) bundle.get(add.BEFOREORAFTER);
		if (bORaORf.equals(add.FACE))
			cameraImgUri = Uri.parse(IMAGE_FILE_LOCATION);
		else if (bORaORf.equals(add.AFTER)){
			cameraImgUri = Uri.parse(IMAGE_FILE_LOCATION_AFTER);
			get_pic.setVisibility(View.GONE);}
		else if (bORaORf.equals(add.BEFORE))
			cameraImgUri = Uri.parse(IMAGE_FILE_LOCATION_BEFORE);
			
			

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ContentResolver resolver = getContentResolver();
		switch (requestCode) {
		case CAMERA_WITH_DATA:

			// 将图片内容解析成字节数组
			try {
				Uri originalUri = cameraImgUri;
				mContent = readStream(resolver.openInputStream(Uri
						.parse(originalUri.toString())));
				Bitmap b = getPicFromBytes(mContent, null);
				CropApplication.setBitmap(b);


				Intent intent = new Intent(GetImageActivity.this,
						FaceActivity.class);

				startActivity(intent);
				finish();
			}

			catch (Exception e) {
				e.printStackTrace();
			}

			break;

		case PHOTO_PICKED_WITH_DATA:
			try {
				Uri originalUri = data.getData();
				mContent = readStream(resolver.openInputStream(Uri
						.parse(originalUri.toString())));
				Bitmap b = getPicFromBytes(mContent, null);
				CropApplication.setBitmap(b);


				Intent intent = new Intent(GetImageActivity.this,
						FaceActivity.class);

				startActivity(intent);
				finish();
			}

			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.take_photo:

			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				doTakePhoto();
			} else {
				Toast.makeText(context, "没有SD卡！请选择本地图片", Toast.LENGTH_LONG)
						.show();
			}
			break;

		case R.id.get_pic:
			doSelectImageFromLoacal();
			break;

		default:
			break;
		}
	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		Bitmap b;
		if (bytes != null)
			if (opts != null){
				b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
				return b; 
			}
			else{
				 b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				 return b;
			}
		return null;
	}

	private void doSelectImageFromLoacal() {
		Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
		openAlbumIntent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(openAlbumIntent, PHOTO_PICKED_WITH_DATA);
	}

	private void doTakePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImgUri);
		startActivityForResult(intent, CAMERA_WITH_DATA);
	}

}
