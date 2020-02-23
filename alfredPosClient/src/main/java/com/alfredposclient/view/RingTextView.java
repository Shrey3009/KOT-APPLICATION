package com.alfredposclient.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.alfredposclient.R;

public class RingTextView extends TextView {

	private  Paint paint;  
    private final Context context;  
    private Style style;
    private int color;
    private String text; 
    private int textColor;
    private Typeface trajanProRegular;
    public RingTextView(Context context) {  
        this(context, null);  
    }  
    
    public RingTextView(Context context, AttributeSet attrs) {  
       super(context, attrs);  
       this.context = context;  
       init();
    }  
     
    private void init() {
    	 this.paint = new Paint();  
         this.paint.setAntiAlias(true); //消除锯齿
         color = context.getResources().getColor(R.color.height_gray);
         textColor = context.getResources().getColor(android.R.color.black);
         style = Paint.Style.STROKE;
         text = "?";
         trajanProRegular = Typeface.createFromAsset(context.getAssets(),
					"fonts/TrajanProRegular.otf");
	}
    
    @SuppressLint("ResourceAsColor")
	@Override  
    protected void onDraw(Canvas canvas) { 
    	super.onDraw(canvas);
        int center = getWidth()/2;
        int innerCircle = center - 4; //设置内圆半径
        //绘制内圆  
        this.paint.setStyle(style); //绘制空心圆
        this.paint.setColor(color);
        this.paint.setStrokeWidth(2);
        canvas.drawCircle(center,center, innerCircle, this.paint);
      //绘制文字
        this.paint.setColor(textColor);
        this.paint.setTextSize(center);
        this.paint.setStyle(Paint.Style.FILL);	// 实心字
        this.paint.setTextAlign(Align.CENTER);
        this.paint.setTypeface(trajanProRegular);
        FontMetrics fontMetrics = this.paint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float offY = fontTotalHeight / 2 - fontMetrics.bottom;
        canvas.drawText(text, center,center + offY, paint);
        
    }  
    
    public void setCircleColor(int color, int index) {
    	this.color = color;
    	this.textColor = context.getResources().getColor(android.R.color.white);
    	this.style = Paint.Style.FILL_AND_STROKE;
    	text = String.valueOf(index);
    	postInvalidate();
    }
    
    public void restoreCircleColor() {
    	color = context.getResources().getColor(R.color.height_gray);
        textColor = context.getResources().getColor(android.R.color.black);
        style = Paint.Style.STROKE;
        text = "?";
    	postInvalidate();
    }
    
    public void setDoneCircleColor(int index) {
    	color = context.getResources().getColor(R.color.height_gray);
        textColor = context.getResources().getColor(android.R.color.black);
        style = Paint.Style.STROKE;
        text = String.valueOf(index);
    	postInvalidate();
    }
 
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
}
