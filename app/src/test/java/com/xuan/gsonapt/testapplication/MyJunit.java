package com.xuan.gsonapt.testapplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.reflect.TypeToken;
import com.xuan.gsonapt.GsonAPT;
import com.xuan.gsonapt.testapplication.bean.OtherBean;
import com.xuan.gsonapt.testapplication.bean.SubBean;
import com.xuan.gsonapt.testapplication.bean.SubBean2;
import com.xuan.gsonapt.testapplication.bean.SubTestBean;
import com.xuan.gsonapt.testapplication.bean.SuperBean;
import com.xuan.gsonapt.testapplication.bean.TestBean;
import com.xuan.gsonapt.testapplication.bean.WildCardBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.WeakHashMap;

import static com.xuan.gsonapt.GsonAPT.toJson;

public class MyJunit {
    public static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().create();
    ToJson[] toJsons = new ToJson[2];
    FromJson[] fromJsons = new FromJson[2];

    @Before
    public void before() {
        GsonAPT.setGson(new GsonBuilder().enableComplexMapKeySerialization().create());
        toJsons[0] = new GsonToJson();
        toJsons[1] = new GsonAPTToJson();

        fromJsons[0] = new GsonFromJson();
        fromJsons[1] = new GsonAPTFromJson();
    }

    @Test
    public void test() {
        TestBean testBean = new TestBean();
        testTestBean(testBean);

        Map<Integer, OtherBean> map = new HashMap<>();
        map.put(9, new OtherBean(""));
        map.put(1, new OtherBean(null));
//        map.put(null,new OtherBean("key null")); Gson is not support because some map are not support the null key
        testBean.setIntegerOtherBeanMap(map);
        testTestBean(testBean);

        Set<String> stringSet = new HashSet<>();
        stringSet.add("hello");
        stringSet.add(null);
        stringSet.add("world");
        testBean.setStringSet(stringSet);
        testTestBean(testBean);

        List<OtherBean> list = new ArrayList<>();
        list.add(new OtherBean());
        list.add(null);
        list.add(new OtherBean("hello"));
        testBean.setOtherBeanList(list);
        testTestBean(testBean);
        toJsonFromJson(list, new TypeToken<List<OtherBean>>() {
        }.getType());
        toJsonFromJson(list, new TypeToken<ArrayList<OtherBean>>() {
        }.getType());
        toJsonFromJson(list, new TypeToken<LinkedList<OtherBean>>() {
        }.getType());
        toJsonFromJson(null, new TypeToken<LinkedList<OtherBean>>() {
        }.getType());


        Map<OtherBean, String> complexKeyMap = new HashMap<>();
        complexKeyMap.put(new OtherBean("hello"), "hello");
        complexKeyMap.put(new OtherBean("world"), "world");
//        complexKeyMap.put(null, "world"); Gson is not support because some map are not support the null key
        testBean.setComplexKeyMap(complexKeyMap);
        testTestBean(testBean);
        toJsonFromJson(complexKeyMap, new TypeToken<Map<OtherBean, String>>() {
        }.getType());
        toJsonFromJson(complexKeyMap, new TypeToken<HashMap<OtherBean, String>>() {
        }.getType());
        toJsonFromJson(complexKeyMap, new TypeToken<LinkedHashTreeMap<OtherBean, String>>() {
        }.getType());
        toJsonFromJson(complexKeyMap, new TypeToken<LinkedHashMap<OtherBean, String>>() {
        }.getType());
        toJsonFromJson(complexKeyMap, new TypeToken<Hashtable<OtherBean, String>>() {
        }.getType());
        toJsonFromJson(complexKeyMap, new TypeToken<WeakHashMap<OtherBean, String>>() {
        }.getType());
        toJsonFromJson(complexKeyMap, new TypeToken<TreeMap<OtherBean, String>>() {
        }.getType());
        toJsonFromJson(null, new TypeToken<TreeMap<OtherBean, String>>() {
        }.getType());


        Set<Integer> treeSet = new TreeSet<>();
        treeSet.add(100);
        treeSet.add(101);
//        treeSet.add(null); Some Set are not support
        toJsonFromJson(treeSet, new TypeToken<TreeSet<Integer>>() {
        }.getType());
        toJsonFromJson(treeSet, new TypeToken<Set<Integer>>() {
        }.getType());
        toJsonFromJson(treeSet, new TypeToken<HashSet<Integer>>() {
        }.getType());
        toJsonFromJson(null, new TypeToken<HashSet<Integer>>() {
        }.getType());

        Set<TestBean> testBeanSet = new HashSet<>();
        testBeanSet.add(new SubTestBean("subName 1"));
        testBeanSet.add(new SubTestBean("subName 2"));
        PrintUtil.print(GSON.toJson(testBeanSet));
        PrintUtil.print(toJson(testBeanSet));

        List<SuperBean> superBeanList = new ArrayList<>();
        superBeanList.add(new SubBean("sub bean"));
        superBeanList.add(new SubBean2("sub bean"));
        testBean.setSuperBeanList(superBeanList);
        PrintUtil.print(GSON.toJson(testBean));
        PrintUtil.print(toJson(testBean));
        String string = GSON.toJson(testBean);
        testBean = GSON.fromJson(string, TestBean.class);
        PrintUtil.print(GSON.toJson(testBean));


        WildCardBean<String, OtherBean> wildCardBean = new WildCardBean<>("", new OtherBean());
        String str = GsonAPT.toJson(wildCardBean);
        GsonAPT.fromJson(str, new TypeToken<WildCardBean<String, OtherBean>>() {
        }.getType());
    }

    public void testTestBean(TestBean testBean) {
        TestBean oldBean = testBean;
        Random random = new Random(System.nanoTime());
        String str = null;
        for (int i = 0; i < 1000; i++) {
            int r = random.nextInt(2);
            str = toJsons[r].toJson(testBean);
            r = random.nextInt(2);
            testBean = fromJsons[r].fromJson(str, TestBean.class);

            if (!oldBean.equals(testBean)) {
                oldBean.equals(testBean);
            }
            Assert.assertEquals(oldBean, testBean);

        }
    }

    public void toJsonFromJson(Object object, Type type) {
        Object old = object;
        String str = GSON.toJson(object);
        object = GsonAPT.fromJson(str, type);
        Assert.assertEquals(old, object);
        str = toJson(object);
        object = GSON.fromJson(str, type);
        Assert.assertEquals(old, object);
    }

}

interface ToJson {
    String toJson(Object object);
}

interface FromJson {
    <T> T fromJson(String jsonStr, Type type);
}

class GsonToJson implements ToJson {
    Object object;

    @Override
    public String toJson(Object object) {
//        PrintUtil.print(" Gson toJson");
        this.object = object;
        return MyJunit.GSON.toJson(object);
    }
}

class GsonAPTToJson implements ToJson {
    Object object;

    @Override
    public String toJson(Object object) {
//        PrintUtil.print(" APT toJson");
        this.object = object;
        return GsonAPT.toJson(object);
    }
}

class GsonFromJson implements FromJson {

    @Override
    public <T> T fromJson(String jsonStr, Type type) {
        PrintUtil.print(" Gson fromJson");

        T t = MyJunit.GSON.fromJson(jsonStr, type);
        return t;
    }
}

class GsonAPTFromJson implements FromJson {

    @Override
    public <T> T fromJson(String jsonStr, Type type) {
        PrintUtil.print(" APT fromJson");

        T t = GsonAPT.fromJson(jsonStr, type);
        return t;
    }
}