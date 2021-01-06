package command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.web.client.RestTemplate;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2021-01-07 07:30.
 */
public class CustomerHystrixCommand extends HystrixCommand<Object> {
    protected CustomerHystrixCommand(RestTemplate restTemplate) {

        super(Setter.withGroupKey());
    }

    @Override
    protected Object run() throws Exception {
        return null;
    }

    @Override
    protected Object getFallback() {
        //核心方法，降级之后会来执行到此
        return "服务降级了";
    }
}

