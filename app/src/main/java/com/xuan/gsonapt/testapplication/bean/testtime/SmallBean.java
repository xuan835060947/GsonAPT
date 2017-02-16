package com.xuan.gsonapt.testapplication.bean.testtime;

import com.xuan.gsonapt.JsonBean;
import com.xuan.gsonapt.testapplication.bean.LittleBean;

/**
 * Created by chenxiaoxuan1 on 17/2/16.
 */

@JsonBean
public class SmallBean {
    boolean b;
    Boolean aBoolean = true;
    char c;
    Character aCharacter = ' ';
    byte by;
    Byte aByte = 100;
    short s;
    Short aShort = 100;
    int i;
    Integer aInteger= 100;
    long l;
    Long aLong = 100l;
    float f;
    Float aFloat = 100f;
    double d;
    Double aDouble = 100d;
    String str = "small";
    LittleBean littleBean = new LittleBean("哈哈");

    public SmallBean() {
    }

    public SmallBean(String str, boolean b, Boolean aBoolean, char c, Character aCharacter, byte by, Byte aByte, short s, Short aShort, int i, Integer aInteger, long l, Long aLong, float f, Float aFloat, double d, Double aDouble) {
        this.str = str;
        this.b = b;
        this.aBoolean = aBoolean;
        this.c = c;
        this.aCharacter = aCharacter;
        this.by = by;
        this.aByte = aByte;
        this.s = s;
        this.aShort = aShort;
        this.i = i;
        this.aInteger = aInteger;
        this.l = l;
        this.aLong = aLong;
        this.f = f;
        this.aFloat = aFloat;
        this.d = d;
        this.aDouble = aDouble;
    }
}
