package com.loonggg.lib.alarmmanager.clock;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/12/26.
 */

public class CalculateDialog extends Dialog {
    protected static int default_width = WindowManager.LayoutParams.WRAP_CONTENT; // 默认宽度
    protected static int default_height = WindowManager.LayoutParams.WRAP_CONTENT;// 默认高度
    public static int TYPE_TWO_BT = 2;
    public static int TYPE_NO_BT = 0;
    public TextView dialog_title, tv_calculate_expression;
    public EditText et_calculate_result;
    public Button bt_cancel, bt_confirm;
    private LinearLayout ll_button;
    protected Context mContext;
    private View.OnClickListener listener;
    private View customView;
    //	@Bind(R.id.icon)
    ImageView icon;


    public CalculateDialog(Context context, int style) {
        super(context, R.style.FullScreenDialog);
        mContext = context;
        customView = LayoutInflater.from(context).inflate(R.layout.dialog_calculate, null);

        icon = (ImageView) customView.findViewById(R.id.icon);

        ll_button = (LinearLayout) customView.findViewById(R.id.ll_button);
        dialog_title = (TextView) customView.findViewById(R.id.dialog_title);
        setTitle("提示信息");
        et_calculate_result = (EditText) customView.findViewById(R.id.et_calculate_result);
        tv_calculate_expression = (TextView) customView.findViewById(R.id.tv_calculate_expression);
//        et_calculate_result.clearFocus();
        bt_confirm = (Button) customView.findViewById(R.id.dialog_confirm);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(customView);
        //ButterKnife  view绑定
        //ButterKnife.bind(this,customView);
    }

    public CalculateDialog setClickListener(View.OnClickListener listener) {
        this.listener = listener;
        bt_confirm.setOnClickListener(listener);
        return this;
    }

    public CalculateDialog setExpression(String message) {
        tv_calculate_expression.setText(message);
        return this;
    }

//    public CalculateDialog setCalculateResult(String message) {
//        et_calculate_result.setText(message);
//        return this;
//    }

    public CalculateDialog setTitle(String title) {
        dialog_title.setText(title);
        return this;
    }

    public CalculateDialog setIcon(int iconResId) {
        dialog_title.setVisibility(View.GONE);
        icon.setVisibility(View.VISIBLE);
        icon.setBackgroundResource(iconResId);

        return this;
    }

    public int getCalculateResult() {
        String result = "0";
        if (!TextUtils.isEmpty(et_calculate_result.getText().toString())) {
            result = et_calculate_result.getText().toString();
        }
        return Integer.parseInt(result);
    }
}
