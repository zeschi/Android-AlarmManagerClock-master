package com.loonggg.alarmmanager.clock;

import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MyApp;
import com.bigkoo.pickerview.TimePickerView;
import com.loonggg.lib.alarmmanager.clock.bean.Alarm;
import com.loonggg.alarmmanager.clock.service.IMyBinder;
import com.loonggg.alarmmanager.clock.view.SelectRemindCyclePopup;
import com.loonggg.alarmmanager.clock.view.SelectRemindWayPopup;
import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;
import com.zes.greendao.gen.AlarmDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView date_tv;
    private TimePickerView pvTime;
    private RelativeLayout repeat_rl, ring_rl;
    private TextView tv_repeat_value, tv_ring_value;
    private LinearLayout allLayout;
    private Button set_btn, cancel_btn;

    private String time;
    private int cycle;
    private int ring;
    private JobScheduler mJobScheduler;
    private MyConn myConn;
    private IMyBinder myBinder;

    private Alarm mAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAlarm = (Alarm) getIntent().getSerializableExtra("Alarm");

        allLayout = (LinearLayout) findViewById(R.id.all_layout);
        set_btn = (Button) findViewById(R.id.set_btn);
        set_btn.setOnClickListener(this);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(this);

        date_tv = (TextView) findViewById(R.id.date_tv);
        repeat_rl = (RelativeLayout) findViewById(R.id.repeat_rl);
        repeat_rl.setOnClickListener(this);
        ring_rl = (RelativeLayout) findViewById(R.id.ring_rl);
        ring_rl.setOnClickListener(this);
        tv_repeat_value = (TextView) findViewById(R.id.tv_repeat_value);
        tv_ring_value = (TextView) findViewById(R.id.tv_ring_value);
        pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                time = getTime(date);
                date_tv.setText(time);
            }
        });

        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
//          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//            JobInfo.Builder builder = new JobInfo.Builder(12345,
//                    new ComponentName(getPackageName(), JobSchedulerService.class.getName()));
//                builder.setPeriodic(3000); //每隔60秒运行一次
//                builder.setRequiresCharging(true);
//                builder.setPersisted(true);  //设置设备重启后，是否重新执行任务
//                builder.setRequiresDeviceIdle(true);
//              mJobScheduler.schedule(builder.build());          }
//
//
            }
        });
