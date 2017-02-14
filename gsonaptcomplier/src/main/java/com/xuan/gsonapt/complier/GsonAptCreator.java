package com.xuan.gsonapt.complier;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.WeakHashMap;

/**
 * Created by chenxiaoxuan1 on 17/2/13.
 */

public class GsonAptCreator {
    private static final String packageName = "com.xuan.gsonapt";
    private static final String className = "GsonAPT";
    private Set<String> importSet = new HashSet<>();

    public String getFullName() {
        return packageName + "." + className;
    }

    public String createCode(List<JsonClassProxyInfo> jsonClassProxyInfos) {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code from GsonAPT. Do not modify!\n");
        builder.append("package ").append(packageName).append(";\n\n");
        builder.append('\n');
        initImport();
        for (String s : importSet) {
            builder.append("import " + s + ";\n");
        }

        builder.append("public class ").append(className);
        builder.append(" {\n");
        builder.append("    private static Gson sGson = new Gson();\n" +
                "\n" +
                "public static String toJson(Object object) {\n" +
                "        StringWriter writer = new StringWriter();\n" +
                "        JsonWriter jsonWriter = new JsonWriter(writer);\n" +
                "        jsonWriter.setSerializeNulls(sGson.serializeNulls());\n"+
                "        try {\n" +
                "            toJson(jsonWriter, object);\n" +
                "        } catch (IOException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "        return writer.toString();\n" +
                "    }\n" +
                "\n" +
                "    public static void toJson(JsonWriter jsonWriter, Object object) throws IOException {\n" +
                "        if (object == null) {\n" +
                "            jsonWriter.nullValue();\n" +
                "            return;\n" +
                "        }\n" +
                "        Class clazz = object.getClass();\n" +
                "        if (clazz.isArray()) {\n" +
                "            Object[] reslut = (Object[]) object;\n" +
                "            jsonWriter.beginArray();\n" +
                "            for (Object item : reslut) {\n" +
                "                toJson(jsonWriter, item);\n" +
                "            }\n" +
                "            jsonWriter.endArray();\n" +
                "        } else if (object instanceof Map) {\n" +
                "            Map map = (Map) object;\n" +
                "            Set<Map.Entry> set = map.entrySet();\n" +
                "            if(set.size() == 0){\n" +
                "                sGson.toJson(object, object.getClass(), jsonWriter);\n" +
                "                return;\n" +
                "            }\n" +
                "            for (Object item : set) {\n" +
                "                Map.Entry entry = (Map.Entry) item;\n" +
                "                if (entry.getKey()!=null && !isBaseValue(entry.getKey().getClass())) {\n" +
                "                    sGson.toJson(object, object.getClass(), jsonWriter);\n" +
                "                    return;\n" +
                "                }\n" +
                "            }"+
                "            jsonWriter.beginObject();\n" +
                "            for (Object item : map.entrySet()) {\n" +
                "                Map.Entry entry = (Map.Entry) item;\n" +
                "                jsonWriter.name(getBaseValueString(entry.getKey()));\n" +
                "                toJson(jsonWriter, entry.getValue());\n" +
                "            }\n" +
                "            jsonWriter.endObject();\n" +
                "        } else if (object instanceof List) {\n" +
                "            List<Object> list = (List<Object>) object;\n" +
                "            jsonWriter.beginArray();\n" +
                "            for (Object item : list) {\n" +
                "                toJson(jsonWriter, item);\n" +
                "            }\n" +
                "            jsonWriter.endArray();\n" +
                "        } else if (object instanceof Set) {\n" +
                "            Set<Object> set = (Set<Object>) object;\n" +
                "            jsonWriter.beginArray();\n" +
                "            for (Object item : set) {\n" +
                "                toJson(jsonWriter, item);\n" +
                "            }\n" +
                "            jsonWriter.endArray();\n" +
                "        } else {\n" +
                "            toJsonNormal(jsonWriter, object);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public static void toJsonNormal(JsonWriter jsonWriter, Object object) throws IOException {\n" +
                "        if (object == null) {\n" +
                "            jsonWriter.nullValue();\n" +
                "            return;\n" +
                "        }\n" +
                "        Class type = object.getClass();\n" +
                "\n" +
                "        if (object instanceof Boolean) {\n" +
                "            jsonWriter.value((Boolean) object);\n" +
                "            return;\n" +
                "        }\n" +
                "\n" +
                "        if (object instanceof Byte) {\n" +
                "            jsonWriter.value((Byte) object);\n" +
                "            return;\n" +
                "        }\n" +
                "\n" +
                "        if (object instanceof Character) {\n" +
                "            jsonWriter.value((Character) object);\n" +
                "            return;\n" +
                "        }\n" +
                "        if (object instanceof Integer) {\n" +
                "            jsonWriter.value((Integer) object);\n" +
                "            return;\n" +
                "        }\n" +
                "\n" +
                "        if (object instanceof Long) {\n" +
                "            jsonWriter.value((Long) object);\n" +
                "            return;\n" +
                "        }\n" +
                "\n" +
                "        if (object instanceof Short) {\n" +
                "            jsonWriter.value((Short) object);\n" +
                "            return;\n" +
                "        }\n" +
                "        if (object instanceof Float) {\n" +
                "            jsonWriter.value((Float) object);\n" +
                "            return;\n" +
                "        }\n" +
                "        if (object instanceof Double) {\n" +
                "            jsonWriter.value((Double) object);\n" +
                "            return;\n" +
                "        }\n" +
                "\n" +
                "        if (object instanceof String) {\n" +
                "            jsonWriter.value((String) object);\n" +
                "            return;\n" +
                "        }\n");
        writeToJson(builder, jsonClassProxyInfos);
        builder.append("sGson.toJson(object, object.getClass(), jsonWriter);\n" +
                "    }\n" +
                "\n" +
                "    public static <T> T fromJson(String json, Type type) throws JsonSyntaxException {\n" +
                "        StringReader stringReader = new StringReader(json);\n" +
                "        JsonReader jsonReader = new JsonReader(stringReader);\n" +
                "        try {\n" +
                "            return fromJson(jsonReader, type);\n" +
                "        } catch (IOException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    private static <T> T fromJson(JsonReader jsonReader, Type type) throws IOException {\n" +
                "        if(jsonReader.peek() == JsonToken.NULL){\n" +
                "            jsonReader.nextNull();"+
                "            return null;\n" +
                "        }" +
                "        if (type instanceof Class) {\n" +
                "            Class<?> c = (Class<?>) type;\n" +
                "            if (c.isArray()) {\n" +
                "                Class<?> conponentType = c.getComponentType();\n" +
                "                List<Object> list = new ArrayList<>();\n" +
                "                jsonReader.beginArray();\n" +
                "                while (jsonReader.hasNext()) {\n" +
                "                    list.add(fromJson(jsonReader, conponentType));\n" +
                "                }\n" +
                "                jsonReader.endArray();\n" +
                "                int i = 0;\n" +
                "                Object[] reslut = (Object[]) Array.newInstance(conponentType, list.size());\n" +
                "                for (Object obj : list) {\n" +
                "                    reslut[i++] = obj;\n" +
                "                }\n" +
                "                return (T) reslut;\n" +
                "            } else {\n" +
                "                return fromJsonNormalBean(jsonReader, type);\n" +
                "            }\n" +
                "\n" +
                "        } else if (type instanceof ParameterizedType) {\n" +
                "            ParameterizedType p = (ParameterizedType) type;\n" +
                "            TypeToken<T> typeToken = (TypeToken<T>) TypeToken.get(type);\n" +
                "            Class<? super T> rawType = typeToken.getRawType();\n" +
                "            if (List.class.isAssignableFrom(rawType)) {\n" +
                "                List<Object> list = createCollection(rawType);\n" +
                "                jsonReader.beginArray();\n" +
                "                Type[] actualType = p.getActualTypeArguments();\n" +
                "                while (jsonReader.hasNext()) {\n" +
                "                    list.add(fromJson(jsonReader, actualType[0]));\n" +
                "                }\n" +
                "                jsonReader.endArray();\n" +
                "                return (T) list;\n" +
                "            } else if (Map.class.isAssignableFrom(rawType)) {\n" +
                "                Map map = createCollection(rawType);\n" +
                "                Type[] actualType = p.getActualTypeArguments();\n" +
                "                if (isBaseValue(actualType[0])) {"+
                "                jsonReader.beginObject();\n" +
                "                while (jsonReader.hasNext()) {\n" +
                "                    String firstMapName = jsonReader.nextName();\n" +
                "                    JsonReader mapJsonReader = new JsonReader(new StringReader(firstMapName));\n" +
                "                    Object keyBean = getBaseValueFrom(firstMapName, actualType[0]);\n" +
                "\n" +
                "                    Object mapBean = fromJson(jsonReader, actualType[1]);\n" +
                "                    map.put(keyBean, mapBean);\n" +
                "                }\n" +
                "                jsonReader.endObject();\n" +
                "                return (T) map;\n" +
                "                }"+
                "            } else if (Set.class.isAssignableFrom(rawType)) {\n" +
                "                Set<Object> set = createCollection(rawType);\n" +
                "                jsonReader.beginArray();\n" +
                "                Type[] actualType = p.getActualTypeArguments();\n" +
                "                while (jsonReader.hasNext()) {\n" +
                "                    set.add(fromJson(jsonReader, actualType[0]));\n" +
                "                }\n" +
                "                jsonReader.endArray();\n" +
                "                return (T) set;\n" +
                "            }\n" +
                "        } else if (type instanceof GenericArrayType) {\n" +
                "\n" +
                "        } else if (type instanceof WildcardType) {\n" +
                "            WildcardType wildcardType = (WildcardType) type;\n" +
                "            //use the bound type\n" +
                "            Type boundType;\n" +
                "            if (wildcardType.getLowerBounds().length == 0) {\n" +
                "                boundType = wildcardType.getUpperBounds()[0];\n" +
                "            } else {\n" +
                "                boundType = wildcardType.getLowerBounds()[0];\n" +
                "            }\n" +
                "            return fromJson(jsonReader, boundType);\n" +
                "        }\n" +
                "        return sGson.fromJson(jsonReader, type);\n" +
                "    }\n" +
                "\n" +
                "    private static <T> T fromJsonNormalBean(JsonReader jsonReader, Type type) throws IOException {\n" +
                "        String className = null;\n" +
                "        if (type instanceof Class) {\n" +
                "            className = ((Class) type).getName();\n" +
                "        }\n" +
                "        if(jsonReader.peek() == JsonToken.NULL){\n" +
                "            return null;\n" +
                "        }" +
                "        switch (className) {");
        writeFromJson(builder, jsonClassProxyInfos);
        builder.append("case \"java.lang.Boolean\":\n" +
                "                return (T) (Boolean) jsonReader.nextBoolean();\n" +
                "            case \"java.lang.Byte\":\n" +
                "                int i = jsonReader.nextInt();\n" +
                "                return (T) (Byte) (byte) (0xff & i);\n" +
                "            case \"java.lang.Character\":\n" +
                "                return (T) (Character) jsonReader.nextString().charAt(0);\n" +
                "            case \"java.lang.Integer\":\n" +
                "                return (T) (Integer) jsonReader.nextInt();\n" +
                "            case \"java.lang.Long\":\n" +
                "                return (T) (Long) jsonReader.nextLong();\n" +
                "            case \"java.lang.Short\":\n" +
                "                return (T) (Short) (short) jsonReader.nextInt();\n" +
                "            case \"java.lang.Float\":\n" +
                "                return (T) (Float) (float) jsonReader.nextDouble();\n" +
                "            case \"java.lang.Double\":\n" +
                "                return (T) (Double) (double) jsonReader.nextDouble();\n" +
                "            case \"java.lang.String\":\n" +
                "                return (T) jsonReader.nextString();\n" +
                "            default:\n" +
                "                return sGson.fromJson(jsonReader, type);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    private static <T> T getBaseValueFrom(String value, Type type) {\n" +
                "        String className = null;\n" +
                "        if (type instanceof Class) {\n" +
                "            className = ((Class) type).getName();\n" +
                "        }\n" +
                "        switch (className) {\n" +
                "            case \"java.lang.Boolean\":\n" +
                "                return (T) (Boolean) (Boolean.parseBoolean(value));\n" +
                "            case \"java.lang.Byte\":\n" +
                "                return (T) (Byte) (Byte.parseByte(value));\n" +
                "            case \"java.lang.Character\":\n" +
                "                if (value != null && value.length() != 0) {\n" +
                "                    return (T) (Character) (value.charAt(0));\n" +
                "                } else {\n" +
                "                    return null;\n" +
                "                }\n" +
                "            case \"java.lang.Integer\":\n" +
                "                return (T) (Integer) (Integer.parseInt(value));\n" +
                "            case \"java.lang.Long\":\n" +
                "                return (T) (Long) (Long.parseLong(value));\n" +
                "            case \"java.lang.Short\":\n" +
                "                return (T) (Short) (Short.parseShort(value));\n" +
                "            case \"java.lang.Float\":\n" +
                "                return (T) (Float) (Float.parseFloat(value));\n" +
                "            case \"java.lang.Double\":\n" +
                "                return (T) (Double) (Double.parseDouble(value));\n" +
                "            case \"java.lang.String\":\n" +
                "                return (T) value;\n" +
                "        }\n" +
                "        return sGson.fromJson(value, type);\n" +
                "    }\n" +
                "\n" +
                "    private static String getBaseValueString(Object value) {\n" +
                "        return value.toString();\n" +
                "    }" +
                "\n" +
                "\n" +
                "    private static <T> T createCollection(Type type) {\n" +
                "        String className = null;\n" +
                "        Class clazz = (Class) type;\n" +
                "        if (type instanceof Class) {\n" +
                "            className = clazz.getName();\n" +
                "        }\n" +
                "        if (className.equals(List.class.getName()) || className.equals(ArrayList.class.getName())) {\n" +
                "            return (T) new ArrayList();\n" +
                "        }\n" +
                "        if (className.equals(LinkedList.class.getName())) {\n" +
                "            return (T) new LinkedList<>();\n" +
                "        }\n" +
                "\n" +
                "        if (className.equals(Set.class.getName()) || className.equals(HashSet.class.getName())) {\n" +
                "            return (T) new HashSet<>();\n" +
                "        }\n" +
                "        if (className.equals(TreeSet.class.getName())) {\n" +
                "            return (T) new TreeSet<>();\n" +
                "        }\n" +
                "        if (className.equals(LinkedHashSet.class.getName())) {\n" +
                "            return (T) new LinkedHashSet<>();\n" +
                "        }\n" +
                "\n" +
                "        if (className.equals(Map.class.getName()) || className.equals(LinkedHashMap.class.getName())) {\n" +
                "            return (T) new LinkedHashMap<>();\n" +
                "        }\n" +
                "        if (className.equals(TreeMap.class.getName())) {\n" +
                "            return (T) new TreeMap<>();\n" +
                "        }\n" +
                "        if (className.equals(HashMap.class.getName())) {\n" +
                "            return (T) new HashMap<>();\n" +
                "        }\n" +
                "        if (className.equals(WeakHashMap.class.getName())) {\n" +
                "            return (T) new WeakHashMap<>();\n" +
                "        }\n" +
                "        if (className.equals(Hashtable.class.getName())) {\n" +
                "            return (T) new Hashtable<>();\n" +
                "        }\n" +
                "        if (className.equals(IdentityHashMap.class.getName())) {\n" +
                "            return (T) new IdentityHashMap<>();\n" +
                "        }\n" +
                "\n" +
                "        try {\n" +
                "            return (T) clazz.newInstance();\n" +
                "        } catch (InstantiationException e) {\n" +
                "            e.printStackTrace();\n" +
                "        } catch (IllegalAccessException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "        return null;\n" +
                "    }"+
                "\n" +
                "    private static boolean isBaseValue(Type type) {\n" +
                "        String className = null;\n" +
                "        if (type instanceof Class) {\n" +
                "            className = ((Class) type).getName();\n" +
                "        }\n" +
                "        switch (className) {\n" +
                "            case \"java.lang.Boolean\":\n" +
                "            case \"java.lang.Byte\":\n" +
                "            case \"java.lang.Character\":\n" +
                "            case \"java.lang.Integer\":\n" +
                "            case \"java.lang.Long\":\n" +
                "            case \"java.lang.Short\":\n" +
                "            case \"java.lang.Float\":\n" +
                "            case \"java.lang.Double\":\n" +
                "            case \"java.lang.String\":\n" +
                "                return true;\n" +
                "        }\n" +
                "        return false;\n" +
                "    }\n"+
                "\n"+
                "    public static Gson getGson() {\n" +
                "        return sGson;\n" +
                "    }\n" +
                "\n" +
                "    public static void setGson(Gson gson) {\n" +
                "        sGson = gson;\n" +
                "    }");
        builder.append(" }\n");
        return builder.toString();
    }

