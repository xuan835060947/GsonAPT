package com.xuan.gsonapt.complier;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static com.xuan.gsonapt.complier.GsonAPTProcessor.TYPE_UTILS;

/**
 * Created by chenxiaoxuan1 on 17/2/5.
 */

public class JsonClassProxyInfo {
    public static final String PROXY = "JsonParse";

    private Element element;
    private String packageName;
    private String simpleClassName;
    private String proxyClassName;

    JsonClassProxyInfo(String packageName, String className, Element element) {
        this.packageName = packageName;
        this.simpleClassName = className;
        this.proxyClassName = className + "$$" + PROXY;
        this.element = element;
    }

    public String getClassName() {
        return packageName + "." + simpleClassName;
    }

    String generateJavaCode() {
        return new GenerateJavaCode("bean").generateJavaCode();
    }

    String getProxyClassFullName() {
        return packageName + "." + proxyClassName;
    }

    public Element getElement() {
        return element;
    }

    class GenerateJavaCode {
        private Set<String> mImportSet = new HashSet<>();
        private String mLocalBeanName;

        public GenerateJavaCode(String localBeanName) {
            this.mLocalBeanName = localBeanName;
        }

        String generateJavaCode() {

            StringBuilder builder = new StringBuilder();
            builder.append("// Generated code from GsonAPT. Do not modify!\n");
            builder.append("package ").append(packageName).append(";\n\n");
            //import
            builder.append('\n');
            initImportSet();
            for (String s : mImportSet) {
                builder.append("import " + s + ";\n");
            }
            builder.append("public class ").append(proxyClassName);
            builder.append(" {\n");

            //generate code for toJson method
            builder.append(" public static void toJson(JsonWriter jsonWriter," + simpleClassName + " " + mLocalBeanName + ") " + " throws IOException " + "{\n");
            toJsonNormalBean(builder, element);
            builder.append('\n');
            builder.append("    }\n");

            //generate code for fromJson method
            new GenerateFromJsonCode(mLocalBeanName).generateCode(builder, simpleClassName, element);

            builder.append("}\n");
            return builder.toString();

        }

        public void toJson(StringBuilder builder, Element element, String localBeanName) {
            switch (ElementUtil.getJsonKind(element)) {
                case NORMAL_BEAN:
                    new GenerateJavaCode(localBeanName).toJsonNormalBean(builder, element);
                    break;

                case BOOLEAN:
                case BYTE:
                case SHORT:
                case CHAR:
                case INT:
                case LONG:
                case DOUBLE:
                case STRING:
                    new GenerateJavaCode(localBeanName).toJsonBaseType(builder, element);
                    break;
                case Set:
                case LIST:
                case ARRAY:
                    new GenerateJavaCode(localBeanName).toJsonArray(builder, element);
                    break;
                case MAP:
                    new GenerateJavaCode(localBeanName).toJsonMap(builder, element);
                    break;

            }
        }

        private void toJsonBaseType(StringBuilder builder, Element element) {
            TypeMirror typeMirror = element.asType();
//            String itemName = localBeanName + "." + element;
            if (ElementUtil.getJsonKind(element).equals(TypeJsonKind.CHAR)) {
                if (typeMirror.getKind().equals(TypeKind.DECLARED)) {
                    builder.append("if( " + mLocalBeanName + " == null){\n");
                    builder.append("jsonWriter.nullValue();\n");
                    builder.append("} else {\n");
                    builder.append("jsonWriter.value(String.valueOf(" + mLocalBeanName + "));\n");
                    builder.append("}\n");
                } else {
                    builder.append("jsonWriter.value(String.valueOf(" + mLocalBeanName + "));\n");
                }
            } else if (typeMirror.getKind().equals(TypeKind.DECLARED)) {
                builder.append("if( " + mLocalBeanName + " == null){\n");
                builder.append("jsonWriter.nullValue();\n");
                builder.append("} else {\n");
                builder.append("jsonWriter.value(" + mLocalBeanName + ");\n");
                builder.append("}\n");
            } else {
                builder.append("jsonWriter.value(" + mLocalBeanName + ");\n");
            }
        }

