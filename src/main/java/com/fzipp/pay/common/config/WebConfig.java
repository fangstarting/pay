package com.fzipp.pay.common.config;

import com.fzipp.pay.common.filters.MyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebConfig
 * @Description
 * @Author 24k
 * @Date 2021/12/26 21:23
 * @Version 1.0
 */
@Configuration
public class WebConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);

    @Autowired
    private MyInterceptor myInterceptor;

    // 设置允许跨域请求
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                LOGGER.info("配置拦截器addCorsMappings>>>");
                registry
                        .addMapping("/**")
//                        .allowedOrigins("http://yyy.com", "http://xxx.com") //
//                        .allowedOrigins("http://localhost:8088")
//                        .allowedOrigins("*") //浏览器允许所有的域访问 / 注意 * 不能满足带有cookie的访问,Origin 必须是全匹配
                        // 允许跨域的域名
                        .allowedOriginPatterns("*") // 允许所有域
                        .allowedMethods("*") // 允许任何方法（post、get等）
                        .allowedHeaders("*") // 允许任何请求头
                        .allowCredentials(true) // 允许证书、cookie
                        .exposedHeaders(HttpHeaders.SET_COOKIE)
                        .maxAge(3600L); // maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                LOGGER.info("配置拦截器addInterceptors>>>");
                //注册拦截器并配置拦截的路径
                registry
                        .addInterceptor(myInterceptor)
                        .addPathPatterns("/**")
                        .excludePathPatterns("/test/**") //测试接口放行
                        .excludePathPatterns("/captcha/**") //验证码
                        .excludePathPatterns("/load/static/resource/**") //静态资源接口放行
                        .excludePathPatterns("/account/login.action", "/account/reg.action", "/email/verify/**"); //登陆注册接口放行
            }
        };
    }

}
