package com.gzj.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
@Slf4j
public class SpringBootDemo {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemo.class,args);
        log.info("======后台启动成功======");
    }
}