        private void toJsonNormalBean(StringBuilder builder, Element element) {
            builder.append("if( " + mLocalBeanName + "== null){\n");
            builder.append("jsonWriter.nullValue();\n");
            builder.append("} else {\n");
            builder.append("jsonWriter.beginObject();\n");
            List<VariableElement> allFields = ElementUtil.getAllFields(element);
            for (VariableElement field : allFields) {
                writeName(builder, field.toString());
                String getValue = CreateCodeUtil.getGetMethod(mLocalBeanName, element, field).getGetMethodCode();
                toJson(builder, field, getValue);
            }
            builder.append("jsonWriter.endObject();\n");
            builder.append("}\n");
        }

        private void toJsonArray(StringBuilder builder, Element element) {
            builder.append("if( " + mLocalBeanName + "== null){\n");
            builder.append("jsonWriter.nullValue();\n");
            builder.append("} else {\n");
            String objName = "collection";
            builder.append(element.asType() + " " + objName + " = " + mLocalBeanName + " ;\n");
            builder.append("jsonWriter.beginArray();\n");
            TypeMirror itemType;
            if (element.asType().getKind().equals(TypeKind.ARRAY)) {
                ArrayType arrayType = (ArrayType) element.asType();
                itemType = arrayType.getComponentType();
            } else {//List,Set
                DeclaredType declaredType = (DeclaredType) element.asType();
                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                if(typeArguments.size() == 0){
                    throw new IllegalArgumentException(mLocalBeanName + " must have the specific wildcard type ");
                }
                itemType = typeArguments.get(0);
            }

            String itemName = "item";
            builder.append("for (" + itemType + " " + itemName + " : " + objName + "){\n");
            toJson(builder, TYPE_UTILS.asElement(itemType), itemName);
            builder.append("}\n");
            builder.append("jsonWriter.endArray();\n");
            builder.append("}\n");
        }

        private void toJsonMap(StringBuilder builder, Element element) {
            builder.append("if( " + mLocalBeanName + "== null){\n");
            builder.append("jsonWriter.nullValue();\n");
            builder.append("} else {\n");
            String objName = "collection";
            builder.append(element.asType() + " " + objName + " = " + mLocalBeanName + " ;\n");
            builder.append("jsonWriter.beginObject();\n");
            DeclaredType declaredType = (DeclaredType) element.asType();
            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            if(typeArguments.size() == 0){
                throw new IllegalArgumentException(mLocalBeanName + " must have the specific wildcard type ");
            }
            String itemName = "item";
            builder.append("for (Map.Entry<" + typeArguments.get(0) + "," + typeArguments.get(1) + "> " + itemName + " : " + objName + ".entrySet()){\n");
            builder.append("jsonWriter.name(String.valueOf(" + itemName + ".getKey()));\n");
            toJson(builder, TYPE_UTILS.asElement(typeArguments.get(1)), itemName + ".getValue()\n");
            builder.append("}\n");
            builder.append("jsonWriter.endObject();\n");
            builder.append("}\n");
        }

        private void writeName(StringBuilder builder, String name) {
            builder.append("jsonWriter.name(" + "\"" + name + "\");\n");
        }


        public void initImportSet() {
            mImportSet.add(JsonWriter.class.getName());
            mImportSet.add(JsonReader.class.getName());
            mImportSet.add(StringWriter.class.getName());
            mImportSet.add(IOException.class.getName());
            mImportSet.add(StringReader.class.getName());
            mImportSet.add(JsonReader.class.getName());
            mImportSet.add(Map.class.getName());
            mImportSet.add(LinkedHashMap.class.getName());
            mImportSet.add(HashSet.class.getName());
            mImportSet.add(ArrayList.class.getName());
            mImportSet.add(JsonToken.class.getName());
            mImportSet.add(TypeToken.class.getName());
            mImportSet.add(String.class.getName());
            mImportSet.add(packageName + "." + simpleClassName);
            mImportSet.add("com.xuan.gsonapt" + "." + "GsonAPT");
            List<VariableElement> elements = ElementUtil.getAllFields(element);
            for (VariableElement v : elements) {
                TypeMirror type = v.asType();
                if (type.getKind().equals(TypeKind.DECLARED)) {
                    DeclaredType declaredType = (DeclaredType) type;
                    addImport(declaredType);
                }
            }
        }


