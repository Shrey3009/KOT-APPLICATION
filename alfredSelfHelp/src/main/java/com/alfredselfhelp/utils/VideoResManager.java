package com.alfredselfhelp.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.alfredselfhelp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class VideoResManager {
	public static Context mCtx;
	private List<String> mFilePaths;
	private int mCurrentFileIndex;

	private boolean mbExSdUsed;
	private String mExSdRoot;

	public static final String HD2_EXT = ".hd2";
	public VideoResManager(Context c)
	{
		mCtx  = c;	
		UpdateVideo();
	}
	public int AddFile(String filepath)
	{
		//LogFile.i("添加文件:"+filepath);
		mFilePaths.add(filepath);
		return mFilePaths.size();
	}
	
	public int AddDir(String path)
	{
		//LogFile.i("添加目录:"+path);
		Fill(path,mFilePaths);
		return mFilePaths.size();
	}
//	public static void DelFilesExcept(String dstpath,String   []Mediafiles)
//	{
//		List<String> existsmediafiles = new ArrayList<String>();
//
//		Fill(dstpath,existsmediafiles);
//
//		for(String filepath:existsmediafiles)
//		{
//			boolean preserved = false;//保留
//			for(String url:Mediafiles)
//			{
//				String filename = ShopInfo.extractFileName(url);
//				if(filepath.contains(filename))//服务器配置的
//				{
//					preserved = true;
//					break;
//				}
//			}
//			if(!preserved)
//			{
//				File file = new File(filepath);
//				if(!file.delete())//删除服务端删除的
//				{
//					LogFile.i("删除文件失败:"+filepath);
//				}
//			}
//		}
//	}
	public void UpdateVideo()
	{
		mCurrentFileIndex=0;
		mFilePaths = new ArrayList<String>();	
		String path = TvPref.readVideoFile();
		
		if(!checkExSD(path))//检测外置SD是否存在且有播放视频
		{			
			if(!path.isEmpty())
			{
				Fill(path,mFilePaths);
			}
		}
		
		Log.d("LXX","mFilePaths长度："+mFilePaths.size());
		//添加从服务器下载视频
//		String dstpath = ShopInfo.getVideoPath();
//		if(!dstpath.isEmpty()) {
//			LogFile.i("查询服务器配置视频:"+dstpath);
//			AddDir(dstpath);
//		}
//
		locateVideo();//从上一次播放的文件开始
	}

	public void clear()
	{
		mFilePaths.clear();
		mCurrentFileIndex=0;
		mbExSdUsed = false;		
	}

	public boolean isSDUsed()
	{
		return mbExSdUsed;
	}
	
	public void setSDUsed(boolean used)
	{
		mbExSdUsed = used;
	}
	
	static public int checkVideo(String path)
	{
		File dirf = new File(path);
		if(!path.isEmpty() && dirf.exists() && dirf.listFiles() != null){
			List<String> filelists = new ArrayList<String>();
			Fill(path,filelists);
			if(filelists.size() > 0)
    		{
				return 1;
    		}
			return 0;
		}
		
		return -1;
	}

	private boolean checkExSD(String path) {
		mbExSdUsed = false;
		mExSdRoot = TvPref.readExstoragePath();
		
		mExSdRoot = path.substring(0,path.lastIndexOf("/")+1);
		Log.d("LXX", "mExSdRoot = "+mExSdRoot);
		File dirf = new File(mExSdRoot);
		
		if (!mExSdRoot.isEmpty() && dirf.exists() && dirf.listFiles() != null) {
			/*if (path.contains(mExSdRoot)) {
				Fill(path, mFilePaths);// 指定播放外置SD卡中的
			} else {
				Fill(mExSdRoot, mFilePaths);// 没有指定，默认根目录
			}*/
			Fill(mExSdRoot, mFilePaths);
			if (mFilePaths.size() == 0) {
				Toast.makeText(mCtx, mCtx.getString(R.string.no_support_video_on_sdcard), Toast.LENGTH_LONG).show();
			} else {
				mbExSdUsed = true;
			}
		}
		return mbExSdUsed;
	}
	private void locateVideo()
	{
		String lastplaying = TvPref.readPlayingFile();
		if(!lastplaying.isEmpty())
		{
			for(int i=0;i<mFilePaths.size();i++){
				if(lastplaying.equalsIgnoreCase(mFilePaths.get(i))){		
					mCurrentFileIndex = i;
					break;
				}
			}
		}
	}

	public static void Fill(String dir,List<String> filelists) {
		File file = new File(dir);
		if (!file.exists()) {
			return;
		}

		if (file.isFile()) {
		//	Log.i(String.format("video dir:%s", dir));
			filelists.add(dir);// 选中一个文件
			return;
		}
		File[] subfiles = file.listFiles();
		if (subfiles == null) {
			return;
		}

		List<FileNameText> files = new ArrayList<FileNameText>();
		List<FileNameText> subdirs = new ArrayList<FileNameText>();
		List<FileNameText> dh2files = new ArrayList<FileNameText>();
		for (File currentFile : subfiles) {

			// 取得文件名
			String fileName = currentFile.getName().toLowerCase();
			if (currentFile.isDirectory()) {
				subdirs.add(new FileNameText(fileName));
			} else {
				if (currentFile.length() == 0) {
					continue;
				}
				if (!checkEndsWithInStringArray(fileName, mCtx.getResources()
						.getStringArray(R.array.fileEndingVideo))) {
					continue;
				}
				if (fileName.endsWith(HD2_EXT)) {
					dh2files.add(new FileNameText(fileName));
				} else {
					files.add(new FileNameText(fileName));
				}
			}
		}
		Collections.sort(dh2files);
		Collections.sort(files);
		Collections.sort(subdirs);

		// 处理dh2
		dh2Proc(file.getAbsolutePath(), dh2files, filelists);

		for (int i = 0; i < files.size(); i++) {
			String filename = files.get(i).getText();
			String path = file.getAbsolutePath() + File.separator + filename;
		//	LogFile.i(String.format("video file:%s", path));
			filelists.add(path);
		}
		for (int i = 0; i < subdirs.size(); i++) {
			String subdir = file.getAbsolutePath() + File.separator
					+ subdirs.get(i).getText();
			Fill(subdir, filelists);// 递归
		}

	}
	
	static void dh2Proc(String parent, List<FileNameText> files,
			List<String> filelists) {

		int base = 0x7fffffff;
		try {
			for (int i = 0; i < files.size(); i++) {
				String filename = files.get(i).getText();
				filename = filename.substring(0, filename.lastIndexOf("."));
				int num = Integer.parseInt(filename);
				if (num < base) {
					base = num;
				}
			}
		} catch (NumberFormatException e) {
			for (int i = 0; i < files.size(); i++) {
				String filename = files.get(i).getText();
				String path = parent + File.separator + filename;
				filelists.add(path);
			}
			return;
		}
		for (int i = 0; i < files.size(); i++) {
			String filename = String.format(Locale.US,"%d%s", i + base, HD2_EXT);
			String path = parent + File.separator + filename;
			File f = new File(path);
			if (f.exists()) {
				filelists.add(path);
			}
		}
	}
	
	// 通过文件名判断是什么类型的文件
	public static boolean checkEndsWithInStringArray(String checkItsEnd,
			String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (aEnd.equals(".*")||checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}

	public boolean hasVideo()
	{
		return mFilePaths.size() > 0;
	}
	public String getNextVideo()
	{
		if(mFilePaths.size() == 0)
		{
			return null;
		}
		
		if(mCurrentFileIndex >= mFilePaths.size())
		{
			mCurrentFileIndex = 0;
		}
		String playing = mFilePaths.get(mCurrentFileIndex); 
		mCurrentFileIndex++;
		
		TvPref.savePlayingFile(playing);
		return playing;
	}
	
	public void badFile(String filepath)
	{
		for(int i=0;i<mFilePaths.size();i++){
			if(filepath.equalsIgnoreCase(mFilePaths.get(i))){				
				mFilePaths.remove(i);
				mCurrentFileIndex--;
				break;
			}
		}
	}
	public static class FileNameText implements Comparable<FileNameText>
	{
		private String mFileName;
		public FileNameText(String filename)
		{
			mFileName = filename;
		}
		//得到文件名
		public String getText()
		{
			return mFileName;
		}
		@Override
		public int compareTo(FileNameText another) {
			if (this.mFileName != null)
				return this.mFileName.compareTo(another.getText());
			else
				throw new IllegalArgumentException();
		}
		
	}
}
