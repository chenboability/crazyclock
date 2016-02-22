package com.example.crazyclock.Atys;

import com.example.crazyclock.MainActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class sensor extends Activity implements SensorEventListener {
	// ����Sensor������
	private SensorManager mSensorManager;
	public static final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	String weixin;

//	EditText etOrientation;
//	EditText etMagnetic;

//	TextView daojishi;
//	TextView daojishi1;
	float[] values;
	float[] values1;
	float[] before = new float[6];
	float[] after = new float[6];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("������");
		weixin = "�ҽ�����˯��ͷ�ˣ���������������һ��ɵ�ơ����ҽ�����˯��ͷ�ˣ���������������һ��ɵ�ơ����ҽ�����˯��ͷ�ˣ���������������һ��ɵ�ơ���";
		String appID = "wx444edeebeb5c0402";
		String appSecret = "a5bbf5e67240b9ccc65c416e811e5368";
		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(sensor.this, appID,
				appSecret);
		wxHandler.addToSocialSDK();
		// ���΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(sensor.this,
				appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		
		// ����΢������Ȧ��������
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(weixin);
		// ��������Ȧtitle
		circleMedia.setTitle("����Ȧ");
	//	 UMImage localImage = new UMImage(AlarmActivity.this, MainActivity.b);
	//	circleMedia.setShareImage(localImage );
		// circleMedia.setTargetUrl("���URL����");
		mController.setShareMedia(circleMedia);
		
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.DOUBAN);
		
		// ��ȡ�������������
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // ��
		new MyCount(10000, 1000) {
			public void onTick(long millisUntilFinished) {
			//	daojishi.setText("seconds remaining: " + millisUntilFinished
			//			/ 1000);
				System.out.println("seconds remaining: " + millisUntilFinished/1000);
			}

			public void onFinish() {
				before[0] = values[0];
				before[1] = values[1];
				before[2] = values[2];
				before[3] = values1[0];
				before[4] = values1[1];
				before[5] = values1[2];
				System.out.println("111before:"+before[0]+":"+before[1]+":"+before[2]+":"+before[3]+":"+before[4]+":"+before[5]+":");
			}
		}.start();
		new MyCount(20000, 1000) {
			public void onTick(long millisUntilFinished) {
			//	daojishi1.setText("seconds remaining: " + millisUntilFinished
			//			/ 1000);
				System.out.println("seconds remaining: " + millisUntilFinished/1000);
			}

			public void onFinish() {
				after[0] = values[0];
				after[1] = values[1];
				after[2] = values[2];
				after[3] = values1[0];
				after[4] = values1[1];
				after[5] = values1[2];
				System.out.println("after:"+after[0]+":"+after[1]+":"+after[2]+":"+after[3]+":"+after[4]+":"+after[5]+":");
			if(Math.abs(after[0]-before[0])>10||Math.abs(after[1]-before[1])>10||Math.abs(after[2]-before[2])>10||Math.abs(after[3]-before[3])>10||Math.abs(after[4]-before[4])>10||Math.abs(after[5]-before[5])>10){
				System.out.println("yesyesyes");
				mController.directShare(sensor.this,
										SHARE_MEDIA.WEIXIN_CIRCLE, null);
			}else{
				System.out.println("nonono");
			}
			sensor.this.finish();
			startActivity(new Intent(sensor.this,MainActivity.class));
			}
		}.start();
	}

	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			// tv.setText("finish");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// tv.setText("��ȴ�30��(" + millisUntilFinished / 1000 + ")...");
			Toast.makeText(sensor.this, millisUntilFinished / 1000 + "",
					Toast.LENGTH_LONG).show();// toast����ʾʱ���ӳ�
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Ϊϵͳ�ķ��򴫸���ע�������
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
		// Ϊϵͳ�Ĵų�������ע�������
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME);

	}

	@Override
	protected void onStop() {
		// �����˳�ʱȡ��ע�ᴫ����������
		mSensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	protected void onPause() {
		// ������ͣʱȡ��ע�ᴫ����������
		mSensorManager.unregisterListener(this);
		super.onPause();
	}

	// ������ʵ��SensorEventListener�ӿڱ���ʵ�ֵķ���
	@Override
	// �����������ȸı�ʱ�ص��÷�����
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// ��ȡ����event�Ĵ���������
		int sensorType = event.sensor.getType();
		StringBuilder sb = null;
		// �ж����ĸ������������ı�
		switch (sensorType) {
		// ���򴫸���
		case Sensor.TYPE_ORIENTATION:
			values = event.values;
			sb = new StringBuilder();
			sb.append("��Z��ת���ĽǶȣ�");
			sb.append(values[0]);
			sb.append("\n��X��ת���ĽǶȣ�");
			sb.append(values[1]);
			sb.append("\n��Y��ת���ĽǶȣ�");
			sb.append(values[2]);
		//	etOrientation.setText(sb.toString());
			break;
		// �ų�������
		case Sensor.TYPE_MAGNETIC_FIELD:
			values1 = event.values;
			sb = new StringBuilder();
			sb.append("X�����ϵĽǶȣ�");
			sb.append(values1[0]);
			sb.append("\nY�����ϵĽǶȣ�");
			sb.append(values1[1]);
			sb.append("\nZ�����ϵĽǶȣ�");
			sb.append(values1[2]);
		//	etMagnetic.setText(sb.toString());
			break;

		}
	}
}
