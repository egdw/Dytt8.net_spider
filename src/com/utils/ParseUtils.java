package com.utils;

import com.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by hdy on 2017/8/21.
 * 解析界面封装
 * 由于该网站只有三种类型的界面
 * <p>
 * 第一种:主页类似界面
 * <p>
 * 第二种:搜索类似界面
 * <p>
 * 第三种:详细文章界面
 * <p>
 * 所有解析能够进行相应的整合
 */
public class ParseUtils {

    /**
     * 获取分类数据
     * <p>
     * 0 代表首页排版
     * <p>
     * 1 代表搜索排版
     * <p>
     * 2 代表详细排版
     *
     * @return
     */
    public static Map<String, String> getType() {
        Map<String, String> urls = new HashMap<String, String>();
        System.out.println();
        Elements types = Jsoup.parse(NetworkUtils.get("http://www.dytt8.net/")).select("div#menu > div.contain > ul > li > a");
        for (int i = 0; i < types.size(); i++) {
            String href = types.get(i).attr("href");
            if (!href.contains("http")) {
                if ("/".equals(href.substring(0, 1))) {
                    //判断是否已斜杠开头
                    href = "http://dytt8.net" + href;
                } else {
                    href = "http://dytt8.net/" + href;
                }
            }
            String title = types.get(i).text();
            urls.put(title, href);
        }
        return urls;
    }

    /**
     * 主页内容解析
     *
     * @return
     */
    public static Movie parseIndex(Document document) {
        Movie movie = null;
        {
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


    /**
     * 主页界面最新数据解析
     */
    public static Movie parseIndexLasted(Document document) {
        Elements elements = document.select("div.co_area2 > div.co_content2 > ul");
        Movie movie = new Movie();
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
     * 搜索界面解析
     */
    public static ArrayList<MovieSearchDetail> parseSearch(Document document) {
        ArrayList<MovieSearchDetail> details = new ArrayList<MovieSearchDetail>();
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
                String title = tr1.select("td[width$=55%] > b > a").get(0).text().replaceAll("<font color=\"red\">", "").replaceAll("</font>", "");
                //第二个获取描述
                String description = tr2.getElementsByTag("td").get(0).text().replaceAll("<font color=\"red\">", "").replaceAll("</font>", "");
                MovieSearchDetail detail = new MovieSearchDetail(title, href, description);
                details.add(detail);
            } else {
                continue;
            }
        }
        return details;
    }

    /**
     * 分类界面解析
     */
    public static ArrayList<MovieSearchDetail> parseTypeSearch(Document document) {
        ArrayList<MovieSearchDetail> details = new ArrayList<MovieSearchDetail>();
        Elements elements = document.select("div.co_area2 > div.co_content8 > ul > table[width$=100%] > tbody");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            Elements trs = element.getElementsByTag("tr");
            if (trs.size() >= 3) {
                //等于三个才是正常
                Element tr1 = trs.get(1);
                Element tr2 = trs.get(3);
                //第一个获取标题和地址
                String title = "";
                Elements as = tr1.select("td[height$=26] > b > a");

                String href = "";
                if (as.size() >= 2) {
                    href = as.get(1).attr("href");
                    //说明链接地址在第二行
                } else {
                    //链接地址在第一行
                    href = as.get(0).attr("href");
                }

                for (int j = 0; j < as.size(); j++) {
                    Element element1 = as.get(j);
                    title = title + element1.text().replaceAll("<font color=\"red\">", "").replaceAll("</font>", "");
                }                //第二个获取描述
                String description = tr2.getElementsByTag("td").get(0).text().replaceAll("<font color=\"red\">", "").replaceAll("</font>", "");
                MovieSearchDetail detail = new MovieSearchDetail(title, href, description);
                details.add(detail);
            } else {
                continue;
            }
        }
        return details;
    }

    /**
     * 详细界面解析
     */
    public static MovieDetail parseDetail(Document document) {
        Elements elements = document.select("div.co_area2 > div.co_content8 > ul");
        if (elements.size() == 1) {
            Element element = elements.get(0);
            int timeIndex = element.text().indexOf("发布时间：");
            //获取发布时间
            String time = element.text().substring(timeIndex + 5, timeIndex + 15);
            //获取标题
            String title = element.parent().parent().select("div.title_all > h1 > font").get(0).text();
            //获取html数据
            String description = document.getElementById("Zoom").html().substring(0, document.getElementById("Zoom").html().indexOf("<center>"));
            String downloadUrl = document.getElementById("Zoom").select("table > tbody > tr > td > a").attr("href");
            System.out.println(downloadUrl);
            MovieDetail detail = new MovieDetail(title, description, downloadUrl, time);
            return detail;
        }
        return null;
    }
}
