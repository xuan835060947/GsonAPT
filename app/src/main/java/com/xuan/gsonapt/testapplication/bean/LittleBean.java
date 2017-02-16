package com.xuan.gsonapt.testapplication.bean;

import com.xuan.gsonapt.JsonBean;

/**
 * Created by chenxiaoxuan1 on 17/2/15.
 */
@JsonBean
public class LittleBean {
    String littleBeanName;

    public String getLittleBeanName() {
        return littleBeanName;
    }

    public void setLittleBeanName(String littleBeanName) {
        this.littleBeanName = littleBeanName;
    }

    public LittleBean() {
    }

    public LittleBean(String littleBeanName) {
        this.littleBeanName = littleBeanName;
    }
}
