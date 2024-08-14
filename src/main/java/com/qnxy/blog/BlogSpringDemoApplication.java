package com.qnxy.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qnxy.blog.mapper")
public class BlogSpringDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogSpringDemoApplication.class, args);
    }

}
