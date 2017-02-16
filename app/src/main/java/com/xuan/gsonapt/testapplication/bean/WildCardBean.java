package com.xuan.gsonapt.testapplication.bean;

/**
 * Created by chenxiaoxuan1 on 17/2/16.
 */

public class WildCardBean<T,V> {
    T t;
    V v;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public WildCardBean() {
    }

    public V getV() {
        return v;
    }

    public void setV(V v) {
        this.v = v;
    }

    public WildCardBean(T t, V v) {
        this.t = t;
        this.v = v;
    }
}
