package com.leanstacks.hellojwt.web.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LandingController {

    @RequestMapping("/api")
    public String hello() {
        return "hello world";
    }

}
