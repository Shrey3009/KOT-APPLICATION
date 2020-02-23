package com.alfredbase.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;


public class RxBus {

    public static final String RX_MSG_1 = "RX_MSG_1";
    public static final String RX_MSG_2 = "RX_MSG_2";
    public static final String RX_MSG_3 = "RX_MSG_3";
    public static final String RECEIVE_IP_ACTION = "RECEIVE_IP_ACTION";
    public static final String RX_REFRESH_TABLE = "RX_REFRESH_TABLE";
    public static final String RX_REFRESH_STOCK = "RX_REFRESH_STOCK";
    public static final String RX_GET_STOCK = "RX_GET_STOCK";
    public static final String RX_REFRESH_ORDER = "RX_REFRESH_ORDER";
    public static final String RX_WIFI_STORE = "RX_WIFI_STORE";

    public static final String showWelcom = "SHOW_WELCOM_VIEW";
    public static final String showOrder = "SHOW_ORDER_VIEW";
    public static final String RX_TRAIN = "RX_TRAIN";
    private static RxBus instance;
    private ConcurrentHashMap<Object, List<Subject>> maps = new ConcurrentHashMap<Object, List<Subject>>();

    private RxBus() {

    }

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    /**
     * 注册信息
     * @param tag   事件标识
     * @param <T>
     * @return
     */
    public <T> Observable<T> register(@NonNull String tag) {
        List<Subject> subjects = maps.get(tag);
        if (subjects == null) {
            subjects = new ArrayList<Subject>();
            maps.put(tag, subjects);
        }
        Subject<T, T> subject = PublishSubject.<T>create();
        subjects.add(subject);
        return subject;
    }

    /**
     * 解除注册
     * @param tag
     * @param observable
     */
    public void unregister(@NonNull String tag, @NonNull Observable observable) {
        List<Subject> subjects = maps.get(tag);
        if (subjects != null) {
            subjects.remove((Subject) observable);
            if (subjects.isEmpty()) {
                maps.remove(tag);
            }
        }
    }

    /**
     * 发送事件
     *
     * @param o
     */
    public void post(@NonNull String tag, Object o) {
        try {
            List<Subject> subjects = maps.get(tag);
            if (subjects != null && !subjects.isEmpty()) {
                for (Subject s : subjects) {
//                s.delay(100, TimeUnit.MILLISECONDS);
                    s.onNext(o);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isEmpty(Collection collection) {

        return null == collection || collection.isEmpty();

    }
}
