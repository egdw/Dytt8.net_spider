package com.model;

import com.utils.MovieUtils;

import java.util.ArrayList;

/**
 * Created by hdy on 17-8-21.
 */
public class MovieSearch {
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
        MovieSearch search = MovieUtils.MovieSearchReword(this);
        return search;
    }

    public MovieSearch nextPage() {
        this.page++;
        if (page > pageCount) {
            return null;
        }
        MovieSearch search = MovieUtils.MovieSearchReword(this);
        return search;
    }

    public MovieSearch formerPage() {
        this.page--;
        if (page < 0) {
            page = 0;
        }
        MovieSearch search = MovieUtils.MovieSearchReword(this);
        return search;
    }

    /**
     * 页面跳转
     */
    public MovieSearch changePage(int page) {
        if (page > pageCount) {
            return null;
        }
        this.page = page;
        MovieSearch search = MovieUtils.MovieSearchReword(this);
        return search;
    }

    /**
     * 跳转到最后一页
     */
    public MovieSearch toEnd() {
        page = pageCount;
        MovieSearch search = MovieUtils.MovieSearchReword(this);
        return search;
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

    @Override
    public String toString() {
        return "MovieSearch{" +
                "page=" + page +
                ", pageCount=" + pageCount +
                ", searchName='" + searchName + '\'' +
                ", details=" + details +
                '}';
    }
}
