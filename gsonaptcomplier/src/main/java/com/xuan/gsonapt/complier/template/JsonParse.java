package com.xuan.gsonapt.complier.template;

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
 * the code template for the GsonAPT class
 * Created by chenxiaoxuan1 on 17/2/8.
 */

class JsonParse {
    private static Gson sGson = new Gson();

    public static String toJson(Object object) {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);
        jsonWriter.setSerializeNulls(sGson.serializeNulls());
        try {
            toJson(jsonWriter, object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    public static void toJson(JsonWriter jsonWriter, Object object) throws IOException {
        if (object == null) {
            jsonWriter.nullValue();
            return;
        }
        Class clazz = object.getClass();
        if (clazz.isArray()) {
            Object[] reslut = (Object[]) object;
            jsonWriter.beginArray();
            for (Object item : reslut) {
                toJson(jsonWriter, item);
            }
            jsonWriter.endArray();
        } else if (object instanceof Map) {
            Map map = (Map) object;
            Set<Map.Entry> set = map.entrySet();
            if (set.size() == 0) {
                sGson.toJson(object, object.getClass(), jsonWriter);
                return;
            }
            for (Object item : set) {
                Map.Entry entry = (Map.Entry) item;
                if (entry.getKey() != null && !isBaseValue(entry.getKey().getClass())) {
                    sGson.toJson(object, object.getClass(), jsonWriter);
                    return;
                }
            }
            jsonWriter.beginObject();
            for (Object item : set) {
                Map.Entry entry = (Map.Entry) item;
                jsonWriter.name(getBaseValueString(entry.getKey()));
                toJson(jsonWriter, entry.getValue());
            }
            jsonWriter.endObject();
        } else if (object instanceof List) {
            List<Object> list = (List<Object>) object;
            jsonWriter.beginArray();
            for (Object item : list) {
                toJson(jsonWriter, item);
            }
            jsonWriter.endArray();
        } else if (object instanceof Set) {
            Set<Object> set = (Set<Object>) object;
            jsonWriter.beginArray();
            for (Object item : set) {
                toJson(jsonWriter, item);
            }
            jsonWriter.endArray();
        } else {
            toJsonNormal(jsonWriter, object);
        }
    }

    private static void toJsonNormal(JsonWriter jsonWriter, Object object) throws IOException {
        if (object == null) {
            jsonWriter.nullValue();
            return;
        }
        Class type = object.getClass();

        if (object instanceof Boolean) {
            jsonWriter.value((Boolean) object);
            return;
        }

        if (object instanceof Byte) {
            jsonWriter.value((Byte) object);
            return;
        }

        if (object instanceof Character) {
            jsonWriter.value((Character) object);
            return;
        }
        if (object instanceof Integer) {
            jsonWriter.value((Integer) object);
            return;
        }

        if (object instanceof Long) {
            jsonWriter.value((Long) object);
            return;
        }

        if (object instanceof Short) {
            jsonWriter.value((Short) object);
            return;
        }
        if (object instanceof Float) {
            jsonWriter.value((Float) object);
            return;
        }
        if (object instanceof Double) {
            jsonWriter.value((Double) object);
            return;
        }

        if (object instanceof String) {
            jsonWriter.value((String) object);
            return;
        }

        if (object.getClass().equals(Bean.class)) {//all the json bean parser
            Bean$$JsonParse.toJson(jsonWriter, (Bean) object);
            return;
        }
        sGson.toJson(object, object.getClass(), jsonWriter);
    }

    public static <T> T fromJson(String json, Type type) throws JsonSyntaxException {
        StringReader stringReader = new StringReader(json);
        JsonReader jsonReader = new JsonReader(stringReader);
        jsonReader.setLenient(true);
        try {
            return fromJson(jsonReader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(JsonReader jsonReader, Type type) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        if (type instanceof Class) {
            Class<?> c = (Class<?>) type;
            if (c.isArray()) {
                Class<?> conponentType = c.getComponentType();
                List<Object> list = new ArrayList<>();
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    list.add(fromJson(jsonReader, conponentType));
                }
                jsonReader.endArray();
                int i = 0;
                Object[] reslut = (Object[]) Array.newInstance(conponentType, list.size());
                for (Object obj : list) {
                    reslut[i++] = obj;
                }
                return (T) reslut;
            } else {
                return fromJsonNormalBean(jsonReader, type);
            }

        } else if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            TypeToken<T> typeToken = (TypeToken<T>) TypeToken.get(type);
            Class<? super T> rawType = typeToken.getRawType();
            if (List.class.isAssignableFrom(rawType)) {
                List<Object> list = createCollection(rawType);
                jsonReader.beginArray();
                Type[] actualType = p.getActualTypeArguments();
                while (jsonReader.hasNext()) {
                    list.add(fromJson(jsonReader, actualType[0]));
                }
                jsonReader.endArray();
                return (T) list;
            } else if (Map.class.isAssignableFrom(rawType)) {
                Map map = createCollection(rawType);
                Type[] actualType = p.getActualTypeArguments();
                if (isBaseValue(actualType[0])) {
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String firstMapName = jsonReader.nextName();
                        JsonReader mapJsonReader = new JsonReader(new StringReader(firstMapName));
                        Object keyBean = getBaseValueFrom(firstMapName, actualType[0]);

                        Object mapBean = fromJson(jsonReader, actualType[1]);
                        map.put(keyBean, mapBean);
                    }
                    jsonReader.endObject();
                    return (T) map;
                }
            } else if (Set.class.isAssignableFrom(rawType)) {
                Set<Object> set = createCollection(rawType);
                jsonReader.beginArray();
                Type[] actualType = p.getActualTypeArguments();
                while (jsonReader.hasNext()) {
                    set.add(fromJson(jsonReader, actualType[0]));
                }
                jsonReader.endArray();
                return (T) set;
            }
        } else if (type instanceof GenericArrayType) {

        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            //直接用边界类型
            Type boundType;
            if (wildcardType.getLowerBounds().length == 0) {
                boundType = wildcardType.getUpperBounds()[0];
            } else {
                boundType = wildcardType.getLowerBounds()[0];
            }
            return fromJson(jsonReader, boundType);
        }
        return sGson.fromJson(jsonReader, type);
    }

    private static <T> T fromJsonNormalBean(JsonReader jsonReader, Type type) throws IOException {
        String className = null;
        if (type instanceof Class) {
            className = ((Class) type).getName();
        }
        if (jsonReader.peek() == JsonToken.NULL) {
            return null;
        }
        switch (className) {
            case "com.xuan.gsonapt.complier.template.Bean":
                //该bean的解析方法return bean.formJson(JsonReader jsonReader)
                return (T) Bean$$JsonParse.fromJson(jsonReader);
            case "java.lang.Boolean":
                return (T) (Boolean) jsonReader.nextBoolean();
            case "java.lang.Byte":
                int i = jsonReader.nextInt();
                return (T) (Byte) (byte) (0xff & i);
            case "java.lang.Character":
                return (T) (Character) jsonReader.nextString().charAt(0);
            case "java.lang.Integer":
                return (T) (Integer) jsonReader.nextInt();
            case "java.lang.Long":
                return (T) (Long) jsonReader.nextLong();
            case "java.lang.Short":
                return (T) (Short) (short) jsonReader.nextInt();
            case "java.lang.Float":
                return (T) (Float) (float) jsonReader.nextDouble();
            case "java.lang.Double":
                return (T) (Double) (double) jsonReader.nextDouble();
            case "java.lang.String":
                return (T) jsonReader.nextString();
            default:
                return sGson.fromJson(jsonReader, type);
        }
    }

    private static <T> T getBaseValueFrom(String value, Type type) {
        String className = null;
        if (type instanceof Class) {
            className = ((Class) type).getName();
        }
        switch (className) {
            case "java.lang.Boolean":
                return (T) (Boolean) (Boolean.parseBoolean(value));
            case "java.lang.Byte":
                return (T) (Byte) (Byte.parseByte(value));
            case "java.lang.Character":
                if (value != null && value.length() != 0) {
                    return (T) (Character) (value.charAt(0));
                } else {
                    return null;
                }
            case "java.lang.Integer":
                return (T) (Integer) (Integer.parseInt(value));
            case "java.lang.Long":
                return (T) (Long) (Long.parseLong(value));
            case "java.lang.Short":
                return (T) (Short) (Short.parseShort(value));
            case "java.lang.Float":
                return (T) (Float) (Float.parseFloat(value));
            case "java.lang.Double":
                return (T) (Double) (Double.parseDouble(value));
            case "java.lang.String":
                return (T) value;
        }
        return sGson.fromJson(value, type);
    }

    private static boolean isBaseValue(Type type) {
        String className = null;
        if (type instanceof Class) {
            className = ((Class) type).getName();
        }
        switch (className) {
            case "java.lang.Boolean":
            case "java.lang.Byte":
            case "java.lang.Character":
            case "java.lang.Integer":
            case "java.lang.Long":
            case "java.lang.Short":
            case "java.lang.Float":
            case "java.lang.Double":
            case "java.lang.String":
                return true;
        }
        return false;
    }

    private static String getBaseValueString(Object value) {
        return value.toString();
    }

    private static <T> T createCollection(Type type) {
        String className = null;
        Class clazz = (Class) type;
        if (type instanceof Class) {
            className = clazz.getName();
        }
        if (className.equals(List.class.getName()) || className.equals(ArrayList.class.getName())) {
            return (T) new ArrayList();
        }
        if (className.equals(LinkedList.class.getName())) {
            return (T) new LinkedList<>();
        }

        if (className.equals(Set.class.getName()) || className.equals(HashSet.class.getName())) {
            return (T) new HashSet<>();
        }
        if (className.equals(TreeSet.class.getName())) {
            return (T) new TreeSet<>();
        }
        if (className.equals(LinkedHashSet.class.getName())) {
            return (T) new LinkedHashSet<>();
        }

        if (className.equals(Map.class.getName()) || className.equals(LinkedHashMap.class.getName())) {
            return (T) new LinkedHashMap<>();
        }
        if (className.equals(TreeMap.class.getName())) {
            return (T) new TreeMap<>();
        }
        if (className.equals(HashMap.class.getName())) {
            return (T) new HashMap<>();
        }
        if (className.equals(WeakHashMap.class.getName())) {
            return (T) new WeakHashMap<>();
        }
        if (className.equals(Hashtable.class.getName())) {
            return (T) new Hashtable<>();
        }
        if (className.equals(IdentityHashMap.class.getName())) {
            return (T) new IdentityHashMap<>();
        }

        try {
            return (T) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Gson getGson() {
        return sGson;
    }

    public static void setGson(Gson gson) {
        sGson = gson;
    }
}

class Bean {

}

class Bean$$JsonParse {//simulate Bean JsonParser

    public static void toJson(JsonWriter jsonWriter, Bean bean) {

    }

    public static Bean fromJson(JsonReader jsonReader) {
        return new Bean();
    }
}