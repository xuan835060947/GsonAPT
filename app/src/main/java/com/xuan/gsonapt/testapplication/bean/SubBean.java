package com.xuan.gsonapt.testapplication.bean;

import com.xuan.gsonapt.JsonBean;

/**
 * Created by chenxiaoxuan1 on 17/2/13.
 */
@JsonBean
public class SubBean extends SuperBean {
    String subName;

    public SubBean() {
    }

    public SubBean(String subName) {
        this.subName = subName;
    }
}
