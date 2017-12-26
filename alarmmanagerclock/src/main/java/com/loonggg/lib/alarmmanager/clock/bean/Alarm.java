package com.loonggg.lib.alarmmanager.clock.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/24.
 */
@Entity
public class Alarm implements Serializable {

    @Id(autoincrement = true)
    private Long id;
    private String alarmTime;
    private int alarmType;
    private String alarmTypeName;
    private boolean isOpen;
    private int cycle;
    private boolean isLater;
    private long timeInMillis;
    private int weekLengths;

    public int getWeekLengths() {
        return this.weekLengths;
    }

    public void setWeekLengths(int weekLengths) {
        this.weekLengths = weekLengths;
    }

    public long getTimeInMillis() {
        return this.timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public boolean getIsLater() {
        return this.isLater;
    }

    public void setIsLater(boolean isLater) {
        this.isLater = isLater;
    }

    public int getCycle() {
        return this.cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public boolean getIsOpen() {
        return this.isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getAlarmTypeName() {
        return this.alarmTypeName;
    }

    public void setAlarmTypeName(String alarmTypeName) {
        this.alarmTypeName = alarmTypeName;
    }

    public int getAlarmType() {
        return this.alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmTime() {
        return this.alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1466325359)
    public Alarm(Long id, String alarmTime, int alarmType, String alarmTypeName,
                 boolean isOpen, int cycle, boolean isLater, long timeInMillis,
                 int weekLengths) {
        this.id = id;
        this.alarmTime = alarmTime;
        this.alarmType = alarmType;
        this.alarmTypeName = alarmTypeName;
        this.isOpen = isOpen;
        this.cycle = cycle;
        this.isLater = isLater;
        this.timeInMillis = timeInMillis;
        this.weekLengths = weekLengths;
    }

    @Generated(hash = 1972324134)
    public Alarm() {
    }


}
