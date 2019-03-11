package com.weixiao.smart.ebidding.adapter.service.impl;

import com.weixiao.smart.ebidding.adapter.service.PayService;
import org.springframework.stereotype.Service;

/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2019/3/11 10:39
 */
@Service("zxPayService")
public class ZXPayServiceImpl implements PayService {
    @Override
    public String pay() {
        return "this is a  pay method  zx ";
    }

    @Override
    public String getBankStatement() {
        return "return data  of bank statement from zx  ";
    }
}
