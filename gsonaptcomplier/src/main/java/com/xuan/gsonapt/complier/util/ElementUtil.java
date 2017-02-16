package com.xuan.gsonapt.complier.util;

import com.xuan.gsonapt.complier.GsonAPTProcessor;
import com.xuan.gsonapt.complier.TypeJsonKind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import static com.xuan.gsonapt.complier.GsonAPTProcessor.ELEMENT_UTILS;
import static com.xuan.gsonapt.complier.GsonAPTProcessor.TYPE_UTILS;
import static com.xuan.gsonapt.complier.TypeJsonKind.BOOLEAN;
import static com.xuan.gsonapt.complier.TypeJsonKind.BYTE;
import static com.xuan.gsonapt.complier.TypeJsonKind.CHAR;
import static com.xuan.gsonapt.complier.TypeJsonKind.DOUBLE;
import static com.xuan.gsonapt.complier.TypeJsonKind.FLOAT;
import static com.xuan.gsonapt.complier.TypeJsonKind.INT;
import static com.xuan.gsonapt.complier.TypeJsonKind.LONG;
import static com.xuan.gsonapt.complier.TypeJsonKind.SHORT;
import static com.xuan.gsonapt.complier.TypeJsonKind.STRING;

/**
 * Created by chenxiaoxuan1 on 17/2/5.
 */

public class ElementUtil {
    public static final String INTERFACE_LIST = "java.util.List";
    public static final String INTERFACE_MAP = "java.util.Map";
    public static final String INTERFACE_SET = "java.util.Set";
    private static final String CLASS_CHARACCTER = "java.lang.Character";
    private static final String CLASS_BOOLEAN = "java.lang.Boolean";
    private static final String CLASS_STRING = "java.lang.String";
    private static final String CLASS_FLOAT = "java.lang.Float";
    private static final String CLASS_DOUBLE = "java.lang.Double";
    private static final String CLASS_INTEGER = "java.lang.Integer";
    private static final String CLASS_LONG = "java.lang.Long";
    private static final String CLASS_SHORT = "java.lang.Short";
    private static final String CLASS_BYTE = "java.lang.Byte";

    private static final Map<TypeMirror, List<ExecutableElement>> ALL_METHODS = new HashMap<>();

    public static TypeJsonKind getJsonKind(Element element) {
        TypeMirror typeMirror = element.asType();
        if (typeMirror.getKind().equals(TypeKind.DECLARED)) {
            String className = doubleErasure(typeMirror);
            TypeElement typeElement = ELEMENT_UTILS.getTypeElement(className);

            switch (className) {
                case CLASS_BOOLEAN:
                    return BOOLEAN;
                case CLASS_STRING:
                    return STRING;
                case CLASS_CHARACCTER:
                    return CHAR;
                case CLASS_FLOAT:
                    return FLOAT;
                case CLASS_DOUBLE:
                    return DOUBLE;
                case CLASS_INTEGER:
                    return INT;
                case CLASS_SHORT:
                    return SHORT;
                case CLASS_BYTE:
                    return BYTE;
                case CLASS_LONG:
                    return LONG;
            }

            List<? extends TypeMirror> interfaces = getAllInterfaces(typeElement);
            for (TypeMirror t : interfaces) {
                switch (doubleErasure(t)) {
                    case INTERFACE_LIST:
                        return TypeJsonKind.LIST;
                    case INTERFACE_MAP:
                        return TypeJsonKind.MAP;
                    case INTERFACE_SET:
                        return TypeJsonKind.Set;
                }
            }
            return TypeJsonKind.NORMAL_BEAN;
        } else if (typeMirror.getKind().equals(TypeKind.ARRAY)) {
            return TypeJsonKind.ARRAY;
        } else {
            switch (typeMirror.getKind()) {
                case BOOLEAN:
                    return BOOLEAN;
                case INT:
                    return INT;
                case BYTE:
                    return BYTE;
                case SHORT:
                    return SHORT;
                case LONG:
                    return LONG;
                case CHAR:
                    return CHAR;
                case FLOAT:
                    return FLOAT;
                case DOUBLE:
                    return DOUBLE;
            }
        }
        throw new IllegalArgumentException();
    }

    public static List<ExecutableElement> getAllMethod(Element element) {
        TypeMirror type = element.asType();
        List<ExecutableElement> allMethods = ALL_METHODS.get(type);
        if (allMethods != null) {
            return allMethods;
        }

        allMethods = new ArrayList<>();

        TypeElement typeElement = null;
        if (element.getKind().isClass()) {
            typeElement = (TypeElement) element;
        }

        if (element.getKind().isField()) {
            TypeMirror typeMirror = element.asType();
            if (typeMirror.getKind().equals(TypeKind.DECLARED)) {
                String className = doubleErasure(typeMirror);
                typeElement = ELEMENT_UTILS.getTypeElement(className);
            }
        }
        do {
            List<? extends Element> elements = typeElement.getEnclosedElements();
            List<ExecutableElement> executableElements = ElementFilter.methodsIn(elements);
            for (ExecutableElement v : executableElements) {
                allMethods.add(v);
            }

            Element superElemets = TYPE_UTILS.asElement(typeElement.getSuperclass());
            typeElement = (TypeElement) superElemets;
        } while (!doubleErasure(typeElement.asType()).equals("java.lang.Object"));
        return allMethods;
    }

    public static List<VariableElement> getAllFields(Element element) {
        List<VariableElement> allFields = new ArrayList<>();

        TypeElement typeElement = null;
        if (element.getKind().isClass()) {
            typeElement = (TypeElement) element;
        }

        if (element.getKind().isField()) {
            TypeMirror typeMirror = element.asType();
            if (typeMirror.getKind().equals(TypeKind.DECLARED)) {
                String className = doubleErasure(typeMirror);
                typeElement = ELEMENT_UTILS.getTypeElement(className);
            }
        }
        do {
            List<? extends Element> elements = typeElement.getEnclosedElements();
            List<VariableElement> variableElements = ElementFilter.fieldsIn(elements);
            for (VariableElement v : variableElements) {
                allFields.add(v);
            }

            Element superElemets = TYPE_UTILS.asElement(typeElement.getSuperclass());
            typeElement = (TypeElement) superElemets;
        } while (!doubleErasure(typeElement.asType()).equals("java.lang.Object"));
        return allFields;
    }

    public static List<TypeMirror> getAllInterfaces(TypeElement typeElement) {
        List<TypeMirror> interfaces = new ArrayList<>();
        if (typeElement.getKind().isInterface()) {
            interfaces.add(typeElement.asType());
        }
        Element superElemets = null;
        do {
            if (superElemets != null) {
                typeElement = (TypeElement) superElemets;
            }
            List<? extends TypeMirror> thisClassInterfaces = typeElement.getInterfaces();
            for (TypeMirror typeMirror : thisClassInterfaces) {
                interfaces.add(typeMirror);
            }

            superElemets = TYPE_UTILS.asElement(typeElement.getSuperclass());
        }
        while (!ElementUtil.doubleErasure(typeElement.getSuperclass()).equals("java.lang.Object") && superElemets != null);
        return interfaces;
    }

    /**
     * 擦除 泛型,获得原生类型
     *
     * @param elementType
     * @return
     */
    public static String doubleErasure(TypeMirror elementType) {
        String name = GsonAPTProcessor.TYPE_UTILS.erasure(elementType).toString();
        int typeParamStart = name.indexOf('<');
        if (typeParamStart != -1) {
            name = name.substring(0, typeParamStart);
        }
        return name;
    }
}
