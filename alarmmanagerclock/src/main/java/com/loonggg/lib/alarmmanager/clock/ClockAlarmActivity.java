package com.loonggg.lib.alarmmanager.clock;

import android.app.Activity;
import android.app.Service;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.MyApp;
import com.loonggg.lib.alarmmanager.clock.bean.Alarm;
import com.zes.greendao.gen.AlarmDao;

import java.util.List;


public class ClockAlarmActivity extends Activity {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private int leftNumber, rightNumber;
    private int calculateResult;
    private int ringType;
    private String message = "";
    private int id;
    private long timeInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_alarm);
        int flag = this.getIntent().getIntExtra("flag", 0);
        ringType = this.getIntent().getIntExtra("ringType", 0);
        message = getIntent().getStringExtra("msg");
        id = getIntent().getIntExtra("id", 0);
        timeInMillis = getIntent().getLongExtra("timeInMillis", 0);
        showDialogInBroadcastReceiver();
    }

    private void showDialogInBroadcastReceiver() {
//        if (flag == 1 || flag == 2) {
        //

//        mediaPlayer = MediaPlayer.create(this, R.raw.in_call_alarm);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();
////        }
//        //数组参数意义：第一个参数为等待指定时间后开始震动，震动时间为第二个参数。后边的参数依次为等待震动和震动的时间
//        //第二个参数为重复次数，-1为不重复，0为一直震动
//        if (flag == 0 || flag == 2) {
        vibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{100, 10, 100, 600}, 0);
//        }

        switch (ringType) {
            case 0:
                normalWakeUp();
                break;
            case 1:
                easyWakeUp();
                break;
            case 2:
                forceWakeUp();
                break;
        }

    }

    /**
     * 普通唤醒
     */
    private void normalWakeUp() {
        final SimpleDialog dialog = new SimpleDialog(this, R.style.Theme_dialog);
        dialog.show();
        dialog.setTitle(message);
        dialog.setMessage("Alarming!!!");
        dialog.setLaterVisible();
        dialog.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.bt_confirm == v) {
//                    if (flag == 1 || flag == 2) {
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    }
//                    if (flag == 0 || flag == 2) {
                    vibrator.cancel();
//                    }
                    AlarmManagerUtil.cancelAlarm(ClockAlarmActivity.this, LoongggAlarmReceiver.GRAY_WAKE_ACTION, id);
                    dialog.dismiss();
                    finish();
                }
            }
        });
        dialog.setLaterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.bt_later == v) {
//                    if (flag == 1 || flag == 2) {
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    }
//                    if (flag == 0 || flag == 2) {
                    List<Alarm> alarms = MyApp.instances.getDaoSession().getAlarmDao().queryBuilder().where(AlarmDao.Properties.Id.eq(id)).list();
                    if (alarms != null && alarms.size() > 0) {
                        if (!alarms.get(0).getIsLater()) {
                            AlarmManagerUtil.setAlarmLaterInMinute(ClockAlarmActivity.this, timeInMillis, id, 1000 * 60 * 1);
                        } else {
                            alarms.get(0).setIsLater(true);
                            MyApp.instances.getDaoSession().getAlarmDao().update(alarms.get(0));
                        }
                    }
                    vibrator.cancel();
                    dialog.dismiss();
                    finish();
                }
            }
        });

    }

    /**
     * 轻松唤醒
     */
    private void easyWakeUp() {
        final SimpleDialog dialog = new SimpleDialog(this, R.style.Theme_dialog);
        dialog.show();
        dialog.setTitle(message);
        dialog.setMessage("Alarming!!!");
        dialog.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.bt_confirm == v) {
//                    if (flag == 1 || flag == 2) {
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    }
//                    if (flag == 0 || flag == 2) {
                    vibrator.cancel();
//                    }
                    dialog.dismiss();
                    finish();
                }
            }
        });
    }

    /**
     * 强制唤醒
     */
    private void forceWakeUp() {
        final CalculateDialog dialog = new CalculateDialog(this, R.style.Theme_dialog);
        dialog.show();
        dialog.setTitle(message);
        leftNumber = (int) (Math.random() * 100);
        rightNumber = (int) (Math.random() * 100);
        String opt = "+";
        if (Math.random() >= 0.5) {
            calculateResult = leftNumber + rightNumber;
        } else {
            opt = "-";
            calculateResult = leftNumber - rightNumber;
        }
        dialog.setExpression(leftNumber + " " + opt + " " + rightNumber + " =");
        dialog.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.bt_confirm == v || dialog.bt_cancel == v) {
//                    if (flag == 1 || flag == 2) {
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    }
//                    if (flag == 0 || flag == 2) {
                    if (dialog.getCalculateResult() != calculateResult) {
                        Toast.makeText(ClockAlarmActivity.this, "计算错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    }
                    vibrator.cancel();
                    dialog.dismiss();
                    finish();
                }
            }
        });
    }
}
