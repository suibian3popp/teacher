//package org.example.teacherservice.configs;
//
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Slf4j
//@Configuration
//public class Config {
//    @Resource
//    private MinIOInfo minIOInfo;
//
//    //链式编程 构建MinioClient对象
//    //单例 无线程安全问题
//    @Bean
//    public MinioClient minioClient() {
//        return MinioClient.builder()
//                .endpoint(minIOInfo.getEndpoint())//构建器构建一个端点
//                .credentials(minIOInfo.getAccessKey(), minIOInfo.getSecretKey())//账号密码
//                .build();
//    }
//}