        private void addImport(TypeMirror elementType) {
            mImportSet.add(ElementUtil.doubleErasure(elementType));
            if (elementType.getKind().equals(TypeKind.DECLARED)) {
                DeclaredType declaredType = (DeclaredType) elementType;
                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                for (TypeMirror typeMirror : typeArguments) {
                    addImport(typeMirror);
                }
            }

        }
    }

    private static class GenerateFromJsonCode {
        private String mLocalBeanName;
        private String mJsonReaderName = "jsonReader";
        private CreateCodeUtil.SetMethod mSetMethod;

        public GenerateFromJsonCode(String localBeanName) {
            this.mLocalBeanName = localBeanName;
        }

        public String getJsonReaderName() {
            return mJsonReaderName;
        }

        public void setJsonReaderName(String jsonReaderName) {
            this.mJsonReaderName = jsonReaderName;
        }

        public CreateCodeUtil.SetMethod getSetMethod() {
            return mSetMethod;
        }

        public void setSetMethod(CreateCodeUtil.SetMethod setMethod) {
            this.mSetMethod = setMethod;
        }

        private void generateCode(StringBuilder builder, String simpleClassName, Element element) {
            //fromJson method code
            mLocalBeanName = "bean";
            builder.append("public static " + simpleClassName + " fromJson(JsonReader " + mJsonReaderName + ") throws IOException {\n");
            builder.append(simpleClassName + " " + mLocalBeanName + " = new " + simpleClassName + "();\n");
            new GenerateFromJsonCode(mLocalBeanName).fromJson(builder, element, mLocalBeanName);
            builder.append("return " + mLocalBeanName + ";\n");
            builder.append("}\n");
        }

        private void fromJson(StringBuilder builder, Element element, String localBeanName) {
            TypeJsonKind jsonKind = ElementUtil.getJsonKind(element);
            GenerateFromJsonCode generateJavaCode = new GenerateFromJsonCode(localBeanName);
            generateJavaCode.setJsonReaderName(getJsonReaderName());
            generateJavaCode.setSetMethod(getSetMethod());
            switch (jsonKind) {
                case NORMAL_BEAN:
                    generateJavaCode.fromJsonNormalBean(builder, element);
                    break;

                case BOOLEAN:
                case INT:
                case LONG:
                case DOUBLE:
                case STRING:
                    generateJavaCode.fromJsonBaseType(builder, element, jsonKind);
                    break;
                case Set:
                    generateJavaCode.fromJsonSet(builder, element);
                    break;
                case LIST:
                    generateJavaCode.fromJsonList(builder, element);
                    break;
                case ARRAY:
                    generateJavaCode.fromJsonArray(builder, element);
                    break;
                case MAP:
                    generateJavaCode.fromJsonMap(builder, element);
                    break;

            }
        }

