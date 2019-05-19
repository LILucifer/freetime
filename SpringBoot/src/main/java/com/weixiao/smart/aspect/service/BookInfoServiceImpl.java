package com.weixiao.smart.aspect.service;

import com.weixiao.smart.aspect.entity.BookInfo;
import org.springframework.stereotype.Service;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-04-20 12:49.
 */
@Service
public class BookInfoServiceImpl implements IBookInfoService {
    @Override
    public BookInfo getBookInfoDetail(String bookNo) {
        return BookInfo.builder()
                .bookName("this is new book  at " + System.currentTimeMillis())
                .bookNo(bookNo).price(56.44)
                .stock(50)
                .build();
    }
}
