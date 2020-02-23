package com.alfredmenu.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.alfredmenu.R;

public class MyToggleButton extends View{
    private Bitmap slideon;//滑块的背景图
    private Bitmap slideoff;//滑块的背景图
    private Bitmap switchOn;//滑动开关开的背景图
    private Bitmap switchOff;//滑动开关关的背景图

    private Bitmap switchOnBkg; // 开关开启时的背景
    private Bitmap switchOffBkg; // 开关关闭时的背景
    private Bitmap slipSwitchButton; // 滑动开关的图片
    private Bitmap slipSwitchButtonOff; // 滑动开关的图片
    private boolean isSlipping = false; // 是否正在滑动
    private boolean isSwitchOn ; // 当前开关的状态，true表示开启，flase表示关闭

    private Rect onRect; // 左半边矩形
    private Rect offRect; // 右半边矩形

    private Boolean state;
  //  private ToggleState state = ToggleState.OPEN;//滑动开关的状态，默认为OPEN；
    //手指触摸在view上的坐标，这个是相对于view的坐标
    private int currentX;
  //  private float currentX; // 当前的水平坐标X
    //是否在滑动
    private boolean isSliding = true;

    /**
     * 如果自定的控件需要在Java中实例化，重写该构造方法
     *
     * @param context
     */
    public MyToggleButton(Context context) {
        super(context);
        init();
    }

    /**
     * 如果自定义控件需要在xml文件使用，重写该构造方法
     *
     * @param context
     * @param attrs
     */
    public MyToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 设置滑块的背景图片
     *
     * @param
     */


    public void init(){
        //载入图片资源
        switchOn = BitmapFactory.decodeResource(getResources(),R.drawable.bg_slip_ons);
        switchOff = BitmapFactory.decodeResource(getResources(), R.drawable.bg_slip_offs);
        slideon = BitmapFactory.decodeResource(getResources(), R.drawable.slip_ons);
        slideoff = BitmapFactory.decodeResource(getResources(), R.drawable.slip_offs);
//        this.setOnTouchListener(this); // 设置触摸监听器
//        switchOnBkg = BitmapFactory.decodeResource(this.getResources(),
//                R.drawable.bg_slip_ons);
//        switchOffBkg = BitmapFactory.decodeResource(this.getResources(),
//                R.drawable.bg_slip_offs);
//        slipSwitchButton = BitmapFactory.decodeResource(this.getResources(),
//                R.drawable.slip_ons);
//
//        slipSwitchButtonOff = BitmapFactory.decodeResource(this.getResources(),
//                R.drawable.slip_offs);

//        // 右半边rect，滑动开关在右半边时表示开启
//        onRect = new Rect(switchOnBkg.getWidth() - slipSwitchButton.getWidth(),
//                0, switchOnBkg.getWidth(), slipSwitchButton.getHeight());
//        // 左半边rect，滑动开关在左半边时表示关闭
//        offRect = new Rect(0, 0, slipSwitchButton.getWidth(),
//                slipSwitchButton.getHeight());

        //setOnTouchListener(this);
    }//
//    public void setSildeBackgroundResource(int slide_bg) {
//        slideon = BitmapFactory.decodeResource(getResources(), slide_bg);
//    }
//
//    public void setSildeBackgroundoffResource(int slide_bg) {
//        slideoff = BitmapFactory.decodeResource(getResources(), slide_bg);
//    }
//
//    /**
//     * 设置滑动开关开的背景图
//     *
//     * @param switch_on
//     */
//    public void setSwitchOnBackgroundResource(int switch_on) {
//        switchOn = BitmapFactory.decodeResource(getResources(), switch_on);
//    }
//
//    /**
//     * 设置滑动开关关的背景图
//     *
//     * @param switch_off
//     */
//    public void setSwitchOffBackgroundResource(int switch_off) {
//        switchOff = BitmapFactory.decodeResource(getResources(), switch_off);
//    }

//    public void setToggleState(ToggleState toggleState) {
//        this.state = toggleState;
//    }

    public enum ToggleState {
        OPEN, CLOSE
    }

