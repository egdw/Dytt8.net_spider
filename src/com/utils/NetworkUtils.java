package com.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

/**
 * Created by hdy on 17-8-21.
 */
public class NetworkUtils {

    public static java.lang.String get(java.lang.String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        System.out.println("请求的地址为：" + get.getURI());
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
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            get.releaseConnection();
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static java.lang.String read(java.lang.String path) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            char[] chars = new char[1024];
            int len = -1;
            StringBuilder sb = new StringBuilder();
            while ((len = reader.read(chars)) != -1) {
                sb.append(new String(chars, 0, len));
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
