package com.daiju.cloudsign.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @Author WDY
 * @Date 2021-01-07 17:11
 * @Description TODO
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor())
                .addPathPatterns("/*")
                .excludePathPatterns("/api/user/login","/","/api/user/register");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**/*.html").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/.ico").addResourceLocations("classpath:/static/images/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/page/login-3");
        registry.addViewController("/index").setViewName("/index");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/api/**")
                .allowedHeaders("*")
                .allowedOrigins("http://localhost:8000")
                .allowedMethods("*")
                .maxAge(3600)
                .allowCredentials(true);
    }
}
