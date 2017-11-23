package com.yardsale.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class Launcher {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "yardsale.user_service";
    }

    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }
}
