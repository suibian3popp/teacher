package org.example.teachergateway.repository;

import com.alibaba.cloud.nacos.NacosConfigManager;
import org.slf4j.Logger;          // 使用 SLF4J
import org.slf4j.LoggerFactory;   // 使用 SLF4J
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.StringReader;
import java.util.Properties;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;

public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {

    private final NacosConfigManager nacosConfigManager;
    private final String dataId = "teacher-gateway.properties";
    private final String group = "DEFAULT_GROUP";
    // 声明 Logger
    private static final Logger log = LoggerFactory.getLogger(NacosRouteDefinitionRepository.class);

    public NacosRouteDefinitionRepository(NacosConfigManager nacosConfigManager) {
        this.nacosConfigManager = nacosConfigManager;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {

        try {
            // 1. 从Nacos获取配置
            String configContent = nacosConfigManager.getConfigService()
                    .getConfig(dataId, group, 5000);

            if (configContent == null || configContent.isEmpty()) {
                log.warn("No route configuration found in Nacos for dataId: {}, group: {}", dataId, group);
                return Flux.empty();
            }

            // 2. 转换为Properties对象
            Properties props = new Properties();
            props.load(new StringReader(configContent));

            // 3. 使用Spring的Binder绑定到RouteDefinition列表
            StandardEnvironment env = new StandardEnvironment();
            env.getPropertySources().addFirst(new PropertiesPropertySource("nacos-routes", props));

            return Binder.get(env)
                    .bind("routes", Bindable.listOf(RouteDefinition.class))
                    .map(Flux::fromIterable)
                    .orElse(Flux.empty());
        } catch (Exception e) {
            return Flux.error(new RuntimeException("加载路由配置失败", e));
        }

    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return Mono.empty(); // 按需实现
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return Mono.empty(); // 按需实现
    }
}
