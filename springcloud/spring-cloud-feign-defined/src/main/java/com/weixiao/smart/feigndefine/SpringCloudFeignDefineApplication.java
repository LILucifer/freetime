package com.weixiao.smart.feigndefine;

import com.weixiao.smart.feigndefine.configure.FeignRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(FeignRegister.class)
public class SpringCloudFeignDefineApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudFeignDefineApplication.class, args);
	}

}
