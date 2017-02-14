package com.xuan.gsonapt.testapplication.bean;

/**
 * Created by chenxiaoxuan1 on 17/2/5.
 */

public class OtherBean implements Comparable {
    String otherName;

    public OtherBean() {
    }

    public OtherBean(String otherName) {
        this.otherName = otherName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OtherBean otherBean = (OtherBean) o;

        return otherName != null ? otherName.equals(otherBean.otherName) : otherBean.otherName == null;

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
