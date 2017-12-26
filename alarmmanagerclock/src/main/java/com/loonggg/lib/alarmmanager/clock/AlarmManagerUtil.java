package com.loonggg.lib.alarmmanager.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


import java.util.Calendar;

/**
 * Created by loonggg on 2016/3/21.
 */
public class AlarmManagerUtil {
    public static final String ALARM_ACTION = "com.loonggg.alarm.clock";

    public static void setAlarmTime(Context context, long timeInMillis, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, intent.getIntExtra("id", 0),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        int interval = (int) intent.getLongExtra("intervalMillis", 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setWindow(AlarmManager.RTC_WAKEUP, timeInMillis, interval, sender);
        }
    }

    public static void cancelAlarm(Context context, String action, int id) {
        Intent intent = new Intent(ALARM_ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }

    /**
     * @param flag   周期性时间间隔的标志,flag = 0 表示一次性的闹钟, flag = 1 表示每天提醒的闹钟(1天的时间间隔),flag = 2
     *               表示按周每周提醒的闹钟（一周的周期性时间间隔）
     * @param hour   时
     * @param minute 分
     * @param id     闹钟的id
     * @param week   week=0表示一次性闹钟或者按天的周期性闹钟，非0 的情况下是几就代表以周为周期性的周几的闹钟
     * @param tips   闹钟提示信息
     * @param ring   2表示声音和震动都执行，1表示只有铃声提醒，0表示只有震动提醒
     */
    public static void setAlarm(Context context, int flag, int hour, int minute, int id, int
            week, String tips, int ring) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        Intent intent = new Intent(ALARM_ACTION);
        intent.putExtra("ringType", ring);
        intent.putExtra("id", id);
//        intent.putExtra("soundOrVibrator", ring);
        switch (ring) {
            case 0:
                intent.putExtra("msg", "Normal to wake up");
                break;
            case 1:
                intent.putExtra("msg", "Easy to wake up");
                minute -= (int) Math.random() * 30;
                break;
            case 2:
                intent.putExtra("msg", "Force to wake up");
                break;
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), hour, minute, 5);
        intent.putExtra("timeInMillis", calMethod(week, calendar.getTimeInMillis()));

        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            am.setWindow(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis()),
//                    intervalMillis, sender);
//        } else {
//            if (flag == 0) {
//                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
//            } else {
//                am.setRepeating(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis
//                        ()), intervalMillis, sender);
//            }
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //API19以上使用
            if (flag == 0) {
                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis()), AlarmManager.INTERVAL_DAY, sender);
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis()), sender);
            }
        } else {
            if (flag == 0) {
                am.setRepeating(AlarmManager.RTC_WAKEUP, calMethod(week, calendar.getTimeInMillis()), AlarmManager.INTERVAL_DAY, sender);
            } else {
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            }
        }
    }


    /**
     * @param weekflag 传入的是周几
     * @param dateTime 传入的是时间戳（设置当天的年月日+从选择框拿来的时分秒）
     * @return 返回起始闹钟时间的时间戳
     */
    public static long calMethod(int weekflag, long dateTime) {
        long time = 0;
        //weekflag == 0表示是按天为周期性的时间间隔或者是一次行的，weekfalg非0时表示每周几的闹钟并以周为时间间隔
        if (weekflag != 0) {
            Calendar c = Calendar.getInstance();
            int week = c.get(Calendar.DAY_OF_WEEK);
            if (1 == week) {
                week = 7;
            } else if (2 == week) {
                week = 1;
            } else if (3 == week) {
                week = 2;
            } else if (4 == week) {
                week = 3;
            } else if (5 == week) {
                week = 4;
            } else if (6 == week) {
                week = 5;
            } else if (7 == week) {
                week = 6;
            }

            if (weekflag == week) {
                if (dateTime > System.currentTimeMillis()) {
                    time = dateTime;
                } else {
                    time = dateTime + 7 * 24 * 3600 * 1000;
                }
            } else if (weekflag > week) {
                time = dateTime + (weekflag - week) * 24 * 3600 * 1000;
            } else if (weekflag < week) {
                time = dateTime + (weekflag - week + 7) * 24 * 3600 * 1000;
            }
        } else {
            if (dateTime > System.currentTimeMillis()) {
                time = dateTime;
            } else {
                time = dateTime + 24 * 3600 * 1000;
            }
        }
        return time;
    }

    /**
     * 设置推迟闹钟的时间
     *
     * @param context
     * @param timeInMillis
     * @param id
     * @param intervalMillis
     */
    public static void setAlarmLaterInMinute(Context context, long timeInMillis, int id, int intervalMillis) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ALARM_ACTION);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeInMillis,
                intervalMillis, sender);
    }

    public static void setAlarm(Context context, long timeInMillis, int id) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ALARM_ACTION);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //API19以上使用
            am.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
        }
    }

}
