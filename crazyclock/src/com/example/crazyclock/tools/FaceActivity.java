package com.example.crazyclock.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.example.crazyclock.MainActivity;
import com.example.crazyclock.R;
import com.example.crazyclock.Atys.AlarmActivity;
import com.example.crazyclock.Atys.add;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FaceActivity extends Activity implements OnClickListener {
	public static final String IMAGE_BIG = "/big.jpg";
	public static final String IMAGE_BEFORE = "/before.jpg";
	public static final String IMAGE_AFTER = "/after.jpg";

	public static String filepathFace = "file:///sdcard/CarzyAlarm/big.jpg";
	public static String filepathBefore = "file:///sdcard/CarzyAlarm/before.jpg";
	public static String filepathAfter = "file:///sdcard/CarzyAlarm/after.jpg";
	File file = null;
	File path = null;
	private ImageView imageView;
	private ImageView image2;
	private LinearLayout cropLayout;
	private Button btn, cancelBtn;
	private static int height;
	private static int width;
	private static int line;
	private Matrix matrix = new Matrix();
	private static float startX, startY;
	private float zoomDegree = 1;
	private Context context;
	private static Handler handler;
	public static Bitmap img;
	private View myView;
	public static String SavePath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		img = CropApplication.getBitmap();
		filepathFace = getSDCardPath() + "/CarzyAlarm" + IMAGE_BIG;
		setContentView(R.layout.main);
		this.context = this;
		initView();
		initHandler();

	}

	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					findViewById(R.id.bar).setVisibility(View.GONE);
					image2.setVisibility(View.VISIBLE);
					break;

				default:
					break;
				}
			}
		};
	}

	public static void sendHandlerMessage(int what, Object object) {
		if (handler == null) {
			return;
		}

		Message msg = handler.obtainMessage(what, object);
		handler.sendMessage(msg);
	}

	private void initView() {
		imageView = (ImageView) findViewById(R.id.img);
		imageView.setOnTouchListener(new TounchListener());
		image2 = (ImageView) findViewById(R.id.img2);
		cropLayout = (LinearLayout) findViewById(R.id.cropLayout);
		myView = new MyView(this);
		cropLayout.addView(myView);
		btn = (Button) findViewById(R.id.ok);
		btn.setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.cancel);
		cancelBtn.setOnClickListener(this);

	}

	/**
	 * 监听缩放、平移
	 * 
	 * @author lcy
	 *
	 */
	private class TounchListener implements OnTouchListener {

		private PointF startPoint = new PointF();

		private Matrix currentMaritx = new Matrix();

		private int mode = 0;// 用于标记模式
		private static final int DRAG = 1;// 拖动
		private static final int ZOOM = 2;// 放大
		private float startDis = 0;
		private PointF midPoint;// 中心点

		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				mode = DRAG;
				currentMaritx.set(imageView.getImageMatrix());// 记录ImageView当期的移动位置
				startPoint.set(event.getX(), event.getY());// 开始点
				break;

			case MotionEvent.ACTION_MOVE:// 移动事件
				if (mode == DRAG) {// 图片拖动事件
					float dx = event.getX() - startPoint.x;// x轴移动距离
					float dy = event.getY() - startPoint.y;

					int x = (int) (dx / 50);
					int y = (int) (dy / 50);

					int[] start = new int[2];
					imageView.getLocationInWindow(start);
					matrix.set(currentMaritx);// 在当前的位置基础上移动
					matrix.postTranslate(dx, dy);
					// }
				} else if (mode == ZOOM) {// 图片放大事件
					float endDis = distance(event);// 结束距离
					if (endDis > 10f) {
						float scale = endDis / startDis;// 放大倍数
						matrix.set(currentMaritx);
						matrix.postScale(scale, scale, midPoint.x, midPoint.y);
					}

				}

				break;

			case MotionEvent.ACTION_UP:
				float dx = event.getX() - startPoint.x;// x轴移动距离
				float dy = event.getY() - startPoint.y;
				// startX -= dx / zoomDegree;
				// startY -= dy / zoomDegree;
				mode = 0;
				break;
			// 有手指离开屏幕，但屏幕还有触点(手指)
			case MotionEvent.ACTION_POINTER_UP:
				float[] f = new float[9];
				matrix.getValues(f);
				zoomDegree = f[0];
				mode = 0;
				break;
			// 当屏幕上已经有触点（手指）,再有一个手指压下屏幕
			case MotionEvent.ACTION_POINTER_DOWN:
				mode = ZOOM;
				startDis = distance(event);

				if (startDis > 10f) {// 避免手指上有两个茧
					midPoint = mid(event);
					currentMaritx.set(imageView.getImageMatrix());// 记录当前的缩放倍数
				}

				break;

			}
			imageView.setImageMatrix(matrix);
			return true;
		}
	}

	/**
	 * 两点之间的距离
	 * 
	 * @param event
	 * @return
	 */
	private static float distance(MotionEvent event) {
		// 两根线的距离
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		return FloatMath.sqrt(dx * dx + dy * dy);
	}

	/**
	 * 获得屏幕中心点
	 * 
	 * @param event
	 * @return
	 */
	private static PointF mid(MotionEvent event) {
		return new PointF(width / 2, height / 2);
	}

	private class MyView extends View {

		public MyView(Context context) {
			super(context);
		}



		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			height = getHeight();
			width = getWidth();
			line = width / 2;
			startX = width / 2 - line / 2;
			startY = height / 2 - line / 2;

			Paint p = new Paint();
			p.setColor(Color.RED);
			p.setStyle(Style.STROKE);
			p.setStrokeWidth(2);
			canvas.drawRect(width / 2 - line / 2, height / 2 - line / 2, width
					/ 2 + line / 2, height / 2 + line / 2, p);
			Paint p2 = new Paint();
			p2.setColor(Color.BLACK);
			p2.setAlpha(100);
			canvas.drawRect(0, 0, width / 2 - line / 2, height, p2);
			canvas.drawRect(width / 2 + line / 2, 0, width, height, p2);
			canvas.drawRect(width / 2 - line / 2, height / 2 + line / 2, width
					/ 2 + line / 2, height, p2);
			canvas.drawRect(width / 2 - line / 2, 0, width / 2 + line / 2,
					height / 2 - line / 2, p2);

			int h = img.getHeight();
			int w = img.getWidth();
			float zoom1 = (float) height / (float) h;
			float zoom2 = (float) width / (float) w;
			if (zoom1 < 1 || zoom2 < 1) {
				Matrix m = new Matrix();
				float size = zoom1 > zoom2 ? zoom1 : zoom2;
				m.setScale(size, size);

				img = Bitmap.createBitmap(img, 0, 0, img.getWidth(),
						img.getHeight(), m, true);
			}

			imageView.setImageBitmap(img);
		
		}

	}

	public static String getSDCardPath() {
		File sdcardDir = null;
		// 判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok:
			Bitmap bmp = img;
			
			imageView.setDrawingCacheEnabled(false);
			float[] f = new float[9];
			matrix.getValues(f);
			startX = (-f[2] / zoomDegree + ((float) (width - line) / 2)
					/ zoomDegree);
			startY = (-f[5] / zoomDegree + ((float) (height - line) / 2)
					/ zoomDegree);
			try {
				Bitmap bitmap = Bitmap.createBitmap(bmp, (int) startX,
						(int) startY, (int) (line / zoomDegree),
						(int) (line / zoomDegree), matrix, true);
				image2.setImageBitmap(bitmap);
				imageView.setVisibility(View.GONE);
				image2.setVisibility(View.VISIBLE);
				if (GetImageActivity.bORaORf.equals(add.FACE)) {
					MainActivity.myface.setImageBitmap(bitmap);
					// 保存Bitmap
					SavePath = getSDCardPath() + "/CarzyAlarm";
					path = new File(SavePath);
					// 文件
					filepathFace = SavePath + IMAGE_BIG;
					file = new File(filepathFace);
				} else if (GetImageActivity.bORaORf.equals(add.BEFORE)) {
					add.image.setImageBitmap(bitmap);
					// 保存Bitmap
					SavePath = getSDCardPath() + "/CarzyAlarm";
					path = new File(SavePath);
					// 文件
					filepathBefore = SavePath + IMAGE_BEFORE;
					file = new File(filepathBefore);
				} else if (GetImageActivity.bORaORf.equals(add.AFTER)) {
					// 保存Bitmap
					SavePath = getSDCardPath() + "/CarzyAlarm";
					path = new File(SavePath);
					// 文件
					filepathBefore = SavePath + IMAGE_AFTER;
					file = new File(filepathBefore);

				}
				if (!path.exists()) {
					path.mkdirs();
				}
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fos = null;
				fos = new FileOutputStream(file);
				if (null != fos) {
					bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
					fos.flush();
					fos.close();
				}
				if (img != null && !img.isRecycled()) {
					// 回收并且置为null
					img.recycle();
					System.out
							.println("回收22222！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！11");
					img = null;
				}
				System.gc();
				// 先判断是否已经回收
				if (bmp != null && !bmp.isRecycled()) {
					// 回收并且置为null
					bmp.recycle();
					System.out
							.println("回收！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！11");
					bmp = null;
				}

				System.gc();
				

			} catch (IllegalArgumentException e) {
				Toast.makeText(context, "请正确选择图片区域", Toast.LENGTH_LONG).show();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.finish();
			if (AlarmActivity.isTwoPictureSame == true) {
				startActivity(new Intent(FaceActivity.this, picturePD.class));
			}
			break;

		case R.id.cancel:
			this.finish();
			break;

		default:
			break;
		}

	}

}
