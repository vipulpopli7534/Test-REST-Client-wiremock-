package com.integrate.demo.main.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrate.demo.main.model.Example;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/*
@author : 'Vipul Popli'
*/
public class ReqResClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private String url;

    public ReqResClient(ObjectMapper objectMapper, String url) {
        this.objectMapper = objectMapper;
        this.httpClient = createClient();
        this.url = url;
    }


    public HttpClient createClient() {
        HttpClient httpClient = new HttpClient();
        httpClient.setMaxConnectionsPerDestination(500);
        httpClient.setIdleTimeout(60000);
        httpClient.setMaxRequestsQueuedPerDestination(100);
        httpClient.setFollowRedirects(false);
        httpClient.setConnectTimeout(5000);
        try {
            httpClient.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return httpClient;
    }

    public Example getRes() throws InterruptedException, ExecutionException, TimeoutException, JsonProcessingException {
        ContentResponse get = httpClient.GET(url);
        String contentAsString = get.getContentAsString();
        return objectMapper.readValue(contentAsString, Example.class);
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}
