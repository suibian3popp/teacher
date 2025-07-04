package org.example.teachergateway.repository;

import com.alibaba.cloud.nacos.NacosConfigManager;
import org.slf4j.Logger;          // 使用 SLF4J
import org.slf4j.LoggerFactory;   // 使用 SLF4J
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.handler.IgnoreTopLevelConverterNotFoundBindHandler;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;
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

//    @Override
//    public Flux<RouteDefinition> getRouteDefinitions() {
//
//        try {
//            // 1. 获取原始配置
//            String configContent = nacosConfigManager.getConfigService()
//                    .getConfig(dataId, group, 5000);
//            log.info("=== 从Nacos获取的原始配置 ===\n{}", configContent); // 打印原始内容
//
//            if (configContent == null || configContent.isEmpty()) {
//                log.warn("配置内容为空，请检查Nacos");
//                return Flux.empty();
//            }
//            // 2. 转换为Properties对象
//            Properties props = new Properties();
//            props.load(new StringReader(configContent));
//
//            // 3. 使用Spring的Binder绑定到RouteDefinition列表
//            StandardEnvironment env = new StandardEnvironment();
//            env.getPropertySources().addFirst(new PropertiesPropertySource("nacos-routes", props));
//
//            // 关键修改：绑定键名与配置前缀一致
//            return Binder.get(env)
//                    .bind("spring.cloud.gateway.routes", Bindable.listOf(RouteDefinition.class))
//                    .map(Flux::fromIterable)
//                    .orElse(Flux.empty());
//        } catch (Exception e) {
//            return Flux.error(new RuntimeException("加载路由配置失败", e));
//        }
//
//    }
@Override
public Flux<RouteDefinition> getRouteDefinitions() {
    try {
        String configContent = nacosConfigManager.getConfigService()
                .getConfig(dataId, group, 5000);
        log.info("=== Raw Config ===\n{}", configContent);

        // 转换为Properties并打印调试信息
        Properties props = new Properties();
        props.load(new StringReader(configContent));
        props.forEach((k,v) -> log.debug("Property: {} = {}", k, v)); // 打印所有属性

        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new PropertiesPropertySource("nacos-routes", props));

        // 关键修改：使用宽松绑定模式
        Binder binder = Binder.get(env);
        BindHandler handler = new IgnoreTopLevelConverterNotFoundBindHandler();

        List<RouteDefinition> routes = binder.bind(
                "spring.cloud.gateway.routes",
                Bindable.listOf(RouteDefinition.class),
                handler
        ).orElse(Collections.emptyList());

        log.info("=== Parsed Routes ===\n{}", routes);
        return Flux.fromIterable(routes);
    } catch (Exception e) {
        log.error("Failed to bind routes", e);
        return Flux.error(e);
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