    private void writeToJson(StringBuilder builder, List<JsonClassProxyInfo> jsonClassProxyInfos) {
        for (JsonClassProxyInfo info : jsonClassProxyInfos) {
            builder.append(" if (object instanceof " + info.getClassName() + "){\n");
            builder.append(info.getProxyClassFullName() + ".toJson(jsonWriter, (" + info.getClassName() + ") object);\n");
            builder.append("return;\n");
            builder.append("}\n");
        }
    }

    private void writeFromJson(StringBuilder builder, List<JsonClassProxyInfo> jsonClassProxyInfos) {
        for (JsonClassProxyInfo info : jsonClassProxyInfos) {
            builder.append(" case \"" + info.getClassName() + "\":\n");
            builder.append("return (T) " + info.getProxyClassFullName() + ".fromJson(jsonReader);\n");
        }
    }

    private void initImport() {
        importSet.add(Gson.class.getName());
        importSet.add(JsonSyntaxException.class.getName());
        importSet.add(TypeToken.class.getName());
        importSet.add(JsonToken.class.getName());
        importSet.add(JsonReader.class.getName());
        importSet.add(JsonWriter.class.getName());
        importSet.add(IOException.class.getName());
        importSet.add(StringReader.class.getName());
        importSet.add(StringWriter.class.getName());
        importSet.add(Array.class.getName());
        importSet.add(GenericArrayType.class.getName());
        importSet.add(ParameterizedType.class.getName());
        importSet.add(Type.class.getName());
        importSet.add(WildcardType.class.getName());
        importSet.add(List.class.getName());
        importSet.add(ArrayList.class.getName());
        importSet.add(LinkedList.class.getName());
        importSet.add(HashSet.class.getName());
        importSet.add(TreeSet.class.getName());
        importSet.add(LinkedHashSet.class.getName());
        importSet.add(Map.class.getName());
        importSet.add(TreeMap.class.getName());
        importSet.add(HashMap.class.getName());
        importSet.add(WeakHashMap.class.getName());
        importSet.add(Hashtable.class.getName());
        importSet.add(LinkedHashMap.class.getName());
        importSet.add(IdentityHashMap.class.getName());
        importSet.add(Set.class.getName());
    }
}