        private void fromJsonBaseType(StringBuilder builder, Element element, TypeJsonKind kind) {
            boolean isDeclareType = element.asType().getKind().equals(TypeKind.DECLARED);
            if (isDeclareType) {
                builder.append("if (" + mJsonReaderName + ".peek() == JsonToken.NULL) {\n");
                builder.append(mJsonReaderName + ".nextNull();\n");
                builder.append("} else { \n");
            }
            switch (kind) {
                case BOOLEAN:
                    builder.append(mLocalBeanName + " = " + mJsonReaderName + ".nextBoolean();\n");
                    break;
                case INT:
                    builder.append(mLocalBeanName + " = " + mJsonReaderName + ".nextInt();\n");
                    break;
                case LONG:
                    builder.append(mLocalBeanName + " = " + mJsonReaderName + ".nextLong();\n");
                    break;
                case DOUBLE:
                    builder.append(mLocalBeanName + " = " + mJsonReaderName + ".nextDouble();\n");
                    break;
                case STRING:
                    builder.append(mLocalBeanName + " = " + mJsonReaderName + ".nextString();\n");
                    break;
                case CHAR:
                    builder.append("String str = " + mJsonReaderName + ".nextString();\n");
                    builder.append("if( str.length() >= 1 ){\n");
                    builder.append(mLocalBeanName + " = str.charAt(0);\n");
                    builder.append("}\n");
                    break;
                case BYTE:
                    builder.append(mLocalBeanName + " = (byte)" + mJsonReaderName + ".nextInt();\n");
                    break;
                case SHORT:
                    builder.append(mLocalBeanName + " = (short)" + mJsonReaderName + ".nextInt();\n");
                    break;
            }
            if (isDeclareType) {
                builder.append("}\n");
            }
        }

        private void fromJsonNormalBean(StringBuilder builder, Element element) {
            String itemName = mLocalBeanName.replace(".", "") + "Item";
            builder.append(mLocalBeanName + " = new " + element.asType() + "();\n");
            builder.append("if (" + mJsonReaderName + ".peek() == JsonToken.NULL) {\n");
            builder.append(mJsonReaderName + ".nextNull();\n");
            builder.append("} else { \n");
            builder.append("" + mJsonReaderName + ".beginObject();\n");
            builder.append("while (" + mJsonReaderName + ".hasNext()){\n");
            builder.append("String " + itemName + " = " + mJsonReaderName + ".nextName();\n");
            //process all field
            List<VariableElement> allFields = ElementUtil.getAllFields(element);
            for (VariableElement field : allFields) {
                builder.append("if(" + itemName + ".equals(\"" + field + "\")){\n");
                String fieldName = mLocalBeanName + field;
                declareObjCode(builder, field, fieldName);
                fromJson(builder, field, fieldName);
                builder.append(CreateCodeUtil.getSetMethod(mLocalBeanName, element, field).getSetMethodCode(fieldName));
                builder.append("continue;\n");
                builder.append("}\n");
            }
            builder.append(mJsonReaderName + ".skipValue();\n");
            builder.append("}\n");
            builder.append("" + mJsonReaderName + ".endObject();");
            builder.append("}\n");
        }

        private void fromJsonSet(StringBuilder builder, Element element) {
            builder.append("if (" + mJsonReaderName + ".peek() == JsonToken.NULL) {\n");
            builder.append(mJsonReaderName + ".nextNull();\n");
            builder.append("} else { \n");
            String className = ElementUtil.doubleErasure(element.asType());
            if (className.equals(Set.class.getName())) {
                builder.append(mLocalBeanName + " = new HashSet<>();\n");
            } else {
                builder.append(mLocalBeanName + " = new " + element.asType() + "();\n");
            }
            DeclaredType declaredType = (DeclaredType) element.asType();
            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            if(typeArguments.size() == 0){
                throw new IllegalArgumentException(mLocalBeanName + " must have the specific wildcard type ");
            }
            TypeMirror itemType = typeArguments.get(0);
            String itemName = "item";
            builder.append("" + mJsonReaderName + ".beginArray();\n");
            builder.append("while (" + mJsonReaderName + ".hasNext()){\n");
//            builder.append(itemType + " " + itemName + " = null;\n");
            declareObjCode(builder, GsonAPTProcessor.TYPE_UTILS.asElement(itemType), itemName);
            fromJson(builder, TYPE_UTILS.asElement(itemType), itemName);
            builder.append(mLocalBeanName + ".add(" + itemName + ");\n");
            builder.append("}\n");
            builder.append("" + mJsonReaderName + ".endArray();\n");
            builder.append("}\n");
        }

