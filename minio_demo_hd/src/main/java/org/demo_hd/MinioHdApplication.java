package org.demo_hd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = "org.demo_hd.mapper")
@SpringBootApplication
public class MinioHdApplication {

    public static void main(String[] args) {

        SpringApplication.run(MinioHdApplication.class, args);
    }

}
