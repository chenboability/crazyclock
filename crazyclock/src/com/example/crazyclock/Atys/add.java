package com.example.crazyclock.Atys;

import java.io.FileNotFoundException;
import java.text.BreakIterator;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;

import com.example.crazyclock.MainActivity;
import com.example.crazyclock.R;
import com.example.crazyclock.configure;
import com.example.crazyclock.R.id;
import com.example.crazyclock.R.layout;
import com.example.crazyclock.data.items;
import com.example.crazyclock.data.myDatabaseHelper;
import com.example.crazyclock.tools.FaceActivity;
import com.example.crazyclock.tools.GetImageActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class add extends Activity {

	public static final int ALARMMUSIC = 0;
	public static final String BEFOREORAFTER = "beforeOrAfter";
	public static final String BEFORE = "before";
	public static final String FACE = "face";
	public static final String AFTER = "after";
	public Button cancel;
	public Button ok;
	public Button ringtone, picture;
	public static ImageView image;
	public Spinner spinnerMode;
	public Spinner spinnerRepeat;
	public Switch vibrate;
	public EditText label;
	public TextView timeinf;
	public TimePicker timepicker;
	AlertDialog.Builder builder;

	int isSet = 0;
	boolean isBeforeExit = true;
	final int isSelect[] = new int[7];
	String when[] = { "����һ", "���ڶ�", "������", "������", "������", "������", "������" };

	myDatabaseHelper mydbh;
	static SQLiteDatabase db;

	String statusStr = null;
	int mode;
	String repeat;
	String ring;
	Uri pickedUri_alarm = null;
	int isvibrate;
	String strLaber;
	int strHour;
	int strMinute;
	int prestrHour;
	int prestrMinute;

	public static items closestAlarm;
	public static Calendar c;

	public static AlarmManager amenage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_add);

		cancel = (Button) findViewById(R.id.cancel);
		ok = (Button) findViewById(R.id.ok);
		picture = (Button) findViewById(R.id.picture);
		image = (ImageView) findViewById(R.id.image);
		Uri cameraImgUri = Uri.parse("file:///sdcard/CarzyAlarm/before.jpg");
		ContentResolver resolver = getContentResolver();
		byte[] mContent;
		try {
			mContent = GetImageActivity.readStream(resolver.openInputStream(Uri
					.parse(cameraImgUri.toString())));
			Bitmap b = GetImageActivity.getPicFromBytes(mContent, null);
			image.setImageBitmap(b);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			isBeforeExit = false;
		} catch (Exception e) {
			e.printStackTrace();
			isBeforeExit = false;
		}
		ringtone = (Button) findViewById(R.id.ringtone);
		ringtone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(RingtoneManager.ACTION_RINGTONE_PICKER);
				// Don't pick 'Default'
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT,
						false);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "������������");
				// Show only ringtones
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
						RingtoneManager.TYPE_ALL);

				// �õ�ϵͳ������uri
				Uri pickedUri_alarm = RingtoneManager
						.getActualDefaultRingtoneUri(add.this,
								RingtoneManager.TYPE_ALARM);
				// Put checkmark next to the current ringtone for this contact
				if (pickedUri_alarm != null) {
					intent.putExtra(
							RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
							pickedUri_alarm);
					// intent.putExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI,
					// myUri);
				}
				// Launch!
				startActivityForResult(intent, ALARMMUSIC);
			}
		});
		spinnerMode = (Spinner) findViewById(R.id.spinner_mode);
		spinnerRepeat = (Spinner) findViewById(R.id.spinner_repeat);
		vibrate = (Switch) findViewById(R.id.vibrate);
		label = (EditText) findViewById(R.id.label);
		timeinf = (TextView) findViewById(R.id.timeinf);
		timepicker = (TimePicker) findViewById(R.id.timepicker);

		mydbh = new myDatabaseHelper(this, configure.DB_NAME,
				myDatabaseHelper.TYPE_ALARM);
		db = mydbh.getReadableDatabase();
		amenage = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		pickedUri_alarm = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_ALARM);

		// �޸�ʱ
		if (MainActivity.isChange == 1) { // Mainactivity�޸���ת��add

			switch (MainActivity.i.getMode()) {
			case 1:
				spinnerMode.setSelection(0);
				break;
			case 2:
				spinnerMode.setSelection(1);
				break;
			case 3:
				spinnerMode.setSelection(2);
				break;
			}

			if (MainActivity.i.getRepeat().equals(configure.REPEAT_ONCE))
				spinnerRepeat.setSelection(0);
			else if (MainActivity.i.getRepeat().equals(
					configure.REPEAT_EVERYDAY))
				spinnerRepeat.setSelection(1);
		//	else if (MainActivity.i.getRepeat()
		//			.equals(configure.REPEAT_WORKDAY))
		//		spinnerRepeat.setSelection(2);
		//	else {
		//		spinnerRepeat.setSelection(3);
		//	}

			ringtone.setText(MainActivity.i.getRing());

			if (MainActivity.i.getVivrate() == 1) {
				vibrate.setChecked(true);
			} else if (MainActivity.i.getVivrate() == 0) {
				vibrate.setChecked(false);
			}

			label.setText(MainActivity.i.getLabel());

			strHour = MainActivity.i.getHour();
			strMinute = MainActivity.i.getMinute();
			timepicker.setCurrentHour(MainActivity.i.getHour());
			timepicker.setCurrentMinute(MainActivity.i.getMinute());

		} else {// ��ͨ
			strHour = timepicker.getCurrentHour();
			strMinute = timepicker.getCurrentMinute();
		}

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isSet == 1) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							add.this).setTitle("ע��").setMessage("�Ƿ�����������ӣ�");

					builder.setNegativeButton("ȡ��", null)
							.setPositiveButton("����",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											add.this.finish();
										}
									}).create().show();

				} else if ((MainActivity.isChange == 1) && (!ischanged())) { // �޸���
					AlertDialog.Builder builder = new AlertDialog.Builder(
							add.this).setTitle("ע��").setMessage("�Ƿ�����޸����ӣ�");

					builder.setNegativeButton("ȡ��", null)
							.setPositiveButton("����",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											add.this.finish();

										}
									}).create().show();
				} else {
					add.this.finish();
				}

			}
		});

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (MainActivity.isChange == 0) { // һ�������

					addItemClock();
					Bundle bundle = reCaculateAlarm(-1);
					Intent intent = new Intent(add.this, alarmReceiver.class); // ����Intent����
					intent.putExtras(bundle);
					PendingIntent pi = PendingIntent.getBroadcast(add.this,
							closestAlarm.getMark(), intent, 0); // ����PendingIntent
					amenage.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
							pi);
					Toast.makeText(add.this, "�������óɹ���" + statusStr,
							Toast.LENGTH_SHORT).show();
					add.this.finish();
					Intent intent1 = new Intent(add.this, MainActivity.class);
					startActivity(intent1);

				} else if (MainActivity.isChange == 1) {// ���޸ģ�Ҫ����
					MainActivity.isChange = 0;
					if (ischanged()) { // ����
						add.this.finish();

					} else {
						MainActivity.deleteItem(MainActivity.i.getMark());
						addItemClock();

						Bundle bundle = reCaculateAlarm(-1);
						Intent intent = new Intent(add.this,
								alarmReceiver.class); // ����Intent����
						intent.putExtras(bundle);
						PendingIntent pi = PendingIntent.getBroadcast(add.this,
								closestAlarm.getMark(), intent, 0); // ����PendingIntent
						amenage.set(AlarmManager.RTC_WAKEUP,
								c.getTimeInMillis(), pi);

						Toast.makeText(add.this, "�������óɹ���" + statusStr,
								Toast.LENGTH_SHORT).show();
						add.this.finish();
						Intent intent1 = new Intent(add.this,
								MainActivity.class);
						startActivity(intent1);
					}
				}
			}
		});

		picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent before = new Intent(add.this, GetImageActivity.class);
				before.putExtra(BEFOREORAFTER, BEFORE);
				startActivity(before);
				
			}
		});
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isBeforeExit){
				Intent viewBigImage = new Intent(add.this,
						BigImage.class);
				Bundle bundle = new Bundle();
				bundle.putString(MainActivity.BIG_IMAGE, BEFORE);
				viewBigImage.putExtras(bundle);
				startActivity(viewBigImage);
				}
			}
		});
		
		

		spinnerMode.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 3) {
					picture.setVisibility(View.VISIBLE);
					image.setVisibility(View.VISIBLE);
				} else {
					picture.setVisibility(View.GONE);
					image.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	/*	spinnerRepeat.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 3) {
					if (MainActivity.isChange == 0) { // ���޸�
						builder = new AlertDialog.Builder(add.this).setTitle(
								"��ѡ��").setMultiChoiceItems(
								when,
								new boolean[] { false, false, false, false,
										false, false, false },
								new OnMultiChoiceClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which, boolean isChecked) {
										if (isChecked) {
											isSelect[which] = 1;
										} else {
											isSelect[which] = 0;
										}
									}
								});

						builder.setNegativeButton("ȡ��", null)
								.setPositiveButton("ȷ��",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton) {
												String what = null;
												StringBuffer selectDay = new StringBuffer();
												for (int i = 0; i < isSelect.length; i++) {
													switch (i) {
													case 0:
														what = "����һ";
														break;
													case 1:
														what = "���ڶ�";
														break;
													case 2:
														what = "������";
														break;
													case 3:
														what = "������";
														break;
													case 4:
														what = "������";
														break;
													case 5:
														what = "������";
														break;
													case 6:
														what = "������";
														break;
													}
													if (isSelect[i] == 1) {
														selectDay.append(what
																+ "");
													}
												}
												repeat = selectDay.toString();

											}
										}).create().show();
					} else {// �޸�
						builder = new AlertDialog.Builder(add.this).setTitle(
								"��ѡ��").setMultiChoiceItems(
								when,
								new boolean[] { true, false, false, false,
										false, false, false },
								new OnMultiChoiceClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which, boolean isChecked) {
										if (isChecked) {
											isSelect[which] = 1;
										} else {
											isSelect[which] = 0;
										}
									}
								});

						builder.setNegativeButton("ȡ��", null)
								.setPositiveButton("ȷ��",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton) {
												String what = null;
												StringBuffer selectDay = new StringBuffer();
												for (int i = 0; i < isSelect.length; i++) {
													switch (i) {
													case 0:
														what = "����һ";
														break;
													case 1:
														what = "���ڶ�";
														break;
													case 2:
														what = "������";
														break;
													case 3:
														what = "������";
														break;
													case 4:
														what = "������";
														break;
													case 5:
														what = "������";
														break;
													case 6:
														what = "������";
														break;
													}
													if (isSelect[i] == 1) {
														selectDay.append(what
																+ "");
													}
												}
												repeat = selectDay.toString();

											}
										}).create().show();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});*/

		timepicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int setHour,
					int setMinute) {
				isSet = 1;

				strHour = setHour;
				strMinute = setMinute;

				Calendar cc = Calendar.getInstance();
				int hour1 = cc.get(Calendar.HOUR_OF_DAY);
				int minute1 = cc.get(Calendar.MINUTE);

				if (setHour == hour1) {
					if (setMinute > minute1)
						statusStr = "���ӽ���" + (setMinute - minute1) + "���Ӻ���";
					else if (setMinute == minute1)
						statusStr = "���ӽ���С��1���Ӻ���";
					else if (setMinute < minute1)
						statusStr = "���ӽ���23Сʱ" + (60 - (minute1 - setMinute))
								+ "���Ӻ���";
				} else if (setHour > hour1) {
					if (setMinute > minute1)
						statusStr = "���ӽ���" + (setHour - hour1) + "Сʱ"
								+ (setMinute - minute1) + "���Ӻ���";
					else if (setMinute == minute1)
						statusStr = "���ӽ���" + (setHour - hour1) + "Сʱ����";
					else if (setMinute < minute1)
						statusStr = "���ӽ���" + (setHour - hour1 - 1) + "Сʱ"
								+ (60 - (minute1 - setMinute)) + "���Ӻ���";

				} else if (setHour < hour1) {
					if (setMinute > minute1)
						statusStr = "���ӽ���" + (24 - (hour1 - setHour)) + "Сʱ"
								+ (setMinute - minute1) + "���Ӻ���";
					else if (setMinute == minute1)
						statusStr = "���ӽ���" + (24 - (hour1 - setHour)) + "Сʱ����";
					else if (setMinute < minute1)
						statusStr = "���ӽ���" + (24 - (hour1 - setHour) - 1)
								+ "Сʱ" + (60 - (minute1 - setMinute)) + "���Ӻ���";

				}

				timeinf.setText(statusStr);
			}
		});

	}

	// �ж��Ƿ��иı�
	private boolean ischanged() {
		int nowMode = 0;
		String nowRepeat = null;
		int nowVib = 0;
		switch (spinnerMode.getSelectedItemPosition()) {
		case 0:
			nowMode = configure.MODE_NORMAL;
			break;
		case 1:
			nowMode = configure.MODE_ENGLISH;
			break;
		case 2:
			nowMode = configure.MODE_WX;
			break;
		case 3:
			nowMode = configure.MODE_PICTURE;
			break;
		}

		switch (spinnerRepeat.getSelectedItemPosition()) {
		case 0:
			nowRepeat = configure.REPEAT_ONCE;
			break;
		case 1:
			nowRepeat = configure.REPEAT_EVERYDAY;
			break;
	//	case 2:
	//		nowRepeat = configure.REPEAT_WORKDAY;
	//		break;

		}
		if (vibrate.isChecked()) {
			nowVib = 1;
		} else {
			nowVib = 0;
		}

		if ((MainActivity.i.getMode() == nowMode) // û���޸ģ�true
				&& (MainActivity.i.getRepeat().equals(nowRepeat))
				&& (MainActivity.i.getRing().equals(ringtone.getText()
						.toString()))
				&& (MainActivity.i.getVivrate() == nowVib)
				&& (MainActivity.i.getLabel()
						.equals(label.getText().toString()))
				&& (MainActivity.i.getHour() == timepicker.getCurrentHour())
				&& (MainActivity.i.getMinute() == timepicker.getCurrentMinute()))

		{
			return true;
		}
		return false;

	}

	// �������
	private void addItemClock() {
		switch (spinnerMode.getSelectedItemPosition()) {
		case 0:
			mode = configure.MODE_NORMAL;
			break;
		case 1:
			mode = configure.MODE_ENGLISH;
			break;
		case 2:
			mode = configure.MODE_WX;
			break;
		case 3:
			mode = configure.MODE_PICTURE;
			break;
		}

		switch (spinnerRepeat.getSelectedItemPosition()) {
		case 0:
			repeat = configure.REPEAT_ONCE;
			break;
		case 1:
			repeat = configure.REPEAT_EVERYDAY;
			break;
	//	case 2:
	//		repeat = configure.REPEAT_WORKDAY;
	//		break;

		}

		ring = ringtone.getText().toString();

		if (vibrate.isChecked()) {
			isvibrate = 1;
		} else {
			isvibrate = 0;
		}

		strLaber = label.getText().toString();

		String insert = "insert into " + myDatabaseHelper.TABLE_DB
				+ " values(null," + mode + ",?,?,?," + isvibrate + ","
				+ strHour + "," + strMinute + ",?," + configure.STATUS_TRUE
				+ ")";
		db.execSQL(insert, new String[] { repeat, ring, pickedUri_alarm + "",
				strLaber });
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case ALARMMUSIC:
			try {
				// �õ�����ѡ�������
				pickedUri_alarm = data
						.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

			} catch (Exception e) {
			}
			getName(RingtoneManager.TYPE_ALARM, ringtone, pickedUri_alarm);
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// ��ȡ���ӵ�����
	private void getName(int type, Button button, Uri pickedUri_alarm) {
		Uri pickedUri = pickedUri_alarm;
		// ��picked_Uri=null, ��ʾ����
		if (pickedUri == null) {
			String ring_name = "silent";
			button.setText(ring_name);
		}
		// ��ѯ���ݿ⣬��ѯ��ǰ���������Ʋ���ʾ
		Cursor cursor = this.getContentResolver()
				.query(pickedUri,
						new String[] { MediaStore.Audio.Media.TITLE }, null,
						null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				String ring_name = cursor.getString(0);
				button.setText(ring_name);
			}
			cursor.close();
		}

	}

	public static Bundle reCaculateAlarm(int num) {

		closestAlarm = Alarms.getClosestAlarm(num);
		if (closestAlarm != null) {

			int closestIsVibrate = closestAlarm.getVivrate();
			String closestStrLabel = closestAlarm.getLabel();
			String closestpickedUri_alarm = closestAlarm.getRingUri();
			int closestMark = closestAlarm.getMark();
			String closestRepeat = closestAlarm.getRepeat();
			int closestMode = closestAlarm.getMode();
			int closestHour = closestAlarm.getHour();
			int closestMinute = closestAlarm.getMinute();

			Bundle bundle = new Bundle();
			if (closestIsVibrate == 0)
				bundle.putInt(configure.VIBRATE, configure.VIVRATE_FALSE);
			else
				bundle.putInt(configure.VIBRATE, configure.VIVRATE_TRUE);
			bundle.putString(configure.LABEL, closestStrLabel);
			bundle.putString(configure.MUSIC, closestpickedUri_alarm);
			bundle.putInt(configure.MARK, closestMark);
			bundle.putString(configure.REPEAT, closestRepeat);
			bundle.putInt(configure.MODE, closestMode);
			bundle.putInt(configure.HOUR, closestHour);
			bundle.putInt(configure.MINUTE, closestMinute);

			c = Calendar.getInstance();

			c.setTimeInMillis(System.currentTimeMillis()); // ����Calendar����
			c.set(Calendar.HOUR_OF_DAY, closestAlarm.getHour());
			c.set(Calendar.MINUTE, closestAlarm.getMinute());
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			Calendar currentTime = Calendar.getInstance();

			if (c.getTimeInMillis() <= currentTime.getTimeInMillis()) {
				c.setTimeInMillis(c.getTimeInMillis() + 24 * 60 * 60 * 1000);
			}

			return bundle;
		}
		return null;
	}

	public static class Alarms {
		static long currTime;
		static long alarmTime = 0;
		static int minindex = 0;
		static long minTime;

		public static items getClosestAlarm(int num) {
			List<items> allAlarm = getAllAlarm(num);
			if (allAlarm.size() > 0) {
				items i = new items();
				currTime = getNowTime();// ��ǰʱ��
				System.out.println(currTime
						+ "                            currTime");

				for (int j = 0; j < allAlarm.size(); j++) {
					Calendar minAlarm = Calendar.getInstance();
					int hour = allAlarm.get(j).getHour();
					int min = allAlarm.get(j).getMinute();
					minAlarm.set(Calendar.HOUR_OF_DAY, hour);
					minAlarm.set(Calendar.MINUTE, min);
					if (minAlarm.getTimeInMillis() <= currTime) {
						minAlarm.setTimeInMillis(minAlarm.getTimeInMillis()
								+ 24 * 60 * 60 * 1000);
					}
					if (j == 0) {
						minTime = minAlarm.getTimeInMillis();
					}

					alarmTime = minAlarm.getTimeInMillis();
					System.out.println(alarmTime
							+ "                      alarmTime");
					System.out.println(hour + ":" + min
							+ "                      h&m");
					if (alarmTime < minTime) {
						minindex = j;
						minTime = alarmTime;
					}

				}
				System.out.println("min:" + minTime);
				System.out.println(minindex);
				i = allAlarm.get(minindex);
				minindex = 0;
				System.out.println(i.getHour() + ":" + i.getMinute());
				return i;
			}
			return null;
		}

		static long getNowTime() {
			Calendar currCalendar = Calendar.getInstance();
			currCalendar.setTimeInMillis(System.currentTimeMillis());
			return currCalendar.getTimeInMillis();
		}

		static List<items> getAllAlarm(int num) {
			Cursor cursor = db.rawQuery("select * from "
					+ myDatabaseHelper.TABLE_DB + " where "
					+ myDatabaseHelper.STATUS + " = " + configure.STATUS_TRUE
					+ " and " + myDatabaseHelper.MARK + " <> " + num
					+ " order by " + myDatabaseHelper.HOUR + ","
					+ myDatabaseHelper.MINUTE, null);
			List<items> lists = MainActivity.converCursotToList(cursor);
			return lists;
		}

	}
}
