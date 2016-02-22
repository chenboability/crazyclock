package com.example.crazyclock.tools;

import java.text.DecimalFormat;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * AndroidͼƬ�Ա�(���ؾ�׼�Ա�),�ٶȽ��������ö��̻߳�ȡ
 * 
 * @author xupp
 * @createData 2013-7-18
 */

public class PictureContrast {
	private static int t = 0;
	private static int f = 0;

	public static double similarity(String url_one, String url_two) {
		// ��ͼƬת��ΪBitmap
		Bitmap bm_one = BitmapFactory.decodeFile(url_one);
		Bitmap bm_two = BitmapFactory.decodeFile(url_two);
		// ����ͼƬ�������ظ��������飬ͼƬ�����
		int[] pixels_one = new int[bm_one.getWidth() * bm_one.getHeight()];
		int[] pixels_two = new int[bm_two.getWidth() * bm_two.getHeight()];
		// ��ȡÿ�����ص�RGBֵ
		bm_one.getPixels(pixels_one, 0, bm_one.getWidth(), 0, 0,
				bm_one.getWidth(), bm_one.getHeight());
		bm_two.getPixels(pixels_two, 0, bm_two.getWidth(), 0, 0,
				bm_two.getWidth(), bm_two.getHeight());
		// ���ͼƬһ�����ش���ͼƬ2�����أ����������ٵ���Ϊѭ�����������ⱨ��
		if (pixels_one.length >= pixels_two.length) {
			// ��ÿһ�����ص�RGBֵ���бȽ�
			for (int i = 0; i < pixels_two.length; i++) {
				int clr_one = pixels_one[i];
				int clr_two = pixels_two[i];
				// RGBֵһ���ͼ�һ���Ա���ٷֱȣ�
				if (clr_one == clr_two) {
					t++;
				} else {
					f++;
				}
			}
		} else {
			for (int i = 0; i < pixels_one.length; i++) {
				int clr_one = pixels_one[i];
				int clr_two = pixels_two[i];
				if (clr_one == clr_two) {
					t++;
				} else {
					f++;
				}
			}

		}

		return  myPercent(t, t + f);

	}

	/**
	 * �ٷֱȵļ���
	 * 
	 * @author xupp
	 * @param y
	 *            (ĸ��)
	 * @param z
	 *            �����ӣ�
	 * @return �ٷֱȣ�����С�������λ��
	 */
	public static double myPercent(int y, int z) {
		//String baifenbi = "";// ���ܰٷֱȵ�ֵ
		double baiy = y * 1.0;
		double baiz = z * 1.0;
		double fen = baiy / baiz;
	//	DecimalFormat df1 = new DecimalFormat("00.00%"); // ##.00%
															// �ٷֱȸ�ʽ�����治��2λ����0����
	//	baifenbi = df1.format(fen);
		return fen;
	}

}