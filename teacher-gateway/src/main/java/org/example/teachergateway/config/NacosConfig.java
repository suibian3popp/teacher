//package org.example.teachergateway.config;
//
//
//import com.alibaba.cloud.nacos.NacosConfigManager;
//import com.alibaba.cloud.nacos.NacosConfigProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//
//@Configuration
//public class NacosConfig {
//
//    @Bean
//    public NacosConfigProperties nacosConfigProperties() {
//        NacosConfigProperties properties = new NacosConfigProperties();
//        properties.setServerAddr("127.0.0.1:8848");
//        properties.setGroup("DEFAULT_GROUP");
//        properties.setTimeout(3000);
//        properties.setNamespace("");
//        return properties;
//    }
//    @Bean
//    public NacosConfigManager nacosConfigManager(NacosConfigProperties nacosConfigProperties) {
//        return new NacosConfigManager(nacosConfigProperties);
//    }
//}