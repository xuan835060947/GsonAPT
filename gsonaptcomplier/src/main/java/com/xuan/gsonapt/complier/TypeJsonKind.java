package com.xuan.gsonapt.complier;

/**
 * Created by chenxiaoxuan1 on 17/2/6.
 */

public enum TypeJsonKind {
    BOOLEAN,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    STRING,
    NORMAL_BEAN,
    ARRAY,
    Set,
    LIST,
    MAP;

    public boolean isValue(){
        switch (this.ordinal()){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return true;
        }
        return false;
    }
}
