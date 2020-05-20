package com.integrate.demo.main.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.integrate.demo.main.client.ReqResClient;
import com.integrate.demo.main.model.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/*
@author : 'Vipul Popli'
*/
@RestController
public class ReqResController {

    private final ReqResClient reqResClient;

    public ReqResController(ReqResClient reqResClient) {
        this.reqResClient = reqResClient;
    }

    @GetMapping("/details")
    public Example getRes() throws InterruptedException, ExecutionException, TimeoutException, JsonProcessingException {
        return reqResClient.getRes();
    }
}
