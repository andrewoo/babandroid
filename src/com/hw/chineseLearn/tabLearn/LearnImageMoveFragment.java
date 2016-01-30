package com.hw.chineseLearn.tabLearn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.LearnUnitAdapter;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.base.CustomApplication;
import com.util.thread.ThreadWithDialogTask;

/**
 * 学习-拼汉子
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LearnImageMoveFragment extends BaseFragment implements
		OnClickListener {
	private View contentView;// 主view

	private ThreadWithDialogTask task;
	public static LearnImageMoveFragment fragment;
	Context context;
	LearnUnitAdapter learnUnit1Adapter, learnUnit2Adapter;

	private ImageView iv_dv_view;
	private TextView tv_drag_view;
	private int startx;
	private int starty;
	private SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fragment = this;
		context = getActivity();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_lesson_image_move,
				null);

		task = new ThreadWithDialogTask();

		iv_dv_view = (ImageView) contentView.findViewById(R.id.iv_dv_view);
		tv_drag_view = (TextView) contentView.findViewById(R.id.tv_drag_view);
		sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
		iv_dv_view.setOnTouchListener(onTouchListener);

		return contentView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();
		int x = sp.getInt("lastx", 0);
		int y = sp.getInt("lasty", 0);
		// iv_dv_view.layout(iv_dv_view.getLeft() + x, iv_dv_view.getTop() + y,
		// iv_dv_view.getRight() + x, iv_dv_view.getBottom() + y);
		// iv_dv_view.invalidate();//界面重新渲染

		LayoutParams params = (LayoutParams) iv_dv_view.getLayoutParams();
		params.leftMargin = x;
		params.topMargin = y;
		iv_dv_view.setLayoutParams(params);
	}

	OnTouchListener onTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			// 如果手指放在imageView上拖动
			case R.id.iv_dv_view:
				// event.getRawX(); //获取手指第一次接触屏幕在x方向的坐标
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 获取手指第一次接触屏幕
					startx = (int) event.getRawX();
					starty = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动对应的事件
					int x = (int) event.getRawX();
					int y = (int) event.getRawY();

					if (y < 400) {
						// 设置TextView在窗体的下面
						tv_drag_view.layout(tv_drag_view.getLeft(), 420,
								tv_drag_view.getRight(), 440);
					} else {
						tv_drag_view.layout(tv_drag_view.getLeft(), 60,
								tv_drag_view.getRight(), 80);
					}

					// 获取手指移动的距离
					int dx = x - startx;
					int dy = y - starty;
					// 得到imageView最开始的各顶点的坐标
					int l = iv_dv_view.getLeft();
					int r = iv_dv_view.getRight();
					int t = iv_dv_view.getTop();
					int b = iv_dv_view.getBottom();

					int newl = l + dx;
					int newt = t + dy;
					int newr = r + dx;
					int newb = b + dy;
					// 更改imageView在窗体的位置
					iv_dv_view.layout(newl, newt, newr, newb);

					// 获取移动后的位置
					startx = (int) event.getRawX();
					starty = (int) event.getRawY();

					int screenWidth = CustomApplication.app.displayMetrics.widthPixels;
					int screenHeight = CustomApplication.app.displayMetrics.heightPixels;
					
					if ((newl < 20) || (newt < 20) || (newr > screenWidth/2)
							|| (newb > screenHeight/2)) {
						break;
					}

					break;
				case MotionEvent.ACTION_UP:// 手指离开屏幕对应事件
					// Log.i(TAG, "手指离开屏幕");
					// 记录最后图片在窗体的位置
					int lasty = iv_dv_view.getTop();
					int lastx = iv_dv_view.getLeft();
					Editor editor = sp.edit();
					editor.putInt("lasty", lasty);
					editor.putInt("lastx", lastx);
					editor.commit();
					break;
				}
				break;

			}
			return true;// 不会中断触摸事件的返回
		}
	};

}
