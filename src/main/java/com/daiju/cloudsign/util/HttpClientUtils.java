package com.daiju.cloudsign.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Author WDY
 * @Date 2021-01-05 8:42
 * @Description TODO
 */
@Component
public class HttpClientUtils {

    public CloseableHttpClient closeableHttpClientWithCookie(){
        BasicCookieStore cookieStore = new BasicCookieStore();
        return HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    }

    public CloseableHttpResponse post(HttpPost request, Map<String,String> headers) throws IOException {
        CloseableHttpClient httpClient = closeableHttpClientWithCookie();
        headers.forEach(request::setHeader);
        return httpClient.execute(request);
    }

    public CloseableHttpResponse post(HttpPost request) throws IOException {
        CloseableHttpClient httpClient = closeableHttpClientWithCookie();
        return httpClient.execute(request);
    }

    public CloseableHttpResponse post(HttpPost request, Map<String,String> headers, Map<String,Object> params) throws Exception {
        CloseableHttpClient httpClient = closeableHttpClientWithCookie();
        headers.forEach(request::setHeader);
        JSONObject jsonBody = new JSONObject(params);
        StringEntity stringEntity = new StringEntity(jsonBody.toJSONString(),"UTF-8");
        stringEntity.setContentType("application/json");
        stringEntity.setContentEncoding("UTF-8");
        request.setEntity(stringEntity);
        return httpClient.execute(request);
    }

    public CloseableHttpResponse post(HttpUriRequest request, Map<String,Object> params) throws Exception {
        CloseableHttpClient httpClient = closeableHttpClientWithCookie();
        JSONObject jsonBody = new JSONObject(params);
        StringEntity stringEntity = new StringEntity(jsonBody.toJSONString(),"UTF-8");
        stringEntity.setContentType("application/json");
        stringEntity.setContentEncoding("UTF-8");
        ((HttpPost)request).setEntity(stringEntity);
        return httpClient.execute(request);
    }

    public CloseableHttpResponse get(HttpGet request,Map<String,String> headers) throws Exception {
        CloseableHttpClient httpClient = closeableHttpClientWithCookie();
        headers.forEach(request::setHeader);
        return httpClient.execute(request);
    }


}
