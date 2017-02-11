package com.xuan.gsonapt.complier;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

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
        this.proxyClassName = className + "$" + PROXY;
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
        private Set<String> importSet = new HashSet<>();
        private String localBeanName;
        private String fieldName;
        private String jsonReaderName = "jsonReader";

        public GenerateJavaCode(String localBeanName) {
            this.localBeanName = localBeanName;
        }

        public String getJsonReaderName() {
            return jsonReaderName;
        }

        public void setJsonReaderName(String jsonReaderName) {
            this.jsonReaderName = jsonReaderName;
        }

        String generateJavaCode() {

            StringBuilder builder = new StringBuilder();
            builder.append("// Generated code from OnceClick. Do not modify!\n");
            builder.append("package ").append(packageName).append(";\n\n");
            //import
            builder.append('\n');
            initImportSet();
            for (String s : importSet) {
                builder.append("import " + s + ";\n");
            }
            builder.append("public class ").append(proxyClassName);
            builder.append(" {\n");

            //生成toJson
            builder.append(" public static void toJson(JsonWriter jsonWriter," + simpleClassName + " " + localBeanName + ") " + " throws IOException " + "{\n");
//            builder.append("StringWriter writer = new StringWriter();\n");
//            builder.append("JsonWriter jsonWriter = new JsonWriter(writer);\n");
            toJsonNormalBean(builder, element);
            builder.append('\n');
//            builder.append("return writer.toString();\n");
            builder.append("    }\n");

            //生成fromJson
            builder.append("public static " + simpleClassName + " fromJson(JsonReader " + jsonReaderName + ",String str) throws IOException {\n");
//            builder.append("StringReader reader = new StringReader(str);\n");
//            builder.append("JsonReader " + jsonReaderName + "= new JsonReader(reader);\n");
            builder.append(simpleClassName + " " + localBeanName + " = new " + simpleClassName + "();\n");
            new CreateFromJsonCode(localBeanName).fromJson(builder, element, localBeanName);
            builder.append("return " + localBeanName + ";\n");
            builder.append("}\n");

            //end class
            builder.append("}\n");
            return builder.toString();

        }

        public void toJson(StringBuilder builder, Element element, String localBeanName) {
            switch (ElementUtil.getJsonKind(element)) {
                case NORMAL_BEAN:
                    new GenerateJavaCode(localBeanName).toJsonNormalBean(builder, element);
                    break;

                case BOOLEAN:
                case INT:
                case LONG:
                case DOUBLE:
                case STRING:
                    toJsonBaseType(builder, element);
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
            String itemName = localBeanName + "." + element;
            if (typeMirror.getKind().equals(TypeKind.DECLARED)) {
                builder.append("if( " + itemName + " == null){\n");
                builder.append("jsonWriter.nullValue();\n");
                builder.append("} else {\n");
                builder.append("jsonWriter.value(" + localBeanName + "." + element + ");\n");
                builder.append("}\n");
            } else {
                builder.append("jsonWriter.value(" + localBeanName + "." + element + ");\n");
            }
        }

        private void toJsonNormalBean(StringBuilder builder, Element element) {
            builder.append("if( " + localBeanName + "== null){\n");
            builder.append("jsonWriter.nullValue();\n");
            builder.append("} else {\n");
            builder.append("jsonWriter.beginObject();\n");
            List<VariableElement> allFields = ElementUtil.getAllFields(element);
            for (Element field : allFields) {
                writeName(builder, field.toString());
                toJson(builder, field, localBeanName + "." + field.toString());
            }
            builder.append("jsonWriter.endObject();\n");
            builder.append("}\n");
        }

        private void toJsonArray(StringBuilder builder, Element element) {
            builder.append("if( " + localBeanName + "== null){\n");
            builder.append("jsonWriter.nullValue();\n");
            builder.append("} else {\n");
            builder.append("jsonWriter.beginArray();\n");
            TypeMirror itemType;
            if (element.asType().getKind().equals(TypeKind.ARRAY)) {
                ArrayType arrayType = (ArrayType) element.asType();
                itemType = arrayType.getComponentType();
            } else {//List,Set
                DeclaredType declaredType = (DeclaredType) element.asType();
                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                itemType = typeArguments.get(0);
            }

            String itemName = "item";
            builder.append("for (" + itemType + " " + itemName + " : " + localBeanName + "){\n");
            toJson(builder, TYPE_UTILS.asElement(itemType), itemName);
            builder.append("}\n");
            builder.append("jsonWriter.endArray();\n");
            builder.append("}\n");
        }

        private void toJsonMap(StringBuilder builder, Element element) {
            builder.append("if( " + localBeanName + "== null){\n");
            builder.append("jsonWriter.nullValue();\n");
            builder.append("} else {\n");
            builder.append("jsonWriter.beginObject();\n");
            DeclaredType declaredType = (DeclaredType) element.asType();
            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();

            String itemName = "item";
            builder.append("for (Map.Entry<" + typeArguments.get(0) + "," + typeArguments.get(1) + "> " + itemName + " : " + localBeanName + ".entrySet()){\n");
            builder.append("jsonWriter.name(" + itemName + ".getKey());\n");
            toJson(builder, TYPE_UTILS.asElement(typeArguments.get(1)), itemName + ".getValue()\n");
            builder.append("}\n");
            builder.append("jsonWriter.endObject();\n");
            builder.append("}\n");
        }

        private void writeName(StringBuilder builder, String name) {
            builder.append("jsonWriter.name(" + "\"" + name + "\");\n");
        }


        public void initImportSet() {
            importSet.add(JsonWriter.class.getName());
            importSet.add(JsonReader.class.getName());
            importSet.add(StringWriter.class.getName());
            importSet.add(IOException.class.getName());
            importSet.add(StringReader.class.getName());
            importSet.add(JsonReader.class.getName());
            importSet.add(Map.class.getName());
            importSet.add(packageName + "." + simpleClassName);
            List<? extends Element> elements = element.getEnclosedElements();
            List<VariableElement> variableElements = ElementFilter.fieldsIn(elements);
            for (VariableElement v : variableElements) {
                TypeMirror type = v.asType();
                if (type.getKind().equals(TypeKind.DECLARED)) {
                    DeclaredType declaredType = (DeclaredType) type;
//                    print("成员" + type.getKind() + "  " + doubleErasure(declaredType) + "  " + v.getSimpleName());
                    addImport(declaredType);
                }
            }

            TypeElement typeElement = (TypeElement) element;
            do {

                Element superElemets = TYPE_UTILS.asElement(typeElement.getSuperclass());
                List<? extends Element> enclosingElements = superElemets.getEnclosedElements();

                List<VariableElement> enclosingV = ElementFilter.fieldsIn(enclosingElements);
                for (VariableElement v : enclosingV) {
                    TypeMirror type = v.asType();
                    if (type.getKind().equals(TypeKind.DECLARED)) {
                        DeclaredType declaredType = (DeclaredType) type;
//                        print("成员" + type.getKind() + "  " + doubleErasure(declaredType) + "  " + v.getSimpleName());
                        addImport(declaredType);
                    }
                }
                typeElement = (TypeElement) superElemets;
            }
            while (!ElementUtil.doubleErasure(typeElement.getSuperclass()).equals("java.lang.Object"));

            GsonAPTProcessor.print("all the import");
            for (String s : importSet) {
                GsonAPTProcessor.print(s);
            }
        }


        private void addImport(TypeMirror elementType) {
            importSet.add(ElementUtil.doubleErasure(elementType));
            if (elementType.getKind().equals(TypeKind.DECLARED)) {
                DeclaredType declaredType = (DeclaredType) elementType;
                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                for (TypeMirror typeMirror : typeArguments) {
                    addImport(typeMirror);
                }
            }

        }
    }

    private class CreateFromJsonCode {
        private String localBeanName;
        private String jsonReaderName = "jsonReader";
        private CreateCodeUtil.SetMethod setMethod;

        public CreateFromJsonCode(String localBeanName) {
            this.localBeanName = localBeanName;
        }

        public String getJsonReaderName() {
            return jsonReaderName;
        }

        public void setJsonReaderName(String jsonReaderName) {
            this.jsonReaderName = jsonReaderName;
        }

        public CreateCodeUtil.SetMethod getSetMethod() {
            return setMethod;
        }

        public void setSetMethod(CreateCodeUtil.SetMethod setMethod) {
            this.setMethod = setMethod;
        }

        private void fromJson(StringBuilder builder, Element element, String localBeanName) {
            TypeJsonKind jsonKind = ElementUtil.getJsonKind(element);
            CreateFromJsonCode generateJavaCode = new CreateFromJsonCode(localBeanName);
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
            switch (kind) {
                case BOOLEAN:
                    builder.append(localBeanName + " = " + jsonReaderName + ".nextBoolean();\n");
                    break;
                case INT:
                    builder.append(localBeanName + " = " + jsonReaderName + ".nextInt();\n");
                    break;
                case LONG:
                    builder.append(localBeanName + " = " + jsonReaderName + ".nextLong();\n");
                    break;
                case DOUBLE:
                    builder.append(localBeanName + " = " + jsonReaderName + ".nextDouble();\n");
                    break;
                case STRING:
                    builder.append(localBeanName + " = " + jsonReaderName + ".nextString();\n");
                    break;
            }
        }

        private void fromJsonNormalBean(StringBuilder builder, Element element) {
            String itemName = localBeanName.replace(".", "") + "Item";
            builder.append("if (" + jsonReaderName + ".peek() == JsonToken.NULL) {\n");
            builder.append(jsonReaderName + ".nullValue();\n");
            builder.append("} else { \n");
            //// TODO: 17/2/11 创建对象
            builder.append("" + jsonReaderName + ".beginObject();\n");
            builder.append("while (" + jsonReaderName + ".hasNext()){\n");
            builder.append("String " + itemName + " = " + jsonReaderName + ".nextName();\n");
            //process all field
            List<VariableElement> allFields = ElementUtil.getAllFields(element);
            for (VariableElement field : allFields) {
                builder.append("if(" + itemName + ".equals(\"" + field + "\")){\n");
                fromJson(builder, field, localBeanName + "." + field);
                builder.append("continue;\n");
                builder.append("}\n");
            }
            builder.append(jsonReaderName + ".skipValue();\n");
            builder.append("}\n");
            builder.append("" + jsonReaderName + ".endObject();");
            builder.append("}\n");
        }

        private void fromJsonList(StringBuilder builder, Element element) {
            DeclaredType declaredType = (DeclaredType) element.asType();
            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            TypeMirror itemType = typeArguments.get(0);
            String itemName = "item";
            builder.append("" + jsonReaderName + ".beginArray();\n");
            builder.append("while (" + jsonReaderName + ".hasNext()){\n");
            builder.append(itemType + " " + itemName + " = new " + itemType + "();\n");
            fromJson(builder, TYPE_UTILS.asElement(itemType), itemName);
            builder.append(localBeanName + ".add(" + itemName + ");\n");
            builder.append("}\n");
            builder.append("" + jsonReaderName + ".endArray();\n");
        }

        private void fromJsonArray(StringBuilder builder, Element element) {
            ArrayType arrayType = (ArrayType) element.asType();
            TypeMirror itemType = arrayType.getComponentType();
            String arrayListName = "arrayList";
            String itemName = "item";
            String arrayName = "arr";
            builder.append("ArrayList<" + itemType + "> " + arrayListName + " = new ArrayList<>();\n");
            builder.append("" + jsonReaderName + ".beginArray();\n");
            builder.append("while (" + jsonReaderName + ".hasNext()){\n");
            builder.append(itemType + " " + itemName + " = new " + itemType + "();\n");
            fromJson(builder, TYPE_UTILS.asElement(itemType), itemName);
            builder.append(localBeanName + ".add(" + itemName + ");\n");
            builder.append("}\n");
            builder.append(itemType + "[] " + arrayName + " = new " + itemType + "[" + arrayListName + ".size()}\n");
            builder.append("int i = 0;\n");
            builder.append("for(" + itemType + " " + itemName + " : " + arrayListName + "){\n");
            builder.append(arrayName + "[i++] = " + itemName + ";");
            builder.append("}\n");
            builder.append("" + jsonReaderName + ".endArray();\n");
        }

        private void fromJsonMap(StringBuilder builder, Element element) {
            builder.append("" + jsonReaderName + ".beginObject();\n");
            DeclaredType declaredType = (DeclaredType) element.asType();
            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            String keyStrName = "keyStr";
            String keyObjName = "keyObj";
            String valueObjName = "valueObj";
            builder.append("while (" + jsonReaderName + ".hasNext()){\n");
            builder.append("String " + keyStrName + " = " + jsonReaderName + ".nextName();\n");
            createObjectFromKey(builder, TYPE_UTILS.asElement(typeArguments.get(0)), keyObjName, keyStrName);
            builder.append(typeArguments.get(1) + " " + valueObjName + " = new " + typeArguments.get(1) + "();\n");
            fromJson(builder, TYPE_UTILS.asElement(typeArguments.get(1)), valueObjName);
            builder.append(localBeanName + ".put(" + keyObjName + "," + valueObjName + ");\n");
            builder.append("}\n");
            builder.append("" + jsonReaderName + ".endObject();\n");
        }

        private void createObjCode(StringBuilder builder, Element element, String objName) {
            TypeJsonKind jsonKind = ElementUtil.getJsonKind(element);
            TypeMirror typeMirror = element.asType();
            switch (jsonKind) {
                case NORMAL_BEAN:
                    builder.append(typeMirror + " " + objName + " = new " + typeMirror + "();");
                    break;

            }
        }

        private void createObjectFromKey(StringBuilder builder, Element element, String resultItemName, String keyName) {
            TypeJsonKind jsonKind = ElementUtil.getJsonKind(element);
            TypeMirror typeMirror = element.asType();
            switch (jsonKind) {
                case NORMAL_BEAN:
                    String reader = "keyReader";
                    builder.append("StringReader keyStrReader = new StringReader(" + keyName + ");\n");
                    builder.append("JsonReader " + reader + " = new JsonReader(keyStrReader);\n");
                    CreateFromJsonCode generateJavaCode = new CreateFromJsonCode(resultItemName);
                    generateJavaCode.setJsonReaderName(reader);
                    builder.append(typeMirror + " " + resultItemName + " = new " + typeMirror + "();");
                    generateJavaCode.fromJson(builder, element, resultItemName);
                    break;

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
                //暂不处理下面
                case Set:
                case LIST:
//                    new GenerateJavaCode(localBeanName).fromJsonList(builder, element);
                    break;
                case ARRAY:
//                    new GenerateJavaCode(localBeanName).fromJsonArray(builder, element);
                    break;
                case MAP:
//                    new GenerateJavaCode(localBeanName).toJsonMap(builder, element);
                    break;

            }
        }
    }

}
