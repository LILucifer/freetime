package com.weixiao.smart.springcloudbus;

import com.weixiao.smart.springcloudbus.event.CustomerApplicationEvent;
import com.weixiao.smart.springcloudbus.stream.StreamClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableBinding(StreamClient.class)
public class SpringCloudBusApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext =
				SpringApplication.run(SpringCloudBusApplication.class, args);


		configurableApplicationContext.publishEvent(new CustomerApplicationEvent("this is CustomerApplicationEvent!!"));

//		 AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
//
//        //注册到我们的spring上下文中
//        applicationContext.register(EventConfiguration.class);
//        //启动上下文
//        applicationContext.refresh();
//
//        ApplicationEventPublisher publisher = applicationContext;
//
//        publisher.publishEvent(new CustomerApplicationEvent("开始学习springcloud -bus事件"));

	}


	@Configuration
	public class EventConfiguration {
		@EventListener
		public void onEvent(CustomerApplicationEvent event) {
			System.out.println("监听到事件 "+event);
		}
	}


}
