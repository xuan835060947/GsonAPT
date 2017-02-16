package com.xuan.gsonapt.testapplication.bean.testtime;

import com.xuan.gsonapt.JsonBean;

import java.util.List;

/**
 * Created by chenxiaoxuan1 on 17/2/16.
 */
@JsonBean
public class BigBean {
    boolean b;
    Boolean aBoolean;
    char c;
    Character aCharacter;
    byte by;
    Byte aByte;
    short s;
    Short aShort;
    int i;
    Integer aInteger;
    long l;
    Long aLong;
    float f;
    Float aFloat;
    double d;
    Double aDouble;
    String str;

    List<SmallBean> smallBeanList;

    public List<SmallBean> getLittleBeanList() {
        return smallBeanList;
    }

    public void setLittleBeanList(List<SmallBean> littleBeanList) {
        this.smallBeanList = littleBeanList;
    }
}
