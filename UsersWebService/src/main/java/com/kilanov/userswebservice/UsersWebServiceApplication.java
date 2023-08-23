package com.kilanov.userswebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UsersWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersWebServiceApplication.class, args);
    }

}
