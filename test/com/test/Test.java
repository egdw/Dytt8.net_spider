package com.test;

import com.model.Movie;
import com.model.MovieSearch;
import com.model.MovieTop;
import com.model.MovieType;
import com.utils.MovieUtils;
import com.utils.NetworkUtils;
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
    @org.junit.Test
    public void test3() {
        MovieSearch search = MovieUtils.search("异形");
        search = search.nextPage();
        System.out.println(search);
    }


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

    @org.junit.Test
    public void test2() {
//        Scanner scanner = new Scanner(System.in);
//        int i = scanner.nextInt();
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

    @org.junit.Test
    public void jsoupTest() {
//        String content = NetworkUtils.get("http://www.dytt8.net/");
        String content = NetworkUtils.read("/home/hdy/Desktop/index.html");
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
                    System.out.println("第一次执行");
                    MovieType movieType = new MovieType(type_href, type_content);
                    movieTypes.add(movieType);
                } else {
                    MovieType last = movieTypes.getLast();
                    if (type_content.equals(last.getTitle())) {
                        //说明是一样的
                        System.out.println("一样");
                        movieTops.add(new MovieTop(movie_href, movie_title, time));
                        if (times == elements.size() - 1) {
                            //如果一样,说明是最后了
                            System.out.println("最后一个了");
                            movie.getMap().put(movieTypes.getLast(), movieTops);
                        }
                    } else {
                        //说明是不一样的
                        System.out.println("频道切换");
                        movie.getMap().put(movieTypes.getLast(), movieTops);
                        movieTops = new ArrayList<MovieTop>();
                        MovieType movieType = new MovieType(type_href, type_content);
                        movieTypes.add(movieType);
                    }
                }
                System.out.println(type_content);
            }
        }
    }
}
