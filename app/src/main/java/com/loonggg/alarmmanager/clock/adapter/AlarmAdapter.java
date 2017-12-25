package com.loonggg.alarmmanager.clock.adapter;


import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.loonggg.alarmmanager.clock.MyApp;
import com.loonggg.alarmmanager.clock.R;
import com.loonggg.alarmmanager.clock.bean.Alarm;
import com.loonggg.alarmmanager.clock.view.CustomDialog;
import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;
import com.zes.greendao.gen.AlarmDao;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/24.
 */

public class AlarmAdapter extends CommonRecycleAdapter<Alarm> {
    private Context mContext;

    public AlarmAdapter(Context context, List<Alarm> datas, int layoutId) {

        super(context, datas, layoutId);
        this.mContext = context;
    }

    @Override
    protected void convertView(final RecycleViewHolder holder, final Alarm data, final int position) {

        holder.setText(R.id.tv_alarm_time, data.getAlarmTime());
        holder.getView(R.id.iv_alarm_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(data, position);
            }
        });
        Switch sw = holder.getView(R.id.sw_alarm);
        if (data.getIsOpen()) {
            sw.setChecked(true);
        } else {
            sw.setChecked(false);
        }
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                data.setIsOpen(b);
                updateAlarm(data, position);
                setIcon(holder, data);
            }
        });
        holder.setText(R.id.tv_alarm_type, data.getAlarmTypeName());
        setIcon(holder, data);
    }

    /**
     * 设置icon
     *
     * @param holder
     * @param data
     */
    private void setIcon(RecycleViewHolder holder, Alarm data) {
        switch (data.getAlarmType()) {
            case 0:
                if (data.getIsOpen()) {
                    holder.setImageResource(R.id.iv_alarm_type, R.mipmap.ic_alarm_normal_open);
                } else {
                    holder.setImageResource(R.id.iv_alarm_type, R.mipmap.ic_alarm_normal_close);
                }
                break;
            case 1:
                if (data.getIsOpen()) {
                    holder.setImageResource(R.id.iv_alarm_type, R.mipmap.ic_alarm_easy_open);
                } else {
                    holder.setImageResource(R.id.iv_alarm_type, R.mipmap.ic_alarm_easy_close);
                }
                break;
            default:
                if (data.getIsOpen()) {
                    holder.setImageResource(R.id.iv_alarm_type, R.mipmap.ic_sigh);
                } else {
                    holder.setImageResource(R.id.iv_alarm_type, R.mipmap.ic_sigh_close);
                }

        }
    }

    private void delete(final Alarm data, int position) {
        showDialog(data);
    }

    private Observable<Boolean> delete(final Alarm data) {
        Observable<Boolean> observable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                try {
                    AlarmManagerUtil.cancelAlarm(mContext, AlarmManagerUtil.ALARM_ACTION, getAlarmId());
                    MyApp.instances.getDaoSession().getAlarmDao().delete(data);
                    emitter.onNext(true);
                } catch (Exception e) {
                    emitter.onNext(false);
                }
            }
        });
        //建立连接
        return observable;
    }

    /**
     * 显示CustomDialog
     */
    private void showDialog(final Alarm data) {
        final CustomDialog dialog = new CustomDialog(mContext);
        dialog.show();
        dialog.setHintText("确定要删除吗？");
        dialog.setLeftButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setRightButton("删除", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(data).subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean value) {
                        if (value) {
                            remove(data);
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

            }
        });
    }

    private int getAlarmId() {
        int id = 0;
        List<Alarm> alarmList = MyApp.instances.getDaoSession().getAlarmDao().queryBuilder().orderDesc(AlarmDao.Properties.Id).list();
        if (alarmList != null) {
            id = alarmList.get(0).getId().intValue();
        }
        return id;
    }

    private void updateAlarm(Alarm data, int position) {
        MyApp.instances.getDaoSession().getAlarmDao().update(data);
        if (!data.getIsOpen()) {
            AlarmManagerUtil.cancelAlarm(mContext, "", getAlarmId());
        }
//        notifyDataSetChanged();
    }
}
