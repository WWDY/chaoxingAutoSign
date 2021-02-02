package com.daiju.cloudsign.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @Author WDY
 * @Date 2021-01-06 0:29
 * @Description TODO
 */
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MySqlInjector mySqlInjector(){
        return new MySqlInjector();
    }

}
