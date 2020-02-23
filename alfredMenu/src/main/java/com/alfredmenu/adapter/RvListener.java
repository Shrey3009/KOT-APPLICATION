package com.alfredmenu.adapter;

import android.view.View;

import com.alfredbase.javabean.ItemDetail;

/**
 * Created by fatchao
 * 日期  2017/3/10.
 * 邮箱  fat_chao@163.com
 */

//RecyclerView的item点击事件
public interface RvListener {

    void onItemClick(View id, int position);
}
