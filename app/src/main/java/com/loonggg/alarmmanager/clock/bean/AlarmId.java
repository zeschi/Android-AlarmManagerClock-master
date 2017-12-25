package com.loonggg.alarmmanager.clock.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/12/24.
 */
@Entity
public class AlarmId {

    @Id
    private Long id;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1786365213)
    public AlarmId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1828761131)
    public AlarmId() {
    }



}

