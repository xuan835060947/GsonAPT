package com.xuan.gsonapt.testapplication.bean;

import com.xuan.gsonapt.JsonBean;

/**
 * Created by chenxiaoxuan1 on 17/2/14.
 */

@JsonBean
public class SubTestBean extends TestBean {
    String subName;

    public SubTestBean() {
    }

    public SubTestBean(String subName) {
        this.subName = subName;
    }
}
