package com.model;

/**
 * Created by hdy on 17-8-21.
 * 用于显示首页的置顶的影视和电视剧
 */
public class MovieTop {
    //影视指向地址
    private String url;
    //影视标题
    private String title;
    //发布时间
    private String time;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MovieTop(String url, String title, String time) {
        this.url = url;
        this.title = title;
        this.time = time;
    }

    @Override
    public String toString() {
        return "MovieTop{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
