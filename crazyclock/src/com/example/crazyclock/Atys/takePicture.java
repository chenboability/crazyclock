package com.example.crazyclock.Atys;

import com.example.crazyclock.MainActivity;
import com.example.crazyclock.R;
import com.example.crazyclock.R.id;
import com.example.crazyclock.tools.FaceActivity;
import com.example.crazyclock.tools.GetImageActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class takePicture extends Activity {
	LinearLayout takePhoto;
	ImageView beforeiv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		takePhoto = (LinearLayout) this.getLayoutInflater().inflate(
				R.layout.take_picture, null);
		beforeiv = (ImageView) takePhoto.findViewById(R.id.beforeiv);
		beforeiv.setImageURI(Uri.parse(FaceActivity.filepathBefore));

		new AlertDialog.Builder(takePicture.this).setTitle("Æð´²À´ÅÄÕÕ")
				.setView(takePhoto)
				.setCancelable(false)
				.setPositiveButton("ÅÄÕÕ", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent face = new Intent(takePicture.this,
								GetImageActivity.class);
						face.putExtra(add.BEFOREORAFTER,add.AFTER);
						startActivity(face);
					}
				}).create().show();
	}
}
