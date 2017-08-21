package com.model;

/**
 * Created by hdy on 17-8-21.
 * 用于存放电影类型
 */
public class MovieType {
    //类型
    private String url;
    private String title;

    public MovieType(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "MovieType{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
