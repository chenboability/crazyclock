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
	// 定义Sensor管理器
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
		System.out.println("进来了");
		weixin = "我今天又睡过头了，哈哈哈哈，真是一个傻逼……我今天又睡过头了，哈哈哈哈，真是一个傻逼……我今天又睡过头了，哈哈哈哈，真是一个傻逼……";
		String appID = "wx444edeebeb5c0402";
		String appSecret = "a5bbf5e67240b9ccc65c416e811e5368";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(sensor.this, appID,
				appSecret);
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(sensor.this,
				appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		
		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(weixin);
		// 设置朋友圈title
		circleMedia.setTitle("朋友圈");
	//	 UMImage localImage = new UMImage(AlarmActivity.this, MainActivity.b);
	//	circleMedia.setShareImage(localImage );
		// circleMedia.setTargetUrl("你的URL链接");
		mController.setShareMedia(circleMedia);
		
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.DOUBAN);
		
		// 获取传感器管理服务
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // ①
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
			// tv.setText("请等待30秒(" + millisUntilFinished / 1000 + ")...");
			Toast.makeText(sensor.this, millisUntilFinished / 1000 + "",
					Toast.LENGTH_LONG).show();// toast有显示时间延迟
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 为系统的方向传感器注册监听器
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
		// 为系统的磁场传感器注册监听器
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME);

	}

	@Override
	protected void onStop() {
		// 程序退出时取消注册传感器监听器
		mSensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	protected void onPause() {
		// 程序暂停时取消注册传感器监听器
		mSensorManager.unregisterListener(this);
		super.onPause();
	}

	// 以下是实现SensorEventListener接口必须实现的方法
	@Override
	// 当传感器精度改变时回调该方法。
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// 获取触发event的传感器类型
		int sensorType = event.sensor.getType();
		StringBuilder sb = null;
		// 判断是哪个传感器发生改变
		switch (sensorType) {
		// 方向传感器
		case Sensor.TYPE_ORIENTATION:
			values = event.values;
			sb = new StringBuilder();
			sb.append("绕Z轴转过的角度：");
			sb.append(values[0]);
			sb.append("\n绕X轴转过的角度：");
			sb.append(values[1]);
			sb.append("\n绕Y轴转过的角度：");
			sb.append(values[2]);
		//	etOrientation.setText(sb.toString());
			break;
		// 磁场传感器
		case Sensor.TYPE_MAGNETIC_FIELD:
			values1 = event.values;
			sb = new StringBuilder();
			sb.append("X方向上的角度：");
			sb.append(values1[0]);
			sb.append("\nY方向上的角度：");
			sb.append(values1[1]);
			sb.append("\nZ方向上的角度：");
			sb.append(values1[2]);
		//	etMagnetic.setText(sb.toString());
			break;

		}
	}
}
