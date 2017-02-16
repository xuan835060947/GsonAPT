package com.xuan.gsonapt.testapplication.bean;

import com.xuan.gsonapt.JsonBean;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by chenxiaoxuan1 on 17/2/13.
 */
@JsonBean
public class TestBean {
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
    OtherBean otherBean;
    List<OtherBean> otherBeanList;
    Map<Integer, OtherBean> integerOtherBeanMap;
    Set<String> stringSet;
    Map<OtherBean,String> complexKeyMap;
    List<SuperBean> superBeanList;

    public Map<Integer, OtherBean> getIntegerOtherBeanMap() {
        return integerOtherBeanMap;
    }

    public void setIntegerOtherBeanMap(Map<Integer, OtherBean> integerOtherBeanMap) {
        this.integerOtherBeanMap = integerOtherBeanMap;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public Character getaCharacter() {
        return aCharacter;
    }

    public void setaCharacter(Character aCharacter) {
        this.aCharacter = aCharacter;
    }

    public byte getBy() {
        return by;
    }

    public void setBy(byte by) {
        this.by = by;
    }

    public Byte getaByte() {
        return aByte;
    }

    public void setaByte(Byte aByte) {
        this.aByte = aByte;
    }

    public short getS() {
        return s;
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

    public OtherBean getOtherBean() {
        return otherBean;
    }

    public void setOtherBean(OtherBean otherBean) {
        this.otherBean = otherBean;
    }

    public List<OtherBean> getOtherBeanList() {
        return otherBeanList;
    }

    public void setOtherBeanList(List<OtherBean> otherBeanList) {
        this.otherBeanList = otherBeanList;
    }

    public Set<String> getStringSet() {
        return stringSet;
    }

    public void setStringSet(Set<String> stringSet) {
        this.stringSet = stringSet;
    }

    public Map<OtherBean, String> getComplexKeyMap() {
        return complexKeyMap;
    }

    public void setComplexKeyMap(Map<OtherBean, String> complexKeyMap) {
        this.complexKeyMap = complexKeyMap;
    }

    public List<SuperBean> getSuperBeanList() {
        return superBeanList;
    }

    public void setSuperBeanList(List<SuperBean> superBeanList) {
        this.superBeanList = superBeanList;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        TestBean testBean = (TestBean) o;
//
//        if (b != testBean.b) return false;
//        if (c != testBean.c) return false;
//        if (by != testBean.by) return false;
//        if (s != testBean.s) return false;
//        if (i != testBean.i) return false;
//        if (l != testBean.l) return false;
//        if (Float.compare(testBean.f, f) != 0) return false;
//        if (Double.compare(testBean.d, d) != 0) return false;
//        if (aBoolean != null ? !aBoolean.equals(testBean.aBoolean) : testBean.aBoolean != null)
//            return false;
//        if (aCharacter != null ? !aCharacter.equals(testBean.aCharacter) : testBean.aCharacter != null)
//            return false;
//        if (aByte != null ? !aByte.equals(testBean.aByte) : testBean.aByte != null) return false;
//        if (aShort != null ? !aShort.equals(testBean.aShort) : testBean.aShort != null)
//            return false;
//        if (aInteger != null ? !aInteger.equals(testBean.aInteger) : testBean.aInteger != null)
//            return false;
//        if (aLong != null ? !aLong.equals(testBean.aLong) : testBean.aLong != null) return false;
//        if (aFloat != null ? !aFloat.equals(testBean.aFloat) : testBean.aFloat != null)
//            return false;
//        if (aDouble != null ? !aDouble.equals(testBean.aDouble) : testBean.aDouble != null)
//            return false;
//        if (str != null ? !str.equals(testBean.str) : testBean.str != null) return false;
//        if (otherBean != null ? !otherBean.equals(testBean.otherBean) : testBean.otherBean != null)
//            return false;
//        if (otherBeanList != null ? !otherBeanList.equals(testBean.otherBeanList) : testBean.otherBeanList != null)
//            return false;
//        if (integerOtherBeanMap != null) {
//            if (testBean.integerOtherBeanMap == null) return false;
//            if (integerOtherBeanMap.size() != testBean.integerOtherBeanMap.size()) {
//                return false;
//            }
//            for (Map.Entry<Integer, OtherBean> entry : integerOtherBeanMap.entrySet()) {
//                if (!entry.getValue().equals(testBean.integerOtherBeanMap.get(entry.getKey()))) {
//                    return false;
//                }
//            }
//
//        }else {
//            if(testBean.integerOtherBeanMap != null){
//                return false;
//            }
//        }
//        return stringSet != null ? stringSet.equals(testBean.stringSet) : testBean.stringSet == null;
//
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestBean testBean = (TestBean) o;

        if (b != testBean.b) return false;
        if (c != testBean.c) return false;
        if (by != testBean.by) return false;
        if (s != testBean.s) return false;
        if (i != testBean.i) return false;
        if (l != testBean.l) return false;
        if (Float.compare(testBean.f, f) != 0) return false;
        if (Double.compare(testBean.d, d) != 0) return false;
        if (aBoolean != null ? !aBoolean.equals(testBean.aBoolean) : testBean.aBoolean != null)
            return false;
        if (aCharacter != null ? !aCharacter.equals(testBean.aCharacter) : testBean.aCharacter != null)
            return false;
        if (aByte != null ? !aByte.equals(testBean.aByte) : testBean.aByte != null) return false;
        if (aShort != null ? !aShort.equals(testBean.aShort) : testBean.aShort != null)
            return false;
        if (aInteger != null ? !aInteger.equals(testBean.aInteger) : testBean.aInteger != null)
            return false;
        if (aLong != null ? !aLong.equals(testBean.aLong) : testBean.aLong != null) return false;
        if (aFloat != null ? !aFloat.equals(testBean.aFloat) : testBean.aFloat != null)
            return false;
        if (aDouble != null ? !aDouble.equals(testBean.aDouble) : testBean.aDouble != null)
            return false;
        if (str != null ? !str.equals(testBean.str) : testBean.str != null) return false;
        if (otherBean != null ? !otherBean.equals(testBean.otherBean) : testBean.otherBean != null)
            return false;
        if (otherBeanList != null ? !otherBeanList.equals(testBean.otherBeanList) : testBean.otherBeanList != null)
            return false;
        if (integerOtherBeanMap != null ? !integerOtherBeanMap.equals(testBean.integerOtherBeanMap) : testBean.integerOtherBeanMap != null)
            return false;
        if (stringSet != null ? !stringSet.equals(testBean.stringSet) : testBean.stringSet != null)
            return false;
        if (complexKeyMap != null ? !complexKeyMap.equals(testBean.complexKeyMap) : testBean.complexKeyMap != null)
            return false;
        return superBeanList != null ? superBeanList.equals(testBean.superBeanList) : testBean.superBeanList == null;

    }
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (b ? 1 : 0);
        result = 31 * result + (aBoolean != null ? aBoolean.hashCode() : 0);
        result = 31 * result + (int) c;
        result = 31 * result + (aCharacter != null ? aCharacter.hashCode() : 0);
        result = 31 * result + (int) by;
        result = 31 * result + (aByte != null ? aByte.hashCode() : 0);
        result = 31 * result + (int) s;
        result = 31 * result + (aShort != null ? aShort.hashCode() : 0);
        result = 31 * result + i;
        result = 31 * result + (aInteger != null ? aInteger.hashCode() : 0);
        result = 31 * result + (int) (l ^ (l >>> 32));
        result = 31 * result + (aLong != null ? aLong.hashCode() : 0);
        result = 31 * result + (f != +0.0f ? Float.floatToIntBits(f) : 0);
        result = 31 * result + (aFloat != null ? aFloat.hashCode() : 0);
        temp = Double.doubleToLongBits(d);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (aDouble != null ? aDouble.hashCode() : 0);
        result = 31 * result + (str != null ? str.hashCode() : 0);
        result = 31 * result + (otherBean != null ? otherBean.hashCode() : 0);
        result = 31 * result + (otherBeanList != null ? otherBeanList.hashCode() : 0);
        result = 31 * result + (integerOtherBeanMap != null ? integerOtherBeanMap.hashCode() : 0);
        result = 31 * result + (stringSet != null ? stringSet.hashCode() : 0);
        return result;
    }
}
