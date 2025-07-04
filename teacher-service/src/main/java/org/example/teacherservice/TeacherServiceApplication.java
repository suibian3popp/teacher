package org.example.teacherservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(org.example.teachercommon.config.JwtConfig.class) // 显式导入配置
@ComponentScan(basePackages = {
        "org.example.teachercommon",  // 显式添加 common 模块的包
        "org.example.teacherservice"
})
@EnableDiscoveryClient // 关键注解
public class TeacherServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherServiceApplication.class, args);
    }

}