//        myConn = new MyConn();
//        Intent daemonIntent = new Intent(this, DaemonService.class);
////        bindService(daemonIntent, myConn, BIND_AUTO_CREATE);
//        startService(daemonIntent);
        if (mAlarm != null) {
            time = mAlarm.getAlarmTime();
            date_tv.setText(mAlarm.getAlarmTime());
            tv_repeat_value.setText(mAlarm.getAlarmTypeName());
            cycle = mAlarm.getCycle();
            ring = mAlarm.getAlarmType();
            switch (mAlarm.getAlarmType()) {
                case 0:
                    tv_ring_value.setText("Normal to wake up");
                    break;
                case 1:
                    tv_ring_value.setText("Easy to wake up");
                    break;
                case 2:
                    tv_ring_value.setText("Force to wake up");
                    break;
            }
        }
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.repeat_rl:
                selectRemindCycle();
                break;
            case R.id.ring_rl:
                selectRingWay();
                break;
            case R.id.set_btn:
                setClock();
                break;
            case R.id.cancel_btn:
                finish();
            default:
                break;
        }
    }

    private class MyConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //iBinder为服务里面onBind()方法返回的对象，所以可以强转为IMyBinder类型
            myBinder = (IMyBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }


    private void setClock() {
        if (time != null && time.length() > 0 || mAlarm != null) {
            String[] times = time.split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                    (Calendar.DAY_OF_MONTH), Integer.parseInt(times[0]), Integer.parseInt
                    (times[1]), 5);
            Alarm alarm = new Alarm();
            if (mAlarm == null) {
                alarm.setAlarmTime(time);
                alarm.setAlarmType(ring);
                alarm.setIsOpen(true);
                switch (cycle) {
                    case 0:
                        alarm.setAlarmTypeName("EveryDay");
                        break;
                    case -1:
                        alarm.setAlarmTypeName("Once");
                        break;
                    default:
                        alarm.setAlarmTypeName(parseRepeat(cycle, 0));
                }
                alarm.setCycle(cycle);
                alarm.setTimeInMillis(AlarmManagerUtil.calMethod(0, calendar.getTimeInMillis()));

                MyApp.instances.getDaoSession().getAlarmDao().insert(alarm);
            } else {
//                time = mAlarm.getAlarmTime();
                mAlarm.setAlarmTime(time);
                mAlarm.setAlarmType(ring);
                mAlarm.setIsOpen(true);
                mAlarm.setCycle(cycle);
                switch (cycle) {
                    case 0:
                        mAlarm.setAlarmTypeName("EveryDay");
                        break;
                    case -1:
                        mAlarm.setAlarmTypeName("Once");
                        break;
                    default:
                        mAlarm.setAlarmTypeName(parseRepeat(cycle, 0));
                }
                mAlarm.setTimeInMillis(AlarmManagerUtil.calMethod(0, calendar.getTimeInMillis()));
                MyApp.instances.getDaoSession().getAlarmDao().update(mAlarm);
            }

            if (cycle == 0) {//是每天的闹钟
                AlarmManagerUtil.setAlarm(this, 0, Integer.parseInt(times[0]), Integer.parseInt
                        (times[1]), getAlarmId(), 0, "Alarming", ring);
            } else if (cycle == -1) {//是只响一次的闹钟
                AlarmManagerUtil.setAlarm(this, 1, Integer.parseInt(times[0]), Integer.parseInt
                        (times[1]), getAlarmId(), 0, "Alarming", ring);
            } else {//多选，周几的闹钟
                String weeksStr = parseRepeat(cycle, 1);
                String[] weeks = weeksStr.split(",");
                if (mAlarm != null) {
                    for (int i = 1; i <= mAlarm.getWeekLengths(); i++) {
                        MyApp.instances.getDaoSession().getAlarmDao().deleteByKey(mAlarm.getId() + i);
                    }
                    mAlarm.setWeekLengths(mAlarm.getWeekLengths() + weeks.length);
                    mAlarm.setTimeInMillis(AlarmManagerUtil.calMethod(Integer.parseInt(weeks[0]), calendar.getTimeInMillis()));
                    MyApp.instances.getDaoSession().getAlarmDao().update(mAlarm);
                } else {
                    alarm.setWeekLengths(weeks.length);
                    alarm.setTimeInMillis(AlarmManagerUtil.calMethod(Integer.parseInt(weeks[0]), calendar.getTimeInMillis()));
                    MyApp.instances.getDaoSession().getAlarmDao().update(alarm);
                }
                AlarmManagerUtil.setAlarm(this, 2, Integer.parseInt(times[0]), Integer
                        .parseInt(times[1]), getAlarmId(), 0, "Alarming", ring);
                for (int i = 1; i < weeks.length; i++) {
                    AlarmManagerUtil.setAlarm(this, 2, Integer.parseInt(times[0]), Integer
                            .parseInt(times[1]), getAlarmId() + 1, Integer.parseInt(weeks[i]), "Alarming", ring);
                    if (i > 0) {
                        Alarm temp = new Alarm();
                        temp.setTimeInMillis(calMethod(Integer.parseInt(weeks[i]), calendar.getTimeInMillis()));
                        MyApp.instances.getDaoSession().getAlarmDao().insert(temp);
                    }
                }
            }
            Toast.makeText(this, "Setting Success", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Please Choose Time", Toast.LENGTH_LONG).show();
        }

    }


    public void selectRemindCycle() {
        final SelectRemindCyclePopup fp = new SelectRemindCyclePopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindCyclePopupListener(new SelectRemindCyclePopup
                .SelectRemindCyclePopupOnClickListener() {

            @Override
            public void obtainMessage(int flag, String ret) {
                switch (flag) {
                    // 星期一
                    case 0:

                        break;
                    // 星期二
                    case 1:

                        break;
                    // 星期三
                    case 2:

                        break;
                    // 星期四
                    case 3:

                        break;
                    // 星期五
                    case 4:

                        break;
                    // 星期六
                    case 5:

                        break;
                    // 星期日
                    case 6:

                        break;
                    // 确定
                    case 7:
                        int repeat = Integer.valueOf(ret);
                        tv_repeat_value.setText(parseRepeat(repeat, 0));
                        cycle = repeat;
                        fp.dismiss();
                        break;
                    case 8:
                        tv_repeat_value.setText("Every Day");
                        cycle = 0;
                        fp.dismiss();
                        break;
                    case 9:
                        tv_repeat_value.setText("Once");
                        cycle = -1;
                        fp.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public void selectRingWay() {
        SelectRemindWayPopup fp = new SelectRemindWayPopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindWayPopupListener(new SelectRemindWayPopup
                .SelectRemindWayPopupOnClickListener() {

            @Override
            public void obtainMessage(int flag) {
                switch (flag) {
                    // 震动
                    case 0:
                        tv_ring_value.setText("Normal to wake up");
                        ring = 0;
                        break;
                    // 铃声
                    case 1:
                        tv_ring_value.setText("Easy to wake up");
                        ring = 1;
                        break;
                    case 2:
                        tv_ring_value.setText("Force to wake up");
                        ring = 2;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * @param repeat 解析二进制闹钟周期
     * @param flag   flag=0返回带有汉字的周一，周二cycle等，flag=1,返回weeks(1,2,3)
     * @return
     */
    public static String parseRepeat(int repeat, int flag) {
        String cycle = "";
        String weeks = "";
        if (repeat == 0) {
            repeat = 127;
        }
        if (repeat % 2 == 1) {
            cycle = "Mon";
            weeks = "1";
        }
        if (repeat % 4 >= 2) {
            if ("".equals(cycle)) {
                cycle = "Tue";
                weeks = "2";
            } else {
                cycle = cycle + "," + "Tue";
                weeks = weeks + "," + "2";
            }
        }
        if (repeat % 8 >= 4) {
            if ("".equals(cycle)) {
                cycle = "Wed";
                weeks = "3";
            } else {
                cycle = cycle + "," + "Wed";
                weeks = weeks + "," + "3";
            }
        }
        if (repeat % 16 >= 8) {
            if ("".equals(cycle)) {
                cycle = "Thu";
                weeks = "4";
            } else {
                cycle = cycle + "," + "Thu";
                weeks = weeks + "," + "4";
            }
        }
        if (repeat % 32 >= 16) {
            if ("".equals(cycle)) {
                cycle = "Fri";
                weeks = "5";
            } else {
                cycle = cycle + "," + "Fri";
                weeks = weeks + "," + "5";
            }
        }
        if (repeat % 64 >= 32) {
            if ("".equals(cycle)) {
                cycle = "Sat";
                weeks = "6";
            } else {
                cycle = cycle + "," + "Sat";
                weeks = weeks + "," + "6";
            }
        }
        if (repeat / 64 == 1) {
            if ("".equals(cycle)) {
                cycle = "Sun";
                weeks = "7";
            } else {
                cycle = cycle + "," + "Sun";
                weeks = weeks + "," + "7";
            }
        }

        return flag == 0 ? cycle : weeks;
    }

    @Override
    public void finishActivity(int requestCode) {
        super.finishActivity(requestCode);
    }

    private int getAlarmId() {
        int id = 0;
        List<Alarm> alarmList = MyApp.instances.getDaoSession().getAlarmDao().queryBuilder().orderDesc(AlarmDao.Properties.Id).list();
        if (alarmList != null && alarmList.size() > 0) {
            id = alarmList.get(0).getId().intValue();
        }
        return id;
    }

    private long calMethod(int weekflag, long dateTime) {
        long time = 0;
        //weekflag == 0表示是按天为周期性的时间间隔或者是一次行的，weekfalg非0时表示每周几的闹钟并以周为时间间隔
        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.DAY_OF_WEEK);
        if (weekflag != 0) {
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
}
