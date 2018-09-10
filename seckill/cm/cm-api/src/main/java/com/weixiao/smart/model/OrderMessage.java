package com.weixiao.smart.model;

import org.apache.commons.beanutils.BeanMap;

import javax.jms.Message;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lishixiang0925@126.com.
 * @description (mq消息实体类)
 * @Created 2018-08-30 21:02.
 */
public class OrderMessage  implements Serializable{

    private static final long serialVersionUID = -6535611332855117313L;

    /*商品ID*/
    private String commodityId;
    /*用户id*/
    private String userId;
    /*数量*/
    private int count ;

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "OrderMessage{" +
                "commodityId='" + commodityId + '\'' +
                ", userId='" + userId + '\'' +
                ", count=" + count +
                '}';
    }

    public Map<String ,Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("commodityId", this.commodityId);
        map.put("userId", this.userId);
        map.put("count", this.count);
        return map;
    }

}
