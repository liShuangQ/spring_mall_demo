package com.module.mall.utils;

import java.util.ArrayList;
import java.util.List;

public class Tools {
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

}
