package org.example.teachercommon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collections;

@SpringBootApplication
public class TeacherCommonApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TeacherCommonApplication.class);

		// 设置要加载的配置文件名（不带后缀）
		app.setDefaultProperties(Collections
				.singletonMap("spring.config.name", "common_application"));

		ConfigurableApplicationContext context = app.run(args);        // 添加以下代码防止退出
		new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}