    public void setChecked(boolean checked){

        this.state=checked;
        this.invalidate();
//        if(checked){
//            nowX = bg_off.getWidth();
//        }else{
//            nowX = 0;
//        }
//        nowStatus = checked;
    }
    /**
     * 绘制view的宽与高，以滑动开关的宽高绘制
     *
     *
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(switchOn.getWidth(), switchOn.getHeight());
    }
//
//    /**
//     * 画view
//     *
//     * @param canvas
//     */
//    @Override
    protected void onDraw(Canvas canvas) {
        int left =  currentX - slideon.getWidth() / 2;
        // (left > (switchOn.getWidth() / 2)) ? ((state = ToggleState.OPEN)) : ((state = ToggleState.CLOSE));
        //当触摸点超过整view的一半时，就切换开关背景；

        super.onDraw(canvas);
        /**
         * (@NonNull Bitmap bitmap, float left, float top, @Nullable Paint paint)
         * left:图片左边的x坐标
         * top：图片顶部的y坐标
         */
        if (isSliding) {
            if (state) {
                //绘制开关图片
                canvas.drawBitmap(switchOn, 0, 0, null);
                //绘制滑块图片
             //   if (left > (switchOn.getWidth() - slideon.getWidth())) {
                //if (left > (switchOn.getWidth() - slideon.getWidth()))
                    left = switchOn.getWidth() - slideon.getWidth();
                //left = switchOn.getWidth() - slideon.getWidth();
                    canvas.drawBitmap(slideon, left, 0, null);

            } else {
                //绘制开关图片
                canvas.drawBitmap(switchOff, 0, 0, null);
                //绘制滑块图片
                if (left < 0) left = 0;
                canvas.drawBitmap(slideoff, left, 0, null);
            }
        } else {
            if (state) {
                //绘制开关图片
                canvas.drawBitmap(switchOn, 0, 0, null);
//                left = switchOn.getWidth() - slideon.getWidth();
                canvas.drawBitmap(slideon, switchOn.getWidth() - slideon.getWidth(), 0, null);
            } else {
                //绘制开关图片
                canvas.drawBitmap(switchOff, 0, 0, null);

                canvas.drawBitmap(slideoff, 0, 0, null);
            }
        }

    }





    /**
     * 定义滑动
     *
     * @param event
     * @return
     */
//    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currentX = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isSliding = true;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isSliding = false;
                break;
        }
        //在滑动的过程中状态改变也进行监听，触摸结束后也监听状态改变，未改变就无需其他动作
        if (currentX > slideon.getWidth()) {
            if (!state) {
                state =true;
                if (stateChangeListeren != null) {
                    stateChangeListeren.onToggleStateChangeListeren(MyToggleButton.this,state);
                }
            }
        } else {
            //CLOSE
            if (state ) {
                state = false;
                if (stateChangeListeren != null) {
                    stateChangeListeren.onToggleStateChangeListeren(MyToggleButton.this,state);
                }
            }
        }
        //调用此方法，间接调用OnD
        invalidate();
        return true;
    }




//    public boolean onTouch(View v, MotionEvent event) {
//        int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_MOVE:
//                currentX = event.getX();
//                break;
//            case MotionEvent.ACTION_DOWN:
//                isSlipping = true;
//                break;
//            case MotionEvent.ACTION_UP:
//                isSlipping = false;
//               boolean previousState = isSwitchOn;
//
//
//                if(previousState != isSwitchOn){
//                    if (stateChangeListeren != null) {
//                        stateChangeListeren.onToggleStateChangeListeren(MyToggleButton.this, isSwitchOn);
//                    }
//                    }
//
//                break;


//            if (event.getX() > (switchOnBkg.getWidth() / 2)) {
//                if (!isSwitchOn) {
//                    isSwitchOn = true;
//                    if (stateChangeListeren != null) {
//                        stateChangeListeren.onToggleStateChangeListeren(MyToggleButton.this, isSwitchOn);
//                    }
//                }
//            } else {
//                if (isSwitchOn) {
//                    isSwitchOn = false;
//                    if (stateChangeListeren != null) {
//                        stateChangeListeren.onToggleStateChangeListeren(MyToggleButton.this, isSwitchOn);
//                    }
//                }
//            }
//            default:
//                break;
    //    }



//        if (event.getX() > (switchOnBkg.getWidth() / 2)) {
//                if (!isSwitchOn) {
//                    isSwitchOn = true;
//                    if (stateChangeListeren != null) {
//                        stateChangeListeren.onToggleStateChangeListeren(MyToggleButton.this, isSwitchOn);
//                    }
//                }
//            } else {
//                if (isSwitchOn) {
//                    isSwitchOn = false;
//                    if (stateChangeListeren != null) {
//                        stateChangeListeren.onToggleStateChangeListeren(MyToggleButton.this, isSwitchOn);
//                    }
//                }
////            }
//        this.invalidate();
//        return true;
//    }

    /**
     * 开放状态改变接口
     */
    private OnToggleStateChangeListeren stateChangeListeren;

    public void setOnStateChangeListeren(OnToggleStateChangeListeren stateChangeListeren) {
        this.stateChangeListeren = stateChangeListeren;
    }

    public interface OnToggleStateChangeListeren {
        void onToggleStateChangeListeren(MyToggleButton Mybutton, Boolean checkState);
    }
}