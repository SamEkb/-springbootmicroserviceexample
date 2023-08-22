package com.kilanov.photoappapiusers.ui.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {
    private Environment environment;

    public UsersController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/status/check")
    public String status() {
        return "Working on " + environment.getProperty("local.server.port");
    }
}
