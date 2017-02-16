package com.xuan.gsonapt.testapplication.bean;

/**
 * Created by chenxiaoxuan1 on 17/2/15.
 */

public class CycleBean {
    CycleBean cycleBean;
    String name;

    public CycleBean() {
    }

    public CycleBean(String name) {
        this.name = name;
    }
}

