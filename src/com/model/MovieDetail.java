package com.model;

/**
 * Created by hdy on 2017/8/21.
 * 存放详细影片界面的数据
 */
public class MovieDetail {

    private String title;
    private String webText;
    private String downloadUrl;
    private String publishDate;

    public MovieDetail(String title, String webText, String downloadUrl, String publishDate) {
        this.title = title;
        this.webText = webText;
        this.downloadUrl = downloadUrl;
        this.publishDate = publishDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebText() {
        return webText;
    }

    public void setWebText(String webText) {
        this.webText = webText;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        return "MovieDetail{" +
                "title='" + title + '\'' +
                ", webText='" + webText + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", publishDate='" + publishDate + '\'' +
                '}';
    }
}