        private void fromJsonList(StringBuilder builder, Element element) {
            builder.append("if (" + mJsonReaderName + ".peek() == JsonToken.NULL) {\n");
            builder.append(mJsonReaderName + ".nextNull();\n");
            builder.append("} else { \n");
            String className = ElementUtil.doubleErasure(element.asType());
            if (className.equals(List.class.getName())) {
                builder.append(mLocalBeanName + " = new ArrayList<>();\n");
            } else {
                builder.append(mLocalBeanName + " = new " + element.asType() + "();\n");
            }
            DeclaredType declaredType = (DeclaredType) element.asType();
            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            if(typeArguments.size() == 0){
                throw new IllegalArgumentException(mLocalBeanName + " must have the specific wildcard type ");
            }
            TypeMirror itemType = typeArguments.get(0);
            String itemName = "item";
            builder.append("" + mJsonReaderName + ".beginArray();\n");
            builder.append("while (" + mJsonReaderName + ".hasNext()){\n");
//            builder.append(itemType + " " + itemName + ";\n");
            declareObjCode(builder, GsonAPTProcessor.TYPE_UTILS.asElement(itemType), itemName);
            fromJson(builder, TYPE_UTILS.asElement(itemType), itemName);
            builder.append(mLocalBeanName + ".add(" + itemName + ");\n");
            builder.append("}\n");
            builder.append("" + mJsonReaderName + ".endArray();\n");
            builder.append("}\n");
        }

        private void fromJsonArray(StringBuilder builder, Element element) {
            ArrayType arrayType = (ArrayType) element.asType();
            TypeMirror itemType = arrayType.getComponentType();
            String arrayListName = "arrayList";
            String itemName = "item";
            builder.append("if (" + mJsonReaderName + ".peek() == JsonToken.NULL) {\n");
            builder.append(mJsonReaderName + ".nextNull();\n");
            builder.append("} else { \n");
            builder.append("ArrayList<" + itemType + "> " + arrayListName + " = new ArrayList<>();\n");
            builder.append("" + mJsonReaderName + ".beginArray();\n");
            builder.append("while (" + mJsonReaderName + ".hasNext()){\n");
//            builder.append(itemType + " " + itemName + ";\n");
            declareObjCode(builder, GsonAPTProcessor.TYPE_UTILS.asElement(itemType), itemName);
            fromJson(builder, TYPE_UTILS.asElement(itemType), itemName);
            builder.append(mLocalBeanName + ".add(" + itemName + ");\n");
            builder.append("}\n");
            builder.append(mLocalBeanName + " = new " + itemType + "[" + arrayListName + ".size()}\n");
            builder.append("int i = 0;\n");
            builder.append("for(" + itemType + " " + itemName + " : " + arrayListName + "){\n");
            builder.append(mLocalBeanName + "[i++] = " + itemName + ";");
            builder.append("}\n");
            builder.append("" + mJsonReaderName + ".endArray();\n");
            builder.append("}\n");
        }

        private void fromJsonMap(StringBuilder builder, Element element) {
            builder.append("if (" + mJsonReaderName + ".peek() == JsonToken.NULL) {\n");
            builder.append(mJsonReaderName + ".nextNull();\n");
            builder.append("} else { \n");
            DeclaredType declaredType = (DeclaredType) element.asType();
            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            if(typeArguments.size() == 0){
                throw new IllegalArgumentException(mLocalBeanName + " must have the specific wildcard type ");
            }
            if (ElementUtil.getJsonKind(GsonAPTProcessor.TYPE_UTILS.asElement(typeArguments.get(0))).isBaseType()) {
                String keyStrName = "keyStr";
                String keyObjName = "keyObj";
                String valueObjName = "valueObj";
                String className = ElementUtil.doubleErasure(element.asType());
                if (className.equals(Map.class.getName())) {
                    builder.append(mLocalBeanName + " = new LinkedHashMap<>();\n");
                } else {
                    builder.append(mLocalBeanName + " = new " + element.asType() + "();\n");
                }
                builder.append("" + mJsonReaderName + ".beginObject();\n");
                builder.append("while (" + mJsonReaderName + ".hasNext()){\n");
                builder.append("String " + keyStrName + " = " + mJsonReaderName + ".nextName();\n");
                createObjectFromKey(builder, TYPE_UTILS.asElement(typeArguments.get(0)), keyObjName, keyStrName);
                builder.append(typeArguments.get(1) + " " + valueObjName + " ;\n");
                fromJson(builder, TYPE_UTILS.asElement(typeArguments.get(1)), valueObjName);
                builder.append(mLocalBeanName + ".put(" + keyObjName + "," + valueObjName + ");\n");
                builder.append("}\n");
                builder.append("" + mJsonReaderName + ".endObject();\n");
            } else {
                builder.append(mLocalBeanName + " = GsonAPT.getGson().fromJson(" + mJsonReaderName + ",new TypeToken<" + element.asType() + ">(){}.getType());\n");
            }

            builder.append("}\n");
        }

