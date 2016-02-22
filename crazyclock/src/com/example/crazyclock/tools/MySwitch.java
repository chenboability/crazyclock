package com.example.crazyclock.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Switch;

public class MySwitch extends Switch {

	private int index = -1;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public MySwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MySwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MySwitch(Context context) {
		super(context);
	}

}
