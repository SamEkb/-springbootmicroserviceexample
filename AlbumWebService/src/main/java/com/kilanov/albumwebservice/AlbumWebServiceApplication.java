package com.kilanov.albumwebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AlbumWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlbumWebServiceApplication.class, args);
    }

}
