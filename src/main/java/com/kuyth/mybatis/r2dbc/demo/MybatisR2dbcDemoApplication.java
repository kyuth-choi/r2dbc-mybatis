package com.kuyth.mybatis.r2dbc.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@SpringBootApplication
@ConfigurationPropertiesScan

public class MybatisR2dbcDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisR2dbcDemoApplication.class, args);
    }

}
