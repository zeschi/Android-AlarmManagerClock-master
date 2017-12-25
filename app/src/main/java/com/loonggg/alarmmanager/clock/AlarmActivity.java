package com.loonggg.alarmmanager.clock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.loonggg.alarmmanager.clock.adapter.AlarmAdapter;
import com.loonggg.alarmmanager.clock.adapter.CommonRecycleAdapter;
import com.loonggg.alarmmanager.clock.adapter.RecycleViewHolder;
import com.loonggg.alarmmanager.clock.bean.Alarm;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/24.
 */

public class AlarmActivity extends AppCompatActivity implements CommonRecycleAdapter.OnItemClickListener {


    private RecyclerView recyclerView;

    private AlarmAdapter alarmAdapter;
    private List<Alarm> alarmList = new ArrayList<>();

    private FloatingActionButton floatingActionButton;

    public final static int REQUESE_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        initView();
        initData();
    }

    /**
     * 初始化
     */
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_alarm);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_alarm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        alarmAdapter = new AlarmAdapter(this, alarmList, R.layout.item_alarm);
        recyclerView.setAdapter(alarmAdapter);
        alarmAdapter.setOnItemClickLitener(this);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AlarmActivity.this, MainActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (alarmAdapter != null) {
            initData();
        }
    }

    private void initData() {
        getAlarmList().subscribe(new Observer<List<Alarm>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Alarm> value) {
                alarmList = value;
                alarmAdapter.setData(alarmList);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onItemClick(RecycleViewHolder holder, View view, int position) {
        Intent alarmIntent = new Intent(this, MainActivity.class);
        alarmIntent.putExtra("Alarm", alarmList.get(position));
        startActivity(alarmIntent);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    private Observable<List<Alarm>> getAlarmList() {
        Observable<List<Alarm>> observable = Observable.create(new ObservableOnSubscribe<List<Alarm>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Alarm>> emitter) throws Exception {
                List<Alarm> alarmList = MyApp.instances.getDaoSession().getAlarmDao().queryBuilder().build().list();
                emitter.onNext(alarmList);
            }
        });
        //建立连接
        return observable;
    }
}
