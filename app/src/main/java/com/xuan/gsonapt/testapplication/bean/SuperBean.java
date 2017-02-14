package com.xuan.gsonapt.testapplication.bean;

import com.xuan.gsonapt.JsonBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by chenxiaoxuan1 on 17/1/12.
 */
@JsonBean
public class SuperBean {
    boolean isSuper;
    String superName = "super";
    long l;
    Long aLong;
    List<OtherBean> otherBeens = new ArrayList<>();
    ArrayList<OtherBean> arrayList;
    HashMap<String, OtherBean> hashMap = new HashMap<>();
    Set<Integer> hashSet = new HashSet();
    LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet();
    Map<OtherBean,String> otherBeanStringMap= new HashMap<>();

    public boolean isSuper() {
        return isSuper;
    }

    public void setSuper(boolean aSuper) {
        isSuper = aSuper;
    }

    public String getSuperName() {
        return superName;
    }

    public void setSuperName(String superName) {
        this.superName = superName;
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

    public List<OtherBean> getOtherBeens() {
        return otherBeens;
    }

    public void setOtherBeens(List<OtherBean> otherBeens) {
        this.otherBeens = otherBeens;
    }

    public ArrayList<OtherBean> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<OtherBean> arrayList) {
        this.arrayList = arrayList;
    }

    public HashMap<String, OtherBean> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, OtherBean> hashMap) {
        this.hashMap = hashMap;
    }
}
