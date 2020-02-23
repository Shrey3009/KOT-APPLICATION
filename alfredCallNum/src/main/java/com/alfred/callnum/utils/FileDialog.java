package com.alfred.callnum.utils;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alfred.callnum.R;
import com.alfred.callnum.adapter.IconifiedText;
import com.alfred.callnum.adapter.IconifiedTextListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileDialog extends ListActivity {
    private List<IconifiedText> directoryEntries = new ArrayList<IconifiedText>();
    private File currentDirectory = null;

    public static final int RESULT_SELECT_FILE = 0x100;
    public static final String RESULT_SELECT_FILEPATH = "selected_file";


    public static final String INP_FILE_FILTER = "file_filters";
    public static final String INP_CURRENT_PATH = "current_path";

    private String[] mFileFilter;

    public static final String ROOT_DEFAULT = "/mnt";

    /**
     * Called when the com.alfredselfhelp.activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mFileFilter = getIntent().getStringArrayExtra(INP_FILE_FILTER);
        if (mFileFilter == null) {
            mFileFilter = new String[]{".*"};
        }
        String curpath = getIntent().getStringExtra(INP_CURRENT_PATH);
        File file = new File(curpath);
        if (curpath == null || !file.exists()) {
            curpath = ROOT_DEFAULT;
        } else if (!file.isDirectory()) {
            curpath = file.getParent();
        }
        File curfile = new File(curpath);
        browseTo(curfile);

        this.setSelection(0);
        //长按选择整个目录所在文件
        this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {

                String pathname = currentDirectory.getAbsolutePath() + File.separator
                        + directoryEntries.get(position).getText();
                onFinish(pathname);

                return false;
            }

        });
    }

    // 浏览文件系统的根目录
    private void browseToRoot() {
        browseTo(new File(ROOT_DEFAULT));
    }

    // 返回上一级目录
    private void upOneLevel() {
        String path = currentDirectory.getAbsolutePath();
        if (path.equals(ROOT_DEFAULT)
                || path.equals("/")) {
            onFinish(null);
        } else if (this.currentDirectory.getParent() != null) {
            this.browseTo(this.currentDirectory.getParentFile());
        }
    }

    // 浏览指定的目录,如果是文件则进行打开操作
    private void browseTo(File file) {
        this.setTitle(file.getAbsolutePath());
        if (file.isDirectory()) {
            this.currentDirectory = file;
            fill(file.listFiles());
        } else {
            onFinish(file.getAbsolutePath());
        }
    }

    // 这里可以理解为设置ListActivity的源
    private void fill(File[] files) {
        if (files == null) {
            Toast.makeText(this, this.getString(R.string.access_denied), Toast.LENGTH_SHORT).show();
            this.browseTo(this.currentDirectory.getParentFile());
            return;
        }
        // 清空列表
        this.directoryEntries.clear();

        // 如果不是根目录则添加上一级目录项
        String path = currentDirectory.getAbsolutePath();
        if (path.equals(ROOT_DEFAULT)
                || path.equals("/")) {
            this.directoryEntries.add(new IconifiedText(
                    getString(R.string.exit_filed), getResources()
                    .getDrawable(R.drawable.logo_icon)));
        } else {
            this.directoryEntries.add(new IconifiedText(
                    getString(R.string.up_one_level), getResources()
                    .getDrawable(R.drawable.logo_icon)));
        }

        Drawable currentIcon = null;
        for (File currentFile : files) {

            // 取得文件名
            String fileName = currentFile.getName();

            // 判断是一个文件夹还是一个文件
            if (currentFile.isDirectory()) {
                currentIcon = getResources().getDrawable(R.drawable.logo_icon);
            } else {
                if (currentFile.length() == 0) {
                    continue;
                }
                if (!checkEndsWithInStringArray(fileName, mFileFilter)) {
                    continue;
                }
                // 根据文件名来判断文件类型，设置不同的图标
                if (checkEndsWithInStringArray(fileName, getResources()
                        .getStringArray(R.array.fileEndingImage))) {
                    currentIcon = getResources().getDrawable(R.drawable.logo_icon);
                } else if (checkEndsWithInStringArray(fileName, getResources()
                        .getStringArray(R.array.fileEndingWebText))) {
                    currentIcon = getResources()
                            .getDrawable(R.drawable.logo_icon);
                } else if (checkEndsWithInStringArray(fileName, getResources()
                        .getStringArray(R.array.fileEndingPackage))) {
                    currentIcon = getResources().getDrawable(R.drawable.logo_icon);
                } else if (checkEndsWithInStringArray(fileName, getResources()
                        .getStringArray(R.array.fileEndingAudio))) {
                    currentIcon = getResources().getDrawable(R.drawable.logo_icon);
                } else if (checkEndsWithInStringArray(fileName, getResources()
                        .getStringArray(R.array.fileEndingVideo))) {
                    currentIcon = getResources().getDrawable(R.drawable.logo_icon);
                } else {
                    currentIcon = getResources().getDrawable(R.drawable.logo_icon);
                }
            }

            this.directoryEntries.add(new IconifiedText(fileName,
                    currentIcon));
        }
        Collections.sort(this.directoryEntries);
        IconifiedTextListAdapter itla = new IconifiedTextListAdapter(this);
        // 将表设置到ListAdapter中
        itla.setListItems(this.directoryEntries);
        // 为ListActivity添加一个ListAdapter
        this.setListAdapter(itla);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // 取得选中的一项的文件名
        String selectedFileString = this.directoryEntries.get(position)
                .getText();

        if (selectedFileString.equals(getString(R.string.up_one_level))
                || selectedFileString.equals(getString(R.string.exit_filed))) {
            // 返回上一级目录
            this.upOneLevel();
        } else {

            File clickedFile = null;
            clickedFile = new File(this.currentDirectory.getAbsolutePath() + File.separator
                    + this.directoryEntries.get(position).getText());
            if (clickedFile != null)
                this.browseTo(clickedFile);
        }
    }

    // 通过文件名判断是什么类型的文件
    private boolean checkEndsWithInStringArray(String filename,
                                               String[] fileEndings) {
        String filename_low = filename.toLowerCase();
        for (String aEnd : fileEndings) {
            if (aEnd.equals(".*") || filename_low.endsWith(aEnd))
                return true;
        }
        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            // 返回上一级目录
            this.upOneLevel();

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    // 得到当前目录的绝对路劲
    public String GetCurDirectory() {
        return this.currentDirectory.getAbsolutePath();
    }

    // 移动文件
    public void moveFile(String source, String destination) {
        new File(source).renameTo(new File(destination));
    }

    private void onFinish(String filepath) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_SELECT_FILEPATH, filepath);
        setResult(RESULT_SELECT_FILE, intent);
        this.finish();
    }
}
