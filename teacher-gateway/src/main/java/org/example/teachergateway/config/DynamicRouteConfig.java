package org.example.teachergateway.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import org.example.teachergateway.repository.NacosRouteDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class DynamicRouteConfig {

    @Autowired
    private NacosConfigManager nacosConfigManager;

    @Bean
    public NacosRouteDefinitionRepository nacosRouteDefinitionRepository() {
        return new NacosRouteDefinitionRepository(nacosConfigManager);
    }

    @Bean
    public ApplicationListener<RefreshRoutesEvent> routesRefreshListener() {
        return event -> {
            System.out.println("Routes refreshed at: " + new Date());
        };
    }
}
