package com.example.crazyclock.Atys;

import java.io.IOException;
import java.util.Calendar;

import com.example.crazyclock.MainActivity;
import com.example.crazyclock.R;
import com.example.crazyclock.configure;
import com.example.crazyclock.data.myDatabaseHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AlarmActivity extends Activity {

	String weibo, weixin;
	public static MediaPlayer mMediaPlayer;
	public static Vibrator vibarator;
	myDatabaseHelper mydbh;
	static SQLiteDatabase db;
	public static boolean isTwoPictureSame = false;
	boolean isRight = false;

	String label = "˯����� ������High��";
	String music = null;
	int mark = 0;
	String repeat = null;
	int mode = 0;
	String strMode;
	int hour = 0;
	int minute = 0;
	Uri musicUri = null;

	String[] items;

	public static final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		weibo = "�ҽ�����˯��ͷ�ˣ���������������һ��ɵ�ơ���";
		weixin = "�ҽ�����˯��ͷ�ˣ���������������һ��ɵ�ơ����ҽ�����˯��ͷ�ˣ���������������һ��ɵ�ơ����ҽ�����˯��ͷ�ˣ���������������һ��ɵ�ơ���";

		String appID = "wx444edeebeb5c0402";
		String appSecret = "a5bbf5e67240b9ccc65c416e811e5368";
		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(AlarmActivity.this, appID,
				appSecret);
		wxHandler.addToSocialSDK();
		// ���΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(AlarmActivity.this,
				appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// ����������Activity��������³�Ա����

		// ���÷�������
	//	mController.setShareContent(weibo);
		
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		
		// ���÷���ͼƬ, ����2ΪͼƬ��url��ַ
		// mController.setShareMedia(new UMImage(AlarmActivity.this,
		// "http://www.umeng.com/images/pic/banner_module_social.png"));
		// ���÷���ͼƬ������2Ϊ����ͼƬ����Դ����
		// mController.setShareMedia(new UMImage(AlarmActivity.this,
		// R.drawable.bottom_bar));
		// ���÷���ͼƬ������2Ϊ����ͼƬ��·��(����·��)
	//	 mController.setShareMedia(new UMImage(AlarmActivity.this,
	//	 BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

		// ���÷�������
		// UMusic uMusic = new
		// UMusic("http://sns.whalecloud.com/test_music.mp3");
		// uMusic.setAuthor("GuGu");
		// uMusic.setTitle("����֮��");
		// ������������ͼ
		// uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		// mController.setShareMedia(uMusic);

		// ���÷�����Ƶ
		// UMVideo umVideo = new UMVideo(
		// "http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
		// ������Ƶ����ͼ
		// umVideo.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		// umVideo.setTitle("������ữ����!");
		// mController.setShareMedia(umVideo);
		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.DOUBAN);

		// ����΢�ź��ѷ�������
	//	WeiXinShareContent weixinContent = new WeiXinShareContent();
		// ���÷�������
	//	weixinContent.setShareContent("����������ữ�����SDK�����ƶ�Ӧ�ÿ��������罻�����ܣ�΢��");
		// ����title
	//	weixinContent.setTitle("������ữ�������-΢��");
		// ���÷���������תURL
		// weixinContent.setTargetUrl("���URL����");
		// ���÷���ͼƬ
		// weixinContent.setShareImage(localImage);
	//	mController.setShareMedia(weixinContent);

		// ����΢������Ȧ��������
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(weixin);
		// ��������Ȧtitle
		circleMedia.setTitle("����Ȧ");
	//	 UMImage localImage = new UMImage(AlarmActivity.this, MainActivity.b);
	//	circleMedia.setShareImage(localImage );
		// circleMedia.setTargetUrl("���URL����");
		mController.setShareMedia(circleMedia);

		musicUri = getSystemDefultRingtoneUri();

		mydbh = new myDatabaseHelper(this, configure.DB_NAME,
				myDatabaseHelper.TYPE_ALARM);
		db = mydbh.getReadableDatabase();

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		Bundle bundle = this.getIntent().getExtras();
		vibarator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

		if (bundle.getInt(configure.VIBRATE) == configure.VIVRATE_TRUE) {
			vibarator.vibrate(new long[] { 400, 800, 1200, 1600 }, 0);
		}
		if ((!bundle.getString(configure.LABEL).equals(""))
				&& bundle.getString(configure.LABEL) != null)
			label = bundle.getString(configure.LABEL);
		music = bundle.getString(configure.MUSIC);
		mark = bundle.getInt(configure.MARK);
		repeat = bundle.getString(configure.REPEAT);
		mode = bundle.getInt(configure.MODE);
		hour = bundle.getInt(configure.HOUR);
		minute = bundle.getInt(configure.MINUTE);
		musicUri = Uri.parse(music);
		switch (mode) {
		case configure.MODE_NORMAL:
			strMode = "��ͨģʽ";
			break;
		case configure.MODE_ENGLISH:
			strMode = "Ӣ��ģʽ";
			break;
		case configure.MODE_WX:
			strMode = "����Ȧģʽ";
			break;
		case configure.MODE_PICTURE:
			strMode = "����ģʽ";
			break;
		default:
			break;
		}

		startAlarm();

		new AlertDialog.Builder(AlarmActivity.this).setTitle("����")
				.setMessage(label).setCancelable(false)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if (mode == configure.MODE_NORMAL) { // ����ģʽ
							vibarator.cancel();
							mMediaPlayer.stop();
							insertInf(hour, minute, label, strMode);// ����֪ͨ
							if (repeat.equals(configure.REPEAT_ONCE)) {
								updateItemOpen(mark, configure.STATUS_FALSE);
							}
							toNext();

						} else if (mode == configure.MODE_ENGLISH) {

							if (repeat.equals(configure.REPEAT_ONCE)) {
								updateItemOpen(mark, configure.STATUS_FALSE);
							}
							toNext();
							insertInf(hour, minute, label, strMode);// ����֪ͨ
							startActivity(new Intent(AlarmActivity.this,
									englishTi.class));

						} else if (mode == configure.MODE_WX) {
							
							/*
							 * mController.getConfig().removePlatform(
							 * SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN,
							 * SHARE_MEDIA.TENCENT);
							 */
						//	mController.registerListener(mSnsPostListener);
						//	 mController.openShare(AlarmActivity.this, false);
							// mController.shareTo(AlarmActivity.this,
							// SHARE_MEDIA.SINA, weibo, null);
							// mController.directShare(AlarmActivity.this,
							// SHARE_MEDIA.SINA, null);
					//		mController.directShare(AlarmActivity.this,
					//				SHARE_MEDIA.WEIXIN_CIRCLE, null);
							// mController.postShare(AlarmActivity.this,
							// SHARE_MEDIA.WEIXIN_CIRCLE, null);
							// mController.postShareMulti(AlarmActivity.this,
							// null,
							// SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA);

							vibarator.cancel();
							mMediaPlayer.stop();
							MainActivity.isWX = true;
							insertInf(hour, minute, label, strMode);// ����֪ͨ
							if (repeat.equals(configure.REPEAT_ONCE)) {
								updateItemOpen(mark, configure.STATUS_FALSE);
							}
							toNext();
							startActivity(new Intent(AlarmActivity.this,MainActivity.class));
					
						} else if (mode == configure.MODE_PICTURE) {
							isTwoPictureSame = true;
							if (repeat.equals(configure.REPEAT_ONCE)) {
								updateItemOpen(mark, configure.STATUS_FALSE);
							}
							toNext();
							insertInf(hour, minute, label, strMode);// ����֪ͨ
							startActivity(new Intent(AlarmActivity.this,
									takePicture.class));
							
						}

					}
				}).create().show();
	}


	private void startAlarm() {
		mMediaPlayer = MediaPlayer.create(this, musicUri);
		mMediaPlayer.setLooping(true);
		try {
			mMediaPlayer.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mMediaPlayer.start();
	}

	private Uri getSystemDefultRingtoneUri() {
		return RingtoneManager.getActualDefaultRingtoneUri(this,
				RingtoneManager.TYPE_RINGTONE);
	}

	// �޸�����(״̬)
	public void updateItemOpen(int mark, int isOpen) {
		ContentValues values = new ContentValues();
		if (isOpen == configure.STATUS_TRUE) {
			values.put(myDatabaseHelper.STATUS, configure.STATUS_TRUE);
			// ��ʼ����
		} else {
			values.put(myDatabaseHelper.STATUS, configure.STATUS_FALSE);
			// ȡ������
		}
		db.update(myDatabaseHelper.TABLE_DB, values, myDatabaseHelper.MARK
				+ "=?", new String[] { mark + "" });
	}

	void insertInf(int hour, int minute, String text, String mode) {
		Calendar currCalendar = Calendar.getInstance();
		currCalendar.setTimeInMillis(System.currentTimeMillis());
		int strMoth = currCalendar.get(Calendar.MONTH);
		int strDay = currCalendar.get(Calendar.DAY_OF_MONTH);
		String insert = "insert into " + myDatabaseHelper.TABLE_IFO_DB
				+ " values(null," + (strMoth + 1) + "," + (strDay + 1) + ","
				+ hour + "," + minute + ",?,?)";
		MainActivity.dbInf.execSQL(insert, new String[] { text, mode });
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** ʹ��SSO��Ȩ����������´��� */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		
	}

	public void toNext() {
		Bundle bundle = add.reCaculateAlarm(mark);
		if (bundle != null) {
			Intent intent = new Intent(AlarmActivity.this, alarmReceiver.class); // ����Intent����
			intent.putExtras(bundle);
			PendingIntent pi = PendingIntent.getBroadcast(AlarmActivity.this,
					add.closestAlarm.getMark(), intent, 0); // ����PendingIntent
			add.amenage.set(AlarmManager.RTC_WAKEUP, add.c.getTimeInMillis(),
					pi);
		}
		startActivity(new Intent(AlarmActivity.this, MainActivity.class));
		AlarmActivity.this.finish();
	}

	SnsPostListener mSnsPostListener = new SnsPostListener() {

		@Override
		public void onStart() {

		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int stCode,
				SocializeEntity entity) {
			if (stCode == 200) {
				Toast.makeText(AlarmActivity.this, "����ɹ�", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(AlarmActivity.this,
						"����ʧ�� : error code : " + stCode, Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

}
