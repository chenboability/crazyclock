package com.example.crazyclock.data;

public class word {

	String english;
	String a;
	String b;
	String c;
	String d;
	int rightAnswer;

	public word() {
	}

	public word(String english, String a, String b, String c, String d,
		int rightAnswer) {
		super();
		this.english = english;
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.rightAnswer = rightAnswer;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public int getRightAnswer() {
		return rightAnswer;
	}

	public void setRightAnswer(int rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

}
