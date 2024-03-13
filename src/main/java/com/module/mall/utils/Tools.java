package com.module.mall.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tools {
    /**
     * 指定批次数量的List分批
     *
     * @param batchList
     * @param num
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> toBatchList(List<T> batchList, int num) {
        List<List<T>> out = new ArrayList<>();
        List<T> itemOut = new ArrayList<>();
        for (int i = 0; i < batchList.size(); i++) {
            itemOut.add(batchList.get(i));
            if ((i + 1) % num == 0 || (batchList.size() - 1) == i) {
                out.add(itemOut);
                itemOut = new ArrayList<>();
            }
        }
        return out;
    }

    /**
     * 默认200个每个批次的List分批
     *
     * @param batchList
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> toBatchList(List<T> batchList) {
        List<List<T>> out = new ArrayList<>();
        List<T> itemOut = new ArrayList<>();
        for (int i = 0; i < batchList.size(); i++) {
            itemOut.add(batchList.get(i));
            if ((i + 1) % 200 == 0 || (batchList.size() - 1) == i) {
                out.add(itemOut);
                itemOut = new ArrayList<>();
            }
        }
        return out;
    }

    /**
     * 模仿js的模版字符串的写法
     * String template = "name:${name},age:${age}";
     * Map<String, Object> placeholders = new HashMap<>();
     * placeholders.put("name", "John");
     * placeholders.put("age", 25);
     * String result = templateStringWithMap(template, placeholders);
     *
     * @param template
     * @param placeholders
     * @return
     */
    public static String templateStringWithMap(String template, Map<String, Object> placeholders) {
        StringBuilder result = new StringBuilder();
        int index = 0;
        while (index < template.length()) {
            int startPlaceholderIndex = template.indexOf("${", index);
            if (startPlaceholderIndex == -1) {
                result.append(template.substring(index));
                break;
            }
            int endPlaceholderIndex = template.indexOf("}", startPlaceholderIndex + 2);
            if (endPlaceholderIndex == -1) {
                throw new IllegalArgumentException("Invalid template string, missing closing curly brace for placeholder");
            }
            String placeholderName = template.substring(startPlaceholderIndex + 2, endPlaceholderIndex);
            Object placeholderValue = placeholders.get(placeholderName);
            if (placeholderValue == null) {
                placeholderValue = "";
            }
            result.append(template.substring(index, startPlaceholderIndex));
            result.append(placeholderValue.toString());
            index = endPlaceholderIndex + 1;
        }
        return result.toString();
    }

    public static void main(String[] args) {

    }
}
