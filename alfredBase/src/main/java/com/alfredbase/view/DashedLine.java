package com.alfredbase.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

public class DashedLine extends View {


	public DashedLine(Context context) {
		super(context);
	}

	public DashedLine(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int width = getWidth();
		super.onDraw(canvas);
		Paint paint = new Paint();  
        paint.setStyle(Paint.Style.STROKE);  
        paint.setColor(Color.BLACK);  
        Path path = new Path();       
        path.moveTo(0, 2);  
        path.lineTo(width,2);        
        PathEffect effects = new DashPathEffect(new float[]{2,4,2,4},1);  
        paint.setPathEffect(effects);  
        canvas.drawPath(path, paint); 
	}
}
