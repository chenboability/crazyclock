package com.example.crazyclock;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.crazyclock.Atys.AlarmActivity;
import com.example.crazyclock.Atys.BigImage;
import com.example.crazyclock.Atys.add;
import com.example.crazyclock.Atys.alarmReceiver;
import com.example.crazyclock.Atys.sensor;
import com.example.crazyclock.data.infAdapter;
import com.example.crazyclock.data.infitem;
import com.example.crazyclock.data.items;
import com.example.crazyclock.data.myDatabaseHelper;
import com.example.crazyclock.tools.FaceActivity;
import com.example.crazyclock.tools.GetImageActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements SensorEventListener {

	public static final int PICTURE = 1;
	public static final String BIG_IMAGE = "bigImage";
	public static final String BEFORE_IMAGE = "beforeImage";
	private long exitTime = 0;
	String picPath = null;
	public int first = 1;
	boolean isExist = false;
	float[] values;
	float[] values1;
	float[] before = new float[6];
	float[] after = new float[6];
	public static boolean isWX = false;
	String weixin[] = new String[5];
	private SensorManager mSensorManager;

	public static items i;
	public static int isChange = 0;
	private ListView listView;
	TextView countTV;
	ListView information_list;
	List<String> data;
	List<String> infData;
	private TimeLineAdapter timelineAdapter;
	private infAdapter infAdapter1;

	myDatabaseHelper mydbh;
	myDatabaseHelper mydbhUser;
	myDatabaseHelper mydbhInf;
	myDatabaseHelper mydbhEng;
	public static SQLiteDatabase db;
	public static SQLiteDatabase dbUser;
	public static SQLiteDatabase dbInf;
	public static SQLiteDatabase dbEng;

	Button addbn;
	public static ImageView myface;
	AlarmManager alarmManager;
	public static Bitmap b;

	public String SavePath;
	add add1;
	SharedPreferences preferences;
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.login");
	public static final UMSocialService m1Controller = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		preferences = getSharedPreferences("count6", MODE_WORLD_READABLE);
		int count = preferences.getInt("count", 0);

		mController.getConfig().setSsoHandler(new SinaSsoHandler());
//微信分享
		weixin[0] = "我今天又睡过头了，哈哈哈哈，真是一个傻逼……";
		weixin[1] = "如果太阳不出来了，我就不去上课了；如果出来了，我就继续睡觉！因为，我只会睡觉！";
		weixin[2] = "我一个月总有那么30天不想学习,也总有那么30天只会睡觉。";
		weixin[3] = "俗话说：早起的虫儿被鸟吃。为了不被吃，我睡起觉来也是蛮拼的";
		weixin[4] = "什么动物最会睡，看我就知道……";
		String appID = "wx444edeebeb5c0402";
		String appSecret = "a5bbf5e67240b9ccc65c416e811e5368";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(MainActivity.this, appID,
				appSecret);
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(MainActivity.this, appID,
				appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		int whichOne = (int) (Math.random()*4);
		circleMedia.setShareContent(weixin[whichOne]);
		// 设置朋友圈title
		circleMedia.setTitle("朋友圈");
		// UMImage localImage = new UMImage(AlarmActivity.this, MainActivity.b);
		// circleMedia.setShareImage(localImage );
		// circleMedia.setTargetUrl("你的URL链接");
		m1Controller.setShareMedia(circleMedia);
		m1Controller.getConfig().setSsoHandler(new SinaSsoHandler());
		m1Controller.getConfig().removePlatform(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.DOUBAN);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // ①
		if (isWX) {
			isWX = false;
			// 获取传感器管理服务

			new MyCount(10000, 1000) {
				public void onTick(long millisUntilFinished) {
					// daojishi.setText("seconds remaining: " +
					// millisUntilFinished
					// / 1000);
					System.out.println("seconds remaining: "
							+ millisUntilFinished / 1000);
				}

				public void onFinish() {
					before[0] = values[0];
					before[1] = values[1];
					before[2] = values[2];
					before[3] = values1[0];
					before[4] = values1[1];
					before[5] = values1[2];
					System.out.println("111before:" + before[0] + ":"
							+ before[1] + ":" + before[2] + ":" + before[3]
							+ ":" + before[4] + ":" + before[5] + ":");
				}
			}.start();
			new MyCount(20000, 1000) {
				public void onTick(long millisUntilFinished) {
					// daojishi1.setText("seconds remaining: " +
					// millisUntilFinished
					// / 1000);
					System.out.println("seconds remaining: "
							+ millisUntilFinished / 1000);
				}

				public void onFinish() {
					after[0] = values[0];
					after[1] = values[1];
					after[2] = values[2];
					after[3] = values1[0];
					after[4] = values1[1];
					after[5] = values1[2];
					System.out.println("after:" + after[0] + ":" + after[1]
							+ ":" + after[2] + ":" + after[3] + ":" + after[4]
							+ ":" + after[5] + ":");
					if (Math.abs(after[0] - before[0]) > 10
							|| Math.abs(after[1] - before[1]) > 10
							|| Math.abs(after[2] - before[2]) > 10
							|| Math.abs(after[3] - before[3]) > 10
							|| Math.abs(after[4] - before[4]) > 10
							|| Math.abs(after[5] - before[5]) > 10) {
						System.out.println("yesyesyes");
					
					} else {
						System.out.println("nonono");
						m1Controller.directShare(MainActivity.this,
								SHARE_MEDIA.WEIXIN_CIRCLE, null);
					}
					MainActivity.this.finish();
					startActivity(new Intent(MainActivity.this,
							MainActivity.class));
				}
			}.start();
		}

		/*
		 * // 判断程序与第几次运行，如果是第一次运行则跳转到引导页面 if (count == 0) {
		 * System.out.println("这个第一次"); inti(); Editor editor =
		 * preferences.edit(); // 存入数据 editor.putInt("count", ++count); // 提交修改
		 * editor.commit(); } else { System.out.println("这个不是第一次"); }
		 */

		add1 = new add();
		addbn = (Button) findViewById(R.id.add);
		myface = (ImageView) findViewById(R.id.myface);
		// myface.setImageURI(Uri.parse(FaceActivity.filepathFace));
		Uri cameraImgUri = Uri.parse("file:///sdcard/CarzyAlarm/big.jpg");
		ContentResolver resolver = getContentResolver();
		byte[] mContent;
		try {
			mContent = GetImageActivity.readStream(resolver.openInputStream(Uri
					.parse(cameraImgUri.toString())));
			b = GetImageActivity.getPicFromBytes(mContent, null);
			myface.setImageBitmap(b);
			isExist = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		addbn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				isChange = 0;
				Intent intent = new Intent(MainActivity.this, add.class);
				startActivity(intent);

			}
		});

		myface.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((isExist) || (isNothingPic())) { // 存在
					Intent viewBigImage = new Intent(MainActivity.this,
							BigImage.class);
					Bundle bundle = new Bundle();
					bundle.putString(BIG_IMAGE, add.FACE);
					viewBigImage.putExtras(bundle);
					startActivity(viewBigImage);
				} else { // 不存在
					Intent face = new Intent(MainActivity.this,
							GetImageActivity.class);
					face.putExtra(add.BEFOREORAFTER, add.FACE);
					startActivity(face);
				}

			}
		});
		myface.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Intent face = new Intent(MainActivity.this,
						GetImageActivity.class);
				face.putExtra(add.BEFOREORAFTER, add.FACE);
				startActivity(face);
				return true;
			}
		});

		TabHost tabHost = getTabHost();

		TabSpec tab1 = tabHost.newTabSpec("tab1").setIndicator("闹钟") // 设置标题
				.setContent(R.id.tab01); // 设置内容
		tabHost.addTab(tab1);

		TabSpec tab3 = tabHost.newTabSpec("tab3").setIndicator("我的")
				.setContent(R.id.tab03);
		tabHost.addTab(tab3);

		TabSpec tab4 = tabHost.newTabSpec("tab4").setIndicator("通知")
				.setContent(R.id.tab04);
		tabHost.addTab(tab4);

		listView = (ListView) this.findViewById(R.id.alarm_list);
		listView.setDividerHeight(0);
		listView.setItemsCanFocus(false);
		information_list = (ListView) findViewById(R.id.information_list);
		information_list.setDividerHeight(0);
		information_list.setItemsCanFocus(false);

		mydbh = new myDatabaseHelper(this, configure.DB_NAME,
				myDatabaseHelper.TYPE_ALARM);
		db = mydbh.getReadableDatabase();
		// mydbhUser = new myDatabaseHelper(this, configure.DB_NAME_USER,
		// myDatabaseHelper.TYPE_USERS);
		// dbUser = mydbhUser.getReadableDatabase();
		mydbhInf = new myDatabaseHelper(this, configure.DB_NAME_INF,
				myDatabaseHelper.TYPE_INFS);
		dbInf = mydbhInf.getReadableDatabase();

		countTV = (TextView) findViewById(R.id.count);
		countTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// AlarmActivity.mController.login(MainActivity.this,
				// SHARE_MEDIA.SINA, null);

				mController.doOauthVerify(MainActivity.this, SHARE_MEDIA.SINA,
						new UMAuthListener() {
							@Override
							public void onError(SocializeException e,
									SHARE_MEDIA platform) {
							}

							@Override
							public void onComplete(Bundle value,
									SHARE_MEDIA platform) {
								if (value != null
										&& !TextUtils.isEmpty(value
												.getString("uid"))) {
									Toast.makeText(MainActivity.this, "授权成功.",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(MainActivity.this, "授权失败",
											Toast.LENGTH_SHORT).show();
								}
							}

							@Override
							public void onCancel(SHARE_MEDIA platform) {
							}

							@Override
							public void onStart(SHARE_MEDIA platform) {
							}
						});

				mController.getPlatformInfo(MainActivity.this,
						SHARE_MEDIA.SINA, new UMDataListener() {
							@Override
							public void onStart() {
								Toast.makeText(MainActivity.this,
										"获取平台数据开始...", Toast.LENGTH_SHORT)
										.show();
							}

							@Override
							public void onComplete(int status,
									Map<String, Object> info) {
								if (status == 200 && info != null) {
									StringBuilder sb = new StringBuilder();
									Set<String> keys = info.keySet();
									for (String key : keys) {
										sb.append(key + "="
												+ info.get(key).toString()
												+ "\r\n");
									}
									// Log.d("TestData",sb.toString());
								} else {
									// Log.d("TestData","发生错误："+status);
								}
							}
						});
			}
		});

		infAdapter1 = new infAdapter(this, getInfData());
		information_list.setAdapter(infAdapter1);

		timelineAdapter = new TimeLineAdapter(this, getData());
		listView.setAdapter(timelineAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				i = (items) timelineAdapter.getItem(position);
				isChange = 1;
				startActivity(new Intent(MainActivity.this, add.class));

			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this).setTitle("删除").setMessage("是否删除该闹钟");

				builder.setNegativeButton("取消", null)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										items i = (items) timelineAdapter
												.getItem(position);
										deleteItem(i.getMark());
										cancelAlarm(i.getMark());
										MainActivity.this.finish();
										startActivity(new Intent(
												MainActivity.this,
												MainActivity.class));
									}
								}).create().show();

				return true;
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 查询数据
	public List<items> getData() { // 添加数据

		Cursor cursor = db.rawQuery("select * from "
				+ myDatabaseHelper.TABLE_DB + " order by "
				+ myDatabaseHelper.HOUR + "," + myDatabaseHelper.MINUTE, null);
		List<items> lists = converCursotToList(cursor);
		return lists;

	}

	// 删除数据
	public static void deleteItem(int mark) {
		db.delete(myDatabaseHelper.TABLE_DB,
				myDatabaseHelper.MARK + "=" + mark, null);

	}

	// 修改数据(状态)
	public void updateItemOpen(int mark, boolean isOpen) {
		ContentValues values = new ContentValues();
		if (isOpen) {
			values.put(myDatabaseHelper.STATUS, configure.STATUS_TRUE);
			db.update(myDatabaseHelper.TABLE_DB, values, myDatabaseHelper.MARK
					+ "=?", new String[] { mark + "" });
			// 开始闹钟

		} else {
			values.put(myDatabaseHelper.STATUS, configure.STATUS_FALSE);
			db.update(myDatabaseHelper.TABLE_DB, values, myDatabaseHelper.MARK
					+ "=?", new String[] { mark + "" });

		}

	}

	public void cancelAlarm(int mark) {
		Intent intent = new Intent(MainActivity.this, alarmReceiver.class); // 创建Intent对象
		PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, mark,
				intent, 0); // 创建PendingIntent
		alarmManager.cancel(pi);
		System.out.println("取消闹钟" + mark);
	}

	public static ArrayList<items> converCursotToList(Cursor cursor) {

		ArrayList<items> lists = new ArrayList<items>();
		if (cursor.getCount() != 0) {

			while (cursor.moveToNext()) {
				items i = new items();

				i.setMark(cursor.getInt(0));
				i.setMode(cursor.getInt(1));
				i.setRepeat(cursor.getString(2));
				i.setRing(cursor.getString(3));
				i.setRingUri(cursor.getString(4));
				i.setVivrate(cursor.getInt(5));
				i.setHour(cursor.getInt(6));
				i.setMinute(cursor.getInt(7));
				i.setLabel(cursor.getString(8));
				i.setStatus(cursor.getInt(9));

				lists.add(i);
			}
		}
		return lists;

	}

	private List<infitem> getInfData() { // 添加数据

		Cursor cursor = dbInf.rawQuery("select * from "
				+ myDatabaseHelper.TABLE_IFO_DB + " order by "
				+ myDatabaseHelper.INFMONTH + "," + myDatabaseHelper.INFDAY
				+ "," + myDatabaseHelper.INFHOUR + ","
				+ myDatabaseHelper.INFMINUTE, null);
		List<infitem> lists = converCursotToListInf(cursor);
		return lists;

	}

	ArrayList<infitem> converCursotToListInf(Cursor cursor) {

		ArrayList<infitem> lists = new ArrayList<infitem>();
		if (cursor.getCount() != 0) {
			while (cursor.moveToNext()) {
				infitem i = new infitem();

				i.setMonth(cursor.getInt(1));
				i.setDay(cursor.getInt(2));
				i.setHour(cursor.getInt(3));
				i.setMinute(cursor.getInt(4));
				i.setText(cursor.getString(5));
				i.setMode(cursor.getString(6));
				lists.add(i);
			}
		}
		return lists;
	}

	public boolean isNothingPic() { // 存在返回true
		try {

			File f = new File(FaceActivity.filepathFace);
			if (!f.exists()) {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public String getSDCardPath() {
		File sdcardDir = null;
		// 判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
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
			Toast.makeText(MainActivity.this, millisUntilFinished / 1000 + "",
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
			// etOrientation.setText(sb.toString());
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
			// etMagnetic.setText(sb.toString());
			break;

		}
	}

}