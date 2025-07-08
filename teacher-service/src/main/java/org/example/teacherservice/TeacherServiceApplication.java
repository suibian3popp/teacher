package org.example.teacherservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(org.example.teachercommon.config.JwtConfig.class) // 显式导入配置
@ComponentScan(basePackages = {
        "org.example.teachercommon",  // 显式添加 common 模块的包
        "org.example.teacherservice",
        "org.example.teachercommon.utils",
        "course" // 添加 course 包到扫描路径
})
@EnableDiscoveryClient // 关键注解
@MapperScan({"org.example.teacherservice.mapper", "course.mapper"}) // 合并 Mapper 扫描
public class TeacherServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherServiceApplication.class, args);
    }

}
