package com.daiju.cloudsign;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 *@Author WDY
 *@Date 2021-01-20 15:22
 */
@SpringBootApplication
@MapperScan("com.daiju.cloudsign.mapper")
@EnableCaching
public class CloudSignApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudSignApplication.class, args);
    }

}
