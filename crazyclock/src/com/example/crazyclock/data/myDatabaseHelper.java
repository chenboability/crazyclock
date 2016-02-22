package com.example.crazyclock.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class myDatabaseHelper extends SQLiteOpenHelper {

	public static final String TABLE_DB = "alarmDB";
	public static final String TABLE_USER_DB = "userDB";
	public static final String TABLE_IFO_DB = "infDB";
	public static final String TABLE_ENGLISH_DB = "englishDB";

	public static final String MARK = "alarmMark";
	public static final String MODE = "alarmMode";
	public static final String REPEAT = "alarmRepert";
	public static final String RING = "alarmRing";
	public static final String RINGURI = "alarmRingUri";
	public static final String VIVRATE = "alarmVivrate";
	public static final String LABEL = "alarmText";
	public static final String HOUR = "alarmHour";
	public static final String MINUTE = "alarmMinute";
	public static final String STATUS = "alarmStatus";
	
	public static final String ID_ = "userID";
	public static final String FACE = "userFace";
	public static final String NAME = "userName";
	
	public static final String INFID = "infID";
	public static final String INFMONTH = "infMonth";
	public static final String INFDAY = "infDay";
	public static final String INFHOUR = "infHour";
	public static final String INFMINUTE = "infMnute";
	public static final String INFTEXT = "infText";
	public static final String INFMODE = "infMode";
	
	public static final String EMARK = "englishMark";
	public static final String ENGLISH = "englishWord";
	public static final String A = "chineseA";
	public static final String B = "chineseB";
	public static final String C = "chineseC";
	public static final String D = "chineseD";
	public static final String RIGHT = "rightAnswer";
	
	private int type;
	public static final int TYPE_ALARM = 0;
	public static final int TYPE_USERS = 1;
	public static final int TYPE_INFS = 2;
	public static final int TYPE_ENGLISH = 3;

	public static final String CREATE_TABLE = "create table " + TABLE_DB + "("
			+MARK+" integer primary key autoincrement,"
			+MODE+" integer,"
			+REPEAT+" varchar(20),"
			+RING+" varchar(50),"
			+RINGURI+" varchar(80),"
			+VIVRATE+" integer,"
			+HOUR+" integer,"
			+MINUTE+" integer,"
			+LABEL+" varchar(200),"
            +STATUS+" integer)";
	public static final String CREATE_USER_TABLE = "create table " + TABLE_USER_DB+"("
			+ID_+" integer primary key autoincrement,"
			+FACE+" varchar(50),"
			+NAME+" varchar(20))";
	public static final String CREATE_INF_TABLE = "create table " + TABLE_IFO_DB+"("
			+INFID+" integer primary key autoincrement,"
			+INFMONTH+" integer,"
			+INFDAY+" integer,"
			+INFHOUR+" integer,"
			+INFMINUTE+" integer,"
			+INFTEXT+" varchar(200),"
			+INFMODE+" integer)";
	
	public static final String CREATE_TABLE_ENGLISH = "create table " + TABLE_ENGLISH_DB + "("
			+EMARK+" integer primary key autoincrement,"
			+ENGLISH+" varchar(20),"
			+A+" varchar(20),"
			+B+" varchar(20),"
			+C+" varchar(20),"
			+D+" varchar(20),"
			+RIGHT+" integer)";
	

	public myDatabaseHelper(Context context, String name , int type) {
		super(context, name, null, 1);
		this.type = type;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		switch(type){
		case TYPE_ALARM:
			db.execSQL(CREATE_TABLE);
			break;
		case TYPE_USERS:
			db.execSQL(CREATE_USER_TABLE);
			break;
		case TYPE_INFS:
			db.execSQL(CREATE_INF_TABLE);
			break;
		case TYPE_ENGLISH:
			db.execSQL(CREATE_TABLE_ENGLISH);
			break;
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
