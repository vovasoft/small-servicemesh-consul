package com.vova.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author WangYang - vova
 * @version Create in 11:04 2024/10/22
 */


@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Consul-based Service!";
    }


    @Autowired
    private RestTemplate restTemplate;

    // @GetMapping("/call-golang")
    // public String callGolang() {
    //     // 从 Consul 中获取 golang-service 的地址
    //     String golangServiceUrl = "http://localhost:9090/hello";
    //     return restTemplate.getForObject(golangServiceUrl, String.class);
    // }


    @GetMapping("/call-golang-consul")
    public String callGolangForConsul() {
        // 假设 golang-service 运行在 Consul 并注册为 golang-service
        String golangServiceUrl = "http://localhost:8500/v1/catalog/service/golang-service";

        // 通过 Consul 的 API 获取 golang-service 的注册信息
        ResponseEntity<String> response = restTemplate.getForEntity(golangServiceUrl, String.class);

        // 解析 Consul 返回的数据，获取 golang-service 的 IP 和端口
        // 简单实现解析返回的服务列表，这里假设只有一个实例
        // 正常情况下你会用 JSON 解析库来解析更复杂的结果
        String serviceAddress = "http://localhost:9090/hello";

        // 通过获取到的地址，调用 golang-service 提供的接口
        String golangResponse = restTemplate.getForObject(serviceAddress, String.class);

        return "Response from Golang Service: " + golangResponse;
    }

}
