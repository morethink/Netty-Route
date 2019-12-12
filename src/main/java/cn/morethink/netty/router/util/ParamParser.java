package cn.morethink.netty.router.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 李文浩
 * @date 2019/12/11
 */
public class ParamParser {

    public static final ParamParser INSTANCE = new ParamParser();

    @FunctionalInterface
    interface ParseValue {
        Object parseValue(String value);
    }

    @FunctionalInterface
    interface ParseArrayValue {
        Object parseValue(Class c, List<String> paramList);
    }

    private Map<Class, ParseValue> map = new HashMap<>(16);

    private Map<Class, ParseValue> defaultValueMap = new HashMap<>(8);

    private Map<Class, ParseArrayValue> arrayMap = new HashMap<>(16);


    private ParamParser() {
        map.put(String.class, value -> value);
        map.put(Integer.class, Integer::valueOf);
        map.put(Long.class, Long::valueOf);
        map.put(Double.class, Double::valueOf);
        map.put(Float.class, Float::valueOf);
        map.put(Boolean.class, Boolean::valueOf);
        map.put(Byte.class, Byte::valueOf);
        map.put(int.class, Integer::parseInt);
        map.put(long.class, Long::parseLong);
        map.put(double.class, Double::parseDouble);
        map.put(float.class, Float::parseFloat);
        map.put(boolean.class, Boolean::parseBoolean);
        map.put(byte.class, Byte::parseByte);

        defaultValueMap.put(int.class, value -> Integer.parseInt("0"));
        defaultValueMap.put(long.class, value -> 0L);
        defaultValueMap.put(double.class, value -> 0.0d);
        defaultValueMap.put(float.class, value -> 0.0f);
        defaultValueMap.put(boolean.class, value -> false);
        defaultValueMap.put(byte.class, value -> 0);

        arrayMap.put(int.class, (c, paramList) -> paramList.stream().mapToInt(Integer::valueOf).toArray());
        arrayMap.put(long.class, (c, paramList) -> paramList.stream().mapToLong(Long::valueOf).toArray());
        arrayMap.put(double.class, (c, paramList) -> paramList.stream().mapToDouble(Double::valueOf).toArray());
        arrayMap.put(String.class, (c, paramList) -> paramList.toArray(new String[paramList.size()]));
    }

    public Object parseValue(Class c, String value) {
        if (StringUtils.isNotEmpty(value)) {
            return map.get(c).parseValue(value);
        }
        return defaultValueMap.getOrDefault(c, value1 -> null).parseValue(value);
    }

    public Object parseArray(Class c, List<String> paramList) {
        return arrayMap.getOrDefault(c, (c1, p1) -> null).parseValue(c, paramList);
    }
}
