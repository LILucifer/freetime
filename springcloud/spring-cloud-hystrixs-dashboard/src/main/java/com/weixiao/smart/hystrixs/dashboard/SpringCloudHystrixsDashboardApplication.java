package com.weixiao.smart.hystrixs.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class SpringCloudHystrixsDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudHystrixsDashboardApplication.class, args);
	}

}
