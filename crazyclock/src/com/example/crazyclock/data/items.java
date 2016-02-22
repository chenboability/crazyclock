package com.example.crazyclock.data;

public class items {

	int mark;
	int mode;
	String repeat;
	String ring;
	String ringUri;
	int vivrate;
	int hour;
	int minute;
	String label;
	int status;

	public items() {
		super();
	}

	public items(int mark,int mode, String repeat, String ring,  String ringUri,int vivrate, int hour,
			int minute, String label , int status) {
		super();
		this.mark = mark;
		this.mode = mode;
		this.repeat = repeat;
		this.ring = ring;
		this.ringUri = ringUri;
		this.vivrate = vivrate;
		this.hour = hour;
		this.minute = minute;
		this.label = label;
		this.status = status;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public String getRing() {
		return ring;
	}

	public void setRing(String ring) {
		this.ring = ring;
	}

	public String getRingUri() {
		return ringUri;
	}

	public void setRingUri(String ringUri) {
		this.ringUri = ringUri;
	}

	public int getVivrate() {
		return vivrate;
	}

	public void setVivrate(int vivrate) {
		this.vivrate = vivrate;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	

}
