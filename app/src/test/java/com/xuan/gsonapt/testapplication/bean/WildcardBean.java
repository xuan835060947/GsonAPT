package com.xuan.gsonapt.testapplication.bean;

/**
 * Created by chenxiaoxuan1 on 17/2/14.
 */

public class WildcardBean<T> {
    T t;

    public WildcardBean() {
    }

    public WildcardBean(T t) {
        this.t = t;
    }
}
