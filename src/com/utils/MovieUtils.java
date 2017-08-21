package com.utils;

import com.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by hdy on 17-8-21.
 */
public class MovieUtils {

    /**
     * 用于搜索的跳转
     */
    public static MovieSearch MovieSearchReword(MovieSearch movieSearch) {
        ArrayList<MovieSearchDetail> details = new ArrayList<MovieSearchDetail>();
        Document document = null;
        try {
            String searchUrl = "/plus/search.php?keyword=" + URLEncoder.encode(movieSearch.getSearchName(), "gb2312") + "&amp;searchtype=titlekeyword&amp;channeltype=0&amp;orderby=&amp;kwtype=0&amp;pagesize=10&amp;typeid=0&amp;TotalResult=40&amp;PageNo=" + movieSearch.getPage();
            document = Jsoup.parse(NetworkUtils.get(searchUrl));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        String content = NetworkUtils.read("/home/hdy/Desktop/index2.html");
        Elements elements = document.select("div.co_content8 > ul > table[width] > tbody");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            Elements trs = element.getElementsByTag("tr");
            if (trs.size() == 3) {
                //等于三个才是正常
                Element tr1 = trs.get(0);
                Element tr2 = trs.get(1);
                //第一个获取标题和地址
                String href = tr1.select("td[width$=55%] > b > a").get(0).attr("href");
                System.out.println(href);
                String title = tr1.select("td[width$=55%] > b > a").get(0).text().replaceAll("<font color=\"red\">", "").replaceAll("</font>", "");
                System.out.println(title);
                //第二个获取描述
                String description = tr2.getElementsByTag("td").get(0).text().replaceAll("<font color=\"red\">", "").replaceAll("</font>", "");
                System.out.println(description);
                MovieSearchDetail detail = new MovieSearchDetail(title, href, description);
                details.add(detail);
            } else {
                continue;
            }
        }
        MovieSearch search = new MovieSearch(movieSearch.getPage(), movieSearch.getPageCount(), movieSearch.getSearchName(), details);
        return null;
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
        ArrayList<MovieSearchDetail> details = new ArrayList<MovieSearchDetail>();
//        String content = NetworkUtils.read("/home/hdy/Desktop/index2.html");
        Document document = Jsoup.parse(content);
        Elements elements = document.select("div.co_content8 > ul > table[width] > tbody");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            Elements trs = element.getElementsByTag("tr");
            if (trs.size() == 3) {
                //等于三个才是正常
                Element tr1 = trs.get(0);
                Element tr2 = trs.get(1);
                //第一个获取标题和地址
                String href = tr1.select("td[width$=55%] > b > a").get(0).attr("href");
                System.out.println(href);
                String title = tr1.select("td[width$=55%] > b > a").get(0).text().replaceAll("<font color=\"red\">", "").replaceAll("</font>", "");
                System.out.println(title);
                //第二个获取描述
                String description = tr2.getElementsByTag("td").get(0).text().replaceAll("<font color=\"red\">", "").replaceAll("</font>", "");
                System.out.println(description);
                MovieSearchDetail detail = new MovieSearchDetail(title, href, description);
                details.add(detail);
            } else {
                continue;
            }
        }

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
            //说明是搜索不到
            MovieSearch search = new MovieSearch(1, 0, name, details);
        }
        return null;
    }


    /**
     * 获取首页最新发布
     *
     * @return
     */
    public static Movie getIndexLasted() {
        Movie movie = null;
        String content = NetworkUtils.get("http://www.dytt8.net/");
//        String content = NetworkUtils.read("/home/hdy/Desktop/index.html");
        Document document = Jsoup.parse(content);
        //获取推荐的ul
        Elements elements = document.select("div.co_area2 > div.co_content2 > ul");
        movie = new Movie();
        Element element = elements.get(0);
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
        return movie;
    }


    /**
     * 获取首页的推荐信息等等....
     */
    public static Movie getIndexOthers() {
        Movie movie = null;
        String content = NetworkUtils.get("http://www.dytt8.net/");
//        String content = NetworkUtils.read("/home/hdy/Desktop/index.html");
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
        Movie movie = null;
        {
            String content = NetworkUtils.get("http://www.dytt8.net/");
//            String content = NetworkUtils.read("/home/hdy/Desktop/index.html");
            Document document = Jsoup.parse(content);
            Elements elements = document.select("table > tbody > tr");
            //临时存放视频类型
            LinkedList<MovieType> movieTypes = new LinkedList<MovieType>();
            ArrayList<MovieTop> movieTops = new ArrayList<MovieTop>();
            movie = new Movie();
            int times = 0;
            for (Element link : elements) {
                times++;
                Elements select = link.select("td.inddline");
                if (select.size() == 2) {
                    //电影相关信息
                    Elements a = select.get(0).getElementsByTag("a");
                    //类型指向地址
                    String type_href = a.get(0).attr("href");
                    //类型内容
                    String type_content = a.get(0).text();
                    //资源指向地址
                    String movie_href = a.get(1).attr("href");
                    //资源标题
                    String movie_title = a.get(1).text();
                    //时间
                    String time = select.get(1).getElementsByTag("font").get(0).text();
                    if (movieTypes.size() == 0) {
                        MovieType movieType = new MovieType(type_href, type_content);
                        movieTypes.add(movieType);
                    } else {
                        MovieType last = movieTypes.getLast();
                        if (type_content.equals(last.getTitle())) {
                            //说明是一样的
                            movieTops.add(new MovieTop(movie_href, movie_title, time));
                            if (times == elements.size() - 1) {
                                //如果一样,说明是最后了
                                movie.getMap().put(movieTypes.getLast(), movieTops);
                            }
                        } else {
                            //说明是不一样的
                            movie.getMap().put(movieTypes.getLast(), movieTops);
                            movieTops = new ArrayList<MovieTop>();
                            MovieType movieType = new MovieType(type_href, type_content);
                            movieTypes.add(movieType);
                        }
                    }
                }
            }
        }
        return movie;
    }
}
