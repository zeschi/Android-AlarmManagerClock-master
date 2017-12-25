package com.loonggg.alarmmanager.clock.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.loonggg.alarmmanager.clock.R;


public class SelectRemindWayPopup implements OnClickListener {
    private TextView remaind_way_normal, remaind_way_easy, remaind_way_force;
    public PopupWindow mPopupWindow;
    private SelectRemindWayPopupOnClickListener selectRemindWayPopupListener;

    public PopupWindow getmPopupWindow() {
        return mPopupWindow;
    }

    private Context mContext;

    @SuppressWarnings("deprecation")
    public SelectRemindWayPopup(Context context) {
        mContext = context;
        mPopupWindow = new PopupWindow(context);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.FILL_PARENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.setContentView(initViews());

        mPopupWindow.getContentView().setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPopupWindow.setFocusable(false);
                mPopupWindow.dismiss();
                return true;
            }
        });

    }

    public View initViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.selectremindway_pop_window,
                null);

        remaind_way_normal = (TextView) view.findViewById(R.id.tv_normal_to_wake_up);
        remaind_way_easy = (TextView) view.findViewById(R.id.tv_easy_to_wake_up);
        remaind_way_force = (TextView) view.findViewById(R.id.tv_force_to_wake_up);
        remaind_way_normal.setOnClickListener(this);
        remaind_way_easy.setOnClickListener(this);
        remaind_way_force.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_normal_to_wake_up:
                selectRemindWayPopupListener.obtainMessage(0);
                break;
            case R.id.tv_easy_to_wake_up:
                selectRemindWayPopupListener.obtainMessage(1);
                break;
            case R.id.tv_force_to_wake_up:
                selectRemindWayPopupListener.obtainMessage(2);
                break;
            default:
                break;
        }
        dismiss();

    }

    public interface SelectRemindWayPopupOnClickListener {
        void obtainMessage(int flag);
    }

    public void setOnSelectRemindWayPopupListener(SelectRemindWayPopupOnClickListener l) {
        this.selectRemindWayPopupListener = l;
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public void showPopup(View rootView) {
        // 第一个参数是要将PopupWindow放到的View，第二个参数是位置，第三第四是偏移值
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }
}
