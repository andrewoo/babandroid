package com.hw.chineseLearn.tabLearn;

import java.util.ArrayList;
import java.util.Collections;

import ui.custom.pdgrid.DraggableGridView;
import ui.custom.pdgrid.PagedDragDropGrid;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.hw.chineseLearn.R;
import com.hw.chineseLearn.adapter.DragDropGridAdapter;
import com.hw.chineseLearn.adapter.ServiceColumnAdapter;
import com.hw.chineseLearn.base.BaseFragment;
import com.hw.chineseLearn.model.TitleEntity;
import com.util.thread.ThreadWithDialogTask;
import com.util.tool.FileTools;

/**
 * 拼句子
 * 
 * @author yh
 */
@SuppressLint("NewApi")
public class LearnSentenceMoveFragment extends BaseFragment implements
		OnClickListener {
	private View contentView;// 主view

	private ThreadWithDialogTask task;
	public static LearnSentenceMoveFragment fragment;
	Context context;

	GridView gridView;
	ArrayList<TitleEntity> listEntity1;
	ArrayList<TitleEntity> listEntity2;
	PagedDragDropGrid dgvColumn;
	DraggableGridView dgv;
	DragDropGridAdapter adapter1;
	ServiceColumnAdapter adapter2;

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
		contentView = inflater.inflate(R.layout.fragment_lesson_sentence_move,
				null);

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
	}

	/**
	 * 初始化
	 */
	public void init() {

		listEntity1 = new ArrayList<TitleEntity>();
		listEntity2 = new ArrayList<TitleEntity>();
		dgvColumn = (PagedDragDropGrid) contentView
				.findViewById(R.id.column_edit_grid_gv);
		gridView = (GridView) contentView
				.findViewById(R.id.column_edit_gv_gvcolumn);
		initServiceColumn();
		initColumn();
	}

	private void initColumn() {
		String json = "";
		try {
			json = FileTools.readTxtFile(context.getAssets().open(
					"column1.json"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("column", json);
		TitleEntity entity = new TitleEntity();
		listEntity1 = new ArrayList<TitleEntity>();
		try {
			listEntity1 = entity.getListEntity(json);
			Collections.sort(listEntity1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter1 = new DragDropGridAdapter(context, dgvColumn, listEntity1,
				adapter2);
		dgvColumn.setAdapter(adapter1);
	}

	private void initServiceColumn() {
		String json = "";
		try {
			json = FileTools.readTxtFile(context.getAssets().open(
					"column1.json"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("column", json);
		TitleEntity entity = new TitleEntity();
		listEntity2 = new ArrayList<TitleEntity>();
		try {
			listEntity2 = entity.getListEntity(json);
			Collections.sort(listEntity2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter2 = new ServiceColumnAdapter(context, listEntity2);
		gridView.setAdapter(adapter2);
		gridView.setOnItemClickListener(onItemClickListener);
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			int index = Integer.parseInt(arg1.findViewById(R.id.column_tv_id)
					.getTag().toString());
			listEntity1.add(listEntity2.get(index));
			listEntity2.remove(index);
			adapter2.setSelection(position);
			adapter2.notifyDataSetChanged();
			dgvColumn.notifyDataSetChanged();
		}
	};
}
