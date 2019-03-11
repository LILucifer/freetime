package com.weixiao.smart.ebidding.adapter;

import com.weixiao.smart.ebidding.adapter.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2019/3/11 10:47
 */
@Component
public class BeanAdapter {
    @Autowired
    @Qualifier("zxPayService")
    private PayService zxPayService;
    @Autowired
    @Qualifier("wxPayService")
    private PayService wxPayService;

    public PayService beanAdapter(String payType) {
        PayService payService = null;
        switch (payType) {
            case "WX":
                payService = wxPayService;
                break;
            case "ZX":
                payService = zxPayService;
                break;
            default:
                throw new RuntimeException("请选择正确的支付接口！");
        }
        return payService;
    }
}
