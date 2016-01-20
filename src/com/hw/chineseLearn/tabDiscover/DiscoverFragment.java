package com.hw.chineseLearn.tabDiscover;

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
import android.widget.LinearLayout;
import android.widget.ListView;

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
public class DiscoverFragment extends BaseFragment implements OnClickListener {
	private View contentView;// 主view

	// 首页轮播图
	private View layout_adv;
	private LinearLayout layout_dots;
	private ArrayList<View> dot_view;
	private int currentItem = 0;

	// 列表-上下拉刷新
	private PullToRefreshView pullToRefreshView;
	private ListView lv_notice;
	private NoticesListAdapter adapter;
	private ArrayList<MeiTuanGuessBaseModel> noticeList;

	private String serverTime = "";
	private int pageIndex = 1, pageSize = 10;
	private boolean IsEnd = false;
	public static boolean IsRefresh = false;

	private ThreadWithDialogTask task;
	private NoticeModel noticeModel;
	public static DiscoverFragment fragment;

	private boolean IsFirst = true;

	private boolean IsCollect = false;
	AddressSelectPopMenu popWin;
	/**
	 * 中部ViewPager
	 */
	private ViewPager viewPager;

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
		// "discover is loading…");
	}

	/**
	 * 初始化
	 */
	public void init() {

		pullToRefreshView = (PullToRefreshView) contentView
				.findViewById(R.id.pullToRefreshView);
		lv_notice = (ListView) contentView.findViewById(R.id.lv_notice);

		layout_adv = LayoutInflater.from(getActivity()).inflate(
				R.layout.layout_notice_adv, null);
		lv_notice.addHeaderView(layout_adv);

		noticeList = new ArrayList<MeiTuanGuessBaseModel>();
		adapter = new NoticesListAdapter(getActivity(), noticeList);
		lv_notice.setAdapter(adapter);
		lv_notice.setDividerHeight(0);

		pullToRefreshView
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
					@Override
					public void onHeaderRefresh(PullToRefreshView view) {

						view.onHeaderRefreshComplete();

						IsEnd = false;
						pageIndex = 1;
						noticeList.clear();
						adapter.noticeList = noticeList;
						adapter.notifyDataSetChanged();

						task.RunWithMsg(getActivity(), new LoadNoticesThread(),
								"正在加载…");
					}
				});

		pullToRefreshView
				.setOnFooterRefreshListener(new OnFooterRefreshListener() {
					@Override
					public void onFooterRefresh(PullToRefreshView view) {
						view.onFooterRefreshComplete();
						// if (IsEnd) {
						// Toast.makeText(getActivity(), "暂无更多数据",
						// Toast.LENGTH_SHORT).show();
						// } else {
						// task.RunWithMsg(getActivity(),
						// new LoadNoticesThread(), "正在加载…");
						// }
					}
				});

		pullToRefreshView
				.setOnRefreshTouchListener(new OnRefreshTouchListener() {

					@Override
					public void onTouchListener(PullToRefreshView view) {

					}
				});
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

		viewPager = (ViewPager) contentView.findViewById(R.id.center_menu);
		LinearLayout linDots = (LinearLayout) contentView
				.findViewById(R.id.lin_dots);// 滑动的小点
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

		linDots.setVisibility(View.VISIBLE);

		pagerList.add(layout1);
		pagerList.add(layout2);

		getViewPagerData(0);

		viewPager.setAdapter(new ViewPagerAdapter(pagerList));
		viewPager.setCurrentItem(0);

		// ViewPAger监听
		ViewpagerChangeListener guidePager = new ViewpagerChangeListener(
				getActivity(), dots);
		viewPager.setOnPageChangeListener(guidePager);

	}

	/**
	 * 
	 * ViewPager 适配器
	 * 
	 * @author
	 * 
	 */
	class ViewPagerAdapter extends PagerAdapter {
		List<View> list = new ArrayList<View>();

		public ViewPagerAdapter(List<View> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ViewPager pViewPager = ((ViewPager) container);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.addView(list.get(arg1));
			return list.get(arg1);
		}
	}

	/**
	 * 
	 * viewpager 滑动监听类
	 * 
	 * @author YF
	 * 
	 */
	class ViewpagerChangeListener implements OnPageChangeListener {

		private static final String Tag = "ViewpagerChangeListener";
		List<View> dots;
		Context context;
		int currentItem;
		private int oldPosition = 0;

		/**
		 * @param context
		 * @param dots
		 *            构造函数
		 */
		public ViewpagerChangeListener(Context context, List<View> dots) {
			this.context = context;
			this.dots = dots;
		}

		public ViewpagerChangeListener(Context context) {
			this.context = context;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		/**
		 * 滑动事件
		 */
		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			currentItem = position;
			currentPager = position;
			// 改变圆点(焦点)
			dots.get(oldPosition).setBackgroundResource(
					R.drawable.welcome_center_dot_default);
			dots.get(position).setBackgroundResource(
					R.drawable.welcome_center_dot_pressed);
			oldPosition = position;
			getViewPagerData(position);
		}
	}

	/**
	 * 显示数据并设置监听
	 * 
	 * @param
	 */
	public void getViewPagerData(int pagerIndex) {
		// TODO Auto-generated method stub
		View v = pagerList.get(pagerIndex);
		centGridView = (GridView) v.findViewById(R.id.gv_center_gridview);

		if (pagerIndex == 0) {
			// theNewInfoCount[5] = 2;
			images = new int[]

			{ R.drawable.ic_category_0, R.drawable.ic_category_1,
					R.drawable.ic_category_2, R.drawable.ic_category_3,
					R.drawable.ic_category_4, R.drawable.ic_category_5,
					R.drawable.ic_category_6, R.drawable.ic_category_7 };

			texts = new String[] { "美食", "电影", "酒店", "KTV", "NEW", "优惠买单",
					"周边游", "今日新单" };

		} else if (pagerIndex == 1) {

			images = new int[]

			{ R.drawable.ic_category_8, R.drawable.ic_category_9,
					R.drawable.ic_category_10, R.drawable.ic_category_11,
					R.drawable.ic_category_12, R.drawable.ic_category_13,
					R.drawable.ic_category_14, R.drawable.ic_category_15 };

			texts = new String[] { "9", "10", "11", "12", "13", "14", "15",
					"16" };
		}

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

	/**
	 * @author
	 */
	public class LoadNoticesThread implements ThreadWithDialogListener {
		@Override
		public boolean TaskMain() {
			noticeModel = null;
			HttpInterfaces interfaces = new HttpInterfaces(getActivity());

			noticeModel = interfaces.getNotices(pageIndex, pageSize);
			return true;
		}

		@Override
		public boolean OnTaskDone() {
			IsRefresh = false;
			if (noticeModel != null) {
				if (noticeModel.getStatus() == 1) {
					if (noticeModel.getData() != null) {
						if (noticeModel.getData().size() < pageSize) {
							IsEnd = true;
						} else {
							pageIndex++;
						}
						noticeList.addAll(noticeModel.getData());
						adapter.noticeList = noticeList;
						adapter.notifyDataSetChanged();
					}
				}

			}
			return true;
		}

		@Override
		public boolean OnTaskDismissed() {
			// TODO Auto-generated method stub
			return true;
		}
	}

}
