package com.xuan.gsonapt.complier.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import static com.xuan.gsonapt.complier.TypeJsonKind.BOOLEAN;

/**
 * Created by chenxiaoxuan1 on 17/2/10.
 */

public class CreateCodeUtil {
    private static Map<String, Integer> nameMap = new HashMap<>();

    public static String getName( String itemType) {
        Integer num = nameMap.get(itemType);
        num = num == null ? 0 : num;
        nameMap.put(itemType, ++num);
        return itemType.toLowerCase() + num;
    }
    /**
     * create the objName.field or objName.getXxx();
     *
     * @param field
     * @param obgName
     */
    public static GetMethod getGetMethod(String obgName, Element objElement, VariableElement field) {
        List<ExecutableElement> allFields = ElementUtil.getAllMethod(objElement);

        String getMethodName = "get" + captureName(field.getSimpleName().toString());
        String boolGetMethodName = null;
        if (ElementUtil.getJsonKind(field).equals(BOOLEAN)) {
            if (field.toString().length() >= 2 && field.toString().substring(0, 2).equalsIgnoreCase("is")) {
                boolGetMethodName = "is" + captureName(field.toString().substring(2));
            } else {
                boolGetMethodName = "is" + captureName(field.toString());
            }
        }
        for (ExecutableElement executableElement : allFields) {
            String methodNamNoParam = executableElement.getSimpleName().toString();
            String returnTypeName = executableElement.getReturnType().toString();
            String fieldType = field.asType().toString();
            if (!returnTypeName.equals(fieldType)) {
                continue;
            }
            if (methodNamNoParam.equals(getMethodName)
                    && executableElement.getParameters().size() == 0) {
                return new GetMethod(obgName + "." + methodNamNoParam + "()");
            }
            if (boolGetMethodName != null) {
                if (methodNamNoParam.equals(boolGetMethodName)
                        && executableElement.getParameters().size() == 0) {
                    return new GetMethod(obgName + "." + methodNamNoParam + "()");
                }
            }
        }
        return new GetMethod(obgName + "." + field.getSimpleName().toString());
    }

    /**
     * create the objName.field = value or objName.setXxx(value);
     *
     * @param field
     * @param obgName
     */
    public static SetMethod getSetMethod(String obgName, Element objElement, VariableElement field) {
        List<ExecutableElement> allFields = ElementUtil.getAllMethod(objElement);
        String setMethodName = "set" + captureName(field.getSimpleName().toString());
        String boolSetMethodName = null;
        if (ElementUtil.getJsonKind(field).equals(BOOLEAN)) {
            if (field.toString().length() >= 2 && field.toString().substring(0, 2).equalsIgnoreCase("is")) {
                boolSetMethodName = "set" + captureName(field.toString().substring(2));
            } else {
                boolSetMethodName = "set" + captureName(field.toString());
            }
        }
        for (ExecutableElement executableElement : allFields) {
            String methodNamNoParam = executableElement.getSimpleName().toString();
            if (methodNamNoParam.equals(setMethodName)
                    && executableElement.getParameters().size() == 1) {
                return new SetMethod(obgName + "." + setMethodName + "( %s )");
            }
            if (boolSetMethodName != null) {
                if (methodNamNoParam.equals(setMethodName)
                        && executableElement.getParameters().size() == 1) {
                    return new SetMethod(obgName + "." + setMethodName + "( %s )");
                }
            }
        }
        return new SetMethod(obgName + "." + field + " = %s");
    }

    private static String captureName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    public static class GetMethod {
        String methedName;

        public GetMethod(String methedName) {
            this.methedName = methedName;
        }

        public String getGetMethodCode() {
            return methedName;
        }
    }

    public static class SetMethod {
        String methedName;

        public SetMethod(String methedName) {
            this.methedName = methedName;
        }

        public String getSetMethodCode(String valueName) {
            return String.format(methedName, valueName) + "; \n";
        }
    }

}
