package com.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hdy on 17-8-21.
 * 主要的类
 */
public class Movie {
    //存放分类数据
    private Map<MovieType, ArrayList<MovieTop>> map = new HashMap<MovieType, ArrayList<MovieTop>>();

    public Map<MovieType, ArrayList<MovieTop>> getMap() {
        return map;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "map=" + map +
                '}';
    }
}
