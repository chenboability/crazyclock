package com.example.crazyclock.data;

public class infitem {

	int month;
	int day;
	int hour;
	int minute;
	String text;
	String mode;

	public infitem() {
	}

	public infitem(int month, int day, int hour, int minute, String text,
			String mode) {
		super();
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.text = text;
		this.mode = mode;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	

	
}
