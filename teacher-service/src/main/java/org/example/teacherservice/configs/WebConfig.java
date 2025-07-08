//package org.example.teacherservice.configs;
//
//import org.example.teacherservice.util.UserInfoInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    @Autowired
//    private UserInfoInterceptor userInfoInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册拦截器并配置拦截路径
//        registry.addInterceptor(userInfoInterceptor)
//                .addPathPatterns("/**")  // 拦截所有路径
//                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**"); // 排除Swagger相关路径
//    }
//}
