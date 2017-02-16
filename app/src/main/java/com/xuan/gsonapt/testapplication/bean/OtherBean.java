package com.xuan.gsonapt.testapplication.bean;

import com.xuan.gsonapt.JsonBean;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by chenxiaoxuan1 on 17/2/5.
 */
@JsonBean
public class OtherBean implements Comparable {
    String otherName;
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
    LittleBean otherBean;
    List<LittleBean> otherBeanList;
    Map<Integer, LittleBean> integerOtherBeanMap;
    Set<String> stringSet;
    Map<LittleBean,String> complexKeyMap;
    List<LittleBean> superBeanList;

    public OtherBean() {
    }

    public OtherBean(String otherName) {
        this.otherName = otherName;
    }

    public byte getBy() {
        return by;
    }

    public Byte getaByte() {
        return aByte;
    }

    public void setaByte(Byte aByte) {
        this.aByte = aByte;
    }

    public void setS(short s) {
        this.s = s;
    }

    public Short getaShort() {
        return aShort;
    }

    public void setaShort(Short aShort) {
        this.aShort = aShort;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public Integer getaInteger() {
        return aInteger;
    }

    public void setaInteger(Integer aInteger) {
        this.aInteger = aInteger;
    }

    public long getL() {
        return l;
    }

    public void setL(long l) {
        this.l = l;
    }

    public Long getaLong() {
        return aLong;
    }

    public void setaLong(Long aLong) {
        this.aLong = aLong;
    }

    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }

    public Float getaFloat() {
        return aFloat;
    }

    public void setaFloat(Float aFloat) {
        this.aFloat = aFloat;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public Double getaDouble() {
        return aDouble;
    }

    public void setaDouble(Double aDouble) {
        this.aDouble = aDouble;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public LittleBean getOtherBean() {
        return otherBean;
    }

    public void setOtherBean(LittleBean otherBean) {
        this.otherBean = otherBean;
    }

    public List<LittleBean> getOtherBeanList() {
        return otherBeanList;
    }

    public void setOtherBeanList(List<LittleBean> otherBeanList) {
        this.otherBeanList = otherBeanList;
    }

    public Map<Integer, LittleBean> getIntegerOtherBeanMap() {
        return integerOtherBeanMap;
    }

    public void setIntegerOtherBeanMap(Map<Integer, LittleBean> integerOtherBeanMap) {
        this.integerOtherBeanMap = integerOtherBeanMap;
    }

    public Set<String> getStringSet() {
        return stringSet;
    }

    public void setStringSet(Set<String> stringSet) {
        this.stringSet = stringSet;
    }

    public Map<LittleBean, String> getComplexKeyMap() {
        return complexKeyMap;
    }

    public void setComplexKeyMap(Map<LittleBean, String> complexKeyMap) {
        this.complexKeyMap = complexKeyMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OtherBean otherBean1 = (OtherBean) o;

        if (b != otherBean1.b) return false;
        if (c != otherBean1.c) return false;
        if (by != otherBean1.by) return false;
        if (s != otherBean1.s) return false;
        if (i != otherBean1.i) return false;
        if (l != otherBean1.l) return false;
        if (Float.compare(otherBean1.f, f) != 0) return false;
        if (Double.compare(otherBean1.d, d) != 0) return false;
        if (otherName != null ? !otherName.equals(otherBean1.otherName) : otherBean1.otherName != null)
            return false;
        if (aBoolean != null ? !aBoolean.equals(otherBean1.aBoolean) : otherBean1.aBoolean != null)
            return false;
        if (aCharacter != null ? !aCharacter.equals(otherBean1.aCharacter) : otherBean1.aCharacter != null)
            return false;
        if (aByte != null ? !aByte.equals(otherBean1.aByte) : otherBean1.aByte != null)
            return false;
        if (aShort != null ? !aShort.equals(otherBean1.aShort) : otherBean1.aShort != null)
            return false;
        if (aInteger != null ? !aInteger.equals(otherBean1.aInteger) : otherBean1.aInteger != null)
            return false;
        if (aLong != null ? !aLong.equals(otherBean1.aLong) : otherBean1.aLong != null)
            return false;
        if (aFloat != null ? !aFloat.equals(otherBean1.aFloat) : otherBean1.aFloat != null)
            return false;
        if (aDouble != null ? !aDouble.equals(otherBean1.aDouble) : otherBean1.aDouble != null)
            return false;
        if (str != null ? !str.equals(otherBean1.str) : otherBean1.str != null) return false;
        if (otherBean != null ? !otherBean.equals(otherBean1.otherBean) : otherBean1.otherBean != null)
            return false;
        if (otherBeanList != null ? !otherBeanList.equals(otherBean1.otherBeanList) : otherBean1.otherBeanList != null)
            return false;
        if (integerOtherBeanMap != null ? !integerOtherBeanMap.equals(otherBean1.integerOtherBeanMap) : otherBean1.integerOtherBeanMap != null)
            return false;
        if (stringSet != null ? !stringSet.equals(otherBean1.stringSet) : otherBean1.stringSet != null)
            return false;
        if (complexKeyMap != null ? !complexKeyMap.equals(otherBean1.complexKeyMap) : otherBean1.complexKeyMap != null)
            return false;
        return superBeanList != null ? superBeanList.equals(otherBean1.superBeanList) : otherBean1.superBeanList == null;

    }

    @Override
    public int hashCode() {
        return otherName != null ? otherName.hashCode() : 0;
    }

    @Override
    public int compareTo(Object another) {
        if (another == null) {
            return 1;
        }

        OtherBean anotherBean = (OtherBean) another;
        if (otherName == null) {
            if (anotherBean.otherName == null) {
                return 0;
            } else {
                return -1;
            }
        }
        return otherName.compareTo(anotherBean.otherName);
    }
}
