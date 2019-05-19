package com.weixiao.smart.aspect.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-04-20 09:45.
 */
@Data
@Builder
public class BookInfo {
    private String bookName;
    private String bookNo;
    private double price;
    private int stock;


}
