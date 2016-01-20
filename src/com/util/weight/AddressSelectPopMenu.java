package com.util.weight;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hw.chineseLearn.R;

/**
 * 
 * @author Administrator
 */
public class AddressSelectPopMenu {
    public Context context;
    public PopupWindow popupWindow;
    public Button buttonCamera, buttonPhoto;
    public LinearLayout lin_pop_dismess;

    public AddressSelectPopMenu(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.img_select_popmenu, null);

        lin_pop_dismess = (LinearLayout) view
                .findViewById(R.id.lin_pop_dismess);

        popupWindow = new PopupWindow(view,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);// 设置宽高
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响背景

        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimBottom);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        popupWindow.setBackgroundDrawable(dw);

        // 为item设置监听
        lin_pop_dismess.setOnClickListener(itemsOnClick);
    }

    // 显示PopWindow
    public void ShowPopWindow(View v) {
        // popupWindow.showAsDropDown(v, -80, -30);//设置位置
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 160);// 设置位置
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 刷新状态
        popupWindow.update();
    }

    // 设置点击监听器
    public OnClickListener itemsOnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            switch (v.getId()) {
            case R.id.lin_pop_dismess:// 点击消失
                dismiss();
                break;
            default:
                break;
            }

        }

    };

    // 隐藏菜单
    public void dismiss() {
        popupWindow.dismiss();
    }

}
