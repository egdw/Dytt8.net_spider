package com.model;

/**
 * Created by hdy on 17-8-21.
 * 用于显示搜索的model
 */
public class MovieSearchDetail {
    private String title;
    private String url;
    private String description;

    @Override
    public String toString() {
        return "MovieSearchDetail{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


    public MovieSearchDetail(String title, String url, String description) {
        this.title = title;
        this.url = url;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
