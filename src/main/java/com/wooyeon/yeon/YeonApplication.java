package com.wooyeon.yeon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class YeonApplication {
    public static void main(String[] args) {
        SpringApplication.run(YeonApplication.class, args);
    }

}
@RestController
class HelloController {
    @GetMapping("/")
    String hello() {
        return "hello_HEESOO_commit_push";
    }
}
