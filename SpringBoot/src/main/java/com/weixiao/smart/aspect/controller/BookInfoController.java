package com.weixiao.smart.aspect.controller;

import com.weixiao.smart.aspect.entity.BookInfo;
import com.weixiao.smart.aspect.service.IBookInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-04-20 09:46.
 */
@Slf4j
@RestController
@RequestMapping("/bookInfo")
public class BookInfoController {
    @Autowired
    private IBookInfoService bookInfoService;
    @RequestMapping("/bookDetail")
    public BookInfo getBookInfoDetail(@RequestParam("bookNo") String bookNo) {
        log.info("this is the getBookInfoDetail method");
        return bookInfoService.getBookInfoDetail(bookNo) ;
    }
}
