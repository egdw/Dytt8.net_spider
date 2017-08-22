package com.test;

import com.model.*;
import com.utils.MovieUtils;
import com.utils.NetworkUtils;
import com.utils.ParseUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by hdy on 17-8-21.
 */
public class Test {

    /**
     * 单个分类查询
     */
    @org.junit.Test
    public void testOne() {
        Object[] detail = MovieUtils.getTypeDetail("旧版综艺", "http://dytt8.net/html/2009zongyi/index.html");
        MovieSearch o = (MovieSearch) detail[2];
        System.out.println(o);
        MovieSearch movieSearch = o.nextPage();
        System.out.println(movieSearch);
    }



    /**
     * 搜索测试
     */
    @org.junit.Test
    public void test3() {
        MovieSearch search = MovieUtils.search("52赫");
        System.out.println(search);
    }


    /**
     * 获取分类测试
     */
    @org.junit.Test
    public void test5() {
        Map<String, String> map = ParseUtils.getType();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            System.out.println(next.getKey() + " " + next.getValue());
        }
    }

    /**
     * 所有分类查询测试
     * 尽量不要使用遍历....
     * 它服务器不行.....
     */
    @org.junit.Test
    public void testTyoe() {
        Map<String, String> map = ParseUtils.getType();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            System.out.println("当前查询:" + next.getKey() + " " + next.getValue());
            Object[] detail = MovieUtils.getTypeDetail(next.getKey(), next.getValue());
            if (detail == null) {
                System.out.println("当前分类不能查询");
            } else {
                if (detail[0] != null) {
                    System.out.println(detail[0]);
                } else if (detail[1] != null) {
                    System.out.println(detail[1]);
                }
                System.out.println(detail[2]);
            }
        }
    }

    /**
     * 详细数据获取
     */
    @org.junit.Test
    public void testDetail() {
        MovieDetail detail = MovieUtils.detail("/html/gndy/dyzz/20170802/54643.html");
        System.out.println(detail);

    }


    /**
     * 主页数据获取
     */
    @org.junit.Test
    public void test1() {
        Movie info = MovieUtils.getIndexTypeAndInfo();
        System.out.println(info);
        Map<MovieType, ArrayList<MovieTop>> map = info.getMap();
        Iterator<Map.Entry<MovieType, ArrayList<MovieTop>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<MovieType, ArrayList<MovieTop>> next = iterator.next();
            System.out.println("[" + next.getKey().getTitle() + "]");
            ArrayList<MovieTop> list = next.getValue();
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i).getTitle());
            }
        }
    }

    /**
     * 最新发布170部影片
     */
    @org.junit.Test
    public void test2() {
        int i = 2;
        if (i == 0) {
            Movie movie = MovieUtils.getIndexTypeAndInfo();
            Iterator<Map.Entry<MovieType, ArrayList<MovieTop>>> iterator = movie.getMap().entrySet().iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next().getKey().getTitle());
            }
        } else if (i == 1) {
            Movie movie = MovieUtils.getIndexOthers();
            Iterator<Map.Entry<MovieType, ArrayList<MovieTop>>> iterator = movie.getMap().entrySet().iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next().getKey().getTitle());
            }
        } else {
            Movie movie = MovieUtils.getIndexLasted();
            Iterator<Map.Entry<MovieType, ArrayList<MovieTop>>> iterator = movie.getMap().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<MovieType, ArrayList<MovieTop>> next = iterator.next();
                System.out.println(next.getKey().getTitle());
                ArrayList<MovieTop> list = next.getValue();
                for (int j = 0; j < list.size(); j++) {
                    System.out.println(list.get(j).getTitle());
                }
            }
        }
    }


    /**
     * 主页大分类获取
     */
    @org.junit.Test
    public void jsoupTest() {
        String content = NetworkUtils.get("http://www.dytt8.net/");
        Document document = Jsoup.parse(content);
        Elements elements = document.select("table > tbody > tr");
        System.out.println(elements.size());
        //临时存放视频类型
        LinkedList<MovieType> movieTypes = new LinkedList<MovieType>();
        ArrayList<MovieTop> movieTops = new ArrayList<MovieTop>();
        Movie movie = new Movie();
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
                System.out.println(movieTypes);
            }
        }
    }
}
