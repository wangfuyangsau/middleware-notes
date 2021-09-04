package org.example.middleware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * @author Fuyang Wang
 * @version V1.0
 * @Title: org.example.middleware.server.MainApplication
 * @Package PACKAGE_NAME
 * @date 2021/8/30
 */
@SpringBootApplication
@MapperScan("org.example.middleware.mapper")
public class MainApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return super.configure(builder);
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
