package com.util.weight;

import com.util.tool.UiUtil;
import com.util.tool.Utility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class XieLineView extends View {

	private Point one;
	private Point two;
	private Point three;
	private Context context;

	public XieLineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initPoint();
	}

	public XieLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initPoint();
	}

	public XieLineView(Context context) {
		super(context);
		this.context = context;
		initPoint();
	}

	private void initPoint() {
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(Color.GRAY);
		p.setStyle(Paint.Style.STROKE);
		int dip2px = UiUtil.dip2px(context, 10);
		p.setStrokeWidth(dip2px);

		Path path = new Path();
		if(one!=null && two!=null && three!=null){
			path.moveTo(one.x,one.y);
			path.lineTo(two.x, two.y);
			path.lineTo(three.x, three.y);
		}
		canvas.drawPath(path, p);
		// canvas.drawLine(60, 40, 100, 40, p);// 画线
		// canvas.drawLine(110, 40, 190, 80, p);// 斜线
	}
	
	public void setPoint(Point one,Point two,Point three){
		this.one = one;
		this.two = two;
		this.three = three;
		invalidate();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		
		
	}
}
