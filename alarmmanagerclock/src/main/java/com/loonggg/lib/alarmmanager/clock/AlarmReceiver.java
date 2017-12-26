package com.loonggg.lib.alarmmanager.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 闹铃接受器
 *
 * @author wangyubin
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub   
        Toast.makeText(context, "闹钟时间到:", Toast.LENGTH_LONG).show();
    }
}