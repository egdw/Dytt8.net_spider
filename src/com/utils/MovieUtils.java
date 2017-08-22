package com.utils;

import com.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by hdy on 17-8-21.
 * 主要的工具类
 * <p>
 * 里面实现了各种数据的解析和封装
 */
public class MovieUtils {


    /**
     * 返回相应类型检索的详细数据
     * <p>
     * 由于有几种形式的界面
     * <p>
     * 所以返回object数组
     * <p>
     * object[0] 不为null的时候就是经典影片了 主页布局
     * <p>
     * object[1] 不为null的时候就是高分经典了 详细文章布局
     * <p>
     * object[2] 不为null的时候就是其他的频道了 搜索界面布局
     * <p>
     * 这样就可以区分是哪个类和布局了
     * 当然也可以使用instanceof...
     *
     * @param url
     * @return
     */
    public static Object[] getTypeDetail(String title, String url) {
        Object[] obj = new Object[3];
        String content = NetworkUtils.get(url);
        if (content == null) {
            return null;
        }
        Document document = null;
        if ("经典影片".equals(title)) {
            document = Jsoup.parse(content);
            Movie movie = ParseUtils.parseIndex(document);
            obj[0] = movie;
            //这个网站是首页界面的
        } else if ("高分经典".equals(title)) {
            document = Jsoup.parse(content);
            MovieDetail detail = ParseUtils.parseDetail(document);
            obj[1] = detail;
            //这个详细页面界面的
        } else if ("加入收藏".equals(title) || "收藏本站".equals(title) || "设为主页".equals(title)) {
            return null;
            //这个不执行
        } else {
            document = Jsoup.parse(content);
            //其他都是搜索界面的
            ArrayList<MovieSearchDetail> details = ParseUtils.parseTypeSearch(document);
            if (details == null || details.size() == 0) {
                return null;
            }
            //获取页码和页数
            Element elements2 = null;
            if (document.select("div.x").size() >= 2) {
                elements2 = document.select("div.x").get(1);
            } else {
                return null;
            }
            //说明获取正确了
            String attr = elements2.select("a").get(elements2.getElementsByTag("a").size() - 1).attr("href");
            //获取到最后一个页码
            Integer pageNum = Integer.valueOf(attr.substring(attr.lastIndexOf("_") + 1, attr.lastIndexOf(".")));
            //获取当前分页的前缀
            String host = url.substring(0, url.lastIndexOf("/") + 1);
            System.out.println(host);
            String preffix = attr.substring(0, attr.lastIndexOf("_") + 1);
            MovieSearch search = new MovieSearch(1, pageNum, title, details);
            search.setPreffix(preffix);
            search.setHost(host);
            search.setType(MovieSearch.TYPE_SEARCH);
            obj[2] = search;
        }
        return obj;
    }


    /**
     * 获取详细的单页影片数据
     *
     * @param url 网页地址
     */
    public static MovieDetail detail(String url) {
        if (!url.contains("http")) {
            url = "http://www.ygdy8.com/" + url;
        }

        String content = NetworkUtils.get(url);
        if (content == null) {
            return null;
        }
        Document document = Jsoup.parse(content);
        return ParseUtils.parseDetail(document);
    }

    /**
     * 用于分类的跳转
     *
     * @return
     */
    public static MovieSearch movieTypeSearchFoward(MovieSearch movieSearch) {
        String searchUrl = null;
        if (movieSearch.getPreffix().contains("http")) {
            searchUrl = movieSearch.getPreffix() + movieSearch.getPage() + movieSearch.getFormat();
        } else {
            searchUrl = movieSearch.getHost() + movieSearch.getPreffix() + movieSearch.getPage() + movieSearch.getFormat();
        }
        System.out.println(searchUrl);
        String content = NetworkUtils.get(searchUrl);
        if (content == null) {
            return null;
        }
        Document document = Jsoup.parse(content);
        ArrayList<MovieSearchDetail> details = ParseUtils.parseTypeSearch(document);
        movieSearch.setDetails(details);
        return movieSearch;
    }


    /**
     * 用于搜索的跳转
     */
    public static MovieSearch MovieSearchForward(MovieSearch movieSearch) {
        Document document = null;
        try {
            String searchUrl = "http://s.dydytt.net/plus/search.php?keyword=" + URLEncoder.encode(movieSearch.getSearchName(), "gb2312") + "&searchtype=titlekeyword&channeltype=0&orderby=&kwtype=0&pagesize=10&typeid=0&TotalResult=40&PageNo=" + movieSearch.getPage();
            String content = NetworkUtils.get(searchUrl);
            if (content == null) {
                return null;
            }
            document = Jsoup.parse(content);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArrayList<MovieSearchDetail> details = ParseUtils.parseSearch(document);
        movieSearch.setDetails(details);
        return movieSearch;
    }

    /**
     * 搜索功能
     */
    public static MovieSearch search(String name) {
        String searchUrl = null;
        try {
            searchUrl = "http://s.dydytt.net/plus/search.php?kwtype=0&keyword=" + URLEncoder.encode(name, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String content = NetworkUtils.get(searchUrl);
        if (content == null) {
            return null;
        }
        Document document = Jsoup.parse(content);
        ArrayList<MovieSearchDetail> details = ParseUtils.parseSearch(document);

        //获取页码和页数
        Elements elements2 = document.select("div.co_content8 > ul > table[cellpadding] > tbody");
        if (elements2.size() == 1) {
            //说明获取正确了
            String attr = elements2.get(0).select("tr > td[width$=30] > a").attr("href");
            //获取到最后一个页码
            Integer pageNum = Integer.valueOf(attr.substring(attr.indexOf("PageNo=") + 7));
            MovieSearch search = new MovieSearch(1, pageNum, name, details);
            return search;
        } else {
            //说明是没有页码
            MovieSearch search = new MovieSearch(1, 0, name, details);
            return search;
        }
    }


    /**
     * 获取首页最新发布
     *
     * @return
     */
    public static Movie getIndexLasted() {
        String content = NetworkUtils.get("http://www.dytt8.net/");
        if (content == null) {
            return null;
        }
        Document document = Jsoup.parse(content);
        //获取推荐的ul
        return ParseUtils.parseIndexLasted(document);
    }


    /**
     * 获取首页的推荐信息等等....
     */
    public static Movie getIndexOthers() {
        Movie movie = null;
        String content = NetworkUtils.get("http://www.dytt8.net/");
        if (content == null) {
            return null;
        }
        Document document = Jsoup.parse(content);
        //获取推荐的ul
        Elements elements = document.select("div.co_area2 > div.co_content4 > ul");
        movie = new Movie();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            String type_title = element.parent().parent().select("div.title_all > p").text();
            MovieType type = new MovieType(null, type_title);
            Elements as = element.getElementsByTag("a");
            ArrayList<MovieTop> movieTops = new ArrayList<MovieTop>();
            for (int j = 0; j < as.size(); j++) {
                Element a = as.get(j);
                String href = a.attr("href");
                String title = a.text();
                MovieTop top = new MovieTop(href, title, null);
                movieTops.add(top);
            }
            movie.getMap().put(type, movieTops);
        }
        return movie;
    }


    /**
     * 获取首页相关的类型和信息
     *
     * @return
     */
    public static Movie getIndexTypeAndInfo() {
        String content = NetworkUtils.get("http://www.dytt8.net/");
        if (content == null) {
            return null;
        }
        Document document = Jsoup.parse(content);
        return ParseUtils.parseIndex(document);
    }
}
