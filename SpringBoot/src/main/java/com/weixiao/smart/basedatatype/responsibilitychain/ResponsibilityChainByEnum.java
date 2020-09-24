package com.weixiao.smart.basedatatype.responsibilitychain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-09-22 22:15.
 */
@Slf4j
public class ResponsibilityChainByEnum {


    public static void main(String[] args) {
        for (Mail mail : Mail.generator(10)) {
            handler(mail);

        }
    }

    private static void handler(Mail mail) {
        for (MailHandler mailHandler : MailHandler.values()) {
            mailHandler.handler(mail);
        }
        log.info("mail {} is dead later!",mail);
    }

    enum MailHandler{

    }

    public stat

}
