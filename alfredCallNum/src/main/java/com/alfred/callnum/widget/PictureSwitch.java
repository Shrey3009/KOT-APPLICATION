package com.alfred.callnum.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.alfred.callnum.R;
import com.alfredbase.utils.LogUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PictureSwitch extends ImageSwitcher {
	static Context mContext;
	private Handler mAutoSlideHdr = new Handler();
	private long lPrdMills = 4000;

	public PictureSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	/**
	 * 设置图片目录
	 * 
	 * @param dir
	 *            : 图片目录
	 */
	private List<String> mFilePaths;

	// 通过文件名判断是什么类型的文件
	public static boolean checkEndsWithInStringArray(String filename,
			String[] fileEndings) {
		String filename_low = filename.toLowerCase();
		for (String aEnd : fileEndings) {
			if (aEnd.equals(".*") || filename_low.endsWith(aEnd))
				return true;
		}
		return false;
	}

	public static void Fill(String dir, List<String> filePaths) {
		File file = new File(dir);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			dir = file.getParent();
			file = new File(dir);
		}

		File[] subfiles = file.listFiles();
		if (subfiles == null) {
			return;
		}

		List<String> files = new ArrayList<String>();
		List<String> subdirs = new ArrayList<String>();
		for (File currentFile : subfiles) {

			// 取得文件名
			String fileName = currentFile.getName();
			if (currentFile.isDirectory()) {
				subdirs.add(fileName);
			} else {
				if (currentFile.length() == 0) {
					continue;
				}
				if (!checkEndsWithInStringArray(fileName, mContext
						.getResources().getStringArray(R.array.fileEndingImage))) {
					continue;
				}
				files.add(fileName);

			}
		}
		for (int i = 0; i < files.size(); i++) {
			String filename = files.get(i);
			String path = file.getAbsolutePath() + File.separator + filename;
			if (canbedecode(path)) {
				filePaths.add(path);
			} else {
			}
		}
		for (int i = 0; i < subdirs.size(); i++) {
			String subdir = file.getAbsolutePath() + File.separator
					+ subdirs.get(i);
			Fill(subdir, filePaths);// 递归
		}

	}

	static boolean mbLossMem = false;

	public int setImageDir(String dir, boolean bloss) {
		if (!mounted()) {
			return -1;
		}
		mbLossMem = bloss;
		mFilePaths = new ArrayList<String>();
		Fill(dir, mFilePaths);

		return mFilePaths.size();
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		setFactory(viewFactory);
	}

	ViewFactory viewFactory = new ViewFactory() {

		@Override
		public View makeView() {
			// 初始化一个ImageView对象
			ImageView imageView = new ImageView(getContext());
			// 设置保持纵横比居中缩放图像
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			// 设置imageView的宽高
			imageView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			return imageView;
		}
	};

	private Bitmap calcBitmap(String filepath) {

		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// true: 不分配空间但可以计算Options.outWidth和Options.outHeight
		newOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, newOpts);// 此时返回bm为空
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		if (w > MAX_WIDTH) {
			newOpts.outWidth = MAX_WIDTH;
			newOpts.outHeight = h * MAX_WIDTH / w;// 缩小
		} else if (h > MAX_HEIGHT) {
			newOpts.outHeight = MAX_HEIGHT;
			newOpts.outWidth = w * MAX_HEIGHT / h;// 缩小
		}
		newOpts.inJustDecodeBounds = false;
		if (mbLossMem) {
			newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
		}

		Bitmap bmp = BitmapFactory.decodeFile(filepath, newOpts);
		return bmp;
	}

	private void readBitmap() {
		if (mFilePaths == null || mFilePaths.size() == 0) {
			return;
		}
		if (count >= mFilePaths.size()) {
			count = 0;
		}

		Glide.with(getContext())
		.load(mFilePaths.get(count))
		.into(new SimpleTarget<GlideDrawable>() {
					@Override
					public void onResourceReady(GlideDrawable drawable,
							GlideAnimation<? super GlideDrawable> arg1) {
						// TODO Auto-generated method stub
						setImageDrawable(drawable);
					}
				});
		count++;
		mAutoSlideHdr.postDelayed(mAutoSlideRun, lPrdMills);
	}

	int count = 0;
	boolean isPlaying = false;
	private Runnable mAutoSlideRun = new Runnable() {
		public void run() {
			readBitmap();
		}
	};

	public void startPlay() {
		isPlaying = true;
		mAutoSlideHdr.postDelayed(mAutoSlideRun, 0);
	}

	public void stopPlay() {
		if (mAutoSlideHdr != null) {
			mAutoSlideHdr.removeCallbacks(mAutoSlideRun);
			isPlaying = false;
		}
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	final int MAX_WIDTH = 800;
	final int MAX_HEIGHT = 600;

	static boolean canbedecode(String filepath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// true: 不分配空间但可以计算Options.outWidth和Options.outHeight
		newOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, newOpts);// 此时返回bm为空

		if (mbLossMem && (newOpts.outWidth > 4000 || newOpts.outHeight > 4000)) {
			LogUtil.d("LXX",String.format(Locale.US,"%s,w=%d,h=%d,too big!", filepath, newOpts.outWidth, newOpts.outHeight));
			return false;
		}

		return true;
	}

	/**
	 * SD卡装载
	 */
	private boolean mounted() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
}
