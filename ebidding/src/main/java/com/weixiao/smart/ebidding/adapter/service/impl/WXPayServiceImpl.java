package com.weixiao.smart.ebidding.adapter.service.impl;

import com.weixiao.smart.ebidding.adapter.service.PayService;
import org.springframework.stereotype.Service;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2019/3/11 10:41
 */
@Service("wxPayService")
public class WXPayServiceImpl implements PayService {
    @Override
    public String pay() {
        return "this is pay method wx";
    }

    @Override
    public String getBankStatement() {
        return "return data of bank statement from wx";
    }
}
