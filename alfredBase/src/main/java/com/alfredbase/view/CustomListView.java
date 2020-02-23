package com.alfredbase.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
/**
 * 
 * @author
 * @time
 * @tags 自动换行布局
 * @todo FROM网上下载
 */
public class CustomListView extends RelativeLayout {

	    private String TAG = "CustomListView";
	    private CustomAdapter myCustomAdapter;

	    public CustomListView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }

	    @Override
	    protected void onLayout(boolean arg0, int argLeft, int argTop, int argRight, int argBottom) {
	        Log.i(TAG, "L:"+argLeft+" T:"+argTop+" R:"+argRight+" B:"+argBottom);
	        final int count = getChildCount();
	        int row=0;
	        int lengthX=0; 
	        int lengthY=0; 
	        for(int i=0;i<count;i++){

	            final View child = this.getChildAt(i);
	            int width = child.getMeasuredWidth();
	            int height =  child.getMeasuredHeight();

	            if(lengthX == 0){
	                lengthX += width;
	            }else{
	                lengthX += (width+getDividerWidth());
	            }

	            if((i == 0 && lengthX<=argRight)){
	                lengthY += height;
	            }

	            if(lengthX>argRight){
	                lengthX = width;
	                lengthY += getDividerHeight()+height;
	                row++;
	                child.layout(lengthX-width, lengthY-height, lengthX, lengthY);
	            }else{
	                child.layout(lengthX-width, lengthY-height, lengthX, lengthY);
	            }
	        }
//	        android.view.ViewGroup.LayoutParams lp = CustomListView.this.getLayoutParams();
//	        lp.height = lengthY;
//	        CustomListView.this.setLayoutParams(lp);
	        if(isAddChildType()){
	            new Thread(new RefreshCustomThread()).start();
	        }
	    }

	    @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

	        int width = MeasureSpec.getSize(widthMeasureSpec);
	        int height = MeasureSpec.getSize(heightMeasureSpec);
	        setMeasuredDimension(width, height);

	        for (int i = 0; i < getChildCount(); i++) {
	            View child = getChildAt(i);
	            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
	        }
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    }

	    private static boolean addChildType;

	    final static boolean isAddChildType() {
	        return addChildType;
	    }

	    public static void setAddChildType(boolean addChildType) {
	        CustomListView.addChildType = addChildType;
	    }

	    private int dividerHeight = 0;

	    final int getDividerHeight() {
	        return dividerHeight;
	    }

	    public void setDividerHeight(int dividerHeight) {
	        this.dividerHeight = dividerHeight;
	    }

	    private int dividerWidth = 0;

	    final int getDividerWidth() {
	        return dividerWidth;
	    }

	    public void setDividerWidth(int dividerWidth) {
	        this.dividerWidth = dividerWidth;
	    }

	    public void setAdapter(CustomAdapter adapter){
	        this.myCustomAdapter = adapter;
	        setAddChildType(true);
	        adapter.notifyCustomListView(CustomListView.this);
	    }

	    /**
	     * 
	     * @param listener
	     */
	    public void setOnItemClickListener(OnItemClickListener listener){
	        myCustomAdapter.setOnItemClickListener(listener);
	    }

	    /**
	     * Corresponding Item long click event
	     * 
	     * @param listener
	     */
	    public void setOnItemLongClickListener(OnItemLongClickListener listener){
	        myCustomAdapter.setOnItemLongClickListener(listener);
	    }

	    private final Handler handler = new Handler(Looper.getMainLooper()){

	        @Override
	        public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	            try{
	                if(msg.getData().containsKey("getRefreshThreadHandler")){
	                    setAddChildType(false);
	                    myCustomAdapter.notifyCustomListView(CustomListView.this);
	                }
	            }catch(Exception e){
	                Log.w(TAG, e);
	            }
	        }

	    };

	    private final class RefreshCustomThread implements Runnable{

	        @Override
	        public void run() {
	            Bundle b = new Bundle();
	            try{
	                Thread.sleep(50);
	            }catch(Exception e){

	            }finally{
	                b.putBoolean("getRefreshThreadHandler", true);
	                sendMsgHanlder(handler, b);
	            }
	        }
	    } 

	    private final void sendMsgHanlder(Handler handler,Bundle data){
	        Message msg = handler.obtainMessage();
	        msg.setData(data);
	        handler.sendMessage(msg);
	    }
	    
	    public interface OnItemClickListener {
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3);
	    }
	    
	    public interface OnItemLongClickListener {
	        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3);
	    }

	}
