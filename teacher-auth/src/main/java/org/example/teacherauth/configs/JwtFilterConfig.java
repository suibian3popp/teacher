package org.example.teacherauth.configs;

import org.example.teacherauth.utils.JwtAuthFilter;
import org.example.teachercommon.utils.JwtTokenProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class JwtFilterConfig {
//    @Bean
//    public JwtAuthFilter jwtAuthFilter(JwtTokenProvider jwtTokenProvider) {
//        return new JwtAuthFilter(jwtTokenProvider);
//    }

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtFilter(JwtTokenProvider jwtTokenProvider) {
        FilterRegistrationBean<JwtAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthFilter(jwtTokenProvider));
        registrationBean.addUrlPatterns("/auth/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 确保过滤器最先执行
        return registrationBean;
    }
}

