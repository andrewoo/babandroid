package com.hw.chineseLearn.tabLearn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.HomeFunctionAdapter_new;
import com.hw.chineseLearn.adapter.NoticesListAdapter;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.interfaces.HttpInterfaces;
import com.hw.chineseLearn.model.MeiTuanGuessBaseModel;
import com.hw.chineseLearn.model.NoticeModel;
import com.util.thread.ThreadWithDialogListener;
import com.util.thread.ThreadWithDialogTask;
import com.util.weight.AddressSelectPopMenu;
import com.util.weight.PullToRefreshView;
import com.util.weight.PullToRefreshView.OnFooterRefreshListener;
import com.util.weight.PullToRefreshView.OnHeaderRefreshListener;
import com.util.weight.PullToRefreshView.OnRefreshTouchListener;

/**
 * @author yh
 */
@SuppressLint("NewApi")
public class LearnFragment extends BaseFragment implements OnClickListener {
	private View contentView;// 主view

	// 首页轮播图
	private View layout_adv;
	// private AdvViewPagerAdapter pagerAdapter;
	private LinearLayout layout_dots;
	private ArrayList<View> dot_view;
	private int currentItem = 0;

	// 列表-上下拉刷新
	private String serverTime = "";
	private int pageIndex = 1, pageSize = 10;
	private boolean IsEnd = false;
	public static boolean IsRefresh = false;

	private ThreadWithDialogTask task;
	private NoticeModel noticeModel;
	public static LearnFragment fragment;

	private boolean IsFirst = true;

	private boolean IsCollect = false;
	AddressSelectPopMenu popWin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fragment = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_notice, null);

		task = new ThreadWithDialogTask();

		init();

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
		// TODO Auto-generated method stub
		super.onResume();
		// task.RunWithMsg(getActivity(), new LoadNoticesThread(),
		// "Learn is loading…");
	}

	/**
	 * 初始化
	 */
	public void init() {

		createCenter();
	}

	HomeFunctionAdapter_new saImageItemsViewPager;
	/**
	 * ViewPager中所有的页面，list size有几个，就有几个页面
	 */
	ArrayList<View> pagerList;
	GridView centGridView;
	/**
	 * 滑动的小圆点
	 */
	public List<View> dots;
	int currentPager = 0;

	// 主菜单的文本
	private String texts[] = null;
	// 主菜单的图标
	private int images[] = null;

	public int[] theNewInfoCount = new int[30];

	/**
	 * 中部Viewpager
	 */
	private void createCenter() {
		// TODO Auto-generated method stub

		pagerList = new ArrayList<View>();

		View v = contentView.findViewById(R.id.v_dot0);
		View v1 = contentView.findViewById(R.id.v_dot1);

		dots = new ArrayList<View>();

		dots.add(v);
		dots.add(v1);
		// 实例化一个view 添加到pagerList中
		View layout1 = LayoutInflater.from(getActivity()).inflate(
				R.layout.activity_gv_center, null);
		View layout2 = LayoutInflater.from(getActivity()).inflate(
				R.layout.activity_gv_center, null);

		pagerList.add(layout1);
		pagerList.add(layout2);

		getViewPagerData(0);

	}

	/**
	 * 显示数据并设置监听
	 * 
	 * @param
	 */
	public void getViewPagerData() {
		// TODO Auto-generated method stub
		centGridView = (GridView)contentView.findViewById(R.id.gv_center_gridview);

		images = new int[] { R.drawable.ic_category_0,
				R.drawable.ic_category_1, R.drawable.ic_category_2,
				R.drawable.ic_category_3, R.drawable.ic_category_4,
				R.drawable.ic_category_5, R.drawable.ic_category_6,
				R.drawable.ic_category_7 };

		texts = new String[] { "Basics1", "Basics2", "Basics3", "Color", "Number&Measure", "Food", "Shape",
				"Nature" };// Negation Question Time Tense 

		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < images.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", images[i]);
			map.put("itemSign", this.theNewInfoCount[i]);
			map.put("itemText", texts[i]);
			lstImageItem.add(map);
		}

		saImageItemsViewPager = new HomeFunctionAdapter_new(
				getActivity(),
				lstImageItem,// 数据源
				R.layout.night_item_new,// 显示布局
				new String[] { "itemImage", "itemText", "itemSign" },
				new int[] { R.id.itemImage, R.id.itemText, R.id.itemSign });
		centGridView.setAdapter(saImageItemsViewPager);
		saImageItemsViewPager.notifyDataSetChanged();
	}

}
