package com.weixiao.smart.basedatatype.responsibilitychain;



/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-09-22 22:50.
 */
public class Mail {
    enum Readability {
        CAN_GET_READ,YES,NO1,NO2,NO3
    }

    enum Address {
        GUANGZHOU,G1,G2,G3,NO
    }

    enum Scanability {
        GOODIDEA,NO1,NO2,NO3
    }

    enum GetAddress {
        ADDRESS_RETURN,NO1,NO2
    }

    Readability readability;
    Address address;
    Scanability scanability;
    GetAddress getAddress;


    public static Mail randomMails(){
        Mail mail = new Mail();
        return Enums.random(Readability.class);
    }

    public static Iterable<Mail> generator(int count) {

    }
    private Letter randomLetter() {
        int pick = new Random().nextInt(Letter.values().length);
        return Letter.values()[pick];
    }





}
