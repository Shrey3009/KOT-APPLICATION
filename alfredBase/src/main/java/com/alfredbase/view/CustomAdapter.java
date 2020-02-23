package com.alfredbase.view;

import com.alfredbase.view.CustomListView.OnItemClickListener;
import com.alfredbase.view.CustomListView.OnItemLongClickListener;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

/**
 * 
 * @author
 * @time
 * @tags 自动换行布局Adapter
 * @todo FROM网络下载
 */
public class CustomAdapter{

    private String TAG = "CustomAdapter";
    private View myView;
    private ViewGroup myViewGroup;
    private CustomListView myCustomListView;
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public int getCount() {
        return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        return null;
    }

    /**
     * 
     * Add all the View controls to the custom SexangleViewList
     * When you use this SexangleViewList should be instantiated first and then call
     * Because here is not intercept and throw such as null pointer exception
     * The name is called mySexangleView View passed in must be empty
     * Of course the ViewGroup transfer time must also be empty
     * 
     */
    private final void getAllViewAddSexangle(){
        this.myCustomListView.removeAllViews();
        for(int i = 0 ; i < getCount() ; i++){
            View viewItem = getView(i, this.myView, this.myViewGroup);
            this.myCustomListView.addView(viewItem,i);
        }
    }

    /**
     * 
     * The refresh SexangleListView interface
     * Here is set to True representative will execute reset CustomListView twice
     * This method is called before, please first instantiation mySexangleListView
     * Otherwise, this method in redraw CustomListView abnormal happens
     * 
     */
    public void notifyDataSetChanged(){
        CustomListView.setAddChildType(true);
        notifyCustomListView(this.myCustomListView);
    }

    /**
     * Redraw the Custom controls for the first time, you should invoke this method
     * In order to ensure that each load data do not repeat to get rid of the
     * custom of the ListView all View objects
     * The following will be set up to monitor events as controls 
     * First load regardless whether OnItemClickListener and OnItemLongClickListener is NULL, 
     * they do not influence events Settings
     * 
     * @param formateList
     */
    public void notifyCustomListView(CustomListView formateList){
        this.myCustomListView = formateList;
        myCustomListView.removeAllViews();
        getAllViewAddSexangle();
        setOnItemClickListener(listener);
        setOnItemLongClickListener(longListener);
    }

    /**
     * Set the click event of each View, external can realize the interface for your call
     * 
     * @param listener
     */
    public void setOnItemClickListener(final OnItemClickListener listener){
    	if (listener == null) {
			return;
		}
        this.listener = listener;
        for(int i = 0; i < myCustomListView.getChildCount(); i++){
            final int parame = i;
            View view = myCustomListView.getChildAt(i);
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Adapter��ǰItem��ֵ : "+parame);
                    listener.onItemClick(null, v, parame, getCount());
                } 
            });
        }
    }

    /**
     * Set each long press event, the View outside can realize the interface for your call
     * 
     * @param listener
     */
    public void setOnItemLongClickListener(final OnItemLongClickListener listener){
    	if (listener == null) {
			return;
		}
        this.longListener = listener;
        for(int i = 0; i < myCustomListView.getChildCount(); i++){
            final int parame = i;
            View view = myCustomListView.getChildAt(i);
            view.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(null, v, parame, getCount());
                    return true;
                }
            });
        }       
    }

}