        /**
         * @param builder
         * @param element
         * @param objName
         */
        private void declareObjCode(StringBuilder builder, Element element, String objName) {
            TypeJsonKind jsonKind = ElementUtil.getJsonKind(element);
            TypeMirror typeMirror = element.asType();

            switch (jsonKind) {
                case NORMAL_BEAN:
                    builder.append(typeMirror + " " + objName + " = null ;\n");
                    break;
                case BOOLEAN:
                    builder.append(typeMirror + " " + objName + " = false ;\n");
                    break;
                case INT:
                case BYTE:
                case SHORT:
                case CHAR:
                case LONG:
                case FLOAT:
                case DOUBLE:
                    if (element.asType().getKind().equals(TypeKind.DECLARED)) {
                        builder.append(typeMirror + " " + objName + " = null ;\n");
                    } else {
                        builder.append(typeMirror + " " + objName + " = 0 ;\n");
                    }
                    break;
                case STRING:
                    builder.append(typeMirror + " " + objName + " = null;\n");
                    break;
                case Set:
                    builder.append(typeMirror + " " + objName + " = null;\n");
                    break;
                case LIST:
                    builder.append(typeMirror + " " + objName + " = null;\n");
                    break;
                case ARRAY:
                    ArrayType arrayType = (ArrayType) element.asType();
                    TypeMirror itemType = arrayType.getComponentType();
                    builder.append(itemType + "[] " + objName + " = null;\n");
                    break;
                case MAP:
                    builder.append(typeMirror + " " + objName + " = null ;\n");
                    break;
            }
        }

        private void createObjectFromKey(StringBuilder builder, Element element, String resultItemName, String keyName) {
            TypeJsonKind jsonKind = ElementUtil.getJsonKind(element);
            TypeMirror typeMirror = element.asType();
            switch (jsonKind) {
                case BOOLEAN:
                    builder.append(typeMirror + " " + resultItemName + " = Boolean.parseBoolean(" + keyName + ");\n");
                    break;
                case INT:
                    builder.append(typeMirror + " " + resultItemName + " = Integer.parseInt(" + keyName + ");\n");
                    break;
                case LONG:
                    builder.append(typeMirror + " " + resultItemName + " = Long.parseLong(" + keyName + ");\n");
                    break;
                case FLOAT:
                    builder.append(typeMirror + " " + resultItemName + " = Float.parseFloat(" + keyName + ");\n");
                    break;
                case DOUBLE:
                    builder.append(typeMirror + " " + resultItemName + " = Double.parseDouble(" + keyName + ");\n");
                    break;
                case STRING:
                    builder.append(typeMirror + " " + resultItemName + " = " + keyName + ";\n");
                    break;
                case CHAR:
                    builder.append(typeMirror + " " + resultItemName + " = " + keyName + ".charAt(0);\n");
                    break;
                case BYTE:
                    builder.append(typeMirror + " " + resultItemName + " = Byte.parseByte(" + keyName + ");\n");
                    break;
                case SHORT:
                    builder.append(typeMirror + " " + resultItemName + " = Short.parseShort(" + keyName + ");\n");
                    break;
            }
        }
    }

}
