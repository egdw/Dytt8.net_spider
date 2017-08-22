package com.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

/**
 * Created by hdy on 17-8-21.
 * 网络请求类
 */
public class NetworkUtils {
    //设置重新连接次数三次
    public static int reConnectionTimes = 3;
    //设置超时时间
    public static int timeOut = 10000;

    public static java.lang.String get(java.lang.String url) {
        String request = request(url);
        return request;
    }

    private static String request(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        get.setHeader("Referer", "http://dytt8.net/");
        get.setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeOut).setConnectTimeout(timeOut).build();//设置请求和传输超时时间
        get.setConfig(requestConfig);
        boolean flag = true;
        int times = 0;
        while (flag) {
            try {
                CloseableHttpResponse response = httpclient.execute(get);
                HttpEntity entity = response.getEntity();
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "gb2312"));
                char[] c = new char[1024];
                int len = -1;
                StringBuilder sb = new StringBuilder();
                while ((len = reader.read(c)) != -1) {
                    sb.append(new java.lang.String(c, 0, len));
                }
                flag = false;
                return sb.toString();
            } catch (IOException e) {
                if (times <= reConnectionTimes) {
                    times++;
                } else {
                    flag = false;
                    return null;
                }
            } catch (java.lang.IllegalStateException e) {
                if (times <= reConnectionTimes) {
                    times++;
                } else {
                    flag = false;
                    return null;
                }
            } finally {
                try {
                    get.releaseConnection();
                    httpclient.close();
                } catch (IOException e) {
                    flag = false;
                    return null;
                }
            }
        }
        return null;
    }
}
