package com.xuan.gsonapt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by chenxiaoxuan1 on 17/2/5.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface JsonBean {
}
