package com.integrate.demo.main;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.integrate.demo.main.client.ReqResClient;
import com.integrate.demo.main.model.Datum;
import com.integrate.demo.main.model.Example;
import org.junit.jupiter.api.*;
import org.mockito.internal.util.io.IOUtil;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/*
@author : 'Vipul Popli'
*/
public class ReqResClientTest {
    private final ReqResClient reqResClient;
    private final WireMockServer wireMockServer;

    public ReqResClientTest() {
        this.reqResClient = new ReqResClient(getObjectMapper(), "http://localhost:7080/api/v1/employees");
        wireMockServer = new WireMockServer(7080);
    }

    @BeforeEach
    void setup() {
        ResponseDefinitionBuilder mockRes = new ResponseDefinitionBuilder();
        InputStream resourceAsStream = ReqResClientTest.class.getResourceAsStream("/res.json");
        String string = IOUtil.readLines(resourceAsStream).stream().collect(Collectors.joining(""));
        mockRes.withBody(string);
        wireMockServer.start();
        WireMock.configureFor("localhost", 7080);
        WireMock.stubFor(WireMock.get("/api/v1/employees").willReturn(mockRes)); /*use get or post*/
    }

    @Test
    void test() throws InterruptedException, ExecutionException, TimeoutException, JsonProcessingException {
        Example res = reqResClient.getRes();
        List<Datum> data = res.getData();
        res.getData();
    }

    @Test
    void test2() throws InterruptedException, ExecutionException, TimeoutException, JsonProcessingException {
        Example res = reqResClient.getRes();
        List<Datum> data = res.getData();
        res.getData();
    }

    @AfterEach
    void tearDown() {
        if (Objects.nonNull(wireMockServer) && wireMockServer.isRunning()) {
            wireMockServer.shutdownServer();
        }
    }


    ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.setVisibility(mapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .getSerializationConfig()
                .getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.ANY));
        return mapper;
    }
}
