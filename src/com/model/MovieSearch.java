package com.model;

import com.utils.MovieUtils;

import java.util.ArrayList;

/**
 * Created by hdy on 17-8-21.
 * 用于存放搜索或分类结果
 */
public class MovieSearch {
    //判断是普通搜索还是分类搜索
    //0 普通搜索 1 分类搜索
    private int type = 0;
    //分类分页前缀
    private String preffix;
    //分类分页后缀
    private String format = ".html";
    //主机名称
    private String host;
    public static final Integer TYPE_SEARCH = 1;
    public static final Integer NORMAL_SEARCH = 0;

    private int page;
    private int pageCount;
    private String searchName;
    private ArrayList<MovieSearchDetail> details = new ArrayList<MovieSearchDetail>();

    public MovieSearch(int page, int pageCount, String searchName, ArrayList<MovieSearchDetail> details) {
        this.page = page;
        this.pageCount = pageCount;
        this.searchName = searchName;
        this.details = details;
    }

    /**
     * 跳转到首页
     */
    public MovieSearch toFirst() {
        page = 0;
        if (type == NORMAL_SEARCH) {
            return MovieUtils.MovieSearchForward(this);

        }
        return MovieUtils.movieTypeSearchFoward(this);
    }

    public MovieSearch nextPage() {
        this.page++;
        if (page > pageCount) {
            return null;
        }
        if (type == NORMAL_SEARCH) {
            return MovieUtils.MovieSearchForward(this);
        }
        return MovieUtils.movieTypeSearchFoward(this);
    }

    public MovieSearch formerPage() {
        this.page--;
        if (page < 0) {
            page = 0;
        }
        if (type == NORMAL_SEARCH) {
            return MovieUtils.MovieSearchForward(this);
        }
        return MovieUtils.movieTypeSearchFoward(this);
    }

    /**
     * 页面跳转
     */
    public MovieSearch changePage(int page) {
        if (page > pageCount) {
            return null;
        }
        this.page = page;
        if (type == NORMAL_SEARCH) {
            return MovieUtils.MovieSearchForward(this);
        }
        return MovieUtils.movieTypeSearchFoward(this);
    }

    /**
     * 跳转到最后一页
     */
    public MovieSearch toEnd() {
        page = pageCount;
        if (type == NORMAL_SEARCH) {
            return MovieUtils.MovieSearchForward(this);
        }
        return MovieUtils.movieTypeSearchFoward(this);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public ArrayList<MovieSearchDetail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<MovieSearchDetail> details) {
        this.details = details;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPreffix(String preffix) {
        this.preffix = preffix;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public static Integer getTypeSearch() {
        return TYPE_SEARCH;
    }

    public static Integer getNormalSearch() {
        return NORMAL_SEARCH;
    }

    public int getType() {
        return type;
    }

    public String getPreffix() {
        return preffix;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public String toString() {
        return "MovieSearch{" +
                "type=" + type +
                ", preffix='" + preffix + '\'' +
                ", format='" + format + '\'' +
                ", page=" + page +
                ", pageCount=" + pageCount +
                ", searchName='" + searchName + '\'' +
                ", details=" + details +
                '}';
    }
}
